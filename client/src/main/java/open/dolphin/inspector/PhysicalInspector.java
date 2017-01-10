package open.dolphin.inspector;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import open.dolphin.ui.AdditionalTableSettings;
import open.dolphin.client.Chart;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.ClientContext;
import open.dolphin.client.GUIConst;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ObservationModel;
import open.dolphin.infomodel.PhysicalModel;
import open.dolphin.project.Project;
import open.dolphin.table.IndentTableCellRenderer;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.MyJPopupMenu;
import open.dolphin.util.PNSTriple;
import org.apache.log4j.Logger;

/**
 * 身長体重インスペクタクラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class PhysicalInspector implements IInspector {
    public static final InspectorCategory CATEGORY = InspectorCategory.身長体重;

    private ObjectReflectTableModel<PhysicalModel> tableModel;
    private PhysicalView view;
    private final ChartImpl context;

    private final Logger logger;

    /**
     * PhysicalInspectorオブジェクトを生成する.
     * @param ctx
     */
    public PhysicalInspector(ChartImpl ctx) {
        context = ctx;
        logger = ClientContext.getBootLogger();
        initComponents();
    }

    /**
     * GUIコンポーネントを初期化する.
     */
    private void initComponents() {

        view = new PhysicalView();
        view.setName(CATEGORY.name());

        List<PNSTriple<String,Class<?>,String>> reflectList = Arrays.asList(
                new PNSTriple<>(" 身長", String.class, "getHeight"),
                new PNSTriple<>(" 体重", String.class, "getWeight"),
                new PNSTriple<>(" BMI", String.class, "getBmi"),
                new PNSTriple<>(" 測定日", String.class, "getIdentifiedDate")
        );

        // 身長体重テーブルを生成する
        tableModel = new ObjectReflectTableModel<>(reflectList);
        view.getTable().setModel(tableModel);
        view.getTable().setRowHeight(GUIConst.DEFAULT_TABLE_ROW_HEIGHT);
        view.getTable().setDefaultRenderer(Object.class, new IndentTableCellRenderer(IndentTableCellRenderer.NARROW));
        view.getTable().getColumnModel().getColumn(2).setCellRenderer(new BMIRenderer());
        view.getTable().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // インスペクタのサイズ調整
        view.setPreferredSize(new Dimension(DEFAULT_WIDTH, 110));

        // 列幅を調整する カット&トライ
        int[] cellWidth = new int[]{50,50,50,110};
        for (int i = 0; i < cellWidth.length; i++) {
            TableColumn column = view.getTable().getColumnModel().getColumn(i);
            column.setPreferredWidth(cellWidth[i]);
        }

        // 右クリックによる追加削除のメニューを登録する
        MouseAdapter ma = new MouseAdapter() {
            private void mabeShowPopup(MouseEvent e) {
                // isReadOnly対応
                if (context.isReadOnly()) { return; }

                MyJPopupMenu pop = new MyJPopupMenu();
                JMenuItem item = new JMenuItem("追加");
                item.setIcon(GUIConst.ICON_LIST_ADD_16);
                pop.add(item);
                item.addActionListener(ae -> PhysicalEditor.show(PhysicalInspector.this));

                final int row = view.getTable().rowAtPoint(e.getPoint());

                if (tableModel.getObject(row) != null) {
                    pop.add(new JSeparator());
                    JMenuItem item2 = new JMenuItem("削除");
                    item2.setIcon(GUIConst.ICON_LIST_REMOVE_16);
                    pop.add(item2);
                    item2.addActionListener(ae -> delete(row));
                }
                pop.show(e.getComponent(), e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getComponent() == view.getTable().getParent()) {
                    view.getTable().clearSelection();
                }
                if (e.isPopupTrigger()) {
                    mabeShowPopup(e);
                }
            }

            //@Override
            //public void mouseReleased(MouseEvent e) {
            //    mabeShowPopup(e);
            //}
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    PhysicalEditor.show(PhysicalInspector.this);
                }
            }
        };

        view.getTable().addMouseListener(ma);
        AdditionalTableSettings.setTable(view.getTable(), ma);
    }

    public Chart getContext() {
        return context;
    }

    public void clear() {
        tableModel.clear();
    }

    /**
     * レイアウトパネルを返す.
     * @return レイアウトパネル
     */
    @Override
    public JPanel getPanel() {
        return view;
    }

    @Override
    public String getName() {
        return CATEGORY.name();
    }

    @Override
    public String getTitle() {
        return CATEGORY.title();
    }

    /**
     * データ部分までスクロールする.
     * @param ascending
     */
    private void scroll(boolean ascending) {

        int cnt = tableModel.getObjectCount();
        if (cnt > 0) {
            int row = ascending? cnt - 1 : 0;
            Rectangle r = view.getTable().getCellRect(row, row, true);
            view.getTable().scrollRectToVisible(r);
        }
    }

    /**
     * 身長体重データを表示する.
     */
    @Override
    public void update() {
        List<PhysicalModel> list = context.getKarte().getPhysicalEntry();
        if (list.isEmpty()) { return; }

        boolean asc = Project.getPreferences().getBoolean(Project.DOC_HISTORY_ASCENDING, false);
        if (asc) { Collections.sort(list); }
        else { Collections.sort(list, Collections.reverseOrder()); }

        tableModel.setObjectList(list);
        scroll(asc);
    }

    /**
     * 身長体重データを追加する.
     * @param model
     */
    public void add(final PhysicalModel model) {

        // 同定日
        String confirmedStr = model.getIdentifiedDate();
        Date confirmed = ModelUtils.getDateTimeAsObject(confirmedStr + "T00:00:00");

        // 記録日
        Date recorded = new Date();

        final List<ObservationModel> addList = new ArrayList<>(2);

        if (model.getHeight() != null) {
            ObservationModel observation = new ObservationModel();
            observation.setKarte(context.getKarte());
            observation.setCreator(Project.getUserModel());
            observation.setObservation(IInfoModel.OBSERVATION_PHYSICAL_EXAM);
            observation.setPhenomenon(IInfoModel.PHENOMENON_BODY_HEIGHT);
            observation.setValue(model.getHeight());
            observation.setUnit(IInfoModel.UNIT_BODY_HEIGHT);
            observation.setConfirmed(confirmed);        // 確定（同定日）
            observation.setStarted(confirmed);          // 適合開始日
            observation.setRecorded(recorded);          // 記録日
            observation.setStatus(IInfoModel.STATUS_FINAL);
            //observation.setMemo(model.getMemo());
            addList.add(observation);
        }

        if (model.getWeight() != null) {

            ObservationModel observation = new ObservationModel();
            observation.setKarte(context.getKarte());
            observation.setCreator(Project.getUserModel());
            observation.setObservation(IInfoModel.OBSERVATION_PHYSICAL_EXAM);
            observation.setPhenomenon(IInfoModel.PHENOMENON_BODY_WEIGHT);
            observation.setValue(model.getWeight());
            observation.setUnit(IInfoModel.UNIT_BODY_WEIGHT);
            observation.setConfirmed(confirmed);        // 確定（同定日）
            observation.setStarted(confirmed);          // 適合開始日
            observation.setRecorded(recorded);          // 記録日
            observation.setStatus(IInfoModel.STATUS_FINAL);
            //observation.setMemo(model.getMemo());
            addList.add(observation);
        }

        if (addList.isEmpty()) { return ; }

        DBTask task = new DBTask<List<Long>>(context) {

            @Override
            protected List<Long> doInBackground() throws Exception {
                logger.debug("physical add doInBackground");
                DocumentDelegater pdl = new DocumentDelegater();
                List<Long> ids = pdl.addObservations(addList);
                return ids;
            }

            @Override
            protected void succeeded(List<Long> result) {
                logger.debug("physical add succeeded");
                if (model.getHeight() != null && model.getWeight() != null) {
                    model.setHeightId(result.get(0));
                    model.setWeightId(result.get(1));
                } else if (model.getHeight() != null) {
                    model.setHeightId(result.get(0));
                } else {
                    model.setWeightId(result.get(0));
                }
                boolean asc = Project.getPreferences().getBoolean(Project.DOC_HISTORY_ASCENDING, false);
                if (asc) {
                    tableModel.addRow(model);
                } else {
                    tableModel.addRow(0, model);
                }
                scroll(asc);
            }
        };

        task.execute();
    }

    /**
     * テーブルで選択した身長体重データを削除する.
     * @param row
     */
    public void delete(final int row) {
        PhysicalModel model = tableModel.getObject(row);
        if (model == null) { return; }

        final List<Long> list = new ArrayList<>(2);

        if (model.getHeight() != null) {
            list.add(model.getHeightId());
        }

        if (model.getWeight() != null) {
            list.add(model.getWeightId());
        }

        DBTask task = new DBTask<Void>(context) {

            @Override
            protected Void doInBackground() throws Exception {
                logger.debug("physical delete doInBackground");
                DocumentDelegater ddl = new DocumentDelegater();
                ddl.removeObservations(list);
                return null;
            }

            @Override
            protected void succeeded(Void result) {
                logger.debug("physical delete succeeded");
                tableModel.deleteRow(row);
            }
        };

        task.execute();
    }

    /**
     * BMI値 を表示するレンダラクラス.
     */
    private class BMIRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        public BMIRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean isFocused,
                int row, int col) {
            Component c = super.getTableCellRendererComponent(table,
                    value,
                    isSelected,
                    isFocused, row, col);

            PhysicalModel h = tableModel.getObject(row);

            Color fore = (h != null && h.getBmi() != null && h.getBmi().compareTo("25") > 0)  ? Color.RED : Color.BLACK;
            setForeground(fore);

            ((JLabel) c).setText(value == null ? "" : (String) value);

            if (h != null && h.getStandardWeight() != null) {
                setToolTipText("標準体重 = " + h.getStandardWeight());
            }

            return c;
        }
    }
}

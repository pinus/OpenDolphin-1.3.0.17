package open.dolphin.inspector;

import open.dolphin.client.Chart;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.GUIConst;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.helper.DBTask;
import open.dolphin.helper.PNSTriple;
import open.dolphin.infomodel.AllergyModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ObservationModel;
import open.dolphin.project.Project;
import open.dolphin.ui.IndentTableCellRenderer;
import open.dolphin.ui.ObjectReflectTableModel;
import open.dolphin.util.ModelUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

/**
 * AllergyInspector.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class AllergyInspector implements IInspector {
    public static final InspectorCategory CATEGORY = InspectorCategory.アレルギー;

    // TableModel
    private ObjectReflectTableModel<AllergyModel> tableModel;
    // コンテナパネル
    private AllergyView view;
    // Chart
    private final ChartImpl context;

    /**
     * AllergyInspectorオブジェクトを生成する.
     *
     * @param parent PatientInspector
     */
    public AllergyInspector(PatientInspector parent) {
        context = parent.getContext();
        initComponents();
    }

    /**
     * GUIコンポーネントを初期化する.
     */
    private void initComponents() {

        view = new AllergyView();
        view.setName(CATEGORY.name());
        view.setPreferredSize(new Dimension(DEFAULT_WIDTH, 110));

        JTable table = view.getTable();
        table.putClientProperty("Quaqua.Table.style", "striped");

        // アレルギーテーブルを設定する
        List<PNSTriple<String, Class<?>, String>> reflectList = Arrays.asList(
                new PNSTriple<>("　要 因", String.class, "getFactor"),
                new PNSTriple<>("　反応程度", String.class, "getSeverity"),
                new PNSTriple<>("　同定日", String.class, "getIdentifiedDate")
        );

        tableModel = new ObjectReflectTableModel<>(reflectList);
        table.setModel(tableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // レンダラを設定する
        table.setDefaultRenderer(Object.class,
                new IndentTableCellRenderer(IndentTableCellRenderer.NARROW, IndentTableCellRenderer.SMALL_FONT));
        // 表の高さ
        table.setRowHeight(GUIConst.DEFAULT_TABLE_ROW_HEIGHT);
        // コラム幅
        table.getColumnModel().getColumn(0).setMinWidth(120);
        table.getColumnModel().getColumn(1).setMaxWidth(3);
        table.getColumnModel().getColumn(1).setPreferredWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(5);

        // 右クリックによる追加削除のメニューを登録する
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    AllergyEditor.show(AllergyInspector.this);
                }
            }

            private void mabeShowPopup(MouseEvent e) {
                //  isReadOnly対応
                if (context.isReadOnly() || !e.isPopupTrigger()) {
                    return;
                }

                JPopupMenu pop = new JPopupMenu();
                // 追加
                JMenuItem item = new JMenuItem("追加");
                item.setIcon(GUIConst.ICON_LIST_ADD_16);
                pop.add(item);
                item.addActionListener(ae -> AllergyEditor.show(AllergyInspector.this));

                // 削除
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
                mabeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Windows
                mabeShowPopup(e);
            }
        });
    }

    public Chart getContext() {
        return context;
    }

    /**
     * レイアウトパネルを返す.
     *
     * @return JPanel
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

    public void clear() {
        tableModel.clear();
    }

    private void scroll(boolean ascending) {
        int cnt = tableModel.getObjectCount();
        if (cnt > 0) {
            int row = 0;
            if (ascending) {
                row = cnt - 1;
            }
            Rectangle r = view.getTable().getCellRect(row, row, true);
            view.getTable().scrollRectToVisible(r);
        }
    }

    /**
     * アレルギー情報を表示する.
     */
    @Override
    public void update() {
        List<AllergyModel> list = context.getKarte().getAllergyEntry();
        if (list != null && !list.isEmpty()) {
            boolean asc = Project.getPreferences().getBoolean(Project.DOC_HISTORY_ASCENDING, false);
            if (asc) {
                list.sort(Comparator.naturalOrder());
            } else {
                list.sort(Comparator.reverseOrder());
            }
            tableModel.setObjectList(list);
            scroll(asc);
        }
    }

    /**
     * アレルギーデータを追加する.
     *
     * @param model AllergyModel
     */
    public void add(final AllergyModel model) {

        // GUI の同定日をTimeStampに変更する
        Date date = ModelUtils.getDateTimeAsObject(model.getIdentifiedDate() + "T00:00:00");

        final List<ObservationModel> addList = new ArrayList<>(1);

        ObservationModel observation = new ObservationModel();
        observation.setKarte(context.getKarte());
        observation.setCreator(Project.getUserModel());
        observation.setObservation(IInfoModel.OBSERVATION_ALLERGY);
        observation.setPhenomenon(model.getFactor());
        observation.setCategoryValue(model.getSeverity());
        observation.setConfirmed(date);
        observation.setRecorded(new Date());
        observation.setStarted(date);
        observation.setStatus(IInfoModel.STATUS_FINAL);
        observation.setMemo(model.getMemo());
        addList.add(observation);

        DBTask task = new DBTask<List<Long>>(context) {

            @Override
            protected List<Long> doInBackground() {
                //logger.debug("allergy add doInBackground");
                DocumentDelegater ddl = new DocumentDelegater();
                List<Long> ids = ddl.addObservations(addList);
                return ids;
            }

            @Override
            protected void succeeded(List<Long> result) {
                //logger.debug("allergy add succeeded");
                model.setObservationId(result.get(0));
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
     * テーブルで選択したアレルギーを削除する.
     *
     * @param row 削除行
     */
    public void delete(final int row) {

        AllergyModel model = tableModel.getObject(row);

        if (model == null) {
            return;
        }

        final List<Long> list = new ArrayList<>(1);
        list.add(model.getObservationId());

        DBTask task = new DBTask<Void>(this.context) {

            @Override
            protected Void doInBackground() {
                // logger.debug("allergy delete doInBackground");
                DocumentDelegater ddl = new DocumentDelegater();
                ddl.removeObservations(list);
                return null;
            }

            @Override
            protected void succeeded(Void result) {
                //logger.debug("allergy delete succeeded");
                tableModel.deleteRow(row);
            }
        };

        task.execute();
    }
}

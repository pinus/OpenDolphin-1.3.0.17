package open.dolphin.inspector;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import open.dolphin.ui.AdditionalTableSettings;
import open.dolphin.client.Chart;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.GUIConst;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.AllergyModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ObservationModel;
import static open.dolphin.inspector.PatientInspector.DEFAULT_WIDTH;
import open.dolphin.project.Project;
import open.dolphin.table.IndentTableCellRenderer;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.MyJPopupMenu;
import open.dolphin.util.PNSTriple;

/**
 * AllergyInspector.
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class AllergyInspector implements IInspector {
    public static final String NAME = InspectorCategory.アレルギー.name();

    // TableModel
    private ObjectReflectTableModel<AllergyModel> tableModel;
    // コンテナパネル
    private AllergyView view;
    // Chart
    private final ChartImpl context;

    /**
     * AllergyInspectorオブジェクトを生成する.
     * @param context
     */
    public AllergyInspector(ChartImpl context) {
        this.context = context;
        initComponents();
    }

    /**
     * GUIコンポーネントを初期化する.
     */
    private void initComponents() {

        view = new AllergyView();
        JTable table = view.getTable();

        // アレルギーテーブルを設定する
        List<PNSTriple<String,Class<?>,String>> reflectList = Arrays.asList(
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
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    AllergyEditor.show(AllergyInspector.this);
                }
            }

            private void mabeShowPopup(MouseEvent e) {
                //  isReadOnly対応
                if (context.isReadOnly()) { return; }

                MyJPopupMenu pop = new MyJPopupMenu();
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
                // table の関係ないところをクリックしたら，selection をクリア
                if(e.getComponent() == view.getTable().getParent()) {
                    view.getTable().clearSelection();
                }
                if (e.isPopupTrigger()) {
                    mabeShowPopup(e);
                }
            }
            //@Override // windows
            //public void mouseReleased(MouseEvent e) {
            //    if (e.isPopupTrigger()){
            //        mabeShowPopup(e);
            //    }
            //}
        };

        view.getTable().addMouseListener(ma);
        AdditionalTableSettings.setTable(view.getTable(), ma);

        view.setPreferredSize(new Dimension(DEFAULT_WIDTH, 110));
    }

    public Chart getContext() {
        return context;
    }

    /**
     * レイアウトパネルを返す.
     * @return
     */
    @Override
    public JPanel getPanel() {
        return view;
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
        if (list != null && ! list.isEmpty()) {
            boolean asc = Project.getPreferences().getBoolean(Project.DOC_HISTORY_ASCENDING, false);
            if (asc) {
                Collections.sort(list);
            } else {
                Collections.sort(list, Collections.reverseOrder());
            }
            tableModel.setObjectList(list);
            scroll(asc);
        }
    }

    /**
     * アレルギーデータを追加する.
     * @param model
     */
    public void add(final AllergyModel model) {

        // GUI の同定日をTimeStampに変更する
        Date date = ModelUtils.getDateTimeAsObject(model.getIdentifiedDate()+"T00:00:00");

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
            protected List<Long> doInBackground() throws Exception {
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
     * @param row
     */
    public void delete(final int row) {

        AllergyModel model = tableModel.getObject(row);

        if (model == null) { return; }

        final List<Long> list = new ArrayList<>(1);
        list.add(model.getObservationId());

        DBTask task = new DBTask<Void>(this.context) {

            @Override
            protected Void doInBackground() throws Exception {
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

package open.dolphin.client;

import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import open.dolphin.delegater.PvtDelegater;
import open.dolphin.delegater.PatientDelegater;
import open.dolphin.infomodel.KarteState;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.project.Project;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.MyJPopupMenu;
import open.dolphin.ui.MyJSheet;

/**
 * Main Window コンポーネントプラグインの抽象クラス。
 * 具象クラスは start()、stop() を実装する。
 */
public abstract class AbstractMainComponent extends MouseAdapter implements MainComponent {

    private String name;
    private String icon;
    private MainWindow context;
    private JPanel ui;
    private int number = 10000; // pvt がない場合の受付番号 10000から連番で作る

    public AbstractMainComponent() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public MainWindow getContext() {
        return context;
    }

    @Override
    public void setContext(MainWindow context) {
        this.context = context;
    }

    @Override
    public JPanel getUI() {
        return ui;
    }

    @Override
    public void setUI(JPanel ui) {
        this.ui = ui;
    }

    @Override
    public void enter() {
    }

    @Override
    public Callable<Boolean> getStartingTask() {
        return null;
    }

    @Override
    public Callable<Boolean> getStoppingTask() {
        return null;
    }

    @Override
    public abstract void start();

    @Override
    public abstract void stop();

    // MainComponent に共通な作業
    // 必要に応じて extend 側でオーバーライドする

    /**
     * PatientModel のカルテを開く。
     * @param value 対象患者
     */
    public void openKarte(final PatientModel patient) {
        if (canOpen(patient)) {
            Thread t = new Thread() {
                @Override
                public void run() {

                    // 健康保険情報をフェッチする
                    PatientDelegater pdl = new PatientDelegater();
                    pdl.fetchHealthInsurance(patient);

                    // pvt 情報があるかどうかチェック
                    PvtDelegater pvtdl = new PvtDelegater();
                    PatientVisitModel pvtModel = pvtdl.getPvt(patient);

                    // 来院がない場合
                    if (pvtModel == null) {
                        // 来院情報を生成する
                        pvtModel = new PatientVisitModel();
                        pvtModel.setId(0L);
                        pvtModel.setNumber(getNewPvtNumber()); //10000から割り当て
                        pvtModel.setPatient(patient);

                        // 受け付けを通していないのでログイン情報及び設定ファイルを使用する
                        // 診療科名、診療科コード、医師名、医師コード、JMARI
                        pvtModel.setDepartment(constarctDept());
                        getContext().openKarte(pvtModel);

                    // 来院している場合
                    } else {
                        int state = pvtModel.getState();
                        // すでに OPEN ならどっかで開いているということなので編集不可に設定
                        if (KarteState.isOpen(state)) openReadOnlyKarte(pvtModel, state); //
                        // OPEN でなければ，通常どおりオープン
                        else getContext().openKarte(pvtModel);
                    }
                }
            };
            t.start();
        } else {
            // 既に開かれていれば，そのカルテを前に
            ChartImpl.toFront(patient);
            EditorFrame.toFront(patient);
        }
    }

    /**
     * ReadOnly でカルテを開く
     * @param pvtModel
     * @param state
     */
    public void openReadOnlyKarte(final PatientVisitModel pvtModel, final int state) {
        // 元々 ReadOnly のユーザーならそのまま開いて OK
        if (Project.isReadOnly()) {
            getContext().openKarte(pvtModel);
            return;
        }
        // ダイアログで確認する
        String ptName = pvtModel.getPatientName();
        String message = "<html>" +
            "<h3>"+ ptName + " 様のカルテは他の端末で編集中です</h3>" +
            "<p><nobr>閲覧のみで、編集はできません<nobr></p></html>";

        MyJSheet.showConfirmSheet(SwingUtilities.getWindowAncestor(getUI()), message,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, new SheetListener() {
            @Override
            public void optionSelected(SheetEvent se) {
                int result = se.getOption();
                if (result == 0) { // OK
                    pvtModel.setState(KarteState.READ_ONLY);
                    getContext().openKarte(pvtModel);
                }
            }
        });
    }

    /**
     * カルテを開くことが可能かどうかを返す。
     * @return 開くことが可能な時 true
     */
    public boolean canOpen(PatientModel patient) {
        return !ChartImpl.isKarteOpened(patient);
    }

    public String constarctDept() {
        StringBuilder sb = new StringBuilder();
        sb.append(Project.getUserModel().getDepartmentModel().getDepartmentDesc());
        sb.append(",");
        sb.append(Project.getUserModel().getDepartmentModel().getDepartment());
        sb.append(",");
        sb.append(Project.getUserModel().getCommonName());
        sb.append(",");
        sb.append(Project.getUserModel().getUserId());
        sb.append(",");
        sb.append(Project.getJMARICode());
        String ret = sb.toString();
        return ret;
    }

    /**
     * ニセの受付番号を連番で作成
     * @return
     */
    public int getNewPvtNumber() {
        return number++;
    }

    /**
     * MainComponent (WatingList, PatientSearch, LaboImporter) で使われる ContextListener
     * @param <T>
     */
    public abstract class ContextListener<T> extends MouseAdapter {

        protected MyJPopupMenu contextMenu = new MyJPopupMenu();

        private JTable table;
        private ObjectReflectTableModel tableModel;

        public ContextListener(JTable table) {
            this.table = table;
            tableModel = (ObjectReflectTableModel) table.getModel();
            table.addMouseListener(this);
        }

        public abstract void openKarteCommand(T value);
        public abstract void maybeShowPopup(MouseEvent e);

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && !contextMenu.isShowing() && e.getSource() instanceof JTable) {
              int row = table.convertRowIndexToModel(table.getSelectedRow());
              T value = (T) tableModel.getObject(row);
              if (value != null) {
                  openKarteCommand(value);
              }
    //        複数行選択対応 〜 機能しない（どうやっても複数選択のまま double click はできない）
    //          int[] rows = table.getSelectedRows();
    //          for (int i=0; i < rows.length; i++) {
    //              rows[i] = table.convertRowIndexToModel(rows[i]);
    //              T value = (T) tableModel.getObject(rows[i]);
    //              if (value != null) {
    //                  openKarteCommand(value);
    //              }
    //           }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
//      public void mouseReleased(MouseEvent e) { // windows はこちら

            int clickedRow = table.rowAtPoint(e.getPoint());

            // テーブル以外の場所がクリックされていたら選択クリア（popup はのぞく）
            if (clickedRow < 0 && !e.isPopupTrigger()) {
                table.clearSelection();
                e.getComponent().requestFocusInWindow();

            // 選択のない位置で右クリックされていたら選択する（popup用）
            } else {
                if (e.isPopupTrigger()) {
                    // クリックされた場所が選択されているかどうか
                    boolean isSelected = false;
                    for (int row : table.getSelectedRows()) {
                        if (row == clickedRow) {
                            isSelected = true;
                            break;
                        }
                    }
                    // 選択されていない場所でクリックした場合は選択し直す
                    if (!isSelected){
                        table.getSelectionModel().setSelectionInterval(clickedRow, clickedRow);
                        table.requestFocusInWindow();
                    }
                }
            }
            maybeShowPopup(e);
        }
    }
}

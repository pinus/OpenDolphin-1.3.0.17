
package open.dolphin.impl.labrcv;


import open.dolphin.ui.AdditionalTableSettings;
import java.awt.Dimension;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import open.dolphin.infomodel.LaboImportSummary;
import open.dolphin.infomodel.PatientModel;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
// import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import open.dolphin.client.AbstractMainComponent;
import open.dolphin.client.ClientContext;
import open.dolphin.client.GUIConst;
import open.dolphin.client.MainComponentPanel;
import open.dolphin.table.ObjectListTable;
import open.dolphin.helper.ReflectAction;
import open.dolphin.table.IndentTableCellRenderer;
import open.dolphin.ui.MyJPopupMenu;
import open.dolphin.ui.StatusPanel;

/**
 * LaboTestImport
 *
 * @author Kazushi Minagawa
 */
public class LaboTestImporter extends AbstractMainComponent {

    private static final String NAME = "ラボレシーバ";

    // 選択されている患者情報
    private LaboImportSummary selectedLabo;
    //private int number = 100000;

    // GUI コンポーネント
    private ObjectListTable laboListTable;
    private JProgressBar usp;
    private JLabel countLabel;
    private JLabel dateLabel;

    /** Creates new PatientSearch */
    public LaboTestImporter() {
        setName(NAME);
    }

    @Override
    public void start() {
        initComponents();
        connect();
        enter();
    }

    @Override
    public void enter() {
        controlMenu();
    }

    @Override
    public void stop() {
    }

    public LaboImportSummary getSelectedLabo() {
        return selectedLabo;
    }

    /**
     * 受付リストのコンテキストメニュークラス。
     */
    class ContextListener extends AbstractMainComponent.ContextListener<LaboImportSummary> {

        public ContextListener(JTable table) {
            super(table);
        }

        @Override
        public void openKarteCommand(LaboImportSummary value) {
            openKarte(value.getPatient());
        }

        @Override
        public void maybeShowPopup(MouseEvent e) {

            if (e.isPopupTrigger()) {

                final MyJPopupMenu contextMenu = new MyJPopupMenu();

                int row = laboListTable.getTable().rowAtPoint(e.getPoint());
                Object obj = laboListTable.getTableModel().getObject(row);
                int selected = laboListTable.getTable().getSelectedRow();

                if (row == selected && obj != null) {
                    String pop1 = ClientContext.getString("watingList.popup.openKarte");
                    contextMenu.add(new JMenuItem(new ReflectAction(pop1, LaboTestImporter.this, "openKarte")));
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }

    public void setSelectedLabo(LaboImportSummary selectedLabo) {
        this.selectedLabo = selectedLabo;
        controlMenu();
    }

    public void openKarte() {
        openKarte(selectedLabo.getPatient());
    }

    public ObjectListTable getLaboListTable() {
        return laboListTable;
    }

    public JProgressBar getProgressBar() {
        return usp;
    }
    /**
     * 検索結果件数を設定しステータスパネルへ表示する。
     * @param cnt 件数
     */
    public void updateCount() {
        int count = laboListTable.getTableModel().getObjectCount();
        String text = ClientContext.getString("laboTestImport.count.text");
        text += String.valueOf(count);
        countLabel.setText(text);
    }

    /**
     * メニューを制御する
     */
    private void controlMenu() {

        PatientModel pvt = getSelectedLabo() != null
                         ? getSelectedLabo().getPatient()
                         : null;

        boolean enabled = canOpen(pvt);
        getContext().enabledAction(GUIConst.ACTION_OPEN_KARTE, enabled);
    }

    /**
     * GUI コンポーネントを初期化する。
     */
    private void initComponents() {

        JLabel iconLabel = new JLabel(GUIConst.ICON_DOCUMENT_SAVE_22);
        JLabel instLabel = new JLabel("検査結果ファイル(MML形式)を下のテーブルに Drag & Drop してください。");

        // ラボテストテーブルを生成する
        String[] columnNames = {"　患者氏名","　性別","　テスト/検体名","　検体採取日","　報告日","　ステータス","　ラボ名"};
        int startNumRows = ClientContext.getInt("labotestImport.startNumRows");
        String[] methodNames = ClientContext.getStringArray("labotestImport.methodNames");
        Class[] classes = ClientContext.getClassArray("labotestImport.classNames");
        int[] columnWidth = {100,50,100,100,100,80,100};
        int rowHeight = ClientContext.getInt("labotestImport.rowHeight");
        laboListTable = new ObjectListTable(columnNames, startNumRows, methodNames, classes);
        laboListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        laboListTable.getTable().setDefaultRenderer(Object.class, new IndentTableCellRenderer());
        // カラム幅を変更する
        for (int i = 0; i < columnWidth.length; i++) {
            TableColumn column = laboListTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidth[i]);
            if (i == 1) { // 性別の幅は不変にする
                column.setMaxWidth(columnWidth[i]);
                column.setMinWidth(columnWidth[i]);
            }
        }
        //laboListTable.getTable().setRowHeight(rowHeight);
        JScrollPane scroller = laboListTable.getScroller();

        // TransferHandlerを設定する
        final JTable table = laboListTable.getTable();
        table.setTransferHandler(new LaboTestFileTransferHandler(this));

        // Status パネルを生成する
        usp = new JProgressBar();
        Dimension pbSize = new Dimension(100,14);
        usp.setMaximumSize(pbSize);
        usp.setPreferredSize(pbSize);

        //Font font = new Font("Dialog", Font.PLAIN, ClientContext.getInt("watingList.state.font.size"));
        countLabel = new JLabel("");
        dateLabel = new JLabel("");

        // カウント値０を設定する
        updateCount();

        // 日付を設定する
        String formatStr = ClientContext.getString("watingList.state.dateFormat");
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr); // 2006-11-20(水)
        dateLabel.setText(sdf.format(new Date()) + " ");

        // 全体をレイアウトする
        MainComponentPanel view = new MainComponentPanel();
        MainComponentPanel.CommandPanel comPanel = view.getCommandPanel();
        MainComponentPanel.MainPanel mainPanel = view.getMainPanel();
        StatusPanel statusPanel = view.getStatusPanel();

        comPanel.add(iconLabel);
        comPanel.add(instLabel);
        comPanel.setMargin(4);

        mainPanel.add(scroller);

        statusPanel.addGlue();
        statusPanel.add(usp);
        statusPanel.addSeparator();
        statusPanel.add(countLabel);
        statusPanel.addSeparator();
        statusPanel.add(dateLabel);
        statusPanel.setMargin(4);

        setUI(view);

        DropTarget dt = new DropTarget(view, new DropTargetAdapter() {

            @Override
            public void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                table.getTransferHandler().importData(table, dtde.getTransferable());
                dtde.dropComplete(true); // これをしないとドラッグしてきたアイコンが逃げる
            }
        });
        dt.setActive(true);
        // table の関係ないところをクリックしたら，selection をクリア
        ContextListener l = new ContextListener(laboListTable.getTable());
        AdditionalTableSettings.setTable(table);
    }

    /**
     * コンポーンントにリスナを登録し接続する。
     */
    private void connect() {
        // 行選択
        PropertyChangeListener pls = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(ObjectListTable.SELECTED_OBJECT)) {
                    Object[] obj = (Object[]) e.getNewValue();
                    // 情報をリフレッシュするため null かどうかに関係なくセットし通知する必要がある
                    LaboImportSummary value = (obj != null && obj.length > 0) ? (LaboImportSummary) obj[0] : null;
                    setSelectedLabo(value);
                }
            }
        };
        laboListTable.addPropertyChangeListener(ObjectListTable.SELECTED_OBJECT, pls);
    }
}

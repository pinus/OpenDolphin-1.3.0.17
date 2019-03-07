package open.dolphin.impl.labrcv;

import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import open.dolphin.client.AbstractMainComponent;
import open.dolphin.client.ClientContext;
import open.dolphin.client.GUIConst;
import open.dolphin.client.MainComponentPanel;
import open.dolphin.infomodel.LaboImportSummary;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.table.IndentTableCellRenderer;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.MyJScrollPane;
import open.dolphin.ui.StatusPanel;
import open.dolphin.helper.PNSTriple;

/**
 * LaboTestImport.
 *
 * @author Kazushi Minagawa
 */
public class LaboTestImporter extends AbstractMainComponent {

    private static final String NAME = "ラボレシーバ";

    // GUI コンポーネント
    private JTable table;
    private ObjectReflectTableModel<LaboImportSummary> tableModel;
    private JProgressBar usp;
    private JLabel countLabel;
    private JLabel dateLabel;

    public LaboTestImporter() {
        setName(NAME);
    }

    @Override
    public void start() {
        initComponents();
        enter();
    }

    @Override
    public void enter() {
        controlMenu();
    }

    @Override
    public void stop() {
    }

    public LaboImportSummary getSelectedObject() {
        int row = table.getSelectedRow();
        return tableModel.getObject(row);
    }

    public ObjectReflectTableModel<LaboImportSummary> getTableModel() {
        return tableModel;
    }

    public JProgressBar getProgressBar() {
        return usp;
    }
    /**
     * 検索結果件数を設定しステータスパネルへ表示する.
     */
    public void updateCount() {
        int count = tableModel.getObjectCount();
        String text = String.format("登録件数:%d", count);
        countLabel.setText(text);
    }

    /**
     * メニューを制御する.
     */
    private void controlMenu() {
        PatientModel patientModel = getSelectedObject() != null? getSelectedObject().getPatient() : null;
        getContext().enableAction(GUIConst.ACTION_OPEN_KARTE, canOpen(patientModel));
    }

    /**
     * GUI コンポーネントを初期化する.
     */
    private void initComponents() {

        JLabel iconLabel = new JLabel(GUIConst.ICON_DOCUMENT_SAVE_22);
        JLabel instLabel = new JLabel("検査結果ファイル(MML形式)を下のテーブルに Drag & Drop してください. ");

        // ラボテストテーブルを生成する
        List<PNSTriple<String,Class<?>,String>> reflectionList = Arrays.asList(
                new PNSTriple<>("　患者氏名", String.class, "getPatientName"),
                new PNSTriple<>("　性別", String.class, "getPatientGender"),
                new PNSTriple<>("　テスト/検体名", String.class, "getSetName"),
                new PNSTriple<>("　検体採取日", String.class, "getSampleTime"),
                new PNSTriple<>("　報告日", String.class, "getReportTime"),
                new PNSTriple<>("　ステータス", String.class, "getReportStatus"),
                new PNSTriple<>("　ラボ名", String.class, "getLaboratoryCenter")
        );
        int[] columnWidth = {100,50,100,100,100,80,100};
        int rowHeight = ClientContext.getInt("labotestImport.rowHeight");

        tableModel = new ObjectReflectTableModel<>(reflectionList);
        table = new JTable(tableModel);
        table.putClientProperty("Quaqua.Table.style", "striped");
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new IndentTableCellRenderer());

        table.getSelectionModel().addListSelectionListener(e -> controlMenu());
        table.addMouseListener(new ContextListener());

        table.setTransferHandler(new LaboTestFileTransferHandler(this));

        // カラム幅を変更する
        for (int i = 0; i < columnWidth.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidth[i]);
            if (i == 1) { // 性別の幅は不変にする
                column.setMaxWidth(columnWidth[i]);
                column.setMinWidth(columnWidth[i]);
            }
        }
        //laboListTable.getTable().setRowHeight(rowHeight);
        MyJScrollPane scroller = new MyJScrollPane(table);

        // Status パネルを生成する
        usp = new JProgressBar();
        Dimension pbSize = new Dimension(100,14);
        usp.setMaximumSize(pbSize);
        usp.setPreferredSize(pbSize);

        //Font font = new Font("Dialog", Font.PLAIN, ClientContext.getInt("waitingList.state.font.size"));
        countLabel = new JLabel("");
        dateLabel = new JLabel("");

        // カウント値０を設定する
        updateCount();

        // 日付を設定する
        String formatStr = ClientContext.getString("waitingList.state.dateFormat");
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr); // 2006-11-20(水)
        dateLabel.setText(sdf.format(new Date()) + " ");

        // 全体をレイアウトする
        MainComponentPanel view = new MainComponentPanel();
        MainComponentPanel.CommandPanel comPanel = view.getCommandPanel();
        view.remove(comPanel);

        MainComponentPanel.MainPanel mainPanel = view.getMainPanel();
        StatusPanel statusPanel = view.getStatusPanel();

        //comPanel.add(iconLabel);
        //comPanel.add(instLabel);
        //comPanel.setMargin(4);

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
    }

    /**
     * 受付リストのコンテキストメニュークラス.
     */
    private class ContextListener extends AbstractMainComponent.ContextListener<LaboImportSummary> {
        private final JPopupMenu contextMenu;

        public ContextListener() {
            contextMenu = getContextMenu();
        }

        @Override
        public void openKarte(LaboImportSummary value) {
            LaboTestImporter.this.openKarte(value.getPatient());
        }

        @Override
        public void maybeShowPopup(MouseEvent e) {

            if (e.isPopupTrigger()) {

                int row = table.rowAtPoint(e.getPoint());
                LaboImportSummary obj = tableModel.getObject(row);
                int selected = table.getSelectedRow();

                if (row == selected && obj != null) {
                    contextMenu.removeAll();
                    JMenuItem menuItem = new JMenuItem("カルテを開く");
                    menuItem.addActionListener(a -> openKarte(tableModel.getObject(table.getSelectedRow())));
                    contextMenu.add(menuItem);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }
}

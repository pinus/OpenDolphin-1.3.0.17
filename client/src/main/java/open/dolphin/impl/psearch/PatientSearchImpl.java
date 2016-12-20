package open.dolphin.impl.psearch;

import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import java.awt.EventQueue;
import java.awt.event.*;
import java.beans.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import open.dolphin.client.AbstractMainComponent;
import open.dolphin.client.CalendarCardPanel;
import open.dolphin.client.ClientContext;
import open.dolphin.client.GUIConst;
import open.dolphin.delegater.PvtDelegater;
import open.dolphin.delegater.PnsDelegater;
import open.dolphin.dto.PatientSearchSpec;
import open.dolphin.helper.KeyBlocker;
import open.dolphin.helper.ReflectAction;
import open.dolphin.helper.Task;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.table.IndentTableCellRenderer;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.AdditionalTableSettings;
import open.dolphin.ui.IMEControl;
import open.dolphin.ui.MyJSheet;
import open.dolphin.util.StringTool;
import org.apache.log4j.Logger;

/**
 * 患者検索
 * @author Kazushi Minagawa, modified by pns
 */
public class PatientSearchImpl extends AbstractMainComponent {

    private static final String NAME = "患者検索";
    private static final String[] COLUMN_NAMES = {" 　患者 ID","　 氏   名","　 カ  ナ"," 性別","　生年月日", "　最終受診日"};

    private static final ImageIcon SEARCH_ICON = GUIConst.ICON_SYSTEM_SEARCH_22;
    private static final String[] METHOD_NAMES = {"getPatientId","getFullName", "getKanaName","getGenderDesc","getAgeBirthday","getFormattedLastVisit"};
    private static final int[] COLUMN_WIDTH = {80,100,120,40,120,120};
    private static final String UNSUITABLE_CHAR = "検索に適さない文字が含まれています。";
    // Preferences の key
    private static final String NARROWING_SEARCH = "narrowingSearch";
    private static final String AGE_DISPLAY = "ageDisplay";
    // 年齢生年月日メソッド
    private static final String[] AGE_METHOD = new String[]{"getAgeBirthday", "getBirthday"};
    // 選択されている患者情報
    private PatientModel[] selectedPatient;
    // 年齢表示をするかどうか
    private boolean ageDisplay;
    // 年齢表示カラム
    private static int AGE_COLUMN = 4;
    // View
    //private PatientSearchView view;
    private PatientSearchPanel view;
    private KeyBlocker keyBlocker;
    private Logger logger;
    private Preferences prefs;

    public PatientSearchImpl() {
        setName(NAME);
        logger = ClientContext.getBootLogger();
        prefs = Preferences.userNodeForPackage(this.getClass());
    }

    @Override
    public void start() {
        initComponents();
        connect();
    }

    @Override
    public void enter() {
        controlMenu();
        // 入ってきたら，キーワードフィールドにフォーカス
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.getKeywordFld().requestFocusInWindow();
                view.getKeywordFld().selectAll();
            }
        });
    }

    @Override
    public void stop() {

    }

    /**
     * GUI コンポーネントを初期化する。
     */
    private void initComponents() {

        //view = new PatientSearchView();
        view = new PatientSearchPanel();
        setUI(view);
        view.getSearchLbl().setIcon(SEARCH_ICON);
        JTable table = view.getTable();
        //
        // 年齢表示をしないなんて信じられない要望!
        //
        ageDisplay = prefs.getBoolean(AGE_DISPLAY, true);
        if (!ageDisplay) {
            METHOD_NAMES[4] = AGE_METHOD[1];
        }

        ObjectReflectTableModel tableModel = new ObjectReflectTableModel(COLUMN_NAMES, 0, METHOD_NAMES, null);
        table.setModel(tableModel);
        TableRowSorter<ObjectReflectTableModel> sorter = new TableRowSorter<ObjectReflectTableModel>(tableModel) {
            // ASCENDING -> DESENDING -> 初期状態 と切り替える
            @Override
            public void toggleSortOrder(int column) {
                if(column >= 0 && column < getModelWrapper().getColumnCount() && isSortable(column)) {
                    List<SortKey> keys = new ArrayList<SortKey>(getSortKeys());
                    if(!keys.isEmpty()) {
                        SortKey sortKey = keys.get(0);
                        if(sortKey.getColumn() == column && sortKey.getSortOrder() == SortOrder.DESCENDING) {
                            setSortKeys(null);
                            return;
                        }
                    }
                }
                super.toggleSortOrder(column);
            }
        };
        // 生年月日コラムに comparator を設定「32.10 歳(S60-01-01)」というのをソートできるようにする
        sorter.setComparator(AGE_COLUMN, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                String birthday1;
                String birthday2;
                if (ageDisplay) {
                    birthday1 = ModelUtils.getMmlBirthdayFromAge((String)o1);
                    birthday2 = ModelUtils.getMmlBirthdayFromAge((String)o2);
                    return birthday2.compareTo(birthday1);
                } else {
                    birthday1 = (String)o1;
                    birthday2 = (String)o2;
                    return birthday1.compareTo(birthday2);
                }
            }
        });
        table.setRowSorter(sorter);

        // カラム幅を変更する
        for (int i = 0; i < COLUMN_WIDTH.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(COLUMN_WIDTH[i]);
            if (i == 3 || i == 0) { // ID, 性別の幅は不変にする
                column.setMaxWidth(COLUMN_WIDTH[i]);
                column.setMinWidth(COLUMN_WIDTH[i]);
            }
        }

        // レンダラを設定する
        view.getTable().setDefaultRenderer(Object.class, new IndentTableCellRenderer(7));

        // 日付を設定する
        String formatStr = "yyyy-M-d (EEE)";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        view.getDateLbl().setText(sdf.format(new Date()));

        // hibernate index ボタン
        view.getHibernateIndexItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String message = "<html><b><u>Hibernate Search のインデックス作成をします</u></b><br><br>"
                        + "ホストのトランザクションタイムアウト時間を<br>"
                        + "のばしておく必要があります。<br>"
                        + "途中でキャンセルはできません。<br>"
                        + "よろしいですか？</html>";
                int option = MyJSheet.showConfirmDialog(view, message, "インデックス作成", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    PnsDelegater dl = new PnsDelegater();
                    dl.makeInitialIndex();
                }
            }
        });

        // 絞り込み検索ボタンの初期値
        view.getNarrowingSearchCb().setSelected(prefs.getBoolean(NARROWING_SEARCH, false));
    }

    /**
     * コンポネントにリスナを登録し接続する。
     */
    private void connect() {
        // カレンダによる日付検索を設定する
        new PopupListener(view.getKeywordFld());
        keyBlocker = new KeyBlocker(view.getKeywordFld());

        // KeywordFld に ActionListener 登録
        view.getKeywordFld().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField tf = (JTextField) e.getSource();
                String test = tf.getText().trim();
                if (!test.equals("")) {
                    // 検索開始
                    find(test);

                } else {
                    // キーワードが "" ならテーブルクリアして，検索件数をリセット
                    ((ObjectReflectTableModel) view.getTable().getModel()).clear();
                    view.getCntLbl().setText("0 件");
                }
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        view.getKeywordFld().requestFocus();
                    }
                });
            }
        });
        // 履歴保存する prefs をセット
        view.getKeywordFld().setPreferences(prefs);

        // クリアボタンの動作
        view.getClearBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.getKeywordFld().setText("");
                ((ObjectReflectTableModel) view.getTable().getModel()).clear();
                view.getCntLbl().setText("0 件");
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        view.getKeywordFld().requestFocus();
                    }
                });
            }
        });

        // IME off
        IMEControl.setImeOffIfFocused(view.getKeywordFld());

        // Table に ListSelectionListener 登録
        final JTable table = view.getTable();
        final ObjectReflectTableModel tableModel = (ObjectReflectTableModel) table.getModel();

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {

                    int[] rows = table.getSelectedRows();
                    if (rows == null) {
                        setSelectedPatinet(null);
                    } else {
                        PatientModel[] patients = new PatientModel[rows.length];
                        for (int i=0; i < rows.length; i++) {
                            rows[i] = table.convertRowIndexToModel(rows[i]);
                            patients[i] = (PatientModel) tableModel.getObject(rows[i]);
                        }
                        setSelectedPatinet(patients);
                    }
                }
            }
        });

        // 絞り込み選択ボタンが解除されたときは背景もクリアする
        view.getNarrowingSearchCb().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                prefs.putBoolean(NARROWING_SEARCH, view.getNarrowingSearchCb().isSelected());

                if (tableModel.getObjectCount() > 0 && view.getNarrowingSearchCb().isSelected()) {
                    view.getKeywordFld().setBackground(PatientSearchPanel.NARROWING_SEARCH_BACKGROUND_COLOR);
                } else {
                    view.getKeywordFld().setBackground(PatientSearchPanel.NORMAL_SEARCH_BACKGROUND_COLOR);
                }
            }
        });

        // テーブルの状態による絞り込み検索モードの制御
        tableModel.addTableModelListener(new TableModelListener(){
            @Override
            public void tableChanged(TableModelEvent e) {
                if (tableModel.getObjectCount() > 0 && view.getNarrowingSearchCb().isSelected()) {
                    view.getKeywordFld().setBackground(PatientSearchPanel.NARROWING_SEARCH_BACKGROUND_COLOR);
                } else {
                    view.getKeywordFld().setBackground(PatientSearchPanel.NORMAL_SEARCH_BACKGROUND_COLOR);
                }
            }
        });

        // コンテキストメニューを設定する
        ContextListener l = new ContextListener(table);
        AdditionalTableSettings.setTable(table, l);
    }

    /**
     * メニューを制御する
     * 複数行選択対応 by pns
     * １つでも開けられないものがあれば false とする
     */
    private void controlMenu() {
        getContext().enabledAction(GUIConst.ACTION_OPEN_KARTE, canOpen());
    }

    /**
     * 現在選択されている PatientModel[] を返す
     * @return
     */
    public PatientModel[] getSelectedPatinet() {
        return selectedPatient;
    }

    /**
     * SelectionListener から呼ばれて selectedPatient をセットする
     * @param model
     */
    public void setSelectedPatinet(PatientModel[] model) {
        selectedPatient = model;
        controlMenu();
    }

    /**
     * 年齢表示をオンオフする。
     */
    public void switchAgeDisplay() {
        ageDisplay = !ageDisplay;
        prefs.putBoolean(AGE_DISPLAY, ageDisplay);

        if (view.getTable() != null) {
            ObjectReflectTableModel tModel = (ObjectReflectTableModel) view.getTable().getModel();
            String method = ageDisplay ? AGE_METHOD[0] : AGE_METHOD[1];
            tModel.setMethodName(method, AGE_COLUMN);
        }
    }

    /**
     * 現在の selectedPatinet が canOpen かどうか判定 by pns
     * １つでも開けられないものがあれば false とする
     */
    private boolean canOpen() {
/*
        boolean isCanOpen = false;
        PatientModel[] pt = getSelectedPatinet();

        if (pt == null || pt.length == 0) {
            isCanOpen = false;
        } else {
            isCanOpen = true;
            for (int i=0; i < pt.length; i++) {
                if (! canOpen(pt[i])) {
                    isCanOpen = false;
                    break;
                }
            }
        }
        return isCanOpen;
 */
        // 既に開かれているカルテを open すると，toFront として処理されるので
        // PatientSearchImpl では open できないカルテというのはない
        return true;
    }

    /**
     * カルテを開く
     * popupMenu で選択した場合はこれが呼ばれる　複数行選択対応
     * openKarte(patient) は abstract に移動
     * by pns
     */
    public void openKarte() {
        PatientModel patient[] = getSelectedPatinet();
        if (patient == null) return;

        for (int i=0; i < patient.length; i++) {
            openKarte(patient[i]);
        }
    }

    /**
     * リストで選択された患者を受付に登録する。
     * 複数行選択対応 by pns
     */
    public void addAsPvt() {

        PatientModel[] patients = getSelectedPatinet();
        if (patients == null) return;

        PatientVisitModel[] pvts = new PatientVisitModel[patients.length];
        String pvtDate = ModelUtils.getDateTimeAsString(new Date());
        String dept = constarctDept();

        for (int i=0; i < patients.length; i++) {
            // 来院情報を生成する
            PatientVisitModel pvt = new PatientVisitModel();
            pvt.setId(0L);
            pvt.setNumber(getNewPvtNumber()); //10000から割り当て
            pvt.setPatient(patients[i]);
            // 受け付けを通していないので診療科はユーザ登録してあるものを使用する
            pvt.setDepartment(dept);
            // 来院日
            pvt.setPvtDate(pvtDate);

            pvts[i] = pvt;
        }

        AddAsPvtTask task = new AddAsPvtTask(pvts);
        task.execute();
    }


    /**
     * 選択患者を受付する
     * 複数行選択対応　by pns
     */
    private class AddAsPvtTask extends SwingWorker<Void, Void> {

        private PatientVisitModel[] pvts;

        public AddAsPvtTask(PatientVisitModel[] pvts) {
            super();
            this.pvts = pvts;
        }

        @Override
        protected Void doInBackground() {

            PvtDelegater pdl = new PvtDelegater();
            for(PatientVisitModel pvt : pvts) {
                pdl.addPvt(pvt);
            }
            return null;
        }
    }

    /**
     * 検索テキストを解析して，該当する検索タスクを呼び出す
     * @param text キーワード
     */
    private void find(String text) {

        PatientSearchSpec spec = new PatientSearchSpec();

        if (text.startsWith("N ") || text.startsWith("n ")) {
            spec.setCode(PatientSearchSpec.NAME_SEARCH);
            text = text.substring(2);
            spec.setName(text);

        } else if (text.startsWith("K ") || text.startsWith("k ")) {
            spec.setCode(PatientSearchSpec.KANA_SEARCH);
            text = text.substring(2);
            spec.setName(text);

        } else if (text.startsWith("A ") || text.startsWith("a ")) {
            spec.setCode(PatientSearchSpec.ADDRESS_SEARCH);
            text = text.substring(2);
            spec.setAddress(text);

        } else if (text.startsWith("Z ") || text.startsWith("z ")) {
            spec.setCode(PatientSearchSpec.ZIPCODE_SEARCH);
            text = text.substring(2);
            spec.setZipCode(text);

        } else if (text.startsWith("T ") || text.startsWith("t ")) {
            spec.setCode(PatientSearchSpec.TELEPHONE_SEARCH);
            text = text.substring(2);
            spec.setTelephone(text);

        } else if (text.startsWith("I ") || text.startsWith("i ")) {
            spec.setCode(PatientSearchSpec.ID_SEARCH);
            text = text.substring(2);
            spec.setPatientId(text);

        } else if (text.startsWith("E ") || text.startsWith("e ")) {
            spec.setCode(PatientSearchSpec.EMAIL_SEARCH);
            text = text.substring(2);
            spec.setEmail(text);

        } else if (text.startsWith("O ") || text.startsWith("o ")) {
            spec.setCode(PatientSearchSpec.OTHERID_SEARCH);
            text = text.substring(2);
            spec.setOtherId(text);

        } else if (text.startsWith("B ") || text.startsWith("b ")) {
            spec.setCode(PatientSearchSpec.BIRTHDAY_SEARCH);
            text = text.substring(2);
            spec.setBirthday(ModelUtils.toSeireki(text));

        } else if (text.startsWith("M ") || text.startsWith("m ")) {
            spec.setCode(PatientSearchSpec.MEMO_SEARCH);
            text = text.substring(2);
            spec.setSearchText(text);

        } else if (text.startsWith("F ") || text.startsWith("f ")) {
            spec.setCode(PatientSearchSpec.FULL_TEXT_SEARCH);
            text = text.substring(2);
            spec.setSearchText(text);

        } else if (isNengoDate(text)) {
            spec.setCode(PatientSearchSpec.BIRTHDAY_SEARCH);
            spec.setBirthday(ModelUtils.toSeireki(text));

        } else if (isOrcaDate(text)) {
            spec.setCode(PatientSearchSpec.BIRTHDAY_SEARCH);
            spec.setBirthday(ModelUtils.toSeireki(ModelUtils.OrcaDateToNengo(text)));

        } else if (isDate(text)) {
            //System.out.println("Date search");
            spec.setCode(PatientSearchSpec.DATE_SEARCH);
            spec.setDate(ModelUtils.toSeireki(text)); // 月/日が１桁で指定された場合の調整 by pns

        } else if (isKana(text)) {
            spec.setCode(PatientSearchSpec.KANA_SEARCH);
            spec.setName(text);

        } else if (isHiragana(text)){
            spec.setCode(PatientSearchSpec.KANA_SEARCH);
            spec.setName(StringTool.hiraganaToKatakana(text));

        } else if (isId(text)) {
            spec.setCode(PatientSearchSpec.ID_SEARCH);
            spec.setPatientId(text);

        } else if (text.length() <= 1) {
            MyJSheet.showMessageDialog(getContext().getFrame(),
                "検索文字列は２文字以上入力して下さい",  "検索文字列入力エラー", JOptionPane.ERROR_MESSAGE);
            return;

        } else {
            spec.setCode(PatientSearchSpec.FULL_TEXT_SEARCH);
            spec.setSearchText(text);
        }

        FindTask task = new FindTask(view, "患者検索", "検索中...",  spec);

        task.setInputBlocker(new Blocker());
        // キャンセルした際 interrupt すると delegater で NamigException が出る
        task.setInterruptOnCancel(false);
        task.execute();
    }

    /**
     * FindTask で使う InputBlocker
     */
    private class Blocker implements Task.InputBlocker {

        @Override
        public void block() {
            keyBlocker.block();
            getContext().getGlassPane().block();
            JProgressBar bar = view.getProgressBar();
            //bar.setIndeterminate(true);
        }

        @Override
        public void unblock() {
            keyBlocker.unblock();
            getContext().getGlassPane().unblock();
            JProgressBar bar = view.getProgressBar();
            //bar.setIndeterminate(false);
            bar.setValue(0);
        }
    }

    private boolean isDate(String text) {
        return text==null? false: text.matches("[0-9][0-9][0-9][0-9]-[0-9]+-[0-9]+");
    }
    private boolean isNengoDate(String text) {
        return text==null? false: text.matches("[MmTtSsHh][0-9]+-[0-9]+-[0-9]+");
    }
    private boolean isOrcaDate(String text) {
        // S12-3-4 = 3120304
        return text==null? false: text.matches("[1-4][0-6][0-9][0-1][0-9][0-3][0-9]");
    }
    private boolean isKana(String text) {
        boolean maybe = true;
        if (text != null) {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                // スペースが入っていてもカタカナと判断するようにする
                if (!StringTool.isKatakana(c) && !StringTool.isSpace(c)) {
                    maybe = false;
                    break;
                }
            }
            return maybe;
        }
        return false;
    }
    private boolean isHiragana(String text) {
        boolean maybe = true;
        if (text != null) {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (!StringTool.isHiragana(c) && !StringTool.isSpace(c)) {
                    maybe = false;
                    break;
                }
            }
            return maybe;
        }
        return false;
    }
    private boolean isId(String text) {
        if (text == null || text.length() > 6) return false;
        return text.matches("[0-9]+");
    }

    /**
     * 受付リストのコンテキストメニュークラス。
     * modified by pns
     */
    private class ContextListener extends AbstractMainComponent.ContextListener<PatientModel> {
        private final JPopupMenu contextMenu;

        public ContextListener(JTable table) {
            super(table);
            contextMenu = getContextMenu();
        }

        @Override
        public void openKarte(PatientModel pvt) {
            PatientSearchImpl.this.openKarte(pvt);
        }

        @Override
        public void maybeShowPopup(MouseEvent e) {

            if (e.isPopupTrigger()) {

                contextMenu.removeAll();

                if (canOpen()) {
                    String pop1 = ClientContext.getString("watingList.popup.openKarte");
                    JMenuItem openKarte = new JMenuItem(new ReflectAction(pop1, PatientSearchImpl.this, "openKarte"));
                    JMenuItem addAsPvt = new JMenuItem(new ReflectAction("受付登録", PatientSearchImpl.this, "addAsPvt"));
                    openKarte.setIconTextGap(8);
                    addAsPvt.setIconTextGap(8);
                    contextMenu.add(openKarte);
                    contextMenu.add(addAsPvt);
                }

                JCheckBoxMenuItem item = new JCheckBoxMenuItem("年齢表示");
                item.setIconTextGap(12);
                contextMenu.add(item);
                item.setSelected(ageDisplay);
                item.addActionListener(EventHandler.create(ActionListener.class, PatientSearchImpl.this, "switchAgeDisplay"));

                // 検索結果をファイル保存
                int selectedRowCount = view.getTable().getSelectedRowCount();
                if (selectedRowCount > 0) {
                    JMenuItem export = new JMenuItem(new ReflectAction("選択された " + selectedRowCount + " 件をファイル保存", PatientSearchImpl.this, "exportSearchResult"));
                    export.setIconTextGap(8);
                    contextMenu.add(export);
                }

                contextMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * 検索結果をファイルに書き出す
     * 複数行選択対応
     */
    public void exportSearchResult() {
        final JFileChooser fileChooser = new JFileChooser();
        MyJSheet.showSaveSheet(fileChooser, getContext().getFrame(), new SheetListener() {
            @Override
            public void optionSelected(SheetEvent e) {
                if (e.getOption() == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    if (!file.exists() || overwriteConfirmed(file)) {

                        try {
                            FileWriter writer = new FileWriter(file);
                            JTable table = view.getTable();
                            // 書き出す内容　選択されたものを書き出す
                            StringBuilder sb = new StringBuilder();
                            int[] rows = table.getSelectedRows();
                            for (int i = 0; i < rows.length; i++) {
                                rows[i] = table.convertRowIndexToModel(rows[i]);
                                for (int column = 0; column < table.getColumnCount(); column++) {
                                    sb.append(column==0?'"':",\"");
                                    sb.append(table.getValueAt(rows[i], column));
                                    sb.append('"');
                                }
                                sb.append('\n');
                            }
                            writer.write(sb.toString());
                            writer.close();

                        } catch (IOException ex) {
                            System.out.println("PatientSearchImpl.java: " + ex);
                        }
                    }
                }
            }
        });
    }
    /**
     * ファイル上書き確認ダイアログを表示する。
     * @param file 上書き対象ファイル
     * @return 上書きOKが指示されたらtrue
     */
    private boolean overwriteConfirmed(File file){
        String title = "上書き確認";
        String message = "既存のファイル「" + file.toString() + "」\n"
                        +"を上書きしようとしています。続けますか？";

        int confirm = MyJSheet.showConfirmDialog(
                getContext().getFrame(), message, title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE );

        if(confirm == JOptionPane.OK_OPTION) return true;

        return false;
    }

    /**
     * テキストフィールドへ日付を入力するためのカレンダーポップアップメニュークラス。
     */
    private class PopupListener extends MouseAdapter implements PropertyChangeListener {

        /** ポップアップメニュー */
        private JPopupMenu popup;
        /** ターゲットのテキストフィールド */
        private JTextField tf;

        public PopupListener(JTextField tf) {
            this.tf = tf;
            tf.addMouseListener(this);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {

            if (e.isPopupTrigger()) {
                popup = new JPopupMenu();
                CalendarCardPanel cc = new CalendarCardPanel(ClientContext.getEventColorTable());
                cc.addPropertyChangeListener(CalendarCardPanel.PICKED_DATE, this);
                cc.setCalendarRange(new int[]{-12, 0});
                popup.insert(cc, 0);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName().equals(CalendarCardPanel.PICKED_DATE)) {
                SimpleDate sd = (SimpleDate) e.getNewValue();
                tf.setText(SimpleDate.simpleDateToMmldate(sd));
                popup.setVisible(false);
                popup = null;
                String test = tf.getText().trim();
                if (!test.equals("")) {
                    find(test);
                }
            }
        }
    }
}


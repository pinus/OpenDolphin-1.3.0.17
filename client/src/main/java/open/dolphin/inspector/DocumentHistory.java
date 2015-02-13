package open.dolphin.inspector;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.EventHandler;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import open.dolphin.client.BlockGlass;
import open.dolphin.client.Chart;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.ClientContext;
import open.dolphin.client.NameValuePair;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.dto.DocumentSearchSpec;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.project.Project;
import open.dolphin.table.IndentTableCellRenderer;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.AdditionalTableSettings;
import open.dolphin.ui.IMEControl;
import open.dolphin.ui.MyDefaultCellEditor;

/**
 * 文書履歴を取得し、表示するクラス。
 *
 * @author Minagawa,Kazushi
 */
public class DocumentHistory {

    // PropertyChange 名
    public static final String DOCUMENT_TYPE = "documentTypeProp";
    public static final String SELECTED_HISTORIES = "selectedHistories";
    public static final String SELECTED_KARTES = "selectedKartes";
    public static final String HITORY_UPDATED = "historyUpdated";
    // 文書履歴テーブル
    private ObjectReflectTableModel tableModel;
    //private DocumentHistoryView view;
    private DocumentHistoryPanel view;
    // 抽出期間コンボボックス
    private JComboBox extractionCombo;
    // 文書種別コンボボックス
    private JComboBox contentCombo;
    // 件数フィールド
    private JLabel countField;
    // 束縛サポート
    private PropertyChangeSupport boundSupport;
    // context
    private ChartImpl context;
    // 選択された文書情報(DocInfo)の配列
    private DocInfoModel[] selectedHistories;
    // 抽出コンテント(文書種別)
    private String extractionContent;
    // 抽出開始日
    private Date extractionPeriod;
    // 自動的に取得する文書数
    private int autoFetchCount;
    // 昇順降順のフラグ
    private boolean ascending;
    // 修正版も表示するかどうかのフラグ
    private boolean showModified;
    // フラグ
    private boolean start;
    // 未使用
    private NameValuePair[] contentObject;
    // 文書抽出期間
    public static final NameValuePair[] extractionObjects = {
        //new NameValuePair("1ヶ月", "-1"),
        //new NameValuePair("3ヶ月", "-3"),
        new NameValuePair("半年", "-6"),
        new NameValuePair("1年", "-12"),
        new NameValuePair("2年", "-24"),
        new NameValuePair("3年", "-36"),
        new NameValuePair("5年", "-60"),
        new NameValuePair("全て", "-180") // 15年 must be enough
    };
    // Key入力をブロックするリスナ
    private BlockKeyListener blockKeyListener;
    // 編集終了したカルテの日付を保存する。編集終了した時に必ず選択するために使う
    private String editDate = null;

    /**
     * 文書履歴オブジェクトを生成する。
     * @param owner コンテキシト
     */
    public DocumentHistory(ChartImpl context) {
        this.context = context;
        initComponent();
        connect();
        start = true;
    }

    /**
     * 全部のカルテを選択する command-A を押すと，KarteDocumentViewer の selectAll が呼ばれて，そこからここが呼ばれる
     */
    public void selectAll() {
        JTable table = view.getTable();
        ObjectReflectTableModel model = (ObjectReflectTableModel) table.getModel();
        int r = model.getObjectCount(); //rowCount だとだめ。データがないところも全部選択されてしまう
        ListSelectionModel lsm = table.getSelectionModel();
        lsm.setSelectionInterval(0, r-1);
    }

    /**
     * 履歴テーブルのコレクションを clear する。
     */
    public void clear() {
        tableModel.clear();
    }

    /**
     * 文書履歴テーブルにフォーカスを取る
     */
    public void requestFocus() {
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run() {
                view.getTable().requestFocusInWindow();
                // quaqua を修正して対応した
                //view.getTable().repaint(); // repaint しないと，選択がグレーのままになる
            }
        });
    }

    /**
     * 束縛プロパティリスナを登録する。
     * @param propName プロパティ名
     * @param listener リスナ
     */
    public void addPropertyChangeListener(String propName, PropertyChangeListener listener) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.addPropertyChangeListener(propName, listener);
    }

    /**
     * 束縛プロパティを削除する。
     * @param propName プロパティ名
     * @param listener リスナ
     */
    public void removePropertyChangeListener(String propName, PropertyChangeListener listener) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.removePropertyChangeListener(propName, listener);
    }

    /**
     * 選択された文書履歴(複数)を返す。
     * @return 選択された文書履歴(複数)
     */
    public DocInfoModel[] getSelectedHistories() {
        return selectedHistories;
    }

    /**
     * 束縛プロパティの選択された文書履歴(複数)を設定する。通知を行う。
     * @param newSelected 選択された文書履歴(複数)
     */
    public void setSelectedHistories(DocInfoModel[] newSelected) {

        DocInfoModel[] old = selectedHistories;
        selectedHistories = newSelected;
        //
        // リスナへ通知を行う  -> DocumetnBridge#propertyChange
        //
        if (selectedHistories != null) {
            boundSupport.firePropertyChange(SELECTED_HISTORIES, old, selectedHistories);
        }
    }

    /**
     * 履歴の検索時にテーブルのキー入力をブロックする。
     * @param busy true の時検索中
     */
    public void blockHistoryTable(boolean busy) {
        BlockGlass blockGlass = (BlockGlass) context.getFrame().getGlassPane();
        if (busy) {
            view.getTable().addKeyListener(blockKeyListener);
            blockGlass.setText("読み込み中");
            blockGlass.block();
        } else {
            view.getTable().removeKeyListener(blockKeyListener);
            blockGlass.setText("");
            blockGlass.unblock();
        }
    }

    /**
     * 文書履歴を Karte から取得し表示する。
     */
    public void showHistory() {
        //List list = context.getKarte().getEntryCollection("docInfo");
        List list = context.getKarte().getDocInfoEntry();
        updateHistory(list);
    }

    /**
     * 文書履歴を取得する。
     * 取得するパラメータ(患者ID、文書タイプ、抽出期間)はこのクラスの属性として
     * 定義されている。これらのパラメータは comboBox等で選択される。値が変化する度に
     * このメソッドがコールされる。
     */
    public void getDocumentHistory() {

        if (start && extractionPeriod != null && extractionContent != null) {

            // 検索パラメータセットのDTOを生成する
            DocumentSearchSpec spec = new DocumentSearchSpec();
            spec.setKarteId(context.getKarte().getId());	// カルテID
            spec.setDocType(extractionContent);			// 文書タイプ
            spec.setFromDate(extractionPeriod);			// 抽出期間開始
            spec.setIncludeModifid(showModified);		// 修正履歴
            spec.setCode(DocumentSearchSpec.DOCTYPE_SEARCH);	// 検索タイプ
            spec.setAscending(ascending);

            DocInfoTask task = new DocInfoTask(context, spec, new DocumentDelegater());
            task.execute();
        }
    }

    /**
     * KarteEditor が編集終了したときはここで更新するようにする
     * 編集したカルテを必ず選択するため
     * @param date
     */
    public void getDocumentHistory(String date) {
        editDate = date;
        getDocumentHistory();
    }

    /**
     * 抽出期間等が変化し、履歴を再取得した場合等の処理で、履歴テーブルの更新、 最初の行の自動選択、束縛プロパティの変化通知を行う。
     * DocInfoTask から呼ばれる
     */
    private void updateHistory(List newHistory) {

        // ソーティングする
        if (newHistory != null && !newHistory.isEmpty()) {
            if (ascending) {
                Collections.sort(newHistory);
            } else {
                Collections.sort(newHistory, Collections.reverseOrder());
            }
        }

        // 抽出期間が変化しても，できるだけ現在の選択を再現するため保存しておく
        JTable table = view.getTable();
        List<String> oldSelection = new ArrayList<String>();
        for (int r : table.getSelectedRows()) {
            int row = table.convertRowIndexToModel(r);
            String date = ((DocInfoModel) tableModel.getObject(row)).getFirstConfirmDateTrimTime();
            oldSelection.add(date);
        }

        // 文書履歴テーブルにデータの Arraylist を設定する
        tableModel.setObjectList(newHistory);

        // 束縛プロパティの通知を行う -> DocumetnBridge#propertyChange
        boundSupport.firePropertyChange(HITORY_UPDATED, false, editDate);

        if (newHistory != null && ! newHistory.isEmpty()) {
            int cnt = newHistory.size();
            countField.setText(String.valueOf(cnt) + " 件");
            int fetchCount = cnt > autoFetchCount ? autoFetchCount : cnt;

            ListSelectionModel selectionModel = table.getSelectionModel();

            // 編集終了後に呼ばれた場合（editDate != null）は，そのカルテを必ず選択状態にする
            if (editDate != null) {
                oldSelection.add(editDate);
                editDate = null;
            }

            // oldSelection を復元する
            selectionModel.setValueIsAdjusting(true);

            for(String oldDate : oldSelection) {
                for(int i=0; i<newHistory.size(); i++) {
                    String date = ((DocInfoModel)newHistory.get(i)).getFirstConfirmDateTrimTime();
                    if (date.equals(oldDate)) {
                        int row = table.convertRowIndexToView(i);
                        selectionModel.addSelectionInterval(row, row);
                    }
                }
            }

            // 復元した結果，選択がない場合はデフォルトの選択
            if (table.getSelectedRowCount() == 0) {
                // テーブルの最初の行の自動選択を行う
                int first = 0;
                int last = 0;

                if (ascending) {
                    last = cnt - 1;
                    first = cnt - fetchCount;
                } else {
                    first = 0;
                    last = fetchCount - 1;
                }
                // 自動選択
                selectionModel.addSelectionInterval(first, last);
                // 選択した行が表示されるようにスクロールする
                Rectangle r = table.getCellRect(first, last, true);
                table.scrollRectToVisible(r);
            }

            selectionModel.setValueIsAdjusting(false);

        } else {
            // カルテが見つかるまで抽出期間を自動的に延ばす
            int selected = extractionCombo.getSelectedIndex();
            if (selected < extractionCombo.getItemCount() - 1) {
                extractionCombo.setSelectedIndex(++selected);
                //System.out.println("extraction period extended to " + extractionObjects[selected]);
            } else {
                // 最後まで見つからない＝初診カルテ
                countField.setText("0 件");
            }
        }
    }

    /**
     * 文書履歴のタイトルを変更する。
     */
    public void titleChanged(DocInfoModel docInfo) {

        if (docInfo != null && docInfo.getTitle() != null) {
            ChangeTitleTask task = new ChangeTitleTask(context, docInfo, new DocumentDelegater());
            task.execute();
        }
    }

    /**
     * 抽出期間を変更し再検索する。
     */
    public void periodChanged(int state) {
        if (state == ItemEvent.SELECTED) {
            int index = extractionCombo.getSelectedIndex();
            NameValuePair pair = extractionObjects[index];
            String value = pair.getValue();
            int addValue = Integer.parseInt(value);
            GregorianCalendar today = new GregorianCalendar();
            today.add(GregorianCalendar.MONTH, addValue);
            today.clear(Calendar.HOUR_OF_DAY);
            today.clear(Calendar.MINUTE);
            today.clear(Calendar.SECOND);
            today.clear(Calendar.MILLISECOND);
            setExtractionPeriod(today.getTime());
        }
    }

    /**
     * 文書種別を変更し再検索する。
     */
    public void contentChanged(int state) {
        if (state == ItemEvent.SELECTED) {
            int index = contentCombo.getSelectedIndex();
            NameValuePair pair = contentObject[index];
            setExtractionContent(pair.getValue());
        }
    }

    /**
     * GUI コンポーネントを生成する。
     */
    private void initComponent() {

        view = new DocumentHistoryPanel();

        // selectAll (command-A) を横取りするため削除
        view.getInputMap().remove(KeyStroke.getKeyStroke('A',java.awt.event.InputEvent.META_MASK));

        // 履歴テーブルのパラメータを取得する
        String[] columnNames = ClientContext.getStringArray("docHistory.columnNames"); // {"確定日", "内容"};
        String[] methodNames = ClientContext.getStringArray("docHistory.methodNames"); // {"getFirstConfirmDateTrimTime",// "getTitle"};
        Class[] columnClasses = {String.class, String.class};
        int startNumRows = ClientContext.getInt("docHistory.startNumRows"); // 20

        // 文書履歴テーブルを生成する
        tableModel = new ObjectReflectTableModel(columnNames, startNumRows, methodNames, columnClasses) {

            @Override
            public boolean isCellEditable(int row, int col) {

                if (col == 1 && getObject(row) != null) {
                    return true;
                }
                return false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {

                if (col != 1 || value == null || value.equals("")) {
                    return;
                }

                Object o = getObject(row);
                if (o == null) {
                    return;
                }

                // 文書タイトルを変更し通知する
                DocInfoModel docInfo = (DocInfoModel) o;
                docInfo.setTitle((String) value);
                titleChanged(docInfo);
            }
        };
        view.getTable().setModel(tableModel);

        // カラム幅を調整する
        // カラム幅は日付の入る１カラム目だけを固定にする
        view.getTable().getColumnModel().getColumn(0).setPreferredWidth(90);
        view.getTable().getColumnModel().getColumn(0).setMaxWidth(90);
        view.getTable().getColumnModel().getColumn(0).setMinWidth(90);

        // タイトルカラムに IME ON を設定する
        JTextField tf = new JTextField();
        IMEControl.setImeOnIfFocused(tf);
        TableColumn column = view.getTable().getColumnModel().getColumn(1);
        column.setCellEditor(new MyDefaultCellEditor(tf));

        // isReadOnly対応
        tf.setEnabled(!context.isReadOnly());

        // 奇数偶数レンダラを設定する
        view.getTable().setDefaultRenderer(Object.class, new IndentTableCellRenderer(IndentTableCellRenderer.NARROW));

        // 文書種別(コンテントタイプ) ComboBox を生成する
        contentObject = new NameValuePair[2];
        contentObject[0] = new NameValuePair("カルテ", "karte");
        // contentObject[1] = new NameValuePair("紹介状", "letter");
        contentCombo = view.getDocTypeCombo();

        // 抽出機関 ComboBox を生成する
        extractionCombo = view.getExtractCombo();

        // 件数フィールドを生成する
        countField = view.getCntLbl();

        // table の関係ないところをクリックしたら，selection をクリア
        AdditionalTableSettings.setTable(view.getTable(), null);
    }

    /**
     * レイアウトパネルを返す。
     * @return
     */
    public JPanel getPanel() {
        return (JPanel) view;
    }

    /**
     * Event 接続を行う
     */
    private void connect() {

        // 履歴テーブルで選択された行の文書を表示する
        ListSelectionModel slm = view.getTable().getSelectionModel();
        slm.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    JTable table = view.getTable();
                    int[] selectedRows = table.getSelectedRows();
                    if (selectedRows.length > 0) {
                        ArrayList<DocInfoModel> list = new ArrayList<DocInfoModel>(1);
                        for (int i = 0; i < selectedRows.length; i++) {
                            DocInfoModel obj = (DocInfoModel) tableModel.getObject(selectedRows[i]);
                            if (obj != null) {
                                list.add(obj);
                            }
                        }
                        DocInfoModel[] selected = list.toArray(new DocInfoModel[list.size()]);
                        if (selected != null && selected.length > 0) {
                            setSelectedHistories(selected);
                        } else {
                            setSelectedHistories((DocInfoModel[]) null);
                        }
                    }
                }
            }
        });

        // 文書種別変更
        contentCombo.addItemListener(EventHandler.create(ItemListener.class, this, "contentChanged", "stateChange"));

        // 抽出期間コンボボックスの選択を処理する
        extractionCombo.addItemListener(EventHandler.create(ItemListener.class, this, "periodChanged", "stateChange"));

        // Preference から文書種別を設定する
        extractionContent = IInfoModel.DOCTYPE_KARTE;

        // Preference から抽出期間を設定する
        int past = Project.getPreferences().getInt(Project.DOC_HISTORY_PERIOD, -12);
        int index = NameValuePair.getIndex(String.valueOf(past), extractionObjects);
        extractionCombo.setSelectedIndex(index);
        GregorianCalendar today = new GregorianCalendar();
        today.add(GregorianCalendar.MONTH, past);
        today.clear(Calendar.HOUR_OF_DAY);
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MILLISECOND);
        setExtractionPeriod(today.getTime());

        // Preference から自動文書取得数を設定する
        autoFetchCount = Project.getPreferences().getInt(Project.DOC_HISTORY_FETCHCOUNT, 1);

        // Preference から昇順降順を設定する
        ascending = Project.getPreferences().getBoolean(Project.DIAGNOSIS_ASCENDING, false);

        // Preference から修正履歴表示を設定する
        showModified = Project.getPreferences().getBoolean(Project.DOC_HISTORY_SHOWMODIFIED, false);

        // 文書履歴テーブルのキーボード入力をブロックするリスナ
        blockKeyListener = new BlockKeyListener();
    }

    /**
     * キーボード入力をブロックするリスナクラス。
     */
    private class BlockKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            e.consume();
        }
        @Override
        public void keyPressed(KeyEvent e) {
            e.consume();
        }
        @Override
        public void keyReleased(KeyEvent e) {
            e.consume();
        }
    }

    /**
     * 検索パラメータの文書タイプを設定する。。
     * @param extractionContent 文書タイプ
     */
    public void setExtractionContent(String extractionContent) {
        String old = this.extractionContent;
        this.extractionContent = extractionContent;
        // -> DocumetnBridge#propertyChange
        boundSupport.firePropertyChange(DOCUMENT_TYPE, old, this.extractionContent);
        getDocumentHistory();
    }

    /**
     * 検索パラメータの文書タイプを返す。
     * @return 文書タイプ
     */
    public String getExtractionContent() {
        return extractionContent;
    }

    /**
     * 検索パラメータの抽出期間を設定する。
     * @param extractionPeriod 抽出期間
     */
    public void setExtractionPeriod(Date extractionPeriod) {
        this.extractionPeriod = extractionPeriod;
        getDocumentHistory();
    }

    /**
     * 検索パラメータの抽出期間を返す。
     * @return 抽出期間
     */
    public Date getExtractionPeriod() {
        return extractionPeriod;
    }

    /**
     * 文書履歴表示の昇順/降順を設定する。
     * @param ascending 昇順の時 true
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
        getDocumentHistory();
    }

    /**
     * 文書履歴表示の昇順/降順を返す。
     * @return 昇順の時 true
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * 修正版を表示するかどうかを設定する。
     * @param showModifyed 表示する時 true
     */
    public void setShowModified(boolean showModifyed) {
        this.showModified = showModifyed;
        getDocumentHistory();
    }

    /**
     * 修正版を表示するかどうかを返す。
     * @return 表示する時 true
     */
    public boolean isShowModified() {
        return showModified;
    }

    /**
     * 検索タスク。
     */
    private class DocInfoTask extends DBTask<List<DocInfoModel>> {

        // Delegator
        private DocumentDelegater ddl;
        // 検索パラメータを保持するオブジェクト
        private DocumentSearchSpec spec;

        public DocInfoTask(Chart ctx, DocumentSearchSpec spec, DocumentDelegater ddl) {
            super(ctx);
            this.spec = spec;
            this.ddl = ddl;
        }

        @Override
        protected List<DocInfoModel> doInBackground() {
            //logger.debug("DocInfoTask started");
            List<DocInfoModel> result = (List<DocInfoModel>) ddl.getDocInfoList(spec);
            if (ddl.isNoError()) {
                return result;
            } else {
                return null;
            }
        }

        @Override
        protected void succeeded(List<DocInfoModel> result) {
            //logger.debug("DocInfoTask succeeded");
            if (result != null) {
                updateHistory(result);
            }
        }
    }

    /**
     * タイトル変更タスククラス。
     */
    private class ChangeTitleTask extends DBTask<Boolean> {

        // DocInfo
        private DocInfoModel docInfo;
        // Delegator
        private DocumentDelegater ddl;

        public ChangeTitleTask(Chart ctx, DocInfoModel docInfo, DocumentDelegater ddl) {
            super(ctx);
            this.docInfo = docInfo;
            this.ddl = ddl;
        }

        @Override
        protected Boolean doInBackground() {
            //logger.debug("ChangeTitleTask started");
            ddl.updateTitle(docInfo);
            return ddl.isNoError();
        }

        @Override
        protected void succeeded(Boolean result) {
            //logger.debug("ChangeTitleTask succeeded");
        }
    }
}

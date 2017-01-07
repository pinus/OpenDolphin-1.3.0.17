package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.inspector.DocumentHistory;
import open.dolphin.ui.MyJScrollPane;

/**
 * 参照タブ画面を提供する Bridge クラス.
 * ChartImpl の ChartDocument plugin として登録されるのはこれ.
 * このクラスの scroller へカルテが表示される.
 *
 * @author kazushi Minagawa, Digital Globe, Inc.
 */
public class DocumentBridgeImpl extends AbstractChartDocument implements PropertyChangeListener {

    private static final String TITLE = "参 照";

    // 文書表示クラスのインターフェイス
    private KarteDocumentViewer karteViewer;
    // Scroller
    private MyJScrollPane scroller;
    // 何も文書がないときは blank panel を出す
    private JPanel blankPanel;
    // エディタで編集した直後に呼ばれた場合，その日付を入れる
    private String editDate;

    public DocumentBridgeImpl() {
        initComponents();
    }

    private void initComponents() {
        setTitle(TITLE);

        // blankLabel をダブルクリックしたら，新規カルテ作成を呼ぶ
        blankPanel = new JPanel();
        blankPanel.setBackground(Color.white);
        blankPanel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Chart context = getContext();
                    if (context instanceof ChartImpl) { ((ChartImpl)context).newKarte(); }
                }
            }
        });
    }

    @Override
    public void start() {
        scroller = new MyJScrollPane();
        getUI().setLayout(new BorderLayout());
        getUI().add(scroller, BorderLayout.CENTER);

        // スクロールバーを常に表示しないと，スクロールバーが表示されるときにカルテがスクロールバー分伸びて尻切れになることがある
        scroller.setVerticalScrollBarPolicy(MyJScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 文書履歴のプロパティ通知をリッスンする
        DocumentHistory h = getContext().getDocumentHistory();
        h.addPropertyChangeListener(DocumentHistory.DOCUMENT_TYPE, this);
        h.addPropertyChangeListener(DocumentHistory.HITORY_UPDATED, this);
        h.addPropertyChangeListener(DocumentHistory.SELECTED_HISTORIES, this);

        karteViewer = new KarteDocumentViewer();
        karteViewer.setContext(getContext());
        karteViewer.start();
    }

    @Override
    public void stop() {
        if (karteViewer != null) {
            karteViewer.stop();
        }
    }

    @Override
    public void enter() {
        if (karteViewer != null) {
            // これによりメニューは viewer で制御される
            karteViewer.enter();
        } else {
            super.enter();
        }
    }

    /**
     * Bridge 機能を提供する.
     * KarteDocumentViewer に scroller を渡して表示してもらう.
     * @param docs 表示する文書の DocInfo 配列
     */
    public void showDocuments(DocInfoModel[] docs) {
        if (docs == null || docs.length == 0) { return; }

        if (karteViewer != null) {
            karteViewer.showDocuments(docs, scroller);
        }
    }

    /**
     * DocumentHistory から呼ばれる.
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        switch (evt.getPropertyName()) {
            case DocumentHistory.DOCUMENT_TYPE:
                // 使わない
                break;

            case DocumentHistory.HITORY_UPDATED:
                // 編集直後に来た場合は，編集カルテの editDate が入っている
                editDate = (String) evt.getNewValue();
                // 文書履歴の抽出期間が変更された場合
                if (karteViewer != null) {
                    karteViewer.historyPeriodChanged();
                }   // null の代わりに blankPanel を出す
                scroller.setViewportView(blankPanel);
                break;

            case DocumentHistory.SELECTED_HISTORIES:
                // TODO 編集直後に来た場合は，できれば編集直後のカルテを viewwer で表示するようにしたい
                if (editDate != null) {
                    SwingUtilities.invokeLater(() -> scroller.getViewport().setViewPosition(new java.awt.Point(0,0)));
                    editDate = null;
                }
                // 文書履歴の選択が変更された場合
                DocInfoModel[] selectedHistories = (DocInfoModel[]) evt.getNewValue();
                showDocuments(selectedHistories);
                break;

            default:
                break;
        }
    }

    public KarteViewer2 getBaseKarte() {
        if (karteViewer != null && karteViewer instanceof KarteDocumentViewer) {
            return karteViewer.getBaseKarte();
        }
        return null;
    }
}

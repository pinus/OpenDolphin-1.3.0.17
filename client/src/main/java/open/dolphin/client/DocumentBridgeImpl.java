package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.inspector.DocumentHistory;
import open.dolphin.ui.MyJScrollPane;

/**
 * 参照タブ画面を提供する Bridge クラス。このクラスの scroller へ
 * カルテ、紹介状等のどきゅめんとが表示される。
 *
 * @author kazushi Minagawa, Digital Globe, Inc.
 */
public class DocumentBridgeImpl extends AbstractChartDocument
    implements PropertyChangeListener, DocumentBridger {

    private static final String TITLE = "参 照";

    // 文書表示クラスのインターフェイス
    private DocumentViewer curViwer;
    // Scroller
    private JScrollPane scroller;
    // 何も文書がないときは blank JLabel を出す
    private JPanel blankPanel;
    // エディタで編集した直後に呼ばれた場合，その日付を入れる
    private String editDate;

    public DocumentBridgeImpl() {
        setTitle(TITLE);

        // blankLabel をダブルクリックしたら，新規カルテ作成を呼ぶ
        blankPanel = new JPanel();
        blankPanel.setBackground(Color.white);
        blankPanel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Object context = getContext();
                    if (context instanceof ChartImpl) { ((ChartImpl)context).newKarte(); }
                }
            }
        });
    }

    @Override
    public void start() {

        //scroller = new KarteScrollPane();
        scroller = new MyJScrollPane();
        getUI().setLayout(new BorderLayout());
        getUI().add(scroller, BorderLayout.CENTER);

        // スクロールバーを常に表示しないと，スクロールバーが表示されるときにカルテがスクロールバー分伸びて尻切れになることがある
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 文書履歴のプロパティ通知をリッスンする
        DocumentHistory h = getContext().getDocumentHistory();
        h.addPropertyChangeListener(DocumentHistory.DOCUMENT_TYPE, this);
        h.addPropertyChangeListener(DocumentHistory.HITORY_UPDATED, this);
        h.addPropertyChangeListener(DocumentHistory.SELECTED_HISTORIES, this);

        curViwer = new KarteDocumentViewer();
        curViwer.setContext(getContext());
        curViwer.start();

        //enter();
    }

    @Override
    public void stop() {
        if (curViwer != null) {
            curViwer.stop();
        }
    }

    @Override
    public void enter() {
        if (curViwer != null) {
            // これによりメニューは viwer で制御される
            curViwer.enter();
        } else {
            super.enter();
        }
    }

    /**
     * Bridge 機能を提供する。選択された文書のタイプに応じてビューへブリッジする。
     * @param docs 表示する文書の DocInfo 配列
     */
    @Override
    public void showDocuments(DocInfoModel[] docs) {

        if (docs == null || docs.length == 0) {
            return;
        }

        if (curViwer != null) {
            //getContext().showDocument(0);
            curViwer.showDocuments(docs, scroller);
            //getContext().showDocument(0);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        ChartMediator med = this.getContext().getChartMediator();
        med.setCurKarteComposit(null);
        String prop = evt.getPropertyName();

        if (prop.equals(DocumentHistory.DOCUMENT_TYPE)) {

            String docType = (String) evt.getNewValue();

            if (docType.equals(IInfoModel.DOCTYPE_LETTER)) {
                // curViwer = new LetterViewer();
            } else {
                curViwer = new KarteDocumentViewer();
            }

            curViwer.setContext(getContext());
            curViwer.start();

        } else if (prop.equals(DocumentHistory.HITORY_UPDATED)) {
            // 編集直後に来た場合は，編集カルテの editDate が入っている
            editDate = (String) evt.getNewValue();

            // 文書履歴の抽出期間が変更された場合
            if (curViwer != null) {
                curViwer.historyPeriodChanged();
            }
            // null の代わりに blankPanel を出す
            // this.scroller.setViewportView(null);
            this.scroller.setViewportView(blankPanel);

        } else if (prop.equals(DocumentHistory.SELECTED_HISTORIES)) {

            // TODO 編集直後に来た場合は，できれば編集直後のカルテを viewwer で表示するようにしたい
            if (editDate != null) {
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        scroller.getViewport().setViewPosition(new java.awt.Point(0,0));
                    }
                });
                editDate = null;
            }

            // 文書履歴の選択が変更された場合
            DocInfoModel[] selectedHistories = (DocInfoModel[]) evt.getNewValue();
            this.showDocuments(selectedHistories);
        }
    }

    public KarteViewer getBaseKarte() {
        if (curViwer != null && curViwer instanceof KarteDocumentViewer) {
            return ((KarteDocumentViewer) curViwer).getBaseKarte();
        }
        return null;
    }
}

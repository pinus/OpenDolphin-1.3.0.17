package open.dolphin.inspector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.ClientContext;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.project.Project;
import open.dolphin.ui.ExecuteScript;
import open.dolphin.ui.HorizontalPanel;
import open.dolphin.ui.PNSBadgeTabbedPane;

public class PatientInspector {
    public static final String[] DEFAULT_INSPECTOR = new String[]{
        "メモ", "カレンダー", "文書履歴", "アレルギー", "身長体重", "病名", "関連文書", "なし"
    };

    // 個々のインスペクタ
    // 患者基本情報
    private BasicInfoInspector basicInfoInspector;
    // 来院歴
    private PatientVisitInspector patientVisitInspector;
    // 患者メモ
    private MemoInspector memoInspector;
    // 文書履歴
    private DocumentHistory docHistory;
    // アレルギ
    private AllergyInspector allergyInspector;
    // 身長体重
    private PhysicalInspector physicalInspector;
    // 病名インスペクタ
    private DiagnosisInspector diagnosisInspector;
    // 関連文書インスペクタ
    private FileInspector fileInspector;

    // インスペクタを格納するタブペイン
    private PNSBadgeTabbedPane tabbedPane;

    // このクラスのコンテナパネル
    private JPanel container;

    // Context このインスペクタの親コンテキスト
    private ChartImpl context;

    // 優先される 5つに入ったかどうか. これが false ならタブに格納.
    private boolean bMemo;
    private boolean bAllergy;
    private boolean bPhysical;
    private boolean bCalendar;
    private boolean bDiagnosis;
    private boolean bFile;

    /**
     * 患者インスペクタクラスを生成する.
     * @param context インスペクタの親コンテキスト
     */
    public PatientInspector(ChartImpl context) {
        // このインスペクタが格納される Chart Object
        setContext(context);
        // GUI を初期化する
        initComponents();
    }

    private void initComponents() {
        String memoTitle = "メモ";
        String pvtTitle = "来院歴"; // カレンダー
        String docHistoryTitle = "文書履歴";
        String allergyTitle = "アレルギー";
        String physicalTitle = "身長体重";
        String diagnosisTitle = "病名";
        String fileTitle = "関連文書";

        // Preference に保存されているインスペクタの順番
        String topInspector = Project.getPreferences().get("topInspector", DEFAULT_INSPECTOR[0]); //0"メモ"
        String secondInspector = Project.getPreferences().get("secondInspector", DEFAULT_INSPECTOR[5]); //5"病名"
        String thirdInspector = Project.getPreferences().get("thirdInspector", DEFAULT_INSPECTOR[1]); //1"カレンダ"
        String forthInspector = Project.getPreferences().get("forthInspector", DEFAULT_INSPECTOR[2]); //2"文書履歴"
        String fifthInspector = Project.getPreferences().get("fifthInspector", DEFAULT_INSPECTOR[3]); //3"アレルギー"

        // 各インスペクタを生成する
        basicInfoInspector = new BasicInfoInspector(context);
        memoInspector = new MemoInspector(context);
        patientVisitInspector = new PatientVisitInspector(context);
        docHistory = new DocumentHistory(getContext());
        allergyInspector = new AllergyInspector(context);
        physicalInspector = new PhysicalInspector(context);
        diagnosisInspector = new DiagnosisInspector(context);
        fileInspector = new FileInspector(context);

        // タブパネル
        tabbedPane = new PNSBadgeTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        // docHistory は必ずタブに入れる
        tabbedPane.addTab(docHistoryTitle, docHistory.getPanel());

        // インスペクタのサイズ調整
        int prefW = 260;
        int prefW2 = 260;
        int prefH = 178;
        if (ClientContext.isMac()) { prefW2 += 20; prefH = 175; }

        basicInfoInspector.getPanel().setPreferredSize(new Dimension(prefW2, 42));
        basicInfoInspector.getPanel().setMaximumSize(new Dimension(prefW2, 42));
        basicInfoInspector.getPanel().setMinimumSize(new Dimension(prefW2, 42));
        allergyInspector.getPanel().setPreferredSize(new Dimension(prefW, 110));
        physicalInspector.getPanel().setPreferredSize(new Dimension(prefW, 110));

        //サイズ微調整
        docHistory.getPanel().setPreferredSize(new Dimension(prefW, 350));
        docHistory.getPanel().setMinimumSize(new Dimension(prefW, 350));
        patientVisitInspector.getPanel().setPreferredSize(new Dimension(prefW, prefH));
        patientVisitInspector.getPanel().setMinimumSize(new Dimension(prefW, prefH));
        patientVisitInspector.getPanel().setMaximumSize(new Dimension(1024, prefH));
        memoInspector.getPanel().setMinimumSize(new Dimension(prefW, 70));
        memoInspector.getPanel().setPreferredSize(new Dimension(prefW, 100));

        diagnosisInspector.getPanel().setPreferredSize(new Dimension(prefW, 100));
        diagnosisInspector.getPanel().setMinimumSize(new Dimension(prefW, 100));
        fileInspector.getPanel().setPreferredSize(new Dimension(prefW, 100));

        // 全体の container
        container = new HorizontalPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // 左側のレイアウトを行う
        layoutRow(container, topInspector);
        layoutRow(container, secondInspector);
        layoutRow(container, thirdInspector);
        layoutRow(container, forthInspector);
        layoutRow(container, fifthInspector);

        // 左側にレイアウトされなかったものをタブに格納する
        if (!bMemo) {
            tabbedPane.addTab(memoTitle, memoInspector.getPanel());
        }

        if (!bCalendar) {
            tabbedPane.addTab(pvtTitle, patientVisitInspector.getPanel());
        }

        if (!bAllergy) {
            tabbedPane.addTab(allergyTitle, allergyInspector.getPanel());
        }

        if (!bPhysical) {
            tabbedPane.addTab(physicalTitle, physicalInspector.getPanel());
        }

        if (!bDiagnosis) {
            tabbedPane.addTab(diagnosisTitle, diagnosisInspector.getPanel());
        }
        if (!bFile) {
            tabbedPane.addTab(fileTitle, fileInspector.getPanel());
        }

        // BadgeListener
        if (!bFile) {
            fileInspector.addBadgeListener(tabbedPane::setBadge, tabbedPane.getTabCount()-1);
        }
        fileInspector.update();

        Dimension d = container.getMinimumSize();
        d.width = prefW;
        container.setMinimumSize(d);
    }

    /**
     * content に inspector をレイアウトする.
     * @param content
     * @param itype
     */
    private void layoutRow(JPanel content, String itype) {

        if (itype.equals(DEFAULT_INSPECTOR[0])) { //"メモ"

            // もし関連文書(/Volumes/documents/${患者id}）があれば，メモタイトルを変える
            final String path = FileInspector.getDocumentPath(context.getKarte().getPatient().getPatientId());
            File infoFolder = new File (path);

            // jpeg ファイルフィルタ
            FileFilter ffJpg = file -> file.getName().toLowerCase().endsWith(".jpg");
            // 検査 ファイルフィルタ
            FileFilter ffExam = file -> file.getName().contains("検査");
            // 添書 ファイルフィルタ
            FileFilter ffLetter = file -> file.getName().contains("紹介") | file.getName().contains("返事") | file.getName().contains("手紙");
            // 代替処方 ファイルフィルタ
            FileFilter ffAltDrug = file -> file.getName().contains("代替");

            StringBuilder memoTitle = new StringBuilder();
            Color mColor = null;
            Font mFont = null;

            // 情報ファイルのフォルダがあるかどうか
            if (infoFolder.exists()) {
                boolean miscellaneous = true;

                if (infoFolder.listFiles(ffJpg).length > 0) {
                    if (!memoTitle.toString().equals("")) { memoTitle.append("・"); }
                    memoTitle.append("写真");
                    miscellaneous = false;
                }
                if (infoFolder.listFiles(ffExam).length > 0) {
                    if (!memoTitle.toString().equals("")) { memoTitle.append("・"); }
                    memoTitle.append("検査");
                    miscellaneous = false;
                }
                if (infoFolder.listFiles(ffLetter).length > 0) {
                    if (!memoTitle.toString().equals("")) { memoTitle.append("・"); }
                    memoTitle.append("添書");
                    miscellaneous = false;
                }
                if (infoFolder.listFiles(ffAltDrug).length > 0) {
                    if (!memoTitle.toString().equals("")) { memoTitle.append("・"); }
                    memoTitle.append("代替報告");
                    miscellaneous = false;
                }
                if (miscellaneous) {
                    if (!memoTitle.toString().equals("")) { memoTitle.append("・"); }
                    memoTitle.append("ファイル");
                }

                memoTitle.append("あり");
                if (mColor == null) { mColor = Color.blue; }
                if (mFont == null) { mFont = new Font(Font.SANS_SERIF,Font.BOLD,12); }

                // kick AppleScript to open target folder
                memoInspector.getPanel().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ExecuteScript.openPatientFolder(path);
                    }
                });
            } else {
                // フォルダがない
                if (memoTitle.toString().equals("")) {
                    memoTitle.append("メモ");
                    mColor = Color.black;
                }
            }

            memoInspector.getPanel().setBorder(BorderFactory.createTitledBorder(
                null, memoTitle.toString(), TitledBorder.LEFT, TitledBorder.TOP, mFont, mColor));

            content.add(memoInspector.getPanel());
            bMemo = true;

        } else if (itype.equals(DEFAULT_INSPECTOR[1])) { //"カレンダ"
            patientVisitInspector.getPanel().setBorder(BorderFactory.createTitledBorder("来院歴"));
            content.add(patientVisitInspector.getPanel());
            bCalendar = true;

        } else if (itype.equals(DEFAULT_INSPECTOR[2])) { //"文書履歴"
            content.add(tabbedPane);

        } else if (itype.startsWith(DEFAULT_INSPECTOR[3])) { //"アレルギ"
            allergyInspector.getPanel().setBorder(BorderFactory.createTitledBorder("アレルギー"));
            content.add(allergyInspector.getPanel());
            bAllergy = true;

        } else if (itype.equals(DEFAULT_INSPECTOR[4])) { // "身長体重"
            physicalInspector.getPanel().setBorder(BorderFactory.createTitledBorder("身長体重"));
            content.add(physicalInspector.getPanel());
            bPhysical = true;
        }

        else if (itype.equals(DEFAULT_INSPECTOR[5])) { // "病名"
            diagnosisInspector.getPanel().setBorder(BorderFactory.createTitledBorder("病名"));
            content.add(diagnosisInspector.getPanel());
            bDiagnosis = true;

        } else if (itype.equals(DEFAULT_INSPECTOR[6])) { // "関連文書"
            fileInspector.getPanel().setBorder(BorderFactory.createTitledBorder("関連文書"));
            content.add(fileInspector.getPanel());
            bFile = true;
        }
    }

    /**
     * コンテキストを返す.
     * @return
     */
    public ChartImpl getContext() {
        return context;
    }

    /**
     * コンテキストを設定する.
     */
    private void setContext(ChartImpl context) {
        this.context = context;
    }

    /**
     * 患者カルテを返す.
     * @return  患者カルテ
     */
    public KarteBean getKarte() {
        return context.getKarte();
    }

    /**
     * 患者を返す.
     * @return 患者
     */
    public PatientModel getPatient() {
        return context.getKarte().getPatient();
    }

    /**
     * 基本情報インスペクタを返す.
     * @return 基本情報インスペクタ
     */
    public BasicInfoInspector getBasicInfoInspector() {
        return basicInfoInspector;
    }

    /**
     * 来院歴インスペクタを返す.
     * @return 来院歴インスペクタ
     */
    public PatientVisitInspector getPatientVisitInspector() {
        return patientVisitInspector;
    }

    /**
     * 患者メモインスペクタを返す.
     * @return 患者メモインスペクタ
     */
    public MemoInspector getMemoInspector() {
        return memoInspector;
    }

    /**
     * 文書履歴インスペクタを返す.
     * @return 文書履歴インスペクタ
     */
    public DocumentHistory getDocumentHistory() {
        return docHistory;
    }

    /**
     * 病名インスペクタを返す.
     * @return
     */
    public DiagnosisInspector getDiagnosisInspector() {
        return diagnosisInspector;
    }

    /**
     * 病名インスペクタを返す.
     * @return
     */
    public FileInspector getFileInspector() {
        return fileInspector;
    }

    /**
     * レイアウトのためにインスペクタのコンテナパネルを返す.
     * @return インスペクタのコンテナパネル
     */
    public JPanel getPanel() {
        return container;
    }

    /**
     * 終了処理.
     */
    public void dispose() {
        // List をクリアする
        docHistory.clear();
        allergyInspector.clear();
        physicalInspector.clear();

        // memo 欄の自動セーブ
        memoInspector.updateMemo();
    }
}

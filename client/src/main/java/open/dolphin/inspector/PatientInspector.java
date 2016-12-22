package open.dolphin.inspector;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.ClientContext;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.project.Project;
import open.dolphin.ui.ExecuteScript;
import open.dolphin.ui.PNSTabbedPane;

public class PatientInspector {

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

    // インスペクタを格納するタブペイン View
    private PNSTabbedPane tabbedPane;

    // このクラスのコンテナパネル View
    private JPanel container;

    // Context このインスペクタの親コンテキスト
    private ChartImpl context;

    private boolean bMemo;
    private boolean bAllergy;
    private boolean bPhysical;
    private boolean bCalendar;
    private boolean bDiagnosis;
    private boolean bFile;

    public static String[] INSPECTOR_ITEMS = new String[]{
        "メモ", "カレンダー", "文書履歴", "アレルギー", "身長体重", "病名", "関連文書", "なし"
    };

    protected static final String DEFAULT_DOCUMENT_FOLDER = "/Volumes/documents/";

    // 病名インスペクタ
    private DiagnosisInspector diagnosisInspector;
    public DiagnosisInspector getDiagnosisInspector() {
        return diagnosisInspector;
    }
    // 関連文書インスペクタ
    private FileInspector fileInspector;
    public FileInspector getFileInspector() {
        return fileInspector;
    }

    /**
     * 患者インスペクタクラスを生成する.
     *
     * @param context インスペクタの親コンテキスト
     */
    public PatientInspector(ChartImpl context) {

        // このインスペクタが格納される Chart Object
        setContext(context);

        // GUI を初期化する
        initComponents();
    }

    public void dispose() {
        // List をクリアする
        docHistory.clear();
        allergyInspector.clear();
        physicalInspector.clear();

        // memo 欄の自動セーブ
        memoInspector.updateMemo();
    }

    /**
     * コンテキストを返す.
     */
    public ChartImpl getContext() {
        return context;
    }

    /**
     * コンテキストを設定する.
     */
    public void setContext(ChartImpl context) {
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
     * レイアウトのためにインスペクタのコンテナパネルを返す.
     * @return インスペクタのコンテナパネル
     */
    public JPanel getPanel() {
        return container;
    }


    private void initComponents() {

        // 来院歴
        String pvtTitle = ClientContext.getString("patientInspector.pvt.title");

        // 文書履歴
        String docHistoryTitle = ClientContext.getString("patientInspector.docHistory.title");

        // アレルギ
        String allergyTitle = ClientContext.getString("patientInspector.allergy.title");

        // 身長体重
        String physicalTitle = ClientContext.getString("patientInspector.physical.title");

        // メモ
        String memoTitle = ClientContext.getString("patientInspector.memo.title");

        String diagnosisTitle = "病名";
        String fileTitle = "関連文書";

        String topInspector = Project.getPreferences().get("topInspector", INSPECTOR_ITEMS[0]); //0"メモ"
        String secondInspector = Project.getPreferences().get("secondInspector", INSPECTOR_ITEMS[5]); //5"病名"
        String thirdInspector = Project.getPreferences().get("thirdInspector", INSPECTOR_ITEMS[1]); //1"カレンダ"
        String forthInspector = Project.getPreferences().get("forthInspector", INSPECTOR_ITEMS[2]); //2"文書履歴"
        String fifthInspector = Project.getPreferences().get("fifthInspector", INSPECTOR_ITEMS[3]); //3"アレルギー"

        // 各インスペクタを生成する
        basicInfoInspector = new BasicInfoInspector(context);
        patientVisitInspector = new PatientVisitInspector(context);
        memoInspector = new MemoInspector(context);
        docHistory = new DocumentHistory(getContext());
        allergyInspector = new AllergyInspector(context);
        physicalInspector = new PhysicalInspector(context);
        diagnosisInspector = new DiagnosisInspector(context);
        fileInspector = new FileInspector(context);

        // タブパネルへ格納する(文書履歴，健康保険，アレルギ，身長体重はタブパネルで切り替え表示する)
        //tabbedPane = new MyJTabbedPane(new Insets(5,5,5,5));
        tabbedPane = new PNSTabbedPane();
        tabbedPane.setBorder(BorderFactory.createTitledBorder(""));

        tabbedPane.addTab(docHistoryTitle, docHistory.getPanel());

        int prefW = 260;
        int prefW2 = 260;
        int prefH = 178;
        if (ClientContext.isMac()) {
            prefW2 += 20;
            prefH = 175;
        }
        basicInfoInspector.getPanel().setPreferredSize(new Dimension(prefW2, 42));
        basicInfoInspector.getPanel().setMaximumSize(new Dimension(prefW2, 42));
        basicInfoInspector.getPanel().setMinimumSize(new Dimension(prefW2, 42));
        //memoInspector.getPanel().setPreferredSize(new Dimension(prefW, 70));
        allergyInspector.getPanel().setPreferredSize(new Dimension(prefW, 110));
        // docHistory.getPanel().setPreferredSize(new Dimension(prefW, 280));
        physicalInspector.getPanel().setPreferredSize(new Dimension(prefW, 110));
        //int prefH = patientVisitInspector.getPanel().getPreferredSize().height;
        //patientVisitInspector.getPanel().setPreferredSize(new Dimension(prefW, prefH));

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

        container = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g = (Graphics2D) graphics.create();
                int w = getWidth();
                int h = getHeight();

                if (SwingUtilities.getWindowAncestor(this).isFocused()) g.setColor(Color.BLACK);
                else g.setColor(Color.WHITE);

                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g.drawLine(0, 0, w-1, 0);
                g.drawLine(w-1, 0, w-1, h);

                g.dispose();
            }
        };
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

        Dimension d = container.getMinimumSize();
        d.width = prefW;
        container.setMinimumSize(d);
    }

    private void layoutRow(JPanel content, String itype) {

        if (itype.equals(INSPECTOR_ITEMS[0])) { //"メモ"

            // もし関連文書(/Volumes/documents/${患者id}）があれば，メモタイトルを変える
            //final String path = DEFAULT_DOCUMENT_FOLDER + context.getKarte().getPatient().getPatientId();
            final String path = FileInspector.getDocumentPath(context.getKarte().getPatient().getPatientId());

            File infoFolder = new File (path);
            // jpeg ファイルフィルタ
            FileFilter ffJpg = new FileFilter() {
                @Override
                public boolean accept (File file) {
                    return file.getName().toLowerCase().endsWith(".jpg");
                }
            };
            // 検査 ファイルフィルタ
            FileFilter ffExam = new FileFilter() {
                @Override
                public boolean accept (File file) {
                    return file.getName().contains("検査");
                }
            };

            // 添書 ファイルフィルタ
            FileFilter ffLetter = new FileFilter() {
                @Override
                public boolean accept (File file) {
                    return file.getName().contains("紹介") |
                           file.getName().contains("返事") |
                           file.getName().contains("手紙");
                }
            };

            // 添書 ファイルフィルタ
            FileFilter ffAltDrug = new FileFilter() {
                @Override
                public boolean accept (File file) {
                    return file.getName().contains("代替");
                }
            };

            StringBuilder mTitle = new StringBuilder();
            Color mColor = null;
            Font mFont = null;

            // メモに禁忌情報があるかどうか
            // if (memoInspector.containsContraindication()) {
            //    mTitle.append("■■禁忌あり■■");
            //    mColor = Color.red;
            //    mFont = new Font(Font.SANS_SERIF,Font.BOLD,12);
            // }

            // 情報ファイルのフォルダがあるかどうか
            if (infoFolder.exists()) {
                boolean miscellaneous = true;

                if (infoFolder.listFiles(ffJpg).length > 0) {
                    if (!mTitle.toString().equals("")) mTitle.append("・");
                    mTitle.append("写真");
                    miscellaneous = false;
                }
                if (infoFolder.listFiles(ffExam).length > 0) {
                    if (!mTitle.toString().equals("")) mTitle.append("・");
                    mTitle.append("検査");
                    miscellaneous = false;
                }
                if (infoFolder.listFiles(ffLetter).length > 0) {
                    if (!mTitle.toString().equals("")) mTitle.append("・");
                    mTitle.append("添書");
                    miscellaneous = false;
                }
                if (infoFolder.listFiles(ffAltDrug).length > 0) {
                    if (!mTitle.toString().equals("")) mTitle.append("・");
                    mTitle.append("代替報告");
                    miscellaneous = false;
                }
                if (miscellaneous) {
                    if (!mTitle.toString().equals("")) mTitle.append("・");
                    mTitle.append("ファイル");
                }

                mTitle.append("あり");
                if (mColor == null) mColor = Color.blue;
                if (mFont == null) mFont = new Font(Font.SANS_SERIF,Font.BOLD,12);

                // kick AppleScript to open target folder
                memoInspector.getPanel().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ExecuteScript.openPatientFolder(path);
                    }
                });
            } else {
                // フォルダがない
                if (mTitle.toString().equals("")) {
                    mTitle.append("メモ");
                    mColor = Color.black;
                }
            }

            memoInspector.getPanel().setBorder(BorderFactory.createTitledBorder(
                null,
                mTitle.toString(),
                TitledBorder.LEFT,
                TitledBorder.TOP,
                mFont,
                mColor));
//pns$
            content.add(memoInspector.getPanel());
            bMemo = true;

        } else if (itype.equals(INSPECTOR_ITEMS[1])) { //"カレンダ"
            patientVisitInspector.getPanel().setBorder(BorderFactory.createTitledBorder("来院歴"));
            content.add(patientVisitInspector.getPanel());
            bCalendar = true;

        } else if (itype.equals(INSPECTOR_ITEMS[2])) { //"文書履歴"
            content.add(tabbedPane);

        } else if (itype.startsWith(INSPECTOR_ITEMS[3])) { //"アレルギ"
            allergyInspector.getPanel().setBorder(BorderFactory.createTitledBorder("アレルギー"));
            content.add(allergyInspector.getPanel());
            bAllergy = true;

        } else if (itype.equals(INSPECTOR_ITEMS[4])) { // "身長体重"
            physicalInspector.getPanel().setBorder(BorderFactory.createTitledBorder("身長体重"));
            content.add(physicalInspector.getPanel());
            bPhysical = true;
        }
//pns^
        else if (itype.equals(INSPECTOR_ITEMS[5])) { // "病名"
            diagnosisInspector.getPanel().setBorder(BorderFactory.createTitledBorder("病名"));
            content.add(diagnosisInspector.getPanel());
            bDiagnosis = true;

        } else if (itype.equals(INSPECTOR_ITEMS[6])) { // "関連文書"
            fileInspector.getPanel().setBorder(BorderFactory.createTitledBorder("関連文書"));
            content.add(fileInspector.getPanel());
            bFile = true;
        }
//pns$
    }
}




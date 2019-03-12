package open.dolphin.inspector;

import open.dolphin.client.ChartImpl;
import open.dolphin.client.ClientContext;
import open.dolphin.project.Project;
import open.dolphin.ui.HorizontalPanel;
import open.dolphin.ui.PNSBadgeTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 各々の Inspecter を生成して配置する.
 * @author kazm
 * @author pns
 */
public class PatientInspector {

    public static int TOP_STRUT = 3;
    // 個々のインスペクタ
    // 患者基本情報 (固定インスペクタ)
    private BasicInfoInspector basicInfoInspector;
    // 全インスペクタを入れる map
    private final HashMap<String, IInspector> inspectorMap = new HashMap<>();
    // DocumentHistory インスペクタを格納するタブペイン. ６個目以降のインスペクタはここに追加される.
    private PNSBadgeTabbedPane tabbedPane;
    // このクラスのコンテナパネル
    private JPanel container;
    // Context このインスペクタの親コンテキスト
    private final ChartImpl context;

    /**
     * 患者インスペクタクラスを生成する.
     * @param chart インスペクタの親コンテキスト
     */
    public PatientInspector(ChartImpl chart) {
        context = chart;
        initComponents();
    }

    private void initComponents() {

        // Preference に保存されているインスペクタの順番を読み込む.
        // 文書履歴は必ずこの中に入っている.
        String[] prefName = {
            Project.getPreferences().get("topInspector", InspectorCategory.メモ.name()),        //0"メモ"
            Project.getPreferences().get("secondInspector", InspectorCategory.病名.name()),     //5"病名"
            Project.getPreferences().get("thirdInspector", InspectorCategory.カレンダー.name()), //1"カレンダ"
            Project.getPreferences().get("forthInspector", InspectorCategory.文書履歴.name()),   //2"文書履歴"
            Project.getPreferences().get("fifthInspector", InspectorCategory.アレルギー.name()),  //3"アレルギー"
        };

        // タブパネル
        tabbedPane = new PNSBadgeTabbedPane();
        if (ClientContext.isWin()) { tabbedPane.setButtonPanelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10)); }
        tabbedPane.setBorder(new InspectorBorder(null));
        tabbedPane.setButtonPanelBackground(IInspector.BACKGROUND);

        // 全体の container
        container = new HorizontalPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // 固定インスペクタ
        basicInfoInspector = new BasicInfoInspector(PatientInspector.this);
        inspectorMap.put(basicInfoInspector.getName(), basicInfoInspector);

        // Preference に記録されたインスペクタを入れる配列: 順番を保持して入れていく
        IInspector[] prefInspector = new IInspector[prefName.length];
        // その他のインスペクタを入れる配列
        List<IInspector> otherInspectors = new ArrayList<>();

        // 浮動インスペクタを生成して分類する
        Stream.of(InspectorCategory.values())
                .map(c -> c.clazz()).filter(Objects::nonNull).forEach(clazz -> {
            try {
                    // インスペクタを生成する
                    Constructor<? extends IInspector> c = clazz.getConstructor(PatientInspector.class);
                    IInspector ins = c.newInstance(PatientInspector.this);
                    inspectorMap.put(ins.getName(), ins);

                    // pref インスペクタかどうか分類
                    boolean isPref = false;

                    for (int i=0; i<prefName.length; i++) {
                        if (prefName[i].equals(ins.getName())) {
                            prefInspector[i] = ins;
                            isPref = true;
                            break;
                        }
                    }
                    if (! isPref) {
                        otherInspectors.add(ins);
                    }

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | InstantiationException ex) {
                ex.printStackTrace(System.err);
            }
        });

        // 分類したインスペクタをレイアウトしていく
        for (IInspector pref : prefInspector) {
            if (pref == null) { continue; }

            // 文書履歴だけは必ず Tab に入れる.
            if (pref.getName().equals(InspectorCategory.文書履歴.name())) {
                tabbedPane.addTab(pref.getTitle(), pref.getPanel());
                container.add(Box.createVerticalStrut(TOP_STRUT));
                container.add(tabbedPane);

            } else {
                pref.getPanel().setBorder(pref.getBorder());
                container.add(Box.createVerticalStrut(TOP_STRUT));
                container.add(pref.getPanel());
            }
        }

        // その他のインスペクタをタブに収納
        otherInspectors.forEach(ins -> {
            tabbedPane.addTab(ins.getTitle(), ins.getPanel());
            ins.addBadgeListener(tabbedPane::setBadge, tabbedPane.getTabCount()-1);
        });

        // update
        update();
    }

    /**
     * すべてのインスペクタをアップデートする.
     */
    public void update() {
        inspectorMap.values().forEach(inspector -> inspector.update());
    }

    /**
     * ChartImpl を返す.
     * @return
     */
    public ChartImpl getContext() {
        return context;
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
        return (PatientVisitInspector) inspectorMap.get(InspectorCategory.カレンダー.name());
    }

    /**
     * 患者メモインスペクタを返す.
     * @return 患者メモインスペクタ
     */
    public MemoInspector getMemoInspector() {
        return (MemoInspector) inspectorMap.get(InspectorCategory.メモ.name());
    }

    /**
     * 文書履歴インスペクタを返す.
     * @return 文書履歴インスペクタ
     */
    public DocumentHistory getDocumentHistory() {
        return (DocumentHistory) inspectorMap.get(InspectorCategory.文書履歴.name());
    }

    /**
     * 病名インスペクタを返す.
     * @return
     */
    public DiagnosisInspector getDiagnosisInspector() {
        return (DiagnosisInspector) inspectorMap.get(InspectorCategory.病名.name());
    }

    /**
     * 関連文書インスペクタを返す.
     * @return
     */
    public FileInspector getFileInspector() {
        return (FileInspector) inspectorMap.get(InspectorCategory.関連文書.name());
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
        // memo 欄の自動セーブ
        ((MemoInspector) inspectorMap.get(InspectorCategory.メモ.name())).save();
    }
}

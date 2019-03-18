package open.dolphin.client;

import open.dolphin.infomodel.PVTHealthInsuranceModel;

/**
 * NewKarteParams.
 *
 * @author Kazushi Minagawa
 */
public final class NewKarteParams {

    // ベースのカルテがあるかどうか，タブ及びEditorFrameの別，修正かどうか
    private final Chart.NewKarteOption option;

    // 空白，全コピー，前回処方適用のフラグ
    private Chart.NewKarteMode createMode;

    // 診療科
    private String department;

    // 診療科コード
    private String departmentCode;

    // 健康保険
    private PVTHealthInsuranceModel[] insurances;

    // 初期化時に選択する保険
    private int initialSelectedInsurance;

    // ダイアログでユーザが選択した保険
    private PVTHealthInsuranceModel insurance;

    // EditorFrame で編集するかどうかのフラグ
    private boolean openFrame;

    // 生成するドキュメントの種類
    // 2号カルテ，シングル，紹介状等
    private String docType;

    // 不明
    private String groupId;


    /**
     * Creates a new instance of NewKarteParams.
     *
     * @param option
     */
    public NewKarteParams(Chart.NewKarteOption option) {
        this.option = option;
    }

    public Chart.NewKarteOption getOption() {
        return option;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String val) {
        groupId = val;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String val) {
        department = val;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public PVTHealthInsuranceModel[] getInsurances() {
        return insurances;
    }

    public void setInsurances(PVTHealthInsuranceModel[] ins) {
        insurances = ins;
    }

    public PVTHealthInsuranceModel getPVTHealthInsurance() {
        return insurance;
    }

    public void setPVTHealthInsurance(PVTHealthInsuranceModel val) {
        insurance = val;
    }

    public boolean isOpenFrame() {
        return openFrame;
    }

    public void setOpenFrame(boolean openFrame) {
        this.openFrame = openFrame;
    }

    public Chart.NewKarteMode getCreateMode() {
        return createMode;
    }

    public void setCreateMode(Chart.NewKarteMode createMode) {
        this.createMode = createMode;
    }

    public int getInitialSelectedInsurance() {
        return initialSelectedInsurance;
    }

    public void setInitialSelectedInsurance(int index) {
        initialSelectedInsurance = index;
    }
}

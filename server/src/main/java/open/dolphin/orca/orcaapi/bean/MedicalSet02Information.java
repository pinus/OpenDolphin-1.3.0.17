package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Set02_Information. 診療行為自動算定設定(初期)項目名を変更(2018-09-25)
 *
 * @author pns
 */
public class MedicalSet02Information {
    /**
     * 外来初診・再診料システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Medical_Auto_Class;

    /**
     * 外来初診・再診料名称 (例: )
     */
    private String Medical_Auto_Class_Name;

    /**
     * 育児栄養指導加算システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Child_Med_Auto_Class;

    /**
     * 育児栄養指導加算名称 (例: )
     */
    private String Child_Med_Auto_Class_Name;

    /**
     * 育児栄養指導加算(自動算定科)システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Child_Med_Department_Code;

    /**
     * 育児栄養指導加算(自動算定科)名称 (例: )
     */
    private String Child_Med_Department_Code_Name;

    /**
     * 病名疾患区分からの自動発生システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Disease_Med_Auto_Class;

    /**
     * 病名疾患区分からの自動発生名称 (例: )
     */
    private String Disease_Med_Auto_Class_Name;

    /**
     * 療養担当手当(北海道)システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String MeTeratment_Auto_Class;

    /**
     * 療養担当手当(北海道)名称 (例: )
     */
    private String MeTeratment_Auto_Class_Name;

    /**
     * 外来管理加算チェックシステム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Outpatient_Med_Chk_Class;

    /**
     * 外来管理加算チェック名称 (例: )
     */
    private String Outpatient_Med_Chk_Class_Name;

    /**
     * 時間外加算(小児科特例)システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Child_Time_Class;

    /**
     * 時間外加算(小児科特例)名称 (例: )
     */
    private String Child_Time_Class_Name;

    /**
     * 最終来院日から初診までの期間(月数)システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String LastVisit_Term;

    /**
     * 前回保険組合せ相違チェックシステム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Combination_Number_Chk_Class;

    /**
     * 前回保険組合せ相違チェック区分 (例: )
     */
    private String Combination_Number_Chk_Class_Name;

    /**
     * Medical_Auto_Class
     *
     * @return Medical_Auto_Class
     */
    public String getMedical_Auto_Class() {
        return Medical_Auto_Class;
    }

    /**
     * Medical_Auto_Class
     *
     * @param Medical_Auto_Class to set
     */
    public void setMedical_Auto_Class(String Medical_Auto_Class) {
        this.Medical_Auto_Class = Medical_Auto_Class;
    }

    /**
     * Medical_Auto_Class_Name
     *
     * @return Medical_Auto_Class_Name
     */
    public String getMedical_Auto_Class_Name() {
        return Medical_Auto_Class_Name;
    }

    /**
     * Medical_Auto_Class_Name
     *
     * @param Medical_Auto_Class_Name to set
     */
    public void setMedical_Auto_Class_Name(String Medical_Auto_Class_Name) {
        this.Medical_Auto_Class_Name = Medical_Auto_Class_Name;
    }

    /**
     * Child_Med_Auto_Class
     *
     * @return Child_Med_Auto_Class
     */
    public String getChild_Med_Auto_Class() {
        return Child_Med_Auto_Class;
    }

    /**
     * Child_Med_Auto_Class
     *
     * @param Child_Med_Auto_Class to set
     */
    public void setChild_Med_Auto_Class(String Child_Med_Auto_Class) {
        this.Child_Med_Auto_Class = Child_Med_Auto_Class;
    }

    /**
     * Child_Med_Auto_Class_Name
     *
     * @return Child_Med_Auto_Class_Name
     */
    public String getChild_Med_Auto_Class_Name() {
        return Child_Med_Auto_Class_Name;
    }

    /**
     * Child_Med_Auto_Class_Name
     *
     * @param Child_Med_Auto_Class_Name to set
     */
    public void setChild_Med_Auto_Class_Name(String Child_Med_Auto_Class_Name) {
        this.Child_Med_Auto_Class_Name = Child_Med_Auto_Class_Name;
    }

    /**
     * Child_Med_Department_Code
     *
     * @return Child_Med_Department_Code
     */
    public String getChild_Med_Department_Code() {
        return Child_Med_Department_Code;
    }

    /**
     * Child_Med_Department_Code
     *
     * @param Child_Med_Department_Code to set
     */
    public void setChild_Med_Department_Code(String Child_Med_Department_Code) {
        this.Child_Med_Department_Code = Child_Med_Department_Code;
    }

    /**
     * Child_Med_Department_Code_Name
     *
     * @return Child_Med_Department_Code_Name
     */
    public String getChild_Med_Department_Code_Name() {
        return Child_Med_Department_Code_Name;
    }

    /**
     * Child_Med_Department_Code_Name
     *
     * @param Child_Med_Department_Code_Name to set
     */
    public void setChild_Med_Department_Code_Name(String Child_Med_Department_Code_Name) {
        this.Child_Med_Department_Code_Name = Child_Med_Department_Code_Name;
    }

    /**
     * Disease_Med_Auto_Class
     *
     * @return Disease_Med_Auto_Class
     */
    public String getDisease_Med_Auto_Class() {
        return Disease_Med_Auto_Class;
    }

    /**
     * Disease_Med_Auto_Class
     *
     * @param Disease_Med_Auto_Class to set
     */
    public void setDisease_Med_Auto_Class(String Disease_Med_Auto_Class) {
        this.Disease_Med_Auto_Class = Disease_Med_Auto_Class;
    }

    /**
     * Disease_Med_Auto_Class_Name
     *
     * @return Disease_Med_Auto_Class_Name
     */
    public String getDisease_Med_Auto_Class_Name() {
        return Disease_Med_Auto_Class_Name;
    }

    /**
     * Disease_Med_Auto_Class_Name
     *
     * @param Disease_Med_Auto_Class_Name to set
     */
    public void setDisease_Med_Auto_Class_Name(String Disease_Med_Auto_Class_Name) {
        this.Disease_Med_Auto_Class_Name = Disease_Med_Auto_Class_Name;
    }

    /**
     * MeTeratment_Auto_Class
     *
     * @return MeTeratment_Auto_Class
     */
    public String getMeTeratment_Auto_Class() {
        return MeTeratment_Auto_Class;
    }

    /**
     * MeTeratment_Auto_Class
     *
     * @param MeTeratment_Auto_Class to set
     */
    public void setMeTeratment_Auto_Class(String MeTeratment_Auto_Class) {
        this.MeTeratment_Auto_Class = MeTeratment_Auto_Class;
    }

    /**
     * MeTeratment_Auto_Class_Name
     *
     * @return MeTeratment_Auto_Class_Name
     */
    public String getMeTeratment_Auto_Class_Name() {
        return MeTeratment_Auto_Class_Name;
    }

    /**
     * MeTeratment_Auto_Class_Name
     *
     * @param MeTeratment_Auto_Class_Name to set
     */
    public void setMeTeratment_Auto_Class_Name(String MeTeratment_Auto_Class_Name) {
        this.MeTeratment_Auto_Class_Name = MeTeratment_Auto_Class_Name;
    }

    /**
     * Outpatient_Med_Chk_Class
     *
     * @return Outpatient_Med_Chk_Class
     */
    public String getOutpatient_Med_Chk_Class() {
        return Outpatient_Med_Chk_Class;
    }

    /**
     * Outpatient_Med_Chk_Class
     *
     * @param Outpatient_Med_Chk_Class to set
     */
    public void setOutpatient_Med_Chk_Class(String Outpatient_Med_Chk_Class) {
        this.Outpatient_Med_Chk_Class = Outpatient_Med_Chk_Class;
    }

    /**
     * Outpatient_Med_Chk_Class_Name
     *
     * @return Outpatient_Med_Chk_Class_Name
     */
    public String getOutpatient_Med_Chk_Class_Name() {
        return Outpatient_Med_Chk_Class_Name;
    }

    /**
     * Outpatient_Med_Chk_Class_Name
     *
     * @param Outpatient_Med_Chk_Class_Name to set
     */
    public void setOutpatient_Med_Chk_Class_Name(String Outpatient_Med_Chk_Class_Name) {
        this.Outpatient_Med_Chk_Class_Name = Outpatient_Med_Chk_Class_Name;
    }

    /**
     * Child_Time_Class
     *
     * @return Child_Time_Class
     */
    public String getChild_Time_Class() {
        return Child_Time_Class;
    }

    /**
     * Child_Time_Class
     *
     * @param Child_Time_Class to set
     */
    public void setChild_Time_Class(String Child_Time_Class) {
        this.Child_Time_Class = Child_Time_Class;
    }

    /**
     * Child_Time_Class_Name
     *
     * @return Child_Time_Class_Name
     */
    public String getChild_Time_Class_Name() {
        return Child_Time_Class_Name;
    }

    /**
     * Child_Time_Class_Name
     *
     * @param Child_Time_Class_Name to set
     */
    public void setChild_Time_Class_Name(String Child_Time_Class_Name) {
        this.Child_Time_Class_Name = Child_Time_Class_Name;
    }

    /**
     * LastVisit_Term
     *
     * @return LastVisit_Term
     */
    public String getLastVisit_Term() {
        return LastVisit_Term;
    }

    /**
     * LastVisit_Term
     *
     * @param LastVisit_Term to set
     */
    public void setLastVisit_Term(String LastVisit_Term) {
        this.LastVisit_Term = LastVisit_Term;
    }

    /**
     * Combination_Number_Chk_Class
     *
     * @return Combination_Number_Chk_Class
     */
    public String getCombination_Number_Chk_Class() {
        return Combination_Number_Chk_Class;
    }

    /**
     * Combination_Number_Chk_Class
     *
     * @param Combination_Number_Chk_Class to set
     */
    public void setCombination_Number_Chk_Class(String Combination_Number_Chk_Class) {
        this.Combination_Number_Chk_Class = Combination_Number_Chk_Class;
    }

    /**
     * Combination_Number_Chk_Class_Name
     *
     * @return Combination_Number_Chk_Class_Name
     */
    public String getCombination_Number_Chk_Class_Name() {
        return Combination_Number_Chk_Class_Name;
    }

    /**
     * Combination_Number_Chk_Class_Name
     *
     * @param Combination_Number_Chk_Class_Name to set
     */
    public void setCombination_Number_Chk_Class_Name(String Combination_Number_Chk_Class_Name) {
        this.Combination_Number_Chk_Class_Name = Combination_Number_Chk_Class_Name;
    }
}

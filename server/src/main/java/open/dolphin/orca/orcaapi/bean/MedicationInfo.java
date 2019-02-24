package open.dolphin.orca.orcaapi.bean;

/**
 * Medication_info. 診療内容(繰り返し40)
 * @author pns
 */
public class MedicationInfo {
    /**
     * コード (例: 620811502)
     */
    private String Medication_Code;

    /**
     * 名称 (例: ワルファリンＫ錠１ｍｇ「Ｆ」)
     */
    private String Medication_Name;

    /**
     * 数量（薬剤・器材の数量、きざみ値以外は１） (例: 2)
     */
    private String Medication_Number;

    /**
     * 単位 (例: 016)
     */
    private String Unit_Code;

    /**
     * 単位名称 (例: 錠)
     */
    private String Unit_Code_Name;

    /**
     * 点数識別(1:金額3:点数etc) (例: 3)
     */
    private String Medication_Point_Class;

    /**
     * 点数 (例: 97)
     */
    private String Medication_Point;

    /**
     * 一般処方指示(yes：一般名を使用する、no：銘柄指示、以外：日レセの設定指示に従う) (例: yes)
     */
    private String Medication_Generic_Flg;

    /**
     * 継続コメント区分(1：継続コメント) (例: )
     */
    private String Medication_Continue;

    /**
     * 内服１種類区分(1：内服１種類) (例: )
     */
    private String Medication_Internal_Kinds;

    /**
     * 診療種別区分 (例: 40)
     */
    private String Medical_Class;

    /**
     * 診療種別区分名称 (例: 処置行為)
     */
    private String Medical_Class_Name;

    /**
     * 包括区分（1：包括分） (例: 1)
     */
    private String Medical_Class_code;

    /**
     * 包括剤区分(True：包括対象) (例: True)
     */
    private String Medical_Inclusion_Class;

    /**
     * 算定合計数（算定した日の合計(最大３１日)） (例: 01)
     */
    private String Perform_Total_Number;

    /**
     * 算定日区分 (例: 0000000000000000010000000000000)
     */
    private String Perform_Calendar;

    /**
     * 薬剤・器材数量（繰り返し　３１） (例: )
     */
    private PerformDayInformation[] Perform_Day_Information;

    /**
     * 剤点数 (例: 2)
     */
    private String Medical_Class_Point;

    /**
     * 剤金額 (例: )
     */
    private String Medical_Class_Money;

    /**
     * 算定回数 (例: 1)
     */
    private String Medical_Class_Number;

    /**
     * 自費金額 (例: 12945)
     */
    private String Medication_Money;

    /**
     * コメント埋め込み数値（繰り返し　５） (例: )
     */
    private MedicationInputInfo[] Medication_Input_Info;

    /**
     * コード (例: 620811502)
     * @return the Medication_Code
     */
    public String getMedication_Code() {
        return Medication_Code;
    }

    /**
     * コード (例: 620811502)
     * @param Medication_Code the Medication_Code to set
     */
    public void setMedication_Code(String Medication_Code) {
        this.Medication_Code = Medication_Code;
    }

    /**
     * 名称 (例: ワルファリンＫ錠１ｍｇ「Ｆ」)
     * @return the Medication_Name
     */
    public String getMedication_Name() {
        return Medication_Name;
    }

    /**
     * 名称 (例: ワルファリンＫ錠１ｍｇ「Ｆ」)
     * @param Medication_Name the Medication_Name to set
     */
    public void setMedication_Name(String Medication_Name) {
        this.Medication_Name = Medication_Name;
    }

    /**
     * 数量（薬剤・器材の数量、きざみ値以外は１） (例: 2)
     * @return the Medication_Number
     */
    public String getMedication_Number() {
        return Medication_Number;
    }

    /**
     * 数量（薬剤・器材の数量、きざみ値以外は１） (例: 2)
     * @param Medication_Number the Medication_Number to set
     */
    public void setMedication_Number(String Medication_Number) {
        this.Medication_Number = Medication_Number;
    }

    /**
     * 単位 (例: 016)
     * @return the Unit_Code
     */
    public String getUnit_Code() {
        return Unit_Code;
    }

    /**
     * 単位 (例: 016)
     * @param Unit_Code the Unit_Code to set
     */
    public void setUnit_Code(String Unit_Code) {
        this.Unit_Code = Unit_Code;
    }

    /**
     * 単位名称 (例: 錠)
     * @return the Unit_Code_Name
     */
    public String getUnit_Code_Name() {
        return Unit_Code_Name;
    }

    /**
     * 単位名称 (例: 錠)
     * @param Unit_Code_Name the Unit_Code_Name to set
     */
    public void setUnit_Code_Name(String Unit_Code_Name) {
        this.Unit_Code_Name = Unit_Code_Name;
    }

    /**
     * 点数識別(1:金額3:点数etc) (例: 3)
     * @return the Medication_Point_Class
     */
    public String getMedication_Point_Class() {
        return Medication_Point_Class;
    }

    /**
     * 点数識別(1:金額3:点数etc) (例: 3)
     * @param Medication_Point_Class the Medication_Point_Class to set
     */
    public void setMedication_Point_Class(String Medication_Point_Class) {
        this.Medication_Point_Class = Medication_Point_Class;
    }

    /**
     * 点数 (例: 97)
     * @return the Medication_Point
     */
    public String getMedication_Point() {
        return Medication_Point;
    }

    /**
     * 点数 (例: 97)
     * @param Medication_Point the Medication_Point to set
     */
    public void setMedication_Point(String Medication_Point) {
        this.Medication_Point = Medication_Point;
    }

    /**
     * 一般処方指示(yes：一般名を使用する、no：銘柄指示、以外：日レセの設定指示に従う) (例: yes)
     * @return the Medication_Generic_Flg
     */
    public String getMedication_Generic_Flg() {
        return Medication_Generic_Flg;
    }

    /**
     * 一般処方指示(yes：一般名を使用する、no：銘柄指示、以外：日レセの設定指示に従う) (例: yes)
     * @param Medication_Generic_Flg the Medication_Generic_Flg to set
     */
    public void setMedication_Generic_Flg(String Medication_Generic_Flg) {
        this.Medication_Generic_Flg = Medication_Generic_Flg;
    }

    /**
     * 継続コメント区分(1：継続コメント) (例: )
     * @return the Medication_Continue
     */
    public String getMedication_Continue() {
        return Medication_Continue;
    }

    /**
     * 継続コメント区分(1：継続コメント) (例: )
     * @param Medication_Continue the Medication_Continue to set
     */
    public void setMedication_Continue(String Medication_Continue) {
        this.Medication_Continue = Medication_Continue;
    }

    /**
     * 内服１種類区分(1：内服１種類) (例: )
     * @return the Medication_Internal_Kinds
     */
    public String getMedication_Internal_Kinds() {
        return Medication_Internal_Kinds;
    }

    /**
     * 内服１種類区分(1：内服１種類) (例: )
     * @param Medication_Internal_Kinds the Medication_Internal_Kinds to set
     */
    public void setMedication_Internal_Kinds(String Medication_Internal_Kinds) {
        this.Medication_Internal_Kinds = Medication_Internal_Kinds;
    }

    /**
     * 診療種別区分 (例: 40)
     * @return the Medical_Class
     */
    public String getMedical_Class() {
        return Medical_Class;
    }

    /**
     * 診療種別区分 (例: 40)
     * @param Medical_Class the Medical_Class to set
     */
    public void setMedical_Class(String Medical_Class) {
        this.Medical_Class = Medical_Class;
    }

    /**
     * 診療種別区分名称 (例: 処置行為)
     * @return the Medical_Class_Name
     */
    public String getMedical_Class_Name() {
        return Medical_Class_Name;
    }

    /**
     * 診療種別区分名称 (例: 処置行為)
     * @param Medical_Class_Name the Medical_Class_Name to set
     */
    public void setMedical_Class_Name(String Medical_Class_Name) {
        this.Medical_Class_Name = Medical_Class_Name;
    }

    /**
     * 包括区分（1：包括分） (例: 1)
     * @return the Medical_Class_code
     */
    public String getMedical_Class_code() {
        return Medical_Class_code;
    }

    /**
     * 包括区分（1：包括分） (例: 1)
     * @param Medical_Class_code the Medical_Class_code to set
     */
    public void setMedical_Class_code(String Medical_Class_code) {
        this.Medical_Class_code = Medical_Class_code;
    }

    /**
     * 包括剤区分(True：包括対象) (例: True)
     * @return the Medical_Inclusion_Class
     */
    public String getMedical_Inclusion_Class() {
        return Medical_Inclusion_Class;
    }

    /**
     * 包括剤区分(True：包括対象) (例: True)
     * @param Medical_Inclusion_Class the Medical_Inclusion_Class to set
     */
    public void setMedical_Inclusion_Class(String Medical_Inclusion_Class) {
        this.Medical_Inclusion_Class = Medical_Inclusion_Class;
    }

    /**
     * 算定合計数（算定した日の合計(最大３１日)） (例: 01)
     * @return the Perform_Total_Number
     */
    public String getPerform_Total_Number() {
        return Perform_Total_Number;
    }

    /**
     * 算定合計数（算定した日の合計(最大３１日)） (例: 01)
     * @param Perform_Total_Number the Perform_Total_Number to set
     */
    public void setPerform_Total_Number(String Perform_Total_Number) {
        this.Perform_Total_Number = Perform_Total_Number;
    }

    /**
     * 算定日区分 (例: 0000000000000000010000000000000)
     * @return the Perform_Calendar
     */
    public String getPerform_Calendar() {
        return Perform_Calendar;
    }

    /**
     * 算定日区分 (例: 0000000000000000010000000000000)
     * @param Perform_Calendar the Perform_Calendar to set
     */
    public void setPerform_Calendar(String Perform_Calendar) {
        this.Perform_Calendar = Perform_Calendar;
    }

    /**
     * 薬剤・器材数量（繰り返し　３１） (例: )
     * @return the Perform_Day_Information
     */
    public PerformDayInformation[] getPerform_Day_Information() {
        return Perform_Day_Information;
    }

    /**
     * 薬剤・器材数量（繰り返し　３１） (例: )
     * @param Perform_Day_Information the Perform_Day_Information to set
     */
    public void setPerform_Day_Information(PerformDayInformation[] Perform_Day_Information) {
        this.Perform_Day_Information = Perform_Day_Information;
    }

    /**
     * 剤点数 (例: 2)
     * @return the Medical_Class_Point
     */
    public String getMedical_Class_Point() {
        return Medical_Class_Point;
    }

    /**
     * 剤点数 (例: 2)
     * @param Medical_Class_Point the Medical_Class_Point to set
     */
    public void setMedical_Class_Point(String Medical_Class_Point) {
        this.Medical_Class_Point = Medical_Class_Point;
    }

    /**
     * 剤金額 (例: )
     * @return the Medical_Class_Money
     */
    public String getMedical_Class_Money() {
        return Medical_Class_Money;
    }

    /**
     * 剤金額 (例: )
     * @param Medical_Class_Money the Medical_Class_Money to set
     */
    public void setMedical_Class_Money(String Medical_Class_Money) {
        this.Medical_Class_Money = Medical_Class_Money;
    }

    /**
     * 算定回数 (例: 1)
     * @return the Medical_Class_Number
     */
    public String getMedical_Class_Number() {
        return Medical_Class_Number;
    }

    /**
     * 算定回数 (例: 1)
     * @param Medical_Class_Number the Medical_Class_Number to set
     */
    public void setMedical_Class_Number(String Medical_Class_Number) {
        this.Medical_Class_Number = Medical_Class_Number;
    }

    /**
     * 自費金額 (例: 12945)
     * @return the Medication_Money
     */
    public String getMedication_Money() {
        return Medication_Money;
    }

    /**
     * 自費金額 (例: 12945)
     * @param Medication_Money the Medication_Money to set
     */
    public void setMedication_Money(String Medication_Money) {
        this.Medication_Money = Medication_Money;
    }

    /**
     * コメント埋め込み数値（繰り返し　５） (例: )
     * @return the Medication_Input_Info
     */
    public MedicationInputInfo[] getMedication_Input_Info() {
        return Medication_Input_Info;
    }

    /**
     * コメント埋め込み数値（繰り返し　５） (例: )
     * @param Medication_Input_Info the Medication_Input_Info to set
     */
    public void setMedication_Input_Info(MedicationInputInfo[] Medication_Input_Info) {
        this.Medication_Input_Info = Medication_Input_Info;
    }
}
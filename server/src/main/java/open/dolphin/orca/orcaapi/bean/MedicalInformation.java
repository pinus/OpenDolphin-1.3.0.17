package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Information. 診療行為情報(繰り返し40)
 *
 * @author pns
 */
public class MedicalInformation {
    /**
     * 診療種別区分(詳細については、「日医標準レセプトソフトデータベーステーブル定義書」を参照して下さい。) (例:  210)
     */
    private String Medical_Class;

    /**
     * 診療種別区分 (例: 400)
     */
    private String Medical_Class2;

    /**
     * 診療種別区分名称 (例: 処置行為)
     */
    private String Medical_Class_Name;

    /**
     * 回数 (例: 1)
     */
    private String Medical_Class_Number;

    /**
     * 剤点数（マイナス編集） (例: 2)
     */
    private String Medical_Class_Point;

    /**
     * 剤金額（自費金額、労災の円） (例: 0)
     */
    private String Medical_Class_Money;

    /**
     * 剤区分(１：包括分、２：薬評（治験）) (例: 1)
     */
    private String Medical_Class_code;

    /**
     * 包括剤区分(True：包括対象) (例: True)
     */
    private String Medical_Inclusion_Class;

    /**
     * 包括検査項目数 (例: 07)
     */
    private String Medical_Examination_Count;

    /**
     * 診療行為詳細（繰り返し　５０） (例:  )
     */
    private MedicationInfo[] Medication_info;

    /**
     * 診療種別区分(詳細については、「日医標準レセプトソフトデータベーステーブル定義書」を参照して下さい。) (例:  210)
     *
     * @return the Medical_Class
     */
    public String getMedical_Class() {
        return Medical_Class;
    }

    /**
     * 診療種別区分(詳細については、「日医標準レセプトソフトデータベーステーブル定義書」を参照して下さい。) (例:  210)
     *
     * @param Medical_Class the Medical_Class to set
     */
    public void setMedical_Class(String Medical_Class) {
        this.Medical_Class = Medical_Class;
    }

    /**
     * 診療種別区分名称 (例: 処置行為)
     *
     * @return the Medical_Class_Name
     */
    public String getMedical_Class_Name() {
        return Medical_Class_Name;
    }

    /**
     * 診療種別区分名称 (例: 処置行為)
     *
     * @param Medical_Class_Name the Medical_Class_Name to set
     */
    public void setMedical_Class_Name(String Medical_Class_Name) {
        this.Medical_Class_Name = Medical_Class_Name;
    }

    /**
     * 回数 (例: 1)
     *
     * @return the Medical_Class_Number
     */
    public String getMedical_Class_Number() {
        return Medical_Class_Number;
    }

    /**
     * 回数 (例: 1)
     *
     * @param Medical_Class_Number the Medical_Class_Number to set
     */
    public void setMedical_Class_Number(String Medical_Class_Number) {
        this.Medical_Class_Number = Medical_Class_Number;
    }

    /**
     * 剤点数（マイナス編集） (例: 2)
     *
     * @return the Medical_Class_Point
     */
    public String getMedical_Class_Point() {
        return Medical_Class_Point;
    }

    /**
     * 剤点数（マイナス編集） (例: 2)
     *
     * @param Medical_Class_Point the Medical_Class_Point to set
     */
    public void setMedical_Class_Point(String Medical_Class_Point) {
        this.Medical_Class_Point = Medical_Class_Point;
    }

    /**
     * 剤金額（自費金額、労災の円） (例: 0)
     *
     * @return the Medical_Class_Money
     */
    public String getMedical_Class_Money() {
        return Medical_Class_Money;
    }

    /**
     * 剤金額（自費金額、労災の円） (例: 0)
     *
     * @param Medical_Class_Money the Medical_Class_Money to set
     */
    public void setMedical_Class_Money(String Medical_Class_Money) {
        this.Medical_Class_Money = Medical_Class_Money;
    }

    /**
     * 剤区分(１：包括分、２：薬評（治験）) (例: 1)
     *
     * @return the Medical_Class_code
     */
    public String getMedical_Class_code() {
        return Medical_Class_code;
    }

    /**
     * 剤区分(１：包括分、２：薬評（治験）) (例: 1)
     *
     * @param Medical_Class_code the Medical_Class_code to set
     */
    public void setMedical_Class_code(String Medical_Class_code) {
        this.Medical_Class_code = Medical_Class_code;
    }

    /**
     * 包括剤区分(True：包括対象) (例: True)
     *
     * @return the Medical_Inclusion_Class
     */
    public String getMedical_Inclusion_Class() {
        return Medical_Inclusion_Class;
    }

    /**
     * 包括剤区分(True：包括対象) (例: True)
     *
     * @param Medical_Inclusion_Class the Medical_Inclusion_Class to set
     */
    public void setMedical_Inclusion_Class(String Medical_Inclusion_Class) {
        this.Medical_Inclusion_Class = Medical_Inclusion_Class;
    }

    /**
     * 包括検査項目数 (例: 07)
     *
     * @return the Medical_Examination_Count
     */
    public String getMedical_Examination_Count() {
        return Medical_Examination_Count;
    }

    /**
     * 包括検査項目数 (例: 07)
     *
     * @param Medical_Examination_Count the Medical_Examination_Count to set
     */
    public void setMedical_Examination_Count(String Medical_Examination_Count) {
        this.Medical_Examination_Count = Medical_Examination_Count;
    }

    /**
     * 診療行為詳細（繰り返し　５０） (例:  )
     *
     * @return the Medication_info
     */
    public MedicationInfo[] getMedication_info() {
        return Medication_info;
    }

    /**
     * 診療行為詳細（繰り返し　５０） (例:  )
     *
     * @param Medication_info the Medication_info to set
     */
    public void setMedication_info(MedicationInfo[] Medication_info) {
        this.Medication_info = Medication_info;
    }

    /**
     * 診療種別区分 (例: 400)
     *
     * @return the Medical_Class2
     */
    public String getMedical_Class2() {
        return Medical_Class2;
    }

    /**
     * 診療種別区分 (例: 400)
     *
     * @param Medical_Class2 the Medical_Class2 to set
     */
    public void setMedical_Class2(String Medical_Class2) {
        this.Medical_Class2 = Medical_Class2;
    }
}
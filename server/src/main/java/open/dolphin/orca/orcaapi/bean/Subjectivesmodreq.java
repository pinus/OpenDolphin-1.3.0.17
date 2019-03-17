package open.dolphin.orca.orcaapi.bean;

/**
 * subjectivesmodreq.
 *
 * @author pns
 */
public class Subjectivesmodreq {
    /**
     * 入外区分（I:入院、それ以外:入院外） (例: I)
     */
    private String InOut;

    /**
     * 患者番号 (例: 12)
     */
    private String Patient_ID;

    /**
     * 診療年月 (例: 2012-12)
     */
    private String Perform_Date;

    /**
     * 診療科コード※１（01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * 保険組合せ番号 (例: 0002)
     */
    private String Insurance_Combination_Number;

    /**
     * 保険組合せ情報 (例:  )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * 詳記区分 (例: 07)
     * <pre>
     * 01 　主たる疾患の臨床症状
     * 02 　主たる疾患の診療・検査所見
     * 03 　主な治療行為の必要性
     * 04 　主な治療行為の経過
     * 05 　 100 万点以上の薬剤に係る症状等
     * 06 　 100 万点以上の処置に係る症状等 それぞれ必要な区分に内容を入力します。
     * 07 　その他（ 1 ）
     * 08 　その他（ 2 ）
     * 09 　その他（ 3 ）
     * 50 　治験概要
     * 51 　疾患別リハビリテージョンに係る治療継続理 由
     * 52 　廃用症候群に係る評価表 それぞれ必要な区分に内容を入力します。
     * 90 　上記以外の診療報酬明細書 訪問診療に係わる記録書として使用してください。 ＊入院のレセプトには記載しません。
     * 99 　レセプト摘要欄コメント 区分 01 ～ 09 に分類しないフリーなコメント ※自賠責（第三者行為）レセプトには記載しません。
     * AA 　労災レセプト「傷病の経過」 労災レセプトの傷病の経過欄に記載します。
     * BB 　自賠責（第三者行為）レセプト摘要欄コメン ト 自賠責（第三者行為）レセプトのみ記載します。
     * </pre>
     */
    private String Subjectives_Detail_Record;

    /**
     * 症状詳記内容 (例: その他コメント)
     */
    private String Subjectives_Code;

    /**
     * 入外区分（I:入院、それ以外:入院外） (例: I)
     *
     * @return the InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * 入外区分（I:入院、それ以外:入院外） (例: I)
     *
     * @param InOut the InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
    }

    /**
     * 患者番号 (例: 12)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 12)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 診療年月 (例: 2012-12)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療年月 (例: 2012-12)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 診療科コード※１（01:内科) (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード※１（01:内科) (例: 01)
     *
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 保険組合せ番号 (例: 0002)
     *
     * @return the Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * 保険組合せ番号 (例: 0002)
     *
     * @param Insurance_Combination_Number the Insurance_Combination_Number to set
     */
    public void setInsurance_Combination_Number(String Insurance_Combination_Number) {
        this.Insurance_Combination_Number = Insurance_Combination_Number;
    }

    /**
     * 保険組合せ情報 (例:  )
     *
     * @return the HealthInsurance_Information
     */
    public HealthInsuranceInformation getHealthInsurance_Information() {
        return HealthInsurance_Information;
    }

    /**
     * 保険組合せ情報 (例:  )
     *
     * @param HealthInsurance_Information the HealthInsurance_Information to set
     */
    public void setHealthInsurance_Information(HealthInsuranceInformation HealthInsurance_Information) {
        this.HealthInsurance_Information = HealthInsurance_Information;
    }

    /**
     * 詳記区分 (例: 07)
     * <pre>
     * 01 　主たる疾患の臨床症状
     * 02 　主たる疾患の診療・検査所見
     * 03 　主な治療行為の必要性
     * 04 　主な治療行為の経過
     * 05 　 100 万点以上の薬剤に係る症状等
     * 06 　 100 万点以上の処置に係る症状等 それぞれ必要な区分に内容を入力します。
     * 07 　その他（ 1 ）
     * 08 　その他（ 2 ）
     * 09 　その他（ 3 ）
     * 50 　治験概要
     * 51 　疾患別リハビリテージョンに係る治療継続理 由
     * 52 　廃用症候群に係る評価表 それぞれ必要な区分に内容を入力します。
     * 90 　上記以外の診療報酬明細書 訪問診療に係わる記録書として使用してください。 ＊入院のレセプトには記載しません。
     * 99 　レセプト摘要欄コメント 区分 01 ～ 09 に分類しないフリーなコメント ※自賠責（第三者行為）レセプトには記載しません。
     * AA 　労災レセプト「傷病の経過」 労災レセプトの傷病の経過欄に記載します。
     * BB 　自賠責（第三者行為）レセプト摘要欄コメン ト 自賠責（第三者行為）レセプトのみ記載します。
     * </pre>
     *
     * @return the Subjectives_Detail_Record
     */
    public String getSubjectives_Detail_Record() {
        return Subjectives_Detail_Record;
    }

    /**
     * 詳記区分 (例: 07)
     * <pre>
     * 01 　主たる疾患の臨床症状
     * 02 　主たる疾患の診療・検査所見
     * 03 　主な治療行為の必要性
     * 04 　主な治療行為の経過
     * 05 　 100 万点以上の薬剤に係る症状等
     * 06 　 100 万点以上の処置に係る症状等 それぞれ必要な区分に内容を入力します。
     * 07 　その他（ 1 ）
     * 08 　その他（ 2 ）
     * 09 　その他（ 3 ）
     * 50 　治験概要
     * 51 　疾患別リハビリテージョンに係る治療継続理 由
     * 52 　廃用症候群に係る評価表 それぞれ必要な区分に内容を入力します。
     * 90 　上記以外の診療報酬明細書 訪問診療に係わる記録書として使用してください。 ＊入院のレセプトには記載しません。
     * 99 　レセプト摘要欄コメント 区分 01 ～ 09 に分類しないフリーなコメント ※自賠責（第三者行為）レセプトには記載しません。
     * AA 　労災レセプト「傷病の経過」 労災レセプトの傷病の経過欄に記載します。
     * BB 　自賠責（第三者行為）レセプト摘要欄コメン ト 自賠責（第三者行為）レセプトのみ記載します。
     * </pre>
     *
     * @param Subjectives_Detail_Record the Subjectives_Detail_Record to set
     */
    public void setSubjectives_Detail_Record(String Subjectives_Detail_Record) {
        this.Subjectives_Detail_Record = Subjectives_Detail_Record;
    }

    /**
     * 症状詳記内容 (例: その他コメント)
     *
     * @return the Subjectives_Code
     */
    public String getSubjectives_Code() {
        return Subjectives_Code;
    }

    /**
     * 症状詳記内容 (例: その他コメント)
     *
     * @param Subjectives_Code the Subjectives_Code to set
     */
    public void setSubjectives_Code(String Subjectives_Code) {
        this.Subjectives_Code = Subjectives_Code;
    }
}
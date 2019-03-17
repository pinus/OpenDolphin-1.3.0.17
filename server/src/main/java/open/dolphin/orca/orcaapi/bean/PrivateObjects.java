package open.dolphin.orca.orcaapi.bean;

/**
 * private_objects1.
 *
 * @author pns
 */
public class PrivateObjects {
    /**
     * リクエスト日 (例: 2014-10-23)
     */
    private String Request_Date;

    /**
     * リクエスト時間(時:分:秒) (例: 16:52:00)
     */
    private String Request_Time;

    /**
     * 患者番号 (例: 12)
     */
    private String Patient_ID;

    /**
     * 診療日 (例: 2013-10-10)
     */
    private String Perform_Date;

    /**
     * 診療月 (例: 2013-10)
     */
    private String Perform_Month;

    /**
     * 診療年 (例: 2013)
     */
    private String Perform_Year;

    /**
     * 実施日 (例: 2013-12-11)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 12:21:52)
     */
    private String Information_Time;

    /**
     * 結果コード (例: 0000)
     */
    private String Api_Result;

    /**
     * 結果メッセージ (例: 処理終了)
     */
    private String Api_Result_Message;

    /**
     * 患者情報 (例: )
     */
    private PatientInformation Patient_Information;

    /**
     * 請求情報オーバーフラグ (例: false)
     */
    private String Income_Information_Overflow;

    /**
     * 請求情報（繰り返し　２００）（並び順は診療日の古い順） (例: )
     */
    private IncomeInformation[] Income_Information;

    /**
     * 保険組合せ詳細（繰り返し　２０） (例: )
     */
    private HealthInsuranceInformation[] Insurance_Information;

    /**
     * 未収金額合計（０件の場合は非表示） (例: )
     */
    private String Unpaid_Money_Total;

    /**
     * 未収金情報オーバーフラグ（０件：非表示、１〜５０件：false、５０件超：true） (例: )
     */
    private String Unpaid_Money_Information_Overflow;

    /**
     * 個別の未収金情報（繰り返し　５０）（診療日の新しい順） (例: )
     */
    private UnpaidMoneyInformation[] Unpaid_Money_Information;

    /**
     * 日レセバージョン((M96)マスタ更新管理一覧画面のDB管理情報ORCAver) (例: 040700-1)
     */
    private String Jma_Receipt_Version;

    /**
     * データベース情報 (例:  )
     */
    private DatabaseInformation Database_Information;

    /**
     * マスタ更新情報 (例:  )
     */
    private MasterUpdateInformation Master_Update_Information;

    /**
     * プログラム更新情報(繰り返し10) (例:  )
     */
    private ProgramUpdateInformation[] Program_Update_Information;

    /**
     * リクエスト日 (例: 2014-10-23)
     *
     * @return the Request_Date
     */
    public String getRequest_Date() {
        return Request_Date;
    }

    /**
     * リクエスト日 (例: 2014-10-23)
     *
     * @param Request_Date the Request_Date to set
     */
    public void setRequest_Date(String Request_Date) {
        this.Request_Date = Request_Date;
    }

    /**
     * リクエスト時間(時:分:秒) (例: 16:52:00)
     *
     * @return the Request_Time
     */
    public String getRequest_Time() {
        return Request_Time;
    }

    /**
     * リクエスト時間(時:分:秒) (例: 16:52:00)
     *
     * @param Request_Time the Request_Time to set
     */
    public void setRequest_Time(String Request_Time) {
        this.Request_Time = Request_Time;
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
     * 診療日 (例: 2013-10-10)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療日 (例: 2013-10-10)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 診療月 (例: 2013-10)
     *
     * @return the Perform_Month
     */
    public String getPerform_Month() {
        return Perform_Month;
    }

    /**
     * 診療月 (例: 2013-10)
     *
     * @param Perform_Month the Perform_Month to set
     */
    public void setPerform_Month(String Perform_Month) {
        this.Perform_Month = Perform_Month;
    }

    /**
     * 診療年 (例: 2013)
     *
     * @return the Perform_Year
     */
    public String getPerform_Year() {
        return Perform_Year;
    }

    /**
     * 診療年 (例: 2013)
     *
     * @param Perform_Year the Perform_Year to set
     */
    public void setPerform_Year(String Perform_Year) {
        this.Perform_Year = Perform_Year;
    }

    /**
     * 実施日 (例: 2013-12-11)
     *
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2013-12-11)
     *
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 12:21:52)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 12:21:52)
     *
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード (例: 0000)
     *
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード (例: 0000)
     *
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * 結果メッセージ (例: 処理終了)
     *
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * 結果メッセージ (例: 処理終了)
     *
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * 患者情報 (例: )
     *
     * @return the Patient_Information
     */
    public PatientInformation getPatient_Information() {
        return Patient_Information;
    }

    /**
     * 患者情報 (例: )
     *
     * @param Patient_Information the Patient_Information to set
     */
    public void setPatient_Information(PatientInformation Patient_Information) {
        this.Patient_Information = Patient_Information;
    }

    /**
     * 請求情報オーバーフラグ (例: false)
     *
     * @return the Income_Information_Overflow
     */
    public String getIncome_Information_Overflow() {
        return Income_Information_Overflow;
    }

    /**
     * 請求情報オーバーフラグ (例: false)
     *
     * @param Income_Information_Overflow the Income_Information_Overflow to set
     */
    public void setIncome_Information_Overflow(String Income_Information_Overflow) {
        this.Income_Information_Overflow = Income_Information_Overflow;
    }

    /**
     * 請求情報（繰り返し　２００）（並び順は診療日の古い順） (例: )
     *
     * @return the Income_Information
     */
    public IncomeInformation[] getIncome_Information() {
        return Income_Information;
    }

    /**
     * 請求情報（繰り返し　２００）（並び順は診療日の古い順） (例: )
     *
     * @param Income_Information the Income_Information to set
     */
    public void setIncome_Information(IncomeInformation[] Income_Information) {
        this.Income_Information = Income_Information;
    }

    /**
     * 保険組合せ詳細（繰り返し　２０） (例: )
     *
     * @return the Insurance_Information
     */
    public HealthInsuranceInformation[] getInsurance_Information() {
        return Insurance_Information;
    }

    /**
     * 保険組合せ詳細（繰り返し　２０） (例: )
     *
     * @param Insurance_Information the Insurance_Information to set
     */
    public void setInsurance_Information(HealthInsuranceInformation[] Insurance_Information) {
        this.Insurance_Information = Insurance_Information;
    }

    /**
     * 未収金額合計（０件の場合は非表示） (例: )
     *
     * @return the Unpaid_Money_Total
     */
    public String getUnpaid_Money_Total() {
        return Unpaid_Money_Total;
    }

    /**
     * 未収金額合計（０件の場合は非表示） (例: )
     *
     * @param Unpaid_Money_Total the Unpaid_Money_Total to set
     */
    public void setUnpaid_Money_Total(String Unpaid_Money_Total) {
        this.Unpaid_Money_Total = Unpaid_Money_Total;
    }

    /**
     * 未収金情報オーバーフラグ（０件：非表示、１〜５０件：false、５０件超：true） (例: )
     *
     * @return the Unpaid_Money_Information_Overflow
     */
    public String getUnpaid_Money_Information_Overflow() {
        return Unpaid_Money_Information_Overflow;
    }

    /**
     * 未収金情報オーバーフラグ（０件：非表示、１〜５０件：false、５０件超：true） (例: )
     *
     * @param Unpaid_Money_Information_Overflow the Unpaid_Money_Information_Overflow to set
     */
    public void setUnpaid_Money_Information_Overflow(String Unpaid_Money_Information_Overflow) {
        this.Unpaid_Money_Information_Overflow = Unpaid_Money_Information_Overflow;
    }

    /**
     * 個別の未収金情報（繰り返し　５０）（診療日の新しい順） (例: )
     *
     * @return the Unpaid_Money_Information
     */
    public UnpaidMoneyInformation[] getUnpaid_Money_Information() {
        return Unpaid_Money_Information;
    }

    /**
     * 個別の未収金情報（繰り返し　５０）（診療日の新しい順） (例: )
     *
     * @param Unpaid_Money_Information the Unpaid_Money_Information to set
     */
    public void setUnpaid_Money_Information(UnpaidMoneyInformation[] Unpaid_Money_Information) {
        this.Unpaid_Money_Information = Unpaid_Money_Information;
    }

    /**
     * 日レセバージョン((M96)マスタ更新管理一覧画面のDB管理情報ORCAver) (例: 040700-1)
     *
     * @return the Jma_Receipt_Version
     */
    public String getJma_Receipt_Version() {
        return Jma_Receipt_Version;
    }

    /**
     * 日レセバージョン((M96)マスタ更新管理一覧画面のDB管理情報ORCAver) (例: 040700-1)
     *
     * @param Jma_Receipt_Version the Jma_Receipt_Version to set
     */
    public void setJma_Receipt_Version(String Jma_Receipt_Version) {
        this.Jma_Receipt_Version = Jma_Receipt_Version;
    }

    /**
     * データベース情報 (例:  )
     *
     * @return the Database_Information
     */
    public DatabaseInformation getDatabase_Information() {
        return Database_Information;
    }

    /**
     * データベース情報 (例:  )
     *
     * @param Database_Information the Database_Information to set
     */
    public void setDatabase_Information(DatabaseInformation Database_Information) {
        this.Database_Information = Database_Information;
    }

    /**
     * マスタ更新情報 (例:  )
     *
     * @return the Master_Update_Information
     */
    public MasterUpdateInformation getMaster_Update_Information() {
        return Master_Update_Information;
    }

    /**
     * マスタ更新情報 (例:  )
     *
     * @param Master_Update_Information the Master_Update_Information to set
     */
    public void setMaster_Update_Information(MasterUpdateInformation Master_Update_Information) {
        this.Master_Update_Information = Master_Update_Information;
    }

    /**
     * プログラム更新情報(繰り返し10) (例:  )
     *
     * @return the Program_Update_Information
     */
    public ProgramUpdateInformation[] getProgram_Update_Information() {
        return Program_Update_Information;
    }

    /**
     * プログラム更新情報(繰り返し10) (例:  )
     *
     * @param Program_Update_Information the Program_Update_Information to set
     */
    public void setProgram_Update_Information(ProgramUpdateInformation[] Program_Update_Information) {
        this.Program_Update_Information = Program_Update_Information;
    }
}
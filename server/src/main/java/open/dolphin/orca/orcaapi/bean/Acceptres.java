package open.dolphin.orca.orcaapi.bean;

/**
 * acceptres.
 *
 * @author pns
 */
public class Acceptres {
    /**
     * 実施日 (例: 2015-12-07)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 20:21:38)
     */
    private String Information_Time;

    /**
     * 結果コード（ゼロ以外エラー） (例: K1)
     */
    private String Api_Result;

    /**
     * エラーメッセージ (例: 受付登録終了)
     */
    private String Api_Result_Message;

    /**
     * 警告メッセージ情報（繰り返し　５） (例:  )
     */
    private ApiWarningMessageInformation[] Api_Warning_Message_Information;

    /**
     *   (例: Acceptance_Info)
     */
    private String Reskey;

    /**
     * 受付日 (例: 2015-12-07)
     */
    private String Acceptance_Date;

    /**
     * 受付時間 (例: 20:21:38)
     */
    private String Acceptance_Time;

    /**
     * 受付ID (例: 00001)
     */
    private String Acceptance_Id;

    /**
     * 診療科コード※５(01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: 内科)
     */
    private String Department_WholeName;

    /**
     * ドクターコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * ドクター名 (例: 日本　一)
     */
    private String Physician_WholeName;

    /**
     * 診療内容区分※６(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     */
    private String Medical_Information;

    /**
     * 患者基本情報 (例:  )
     */
    private PatientInformation Patient_Information;

    /**
     * 保険組合せ情報(繰り返し20） (例:  )
     */
    private HealthInsuranceInformation[] HealthInsurance_Information;

    /**
     * 実施日 (例: 2015-12-07)
     *
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2015-12-07)
     *
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 20:21:38)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 20:21:38)
     *
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: K1)
     *
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: K1)
     *
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * エラーメッセージ (例: 受付登録終了)
     *
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * エラーメッセージ (例: 受付登録終了)
     *
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * 警告メッセージ情報（繰り返し　５） (例:  )
     *
     * @return the Api_Warning_Message_Information
     */
    public ApiWarningMessageInformation[] getApi_Warning_Message_Information() {
        return Api_Warning_Message_Information;
    }

    /**
     * 警告メッセージ情報（繰り返し　５） (例:  )
     *
     * @param Api_Warning_Message_Information the Api_Warning_Message_Information to set
     */
    public void setApi_Warning_Message_Information(ApiWarningMessageInformation[] Api_Warning_Message_Information) {
        this.Api_Warning_Message_Information = Api_Warning_Message_Information;
    }

    /**
     *   (例: Acceptance_Info)
     *
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     *   (例: Acceptance_Info)
     *
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 受付日 (例: 2015-12-07)
     *
     * @return the Acceptance_Date
     */
    public String getAcceptance_Date() {
        return Acceptance_Date;
    }

    /**
     * 受付日 (例: 2015-12-07)
     *
     * @param Acceptance_Date the Acceptance_Date to set
     */
    public void setAcceptance_Date(String Acceptance_Date) {
        this.Acceptance_Date = Acceptance_Date;
    }

    /**
     * 受付時間 (例: 20:21:38)
     *
     * @return the Acceptance_Time
     */
    public String getAcceptance_Time() {
        return Acceptance_Time;
    }

    /**
     * 受付時間 (例: 20:21:38)
     *
     * @param Acceptance_Time the Acceptance_Time to set
     */
    public void setAcceptance_Time(String Acceptance_Time) {
        this.Acceptance_Time = Acceptance_Time;
    }

    /**
     * 受付ID (例: 00001)
     *
     * @return the Acceptance_Id
     */
    public String getAcceptance_Id() {
        return Acceptance_Id;
    }

    /**
     * 受付ID (例: 00001)
     *
     * @param Acceptance_Id the Acceptance_Id to set
     */
    public void setAcceptance_Id(String Acceptance_Id) {
        this.Acceptance_Id = Acceptance_Id;
    }

    /**
     * 診療科コード※５(01:内科) (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード※５(01:内科) (例: 01)
     *
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 診療科名称 (例: 内科)
     *
     * @return the Department_WholeName
     */
    public String getDepartment_WholeName() {
        return Department_WholeName;
    }

    /**
     * 診療科名称 (例: 内科)
     *
     * @param Department_WholeName the Department_WholeName to set
     */
    public void setDepartment_WholeName(String Department_WholeName) {
        this.Department_WholeName = Department_WholeName;
    }

    /**
     * ドクターコード (例: 10001)
     *
     * @return the Physician_Code
     */
    public String getPhysician_Code() {
        return Physician_Code;
    }

    /**
     * ドクターコード (例: 10001)
     *
     * @param Physician_Code the Physician_Code to set
     */
    public void setPhysician_Code(String Physician_Code) {
        this.Physician_Code = Physician_Code;
    }

    /**
     * ドクター名 (例: 日本　一)
     *
     * @return the Physician_WholeName
     */
    public String getPhysician_WholeName() {
        return Physician_WholeName;
    }

    /**
     * ドクター名 (例: 日本　一)
     *
     * @param Physician_WholeName the Physician_WholeName to set
     */
    public void setPhysician_WholeName(String Physician_WholeName) {
        this.Physician_WholeName = Physician_WholeName;
    }

    /**
     * 診療内容区分※６(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     *
     * @return the Medical_Information
     */
    public String getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療内容区分※６(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     *
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(String Medical_Information) {
        this.Medical_Information = Medical_Information;
    }

    /**
     * 患者基本情報 (例:  )
     *
     * @return the Patient_Information
     */
    public PatientInformation getPatient_Information() {
        return Patient_Information;
    }

    /**
     * 患者基本情報 (例:  )
     *
     * @param Patient_Information the Patient_Information to set
     */
    public void setPatient_Information(PatientInformation Patient_Information) {
        this.Patient_Information = Patient_Information;
    }

    /**
     * 保険組合せ情報(繰り返し20） (例:  )
     *
     * @return the HealthInsurance_Information
     */
    public HealthInsuranceInformation[] getHealthInsurance_Information() {
        return HealthInsurance_Information;
    }

    /**
     * 保険組合せ情報(繰り返し20） (例:  )
     *
     * @param HealthInsurance_Information the HealthInsurance_Information to set
     */
    public void setHealthInsurance_Information(HealthInsuranceInformation[] HealthInsurance_Information) {
        this.HealthInsurance_Information = HealthInsurance_Information;
    }
}
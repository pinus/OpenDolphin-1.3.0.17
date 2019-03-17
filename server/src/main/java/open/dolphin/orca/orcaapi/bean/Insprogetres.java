package open.dolphin.orca.orcaapi.bean;

/**
 * insprogetres.
 *
 * @author pns
 */
public class Insprogetres {
    /**
     * 実施日 (例: 2013-10-11)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 10:20:23)
     */
    private String Information_Time;

    /**
     * 結果コード（ゼロ以外エラー） (例: 00)
     */
    private String Api_Result;

    /**
     * エラーメッセージ (例: 処理終了)
     */
    private String Api_Result_Message;

    /**
     *   (例: MedicalInfo)
     */
    private String Reskey;

    /**
     * 保険者一覧情報（繰り返し２５００） (例:  )
     */
    private TinsuranceproviderInformation[] TInsuranceProvider_Information;

    /**
     * 実施日 (例: 2013-10-11)
     *
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2013-10-11)
     *
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 10:20:23)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 10:20:23)
     *
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: 00)
     *
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: 00)
     *
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * エラーメッセージ (例: 処理終了)
     *
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * エラーメッセージ (例: 処理終了)
     *
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     *   (例: MedicalInfo)
     *
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     *   (例: MedicalInfo)
     *
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 保険者一覧情報（繰り返し２５００） (例:  )
     *
     * @return the TInsuranceProvider_Information
     */
    public TinsuranceproviderInformation[] getTInsuranceProvider_Information() {
        return TInsuranceProvider_Information;
    }

    /**
     * 保険者一覧情報（繰り返し２５００） (例:  )
     *
     * @param TInsuranceProvider_Information the TInsuranceProvider_Information to set
     */
    public void setTInsuranceProvider_Information(TinsuranceproviderInformation[] TInsuranceProvider_Information) {
        this.TInsuranceProvider_Information = TInsuranceProvider_Information;
    }
}
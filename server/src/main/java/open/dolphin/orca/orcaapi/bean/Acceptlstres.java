package open.dolphin.orca.orcaapi.bean;

/**
 * acceptlstres.
 *
 * @author pns
 */
public class Acceptlstres {
    /**
     * 実施日 (例: 2011-03-13)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 10:50:00)
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
     *   (例: PatientInfo)
     */
    private String Reskey;

    /**
     * 受付日 (例: 2011-03-15)
     */
    private String Acceptance_Date;

    /**
     * 受付一覧情報(繰り返し500) (例:  )
     */
    private AcceptlstInformation[] Acceptlst_Information;

    /**
     * 実施日 (例: 2011-03-13)
     *
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2011-03-13)
     *
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 10:50:00)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 10:50:00)
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
     *   (例: PatientInfo)
     *
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     *   (例: PatientInfo)
     *
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 受付日 (例: 2011-03-15)
     *
     * @return the Acceptance_Date
     */
    public String getAcceptance_Date() {
        return Acceptance_Date;
    }

    /**
     * 受付日 (例: 2011-03-15)
     *
     * @param Acceptance_Date the Acceptance_Date to set
     */
    public void setAcceptance_Date(String Acceptance_Date) {
        this.Acceptance_Date = Acceptance_Date;
    }

    /**
     * 受付一覧情報(繰り返し500) (例:  )
     *
     * @return the Acceptlst_Information
     */
    public AcceptlstInformation[] getAcceptlst_Information() {
        return Acceptlst_Information;
    }

    /**
     * 受付一覧情報(繰り返し500) (例:  )
     *
     * @param Acceptlst_Information the Acceptlst_Information to set
     */
    public void setAcceptlst_Information(AcceptlstInformation[] Acceptlst_Information) {
        this.Acceptlst_Information = Acceptlst_Information;
    }
}
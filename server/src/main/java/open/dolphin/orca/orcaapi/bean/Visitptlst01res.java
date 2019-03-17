package open.dolphin.orca.orcaapi.bean;

/**
 * visitptlst01res.
 *
 * @author pns
 */
public class Visitptlst01res {
    /**
     * 実施日 (例: 2013-09-06)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 13:29:09)
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
     * 来院日付 (例: 2003-01-14)
     */
    private String Visit_Date;

    /**
     * 来院一覧情報(繰り返し500) (例:  )
     */
    private VisitListInformation[] Visit_List_Information;

    /**
     * 実施日 (例: 2013-09-06)
     *
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2013-09-06)
     *
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 13:29:09)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 13:29:09)
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
     * 来院日付 (例: 2003-01-14)
     *
     * @return the Visit_Date
     */
    public String getVisit_Date() {
        return Visit_Date;
    }

    /**
     * 来院日付 (例: 2003-01-14)
     *
     * @param Visit_Date the Visit_Date to set
     */
    public void setVisit_Date(String Visit_Date) {
        this.Visit_Date = Visit_Date;
    }

    /**
     * 来院一覧情報(繰り返し500) (例:  )
     *
     * @return the Visit_List_Information
     */
    public VisitListInformation[] getVisit_List_Information() {
        return Visit_List_Information;
    }

    /**
     * 来院一覧情報(繰り返し500) (例:  )
     *
     * @param Visit_List_Information the Visit_List_Information to set
     */
    public void setVisit_List_Information(VisitListInformation[] Visit_List_Information) {
        this.Visit_List_Information = Visit_List_Information;
    }
}
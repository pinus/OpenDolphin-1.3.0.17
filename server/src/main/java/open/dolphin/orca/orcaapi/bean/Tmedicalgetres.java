package open.dolphin.orca.orcaapi.bean;

/**
 * tmedicalgetres.
 * @author pns
 */
public class Tmedicalgetres {
    /**
     * 実施日 (例: 2013-10-02)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 17:33:58)
     */
    private String Information_Time;

    /**
     * 結果コード (例: 00)
     */
    private String Api_Result;

    /**
     * 結果メッセージ (例: 処理終了)
     */
    private String Api_Result_Message;

    /**
     * レスポンスキー情報 (例: MedicalInfo)
     */
    private String Reskey;

    /**
     * 診療日 (例: 2013-10-02)
     */
    private String Perform_Date;

    /**
     * 入院外来区分 (例: 2)
     */
    private String InOut;

    /**
     * 中途データ一覧情報（繰り返し５００） (例:  )
     */
    private TmedicalListInformation[] Tmedical_List_Information;

    /**
     * 実施日 (例: 2013-10-02)
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2013-10-02)
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 17:33:58)
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 17:33:58)
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード (例: 00)
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード (例: 00)
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * 結果メッセージ (例: 処理終了)
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * 結果メッセージ (例: 処理終了)
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * レスポンスキー情報 (例: MedicalInfo)
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     * レスポンスキー情報 (例: MedicalInfo)
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 診療日 (例: 2013-10-02)
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療日 (例: 2013-10-02)
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 入院外来区分 (例: 2)
     * @return the InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * 入院外来区分 (例: 2)
     * @param InOut the InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
    }

    /**
     * 中途データ一覧情報（繰り返し５００） (例:  )
     * @return the Tmedical_List_Information
     */
    public TmedicalListInformation[] getTmedical_List_Information() {
        return Tmedical_List_Information;
    }

    /**
     * 中途データ一覧情報（繰り返し５００） (例:  )
     * @param Tmedical_List_Information the Tmedical_List_Information to set
     */
    public void setTmedical_List_Information(TmedicalListInformation[] Tmedical_List_Information) {
        this.Tmedical_List_Information = Tmedical_List_Information;
    }
}
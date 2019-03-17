package open.dolphin.orca.orcaapi.bean;

/**
 * masterlastupdatev3reS.
 *
 * @author pns
 */
public class Masterlastupdatev3res {
    /**
     * 実施日 (例: 2017-08-22)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 10:27:33)
     */
    private String Information_Time;

    /**
     * 結果コードゼロ以外エラー (例: 0000)
     */
    private String Api_Result;

    /**
     * エラーメッセージ (例: 正常終了)
     */
    private String Api_Result_Message;

    /**
     * マスタ最終更新日情報リクエストのマスタIDが設定されていない場合に設定（繰り返し20） (例: )
     */
    private MasterLastUpdateInformation[] Master_Last_Update_Information;

    /**
     * マスタIDリクエストのマスタIDが設定されている場合に設定 (例: medication_master)
     */
    private String Master_Id;

    /**
     * 最終更新日リクエストのマスタIDが設定されている場合に設定 (例: 2017-10-11)
     */
    private String Last_Update_Date;

    /**
     * 更新日情報リクエストのマスタIDが設定されている場合に設定（繰り返し5） (例: )
     */
    private UpdateInformation[] Update_Information;

    /**
     * Information_Date
     *
     * @return Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * Information_Date
     *
     * @param Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * Information_Time
     *
     * @return Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * Information_Time
     *
     * @param Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * Api_Result
     *
     * @return Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * Api_Result
     *
     * @param Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * Api_Result_Message
     *
     * @return Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * Api_Result_Message
     *
     * @param Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * Master_Last_Update_Information
     *
     * @return Master_Last_Update_Information
     */
    public MasterLastUpdateInformation[] getMaster_Last_Update_Information() {
        return Master_Last_Update_Information;
    }

    /**
     * Master_Last_Update_Information
     *
     * @param Master_Last_Update_Information to set
     */
    public void setMaster_Last_Update_Information(MasterLastUpdateInformation[] Master_Last_Update_Information) {
        this.Master_Last_Update_Information = Master_Last_Update_Information;
    }

    /**
     * Master_Id
     *
     * @return Master_Id
     */
    public String getMaster_Id() {
        return Master_Id;
    }

    /**
     * Master_Id
     *
     * @param Master_Id to set
     */
    public void setMaster_Id(String Master_Id) {
        this.Master_Id = Master_Id;
    }

    /**
     * Last_Update_Date
     *
     * @return Last_Update_Date
     */
    public String getLast_Update_Date() {
        return Last_Update_Date;
    }

    /**
     * Last_Update_Date
     *
     * @param Last_Update_Date to set
     */
    public void setLast_Update_Date(String Last_Update_Date) {
        this.Last_Update_Date = Last_Update_Date;
    }

    /**
     * Update_Information
     *
     * @return Update_Information
     */
    public UpdateInformation[] getUpdate_Information() {
        return Update_Information;
    }

    /**
     * Update_Information
     *
     * @param Update_Information to set
     */
    public void setUpdate_Information(UpdateInformation[] Update_Information) {
        this.Update_Information = Update_Information;
    }
}

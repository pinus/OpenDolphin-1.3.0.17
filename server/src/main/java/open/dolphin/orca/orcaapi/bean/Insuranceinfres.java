package open.dolphin.orca.orcaapi.bean;

/**
 * insuranceinfres.
 * @author pns
 */
public class Insuranceinfres {
    /**
     * 実施日 (例: )
     */
    private String Information_Date;

    /**
     * 実施時間 (例: )
     */
    private String Information_Time;

    /**
     * エラーコード (例: )
     */
    private String Api_Result;

    /**
     * メッセージ (例: )
     */
    private String Api_Result_Message;

    /**
     *  (例: )
     */
    private String Reskey;

    /**
     *  (例: )
     */
    private InsuranceInformation Insurance_Information;

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
     * Reskey
     *
     * @return Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     * Reskey
     *
     * @param Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * Insurance_Information
     *
     * @return Insurance_Information
     */
    public InsuranceInformation getInsurance_Information() {
        return Insurance_Information;
    }

    /**
     * Insurance_Information
     *
     * @param Insurance_Information to set
     */
    public void setInsurance_Information(InsuranceInformation Insurance_Information) {
        this.Insurance_Information = Insurance_Information;
    }
}
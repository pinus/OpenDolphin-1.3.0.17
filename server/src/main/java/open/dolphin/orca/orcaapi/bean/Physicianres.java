package open.dolphin.orca.orcaapi.bean;

/**
 * physicianres.
 * @author pns
 */
public class Physicianres {
    /**
     * 実施日 (例: 2013-03-13)
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
     * 基準日 (例: 2012-06-01)
     */
    private String Base_Date;

    /**
     * ドクター情報(繰り返し100) (例:  )
     */
    private PhysicianInformation[] Physician_Information;

    /**
     * 実施日 (例: 2013-03-13)
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2013-03-13)
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 10:50:00)
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 10:50:00)
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: 00)
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: 00)
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * エラーメッセージ (例: 処理終了)
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * エラーメッセージ (例: 処理終了)
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     *   (例: PatientInfo)
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     *   (例: PatientInfo)
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 基準日 (例: 2012-06-01)
     * @return the Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * 基準日 (例: 2012-06-01)
     * @param Base_Date the Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }

    /**
     * ドクター情報(繰り返し100) (例:  )
     * @return the Physician_Information
     */
    public PhysicianInformation[] getPhysician_Information() {
        return Physician_Information;
    }

    /**
     * ドクター情報(繰り返し100) (例:  )
     * @param Physician_Information the Physician_Information to set
     */
    public void setPhysician_Information(PhysicianInformation[] Physician_Information) {
        this.Physician_Information = Physician_Information;
    }
}
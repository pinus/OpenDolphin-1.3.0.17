package open.dolphin.orca.orcaapi.bean;

/**
 * medicalsetres.
 *
 * @author pns
 */
public class Medicalsetres {
    /**
     * 実施日 (例: 2015-12-08)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 15:15:43)
     */
    private String Information_Time;

    /**
     * 結果コード（ゼロ以外エラー） (例: E21)
     */
    private String Api_Result;

    /**
     * エラーメッセージ (例: セット内容に誤りがあります。)
     */
    private String Api_Result_Message;

    /**
     * (例: MedicalInfo)
     */
    private String Reskey;

    /**
     * 基準日 (例: 2015-12-08)
     */
    private String Base_Date;

    /**
     * セットコード (例: PAI004)
     */
    private String Set_Code;

    /**
     * セット名称 (例: APIセット３)
     */
    private String Set_Code_Name;

    /**
     * 開始日付 (例: 2013-04-01)
     */
    private String Start_Date;

    /**
     * 終了日付 (例: 9999-12-31)
     */
    private String Ende_Date;

    /**
     * 診療行為登録内容 (例: )
     */
    private MedicalInformation4 Medical_Information;

    /**
     * メッセージ内容 (例: )
     */
    private MedicalMessageInformation Medical_Message_Information;

    /**
     * 実施日 (例: 2015-12-08)
     *
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2015-12-08)
     *
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 15:15:43)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 15:15:43)
     *
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: E21)
     *
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: E21)
     *
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * エラーメッセージ (例: セット内容に誤りがあります。)
     *
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * エラーメッセージ (例: セット内容に誤りがあります。)
     *
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * (例: MedicalInfo)
     *
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     * (例: MedicalInfo)
     *
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 基準日 (例: 2015-12-08)
     *
     * @return the Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * 基準日 (例: 2015-12-08)
     *
     * @param Base_Date the Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }

    /**
     * セットコード (例: PAI004)
     *
     * @return the Set_Code
     */
    public String getSet_Code() {
        return Set_Code;
    }

    /**
     * セットコード (例: PAI004)
     *
     * @param Set_Code the Set_Code to set
     */
    public void setSet_Code(String Set_Code) {
        this.Set_Code = Set_Code;
    }

    /**
     * セット名称 (例: APIセット３)
     *
     * @return the Set_Code_Name
     */
    public String getSet_Code_Name() {
        return Set_Code_Name;
    }

    /**
     * セット名称 (例: APIセット３)
     *
     * @param Set_Code_Name the Set_Code_Name to set
     */
    public void setSet_Code_Name(String Set_Code_Name) {
        this.Set_Code_Name = Set_Code_Name;
    }

    /**
     * 開始日付 (例: 2013-04-01)
     *
     * @return the Start_Date
     */
    public String getStart_Date() {
        return Start_Date;
    }

    /**
     * 開始日付 (例: 2013-04-01)
     *
     * @param Start_Date the Start_Date to set
     */
    public void setStart_Date(String Start_Date) {
        this.Start_Date = Start_Date;
    }

    /**
     * 終了日付 (例: 9999-12-31)
     *
     * @return the Ende_Date
     */
    public String getEnde_Date() {
        return Ende_Date;
    }

    /**
     * 終了日付 (例: 9999-12-31)
     *
     * @param Ende_Date the Ende_Date to set
     */
    public void setEnde_Date(String Ende_Date) {
        this.Ende_Date = Ende_Date;
    }

    /**
     * 診療行為登録内容 (例: )
     *
     * @return the Medical_Information
     */
    public MedicalInformation4 getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療行為登録内容 (例: )
     *
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(MedicalInformation4 Medical_Information) {
        this.Medical_Information = Medical_Information;
    }

    /**
     * メッセージ内容 (例: )
     *
     * @return the Medical_Message_Information
     */
    public MedicalMessageInformation getMedical_Message_Information() {
        return Medical_Message_Information;
    }

    /**
     * メッセージ内容 (例: )
     *
     * @param Medical_Message_Information the Medical_Message_Information to set
     */
    public void setMedical_Message_Information(MedicalMessageInformation Medical_Message_Information) {
        this.Medical_Message_Information = Medical_Message_Information;
    }
}
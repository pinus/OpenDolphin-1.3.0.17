package open.dolphin.orca.orcaapi.bean;

/**
 * medicalsetreq.
 * @author pns
 */
public class Medicalsetreq {
    /**
     * リクエスト番号　01：新規登録　02：削除　03：最終終了日更新　04：セット内容取得 (例: 01)
     */
    private String Request_Number;

    /**
     * 基準日 (例: )
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
     * 入外区分（I：入院分） (例: I)
     */
    private String InOut;

    /**
     * 診療行為情報 (例: )
     */
    private MedicalInformation4 Medical_Information;

    /**
     * リクエスト番号　01：新規登録　02：削除　03：最終終了日更新　04：セット内容取得 (例: 01)
     * @return the Request_Number
     */
    public String getRequest_Number() {
        return Request_Number;
    }

    /**
     * リクエスト番号　01：新規登録　02：削除　03：最終終了日更新　04：セット内容取得 (例: 01)
     * @param Request_Number the Request_Number to set
     */
    public void setRequest_Number(String Request_Number) {
        this.Request_Number = Request_Number;
    }

    /**
     * 基準日 (例: )
     * @return the Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * 基準日 (例: )
     * @param Base_Date the Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }

    /**
     * セットコード (例: PAI004)
     * @return the Set_Code
     */
    public String getSet_Code() {
        return Set_Code;
    }

    /**
     * セットコード (例: PAI004)
     * @param Set_Code the Set_Code to set
     */
    public void setSet_Code(String Set_Code) {
        this.Set_Code = Set_Code;
    }

    /**
     * セット名称 (例: APIセット３)
     * @return the Set_Code_Name
     */
    public String getSet_Code_Name() {
        return Set_Code_Name;
    }

    /**
     * セット名称 (例: APIセット３)
     * @param Set_Code_Name the Set_Code_Name to set
     */
    public void setSet_Code_Name(String Set_Code_Name) {
        this.Set_Code_Name = Set_Code_Name;
    }

    /**
     * 開始日付 (例: 2013-04-01)
     * @return the Start_Date
     */
    public String getStart_Date() {
        return Start_Date;
    }

    /**
     * 開始日付 (例: 2013-04-01)
     * @param Start_Date the Start_Date to set
     */
    public void setStart_Date(String Start_Date) {
        this.Start_Date = Start_Date;
    }

    /**
     * 終了日付 (例: 9999-12-31)
     * @return the Ende_Date
     */
    public String getEnde_Date() {
        return Ende_Date;
    }

    /**
     * 終了日付 (例: 9999-12-31)
     * @param Ende_Date the Ende_Date to set
     */
    public void setEnde_Date(String Ende_Date) {
        this.Ende_Date = Ende_Date;
    }

    /**
     * 入外区分（I：入院分） (例: I)
     * @return the InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * 入外区分（I：入院分） (例: I)
     * @param InOut the InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
    }

    /**
     * 診療行為情報 (例: )
     * @return the Medical_Information
     */
    public MedicalInformation4 getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療行為情報 (例: )
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(MedicalInformation4 Medical_Information) {
        this.Medical_Information = Medical_Information;
    }
}
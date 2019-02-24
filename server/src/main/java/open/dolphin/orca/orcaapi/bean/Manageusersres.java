package open.dolphin.orca.orcaapi.bean;

/**
 * manageusersres.
 * @author pns
 */
public class Manageusersres {
    /**
     * 実施日 (例: 2015-09-01)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 17:32:34)
     */
    private String Information_Time;

    /**
     * 結果コード（ゼロ以外エラー） (例: 0000)
     */
    private String Api_Result;

    /**
     * エラーメッセージ (例: 処理終了)
     */
    private String Api_Result_Message;

    /**
     * リクエスト番号 (例: 01)
     */
    private String Request_Number;

    /**
     * リクエストの基準日 (例: 2015-09-01)
     */
    private String Base_Date;

    /**
     * ユーザー情報（繰り返し最大６００件） (例: )
     */
    private UserInformation[] User_Information;

    /**
     * メニュー項目名称情報（繰り返し　最大５０） (例:  )
     */
    private MenuItemNameInformation[] Menu_Item_Name_Information;

    /**
     * 実施日 (例: 2015-09-01)
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2015-09-01)
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 17:32:34)
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 17:32:34)
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: 0000)
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: 0000)
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
     * リクエスト番号 (例: 01)
     * @return the Request_Number
     */
    public String getRequest_Number() {
        return Request_Number;
    }

    /**
     * リクエスト番号 (例: 01)
     * @param Request_Number the Request_Number to set
     */
    public void setRequest_Number(String Request_Number) {
        this.Request_Number = Request_Number;
    }

    /**
     * リクエストの基準日 (例: 2015-09-01)
     * @return the Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * リクエストの基準日 (例: 2015-09-01)
     * @param Base_Date the Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }

    /**
     * ユーザー情報（繰り返し最大６００件） (例: )
     * @return the User_Information
     */
    public UserInformation[] getUser_Information() {
        return User_Information;
    }

    /**
     * ユーザー情報（繰り返し最大６００件） (例: )
     * @param User_Information the User_Information to set
     */
    public void setUser_Information(UserInformation[] User_Information) {
        this.User_Information = User_Information;
    }

    /**
     * Menu_Item_Name_Information
     *
     * @return Menu_Item_Name_Information
     */
    public MenuItemNameInformation[] getMenu_Item_Name_Information() {
        return Menu_Item_Name_Information;
    }

    /**
     * Menu_Item_Name_Information
     *
     * @param Menu_Item_Name_Information to set
     */
    public void setMenu_Item_Name_Information(MenuItemNameInformation[] Menu_Item_Name_Information) {
        this.Menu_Item_Name_Information = Menu_Item_Name_Information;
    }
}
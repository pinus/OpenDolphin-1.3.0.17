package open.dolphin.orca.orcaapi.bean;

/**
 * manageusersreq.
 *
 * @author pns
 */
public class Manageusersreq {
    /**
     * リクエスト番号　01：ユーザー一覧　02：ユーザー登録　03：ユーザー変更　04：ユーザー削除 (例: 01)
     */
    private String Request_Number;

    /**
     * 基準日 (例: 2015-09-01)
     */
    private String Base_Date;

    /**
     * ユーザー情報 (例: )
     */
    private UserInformation User_Information;

    /**
     * リクエスト番号　01：ユーザー一覧　02：ユーザー登録　03：ユーザー変更　04：ユーザー削除 (例: 01)
     *
     * @return the Request_Number
     */
    public String getRequest_Number() {
        return Request_Number;
    }

    /**
     * リクエスト番号　01：ユーザー一覧　02：ユーザー登録　03：ユーザー変更　04：ユーザー削除 (例: 01)
     *
     * @param Request_Number the Request_Number to set
     */
    public void setRequest_Number(String Request_Number) {
        this.Request_Number = Request_Number;
    }

    /**
     * 基準日 (例: 2015-09-01)
     *
     * @return the Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * 基準日 (例: 2015-09-01)
     *
     * @param Base_Date the Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }

    /**
     * ユーザー情報 (例: )
     *
     * @return the User_Information
     */
    public UserInformation getUser_Information() {
        return User_Information;
    }

    /**
     * ユーザー情報 (例: )
     *
     * @param User_Information the User_Information to set
     */
    public void setUser_Information(UserInformation User_Information) {
        this.User_Information = User_Information;
    }
}
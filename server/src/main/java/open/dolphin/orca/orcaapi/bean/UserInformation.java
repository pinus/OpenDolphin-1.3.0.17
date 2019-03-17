package open.dolphin.orca.orcaapi.bean;

/**
 * User_Information. ユーザー情報
 *
 * @author pns
 */
public class UserInformation {
    /**
     * ユーザーID (例: taro)
     */
    private String User_Id;

    /**
     * システム管理[1010職員情報]職員番号 (例: 0001)
     */
    private String User_Number;

    /**
     * ユーザーパスワード (例: taropassword)
     */
    private String User_Password;

    /**
     * システム管理[1010職員情報]職員区分(0：マスター、1：医師、2：看護師、3：技師、4：事務職、5：管理職) (例: 1)
     */
    private String Group_Number;

    /**
     * ユーザー氏名 (例: 日医　太郎)
     */
    private String Full_Name;

    /**
     * ユーザーカナ氏名 (例: ニチイ　タロウ)
     */
    private String Kana_Name;

    /**
     * システム管理［1010職員情報］管理者権限0:管理者でない1:管理者である (例: 1)
     */
    private String Administrator_Privilege;

    /**
     * メニュー項目情報（繰り返し　最大５０） (例:  )
     */
    private MenuItemInformation[] Menu_Item_Information;

    /**
     * 新しいユーザーID (例: jiro)
     */
    private String New_User_Id;

    /**
     * 新しいユーザーパスワード (例: jiropassword)
     */
    private String New_User_Password;

    /**
     * 新しいユーザー氏名 (例: 日医　次郎)
     */
    private String New_Full_Name;

    /**
     * 新しいユーザーカナ氏名 (例: ニチイ　ジロウ)
     */
    private String New_Kana_Name;

    /**
     * 医療機関識別番号 (例: )
     */
    private String Hospital_Id_Number;

    /**
     * システム管理[1010職員情報]有効開始日 (例: 2015-09-01)
     */
    private String Start_Date;

    /**
     * システム管理[1010職員情報]有効終了日 (例: 2016-08-31)
     */
    private String Expiry_Date;

    /**
     * ユーザーID (例: taro)
     *
     * @return the User_Id
     */
    public String getUser_Id() {
        return User_Id;
    }

    /**
     * ユーザーID (例: taro)
     *
     * @param User_Id the User_Id to set
     */
    public void setUser_Id(String User_Id) {
        this.User_Id = User_Id;
    }

    /**
     * システム管理[1010職員情報]職員番号 (例: 0001)
     *
     * @return the User_Number
     */
    public String getUser_Number() {
        return User_Number;
    }

    /**
     * システム管理[1010職員情報]職員番号 (例: 0001)
     *
     * @param User_Number the User_Number to set
     */
    public void setUser_Number(String User_Number) {
        this.User_Number = User_Number;
    }

    /**
     * ユーザーパスワード (例: taropassword)
     *
     * @return the User_Password
     */
    public String getUser_Password() {
        return User_Password;
    }

    /**
     * ユーザーパスワード (例: taropassword)
     *
     * @param User_Password the User_Password to set
     */
    public void setUser_Password(String User_Password) {
        this.User_Password = User_Password;
    }

    /**
     * システム管理の職員区分[1010職員情報] (例: 1)
     *
     * @return the Group_Number
     */
    public String getGroup_Number() {
        return Group_Number;
    }

    /**
     * システム管理の職員区分[1010職員情報] (例: 1)
     *
     * @param Group_Number the Group_Number to set
     */
    public void setGroup_Number(String Group_Number) {
        this.Group_Number = Group_Number;
    }

    /**
     * ユーザー氏名 (例: 日医　太郎)
     *
     * @return the Full_Name
     */
    public String getFull_Name() {
        return Full_Name;
    }

    /**
     * ユーザー氏名 (例: 日医　太郎)
     *
     * @param Full_Name the Full_Name to set
     */
    public void setFull_Name(String Full_Name) {
        this.Full_Name = Full_Name;
    }

    /**
     * ユーザーカナ氏名 (例: ニチイ　タロウ)
     *
     * @return the Kana_Name
     */
    public String getKana_Name() {
        return Kana_Name;
    }

    /**
     * ユーザーカナ氏名 (例: ニチイ　タロウ)
     *
     * @param Kana_Name the Kana_Name to set
     */
    public void setKana_Name(String Kana_Name) {
        this.Kana_Name = Kana_Name;
    }

    /**
     * 新しいユーザーID (例: jiro)
     *
     * @return the New_User_Id
     */
    public String getNew_User_Id() {
        return New_User_Id;
    }

    /**
     * 新しいユーザーID (例: jiro)
     *
     * @param New_User_Id the New_User_Id to set
     */
    public void setNew_User_Id(String New_User_Id) {
        this.New_User_Id = New_User_Id;
    }

    /**
     * 新しいユーザーパスワード (例: jiropassword)
     *
     * @return the New_User_Password
     */
    public String getNew_User_Password() {
        return New_User_Password;
    }

    /**
     * 新しいユーザーパスワード (例: jiropassword)
     *
     * @param New_User_Password the New_User_Password to set
     */
    public void setNew_User_Password(String New_User_Password) {
        this.New_User_Password = New_User_Password;
    }

    /**
     * 新しいユーザー氏名 (例: 日医　次郎)
     *
     * @return the New_Full_Name
     */
    public String getNew_Full_Name() {
        return New_Full_Name;
    }

    /**
     * 新しいユーザー氏名 (例: 日医　次郎)
     *
     * @param New_Full_Name the New_Full_Name to set
     */
    public void setNew_Full_Name(String New_Full_Name) {
        this.New_Full_Name = New_Full_Name;
    }

    /**
     * 新しいユーザーカナ氏名 (例: ニチイ　ジロウ)
     *
     * @return the New_Kana_Name
     */
    public String getNew_Kana_Name() {
        return New_Kana_Name;
    }

    /**
     * 新しいユーザーカナ氏名 (例: ニチイ　ジロウ)
     *
     * @param New_Kana_Name the New_Kana_Name to set
     */
    public void setNew_Kana_Name(String New_Kana_Name) {
        this.New_Kana_Name = New_Kana_Name;
    }

    /**
     * 医療機関識別番号 (例: )
     *
     * @return the Hospital_Id_Number
     */
    public String getHospital_Id_Number() {
        return Hospital_Id_Number;
    }

    /**
     * 医療機関識別番号 (例: )
     *
     * @param Hospital_Id_Number the Hospital_Id_Number to set
     */
    public void setHospital_Id_Number(String Hospital_Id_Number) {
        this.Hospital_Id_Number = Hospital_Id_Number;
    }

    /**
     * システム管理[1010職員情報]有効開始日 (例: 2015-09-01)
     *
     * @return the Start_Date
     */
    public String getStart_Date() {
        return Start_Date;
    }

    /**
     * システム管理[1010職員情報]有効開始日 (例: 2015-09-01)
     *
     * @param Start_Date the Start_Date to set
     */
    public void setStart_Date(String Start_Date) {
        this.Start_Date = Start_Date;
    }

    /**
     * システム管理[1010職員情報]有効終了日 (例: 2016-08-31)
     *
     * @return the Expiry_Date
     */
    public String getExpiry_Date() {
        return Expiry_Date;
    }

    /**
     * システム管理[1010職員情報]有効終了日 (例: 2016-08-31)
     *
     * @param Expiry_Date the Expiry_Date to set
     */
    public void setExpiry_Date(String Expiry_Date) {
        this.Expiry_Date = Expiry_Date;
    }

    /**
     * Administrator_Privilege
     *
     * @return Administrator_Privilege
     */
    public String getAdministrator_Privilege() {
        return Administrator_Privilege;
    }

    /**
     * Administrator_Privilege
     *
     * @param Administrator_Privilege to set
     */
    public void setAdministrator_Privilege(String Administrator_Privilege) {
        this.Administrator_Privilege = Administrator_Privilege;
    }

    /**
     * Menu_Item_Information
     *
     * @return Menu_Item_Information
     */
    public MenuItemInformation[] getMenu_Item_Information() {
        return Menu_Item_Information;
    }

    /**
     * Menu_Item_Information
     *
     * @param Menu_Item_Information to set
     */
    public void setMenu_Item_Information(MenuItemInformation[] Menu_Item_Information) {
        this.Menu_Item_Information = Menu_Item_Information;
    }
}
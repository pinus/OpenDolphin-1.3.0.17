package open.dolphin.orca.orcaapi.bean;

/**
 * Master_Version_Information. マスタ構造情報(繰り返し99)
 * @author pns
 */
public class MasterVersionInformation {
    /**
     * (M96)マスタ更新管理一覧画面のマスタ更新管理情報マスタ (例: 点数マスタ)
     */
    private String Name;

    /**
     * (M96)マスタ更新管理一覧画面のマスタ更新管理情報レコードver(自) (例: R-040700-1-20140930-1)
     */
    private String Local_Version;

    /**
     * (M96)マスタ更新管理一覧画面のマスタ更新管理情報レコードver(ORCA) (例: R-040700-1-20140930-1)
     */
    private String New_Version;

    /**
     * (M96)マスタ更新管理一覧画面のマスタ更新管理情報マスタ (例: 点数マスタ)
     * @return the Name
     */
    public String getName() {
        return Name;
    }

    /**
     * (M96)マスタ更新管理一覧画面のマスタ更新管理情報マスタ (例: 点数マスタ)
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * (M96)マスタ更新管理一覧画面のマスタ更新管理情報レコードver(自) (例: R-040700-1-20140930-1)
     * @return the Local_Version
     */
    public String getLocal_Version() {
        return Local_Version;
    }

    /**
     * (M96)マスタ更新管理一覧画面のマスタ更新管理情報レコードver(自) (例: R-040700-1-20140930-1)
     * @param Local_Version the Local_Version to set
     */
    public void setLocal_Version(String Local_Version) {
        this.Local_Version = Local_Version;
    }

    /**
     * (M96)マスタ更新管理一覧画面のマスタ更新管理情報レコードver(ORCA) (例: R-040700-1-20140930-1)
     * @return the New_Version
     */
    public String getNew_Version() {
        return New_Version;
    }

    /**
     * (M96)マスタ更新管理一覧画面のマスタ更新管理情報レコードver(ORCA) (例: R-040700-1-20140930-1)
     * @param New_Version the New_Version to set
     */
    public void setNew_Version(String New_Version) {
        this.New_Version = New_Version;
    }
}
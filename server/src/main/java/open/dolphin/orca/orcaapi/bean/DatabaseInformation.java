package open.dolphin.orca.orcaapi.bean;

/**
 * Database_Information. データベース情報
 *
 * @author pns
 */
public class DatabaseInformation {
    /**
     * (M96)マスタ更新管理一覧画面のDB更新管理情報構造Ver(自) (例: S-040700-1-20140527-2)
     */
    private String Local_Version;

    /**
     * (M96)マスタ更新管理一覧画面のDB更新管理情報構造Ver(ORCA) (例: S-040700-1-20140527-2)
     */
    private String New_Version;

    /**
     * (M96)マスタ更新管理一覧画面のDB更新管理情報構造Ver(自) (例: S-040700-1-20140527-2)
     *
     * @return the Local_Version
     */
    public String getLocal_Version() {
        return Local_Version;
    }

    /**
     * (M96)マスタ更新管理一覧画面のDB更新管理情報構造Ver(自) (例: S-040700-1-20140527-2)
     *
     * @param Local_Version the Local_Version to set
     */
    public void setLocal_Version(String Local_Version) {
        this.Local_Version = Local_Version;
    }

    /**
     * (M96)マスタ更新管理一覧画面のDB更新管理情報構造Ver(ORCA) (例: S-040700-1-20140527-2)
     *
     * @return the New_Version
     */
    public String getNew_Version() {
        return New_Version;
    }

    /**
     * (M96)マスタ更新管理一覧画面のDB更新管理情報構造Ver(ORCA) (例: S-040700-1-20140527-2)
     *
     * @param New_Version the New_Version to set
     */
    public void setNew_Version(String New_Version) {
        this.New_Version = New_Version;
    }
}
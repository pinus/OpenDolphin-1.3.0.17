package open.dolphin.orca.orcaapi.bean;

/**
 * Program_Update_Information. プログラム更新情報(繰り返し10)
 *
 * @author pns
 */
public class ProgramUpdateInformation {
    /**
     * (M97)プログラム更新管理一覧提供日 (例: 2014-10-02)
     */
    private String Date;

    /**
     * (M97)プログラム更新管理一覧処理状態 (例: 済)
     */
    private String State;

    /**
     * (M97)プログラム更新管理一覧内容 (例: 提供されている最新の状態でした。)
     */
    private String Comment;

    /**
     * (M97)プログラム更新管理一覧提供日 (例: 2014-10-02)
     *
     * @return the Date
     */
    public String getDate() {
        return Date;
    }

    /**
     * (M97)プログラム更新管理一覧提供日 (例: 2014-10-02)
     *
     * @param Date the Date to set
     */
    public void setDate(String Date) {
        this.Date = Date;
    }

    /**
     * (M97)プログラム更新管理一覧処理状態 (例: 済)
     *
     * @return the State
     */
    public String getState() {
        return State;
    }

    /**
     * (M97)プログラム更新管理一覧処理状態 (例: 済)
     *
     * @param State the State to set
     */
    public void setState(String State) {
        this.State = State;
    }

    /**
     * (M97)プログラム更新管理一覧内容 (例: 提供されている最新の状態でした。)
     *
     * @return the Comment
     */
    public String getComment() {
        return Comment;
    }

    /**
     * (M97)プログラム更新管理一覧内容 (例: 提供されている最新の状態でした。)
     *
     * @param Comment the Comment to set
     */
    public void setComment(String Comment) {
        this.Comment = Comment;
    }
}
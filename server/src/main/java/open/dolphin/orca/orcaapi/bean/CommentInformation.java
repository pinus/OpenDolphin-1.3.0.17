package open.dolphin.orca.orcaapi.bean;

/**
 * Comment_Information. ユーザコメント情報(繰り返し5)
 *
 * @author pns
 */
public class CommentInformation {
    /**
     * カラム位置 (例: 4)
     */
    private String Column_Position;

    /**
     * 桁数 (例:  )
     */
    private String Digit_Number;

    /**
     * カラム位置 (例: 4)
     *
     * @return the Column_Position
     */
    public String getColumn_Position() {
        return Column_Position;
    }

    /**
     * カラム位置 (例: 4)
     *
     * @param Column_Position the Column_Position to set
     */
    public void setColumn_Position(String Column_Position) {
        this.Column_Position = Column_Position;
    }

    /**
     * 桁数 (例:  )
     *
     * @return the Digit_Number
     */
    public String getDigit_Number() {
        return Digit_Number;
    }

    /**
     * 桁数 (例:  )
     *
     * @param Digit_Number the Digit_Number to set
     */
    public void setDigit_Number(String Digit_Number) {
        this.Digit_Number = Digit_Number;
    }
}

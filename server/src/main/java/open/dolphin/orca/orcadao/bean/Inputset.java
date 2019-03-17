package open.dolphin.orca.orcadao.bean;

/**
 * TBL_INPUTSET 関連.
 */
public class Inputset {
    private String inputCd; // .210 or 616130532 ...
    private float suryo1; // suryo1
    private int kaisu; // bundle 数
    private String comment; // coment

    /**
     * セットコード.
     *
     * @return inputCd
     */
    public String getInputCd() {
        return inputCd;
    }

    /**
     * セットコード.
     *
     * @param inputCd to set
     */
    public void setInputCd(String inputCd) {
        this.inputCd = inputCd;
    }

    /**
     * 数量1.
     *
     * @return suryo1
     */
    public float getSuryo1() {
        return suryo1;
    }

    /**
     * 数量1.
     *
     * @param suryo1 to set
     */
    public void setSuryo1(float suryo1) {
        this.suryo1 = suryo1;
    }

    /**
     * 回数.
     *
     * @return kaisu
     */
    public int getKaisu() {
        return kaisu;
    }

    /**
     * 回数.
     *
     * @param kaisu to set
     */
    public void setKaisu(int kaisu) {
        this.kaisu = kaisu;
    }

    /**
     * 入力コメント.
     *
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * 入力コメント.
     *
     * @param comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}



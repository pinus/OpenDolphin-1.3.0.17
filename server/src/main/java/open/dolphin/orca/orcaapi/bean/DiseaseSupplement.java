package open.dolphin.orca.orcaapi.bean;

/**
 * Disease_Supplement. 病名補足コメント情報
 * @author pns
 */
public class DiseaseSupplement {
    /**
     * 補足コメントコード１ (例: 2056)
     */
    private String Disease_Scode1;

    /**
     * 補足コメントコード２ (例: 1053)
     */
    private String Disease_Scode2;

    /**
     * 補足コメントコード３ (例:  )
     */
    private String Disease_Scode3;

    /**
     * 補足コメント (例: 補足コメント)
     */
    private String Disease_Sname;

    /**
     * 補足コメントコード１ (例: 2056)
     * @return the Disease_Scode1
     */
    public String getDisease_Scode1() {
        return Disease_Scode1;
    }

    /**
     * 補足コメントコード１ (例: 2056)
     * @param Disease_Scode1 the Disease_Scode1 to set
     */
    public void setDisease_Scode1(String Disease_Scode1) {
        this.Disease_Scode1 = Disease_Scode1;
    }

    /**
     * 補足コメントコード２ (例: 1053)
     * @return the Disease_Scode2
     */
    public String getDisease_Scode2() {
        return Disease_Scode2;
    }

    /**
     * 補足コメントコード２ (例: 1053)
     * @param Disease_Scode2 the Disease_Scode2 to set
     */
    public void setDisease_Scode2(String Disease_Scode2) {
        this.Disease_Scode2 = Disease_Scode2;
    }

    /**
     * 補足コメントコード３ (例:  )
     * @return the Disease_Scode3
     */
    public String getDisease_Scode3() {
        return Disease_Scode3;
    }

    /**
     * 補足コメントコード３ (例:  )
     * @param Disease_Scode3 the Disease_Scode3 to set
     */
    public void setDisease_Scode3(String Disease_Scode3) {
        this.Disease_Scode3 = Disease_Scode3;
    }

    /**
     * 補足コメント (例: 補足コメント)
     * @return the Disease_Sname
     */
    public String getDisease_Sname() {
        return Disease_Sname;
    }

    /**
     * 補足コメント (例: 補足コメント)
     * @param Disease_Sname the Disease_Sname to set
     */
    public void setDisease_Sname(String Disease_Sname) {
        this.Disease_Sname = Disease_Sname;
    }
}
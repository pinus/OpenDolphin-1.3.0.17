package open.dolphin.orca.orcadao.bean;

/**
 * TBL_SYSKANRI 関連.
 */
public class Syskanri {
    /**
     * 職員コード.
     */
    private String code;

    /**
     * 職員名 (漢字).
     */
    private String wholeName;

    /**
     * 職員名 (カナ).
     */
    private String kanaName;

    /**
     * 職員コード.
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * 職員コード.
     * @param code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 職員名 (漢字).
     * @return wholeName
     */
    public String getWholeName() {
        return wholeName;
    }

    /**
     * 職員名 (漢字).
     * @param wholeName to set
     */
    public void setWholeName(String wholeName) {
        this.wholeName = wholeName;
    }

    /**
     * 職員名 (カナ).
     * @return kanaName
     */
    public String getKanaName() {
        return kanaName;
    }

    /**
     * 職員名 (カナ).
     * @param kanaName to set
     */
    public void setKanaName(String kanaName) {
        this.kanaName = kanaName;
    }
}

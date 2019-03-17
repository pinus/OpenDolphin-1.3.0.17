package open.dolphin.orca.orcaapi.bean;

/**
 * patientlst3req.
 *
 * @author pns
 */
public class Patientlst3req {
    /**
     * 検索氏名 (例: 日医)
     */
    private String WholeName;

    /**
     * 検索開始生年月日 (例: 1975-01-01)
     */
    private String Birth_StartDate;

    /**
     * 検索終了生年月日 (例: 1990-12-31)
     */
    private String Birth_EndDate;

    /**
     * 検索性別 (例: 1)
     */
    private String Sex;

    /**
     * 検索入院・外来区分 (例: 2)
     */
    private String InOut;

    /**
     * 検索氏名 (例: 日医)
     *
     * @return the WholeName
     */
    public String getWholeName() {
        return WholeName;
    }

    /**
     * 検索氏名 (例: 日医)
     *
     * @param WholeName the WholeName to set
     */
    public void setWholeName(String WholeName) {
        this.WholeName = WholeName;
    }

    /**
     * 検索開始生年月日 (例: 1975-01-01)
     *
     * @return the Birth_StartDate
     */
    public String getBirth_StartDate() {
        return Birth_StartDate;
    }

    /**
     * 検索開始生年月日 (例: 1975-01-01)
     *
     * @param Birth_StartDate the Birth_StartDate to set
     */
    public void setBirth_StartDate(String Birth_StartDate) {
        this.Birth_StartDate = Birth_StartDate;
    }

    /**
     * 検索終了生年月日 (例: 1990-12-31)
     *
     * @return the Birth_EndDate
     */
    public String getBirth_EndDate() {
        return Birth_EndDate;
    }

    /**
     * 検索終了生年月日 (例: 1990-12-31)
     *
     * @param Birth_EndDate the Birth_EndDate to set
     */
    public void setBirth_EndDate(String Birth_EndDate) {
        this.Birth_EndDate = Birth_EndDate;
    }

    /**
     * 検索性別 (例: 1)
     *
     * @return the Sex
     */
    public String getSex() {
        return Sex;
    }

    /**
     * 検索性別 (例: 1)
     *
     * @param Sex the Sex to set
     */
    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    /**
     * 検索入院・外来区分 (例: 2)
     *
     * @return the InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * 検索入院・外来区分 (例: 2)
     *
     * @param InOut the InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
    }
}
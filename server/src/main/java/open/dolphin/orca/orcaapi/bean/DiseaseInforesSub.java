package open.dolphin.orca.orcaapi.bean;

/**
 * Disease_Infores. 患者病名情報
 * 上位の disease_infores とクラス名が同じになってしまうので，Sub を付けてある.
 *
 * @author pns
 */
public class DiseaseInforesSub {
    /**
     * 患者番号 (例: 00012)
     */
    private String Patient_ID;

    /**
     * 患者氏名 (例: 日医　太郎)
     */
    private String WholeName;

    /**
     * 患者カナ氏名 (例: ニチイ　タロウ)
     */
    private String WholeName_inKana;

    /**
     * 生年月日 (例: 1975-01-01)
     */
    private String BirthDate;

    /**
     * 性別(1:男性、2:女性) (例: 1)
     */
    private String Sex;

    /**
     * 患者番号 (例: 00012)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 00012)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 患者氏名 (例: 日医　太郎)
     *
     * @return the WholeName
     */
    public String getWholeName() {
        return WholeName;
    }

    /**
     * 患者氏名 (例: 日医　太郎)
     *
     * @param WholeName the WholeName to set
     */
    public void setWholeName(String WholeName) {
        this.WholeName = WholeName;
    }

    /**
     * 患者カナ氏名 (例: ニチイ　タロウ)
     *
     * @return the WholeName_inKana
     */
    public String getWholeName_inKana() {
        return WholeName_inKana;
    }

    /**
     * 患者カナ氏名 (例: ニチイ　タロウ)
     *
     * @param WholeName_inKana the WholeName_inKana to set
     */
    public void setWholeName_inKana(String WholeName_inKana) {
        this.WholeName_inKana = WholeName_inKana;
    }

    /**
     * 生年月日 (例: 1975-01-01)
     *
     * @return the BirthDate
     */
    public String getBirthDate() {
        return BirthDate;
    }

    /**
     * 生年月日 (例: 1975-01-01)
     *
     * @param BirthDate the BirthDate to set
     */
    public void setBirthDate(String BirthDate) {
        this.BirthDate = BirthDate;
    }

    /**
     * 性別(1:男性、2:女性) (例: 1)
     *
     * @return the Sex
     */
    public String getSex() {
        return Sex;
    }

    /**
     * 性別(1:男性、2:女性) (例: 1)
     *
     * @param Sex the Sex to set
     */
    public void setSex(String Sex) {
        this.Sex = Sex;
    }
}

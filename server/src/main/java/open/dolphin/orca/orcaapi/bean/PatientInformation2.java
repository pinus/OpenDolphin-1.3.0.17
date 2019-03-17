package open.dolphin.orca.orcaapi.bean;

/**
 * Patient_Information2. Medicalres 用の患者情報.
 * PatientInfores の PatientInformation とは
 * HealthInsuranceInformation が配列で返ってくるか単独で返ってくるかが違う.
 *
 * @author pns
 */
public class PatientInformation2 {
    /**
     * 患者番号 (例: 000017)
     */
    private String Patient_ID;

    /**
     * 患者氏名(漢字) (例: 日医　太郎)
     */
    private String WholeName;

    /**
     * 患者氏名(カナ) (例: ニチイ　タロウ)
     */
    private String WholeName_inKana;

    /**
     * 生年月日 (例: 1970-01-01)
     */
    private String BirthDate;

    /**
     * 性別(1:男性、2:女性) (例: 1)
     */
    private String Sex;

    /**
     * 保険組合せ情報 (例:  )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * 割引率　割引率+円（％） (例: )
     */
    private String Dis_Rate;

    /**
     * 負担額情報 (例: )
     */
    private CdInformation Cd_Information;

    /**
     * 請求点数 (例: )
     */
    private AcPointInformation Ac_Point_Information;

    /**
     * 患者番号 (例: 000017)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 000017)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 患者氏名(漢字) (例: 日医　太郎)
     *
     * @return the WholeName
     */
    public String getWholeName() {
        return WholeName;
    }

    /**
     * 患者氏名(漢字) (例: 日医　太郎)
     *
     * @param WholeName the WholeName to set
     */
    public void setWholeName(String WholeName) {
        this.WholeName = WholeName;
    }

    /**
     * 患者氏名(カナ) (例: ニチイ　タロウ)
     *
     * @return the WholeName_inKana
     */
    public String getWholeName_inKana() {
        return WholeName_inKana;
    }

    /**
     * 患者氏名(カナ) (例: ニチイ　タロウ)
     *
     * @param WholeName_inKana the WholeName_inKana to set
     */
    public void setWholeName_inKana(String WholeName_inKana) {
        this.WholeName_inKana = WholeName_inKana;
    }

    /**
     * 生年月日 (例: 1970-01-01)
     *
     * @return the BirthDate
     */
    public String getBirthDate() {
        return BirthDate;
    }

    /**
     * 生年月日 (例: 1970-01-01)
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

    /**
     * 保険組合せ情報 (例:  )
     *
     * @return the HealthInsurance_Information
     */
    public HealthInsuranceInformation getHealthInsurance_Information() {
        return HealthInsurance_Information;
    }

    /**
     * 保険組合せ情報 (例:  )
     *
     * @param HealthInsurance_Information the HealthInsurance_Information to set
     */
    public void setHealthInsurance_Information(HealthInsuranceInformation HealthInsurance_Information) {
        this.HealthInsurance_Information = HealthInsurance_Information;
    }

    /**
     * 割引率　割引率+円（％） (例: )
     *
     * @return the Dis_Rate
     */
    public String getDis_Rate() {
        return Dis_Rate;
    }

    /**
     * 割引率　割引率+円（％） (例: )
     *
     * @param Dis_Rate the Dis_Rate to set
     */
    public void setDis_Rate(String Dis_Rate) {
        this.Dis_Rate = Dis_Rate;
    }

    /**
     * 負担額情報 (例: )
     *
     * @return the Cd_Information
     */
    public CdInformation getCd_Information() {
        return Cd_Information;
    }

    /**
     * 負担額情報 (例: )
     *
     * @param Cd_Information the Cd_Information to set
     */
    public void setCd_Information(CdInformation Cd_Information) {
        this.Cd_Information = Cd_Information;
    }

    /**
     * 請求点数 (例: )
     *
     * @return the Ac_Point_Information
     */
    public AcPointInformation getAc_Point_Information() {
        return Ac_Point_Information;
    }

    /**
     * 請求点数 (例: )
     *
     * @param Ac_Point_Information the Ac_Point_Information to set
     */
    public void setAc_Point_Information(AcPointInformation Ac_Point_Information) {
        this.Ac_Point_Information = Ac_Point_Information;
    }
}

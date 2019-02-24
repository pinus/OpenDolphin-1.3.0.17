package open.dolphin.orca.orcadao.bean;

/**
 * TBL_WKSRYACT 関連.
 */
public class Wksryact {

    /**
     * API登録のUID.
     */
    private String medicalUid;

    /**
     * 展開区分.
     * （1:展開中、0:以外）
     */
    private String medicalMode;

    /**
     * 登録区分.
     * （1:中途終了登録分、0:以外）
     */
    private String medicalMode2;

    /**
     * API登録のUID.
     * @return medicalUid
     */
    public String getMedicalUid() {
        return medicalUid;
    }

    /**
     * API登録のUID.
     * @param medicalUid to set
     */
    public void setMedicalUid(String medicalUid) {
        this.medicalUid = medicalUid;
    }

    /**
     * 展開区分.
     * （1:展開中、0:以外）
     * @return medicalMode
     */
    public String getMedicalMode() {
        return medicalMode;
    }

    /**
     * 展開区分.
     * （1:展開中、0:以外）
     * @param medicalMode to set
     */
    public void setMedicalMode(String medicalMode) {
        this.medicalMode = medicalMode;
    }

    /**
     * 登録区分.
     * （1:中途終了登録分、0:以外）
     * @return medicalMode2
     */
    public String getMedicalMode2() {
        return medicalMode2;
    }

    /**
     * 登録区分.
     * （1:中途終了登録分、0:以外）
     * @param medicalMode2 to set
     */
    public void setMedicalMode2(String medicalMode2) {
        this.medicalMode2 = medicalMode2;
    }
}

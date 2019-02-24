package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Information. 診療行為登録関係.
 * for system01dailyres.
 * @author pns
 */
public class MedicalInformation5 {
    /**
     * 診療行為設定項目名を変更(2018-09-25) (例: )
     */
    private MedicalSet01Information Medical_Set01_Information;

    /**
     * 診療行為自動算定設定(初期)項目名を変更(2018-09-25) (例: )
     */
    private MedicalSet02Information Medical_Set02_Information;

    /**
     * 診療行為自動算定・チェック設定項目名を変更(2018-09-25) (例: )
     */
    private MedicalSet03Information Medical_Set03_Information;

    /**
     * 診療行為請求確認設定 (例: )
     */
    private MedicalSet04Information Medical_Set04_Information;

    /**
     * Medical_Set01_Information
     *
     * @return Medical_Set01_Information
     */
    public MedicalSet01Information getMedical_Set01_Information() {
        return Medical_Set01_Information;
    }

    /**
     * Medical_Set01_Information
     *
     * @param Medical_Set01_Information to set
     */
    public void setMedical_Set01_Information(MedicalSet01Information Medical_Set01_Information) {
        this.Medical_Set01_Information = Medical_Set01_Information;
    }

    /**
     * Medical_Set02_Information
     *
     * @return Medical_Set02_Information
     */
    public MedicalSet02Information getMedical_Set02_Information() {
        return Medical_Set02_Information;
    }

    /**
     * Medical_Set02_Information
     *
     * @param Medical_Set02_Information to set
     */
    public void setMedical_Set02_Information(MedicalSet02Information Medical_Set02_Information) {
        this.Medical_Set02_Information = Medical_Set02_Information;
    }

    /**
     * Medical_Set03_Information
     *
     * @return Medical_Set03_Information
     */
    public MedicalSet03Information getMedical_Set03_Information() {
        return Medical_Set03_Information;
    }

    /**
     * Medical_Set03_Information
     *
     * @param Medical_Set03_Information to set
     */
    public void setMedical_Set03_Information(MedicalSet03Information Medical_Set03_Information) {
        this.Medical_Set03_Information = Medical_Set03_Information;
    }

    /**
     * Medical_Set04_Information
     *
     * @return Medical_Set04_Information
     */
    public MedicalSet04Information getMedical_Set04_Information() {
        return Medical_Set04_Information;
    }

    /**
     * Medical_Set04_Information
     *
     * @param Medical_Set04_Information to set
     */
    public void setMedical_Set04_Information(MedicalSet04Information Medical_Set04_Information) {
        this.Medical_Set04_Information = Medical_Set04_Information;
    }
}
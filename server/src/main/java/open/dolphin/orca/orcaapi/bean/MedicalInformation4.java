package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Information. 診療行為情報（繰り返し　３１）
 * for medicalget04res.
 * @author pns
 */
public class MedicalInformation4 {
    /**
     * 算定日 (例: 18)
     */
    private String Medical_Day;

    /**
     * 診療行為情報２（繰り返し　４０） (例: )
     */
    private MedicalInformation[] Medical_Information2;

    /**
     * 診療行為剤内容（繰り返し　４０） (例: )
     */
    private MedicalInfo[] Medical_Info;

    /**
     * 算定日 (例: 18)
     * @return the Medical_Day
     */
    public String getMedical_Day() {
        return Medical_Day;
    }

    /**
     * 算定日 (例: 18)
     * @param Medical_Day the Medical_Day to set
     */
    public void setMedical_Day(String Medical_Day) {
        this.Medical_Day = Medical_Day;
    }

    /**
     * 診療行為情報２（繰り返し　４０） (例: )
     * @return the Medical_Information2
     */
    public MedicalInformation[] getMedical_Information2() {
        return Medical_Information2;
    }

    /**
     * 診療行為情報２（繰り返し　４０） (例: )
     * @param Medical_Information2 the Medical_Information2 to set
     */
    public void setMedical_Information2(MedicalInformation[] Medical_Information2) {
        this.Medical_Information2 = Medical_Information2;
    }

    /**
     * 診療行為剤内容（繰り返し　４０） (例: )
     * @return the Medical_Info
     */
    public MedicalInfo[] getMedical_Info() {
        return Medical_Info;
    }

    /**
     * 診療行為剤内容（繰り返し　４０） (例: )
     * @param Medical_Info the Medical_Info to set
     */
    public void setMedical_Info(MedicalInfo[] Medical_Info) {
        this.Medical_Info = Medical_Info;
    }
}
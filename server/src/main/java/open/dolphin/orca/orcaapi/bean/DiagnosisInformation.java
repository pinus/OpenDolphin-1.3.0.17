package open.dolphin.orca.orcaapi.bean;

/**
 * Diagnosis_Information. 診療情報
 *
 * @author pns
 */
public class DiagnosisInformation {
    /**
     * 診療科コード※１(01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * ドクタコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * 保険組合せ情報 (例:  )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * 診療行為情報(繰り返し40) (例:  )
     */
    private MedicalInformation[] Medical_Information;

    /**
     * 病名情報(繰り返し50) (例:  )
     */
    private DiseaseInformation[] Disease_Information;

    /**
     * 保険組合せ番号 (例: )
     */
    private String Combination_Number;

    /**
     * 診療科コード※１(01:内科) (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード※１(01:内科) (例: 01)
     *
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * ドクタコード (例: 10001)
     *
     * @return the Physician_Code
     */
    public String getPhysician_Code() {
        return Physician_Code;
    }

    /**
     * ドクタコード (例: 10001)
     *
     * @param Physician_Code the Physician_Code to set
     */
    public void setPhysician_Code(String Physician_Code) {
        this.Physician_Code = Physician_Code;
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
     * 診療行為情報(繰り返し40) (例:  )
     *
     * @return the Medical_Information
     */
    public MedicalInformation[] getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療行為情報(繰り返し40) (例:  )
     *
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(MedicalInformation[] Medical_Information) {
        this.Medical_Information = Medical_Information;
    }

    /**
     * 病名情報(繰り返し50) (例:  )
     *
     * @return the Disease_Information
     */
    public DiseaseInformation[] getDisease_Information() {
        return Disease_Information;
    }

    /**
     * 病名情報(繰り返し50) (例:  )
     *
     * @param Disease_Information the Disease_Information to set
     */
    public void setDisease_Information(DiseaseInformation[] Disease_Information) {
        this.Disease_Information = Disease_Information;
    }

    /**
     * 保険組合せ番号 (例: )
     *
     * @return the Combination_Number
     */
    public String getCombination_Number() {
        return Combination_Number;
    }

    /**
     * 保険組合せ番号 (例: )
     *
     * @param Combination_Number the Combination_Number to set
     */
    public void setCombination_Number(String Combination_Number) {
        this.Combination_Number = Combination_Number;
    }
}
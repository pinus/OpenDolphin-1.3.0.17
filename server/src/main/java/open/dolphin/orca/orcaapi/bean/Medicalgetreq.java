package open.dolphin.orca.orcaapi.bean;

/**
 * medicalgetreq.
 *
 * @author pns
 */
public class Medicalgetreq {
    /**
     * 入外区分(I：入院) (例: I)
     */
    private String InOut;

    /**
     * 患者番号 (例: 12)
     */
    private String Patient_ID;

    /**
     * 診療日 (例: 2014-01-06)
     */
    private String Perform_Date;

    /**
     * 月数 (例: 12)
     */
    private String For_Months;

    /**
     * 診療情報 (例:  )
     */
    private MedicalInformation3 Medical_Information;

    /**
     * 入外区分(I：入院) (例: I)
     *
     * @return the InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * 入外区分(I：入院) (例: I)
     *
     * @param InOut the InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
    }

    /**
     * 患者番号 (例: 12)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 12)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 診療日 (例: 2014-01-06)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療日 (例: 2014-01-06)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 月数 (例: 12)
     *
     * @return the For_Months
     */
    public String getFor_Months() {
        return For_Months;
    }

    /**
     * 月数 (例: 12)
     *
     * @param For_Months the For_Months to set
     */
    public void setFor_Months(String For_Months) {
        this.For_Months = For_Months;
    }

    /**
     * 診療情報 (例:  )
     *
     * @return the Medical_Information
     */
    public MedicalInformation3 getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療情報 (例:  )
     *
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(MedicalInformation3 Medical_Information) {
        this.Medical_Information = Medical_Information;
    }
}
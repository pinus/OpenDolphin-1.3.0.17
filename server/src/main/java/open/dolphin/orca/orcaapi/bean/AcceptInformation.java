package open.dolphin.orca.orcaapi.bean;

/**
 * Accept_Information. 受付情報※3
 * @author pns
 */
public class AcceptInformation {
    /**
     * 診療科※4 (例: )
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: )
     */
    private String Department_Name;

    /**
     * 保険情報 (例: )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * Department_Code
     *
     * @return Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * Department_Code
     *
     * @param Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * Department_Name
     *
     * @return Department_Name
     */
    public String getDepartment_Name() {
        return Department_Name;
    }

    /**
     * Department_Name
     *
     * @param Department_Name to set
     */
    public void setDepartment_Name(String Department_Name) {
        this.Department_Name = Department_Name;
    }

    /**
     * HealthInsurance_Information
     *
     * @return HealthInsurance_Information
     */
    public HealthInsuranceInformation getHealthInsurance_Information() {
        return HealthInsurance_Information;
    }

    /**
     * HealthInsurance_Information
     *
     * @param HealthInsurance_Information to set
     */
    public void setHealthInsurance_Information(HealthInsuranceInformation HealthInsurance_Information) {
        this.HealthInsurance_Information = HealthInsurance_Information;
    }
}

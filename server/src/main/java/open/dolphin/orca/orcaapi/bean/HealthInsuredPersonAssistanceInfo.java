package open.dolphin.orca.orcaapi.bean;

/**
 * HealthInsuredPerson_Assistance_Info. 補助区分情報※2（繰り返し30）
 * @author pns
 */
public class HealthInsuredPersonAssistanceInfo {
    /**
     * 補助区分 (例: )
     */
    private String HealthInsuredPerson_Assistance;

    /**
     * 補助区分名称 (例: )
     */
    private String HealthInsuredPerson_Assistance_Name;

    /**
     * 高齢者区分1:高齢書のみ選択可能 (例: )
     */
    private String HealthInsuredPerson_Assistance_Mode;

    /**
     * HealthInsuredPerson_Assistance
     *
     * @return HealthInsuredPerson_Assistance
     */
    public String getHealthInsuredPerson_Assistance() {
        return HealthInsuredPerson_Assistance;
    }

    /**
     * HealthInsuredPerson_Assistance
     *
     * @param HealthInsuredPerson_Assistance to set
     */
    public void setHealthInsuredPerson_Assistance(String HealthInsuredPerson_Assistance) {
        this.HealthInsuredPerson_Assistance = HealthInsuredPerson_Assistance;
    }

    /**
     * HealthInsuredPerson_Assistance_Name
     *
     * @return HealthInsuredPerson_Assistance_Name
     */
    public String getHealthInsuredPerson_Assistance_Name() {
        return HealthInsuredPerson_Assistance_Name;
    }

    /**
     * HealthInsuredPerson_Assistance_Name
     *
     * @param HealthInsuredPerson_Assistance_Name to set
     */
    public void setHealthInsuredPerson_Assistance_Name(String HealthInsuredPerson_Assistance_Name) {
        this.HealthInsuredPerson_Assistance_Name = HealthInsuredPerson_Assistance_Name;
    }

    /**
     * HealthInsuredPerson_Assistance_Mode
     *
     * @return HealthInsuredPerson_Assistance_Mode
     */
    public String getHealthInsuredPerson_Assistance_Mode() {
        return HealthInsuredPerson_Assistance_Mode;
    }

    /**
     * HealthInsuredPerson_Assistance_Mode
     *
     * @param HealthInsuredPerson_Assistance_Mode to set
     */
    public void setHealthInsuredPerson_Assistance_Mode(String HealthInsuredPerson_Assistance_Mode) {
        this.HealthInsuredPerson_Assistance_Mode = HealthInsuredPerson_Assistance_Mode;
    }
}
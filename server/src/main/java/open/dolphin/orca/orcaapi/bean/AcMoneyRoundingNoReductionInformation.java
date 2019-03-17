package open.dolphin.orca.orcaapi.bean;

/**
 * Ac_Money_Rounding_No_Reduction_Information. 請求額端数区分（減免無）情報
 *
 * @author pns
 */
public class AcMoneyRoundingNoReductionInformation {
    /**
     * 請求額端数区分医保（減免無・保険分） (例: 1)
     */
    private String Medical_Insurance_Class;

    /**
     * 請求額端数区分医保（減免無・保険分）名称 (例: １０円未満四捨五入)
     */
    private String Medical_Insurance_Class_Name;

    /**
     * 請求額端数区分医保（減免無・自費分） (例: 0)
     */
    private String Medical_Insurance_Oe_Class;

    /**
     * 請求額端数区分医保（減免無・自費分）名称 (例: 保険分に準ずる)
     */
    private String Medical_Insurance_Oe_Class_Name;

    /**
     * 請求額端数区分労災（減免無・保険分） (例: 4)
     */
    private String Accident_Insurance_Class;

    /**
     * 請求額端数区分労災（減免無・保険分）名称 (例: １０円未満端数処理なし)
     */
    private String Accident_Insurance_Class_Name;

    /**
     * 請求額端数区分労災（減免無・自費分） (例: 3)
     */
    private String Accident_Insurance_Oe_Class;

    /**
     * 請求額端数区分労災（減免無・自費分）名称 (例: １０円未満切り上げ)
     */
    private String Accident_Insurance_Oe_Class_Name;

    /**
     * 請求額端数区分自賠責（減免無・保険分） (例: 2)
     */
    private String Liability_Insurance_Class;

    /**
     * 請求額端数区分自賠責（減免無・保険分）名称 (例: １０円未満切り捨て)
     */
    private String Liability_Insurance_Class_Name;

    /**
     * 請求額端数区分自賠責（減免無・自費分） (例: 1)
     */
    private String Liability_Insurance_Oe_Class;

    /**
     * 請求額端数区分自賠責（減免無・自費分）名称 (例: １０円未満四捨五入)
     */
    private String Liability_Insurance_Oe_Class_Name;

    /**
     * 請求額端数区分公害（減免無・自費分） (例: 1)
     */
    private String Pollution_Oe_Class;

    /**
     * 請求額端数区分公害（減免無・自費分）名称 (例: １０円未満四捨五入)
     */
    private String Pollution_Oe_Class_Name;

    /**
     * 請求額端数区分第三者行為（減免無・保険分） (例: 2)
     */
    private String Third_Party_Class;

    /**
     * 請求額端数区分第三者行為（減免無・保険分）名称 (例: １０円未満切り捨て)
     */
    private String Third_Party_Class_Name;

    /**
     * 請求額端数区分第三者行為（減免無・自費分） (例: 3)
     */
    private String Third_Party_Oe_Class;

    /**
     * 請求額端数区分第三者行為（減免無・自費分）名称 (例: １０円未満切り上げ)
     */
    private String Third_Party_Oe_Class_Name;

    /**
     * Medical_Insurance_Class
     *
     * @return Medical_Insurance_Class
     */
    public String getMedical_Insurance_Class() {
        return Medical_Insurance_Class;
    }

    /**
     * Medical_Insurance_Class
     *
     * @param Medical_Insurance_Class to set
     */
    public void setMedical_Insurance_Class(String Medical_Insurance_Class) {
        this.Medical_Insurance_Class = Medical_Insurance_Class;
    }

    /**
     * Medical_Insurance_Class_Name
     *
     * @return Medical_Insurance_Class_Name
     */
    public String getMedical_Insurance_Class_Name() {
        return Medical_Insurance_Class_Name;
    }

    /**
     * Medical_Insurance_Class_Name
     *
     * @param Medical_Insurance_Class_Name to set
     */
    public void setMedical_Insurance_Class_Name(String Medical_Insurance_Class_Name) {
        this.Medical_Insurance_Class_Name = Medical_Insurance_Class_Name;
    }

    /**
     * Medical_Insurance_Oe_Class
     *
     * @return Medical_Insurance_Oe_Class
     */
    public String getMedical_Insurance_Oe_Class() {
        return Medical_Insurance_Oe_Class;
    }

    /**
     * Medical_Insurance_Oe_Class
     *
     * @param Medical_Insurance_Oe_Class to set
     */
    public void setMedical_Insurance_Oe_Class(String Medical_Insurance_Oe_Class) {
        this.Medical_Insurance_Oe_Class = Medical_Insurance_Oe_Class;
    }

    /**
     * Medical_Insurance_Oe_Class_Name
     *
     * @return Medical_Insurance_Oe_Class_Name
     */
    public String getMedical_Insurance_Oe_Class_Name() {
        return Medical_Insurance_Oe_Class_Name;
    }

    /**
     * Medical_Insurance_Oe_Class_Name
     *
     * @param Medical_Insurance_Oe_Class_Name to set
     */
    public void setMedical_Insurance_Oe_Class_Name(String Medical_Insurance_Oe_Class_Name) {
        this.Medical_Insurance_Oe_Class_Name = Medical_Insurance_Oe_Class_Name;
    }

    /**
     * Accident_Insurance_Class
     *
     * @return Accident_Insurance_Class
     */
    public String getAccident_Insurance_Class() {
        return Accident_Insurance_Class;
    }

    /**
     * Accident_Insurance_Class
     *
     * @param Accident_Insurance_Class to set
     */
    public void setAccident_Insurance_Class(String Accident_Insurance_Class) {
        this.Accident_Insurance_Class = Accident_Insurance_Class;
    }

    /**
     * Accident_Insurance_Class_Name
     *
     * @return Accident_Insurance_Class_Name
     */
    public String getAccident_Insurance_Class_Name() {
        return Accident_Insurance_Class_Name;
    }

    /**
     * Accident_Insurance_Class_Name
     *
     * @param Accident_Insurance_Class_Name to set
     */
    public void setAccident_Insurance_Class_Name(String Accident_Insurance_Class_Name) {
        this.Accident_Insurance_Class_Name = Accident_Insurance_Class_Name;
    }

    /**
     * Accident_Insurance_Oe_Class
     *
     * @return Accident_Insurance_Oe_Class
     */
    public String getAccident_Insurance_Oe_Class() {
        return Accident_Insurance_Oe_Class;
    }

    /**
     * Accident_Insurance_Oe_Class
     *
     * @param Accident_Insurance_Oe_Class to set
     */
    public void setAccident_Insurance_Oe_Class(String Accident_Insurance_Oe_Class) {
        this.Accident_Insurance_Oe_Class = Accident_Insurance_Oe_Class;
    }

    /**
     * Accident_Insurance_Oe_Class_Name
     *
     * @return Accident_Insurance_Oe_Class_Name
     */
    public String getAccident_Insurance_Oe_Class_Name() {
        return Accident_Insurance_Oe_Class_Name;
    }

    /**
     * Accident_Insurance_Oe_Class_Name
     *
     * @param Accident_Insurance_Oe_Class_Name to set
     */
    public void setAccident_Insurance_Oe_Class_Name(String Accident_Insurance_Oe_Class_Name) {
        this.Accident_Insurance_Oe_Class_Name = Accident_Insurance_Oe_Class_Name;
    }

    /**
     * Liability_Insurance_Class
     *
     * @return Liability_Insurance_Class
     */
    public String getLiability_Insurance_Class() {
        return Liability_Insurance_Class;
    }

    /**
     * Liability_Insurance_Class
     *
     * @param Liability_Insurance_Class to set
     */
    public void setLiability_Insurance_Class(String Liability_Insurance_Class) {
        this.Liability_Insurance_Class = Liability_Insurance_Class;
    }

    /**
     * Liability_Insurance_Class_Name
     *
     * @return Liability_Insurance_Class_Name
     */
    public String getLiability_Insurance_Class_Name() {
        return Liability_Insurance_Class_Name;
    }

    /**
     * Liability_Insurance_Class_Name
     *
     * @param Liability_Insurance_Class_Name to set
     */
    public void setLiability_Insurance_Class_Name(String Liability_Insurance_Class_Name) {
        this.Liability_Insurance_Class_Name = Liability_Insurance_Class_Name;
    }

    /**
     * Liability_Insurance_Oe_Class
     *
     * @return Liability_Insurance_Oe_Class
     */
    public String getLiability_Insurance_Oe_Class() {
        return Liability_Insurance_Oe_Class;
    }

    /**
     * Liability_Insurance_Oe_Class
     *
     * @param Liability_Insurance_Oe_Class to set
     */
    public void setLiability_Insurance_Oe_Class(String Liability_Insurance_Oe_Class) {
        this.Liability_Insurance_Oe_Class = Liability_Insurance_Oe_Class;
    }

    /**
     * Liability_Insurance_Oe_Class_Name
     *
     * @return Liability_Insurance_Oe_Class_Name
     */
    public String getLiability_Insurance_Oe_Class_Name() {
        return Liability_Insurance_Oe_Class_Name;
    }

    /**
     * Liability_Insurance_Oe_Class_Name
     *
     * @param Liability_Insurance_Oe_Class_Name to set
     */
    public void setLiability_Insurance_Oe_Class_Name(String Liability_Insurance_Oe_Class_Name) {
        this.Liability_Insurance_Oe_Class_Name = Liability_Insurance_Oe_Class_Name;
    }

    /**
     * Pollution_Oe_Class
     *
     * @return Pollution_Oe_Class
     */
    public String getPollution_Oe_Class() {
        return Pollution_Oe_Class;
    }

    /**
     * Pollution_Oe_Class
     *
     * @param Pollution_Oe_Class to set
     */
    public void setPollution_Oe_Class(String Pollution_Oe_Class) {
        this.Pollution_Oe_Class = Pollution_Oe_Class;
    }

    /**
     * Pollution_Oe_Class_Name
     *
     * @return Pollution_Oe_Class_Name
     */
    public String getPollution_Oe_Class_Name() {
        return Pollution_Oe_Class_Name;
    }

    /**
     * Pollution_Oe_Class_Name
     *
     * @param Pollution_Oe_Class_Name to set
     */
    public void setPollution_Oe_Class_Name(String Pollution_Oe_Class_Name) {
        this.Pollution_Oe_Class_Name = Pollution_Oe_Class_Name;
    }

    /**
     * Third_Party_Class
     *
     * @return Third_Party_Class
     */
    public String getThird_Party_Class() {
        return Third_Party_Class;
    }

    /**
     * Third_Party_Class
     *
     * @param Third_Party_Class to set
     */
    public void setThird_Party_Class(String Third_Party_Class) {
        this.Third_Party_Class = Third_Party_Class;
    }

    /**
     * Third_Party_Class_Name
     *
     * @return Third_Party_Class_Name
     */
    public String getThird_Party_Class_Name() {
        return Third_Party_Class_Name;
    }

    /**
     * Third_Party_Class_Name
     *
     * @param Third_Party_Class_Name to set
     */
    public void setThird_Party_Class_Name(String Third_Party_Class_Name) {
        this.Third_Party_Class_Name = Third_Party_Class_Name;
    }

    /**
     * Third_Party_Oe_Class
     *
     * @return Third_Party_Oe_Class
     */
    public String getThird_Party_Oe_Class() {
        return Third_Party_Oe_Class;
    }

    /**
     * Third_Party_Oe_Class
     *
     * @param Third_Party_Oe_Class to set
     */
    public void setThird_Party_Oe_Class(String Third_Party_Oe_Class) {
        this.Third_Party_Oe_Class = Third_Party_Oe_Class;
    }

    /**
     * Third_Party_Oe_Class_Name
     *
     * @return Third_Party_Oe_Class_Name
     */
    public String getThird_Party_Oe_Class_Name() {
        return Third_Party_Oe_Class_Name;
    }

    /**
     * Third_Party_Oe_Class_Name
     *
     * @param Third_Party_Oe_Class_Name to set
     */
    public void setThird_Party_Oe_Class_Name(String Third_Party_Oe_Class_Name) {
        this.Third_Party_Oe_Class_Name = Third_Party_Oe_Class_Name;
    }
}

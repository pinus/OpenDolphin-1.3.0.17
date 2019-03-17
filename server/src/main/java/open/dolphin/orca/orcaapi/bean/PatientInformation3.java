package open.dolphin.orca.orcaapi.bean;

/**
 * Patient_Information. 患者登録関係 (for system01dailyres)
 *
 * @author pns
 */
public class PatientInformation3 {
    /**
     * 二重登録疑い判定区分システム管理「1017患者登録機能情報」 (例: )
     */
    private String Duplication_Class;

    /**
     * 二重登録疑い判定区分名称 (例: )
     */
    private String Duplication_Class_Name;

    /**
     * 郵便番号自動記載区分システム管理「1017患者登録機能情報」 (例: )
     */
    private String ZipCode_Auto_Class;

    /**
     * 郵便番号自動記載区分名称 (例: )
     */
    private String ZipCode_Auto_Class_Name;

    /**
     * 住所都道府県名記載区分システム管理「1017患者登録機能情報」 (例: )
     */
    private String P_WholeName_Auto_Class;

    /**
     * 住所都道府県名記載区分名称 (例: )
     */
    private String P_WholeName_Auto_Class_Name;

    /**
     * Duplication_Class
     *
     * @return Duplication_Class
     */
    public String getDuplication_Class() {
        return Duplication_Class;
    }

    /**
     * Duplication_Class
     *
     * @param Duplication_Class to set
     */
    public void setDuplication_Class(String Duplication_Class) {
        this.Duplication_Class = Duplication_Class;
    }

    /**
     * Duplication_Class_Name
     *
     * @return Duplication_Class_Name
     */
    public String getDuplication_Class_Name() {
        return Duplication_Class_Name;
    }

    /**
     * Duplication_Class_Name
     *
     * @param Duplication_Class_Name to set
     */
    public void setDuplication_Class_Name(String Duplication_Class_Name) {
        this.Duplication_Class_Name = Duplication_Class_Name;
    }

    /**
     * ZipCode_Auto_Class
     *
     * @return ZipCode_Auto_Class
     */
    public String getZipCode_Auto_Class() {
        return ZipCode_Auto_Class;
    }

    /**
     * ZipCode_Auto_Class
     *
     * @param ZipCode_Auto_Class to set
     */
    public void setZipCode_Auto_Class(String ZipCode_Auto_Class) {
        this.ZipCode_Auto_Class = ZipCode_Auto_Class;
    }

    /**
     * ZipCode_Auto_Class_Name
     *
     * @return ZipCode_Auto_Class_Name
     */
    public String getZipCode_Auto_Class_Name() {
        return ZipCode_Auto_Class_Name;
    }

    /**
     * ZipCode_Auto_Class_Name
     *
     * @param ZipCode_Auto_Class_Name to set
     */
    public void setZipCode_Auto_Class_Name(String ZipCode_Auto_Class_Name) {
        this.ZipCode_Auto_Class_Name = ZipCode_Auto_Class_Name;
    }

    /**
     * P_WholeName_Auto_Class
     *
     * @return P_WholeName_Auto_Class
     */
    public String getP_WholeName_Auto_Class() {
        return P_WholeName_Auto_Class;
    }

    /**
     * P_WholeName_Auto_Class
     *
     * @param P_WholeName_Auto_Class to set
     */
    public void setP_WholeName_Auto_Class(String P_WholeName_Auto_Class) {
        this.P_WholeName_Auto_Class = P_WholeName_Auto_Class;
    }

    /**
     * P_WholeName_Auto_Class_Name
     *
     * @return P_WholeName_Auto_Class_Name
     */
    public String getP_WholeName_Auto_Class_Name() {
        return P_WholeName_Auto_Class_Name;
    }

    /**
     * P_WholeName_Auto_Class_Name
     *
     * @param P_WholeName_Auto_Class_Name to set
     */
    public void setP_WholeName_Auto_Class_Name(String P_WholeName_Auto_Class_Name) {
        this.P_WholeName_Auto_Class_Name = P_WholeName_Auto_Class_Name;
    }
}

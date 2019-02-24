package open.dolphin.orca.orcaapi.bean;

/**
 * acceptlstreq.
 * @author pns
 */
public class Acceptlstreq {
    /**
     * 受付日 (例: 2010-12-20)
     */
    private String Acceptance_Date;

    /**
     * 診療科コード※１(01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * ドクターコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * 診療内容区分※３(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     */
    private String Medical_Information;

    /**
     * 受付日 (例: 2010-12-20)
     * @return the Acceptance_Date
     */
    public String getAcceptance_Date() {
        return Acceptance_Date;
    }

    /**
     * 受付日 (例: 2010-12-20)
     * @param Acceptance_Date the Acceptance_Date to set
     */
    public void setAcceptance_Date(String Acceptance_Date) {
        this.Acceptance_Date = Acceptance_Date;
    }

    /**
     * 診療科コード※１(01:内科) (例: 01)
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード※１(01:内科) (例: 01)
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * ドクターコード (例: 10001)
     * @return the Physician_Code
     */
    public String getPhysician_Code() {
        return Physician_Code;
    }

    /**
     * ドクターコード (例: 10001)
     * @param Physician_Code the Physician_Code to set
     */
    public void setPhysician_Code(String Physician_Code) {
        this.Physician_Code = Physician_Code;
    }

    /**
     * 診療内容区分※３(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     * @return the Medical_Information
     */
    public String getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療内容区分※３(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(String Medical_Information) {
        this.Medical_Information = Medical_Information;
    }
}
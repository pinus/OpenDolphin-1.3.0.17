package open.dolphin.orca.orcaapi.bean;

/**
 * appointlstreq.
 * @author pns
 */
public class Appointlstreq {
    /**
     * 予約日 (例: 2011-03-15)
     */
    private String Appointment_Date;

    /**
     * 診療内容区分※１(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断、07:予防注射、99:該当なし) (例: 01)
     */
    private String Medical_Information;

    /**
     * ドクターコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * 予約日 (例: 2011-03-15)
     * @return the Appointment_Date
     */
    public String getAppointment_Date() {
        return Appointment_Date;
    }

    /**
     * 予約日 (例: 2011-03-15)
     * @param Appointment_Date the Appointment_Date to set
     */
    public void setAppointment_Date(String Appointment_Date) {
        this.Appointment_Date = Appointment_Date;
    }

    /**
     * 診療内容区分※１(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断、07:予防注射、99:該当なし) (例: 01)
     * @return the Medical_Information
     */
    public String getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療内容区分※１(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断、07:予防注射、99:該当なし) (例: 01)
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(String Medical_Information) {
        this.Medical_Information = Medical_Information;
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
}

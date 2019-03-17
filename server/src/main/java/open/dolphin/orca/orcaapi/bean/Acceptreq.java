package open.dolphin.orca.orcaapi.bean;

/**
 * acceptreq.
 *
 * @author pns
 */
public class Acceptreq {
    /**
     * リクエスト番号 (例: 01).
     * <pre>
     * 新規患者の受付登録    リクエスト番号=01
     * 患者番号は空白、患者氏名を必須設定とします。
     * 診療科・ドクターコードは必須です。
     * ＃患者番号に設定があれば、患者氏名を設定しても無視します。
     * 患者氏名（WholeName）に関しては、全角２５文字で設定します。
     * ※外字は■変換します。エラーには出来ません。
     *
     * 新規患者の受付取消    リクエスト番号=02
     * 受付ID、受付日付、受付時間を設定します。
     * 受付日付、受付IDから削除対象の受付を決定します。
     * 決定した受付に患者番号がある時、受付時間が送信内容と一致しない時は
     * エラーとします。
     *
     * 新規患者の受付更新(患者番号設定)    リクエスト番号=03
     * 受付ID、受付日付、受付時間、患者番号、診療科、ドクター、診療内容を設定します。
     * 受付日付、受付IDから更新対象の受付を決定します。
     * 決定した受付に患者番号がある時、受付時間が送信内容と一致しない時はエラーとします。
     * 排他制御中のチェックなど受付登録時と同様のチェックをします。
     * 受付に患者番号と患者基本情報の患者氏名を設定して更新します。
     * 診療科から診療内容も送信内容で更新します。保険組合せ情報は受付登録時と同様の処理とします。
     * (保険組合せ情報の設定がなければ、自動設定します。)
     * </pre>
     */
    private String Request_Number;

    /**
     * 患者番号 (例: 00012)
     */
    private String Patient_ID;

    /**
     * 患者氏名 (例: 日医　太郎)
     */
    private String WholeName;

    /**
     * 受付日 (例: 2011-03-15)
     */
    private String Acceptance_Date;

    /**
     * 受付時間 (例: 15:30:00)
     */
    private String Acceptance_Time;

    /**
     * 受付ID (例:  )
     */
    private String Acceptance_Id;

    /**
     * 診療科コード※１(01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * ドクターコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * 診療内容区分※２(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     */
    private String Medical_Information;

    /**
     * 保険組合せ情報 (例:  )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * リクエスト番号 (例: 01).
     * <pre>
     * 新規患者の受付登録    リクエスト番号=01
     * 患者番号は空白、患者氏名を必須設定とします。
     * 診療科・ドクターコードは必須です。
     * ＃患者番号に設定があれば、患者氏名を設定しても無視します。
     * 患者氏名（WholeName）に関しては、全角２５文字で設定します。
     * ※外字は■変換します。エラーには出来ません。
     *
     * 新規患者の受付取消    リクエスト番号=02
     * 受付ID、受付日付、受付時間を設定します。
     * 受付日付、受付IDから削除対象の受付を決定します。
     * 決定した受付に患者番号がある時、受付時間が送信内容と一致しない時は
     * エラーとします。
     *
     * 新規患者の受付更新(患者番号設定)    リクエスト番号=03
     * 受付ID、受付日付、受付時間、患者番号、診療科、ドクター、診療内容を設定します。
     * 受付日付、受付IDから更新対象の受付を決定します。
     * 決定した受付に患者番号がある時、受付時間が送信内容と一致しない時はエラーとします。
     * 排他制御中のチェックなど受付登録時と同様のチェックをします。
     * 受付に患者番号と患者基本情報の患者氏名を設定して更新します。
     * 診療科から診療内容も送信内容で更新します。保険組合せ情報は受付登録時と同様の処理とします。
     * (保険組合せ情報の設定がなければ、自動設定します。)
     * </pre>
     *
     * @return the Request_Number
     */
    public String getRequest_Number() {
        return Request_Number;
    }

    /**
     * リクエスト番号 (例: 01).
     * <pre>
     * 新規患者の受付登録    リクエスト番号=01
     * 患者番号は空白、患者氏名を必須設定とします。
     * 診療科・ドクターコードは必須です。
     * ＃患者番号に設定があれば、患者氏名を設定しても無視します。
     * 患者氏名（WholeName）に関しては、全角２５文字で設定します。
     * ※外字は■変換します。エラーには出来ません。
     *
     * 新規患者の受付取消    リクエスト番号=02
     * 受付ID、受付日付、受付時間を設定します。
     * 受付日付、受付IDから削除対象の受付を決定します。
     * 決定した受付に患者番号がある時、受付時間が送信内容と一致しない時は
     * エラーとします。
     *
     * 新規患者の受付更新(患者番号設定)    リクエスト番号=03
     * 受付ID、受付日付、受付時間、患者番号、診療科、ドクター、診療内容を設定します。
     * 受付日付、受付IDから更新対象の受付を決定します。
     * 決定した受付に患者番号がある時、受付時間が送信内容と一致しない時はエラーとします。
     * 排他制御中のチェックなど受付登録時と同様のチェックをします。
     * 受付に患者番号と患者基本情報の患者氏名を設定して更新します。
     * 診療科から診療内容も送信内容で更新します。保険組合せ情報は受付登録時と同様の処理とします。
     * (保険組合せ情報の設定がなければ、自動設定します。)
     * </pre>
     *
     * @param Request_Number the Request_Number to set
     */
    public void setRequest_Number(String Request_Number) {
        this.Request_Number = Request_Number;
    }

    /**
     * 患者番号 (例: 00012)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 00012)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 患者氏名 (例: 日医　太郎)
     *
     * @return the WholeName
     */
    public String getWholeName() {
        return WholeName;
    }

    /**
     * 患者氏名 (例: 日医　太郎)
     *
     * @param WholeName the WholeName to set
     */
    public void setWholeName(String WholeName) {
        this.WholeName = WholeName;
    }

    /**
     * 受付日 (例: 2011-03-15)
     *
     * @return the Acceptance_Date
     */
    public String getAcceptance_Date() {
        return Acceptance_Date;
    }

    /**
     * 受付日 (例: 2011-03-15)
     *
     * @param Acceptance_Date the Acceptance_Date to set
     */
    public void setAcceptance_Date(String Acceptance_Date) {
        this.Acceptance_Date = Acceptance_Date;
    }

    /**
     * 受付時間 (例: 15:30:00)
     *
     * @return the Acceptance_Time
     */
    public String getAcceptance_Time() {
        return Acceptance_Time;
    }

    /**
     * 受付時間 (例: 15:30:00)
     *
     * @param Acceptance_Time the Acceptance_Time to set
     */
    public void setAcceptance_Time(String Acceptance_Time) {
        this.Acceptance_Time = Acceptance_Time;
    }

    /**
     * 受付ID (例:  )
     *
     * @return the Acceptance_Id
     */
    public String getAcceptance_Id() {
        return Acceptance_Id;
    }

    /**
     * 受付ID (例:  )
     *
     * @param Acceptance_Id the Acceptance_Id to set
     */
    public void setAcceptance_Id(String Acceptance_Id) {
        this.Acceptance_Id = Acceptance_Id;
    }

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
     * ドクターコード (例: 10001)
     *
     * @return the Physician_Code
     */
    public String getPhysician_Code() {
        return Physician_Code;
    }

    /**
     * ドクターコード (例: 10001)
     *
     * @param Physician_Code the Physician_Code to set
     */
    public void setPhysician_Code(String Physician_Code) {
        this.Physician_Code = Physician_Code;
    }

    /**
     * 診療内容区分※２(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     *
     * @return the Medical_Information
     */
    public String getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療内容区分※２(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     *
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(String Medical_Information) {
        this.Medical_Information = Medical_Information;
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
}

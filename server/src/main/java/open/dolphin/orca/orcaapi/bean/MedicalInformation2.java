package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Information. 医療機関基本情報.
 * for system1001res.
 * @author pns
 */
public class MedicalInformation2 {
    /**
     * 都道府県番号 (例: 13)
     */
    private String Prefectures_Number;

    /**
     * 点数表(1：医科) (例: 1)
     */
    private String Point_list;

    /**
     * 医療機関コード (例: 1234567)
     */
    private String Institution_Code;

    /**
     * 医療機関種別(1：病院、2：診療所) (例: 1)
     */
    private String Institution_Speciation;

    /**
     * 医療機関ID (例: JPN000000000000)
     */
    private String Institution_Id;

    /**
     * 医療機関名称 (例: 医療法人　オルカ)
     */
    private String Institution_WholeName;

    /**
     * 短縮医療機関名称 (例: オルカ)
     */
    private String Short_Institution_WholeName;

    /**
     * 開設者名称 (例: 日本　一)
     */
    private String Establisher_WholeName;

    /**
     * 管理者名称 (例: 日本　一)
     */
    private String Administrator_WholeName;

    /**
     * 病床数（許可） (例: 0020)
     */
    private String Hospital_bed_Capacity;

    /**
     * 病床数（一般） (例: 0020)
     */
    private String Hospital_bed_Capacity_General;

    /**
     * 老人支払区分 (例: 1)
     */
    private String Om_Payment_Class;

    /**
     * 老人支払区分名称 (例: 定率)
     */
    private String Om_Payment_Class_Name;

    /**
     * 旧総合病院フラグ (例: 0)
     */
    private String Old_General_Hospital_Class;

    /**
     * 旧総合病院フラグ名称 (例: 旧総合病院でない)
     */
    private String Old_General_Hospital_Class_Name;

    /**
     * 院外処方区分 (例: 1)
     */
    private String Outside_Class;

    /**
     * 院外処方区分名称 (例: 院外)
     */
    private String Outside_Class_Name;

    /**
     * 医療機関コード（漢字） (例: １２１２１２１)
     */
    private String Institution_Code_Kanji;

    /**
     * 分娩機関管理番号 (例: 1234567890)
     */
    private String Delivery_Organization_Control_Number;

    /**
     * 請求書発行フラグ (例: 2)
     */
    private String Print_Invoice_Receipt_Class;

    /**
     * 請求書発行フラグ名称 (例: 発行する（請求あり）)
     */
    private String Print_Invoice_Receipt_Class_Name;

    /**
     * 院外処方せん発行フラグ (例: 2)
     */
    private String Print_Prescription_Class;

    /**
     * 院外処方せん発行フラグ名称 (例: 院内処方発行)
     */
    private String Print_Prescription_Class_Name;

    /**
     * 前回処方表示フラグ (例: 1)
     */
    private String Last_Prescription_Display_Class;

    /**
     * 前回処方表示フラグ名称 (例: 表示しない)
     */
    private String Last_Prescription_Display_Class_Name;

    /**
     * 薬剤情報発行フラグ (例: 0)
     */
    private String Print_Medicine_Information_Class;

    /**
     * 薬剤情報発行フラグ名称 (例: 発行しない)
     */
    private String Print_Medicine_Information_Class_Name;

    /**
     * 診療費明細書発行フラグ (例: 2)
     */
    private String Print_Statement_Class;

    /**
     * 診療費明細書発行フラグ名称 (例: 発行する（請求あり）)
     */
    private String Print_Statement_Class_Name;

    /**
     * お薬手帳発行フラグ (例: 1)
     */
    private String Print_Medication_Note_Class;

    /**
     * お薬手帳発行フラグ名称 (例: 発行する（後期高齢）)
     */
    private String Print_Medication_Note_Class_Name;

    /**
     * 予約票発行フラグ (例: 0)
     */
    private String Print_Appointment_Form_Class;

    /**
     * 予約票発行フラグ名称 (例: 発行しない)
     */
    private String Print_Appointment_Form_Class_Name;

    /**
     * データ収集作成フラグ (例: 1)
     */
    private String Data_Collection_Creation_Class;

    /**
     * データ収集作成フラグ名称 (例: 作成する)
     */
    private String Data_Collection_Creation_Class_Name;

    /**
     * データ収集提出方法区分 (例: 1)
     */
    private String Data_Collection_Submission_Method_Class;

    /**
     * データ収集提出方法区分名称 (例: 自動で送信)
     */
    private String Data_Collection_Submission_Method_Class_Name;

    /**
     * ORCAサーベイランス区分 (例: 2)
     */
    private String Orca_Surveillance_Class;

    /**
     * ORCAサーベイランス区分名称 (例: 作成する／日)
     */
    private String Orca_Surveillance_Class_Name;

    /**
     * 減免計算対象区分 (例: 3)
     */
    private String Reduction_Calculation_Object_Class;

    /**
     * 減免計算対象区分名称 (例: 自費分のみ)
     */
    private String Reduction_Calculation_Object_Class_Name;

    /**
     * 請求額端数区分（減免有） (例: 2)
     */
    private String Ac_Money_Rounding_Reduction_Class;

    /**
     * 請求額端数区分（減免有）名称 (例: １０円未満切り捨て)
     */
    private String Ac_Money_Rounding_Reduction_Class_Name;

    /**
     * 請求額端数区分（減免無）情報 (例:  )
     */
    private AcMoneyRoundingNoReductionInformation Ac_Money_Rounding_No_Reduction_Information;

    /**
     * 第三者行為（医療費）負担金額計算区分 (例: 2)
     */
    private String Third_Party_Money_Calculation_Class;

    /**
     * 第三者行為（医療費）負担金額計算区分名称 (例: 負担金額１０円未満端数処理なし)
     */
    private String Third_Party_Money_Calculation_Class_Name;

    /**
     * 消費税端数区分 (例: 1)
     */
    private String Tax_Rounding_Class;

    /**
     * 消費税端数区分名称 (例: １円未満四捨五入)
     */
    private String Tax_Rounding_Class_Name;

    /**
     * 自費保険集計先区分 (例: 2)
     */
    private String Self_Insurance_Total_Class;

    /**
     * 自費保険集計先区分名称 (例: 自費分欄)
     */
    private String Self_Insurance_Total_Class_Name;

    /**
     * 地方公費保険番号タブ区分 (例: 1)
     */
    private String Local_Public_Expenses_Insurance_Number_Tab_Class;

    /**
     * 地方公費保険番号タブ区分名称 (例: 有効)
     */
    private String Local_Public_Expenses_Insurance_Number_Tab_Class_Name;

    /**
     * 更正・育成限度額日割計算区分 (例: 1)
     */
    private String Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class;

    /**
     * 更正・育成限度額日割計算区分名称 (例: 日割計算しない)
     */
    private String Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name;

    /**
     * 自費コード数量計算端数区分 (例: 2)
     */
    private String Oe_Rounding_Class;

    /**
     * 自費コード数量計算端数区分名称 (例: １円未満切り捨て)
     */
    private String Oe_Rounding_Class_Name;

    /**
     * 連絡先・広告情報 (例:  )
     */
    private AddressInformation Address_Information;


    /**
     * 都道府県番号 (例: 32)
     * @return the Prefectures_Number
     */
    public String getPrefectures_Number() {
        return Prefectures_Number;
    }

    /**
     * 都道府県番号 (例: 32)
     * @param Prefectures_Number the Prefectures_Number to set
     */
    public void setPrefectures_Number(String Prefectures_Number) {
        this.Prefectures_Number = Prefectures_Number;
    }

    /**
     * 点数表(1：医科) (例: 1)
     * @return the Point_list
     */
    public String getPoint_list() {
        return Point_list;
    }

    /**
     * 点数表(1：医科) (例: 1)
     * @param Point_list the Point_list to set
     */
    public void setPoint_list(String Point_list) {
        this.Point_list = Point_list;
    }

    /**
     * 医療機関コード (例: 1234567)
     * @return the Institution_Code
     */
    public String getInstitution_Code() {
        return Institution_Code;
    }

    /**
     * 医療機関コード (例: 1234567)
     * @param Institution_Code the Institution_Code to set
     */
    public void setInstitution_Code(String Institution_Code) {
        this.Institution_Code = Institution_Code;
    }

    /**
     * 医療機関種別(1：病院、2：診療所) (例: 1)
     * @return the Institution_Speciation
     */
    public String getInstitution_Speciation() {
        return Institution_Speciation;
    }

    /**
     * 医療機関種別(1：病院、2：診療所) (例: 1)
     * @param Institution_Speciation the Institution_Speciation to set
     */
    public void setInstitution_Speciation(String Institution_Speciation) {
        this.Institution_Speciation = Institution_Speciation;
    }

    /**
     * 医療機関ID (例: JPN000000000000)
     * @return the Institution_Id
     */
    public String getInstitution_Id() {
        return Institution_Id;
    }

    /**
     * 医療機関ID (例: JPN000000000000)
     * @param Institution_Id the Institution_Id to set
     */
    public void setInstitution_Id(String Institution_Id) {
        this.Institution_Id = Institution_Id;
    }

    /**
     * 医療機関名称 (例: 医療法人　オルカ)
     * @return the Institution_WholeName
     */
    public String getInstitution_WholeName() {
        return Institution_WholeName;
    }

    /**
     * 医療機関名称 (例: 医療法人　オルカ)
     * @param Institution_WholeName the Institution_WholeName to set
     */
    public void setInstitution_WholeName(String Institution_WholeName) {
        this.Institution_WholeName = Institution_WholeName;
    }

    /**
     * 開設者名称 (例: 日本　一)
     * @return the Establisher_WholeName
     */
    public String getEstablisher_WholeName() {
        return Establisher_WholeName;
    }

    /**
     * 開設者名称 (例: 日本　一)
     * @param Establisher_WholeName the Establisher_WholeName to set
     */
    public void setEstablisher_WholeName(String Establisher_WholeName) {
        this.Establisher_WholeName = Establisher_WholeName;
    }

    /**
     * 管理者名称 (例: 日本　一)
     * @return the Administrator_WholeName
     */
    public String getAdministrator_WholeName() {
        return Administrator_WholeName;
    }

    /**
     * 管理者名称 (例: 日本　一)
     * @param Administrator_WholeName the Administrator_WholeName to set
     */
    public void setAdministrator_WholeName(String Administrator_WholeName) {
        this.Administrator_WholeName = Administrator_WholeName;
    }

    /**
     * 連絡先・広告情報 (例:  )
     * @return the Address_Information
     */
    public AddressInformation getAddress_Information() {
        return Address_Information;
    }

    /**
     * 連絡先・広告情報 (例:  )
     * @param Address_Information the Address_Information to set
     */
    public void setAddress_Information(AddressInformation Address_Information) {
        this.Address_Information = Address_Information;
    }

    /**
     * Short_Institution_WholeName
     *
     * @return Short_Institution_WholeName
     */
    public String getShort_Institution_WholeName() {
        return Short_Institution_WholeName;
    }

    /**
     * Short_Institution_WholeName
     *
     * @param Short_Institution_WholeName to set
     */
    public void setShort_Institution_WholeName(String Short_Institution_WholeName) {
        this.Short_Institution_WholeName = Short_Institution_WholeName;
    }

    /**
     * Hospital_bed_Capacity
     *
     * @return Hospital_bed_Capacity
     */
    public String getHospital_bed_Capacity() {
        return Hospital_bed_Capacity;
    }

    /**
     * Hospital_bed_Capacity
     *
     * @param Hospital_bed_Capacity to set
     */
    public void setHospital_bed_Capacity(String Hospital_bed_Capacity) {
        this.Hospital_bed_Capacity = Hospital_bed_Capacity;
    }

    /**
     * Hospital_bed_Capacity_General
     *
     * @return Hospital_bed_Capacity_General
     */
    public String getHospital_bed_Capacity_General() {
        return Hospital_bed_Capacity_General;
    }

    /**
     * Hospital_bed_Capacity_General
     *
     * @param Hospital_bed_Capacity_General to set
     */
    public void setHospital_bed_Capacity_General(String Hospital_bed_Capacity_General) {
        this.Hospital_bed_Capacity_General = Hospital_bed_Capacity_General;
    }

    /**
     * Om_Payment_Class
     *
     * @return Om_Payment_Class
     */
    public String getOm_Payment_Class() {
        return Om_Payment_Class;
    }

    /**
     * Om_Payment_Class
     *
     * @param Om_Payment_Class to set
     */
    public void setOm_Payment_Class(String Om_Payment_Class) {
        this.Om_Payment_Class = Om_Payment_Class;
    }

    /**
     * Om_Payment_Class_Name
     *
     * @return Om_Payment_Class_Name
     */
    public String getOm_Payment_Class_Name() {
        return Om_Payment_Class_Name;
    }

    /**
     * Om_Payment_Class_Name
     *
     * @param Om_Payment_Class_Name to set
     */
    public void setOm_Payment_Class_Name(String Om_Payment_Class_Name) {
        this.Om_Payment_Class_Name = Om_Payment_Class_Name;
    }

    /**
     * Old_General_Hospital_Class
     *
     * @return Old_General_Hospital_Class
     */
    public String getOld_General_Hospital_Class() {
        return Old_General_Hospital_Class;
    }

    /**
     * Old_General_Hospital_Class
     *
     * @param Old_General_Hospital_Class to set
     */
    public void setOld_General_Hospital_Class(String Old_General_Hospital_Class) {
        this.Old_General_Hospital_Class = Old_General_Hospital_Class;
    }

    /**
     * Old_General_Hospital_Class_Name
     *
     * @return Old_General_Hospital_Class_Name
     */
    public String getOld_General_Hospital_Class_Name() {
        return Old_General_Hospital_Class_Name;
    }

    /**
     * Old_General_Hospital_Class_Name
     *
     * @param Old_General_Hospital_Class_Name to set
     */
    public void setOld_General_Hospital_Class_Name(String Old_General_Hospital_Class_Name) {
        this.Old_General_Hospital_Class_Name = Old_General_Hospital_Class_Name;
    }

    /**
     * Outside_Class
     *
     * @return Outside_Class
     */
    public String getOutside_Class() {
        return Outside_Class;
    }

    /**
     * Outside_Class
     *
     * @param Outside_Class to set
     */
    public void setOutside_Class(String Outside_Class) {
        this.Outside_Class = Outside_Class;
    }

    /**
     * Outside_Class_Name
     *
     * @return Outside_Class_Name
     */
    public String getOutside_Class_Name() {
        return Outside_Class_Name;
    }

    /**
     * Outside_Class_Name
     *
     * @param Outside_Class_Name to set
     */
    public void setOutside_Class_Name(String Outside_Class_Name) {
        this.Outside_Class_Name = Outside_Class_Name;
    }

    /**
     * Institution_Code_Kanji
     *
     * @return Institution_Code_Kanji
     */
    public String getInstitution_Code_Kanji() {
        return Institution_Code_Kanji;
    }

    /**
     * Institution_Code_Kanji
     *
     * @param Institution_Code_Kanji to set
     */
    public void setInstitution_Code_Kanji(String Institution_Code_Kanji) {
        this.Institution_Code_Kanji = Institution_Code_Kanji;
    }

    /**
     * Delivery_Organization_Control_Number
     *
     * @return Delivery_Organization_Control_Number
     */
    public String getDelivery_Organization_Control_Number() {
        return Delivery_Organization_Control_Number;
    }

    /**
     * Delivery_Organization_Control_Number
     *
     * @param Delivery_Organization_Control_Number to set
     */
    public void setDelivery_Organization_Control_Number(String Delivery_Organization_Control_Number) {
        this.Delivery_Organization_Control_Number = Delivery_Organization_Control_Number;
    }

    /**
     * Print_Invoice_Receipt_Class
     *
     * @return Print_Invoice_Receipt_Class
     */
    public String getPrint_Invoice_Receipt_Class() {
        return Print_Invoice_Receipt_Class;
    }

    /**
     * Print_Invoice_Receipt_Class
     *
     * @param Print_Invoice_Receipt_Class to set
     */
    public void setPrint_Invoice_Receipt_Class(String Print_Invoice_Receipt_Class) {
        this.Print_Invoice_Receipt_Class = Print_Invoice_Receipt_Class;
    }

    /**
     * Print_Invoice_Receipt_Class_Name
     *
     * @return Print_Invoice_Receipt_Class_Name
     */
    public String getPrint_Invoice_Receipt_Class_Name() {
        return Print_Invoice_Receipt_Class_Name;
    }

    /**
     * Print_Invoice_Receipt_Class_Name
     *
     * @param Print_Invoice_Receipt_Class_Name to set
     */
    public void setPrint_Invoice_Receipt_Class_Name(String Print_Invoice_Receipt_Class_Name) {
        this.Print_Invoice_Receipt_Class_Name = Print_Invoice_Receipt_Class_Name;
    }

    /**
     * Print_Prescription_Class
     *
     * @return Print_Prescription_Class
     */
    public String getPrint_Prescription_Class() {
        return Print_Prescription_Class;
    }

    /**
     * Print_Prescription_Class
     *
     * @param Print_Prescription_Class to set
     */
    public void setPrint_Prescription_Class(String Print_Prescription_Class) {
        this.Print_Prescription_Class = Print_Prescription_Class;
    }

    /**
     * Print_Prescription_Class_Name
     *
     * @return Print_Prescription_Class_Name
     */
    public String getPrint_Prescription_Class_Name() {
        return Print_Prescription_Class_Name;
    }

    /**
     * Print_Prescription_Class_Name
     *
     * @param Print_Prescription_Class_Name to set
     */
    public void setPrint_Prescription_Class_Name(String Print_Prescription_Class_Name) {
        this.Print_Prescription_Class_Name = Print_Prescription_Class_Name;
    }

    /**
     * Last_Prescription_Display_Class
     *
     * @return Last_Prescription_Display_Class
     */
    public String getLast_Prescription_Display_Class() {
        return Last_Prescription_Display_Class;
    }

    /**
     * Last_Prescription_Display_Class
     *
     * @param Last_Prescription_Display_Class to set
     */
    public void setLast_Prescription_Display_Class(String Last_Prescription_Display_Class) {
        this.Last_Prescription_Display_Class = Last_Prescription_Display_Class;
    }

    /**
     * Last_Prescription_Display_Class_Name
     *
     * @return Last_Prescription_Display_Class_Name
     */
    public String getLast_Prescription_Display_Class_Name() {
        return Last_Prescription_Display_Class_Name;
    }

    /**
     * Last_Prescription_Display_Class_Name
     *
     * @param Last_Prescription_Display_Class_Name to set
     */
    public void setLast_Prescription_Display_Class_Name(String Last_Prescription_Display_Class_Name) {
        this.Last_Prescription_Display_Class_Name = Last_Prescription_Display_Class_Name;
    }

    /**
     * Print_Medicine_Information_Class
     *
     * @return Print_Medicine_Information_Class
     */
    public String getPrint_Medicine_Information_Class() {
        return Print_Medicine_Information_Class;
    }

    /**
     * Print_Medicine_Information_Class
     *
     * @param Print_Medicine_Information_Class to set
     */
    public void setPrint_Medicine_Information_Class(String Print_Medicine_Information_Class) {
        this.Print_Medicine_Information_Class = Print_Medicine_Information_Class;
    }

    /**
     * Print_Medicine_Information_Class_Name
     *
     * @return Print_Medicine_Information_Class_Name
     */
    public String getPrint_Medicine_Information_Class_Name() {
        return Print_Medicine_Information_Class_Name;
    }

    /**
     * Print_Medicine_Information_Class_Name
     *
     * @param Print_Medicine_Information_Class_Name to set
     */
    public void setPrint_Medicine_Information_Class_Name(String Print_Medicine_Information_Class_Name) {
        this.Print_Medicine_Information_Class_Name = Print_Medicine_Information_Class_Name;
    }

    /**
     * Print_Statement_Class
     *
     * @return Print_Statement_Class
     */
    public String getPrint_Statement_Class() {
        return Print_Statement_Class;
    }

    /**
     * Print_Statement_Class
     *
     * @param Print_Statement_Class to set
     */
    public void setPrint_Statement_Class(String Print_Statement_Class) {
        this.Print_Statement_Class = Print_Statement_Class;
    }

    /**
     * Print_Statement_Class_Name
     *
     * @return Print_Statement_Class_Name
     */
    public String getPrint_Statement_Class_Name() {
        return Print_Statement_Class_Name;
    }

    /**
     * Print_Statement_Class_Name
     *
     * @param Print_Statement_Class_Name to set
     */
    public void setPrint_Statement_Class_Name(String Print_Statement_Class_Name) {
        this.Print_Statement_Class_Name = Print_Statement_Class_Name;
    }

    /**
     * Print_Medication_Note_Class
     *
     * @return Print_Medication_Note_Class
     */
    public String getPrint_Medication_Note_Class() {
        return Print_Medication_Note_Class;
    }

    /**
     * Print_Medication_Note_Class
     *
     * @param Print_Medication_Note_Class to set
     */
    public void setPrint_Medication_Note_Class(String Print_Medication_Note_Class) {
        this.Print_Medication_Note_Class = Print_Medication_Note_Class;
    }

    /**
     * Print_Medication_Note_Class_Name
     *
     * @return Print_Medication_Note_Class_Name
     */
    public String getPrint_Medication_Note_Class_Name() {
        return Print_Medication_Note_Class_Name;
    }

    /**
     * Print_Medication_Note_Class_Name
     *
     * @param Print_Medication_Note_Class_Name to set
     */
    public void setPrint_Medication_Note_Class_Name(String Print_Medication_Note_Class_Name) {
        this.Print_Medication_Note_Class_Name = Print_Medication_Note_Class_Name;
    }

    /**
     * Print_Appointment_Form_Class
     *
     * @return Print_Appointment_Form_Class
     */
    public String getPrint_Appointment_Form_Class() {
        return Print_Appointment_Form_Class;
    }

    /**
     * Print_Appointment_Form_Class
     *
     * @param Print_Appointment_Form_Class to set
     */
    public void setPrint_Appointment_Form_Class(String Print_Appointment_Form_Class) {
        this.Print_Appointment_Form_Class = Print_Appointment_Form_Class;
    }

    /**
     * Print_Appointment_Form_Class_Name
     *
     * @return Print_Appointment_Form_Class_Name
     */
    public String getPrint_Appointment_Form_Class_Name() {
        return Print_Appointment_Form_Class_Name;
    }

    /**
     * Print_Appointment_Form_Class_Name
     *
     * @param Print_Appointment_Form_Class_Name to set
     */
    public void setPrint_Appointment_Form_Class_Name(String Print_Appointment_Form_Class_Name) {
        this.Print_Appointment_Form_Class_Name = Print_Appointment_Form_Class_Name;
    }

    /**
     * Data_Collection_Creation_Class
     *
     * @return Data_Collection_Creation_Class
     */
    public String getData_Collection_Creation_Class() {
        return Data_Collection_Creation_Class;
    }

    /**
     * Data_Collection_Creation_Class
     *
     * @param Data_Collection_Creation_Class to set
     */
    public void setData_Collection_Creation_Class(String Data_Collection_Creation_Class) {
        this.Data_Collection_Creation_Class = Data_Collection_Creation_Class;
    }

    /**
     * Data_Collection_Creation_Class_Name
     *
     * @return Data_Collection_Creation_Class_Name
     */
    public String getData_Collection_Creation_Class_Name() {
        return Data_Collection_Creation_Class_Name;
    }

    /**
     * Data_Collection_Creation_Class_Name
     *
     * @param Data_Collection_Creation_Class_Name to set
     */
    public void setData_Collection_Creation_Class_Name(String Data_Collection_Creation_Class_Name) {
        this.Data_Collection_Creation_Class_Name = Data_Collection_Creation_Class_Name;
    }

    /**
     * Data_Collection_Submission_Method_Class
     *
     * @return Data_Collection_Submission_Method_Class
     */
    public String getData_Collection_Submission_Method_Class() {
        return Data_Collection_Submission_Method_Class;
    }

    /**
     * Data_Collection_Submission_Method_Class
     *
     * @param Data_Collection_Submission_Method_Class to set
     */
    public void setData_Collection_Submission_Method_Class(String Data_Collection_Submission_Method_Class) {
        this.Data_Collection_Submission_Method_Class = Data_Collection_Submission_Method_Class;
    }

    /**
     * Data_Collection_Submission_Method_Class_Name
     *
     * @return Data_Collection_Submission_Method_Class_Name
     */
    public String getData_Collection_Submission_Method_Class_Name() {
        return Data_Collection_Submission_Method_Class_Name;
    }

    /**
     * Data_Collection_Submission_Method_Class_Name
     *
     * @param Data_Collection_Submission_Method_Class_Name to set
     */
    public void setData_Collection_Submission_Method_Class_Name(String Data_Collection_Submission_Method_Class_Name) {
        this.Data_Collection_Submission_Method_Class_Name = Data_Collection_Submission_Method_Class_Name;
    }

    /**
     * Orca_Surveillance_Class
     *
     * @return Orca_Surveillance_Class
     */
    public String getOrca_Surveillance_Class() {
        return Orca_Surveillance_Class;
    }

    /**
     * Orca_Surveillance_Class
     *
     * @param Orca_Surveillance_Class to set
     */
    public void setOrca_Surveillance_Class(String Orca_Surveillance_Class) {
        this.Orca_Surveillance_Class = Orca_Surveillance_Class;
    }

    /**
     * Orca_Surveillance_Class_Name
     *
     * @return Orca_Surveillance_Class_Name
     */
    public String getOrca_Surveillance_Class_Name() {
        return Orca_Surveillance_Class_Name;
    }

    /**
     * Orca_Surveillance_Class_Name
     *
     * @param Orca_Surveillance_Class_Name to set
     */
    public void setOrca_Surveillance_Class_Name(String Orca_Surveillance_Class_Name) {
        this.Orca_Surveillance_Class_Name = Orca_Surveillance_Class_Name;
    }

    /**
     * Reduction_Calculation_Object_Class
     *
     * @return Reduction_Calculation_Object_Class
     */
    public String getReduction_Calculation_Object_Class() {
        return Reduction_Calculation_Object_Class;
    }

    /**
     * Reduction_Calculation_Object_Class
     *
     * @param Reduction_Calculation_Object_Class to set
     */
    public void setReduction_Calculation_Object_Class(String Reduction_Calculation_Object_Class) {
        this.Reduction_Calculation_Object_Class = Reduction_Calculation_Object_Class;
    }

    /**
     * Reduction_Calculation_Object_Class_Name
     *
     * @return Reduction_Calculation_Object_Class_Name
     */
    public String getReduction_Calculation_Object_Class_Name() {
        return Reduction_Calculation_Object_Class_Name;
    }

    /**
     * Reduction_Calculation_Object_Class_Name
     *
     * @param Reduction_Calculation_Object_Class_Name to set
     */
    public void setReduction_Calculation_Object_Class_Name(String Reduction_Calculation_Object_Class_Name) {
        this.Reduction_Calculation_Object_Class_Name = Reduction_Calculation_Object_Class_Name;
    }

    /**
     * Ac_Money_Rounding_Reduction_Class
     *
     * @return Ac_Money_Rounding_Reduction_Class
     */
    public String getAc_Money_Rounding_Reduction_Class() {
        return Ac_Money_Rounding_Reduction_Class;
    }

    /**
     * Ac_Money_Rounding_Reduction_Class
     *
     * @param Ac_Money_Rounding_Reduction_Class to set
     */
    public void setAc_Money_Rounding_Reduction_Class(String Ac_Money_Rounding_Reduction_Class) {
        this.Ac_Money_Rounding_Reduction_Class = Ac_Money_Rounding_Reduction_Class;
    }

    /**
     * Ac_Money_Rounding_Reduction_Class_Name
     *
     * @return Ac_Money_Rounding_Reduction_Class_Name
     */
    public String getAc_Money_Rounding_Reduction_Class_Name() {
        return Ac_Money_Rounding_Reduction_Class_Name;
    }

    /**
     * Ac_Money_Rounding_Reduction_Class_Name
     *
     * @param Ac_Money_Rounding_Reduction_Class_Name to set
     */
    public void setAc_Money_Rounding_Reduction_Class_Name(String Ac_Money_Rounding_Reduction_Class_Name) {
        this.Ac_Money_Rounding_Reduction_Class_Name = Ac_Money_Rounding_Reduction_Class_Name;
    }

    /**
     * Ac_Money_Rounding_No_Reduction_Information
     *
     * @return Ac_Money_Rounding_No_Reduction_Information
     */
    public AcMoneyRoundingNoReductionInformation getAc_Money_Rounding_No_Reduction_Information() {
        return Ac_Money_Rounding_No_Reduction_Information;
    }

    /**
     * Ac_Money_Rounding_No_Reduction_Information
     *
     * @param Ac_Money_Rounding_No_Reduction_Information to set
     */
    public void setAc_Money_Rounding_No_Reduction_Information(AcMoneyRoundingNoReductionInformation Ac_Money_Rounding_No_Reduction_Information) {
        this.Ac_Money_Rounding_No_Reduction_Information = Ac_Money_Rounding_No_Reduction_Information;
    }

    /**
     * Third_Party_Money_Calculation_Class
     *
     * @return Third_Party_Money_Calculation_Class
     */
    public String getThird_Party_Money_Calculation_Class() {
        return Third_Party_Money_Calculation_Class;
    }

    /**
     * Third_Party_Money_Calculation_Class
     *
     * @param Third_Party_Money_Calculation_Class to set
     */
    public void setThird_Party_Money_Calculation_Class(String Third_Party_Money_Calculation_Class) {
        this.Third_Party_Money_Calculation_Class = Third_Party_Money_Calculation_Class;
    }

    /**
     * Third_Party_Money_Calculation_Class_Name
     *
     * @return Third_Party_Money_Calculation_Class_Name
     */
    public String getThird_Party_Money_Calculation_Class_Name() {
        return Third_Party_Money_Calculation_Class_Name;
    }

    /**
     * Third_Party_Money_Calculation_Class_Name
     *
     * @param Third_Party_Money_Calculation_Class_Name to set
     */
    public void setThird_Party_Money_Calculation_Class_Name(String Third_Party_Money_Calculation_Class_Name) {
        this.Third_Party_Money_Calculation_Class_Name = Third_Party_Money_Calculation_Class_Name;
    }

    /**
     * Tax_Rounding_Class
     *
     * @return Tax_Rounding_Class
     */
    public String getTax_Rounding_Class() {
        return Tax_Rounding_Class;
    }

    /**
     * Tax_Rounding_Class
     *
     * @param Tax_Rounding_Class to set
     */
    public void setTax_Rounding_Class(String Tax_Rounding_Class) {
        this.Tax_Rounding_Class = Tax_Rounding_Class;
    }

    /**
     * Tax_Rounding_Class_Name
     *
     * @return Tax_Rounding_Class_Name
     */
    public String getTax_Rounding_Class_Name() {
        return Tax_Rounding_Class_Name;
    }

    /**
     * Tax_Rounding_Class_Name
     *
     * @param Tax_Rounding_Class_Name to set
     */
    public void setTax_Rounding_Class_Name(String Tax_Rounding_Class_Name) {
        this.Tax_Rounding_Class_Name = Tax_Rounding_Class_Name;
    }

    /**
     * Self_Insurance_Total_Class
     *
     * @return Self_Insurance_Total_Class
     */
    public String getSelf_Insurance_Total_Class() {
        return Self_Insurance_Total_Class;
    }

    /**
     * Self_Insurance_Total_Class
     *
     * @param Self_Insurance_Total_Class to set
     */
    public void setSelf_Insurance_Total_Class(String Self_Insurance_Total_Class) {
        this.Self_Insurance_Total_Class = Self_Insurance_Total_Class;
    }

    /**
     * Self_Insurance_Total_Class_Name
     *
     * @return Self_Insurance_Total_Class_Name
     */
    public String getSelf_Insurance_Total_Class_Name() {
        return Self_Insurance_Total_Class_Name;
    }

    /**
     * Self_Insurance_Total_Class_Name
     *
     * @param Self_Insurance_Total_Class_Name to set
     */
    public void setSelf_Insurance_Total_Class_Name(String Self_Insurance_Total_Class_Name) {
        this.Self_Insurance_Total_Class_Name = Self_Insurance_Total_Class_Name;
    }

    /**
     * Local_Public_Expenses_Insurance_Number_Tab_Class
     *
     * @return Local_Public_Expenses_Insurance_Number_Tab_Class
     */
    public String getLocal_Public_Expenses_Insurance_Number_Tab_Class() {
        return Local_Public_Expenses_Insurance_Number_Tab_Class;
    }

    /**
     * Local_Public_Expenses_Insurance_Number_Tab_Class
     *
     * @param Local_Public_Expenses_Insurance_Number_Tab_Class to set
     */
    public void setLocal_Public_Expenses_Insurance_Number_Tab_Class(String Local_Public_Expenses_Insurance_Number_Tab_Class) {
        this.Local_Public_Expenses_Insurance_Number_Tab_Class = Local_Public_Expenses_Insurance_Number_Tab_Class;
    }

    /**
     * Local_Public_Expenses_Insurance_Number_Tab_Class_Name
     *
     * @return Local_Public_Expenses_Insurance_Number_Tab_Class_Name
     */
    public String getLocal_Public_Expenses_Insurance_Number_Tab_Class_Name() {
        return Local_Public_Expenses_Insurance_Number_Tab_Class_Name;
    }

    /**
     * Local_Public_Expenses_Insurance_Number_Tab_Class_Name
     *
     * @param Local_Public_Expenses_Insurance_Number_Tab_Class_Name to set
     */
    public void setLocal_Public_Expenses_Insurance_Number_Tab_Class_Name(String Local_Public_Expenses_Insurance_Number_Tab_Class_Name) {
        this.Local_Public_Expenses_Insurance_Number_Tab_Class_Name = Local_Public_Expenses_Insurance_Number_Tab_Class_Name;
    }

    /**
     * Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class
     *
     * @return Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class
     */
    public String getRehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class() {
        return Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class;
    }

    /**
     * Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class
     *
     * @param Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class to set
     */
    public void setRehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class(String Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class) {
        this.Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class = Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class;
    }

    /**
     * Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name
     *
     * @return Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name
     */
    public String getRehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name() {
        return Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name;
    }

    /**
     * Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name
     *
     * @param Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name to set
     */
    public void setRehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name(String Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name) {
        this.Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name = Rehabilitation_Nurture_Credit_Limit_Calculate_Daily_Rate_Class_Name;
    }

    /**
     * Oe_Rounding_Class
     *
     * @return Oe_Rounding_Class
     */
    public String getOe_Rounding_Class() {
        return Oe_Rounding_Class;
    }

    /**
     * Oe_Rounding_Class
     *
     * @param Oe_Rounding_Class to set
     */
    public void setOe_Rounding_Class(String Oe_Rounding_Class) {
        this.Oe_Rounding_Class = Oe_Rounding_Class;
    }

    /**
     * Oe_Rounding_Class_Name
     *
     * @return Oe_Rounding_Class_Name
     */
    public String getOe_Rounding_Class_Name() {
        return Oe_Rounding_Class_Name;
    }

    /**
     * Oe_Rounding_Class_Name
     *
     * @param Oe_Rounding_Class_Name to set
     */
    public void setOe_Rounding_Class_Name(String Oe_Rounding_Class_Name) {
        this.Oe_Rounding_Class_Name = Oe_Rounding_Class_Name;
    }
}
package open.dolphin.orca.orcaapi.bean;

/**
 * 患者病名情報 レスポンス.
 * http://www.orca.med.or.jp/receipt/tec/api/disease.html
 * @author pns
 */
public class DiseaseInfores {
    /**
     * 実施日 (例: 2012-05-29)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 17:11:59)
     */
    private String Information_Time;

    /**
     * 結果コード(ゼロ以外エラー) (例: 00)
     */
    private String Api_Result;

    /**
     * エラーメッセージ (例: 処理終了)
     */
    private String Api_Result_Message;

    /**
     *   (例: MedicalInfo)
     */
    private String Reskey;

    /**
     * 病名情報オーバーフラグ（True:返却対象の病名が200件を越えている） (例: False)
     */
    private String Information_Overflow;

    /**
     * 患者病名情報 (例:  )
     */
    private DiseaseInforesSub Disease_Infores;

    /**
     * 基準月 (例: 2012-05)
     */
    private String Base_Date;

    /**
     * 病名情報（繰り返し　２００） (例:  )
     */
    private DiseaseInformation[] Disease_Information;

    /**
     * 実施日 (例: 2012-05-29)
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2012-05-29)
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 17:11:59)
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 17:11:59)
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード(ゼロ以外エラー) (例: 00)
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード(ゼロ以外エラー) (例: 00)
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * エラーメッセージ (例: 処理終了)
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * エラーメッセージ (例: 処理終了)
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     *   (例: MedicalInfo)
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     *   (例: MedicalInfo)
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 病名情報オーバーフラグ（True:返却対象の病名が200件を越えている） (例: False)
     * @return the Information_Overflow
     */
    public String getInformation_Overflow() {
        return Information_Overflow;
    }

    /**
     * 病名情報オーバーフラグ（True:返却対象の病名が200件を越えている） (例: False)
     * @param Information_Overflow the Information_Overflow to set
     */
    public void setInformation_Overflow(String Information_Overflow) {
        this.Information_Overflow = Information_Overflow;
    }

    /**
     * 患者病名情報 (例:  )
     * @return the Disease_Infores
     */
    public DiseaseInforesSub getDisease_Infores() {
        return Disease_Infores;
    }

    /**
     * 患者病名情報 (例:  )
     * @param Disease_Infores the Disease_Infores to set
     */
    public void setDisease_Infores(DiseaseInforesSub Disease_Infores) {
        this.Disease_Infores = Disease_Infores;
    }

    /**
     * 基準月 (例: 2012-05)
     * @return the Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * 基準月 (例: 2012-05)
     * @param Base_Date the Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }

    /**
     * 病名情報（繰り返し　２００） (例:  )
     * @return the Disease_Information
     */
    public DiseaseInformation[] getDisease_Information() {
        return Disease_Information;
    }

    /**
     * 病名情報（繰り返し　２００） (例:  )
     * @param Disease_Information the Disease_Information to set
     */
    public void setDisease_Information(DiseaseInformation[] Disease_Information) {
        this.Disease_Information = Disease_Information;
    }
}


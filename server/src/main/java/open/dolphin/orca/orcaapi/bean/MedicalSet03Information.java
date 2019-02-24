package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Set03_Information. 診療行為自動算定・チェック設定項目名を変更(2018-09-25)
 * @author pns
 */
public class MedicalSet03Information {
    /**
     * 薬剤投与量チェックシステム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Dosage_Chk_Class;

    /**
     * 薬剤投与量チェック名称 (例: )
     */
    private String Dosage_Chk_Class_Name;

    /**
     * 投薬30日超チェックシステム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Dosage_30Over_Chk_Class;

    /**
     * 投薬30日超チェック名称 (例: )
     */
    private String Dosage_30Over_Chk_Class_Name;

    /**
     * 薬剤情報提供料チェックシステム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String DrugInf_Chk_Class;

    /**
     * 薬剤情報提供料チェック名称 (例: )
     */
    private String DrugInf_Chk_Class_Name;

    /**
     * 手帳記載加算確認システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Medication_Notebook_Chk_Class;

    /**
     * 手帳記載加算確認名称 (例: )
     */
    private String Medication_Notebook_Chk_Class_Name;

    /**
     * 時間外緊急院内検査加算・画像診断加算算定チェックシステム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Time_Chk_Class;

    /**
     * 時間外緊急院内検査加算・画像診断加算算定チェック名称 (例: )
     */
    private String Time_Chk_Class_Name;

    /**
     * 精神療法20未満加算区分システム管理「1038診療行為機能情報」 (例: )
     */
    private String Psy20_Addition_Auto_Class;

    /**
     * 精神療法20未満加算区分名称 (例: )
     */
    private String Psy20_Addition_Auto_Class_Name;

    /**
     * 心身療法20未満加算区分システム管理「1038診療行為機能情報」 (例: )
     */
    private String Mind20_Addition_Auto_Class;

    /**
     * 心身療法20未満加算区分名称 (例: )
     */
    private String Mind20_Addition_Auto_Class_Name;

    /**
     * 残量廃棄算定システム管理「1038診療行為機能情報」 (例: )
     */
    private String Rem_Addition_Auto_Class;

    /**
     * 残量廃棄算定名称 (例: )
     */
    private String Rem_Addition_Auto_Class_Name;

    /**
     * 調剤技術基本料システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Dispensing_Auto_Class;

    /**
     * 調剤技術基本料名称 (例: )
     */
    private String Dispensing_Auto_Class_Name;

    /**
     * 薬剤情報提供料(一般)システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String DrugInf_Auto_Class;

    /**
     * 薬剤情報提供料(一般)名称 (例: )
     */
    private String DrugInf_Auto_Class_Name;

    /**
     * 薬剤情報提供料(老人)システム管理「1007自動算定・チェック機能制御情報」※2 (例: )
     */
    private String Om_DrugInf_Auto_Class;

    /**
     * 薬剤情報提供料(老人)名称 (例: )
     */
    private String Om_DrugInf_Auto_Class_Name;

    /**
     * 特定薬剤治療管理料システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Specific_Drug_Auto_Class;

    /**
     * 特定薬剤治療管理料名称 (例: )
     */
    private String Specific_Drug_Auto_Class_Name;

    /**
     * 訂正時の自動発生(外来)システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Outpatient_Modify_Auto_Class;

    /**
     * 訂正時の自動発生(外来)名称 (例: )
     */
    private String Outpatient_Modify_Auto_Class_Name;

    /**
     * 訂正時の自動発生(特定疾患処方管理)システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Modify_Disease_Med_Auto_Class;

    /**
     * 訂正時の自動発生(特定疾患処方管理)名称 (例: )
     */
    private String Modify_Disease_Med_Auto_Class_Name;

    /**
     * 外来迅速検体検査加算自動発生システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Laboratory_text_Med_Auto_Class;

    /**
     * 外来迅速検体検査加算自動発生名称 (例: )
     */
    private String Laboratory_text_Med_Auto_Class_Name;

    /**
     * 画像診断管理加算システム管理「1007自動算定・チェック機能制御情報」 (例: )
     */
    private String Image_Addition_Auto_Class;

    /**
     * 画像診断管理加算名称 (例: )
     */
    private String Image_Addition_Auto_Class_Name;

    /**
     * Dosage_Chk_Class
     *
     * @return Dosage_Chk_Class
     */
    public String getDosage_Chk_Class() {
        return Dosage_Chk_Class;
    }

    /**
     * Dosage_Chk_Class
     *
     * @param Dosage_Chk_Class to set
     */
    public void setDosage_Chk_Class(String Dosage_Chk_Class) {
        this.Dosage_Chk_Class = Dosage_Chk_Class;
    }

    /**
     * Dosage_Chk_Class_Name
     *
     * @return Dosage_Chk_Class_Name
     */
    public String getDosage_Chk_Class_Name() {
        return Dosage_Chk_Class_Name;
    }

    /**
     * Dosage_Chk_Class_Name
     *
     * @param Dosage_Chk_Class_Name to set
     */
    public void setDosage_Chk_Class_Name(String Dosage_Chk_Class_Name) {
        this.Dosage_Chk_Class_Name = Dosage_Chk_Class_Name;
    }

    /**
     * Dosage_30Over_Chk_Class
     *
     * @return Dosage_30Over_Chk_Class
     */
    public String getDosage_30Over_Chk_Class() {
        return Dosage_30Over_Chk_Class;
    }

    /**
     * Dosage_30Over_Chk_Class
     *
     * @param Dosage_30Over_Chk_Class to set
     */
    public void setDosage_30Over_Chk_Class(String Dosage_30Over_Chk_Class) {
        this.Dosage_30Over_Chk_Class = Dosage_30Over_Chk_Class;
    }

    /**
     * Dosage_30Over_Chk_Class_Name
     *
     * @return Dosage_30Over_Chk_Class_Name
     */
    public String getDosage_30Over_Chk_Class_Name() {
        return Dosage_30Over_Chk_Class_Name;
    }

    /**
     * Dosage_30Over_Chk_Class_Name
     *
     * @param Dosage_30Over_Chk_Class_Name to set
     */
    public void setDosage_30Over_Chk_Class_Name(String Dosage_30Over_Chk_Class_Name) {
        this.Dosage_30Over_Chk_Class_Name = Dosage_30Over_Chk_Class_Name;
    }

    /**
     * DrugInf_Chk_Class
     *
     * @return DrugInf_Chk_Class
     */
    public String getDrugInf_Chk_Class() {
        return DrugInf_Chk_Class;
    }

    /**
     * DrugInf_Chk_Class
     *
     * @param DrugInf_Chk_Class to set
     */
    public void setDrugInf_Chk_Class(String DrugInf_Chk_Class) {
        this.DrugInf_Chk_Class = DrugInf_Chk_Class;
    }

    /**
     * DrugInf_Chk_Class_Name
     *
     * @return DrugInf_Chk_Class_Name
     */
    public String getDrugInf_Chk_Class_Name() {
        return DrugInf_Chk_Class_Name;
    }

    /**
     * DrugInf_Chk_Class_Name
     *
     * @param DrugInf_Chk_Class_Name to set
     */
    public void setDrugInf_Chk_Class_Name(String DrugInf_Chk_Class_Name) {
        this.DrugInf_Chk_Class_Name = DrugInf_Chk_Class_Name;
    }

    /**
     * Medication_Notebook_Chk_Class
     *
     * @return Medication_Notebook_Chk_Class
     */
    public String getMedication_Notebook_Chk_Class() {
        return Medication_Notebook_Chk_Class;
    }

    /**
     * Medication_Notebook_Chk_Class
     *
     * @param Medication_Notebook_Chk_Class to set
     */
    public void setMedication_Notebook_Chk_Class(String Medication_Notebook_Chk_Class) {
        this.Medication_Notebook_Chk_Class = Medication_Notebook_Chk_Class;
    }

    /**
     * Medication_Notebook_Chk_Class_Name
     *
     * @return Medication_Notebook_Chk_Class_Name
     */
    public String getMedication_Notebook_Chk_Class_Name() {
        return Medication_Notebook_Chk_Class_Name;
    }

    /**
     * Medication_Notebook_Chk_Class_Name
     *
     * @param Medication_Notebook_Chk_Class_Name to set
     */
    public void setMedication_Notebook_Chk_Class_Name(String Medication_Notebook_Chk_Class_Name) {
        this.Medication_Notebook_Chk_Class_Name = Medication_Notebook_Chk_Class_Name;
    }

    /**
     * Time_Chk_Class
     *
     * @return Time_Chk_Class
     */
    public String getTime_Chk_Class() {
        return Time_Chk_Class;
    }

    /**
     * Time_Chk_Class
     *
     * @param Time_Chk_Class to set
     */
    public void setTime_Chk_Class(String Time_Chk_Class) {
        this.Time_Chk_Class = Time_Chk_Class;
    }

    /**
     * Time_Chk_Class_Name
     *
     * @return Time_Chk_Class_Name
     */
    public String getTime_Chk_Class_Name() {
        return Time_Chk_Class_Name;
    }

    /**
     * Time_Chk_Class_Name
     *
     * @param Time_Chk_Class_Name to set
     */
    public void setTime_Chk_Class_Name(String Time_Chk_Class_Name) {
        this.Time_Chk_Class_Name = Time_Chk_Class_Name;
    }

    /**
     * Psy20_Addition_Auto_Class
     *
     * @return Psy20_Addition_Auto_Class
     */
    public String getPsy20_Addition_Auto_Class() {
        return Psy20_Addition_Auto_Class;
    }

    /**
     * Psy20_Addition_Auto_Class
     *
     * @param Psy20_Addition_Auto_Class to set
     */
    public void setPsy20_Addition_Auto_Class(String Psy20_Addition_Auto_Class) {
        this.Psy20_Addition_Auto_Class = Psy20_Addition_Auto_Class;
    }

    /**
     * Psy20_Addition_Auto_Class_Name
     *
     * @return Psy20_Addition_Auto_Class_Name
     */
    public String getPsy20_Addition_Auto_Class_Name() {
        return Psy20_Addition_Auto_Class_Name;
    }

    /**
     * Psy20_Addition_Auto_Class_Name
     *
     * @param Psy20_Addition_Auto_Class_Name to set
     */
    public void setPsy20_Addition_Auto_Class_Name(String Psy20_Addition_Auto_Class_Name) {
        this.Psy20_Addition_Auto_Class_Name = Psy20_Addition_Auto_Class_Name;
    }

    /**
     * Mind20_Addition_Auto_Class
     *
     * @return Mind20_Addition_Auto_Class
     */
    public String getMind20_Addition_Auto_Class() {
        return Mind20_Addition_Auto_Class;
    }

    /**
     * Mind20_Addition_Auto_Class
     *
     * @param Mind20_Addition_Auto_Class to set
     */
    public void setMind20_Addition_Auto_Class(String Mind20_Addition_Auto_Class) {
        this.Mind20_Addition_Auto_Class = Mind20_Addition_Auto_Class;
    }

    /**
     * Mind20_Addition_Auto_Class_Name
     *
     * @return Mind20_Addition_Auto_Class_Name
     */
    public String getMind20_Addition_Auto_Class_Name() {
        return Mind20_Addition_Auto_Class_Name;
    }

    /**
     * Mind20_Addition_Auto_Class_Name
     *
     * @param Mind20_Addition_Auto_Class_Name to set
     */
    public void setMind20_Addition_Auto_Class_Name(String Mind20_Addition_Auto_Class_Name) {
        this.Mind20_Addition_Auto_Class_Name = Mind20_Addition_Auto_Class_Name;
    }

    /**
     * Rem_Addition_Auto_Class
     *
     * @return Rem_Addition_Auto_Class
     */
    public String getRem_Addition_Auto_Class() {
        return Rem_Addition_Auto_Class;
    }

    /**
     * Rem_Addition_Auto_Class
     *
     * @param Rem_Addition_Auto_Class to set
     */
    public void setRem_Addition_Auto_Class(String Rem_Addition_Auto_Class) {
        this.Rem_Addition_Auto_Class = Rem_Addition_Auto_Class;
    }

    /**
     * Rem_Addition_Auto_Class_Name
     *
     * @return Rem_Addition_Auto_Class_Name
     */
    public String getRem_Addition_Auto_Class_Name() {
        return Rem_Addition_Auto_Class_Name;
    }

    /**
     * Rem_Addition_Auto_Class_Name
     *
     * @param Rem_Addition_Auto_Class_Name to set
     */
    public void setRem_Addition_Auto_Class_Name(String Rem_Addition_Auto_Class_Name) {
        this.Rem_Addition_Auto_Class_Name = Rem_Addition_Auto_Class_Name;
    }

    /**
     * Dispensing_Auto_Class
     *
     * @return Dispensing_Auto_Class
     */
    public String getDispensing_Auto_Class() {
        return Dispensing_Auto_Class;
    }

    /**
     * Dispensing_Auto_Class
     *
     * @param Dispensing_Auto_Class to set
     */
    public void setDispensing_Auto_Class(String Dispensing_Auto_Class) {
        this.Dispensing_Auto_Class = Dispensing_Auto_Class;
    }

    /**
     * Dispensing_Auto_Class_Name
     *
     * @return Dispensing_Auto_Class_Name
     */
    public String getDispensing_Auto_Class_Name() {
        return Dispensing_Auto_Class_Name;
    }

    /**
     * Dispensing_Auto_Class_Name
     *
     * @param Dispensing_Auto_Class_Name to set
     */
    public void setDispensing_Auto_Class_Name(String Dispensing_Auto_Class_Name) {
        this.Dispensing_Auto_Class_Name = Dispensing_Auto_Class_Name;
    }

    /**
     * DrugInf_Auto_Class
     *
     * @return DrugInf_Auto_Class
     */
    public String getDrugInf_Auto_Class() {
        return DrugInf_Auto_Class;
    }

    /**
     * DrugInf_Auto_Class
     *
     * @param DrugInf_Auto_Class to set
     */
    public void setDrugInf_Auto_Class(String DrugInf_Auto_Class) {
        this.DrugInf_Auto_Class = DrugInf_Auto_Class;
    }

    /**
     * DrugInf_Auto_Class_Name
     *
     * @return DrugInf_Auto_Class_Name
     */
    public String getDrugInf_Auto_Class_Name() {
        return DrugInf_Auto_Class_Name;
    }

    /**
     * DrugInf_Auto_Class_Name
     *
     * @param DrugInf_Auto_Class_Name to set
     */
    public void setDrugInf_Auto_Class_Name(String DrugInf_Auto_Class_Name) {
        this.DrugInf_Auto_Class_Name = DrugInf_Auto_Class_Name;
    }

    /**
     * Om_DrugInf_Auto_Class
     *
     * @return Om_DrugInf_Auto_Class
     */
    public String getOm_DrugInf_Auto_Class() {
        return Om_DrugInf_Auto_Class;
    }

    /**
     * Om_DrugInf_Auto_Class
     *
     * @param Om_DrugInf_Auto_Class to set
     */
    public void setOm_DrugInf_Auto_Class(String Om_DrugInf_Auto_Class) {
        this.Om_DrugInf_Auto_Class = Om_DrugInf_Auto_Class;
    }

    /**
     * Om_DrugInf_Auto_Class_Name
     *
     * @return Om_DrugInf_Auto_Class_Name
     */
    public String getOm_DrugInf_Auto_Class_Name() {
        return Om_DrugInf_Auto_Class_Name;
    }

    /**
     * Om_DrugInf_Auto_Class_Name
     *
     * @param Om_DrugInf_Auto_Class_Name to set
     */
    public void setOm_DrugInf_Auto_Class_Name(String Om_DrugInf_Auto_Class_Name) {
        this.Om_DrugInf_Auto_Class_Name = Om_DrugInf_Auto_Class_Name;
    }

    /**
     * Specific_Drug_Auto_Class
     *
     * @return Specific_Drug_Auto_Class
     */
    public String getSpecific_Drug_Auto_Class() {
        return Specific_Drug_Auto_Class;
    }

    /**
     * Specific_Drug_Auto_Class
     *
     * @param Specific_Drug_Auto_Class to set
     */
    public void setSpecific_Drug_Auto_Class(String Specific_Drug_Auto_Class) {
        this.Specific_Drug_Auto_Class = Specific_Drug_Auto_Class;
    }

    /**
     * Specific_Drug_Auto_Class_Name
     *
     * @return Specific_Drug_Auto_Class_Name
     */
    public String getSpecific_Drug_Auto_Class_Name() {
        return Specific_Drug_Auto_Class_Name;
    }

    /**
     * Specific_Drug_Auto_Class_Name
     *
     * @param Specific_Drug_Auto_Class_Name to set
     */
    public void setSpecific_Drug_Auto_Class_Name(String Specific_Drug_Auto_Class_Name) {
        this.Specific_Drug_Auto_Class_Name = Specific_Drug_Auto_Class_Name;
    }

    /**
     * Outpatient_Modify_Auto_Class
     *
     * @return Outpatient_Modify_Auto_Class
     */
    public String getOutpatient_Modify_Auto_Class() {
        return Outpatient_Modify_Auto_Class;
    }

    /**
     * Outpatient_Modify_Auto_Class
     *
     * @param Outpatient_Modify_Auto_Class to set
     */
    public void setOutpatient_Modify_Auto_Class(String Outpatient_Modify_Auto_Class) {
        this.Outpatient_Modify_Auto_Class = Outpatient_Modify_Auto_Class;
    }

    /**
     * Outpatient_Modify_Auto_Class_Name
     *
     * @return Outpatient_Modify_Auto_Class_Name
     */
    public String getOutpatient_Modify_Auto_Class_Name() {
        return Outpatient_Modify_Auto_Class_Name;
    }

    /**
     * Outpatient_Modify_Auto_Class_Name
     *
     * @param Outpatient_Modify_Auto_Class_Name to set
     */
    public void setOutpatient_Modify_Auto_Class_Name(String Outpatient_Modify_Auto_Class_Name) {
        this.Outpatient_Modify_Auto_Class_Name = Outpatient_Modify_Auto_Class_Name;
    }

    /**
     * Modify_Disease_Med_Auto_Class
     *
     * @return Modify_Disease_Med_Auto_Class
     */
    public String getModify_Disease_Med_Auto_Class() {
        return Modify_Disease_Med_Auto_Class;
    }

    /**
     * Modify_Disease_Med_Auto_Class
     *
     * @param Modify_Disease_Med_Auto_Class to set
     */
    public void setModify_Disease_Med_Auto_Class(String Modify_Disease_Med_Auto_Class) {
        this.Modify_Disease_Med_Auto_Class = Modify_Disease_Med_Auto_Class;
    }

    /**
     * Modify_Disease_Med_Auto_Class_Name
     *
     * @return Modify_Disease_Med_Auto_Class_Name
     */
    public String getModify_Disease_Med_Auto_Class_Name() {
        return Modify_Disease_Med_Auto_Class_Name;
    }

    /**
     * Modify_Disease_Med_Auto_Class_Name
     *
     * @param Modify_Disease_Med_Auto_Class_Name to set
     */
    public void setModify_Disease_Med_Auto_Class_Name(String Modify_Disease_Med_Auto_Class_Name) {
        this.Modify_Disease_Med_Auto_Class_Name = Modify_Disease_Med_Auto_Class_Name;
    }

    /**
     * Laboratory_text_Med_Auto_Class
     *
     * @return Laboratory_text_Med_Auto_Class
     */
    public String getLaboratory_text_Med_Auto_Class() {
        return Laboratory_text_Med_Auto_Class;
    }

    /**
     * Laboratory_text_Med_Auto_Class
     *
     * @param Laboratory_text_Med_Auto_Class to set
     */
    public void setLaboratory_text_Med_Auto_Class(String Laboratory_text_Med_Auto_Class) {
        this.Laboratory_text_Med_Auto_Class = Laboratory_text_Med_Auto_Class;
    }

    /**
     * Laboratory_text_Med_Auto_Class_Name
     *
     * @return Laboratory_text_Med_Auto_Class_Name
     */
    public String getLaboratory_text_Med_Auto_Class_Name() {
        return Laboratory_text_Med_Auto_Class_Name;
    }

    /**
     * Laboratory_text_Med_Auto_Class_Name
     *
     * @param Laboratory_text_Med_Auto_Class_Name to set
     */
    public void setLaboratory_text_Med_Auto_Class_Name(String Laboratory_text_Med_Auto_Class_Name) {
        this.Laboratory_text_Med_Auto_Class_Name = Laboratory_text_Med_Auto_Class_Name;
    }

    /**
     * Image_Addition_Auto_Class
     *
     * @return Image_Addition_Auto_Class
     */
    public String getImage_Addition_Auto_Class() {
        return Image_Addition_Auto_Class;
    }

    /**
     * Image_Addition_Auto_Class
     *
     * @param Image_Addition_Auto_Class to set
     */
    public void setImage_Addition_Auto_Class(String Image_Addition_Auto_Class) {
        this.Image_Addition_Auto_Class = Image_Addition_Auto_Class;
    }

    /**
     * Image_Addition_Auto_Class_Name
     *
     * @return Image_Addition_Auto_Class_Name
     */
    public String getImage_Addition_Auto_Class_Name() {
        return Image_Addition_Auto_Class_Name;
    }

    /**
     * Image_Addition_Auto_Class_Name
     *
     * @param Image_Addition_Auto_Class_Name to set
     */
    public void setImage_Addition_Auto_Class_Name(String Image_Addition_Auto_Class_Name) {
        this.Image_Addition_Auto_Class_Name = Image_Addition_Auto_Class_Name;
    }
}

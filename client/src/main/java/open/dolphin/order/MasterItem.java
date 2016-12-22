package open.dolphin.order;

/**
 * Class to hold selected master item information.
 * StampModelEditor の TablePanel の TableModel はこれを Object として保持する
 * @author  Kazuhi Minagawa, Digital Globe, Inc.
 * modified by pns
 */
public class MasterItem implements java.io.Serializable {
    private static final long serialVersionUID = -6359300744722498857L;

    /**
     * Claim subclass code マスタ項目の種別
     * SYUGI(手技=0), ZAIRYO(材料=1), YAKUZAI(薬剤=2), ADMIN(用法=3)
     */
    private int classCode;
    /** 項目名 */
    private String name;
    /** 項目コード */
    private String code;
    /** コード体系名(ICD10_2001-10-03MEDIS) Diagnosis でしか使ってない */
    private String masterTableId;
    /** 数量　１日（回）量 */
    private String number;
    /** 何日（回）分 */
    private String bundleNumber;
    /** 単位 */
    private String unit;
    /** 医事用病名コード →code に一元化*/
    //private String claimDiseaseCode;
    /** 診療行為区分(Claim 007)・点数集計先 */
    private String claimClassCode;
    /** 薬剤の場合の区分 内用1，外用6，注射薬4 */
    private String ykzKbn;
    /** X の表示に使う */
    private String dummy;

    public MasterItem() {}

    public MasterItem(int classCode, String name, String code) {
        this();
        this.classCode = classCode;
        this.name = name;
        this.code = code;
    }

    /**
     * @param classCode The classCode to set.
     */
    public void setClassCode(int classCode) {
        this.classCode = classCode;
    }

    /**
     * @return Returns the classCode.
     */
    public int getClassCode() {
        return classCode;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @param masterTableId The masterTableId to set.
     */
    public void setMasterTableId(String masterTableId) {
        this.masterTableId = masterTableId;
    }

    /**
     * @return Returns the masterTableId.
     */
    public String getMasterTableId() {
        return masterTableId;
    }

    /**
     * @param number The number to set.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return Returns the number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param unit The unit to set.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return Returns the unit.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param claimDiseaseCode The claimDiseaseCode to set.
     */
    //public void setClaimDiseaseCode(String claimDiseaseCode) {
    //    this.claimDiseaseCode = claimDiseaseCode;
    //}

    /**
     * @return Returns the claimDiseaseCode.
     */
    //public String getClaimDiseaseCode() {
    //    return claimDiseaseCode;
    //}

    /**
     * @param claimClassCode The claimClassCode to set.
     */
    public void setClaimClassCode(String claimClassCode) {
        this.claimClassCode = claimClassCode;
    }

    /**
     * @return Returns the claimClassCode.
     */
    public String getClaimClassCode() {
        return claimClassCode;
    }

    public String getYkzKbn() {
        return ykzKbn;
    }

    public void setYkzKbn(String ykzKbn) {
        this.ykzKbn = ykzKbn;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public String getBundleNumber() {
        return bundleNumber;
    }

    public void setBundleNumber(String bundleNumber) {
        this.bundleNumber = bundleNumber;
    }

    /**
     * 内服薬かどうかは用法のコードで判断する.
     * ykzKbn は stamp の編集の場合はセットされていないので null になっているので使えない.
     * setValue 時に，ModuleModel#getModel で BundleMed を取り出して，getClassCode() を
     * ClaimConst.RECEIPT_CODE_GAIYO/NAIYO と比較して ClaimConst.YKZ_KBN_GAIYO/NAIYO を
     * セットすればいいかもしれないがめんどくさい. 自分専用だからこれでいいのだ.
     * @return
     */
    public boolean isNaiyo() {
        return (Integer.valueOf(code) < 1000500);
    }

    /**
     * 頓用かどうか用法コードで判断
     * @return
     */
    public boolean isTonyo() {
        return code.startsWith("0010005");
    }

    @Override
    public String toString() {
        return getName();
    }
}

package open.dolphin.orca.orcaapi.bean;

/**
 * Physician_Information. ドクター情報(繰り返し100)
 *
 * @author pns
 */
public class PhysicianInformation {
    /**
     * ドクターコード (例: 10001)
     */
    private String Code;

    /**
     * 氏名 (例: 日本　一)
     */
    private String WholeName;

    /**
     * カナ氏名 (例: ニホン　ハジメ)
     */
    private String WholeName_inKana;

    /**
     * 医療登録番号 (例: ISEKI001)
     */
    private String Physician_Permission_Id;

    /**
     * 麻薬施用者免許証番号 (例: 001234)
     */
    private String Drug_Permission_Id;

    /**
     * 専門科コード1 (例: 01)
     */
    private String Department_Code1;

    /**
     * 専門科コード2 (例: 02)
     */
    private String Department_Code2;

    /**
     * 専門科コード3 (例:  )
     */
    private String Department_Code3;

    /**
     * 専門科コード4 (例:  )
     */
    private String Department_Code4;

    /**
     * 専門科コード5 (例:  )
     */
    private String Department_Code5;

    /**
     * ドクターコード (例: 10001)
     *
     * @return the Code
     */
    public String getCode() {
        return Code;
    }

    /**
     * ドクターコード (例: 10001)
     *
     * @param Code the Code to set
     */
    public void setCode(String Code) {
        this.Code = Code;
    }

    /**
     * 氏名 (例: 日本　一)
     *
     * @return the WholeName
     */
    public String getWholeName() {
        return WholeName;
    }

    /**
     * 氏名 (例: 日本　一)
     *
     * @param WholeName the WholeName to set
     */
    public void setWholeName(String WholeName) {
        this.WholeName = WholeName;
    }

    /**
     * カナ氏名 (例: ニホン　ハジメ)
     *
     * @return the WholeName_inKana
     */
    public String getWholeName_inKana() {
        return WholeName_inKana;
    }

    /**
     * カナ氏名 (例: ニホン　ハジメ)
     *
     * @param WholeName_inKana the WholeName_inKana to set
     */
    public void setWholeName_inKana(String WholeName_inKana) {
        this.WholeName_inKana = WholeName_inKana;
    }

    /**
     * 医療登録番号 (例: ISEKI001)
     *
     * @return the Physician_Permission_Id
     */
    public String getPhysician_Permission_Id() {
        return Physician_Permission_Id;
    }

    /**
     * 医療登録番号 (例: ISEKI001)
     *
     * @param Physician_Permission_Id the Physician_Permission_Id to set
     */
    public void setPhysician_Permission_Id(String Physician_Permission_Id) {
        this.Physician_Permission_Id = Physician_Permission_Id;
    }

    /**
     * 麻薬施用者免許証番号 (例: 001234)
     *
     * @return the Drug_Permission_Id
     */
    public String getDrug_Permission_Id() {
        return Drug_Permission_Id;
    }

    /**
     * 麻薬施用者免許証番号 (例: 001234)
     *
     * @param Drug_Permission_Id the Drug_Permission_Id to set
     */
    public void setDrug_Permission_Id(String Drug_Permission_Id) {
        this.Drug_Permission_Id = Drug_Permission_Id;
    }

    /**
     * 専門科コード1 (例: 01)
     *
     * @return the Department_Code1
     */
    public String getDepartment_Code1() {
        return Department_Code1;
    }

    /**
     * 専門科コード1 (例: 01)
     *
     * @param Department_Code1 the Department_Code1 to set
     */
    public void setDepartment_Code1(String Department_Code1) {
        this.Department_Code1 = Department_Code1;
    }

    /**
     * 専門科コード2 (例: 02)
     *
     * @return the Department_Code2
     */
    public String getDepartment_Code2() {
        return Department_Code2;
    }

    /**
     * 専門科コード2 (例: 02)
     *
     * @param Department_Code2 the Department_Code2 to set
     */
    public void setDepartment_Code2(String Department_Code2) {
        this.Department_Code2 = Department_Code2;
    }

    /**
     * 専門科コード3 (例:  )
     *
     * @return the Department_Code3
     */
    public String getDepartment_Code3() {
        return Department_Code3;
    }

    /**
     * 専門科コード3 (例:  )
     *
     * @param Department_Code3 the Department_Code3 to set
     */
    public void setDepartment_Code3(String Department_Code3) {
        this.Department_Code3 = Department_Code3;
    }

    /**
     * 専門科コード4 (例:  )
     *
     * @return the Department_Code4
     */
    public String getDepartment_Code4() {
        return Department_Code4;
    }

    /**
     * 専門科コード4 (例:  )
     *
     * @param Department_Code4 the Department_Code4 to set
     */
    public void setDepartment_Code4(String Department_Code4) {
        this.Department_Code4 = Department_Code4;
    }

    /**
     * 専門科コード5 (例:  )
     *
     * @return the Department_Code5
     */
    public String getDepartment_Code5() {
        return Department_Code5;
    }

    /**
     * 専門科コード5 (例:  )
     *
     * @param Department_Code5 the Department_Code5 to set
     */
    public void setDepartment_Code5(String Department_Code5) {
        this.Department_Code5 = Department_Code5;
    }
}
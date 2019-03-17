package open.dolphin.orca.orcaapi.bean;

/**
 * Department_Information. 診療科情報(繰り返し100)
 *
 * @author pns
 */
public class DepartmentInformation {
    /**
     * 診療科コード※１(01:内科) (例: 01)
     */
    private String Code;

    /**
     * 診療科名称 (例: 内科)
     */
    private String WholeName;

    /**
     * 短縮名称1 (例: 内科)
     */
    private String Name1;

    /**
     * 短縮名称2 (例: 内科)
     */
    private String Name2;

    /**
     * 短縮名称3 (例: 内)
     */
    private String Name3;

    /**
     * レセ電診療科 (例: 01)
     */
    private String Receipt_Code;

    /**
     * 診療科コード※１(01:内科) (例: 01)
     *
     * @return the Code
     */
    public String getCode() {
        return Code;
    }

    /**
     * 診療科コード※１(01:内科) (例: 01)
     *
     * @param Code the Code to set
     */
    public void setCode(String Code) {
        this.Code = Code;
    }

    /**
     * 診療科名称 (例: 内科)
     *
     * @return the WholeName
     */
    public String getWholeName() {
        return WholeName;
    }

    /**
     * 診療科名称 (例: 内科)
     *
     * @param WholeName the WholeName to set
     */
    public void setWholeName(String WholeName) {
        this.WholeName = WholeName;
    }

    /**
     * 短縮名称1 (例: 内科)
     *
     * @return the Name1
     */
    public String getName1() {
        return Name1;
    }

    /**
     * 短縮名称1 (例: 内科)
     *
     * @param Name1 the Name1 to set
     */
    public void setName1(String Name1) {
        this.Name1 = Name1;
    }

    /**
     * 短縮名称2 (例: 内科)
     *
     * @return the Name2
     */
    public String getName2() {
        return Name2;
    }

    /**
     * 短縮名称2 (例: 内科)
     *
     * @param Name2 the Name2 to set
     */
    public void setName2(String Name2) {
        this.Name2 = Name2;
    }

    /**
     * 短縮名称3 (例: 内)
     *
     * @return the Name3
     */
    public String getName3() {
        return Name3;
    }

    /**
     * 短縮名称3 (例: 内)
     *
     * @param Name3 the Name3 to set
     */
    public void setName3(String Name3) {
        this.Name3 = Name3;
    }

    /**
     * レセ電診療科 (例: 01)
     *
     * @return the Receipt_Code
     */
    public String getReceipt_Code() {
        return Receipt_Code;
    }

    /**
     * レセ電診療科 (例: 01)
     *
     * @param Receipt_Code the Receipt_Code to set
     */
    public void setReceipt_Code(String Receipt_Code) {
        this.Receipt_Code = Receipt_Code;
    }
}
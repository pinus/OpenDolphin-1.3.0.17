package open.dolphin.orca.orcaapi.bean;

/**
 * Home2_Information. 帰省先情報
 *
 * @author pns
 */
public class Home2Information {
    /**
     * 帰省先名称 (例: 実家)
     */
    private String WholeName;

    /**
     * 郵便番号 (例: 6900051)
     */
    private String Address_ZipCode;

    /**
     * 住所 (例: 島根県松江市横浜町)
     */
    private String WholeAddress1;

    /**
     * 番地番号 (例: １１５５)
     */
    private String WholeAddress2;

    /**
     * 電話番号 (例: 0852-22-2222)
     */
    private String PhoneNumber;

    /**
     * 帰省先名称 (例: 実家)
     *
     * @return the WholeName
     */
    public String getWholeName() {
        return WholeName;
    }

    /**
     * 帰省先名称 (例: 実家)
     *
     * @param WholeName the WholeName to set
     */
    public void setWholeName(String WholeName) {
        this.WholeName = WholeName;
    }

    /**
     * 郵便番号 (例: 6900051)
     *
     * @return the Address_ZipCode
     */
    public String getAddress_ZipCode() {
        return Address_ZipCode;
    }

    /**
     * 郵便番号 (例: 6900051)
     *
     * @param Address_ZipCode the Address_ZipCode to set
     */
    public void setAddress_ZipCode(String Address_ZipCode) {
        this.Address_ZipCode = Address_ZipCode;
    }

    /**
     * 住所 (例: 島根県松江市横浜町)
     *
     * @return the WholeAddress1
     */
    public String getWholeAddress1() {
        return WholeAddress1;
    }

    /**
     * 住所 (例: 島根県松江市横浜町)
     *
     * @param WholeAddress1 the WholeAddress1 to set
     */
    public void setWholeAddress1(String WholeAddress1) {
        this.WholeAddress1 = WholeAddress1;
    }

    /**
     * 番地番号 (例: １１５５)
     *
     * @return the WholeAddress2
     */
    public String getWholeAddress2() {
        return WholeAddress2;
    }

    /**
     * 番地番号 (例: １１５５)
     *
     * @param WholeAddress2 the WholeAddress2 to set
     */
    public void setWholeAddress2(String WholeAddress2) {
        this.WholeAddress2 = WholeAddress2;
    }

    /**
     * 電話番号 (例: 0852-22-2222)
     *
     * @return the PhoneNumber
     */
    public String getPhoneNumber() {
        return PhoneNumber;
    }

    /**
     * 電話番号 (例: 0852-22-2222)
     *
     * @param PhoneNumber the PhoneNumber to set
     */
    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }
}

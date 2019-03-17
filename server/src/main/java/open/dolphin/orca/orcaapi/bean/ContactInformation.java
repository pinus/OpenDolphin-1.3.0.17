package open.dolphin.orca.orcaapi.bean;

/**
 * Contact_Information. 連絡先情報
 *
 * @author pns
 */
public class ContactInformation {
    /**
     * 連絡先名称 (例: 日医　太郎)
     */
    private String WholeName;

    /**
     * 続柄 (例: 本人)
     */
    private String Relationship;

    /**
     * 郵便番号 (例: 1130021)
     */
    private String Address_ZipCode;

    /**
     * 住所 (例: 東京都文京区本駒込)
     */
    private String WholeAddress1;

    /**
     * 番地番号 (例: ６−１６−３)
     */
    private String WholeAddress2;

    /**
     * 電話番号（昼） (例: 03-3333-2222)
     */
    private String PhoneNumber1;

    /**
     * 電話番号（夜） (例: 03-3333-1133)
     */
    private String PhoneNumber2;

    /**
     * 連絡先名称 (例: 日医　太郎)
     *
     * @return the WholeName
     */
    public String getWholeName() {
        return WholeName;
    }

    /**
     * 連絡先名称 (例: 日医　太郎)
     *
     * @param WholeName the WholeName to set
     */
    public void setWholeName(String WholeName) {
        this.WholeName = WholeName;
    }

    /**
     * 続柄 (例: 本人)
     *
     * @return the Relationship
     */
    public String getRelationship() {
        return Relationship;
    }

    /**
     * 続柄 (例: 本人)
     *
     * @param Relationship the Relationship to set
     */
    public void setRelationship(String Relationship) {
        this.Relationship = Relationship;
    }

    /**
     * 郵便番号 (例: 1130021)
     *
     * @return the Address_ZipCode
     */
    public String getAddress_ZipCode() {
        return Address_ZipCode;
    }

    /**
     * 郵便番号 (例: 1130021)
     *
     * @param Address_ZipCode the Address_ZipCode to set
     */
    public void setAddress_ZipCode(String Address_ZipCode) {
        this.Address_ZipCode = Address_ZipCode;
    }

    /**
     * 住所 (例: 東京都文京区本駒込)
     *
     * @return the WholeAddress1
     */
    public String getWholeAddress1() {
        return WholeAddress1;
    }

    /**
     * 住所 (例: 東京都文京区本駒込)
     *
     * @param WholeAddress1 the WholeAddress1 to set
     */
    public void setWholeAddress1(String WholeAddress1) {
        this.WholeAddress1 = WholeAddress1;
    }

    /**
     * 番地番号 (例: ６−１６−３)
     *
     * @return the WholeAddress2
     */
    public String getWholeAddress2() {
        return WholeAddress2;
    }

    /**
     * 番地番号 (例: ６−１６−３)
     *
     * @param WholeAddress2 the WholeAddress2 to set
     */
    public void setWholeAddress2(String WholeAddress2) {
        this.WholeAddress2 = WholeAddress2;
    }

    /**
     * 電話番号（昼） (例: 03-3333-2222)
     *
     * @return the PhoneNumber1
     */
    public String getPhoneNumber1() {
        return PhoneNumber1;
    }

    /**
     * 電話番号（昼） (例: 03-3333-2222)
     *
     * @param PhoneNumber1 the PhoneNumber1 to set
     */
    public void setPhoneNumber1(String PhoneNumber1) {
        this.PhoneNumber1 = PhoneNumber1;
    }

    /**
     * 電話番号（夜） (例: 03-3333-1133)
     *
     * @return the PhoneNumber2
     */
    public String getPhoneNumber2() {
        return PhoneNumber2;
    }

    /**
     * 電話番号（夜） (例: 03-3333-1133)
     *
     * @param PhoneNumber2 the PhoneNumber2 to set
     */
    public void setPhoneNumber2(String PhoneNumber2) {
        this.PhoneNumber2 = PhoneNumber2;
    }
}
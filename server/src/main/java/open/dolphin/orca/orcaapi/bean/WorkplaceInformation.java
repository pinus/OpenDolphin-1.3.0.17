package open.dolphin.orca.orcaapi.bean;

/**
 * WorkPlace_Information. 勤務先情報
 * @author pns
 */
public class WorkplaceInformation {
    /**
     * 勤務先名 (例: てすと　株式会社)
     */
    private String WholeName;

    /**
     * 郵便番号 (例: 1130022)
     */
    private String Address_ZipCode;

    /**
     * 住所 (例: 東京都文京区本駒込)
     */
    private String WholeAddress1;

    /**
     * 番地番号 (例: ５−１２−１１)
     */
    private String WholeAddress2;

    /**
     * 電話番号 (例: 03-3333-2211)
     */
    private String PhoneNumber;

    /**
     * 勤務先名 (例: てすと　株式会社)
     * @return the WholeName
     */
    public String getWholeName() {
        return WholeName;
    }

    /**
     * 勤務先名 (例: てすと　株式会社)
     * @param WholeName the WholeName to set
     */
    public void setWholeName(String WholeName) {
        this.WholeName = WholeName;
    }

    /**
     * 郵便番号 (例: 1130022)
     * @return the Address_ZipCode
     */
    public String getAddress_ZipCode() {
        return Address_ZipCode;
    }

    /**
     * 郵便番号 (例: 1130022)
     * @param Address_ZipCode the Address_ZipCode to set
     */
    public void setAddress_ZipCode(String Address_ZipCode) {
        this.Address_ZipCode = Address_ZipCode;
    }

    /**
     * 住所 (例: 東京都文京区本駒込)
     * @return the WholeAddress1
     */
    public String getWholeAddress1() {
        return WholeAddress1;
    }

    /**
     * 住所 (例: 東京都文京区本駒込)
     * @param WholeAddress1 the WholeAddress1 to set
     */
    public void setWholeAddress1(String WholeAddress1) {
        this.WholeAddress1 = WholeAddress1;
    }

    /**
     * 番地番号 (例: ５−１２−１１)
     * @return the WholeAddress2
     */
    public String getWholeAddress2() {
        return WholeAddress2;
    }

    /**
     * 番地番号 (例: ５−１２−１１)
     * @param WholeAddress2 the WholeAddress2 to set
     */
    public void setWholeAddress2(String WholeAddress2) {
        this.WholeAddress2 = WholeAddress2;
    }

    /**
     * 電話番号 (例: 03-3333-2211)
     * @return the PhoneNumber
     */
    public String getPhoneNumber() {
        return PhoneNumber;
    }

    /**
     * 電話番号 (例: 03-3333-2211)
     * @param PhoneNumber the PhoneNumber to set
     */
    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }
}
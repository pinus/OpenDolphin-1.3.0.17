package open.dolphin.orca.orcaapi.bean;

/**
 * Address_Information. 連絡先・広告情報
 * @author pns
 */
public class AddressInformation {
    /**
     * 所在地 (例: 東京都文京区本駒込２−２８−１０)
     */
    private String WholeAddress;

    /**
     * 郵便番号 (例: 1130021)
     */
    private String Address_ZipCode;

    /**
     * 電話番号 (例: 03-3333-0001)
     */
    private String PhoneNumber;

    /**
     * FAX番号 (例: 03-3333-0002)
     */
    private String FaxNumber;

    /**
     * eメールアドレス (例: test@orca.ne.jp)
     */
    private String E_mail_Address;

    /**
     * ホームページアドレス (例: http://www.orca.med.or.jp/)
     */
    private String Homepage_Address;

    /**
     * 所在地 (例: 東京都文京区本駒込２−２８−１０)
     * @return the WholeAddress
     */
    public String getWholeAddress() {
        return WholeAddress;
    }

    /**
     * 所在地 (例: 東京都文京区本駒込２−２８−１０)
     * @param WholeAddress the WholeAddress to set
     */
    public void setWholeAddress(String WholeAddress) {
        this.WholeAddress = WholeAddress;
    }

    /**
     * 郵便番号 (例: 1130021)
     * @return the Address_ZipCode
     */
    public String getAddress_ZipCode() {
        return Address_ZipCode;
    }

    /**
     * 郵便番号 (例: 1130021)
     * @param Address_ZipCode the Address_ZipCode to set
     */
    public void setAddress_ZipCode(String Address_ZipCode) {
        this.Address_ZipCode = Address_ZipCode;
    }

    /**
     * 電話番号 (例: 03-3333-0001)
     * @return the PhoneNumber
     */
    public String getPhoneNumber() {
        return PhoneNumber;
    }

    /**
     * 電話番号 (例: 03-3333-0001)
     * @param PhoneNumber the PhoneNumber to set
     */
    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    /**
     * FAX番号 (例: 03-3333-0002)
     * @return the FaxNumber
     */
    public String getFaxNumber() {
        return FaxNumber;
    }

    /**
     * FAX番号 (例: 03-3333-0002)
     * @param FaxNumber the FaxNumber to set
     */
    public void setFaxNumber(String FaxNumber) {
        this.FaxNumber = FaxNumber;
    }

    /**
     * eメールアドレス (例: test@orca.ne.jp)
     * @return the E_mail_Address
     */
    public String getE_mail_Address() {
        return E_mail_Address;
    }

    /**
     * eメールアドレス (例: test@orca.ne.jp)
     * @param E_mail_Address the E_mail_Address to set
     */
    public void setE_mail_Address(String E_mail_Address) {
        this.E_mail_Address = E_mail_Address;
    }

    /**
     * ホームページアドレス (例: http://www.orca.med.or.jp/)
     * @return the Homepage_Address
     */
    public String getHomepage_Address() {
        return Homepage_Address;
    }

    /**
     * ホームページアドレス (例: http://www.orca.med.or.jp/)
     * @param Homepage_Address the Homepage_Address to set
     */
    public void setHomepage_Address(String Homepage_Address) {
        this.Homepage_Address = Homepage_Address;
    }
}
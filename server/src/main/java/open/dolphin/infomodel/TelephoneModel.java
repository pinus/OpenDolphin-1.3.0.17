package open.dolphin.infomodel;

/**
 * TelephoneModel.
 *
 * @author Minagawa,Kazushi
 */
public class TelephoneModel  extends InfoModel {
    private static final long serialVersionUID = -3520256828672499135L;

    private String telephoneType;

    private String telephoneTypeDesc;

    private String telephoneTypeCodeSys;

    private String country;

    private String area;

    private String city;

    private String number;

    private String extension;

    private String memo;

    public void setTelephoneType(String telephoneClass) {
        this.telephoneType = telephoneClass;
    }

    public String getTelephoneType() {
        return telephoneType;
    }

    public void setTelephoneTypeDesc(String telephoneClassDesc) {
        this.telephoneTypeDesc = telephoneClassDesc;
    }

    public String getTelephoneTypeDesc() {
        return telephoneTypeDesc;
    }

    public void setTelephoneTypeCodeSys(String telephoneClassCodeSys) {
        this.telephoneTypeCodeSys = telephoneClassCodeSys;
    }

    public String getTelephoneTypeCodeSys() {
        return telephoneTypeCodeSys;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return memo;
    }
}

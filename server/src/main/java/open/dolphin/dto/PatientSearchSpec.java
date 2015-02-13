package open.dolphin.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * PatientSearchSpec
 *
 * @author Minagawa,Kazushi
 */
public class PatientSearchSpec implements Serializable {
    private static final long serialVersionUID = -3192512318678902328L;

    public static final int ALL_SEARCH        = 0;
    public static final int ID_SEARCH         = 1;
    public static final int NAME_SEARCH       = 2;
    public static final int KANA_SEARCH       = 3;
    public static final int ROMAN_SEARCH      = 4;
    public static final int TELEPHONE_SEARCH  = 5;
    public static final int ZIPCODE_SEARCH    = 6;
    public static final int ADDRESS_SEARCH    = 7;
    public static final int EMAIL_SEARCH      = 8;
    public static final int OTHERID_SEARCH    = 9;
    //public static final int DIGIT_SEARCH      = 10;
    public static final int DATE_SEARCH       = 11;
    public static final int BIRTHDAY_SEARCH   = 12;
    public static final int MEMO_SEARCH       = 13;
    public static final int FULL_TEXT_SEARCH  = 14;

    private int code;
    private String patientId;
    private String name;
    private String telephone;
    private String zipCode;
    private String address;
    private String email;
    private String otherId;
    private String otherIdClass;
    private String otherIdCodeSys;
    private String date;
    private String birthday;
    private String searchText;
    private final List<Long> narrowingIds = new ArrayList<>();

    public void setNarrowingIds(List<Long> list) {
        narrowingIds.clear();
        narrowingIds.addAll(list);
    }

    public List<Long> getNarrowingList() {
        return narrowingIds;
    }

    public void setSearchText(String text) {
        this.searchText = text;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setPatientId(String patientId) {
        // ID が６桁未満の数字の時は，先頭にゼロを補って６桁にする。ただし頭が 0 の時はそのまま通す
        if (patientId != null && patientId.charAt(0) != '0') {
            this.patientId = String.format("%6s", patientId).replace(' ', '0');
        } else {
            this.patientId = patientId;
        }
    }

    public String getPatientId() {
        return patientId;
    }

    public void setName(String name) {
        // 名前に全角スペースが入っていた場合，半角に変換してセットする
        if (name != null) {
            this.name = name.replace('　', ' ');
        } else {
            this.name = name;
        }
    }

    public String getName() {
        return name;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherIdClass(String otherIdClass) {
        this.otherIdClass = otherIdClass;
    }

    public String getOtherIdClass() {
        return otherIdClass;
    }

    public void setOtherIdCodeSys(String otherIdCodeSys) {
        this.otherIdCodeSys = otherIdCodeSys;
    }

    public String getOtherIdCodeSys() {
        return otherIdCodeSys;
    }

    public void setDate(String date) {
            this.date = date;
    }

    public String getDate() {
        return date;
    }
}

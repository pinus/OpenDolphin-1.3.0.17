package open.dolphin.infomodel;

/**
 * PatientLiteModel.
 *
 * @author Minagawa, kazushi
 */
public class PatientLiteModel extends InfoModel {
    private static final long serialVersionUID = 2257606235838636648L;

    private String patientId;
    private String name;    // full name
    private String gender;
    private String genderDesc;
    private String genderCodeSys;
    private String birthday;    // yyyy-mm-dd

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderDesc() {
        return genderDesc;
    }

    public void setGenderDesc(String genderDesc) {
        this.genderDesc = genderDesc;
    }

    public String getGenderCodeSys() {
        return genderCodeSys;
    }

    public void setGenderCodeSys(String genderCodeSys) {
        this.genderCodeSys = genderCodeSys;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}

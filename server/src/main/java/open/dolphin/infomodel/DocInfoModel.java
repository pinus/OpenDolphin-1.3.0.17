package open.dolphin.infomodel;

import open.dolphin.util.ModelUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * DocInfoModel.
 * DocumentModel に Embed される
 *
 * @author Minagawa, kazushi
 */
@Embeddable
public class DocInfoModel extends InfoModel implements Comparable<DocInfoModel> {
    
    @Transient
    private long docPk;

    @Transient
    private long parentPk;  // 親文書の primary key

    @Column(nullable = false, length = 32)
    private String docId;   // 文書 ID

    @Column(nullable = false)
    private String docType; // 文書タイプ

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String purpose; // = recode

    @Transient
    private String purposeDesc;

    @Transient
    private String purposeCodeSys;

    @Transient
    private Date firstConfirmDate;

    @Transient
    private Date confirmDate;

    private String department;

    private String departmentDesc;

    @Transient
    private String departmentCodeSys;

    private String healthInsurance;

    private String healthInsuranceDesc;

    @Transient
    private String healthInsuranceCodeSys;

    private String healthInsuranceGUID;

    private boolean hasMark;

    private boolean hasImage;

    private boolean hasRp;

    private boolean hasTreatment;

    private boolean hasLaboTest;

    private String versionNumber;

    @Transient
    private String versionNotes;

    private String parentId;

    private String parentIdRelation;

    @Transient
    private String parentIdDesc;

    @Transient
    private String parentIdCodeSys;

    @Transient
    private Collection<AccessRightModel> accessRights;

    @Transient
    private String status;

    public long getDocPk() {
        return docPk;
    }

    public void setDocPk(long docPk) {
        this.docPk = docPk;
    }

    public long getParentPk() {
        return parentPk;
    }

    public void setParentPk(long parentPk) {
        this.parentPk = parentPk;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPurposeDesc() {
        return purposeDesc;
    }

    public void setPurposeDesc(String purposeDesc) {
        this.purposeDesc = purposeDesc;
    }

    public String getPurposeCodeSys() {
        return purposeCodeSys;
    }

    public void setPurposeCodeSys(String purposeCodeSys) {
        this.purposeCodeSys = purposeCodeSys;
    }

    public Date getFirstConfirmDate() {
        return firstConfirmDate;
    }

    public void setFirstConfirmDate(Date firstConfirmDate) {
        this.firstConfirmDate = firstConfirmDate;
    }

    public String getFirstConfirmDateTrimTime() {
        return ModelUtils.getDateAsString(getFirstConfirmDate());
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmDateTrimTime() {
        return ModelUtils.getDateAsString(getConfirmDate());
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }

    public String getDepartmentName() {
        String[] tokens = tokenizeDept(departmentDesc);
        return tokens[0];
    }

    public String getDepartmentCode() {
        String[] tokens = tokenizeDept(departmentDesc);
        if (tokens[1] != null) {
            return tokens[1];
        }
        return department;
    }

    public String getAssignedDoctorName() {
        String[] tokens = tokenizeDept(departmentDesc);
        return tokens[2];
    }

    public String getAssignedDoctorId() {
        String[] tokens = tokenizeDept(departmentDesc);
        return tokens[3];
    }

    public String getJMARICode() {
        String[] tokens = tokenizeDept(departmentDesc);
        return tokens[4];
    }

    private String[] tokenizeDept(String dept) {

        // 診療科名、コード、担当医名、担当医コード、JMARI コード
        // を格納する配列を生成する
        String[] ret = new String[5];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = null;
        }

        if (dept != null) {
            int index = 0;
            StringTokenizer st = new StringTokenizer(dept, ",");
            while (st.hasMoreTokens()) {
                ret[index++] = st.nextToken();
            }
        }

        return ret;
    }

    public String getDepartmentCodeSys() {
        return departmentCodeSys;
    }

    public void setDepartmentCodeSys(String departmentCodeSys) {
        this.departmentCodeSys = departmentCodeSys;
    }

    public String getHealthInsurance() {
        return healthInsurance;
    }

    public void setHealthInsurance(String healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public String getHealthInsuranceDesc() {
        return healthInsuranceDesc;
    }

    public void setHealthInsuranceDesc(String healthInsuranceDesc) {
        this.healthInsuranceDesc = healthInsuranceDesc;
    }

    public String getHealthInsuranceCodeSys() {
        return healthInsuranceCodeSys;
    }

    public void setHealthInsuranceCodeSys(String healthInsuranceCodeSys) {
        this.healthInsuranceCodeSys = healthInsuranceCodeSys;
    }

    public String getHealthInsuranceGUID() {
        return healthInsuranceGUID;
    }

    public void setHealthInsuranceGUID(String healthInsuranceGUID) {
        this.healthInsuranceGUID = healthInsuranceGUID;
    }

    public boolean isHasMark() {
        return hasMark;
    }

    public void setHasMark(boolean hasMark) {
        this.hasMark = hasMark;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public boolean isHasRp() {
        return hasRp;
    }

    public void setHasRp(boolean hasRp) {
        this.hasRp = hasRp;
    }

    public boolean isHasTreatment() {
        return hasTreatment;
    }

    public void setHasTreatment(boolean hasTreatment) {
        this.hasTreatment = hasTreatment;
    }

    public boolean isHasLaboTest() {
        return hasLaboTest;
    }

    public void setHasLaboTest(boolean hasLaboTest) {
        this.hasLaboTest = hasLaboTest;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String version) {
        this.versionNumber = version;
    }

    public String getVersionNotes() {
        return versionNotes;
    }

    public void setVersionNotes(String versionNotes) {
        this.versionNotes = versionNotes;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIdRelation() {
        return parentIdRelation;
    }

    public void setParentIdRelation(String parentIdRelation) {
        this.parentIdRelation = parentIdRelation;
    }

    public String getParentIdDesc() {
        return parentIdDesc;
    }

    public void setParentIdDesc(String relationDesc) {
        this.parentIdDesc = relationDesc;
    }

    public String getParentIdCodeSys() {
        return parentIdCodeSys;
    }

    public void setParentIdCodeSys(String relationCodeSys) {
        this.parentIdCodeSys = relationCodeSys;
    }

    public Collection<AccessRightModel> getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(Collection<AccessRightModel> accessRights) {
        this.accessRights = accessRights;
    }

    public void addAccessRight(AccessRightModel accessRight) {
        if (accessRights == null) {
            accessRights = new ArrayList<>(3);
        }
        accessRights.add(accessRight);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return ((docId == null) ? "".hashCode() : docId.hashCode()) + 11;
    }

    @Override
    public boolean equals(Object other) {
        return other != null && getClass() == other.getClass() && getDocId().equals(((DocInfoModel) other).getDocId());
    }

    @Override
    public int compareTo(DocInfoModel other) {
        if (other != null) {
            Date val1 = getFirstConfirmDate();
            Date val2 = other.getFirstConfirmDate();
            int result = val1.compareTo(val2);
            if (result == 0) {
                val1 = getConfirmDate();
                val2 = other.getConfirmDate();
                result = val1.compareTo(val2);
            }
            return result;
        }
        return -1;
    }
}

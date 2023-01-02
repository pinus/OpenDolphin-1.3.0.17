package open.dolphin.infomodel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

/**
 * 外部参照要素クラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Embeddable
public class ExtRefModel extends InfoModel {
    
    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private String medicalRole;

    @Transient
    private String medicalRoleTableId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String href;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String value) {
        contentType = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        title = value;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String value) {
        href = value;
    }

    public String getMedicalRole() {
        return medicalRole;
    }

    public void setMedicalRole(String medicalRole) {
        this.medicalRole = medicalRole;
    }

    public String getMedicalRoleTableId() {
        return medicalRoleTableId;
    }

    public void setMedicalRoleTableId(String medicalRoleTableId) {
        this.medicalRoleTableId = medicalRoleTableId;
    }
}

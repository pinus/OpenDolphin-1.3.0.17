package open.dolphin.dto;

import java.awt.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ImageSearchSpec.
 *
 * @author Minagawa, Kazushi
 */
public class ImageSearchSpec implements Serializable {
    public static final int ID_SEARCH = 0;
    public static final int PATIENT_SEARCH = 1;
    private static final long serialVersionUID = 1306931621795428447L;
    private int code;
    private long karteId;
    private long id;
    private String patientId;
    private String medicalRole;
    private Date[] fromDate;
    private Date[] toDate;
    private Dimension iconSize;
    private String status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getKarteId() {
        return karteId;
    }

    public void setKarteId(long karteId) {
        this.karteId = karteId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getMedicalRole() {
        return medicalRole;
    }

    public void setMedicalRole(String medicalRole) {
        this.medicalRole = medicalRole;
    }

    public Date[] getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date[] fromDate) {
        this.fromDate = fromDate;
    }

    public Date[] getToDate() {
        return toDate;
    }

    public void setToDate(Date[] toDate) {
        this.toDate = toDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Dimension getIconSize() {
        return iconSize;
    }

    public void setIconSize(Dimension iconSize) {
        this.iconSize = iconSize;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

package open.dolphin.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DiagnosisSearchSpec.
 *
 * @author Minagawa,Kazushi
 */
public class DiagnosisSearchSpec implements Serializable {
    private static final long serialVersionUID = 3687480184889000203L;

    public static final int PATIENT_SEARCH         = 0;
    public static final int CODE_SEARCH            = 1;
    public static final int DIAGNOSIS_SEARCH       = 2;
    public static final int CREATOR_SEARCH         = 3;

    private int code;
    private long karteId;
    private String patientId;
    private String diagnosisCode;
    private String diagnosis;
    private String creatorId;
    private Date fromDate;
    private Date toDate;
    private char status;


    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public long getKarteId() {
        return karteId;
    }

    public void setKarteId(long karteId) {
        this.karteId = karteId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public char getStatus() {
        return status;
    }
}

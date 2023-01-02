package open.dolphin.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * ModuleSearchSpec.
 *
 * @author Minagawa, Kazushi
 */
public class ModuleSearchSpec implements Serializable {
    public static final int ENTITY_SEARCH = 0;
        private int code;
    private long karteId;
    private String patientId;
    private String entity;
    private Date[] fromDate;
    private Date[] toDate;
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

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}

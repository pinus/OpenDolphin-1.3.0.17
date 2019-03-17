package open.dolphin.dto;

import java.io.Serializable;

/**
 * LaboSearchSpec.
 *
 * @author Minagawa, Kazushi
 */
public class LaboSearchSpec implements Serializable {
    private static final long serialVersionUID = 2201738793947138141L;

    private long karteId;
    private String fromDate;
    private String toDate;

    public long getKarteId() {
        return karteId;
    }

    public void setKarteId(long patientId) {
        this.karteId = patientId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}

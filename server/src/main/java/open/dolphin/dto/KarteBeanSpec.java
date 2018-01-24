package open.dolphin.dto;

import java.util.Date;

/**
 * KarteBeanSpec.
 *
 * @author pns
 */
public class KarteBeanSpec {
    // PatientModel primary key
    private long patientPk;
    private Date fromDate;

    public long getPatientPk() {
        return patientPk;
}

    public void setPatientPk(long patientPk) {
        this.patientPk = patientPk;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
}

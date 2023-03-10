package open.dolphin.dto;

import open.dolphin.infomodel.PatientVisitModel;

import java.io.Serializable;

/**
 * PatientVisitSpec.
 *
 * @author Minagawa, Kazushi
 */
public class PatientVisitSpec implements Serializable {
    
    private PatientVisitModel patientVisit;
    private String date;
    private int skipCount;
    private String patientId;
    private String appodateFrom;
    private String appodateTo;

    public String getAppodateFrom() {
        return appodateFrom;
    }

    public void setAppodateFrom(String appodateFrom) {
        this.appodateFrom = appodateFrom;
    }

    public String getAppodateTo() {
        return appodateTo;
    }

    public void setAppodateTo(String appodateTo) {
        this.appodateTo = appodateTo;
    }

    public PatientVisitModel getPatientVisit() {
        return patientVisit;
    }

    public void setPatientVisit(PatientVisitModel patientVisitValue) {
        this.patientVisit = patientVisitValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}

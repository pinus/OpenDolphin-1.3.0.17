package open.dolphin.client;

import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.inspector.DocumentHistory;
import open.dolphin.ui.PNSFrame;
import open.dolphin.ui.StatusPanel;

public interface Chart extends MainTool {
    KarteBean getKarte();
    void setKarte(KarteBean karte);
    PatientModel getPatient();
    PatientVisitModel getPatientVisit();
    void setPatientVisit(PatientVisitModel model);
    int getChartState();
    void setChartState(int state);
    boolean isReadOnly();
    void setReadOnly(boolean b);
    void close();
    PNSFrame getFrame();
    StatusPanel getStatusPanel();
    void setStatusPanel(StatusPanel statusPanel);
    ChartMediator getChartMediator();
    void enabledAction(String name, boolean enabled);
    DocumentHistory getDocumentHistory();
    void showDocument(int index);
    boolean isDirty();
    PVTHealthInsuranceModel[] getHealthInsurances();
    PVTHealthInsuranceModel getHealthInsuranceToApply(String uuid);

    enum NewKarteOption {BROWSER_NEW, BROWSER_COPY_NEW, BROWSER_MODIFY, EDITOR_NEW, EDITOR_COPY_NEW, EDITOR_MODIFY}
    enum NewKarteMode {EMPTY_NEW, APPLY_RP, ALL_COPY}
}

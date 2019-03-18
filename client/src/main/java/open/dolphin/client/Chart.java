package open.dolphin.client;

import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.inspector.DocumentHistory;
import open.dolphin.ui.MainFrame;
import open.dolphin.ui.StatusPanel;

public interface Chart extends MainTool {

    public KarteBean getKarte();

    public void setKarte(KarteBean karte);

    public PatientModel getPatient();

    public PatientVisitModel getPatientVisit();

    public void setPatientVisit(PatientVisitModel model);

    public int getChartState();

    public void setChartState(int state);

    public boolean isReadOnly();

    public void setReadOnly(boolean b);

    public void close();

    public MainFrame getFrame();

    public StatusPanel getStatusPanel();

    public void setStatusPanel(StatusPanel statusPanel);

    public ChartMediator getChartMediator();

    public void enabledAction(String name, boolean enabled);

    public DocumentHistory getDocumentHistory();

    public void showDocument(int index);

    public boolean isDirty();

    public PVTHealthInsuranceModel[] getHealthInsurances();

    public PVTHealthInsuranceModel getHealthInsuranceToApply(String uuid);

    public enum NewKarteOption {BROWSER_NEW, BROWSER_COPY_NEW, BROWSER_MODIFY, EDITOR_NEW, EDITOR_COPY_NEW, EDITOR_MODIFY}

    public enum NewKarteMode {EMPTY_NEW, APPLY_RP, ALL_COPY}

}

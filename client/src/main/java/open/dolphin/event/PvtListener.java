package open.dolphin.event;

import java.util.EventListener;
import open.dolphin.infomodel.PatientVisitModel;

/**
 *
 * @author pns
 */
public interface PvtListener extends EventListener {

    public void pvtChanged(PatientVisitModel model);
}

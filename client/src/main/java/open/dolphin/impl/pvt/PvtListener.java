package open.dolphin.impl.pvt;

import java.util.EventListener;
import open.dolphin.infomodel.PatientVisitModel;

/**
 *
 * @author pns
 */
public interface PvtListener extends EventListener {

    public void pvtChanged(PatientVisitModel model);
}

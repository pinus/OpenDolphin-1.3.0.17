package open.dolphin.impl.pvt;

import open.dolphin.infomodel.PatientVisitModel;

import java.util.EventListener;

/**
 *
 * @author pns
 */
public interface PvtListener extends EventListener {

    public void pvtChanged(PatientVisitModel model);
}

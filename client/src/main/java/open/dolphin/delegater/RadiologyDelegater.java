package open.dolphin.delegater;

import java.util.List;
import open.dolphin.infomodel.RadiologyMethodValue;
import open.dolphin.service.RadiologyService;

/**
 *
 * @author pns
 */
public final class RadiologyDelegater extends BusinessDelegater {

    /**
     * select ... from radiology_method where hierarchyCode1 >= 0 order by hierarchyCode1
     * valuObject: RadiologyMethodEntry
     * @return
     */
    public List<RadiologyMethodValue> getRadiologyMethod() {
        return getService().getRadiologyMethod("0");
    }

    /**
     * select ... from radiology_method where hierarchyCode2 like h2% order by hierarchyCode2
     * valuObject: RadiologyMethodEntry
     * @param h2
     * @return
     */
    public List<RadiologyMethodValue> getRadiologyComments(String h2) {
        return getService().getRadiologyComment(h2 + "%");
    }

    private RadiologyService getService() {
            return getService(RadiologyService.class);
    }
}

package open.dolphin.service;

import java.util.List;
import javax.ejb.Stateless;
import open.dolphin.infomodel.RadiologyMethodValue;

/**
 * RadiologyServiceImpl
 *
 * @author pns
 */
@Stateless
public class RadiologyServiceImpl extends DolphinService implements RadiologyService {

    @Override
    public List<RadiologyMethodValue> getRadiologyMethod(String from) {
        return em.createQuery("select r from RadiologyMethodValue r where r.hierarchyCode1 >= :hc1 order by r.hierarchyCode1", RadiologyMethodValue.class)
            .setParameter("hc1", from).getResultList();
    }

    @Override
    public List<RadiologyMethodValue> getRadiologyComment(String hierarchyCode2) {
        return em.createQuery("select r from RadiologyMethodValue r where r.hierarchyCode2 like :hc2 order by r.hierarchyCode2", RadiologyMethodValue.class)
            .setParameter("hc2", hierarchyCode2).getResultList();
    }
}

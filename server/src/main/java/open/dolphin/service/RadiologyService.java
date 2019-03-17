package open.dolphin.service;

import open.dolphin.infomodel.RadiologyMethodValue;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * RadiologyService
 *
 * @author pns
 */
@Path("radiation")
@RolesAllowed("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RadiologyService {

    /**
     * @param from
     * @return 該当マスタのコレクション
     */
    @Path("getRadiologyMethod")
    @POST
    public List<RadiologyMethodValue> getRadiologyMethod(String from);

    /**
     * @param hierarchyCode
     * @return 該当マスタのコレクション
     */
    @Path("getRadiologyComment")
    @POST
    public List<RadiologyMethodValue> getRadiologyComment(String hierarchyCode);
}

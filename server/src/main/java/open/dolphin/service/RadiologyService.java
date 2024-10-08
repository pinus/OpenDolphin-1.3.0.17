package open.dolphin.service;

import open.dolphin.infomodel.RadiologyMethodValue;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
     * @param from date from
     * @return 該当マスタのコレクション
     */
    @Path("getRadiologyMethod")
    @POST
    List<RadiologyMethodValue> getRadiologyMethod(String from);

    /**
     * @param hierarchyCode hierarchy code
     * @return 該当マスタのコレクション
     */
    @Path("getRadiologyComment")
    @POST
    List<RadiologyMethodValue> getRadiologyComment(String hierarchyCode);
}

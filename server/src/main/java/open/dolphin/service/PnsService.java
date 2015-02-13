package open.dolphin.service;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import open.dolphin.infomodel.ModuleModel;

/**
 *
 * @author pns
 */
@Path("pns")
@RolesAllowed("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PnsService {

    @Path("peekKarte")
    @POST
    public List<ModuleModel> peekKarte(Long patientId);

    @Path("makeInitialIndex")
    @POST
    public void makeInitialIndex();
}

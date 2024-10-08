package open.dolphin.service;

import open.dolphin.infomodel.ModuleModel;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author pns
 */
@Path("pns")
@RolesAllowed("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PnsService {

    @Path("peekKarte")
    @POST
    List<ModuleModel> peekKarte(Long patientId);

    @Path("getCalendarData")
    @GET
    String[][] getCalendarData();

    @Path("saveCalendarData")
    @POST
    void saveCalendarData(String[][] data);

    @Path("makeInitialIndex")
    @POST
    void makeInitialIndex();
}

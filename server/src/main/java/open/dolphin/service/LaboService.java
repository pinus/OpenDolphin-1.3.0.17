package open.dolphin.service;

import open.dolphin.dto.LaboSearchSpec;
import open.dolphin.infomodel.LaboModuleValue;
import open.dolphin.infomodel.PatientModel;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author pns
 */
@Path("labo")
@RolesAllowed("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LaboService {

    /**
     * LaboModuleを保存する。
     *
     * @param laboModuleValue LaboModuleValue
     * @return
     */
    @Path("putLaboModule")
    @POST
    public PatientModel putLaboModule(LaboModuleValue laboModuleValue);

    /**
     * 患者の検体検査モジュールを取得する。
     *
     * @param spec LaboSearchSpec 検索仕様
     * @return laboModule の Collection
     */
    @Path("getLaboModules")
    @POST
    public List<LaboModuleValue> getLaboModuleList(LaboSearchSpec spec);
}

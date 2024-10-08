package open.dolphin.service;

import open.dolphin.dto.LaboSearchSpec;
import open.dolphin.infomodel.LaboModuleValue;
import open.dolphin.infomodel.PatientModel;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
     * @return PatientModel
     */
    @Path("putLaboModule")
    @POST
    PatientModel putLaboModule(LaboModuleValue laboModuleValue);

    /**
     * 患者の検体検査モジュールを取得する。
     *
     * @param spec LaboSearchSpec 検索仕様
     * @return laboModule の Collection
     */
    @Path("getLaboModules")
    @POST
    List<LaboModuleValue> getLaboModuleList(LaboSearchSpec spec);
}

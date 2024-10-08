package open.dolphin.service;

import open.dolphin.dto.PatientSearchSpec;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * PatientService
 *
 * @author pns
 */
@Path("patient")
@RolesAllowed("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PatientService {

    /**
     * 患者オブジェクトを取得する.
     *
     * @param spec PatientSearchSpec 検索仕様
     * @return 患者オブジェクトの Collection
     */
    @Path("getPatients")
    @POST
    List<PatientModel> getPatientList(PatientSearchSpec spec);

    /**
     * 健康保険情報を取得する.
     *
     * @param patientPk PatientModel's primary key
     * @return List of PVTHealthInsuranceModel
     */
    @Path("getHealthInsuranceList")
    @POST
    List<PVTHealthInsuranceModel> getHealthInsuranceList(Long patientPk);

    /**
     * 患者ID("000001")を指定して患者オブジェクトを返す.
     *
     * @param patientId 施設内患者ID
     * @return 該当するPatientModel
     */
    @Path("getPatient")
    @POST
    PatientModel getPatient(String patientId);

    /**
     * 患者を登録する.
     *
     * @param patient PatientModel
     * @return データベース Primary Key
     */
    @Path("addPatient")
    @POST
    Long addPatient(PatientModel patient);

    /**
     * 患者情報を更新する.
     *
     * @param patient 更新する「患者オブジェクト
     * @return 更新数
     */
    @Path("update")
    @POST
    int update(PatientModel patient);
}

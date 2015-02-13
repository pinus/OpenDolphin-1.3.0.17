package open.dolphin.service;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import open.dolphin.dto.PatientSearchSpec;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;

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
     * 患者オブジェクトを取得する。
     * @param spec PatientSearchSpec 検索仕様
     * @return 患者オブジェクトの Collection
     */
    @Path("getPatients")
    @POST
    public List<PatientModel> getPatientList(PatientSearchSpec spec);

    /**
     * 健康保険情報を取得する
     * @param patientPk
     * @return
     */
    @Path("getHealthInsuranceList")
    @POST
    public List<PVTHealthInsuranceModel> getHealthInsuranceList(Long patientPk);

    /**
     * 患者ID(BUSINESS KEY)を指定して患者オブジェクトを返す。
     *
     * @param patientId 施設内患者ID
     * @return 該当するPatientModel
     */
    @Path("getPatient")
    @POST
    public PatientModel getPatient(String patientId);

    /**
     * 患者を登録する。
     * @param patient PatientModel
     * @return データベース Primary Key
     */
    @Path("addPatient")
    @POST
    public Long addPatient(PatientModel patient);

    /**
     * 患者情報を更新する。
     * @param patient 更新する「患者オブジェクト
     * @return 更新数
     */
    @Path("update")
    @POST
    public int update(PatientModel patient);
}

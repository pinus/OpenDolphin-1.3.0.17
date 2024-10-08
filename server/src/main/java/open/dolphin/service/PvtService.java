package open.dolphin.service;

import open.dolphin.dto.PatientVisitSpec;
import open.dolphin.dto.PvtStateSpec;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * PvtService
 *
 * @author pns
 */
@Path("pvt")
@RolesAllowed("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PvtService {

    /**
     * 患者来院情報 pvt を保存する.
     *
     * @param pvt PatientVisitModel
     * @return 登録個数 1
     */
    @Path("addPvt")
    @POST
    int addPvt(PatientVisitModel pvt);

    /**
     * 既存の pvt を update する.
     *
     * @param pvt PatientVisitModel
     * @return 登録個数 1
     */
    @Path("updatePvt")
    @POST
    int updatePvt(PatientVisitModel pvt);

    /**
     * 患者来院情報 pvt のリストを取得する.
     *
     * @param spec 検索仕様オブジェクト
     * @return List of PatientVisitModel
     */
    @Path("getPvtList")
    @POST
    List<PatientVisitModel> getPvtList(PatientVisitSpec spec);

    /**
     * 来院情報を削除する.
     *
     * @param id レコード ID
     * @return 削除件数
     */
    @Path("removePvt")
    @POST
    int removePvt(Long id);

    /**
     * 今日の pvt の state だけもってくる.
     *
     * @return List of PvtStateSpec
     */
    @Path("getPvtStateList")
    @POST
    List<PvtStateSpec> getPvtStateList();

    /**
     * 特定の pvt の state だけ持ってくる.
     *
     * @param id pk
     * @return pvt state number
     */
    @Path("getPvtState")
    @POST
    int getPvtState(Long id);

    /**
     * PatientModel patient の今日の pvt をもってくる.
     *
     * @param patient PatientModel
     * @return pvt がなければ null
     */
    @Path("getPvtOfPatient")
    @POST
    List<PatientVisitModel> getPvtOf(PatientModel patient);
}

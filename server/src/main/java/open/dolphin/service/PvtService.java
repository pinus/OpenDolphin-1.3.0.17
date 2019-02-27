package open.dolphin.service;

import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import open.dolphin.dto.PatientVisitSpec;
import open.dolphin.dto.PvtStateSpec;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;

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
     * 患者来院情報を保存する.
     *
     * @param pvt PatientVisitModel
     * @return 登録個数 1
     */
    @Path("addPvt")
    @POST
    public int addPvt(PatientVisitModel pvt);

    /**
     * 内部から呼ぶとき用.
     *
     * @param pvt PatientVisitModel
     * @param facilityId 施設ID
     * @return 登録個数 1
     */
    public int addPvt(PatientVisitModel pvt, String facilityId);

    /**
     * 既存の pvt を update する.
     *
     * @param pvt PatientVisitModel
     * @return 登録個数 1
     */
    @Path("updatePvt")
    @POST
    public int updatePvt(PatientVisitModel pvt);

    /**
     * 施設の患者来院情報を取得する.
     *
     * @param spec 検索仕様オブジェクト
     * @return List of PatientVisitModel
     */
    @Path("getPvtList")
    @POST
    public List<PatientVisitModel> getPvtList(PatientVisitSpec spec);

    /**
     * 来院情報を削除する.
     *
     * @param id レコード ID
     * @return 削除件数
     */
    @Path("removePvt")
    @POST
    public int removePvt(Long id);

    /**
     * 今日の pvt の state だけもってくる.
     *
     * @return List of PvtStateSpec
     */
    @Path("getPvtStateList")
    @POST
    public List<PvtStateSpec> getPvtStateList();

    /**
     * 特定の pvt の state だけ持ってくる.
     *
     * @param id pk
     * @return pvt state number
     */
    @Path("getPvtState")
    @POST
    public int getPvtState(Long id);

    /**
     * PatientModel patient の今日の pvt をもってくる.
     *
     * @param patient PatientModel
     * @return pvt がなければ null
     */
    @Path("getPvtOfPatient")
    @POST
    public PatientVisitModel getPvtOf(PatientModel patient);
}

package open.dolphin.service;

import open.dolphin.dto.*;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.orca.orcadao.bean.OnshiKenshin;
import open.dolphin.orca.orcadao.bean.OnshiYakuzai;
import open.dolphin.orca.orcadao.bean.Syskanri;
import open.dolphin.orca.orcadao.bean.Wksryact;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * OrcaService.
 *
 * @author pns
 */
@Path("orca")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("user")
public interface OrcaService {

    /**
     * 中途終了患者情報.
     *
     * @param spec PatientVisitSpec (patientId と date を使用)
     * @return Wksryact
     */
    @POST
    @Path("getWksryact")
    Wksryact getWksryact(PatientVisitSpec spec);

    /**
     * 中途終了患者情報が存在するかどうか.
     *
     * @param ptId "000001"
     * @return 中途終了情報あり=true
     */
    @POST
    @Path("existsOrcaWorkingData")
    boolean existsOrcaWorkingData(String ptId);

    /**
     * 職員情報.
     *
     * @return Syskanri のリスト
     */
    @POST
    @Path("getSyskanri")
    List<Syskanri> getSyskanri();

    /**
     * TBL_TENSU からキーワードを検索.
     *
     * @param keyword キーワード
     * @return OrcaEntry の List
     */
    @POST
    @Path("findTensu")
    List<OrcaEntry> findTensu(String keyword);

    /**
     * TBL_BYOMEI からキーワードを検索.
     *
     * @param keyword キーワード
     * @return OrcaEntry の List
     */
    @POST
    @Path("findDiagnosisByKeyword")
    List<OrcaEntry> findDiagnosisByKeyword(String keyword);

    /**
     * TBL_BYOMEI から病名コードのリストに対応する病名を検索.
     *
     * @param srycds 病名コードのセット
     * @return OrcaEntry の List
     */
    @POST
    @Path("findDiagnosisByCodes")
    List<OrcaEntry> findDiagnosisByCodes(List<String> srycds);

    /**
     * 移行病名を調べる.
     *
     * @param srycds 病名コードのリスト
     * @return そのうち移行病名になっているののリスト
     */
    @POST
    @Path("findIkouByomei")
    List<String> findIkouByomei(List<String> srycds);

    /**
     * TBL_INPUTCD を検索して入力セット（約束処方、診療セット）のリストを返す.
     *
     * @return ModuleInfoBean
     */
    @POST
    @Path("getOrcaInputCdList")
    List<ModuleInfoBean> getOrcaInputCdList();

    /**
     * StampInfo を元に TBL_INPUTSET，TBL_TENSU を検索してスタンプの実体を作る
     *
     * @param stampInfo ModuleInfoBean
     * @return ModuleModel の List
     */
    @POST
    @Path("getStamp")
    List<ModuleModel> getStamp(ModuleInfoBean stampInfo);

    /**
     * TBL_PTBYOMEI を検索して RegisteredDiagnosisModel を作る
     *
     * @param spec DiagnosisSearchSpec (patientId, fromDate を使用)
     * @return List of RegisteredDiagnosisModel
     */
    @POST
    @Path("getOrcaDisease")
    List<RegisteredDiagnosisModel> getOrcaDisease(DiagnosisSearchSpec spec);

    /**
     * DocumentModel から中途終了データ作成 (medicalmodv2).
     *
     * @param document DocumentModel
     * @return ApiResult
     */
    @POST
    @Path("sendDocument")
    ApiResult sendDocument(DocumentModel document);

    /**
     * medicalmodv2 で ORCA に病名を送る.
     *
     * @param diagnoses List of RegisteredDiagnosisModel
     * @return ApiResult
     */
    @POST
    @Path("sendDiagnoses")
    ApiResult sendDiagnoses(List<RegisteredDiagnosisModel> diagnoses);

    /**
     * subjectivesv2 で ORCA に症状詳記を送る.
     *
     * @param spec SubjectivesSpec
     * @return ApiResult
     */
    @POST
    @Path("sendSubjectives")
    ApiResult sendSubjectives(SubjectivesSpec spec);

    /**
     * subjectiveslstv2 で ORCA から症状詳記の一覧を得る.
     *
     * @param spec SubjectivesSpec
     * @return ApiResult
     */
    @POST
    @Path("getSubjectives")
    List<SubjectivesSpec> getSubjectives(SubjectivesSpec spec);

    /**
     * TBL_ONSHI_YAKUZAI_SUB から資格確認薬剤情報を得る.
     *
     * @param ptnum patient num
     * @return List of OnshiYakuzai
     */
    @POST
    @Path("getDrugHistory")
    List<OnshiYakuzai> getDrugHistory(String ptnum);

    /**
     * TBL_ONSHI_YAKUZAI_SUB に資格確認薬剤情報があるかどうかを返す.
     *
     * @param ptnum patient num
     * @return has drug history or not
     */
    @POST
    @Path("hasDrugHistory")
    boolean hasDrugHistory(String ptnum);

    /**
     * TBL_ONSHI_KENSHIN_SUB から資格確認特定健診情報を得る.
     *
     * @param ptnum patient num
     * @return List of Onshi Kenshin
     */
    @POST
    @Path("getKenshin")
    List<OnshiKenshin> getKenshin(String ptnum);

    /**
     * TBL_ONSHI_KENSHIN_SUB に資格確認特定健診情報があるかどうかを返す.
     *
     * @param ptnum patient num
     * @return has kenshin or not
     */
    @POST
    @Path("hasKenshin")
    boolean hasKenshin(String ptnum);

    /**
     * 長期収載品選定療養区分を返す.
     *
     * @param srycd 対象医薬品の診療行為コード
     * @return 選定療養区分
     */
    @POST
    @Path("getChokisenteikbn")
    int getChokisenteikbn(String srycd);
}

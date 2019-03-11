package open.dolphin.delegater;

import open.dolphin.dto.ApiResult;
import open.dolphin.dto.DiagnosisSearchSpec;
import open.dolphin.dto.OrcaEntry;
import open.dolphin.dto.PatientVisitSpec;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.orca.orcadao.bean.Syskanri;
import open.dolphin.orca.orcadao.bean.Wksryact;
import open.dolphin.service.OrcaService;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * OrcaDelegater.
 *
 * @author pns
 */
public class OrcaDelegater extends BusinessDelegater<OrcaService> {

    /**
     * 中途終了患者情報.
     *
     * @param spec PatientVisitSpec (patientId と date を使用)
     * @return Wksryact
     */
    public Wksryact getWksryact(PatientVisitSpec spec) {
        return getService().getWksryact(spec);
    }

    /**
     * 中途終了患者情報が存在するかどうか.
     *
     * @param ptId "000001"
     * @return 中途終了情報あり=true
     */
    public boolean existsOrcaWorkingData(String ptId) {
        return getService().existsOrcaWorkingData(ptId);
    }

    /**
     * 職員情報.
     *
     * @return Syskanri のリスト
     */
    public List<Syskanri> getSyskanri() {
        return getService().getSyskanri();
    }

    /**
     * TBL_TENSU からキーワードを検索.
     *
     * @param keyword キーワード
     * @return OrcaEntry の List
     */
    public List<OrcaEntry> findTensu(String keyword) {
        return getService().findTensu(keyword);
    }

    /**
     * TBL_BYOMEI からキーワードを検索.
     *
     * @param keyword キーワード
     * @return OrcaEntry の List
     */
    public List<OrcaEntry> findDiagnosis(String keyword) {
        return getService().findDiagnosisByKeyword(keyword);
    }

    /**
     * TBL_BYOMEI から病名コードのリストに対応する病名を検索.
     *
     * @param srycds 病名コードのセット
     * @return OrcaEntry の List
     */
    public List<OrcaEntry> findDiagnosis(List<String> srycds) {
        return getService().findDiagnosisByCodes(srycds);
    }

    /**
     * 移行病名を調べる.
     *
     * @param srycds 病名コードのリスト
     * @return そのうち移行病名になっているののリスト
     */
    public List<String> findIkouByomei(List<String> srycds) {
        return getService().findIkouByomei(srycds);
    }

    /**
     * TBL_INPUTCD を検索して入力セット（約束処方、診療セット）のリストを返す.
     *
     * @return ModuleInfoBean
     */
    public List<ModuleInfoBean> getOrcaInputCdList() {
        return getService().getOrcaInputCdList();
    }

    /**
     * StampInfo を元に TBL_INPUTSET，TBL_TENSU を検索してスタンプの実体を作る
     *
     * @param stampInfo ModuleInfoBean
     * @return ModuleModel の List
     */
    public List<ModuleModel> getStamp(ModuleInfoBean stampInfo) {
        return getService().getStamp(stampInfo);
    }

    /**
     * TBL_PTBYOMEI を検索して RegisteredDiagnosisModel を作る
     *
     * @param patientId 患者 ID "000001"
     * @param from Date
     * @param to Date
     * @param ascend 昇順=true
     * @return List of RegisteredDiagnosisModel
     */
    public List<RegisteredDiagnosisModel> getOrcaDisease(String patientId, LocalDate from, LocalDate to, boolean ascend) {

        DiagnosisSearchSpec spec = new DiagnosisSearchSpec();
        spec.setPatientId(patientId);
        spec.setFromDate(Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spec.setToDate(Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        return getService().getOrcaDisease(spec);
    }

    /**
     * DocumentModel から中途終了データ作成 (medicalmodv2).
     *
     * @param document DocumentModel
     * @return ApiResult
     */
    public Result send(DocumentModel document) {
        ApiResult result = getService().sendDocument(document);
        String apiResult = result.getApiResult();

        // 他端末で使用中(90)の場合は，手動でリトライする
        while("90".equals(apiResult)) {
            logger.info("OrcaApi47: busy, waiting for retrial (" + apiResult + ")");
            if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(null,
                            "ORCA で使用中のため送信できません。リトライしますか？", "ORCA 送信エラー", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE)) {
                return Result.ERROR;
            }
            result = getService().sendDocument(document);
            apiResult = result.getApiResult();
        }
        return Result.NO_ERROR;
    }

    /**
     * medicalmodv2 で ORCA に病名を送る.
     *
     * @param diagnoses List of RegisteredDiagnosisModel
     * @return ApiResult
     */
    public Result send(List<RegisteredDiagnosisModel> diagnoses) {
        ApiResult result = getService().sendDiagnoses(diagnoses);
        return Result.NO_ERROR;
    }
}

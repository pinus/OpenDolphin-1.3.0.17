package open.dolphin.service;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.orca.OrcaEntry;

/**
 *
 * @author pns
 */
@Path("orca")
@RolesAllowed("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface OrcaService {

    /**
     * TBL_WKSRYACT ワーク診療行為（中途終了データ）から UUID と画面展開フラグ（1=画面展開されている）を返す
     * OrcaEntry の UUID = Code，フラグ = Comment に格納
     * @param ptId
     * @return
     */
    public List<OrcaEntry> getWksryactEntries(String ptId);

    /**
     * TBL_SYSKANRI から検索
     * kanricd = Inputcd, kbncd = Code, kanritbl = Comment に格納
     * @param kanricd
     * @return
     */
    public List<OrcaEntry> getSyskanriEntries(String kanricd);

    /**
     * TBL_TENSU からキーワードを検索
     * @param keyword
     * @return
     */
    public List<OrcaEntry> getTensuEntries(String keyword);

    /**
     * TBL_BYOMEI からキーワードを検索
     * @param keyword
     * @return
     */
    public List<OrcaEntry> getByomeiEntriesFromKeyword(String keyword);

    /**
     * 病名コードのリストに対応する OrcaEntry を返す
     * @param codes 病名コードのセット
     * @return DiseaseEntry のリスト
     */
    public List<OrcaEntry> getByomeiEntriesFromCodes(List<String> codes);

    /**
     * TBL_INPUTCD を検索して入力セット（約束処方、診療セット）のリストを返す
     * @return 入力セットコード(inputcd)の昇順リスト
     */
    public List<OrcaEntry> getOrcaInputCdList();

    /**
     * StampInfo を元に TBL_INPUTSET，TBL_TENSU を検索してスタンプの実体を作る
     * @param stampInfo
     * @return
     */
    public List<ModuleModel> getStamp(ModuleInfoBean stampInfo);

    /**
     * TBL_PTBYOMEI を検索して RegisteredDiagnosisModel を作る
     * @param patientId
     * @param from
     * @param to
     * @param ascend
     * @return
     */
    public List<RegisteredDiagnosisModel> getOrcaDisease(String patientId, String from, String to, Boolean ascend);
}

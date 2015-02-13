package open.dolphin.service;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import open.dolphin.dto.*;
import open.dolphin.infomodel.*;

/**
 *
 * @author pns
 */
@Path("karte")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("user")
public interface KarteService {

    /**
     * カルテの基礎的な情報をまとめて返す。
     * これはクライイントがカルテをオープンする時、なるべく通信トラフィックを少なくするための手段である。
     * @param spec PatientModel の primary key と from date をセットして呼ぶ
     * @return 基礎的な情報をフェッチした KarteBean
     */
    @POST
    @Path("getKarte")
    public KarteBean getKarte(KarteBeanSpec spec);

    /**
     * KarteId から 関連する AllergyModel のリストを返す
     * @param karteId
     * @return
     */
    @POST
    @Path("getAllergyList")
    public List<AllergyModel> getAllergyList(Long karteId);

    /**
     * KarteId から関連する PhysicalModel のリストを返す
     * @param karteId
     * @return
     */
    @POST
    @Path("getPhysicalList")
    public List<PhysicalModel> getPhysicalList(Long karteId);

    /**
     * PatientId, fromDate から関連する PatientVisitModel.pvtDate のリストを返す
     * @param spec
     * @return
     */
    @POST
    @Path("getPvtList")
    public List<String> getPvtList(KarteBeanSpec spec);

    /**
     * KarteId から関連する PatientMemoModel を返す
     * @param karteId
     * @return
     */
    @POST
    @Path("getPatientMemo")
    public PatientMemoModel getPatientMemo(Long karteId);

    /**
     * 文書履歴エントリを取得する。
     * @param spec KarteBean の primary key と from date をセットして呼ぶ
     * @return DocInfo のコレクション
     */
    @POST
    @Path("getDocInfoList")
    public List<DocInfoModel> getDocInfoList(DocumentSearchSpec spec);

    /**
     * 文書(DocumentModel Object)を取得する。
     * @param ids DocumentModel の pkコレクション
     * @return DocumentModelのコレクション
     */
    @POST
    @Path("getDocumentList")
    public List<DocumentModel> getDocumentList(List<Long> ids);

    /**
     * ドキュメント DocumentModel オブジェクトを保存する。
     * @param document 追加するDocumentModel オブジェクト
     * @return 追加した数
     */
    @POST
    @Path("addDocument")
    public long addDocument(DocumentModel document);

    /**
     * ドキュメントを論理削除する。
     * @param pk 論理削除するドキュメントの primary key
     * @return 削除した件数
     */
    @POST
    @Path("deleteDocument")
    public int deleteDocument(Long pk);

    /**
     * ドキュメントのタイトルを変更する。
     * @param spec
     * @return 変更した件数
     */
    @POST
    @Path("updateTitle")
    public int updateTitle(DocumentTitleSpec spec);

    /**
     * ModuleModelエントリを取得する。
     * @param spec モジュール検索仕様
     * @return ModuleModelリストのリスト
     */
    @POST
    @Path("getModuleList")
    public List<List<ModuleModel>> getModuleList(ModuleSearchSpec spec);

    /**
     * SchemaModelエントリを取得する。
     * @param spec
     * @return SchemaModelエントリの配列
     */
    @POST
    @Path("getImageList")
    public List<List<SchemaModel>> getImageList(ImageSearchSpec spec);

    /**
     * 画像を取得する。
     * @param id SchemaModel Id
     * @return SchemaModel
     */
    @POST
    @Path("getImage")
    public SchemaModel getImage(Long id);

    /**
     * 傷病名リストを取得する。
     * @param spec 検索仕様
     * @return 傷病名のリスト
     */
    @POST
    @Path("getDiagnosisList")
    public List<RegisteredDiagnosisModel> getDiagnosisList(DiagnosisSearchSpec spec);

    /**
     * 傷病名を追加する。
     * @param addList 追加する傷病名のリスト
     * @return idのリスト
     */
    @POST
    @Path("addDiagnosisList")
    public List<Long> addDiagnosisList(List<RegisteredDiagnosisModel> addList);

    /**
     * 傷病名を更新する。
     * @param updateList
     * @return 更新数
     */
    @POST
    @Path("updateDiagnosisList")
    public int updateDiagnosisList(List<RegisteredDiagnosisModel> updateList);

    /**
     * 傷病名を削除する。
     * @param removeList 削除する傷病名のidリスト
     * @return 削除数
     */
    @POST
    @Path("removeDiagnosisList")
    public int removeDiagnosisList(List<Long> removeList);

    /**
     * Observationを取得する。
     * @param spec 検索仕様
     * @return Observationのリスト
     */
    @POST
    @Path("getObservationList")
    public List<ObservationModel> getObservationList(ObservationSearchSpec spec);

    /**
     * Observationを追加する。
     * @param observations 追加するObservationのリスト
     * @return 追加したObservationのIdリスト
     */
    @POST
    @Path("addObservationList")
    public List<Long> addObservationList(List<ObservationModel> observations);

    /**
     * Observationを更新する。
     * @param observations 更新するObservationのリスト
     * @return 更新した数
     */
    @POST
    @Path("updateObservationList")
    public int updateObservationList(List<ObservationModel> observations);

    /**
     * Observationを削除する。
     * @param observations 削除するObservationのリスト
     * @return 削除した数
     */
    @POST
    @Path("removeObservationList")
    public int removeObservationList(List<Long> observations);

    /**
     * 患者メモを更新する。
     * @param memo 更新するメモ
     * @return
     */
    @POST
    @Path("updatePatientMemo")
    public int updatePatientMemo(PatientMemoModel memo);

    /**
     * 予約を保存、更新、削除する。
     * @param spec 予約情報の DTO
     * @return
     */
    @POST
    @Path("putAppointment")
    public int putAppointment(AppointSpec spec);

    /**
     * 予約を検索する。
     * @param spec 検索仕様
     * @return 予約の Collection
     */
    @POST
    @Path("getAppointmentList")
    public List<List<AppointmentModel>> getAppointmentList(ModuleSearchSpec spec);

}

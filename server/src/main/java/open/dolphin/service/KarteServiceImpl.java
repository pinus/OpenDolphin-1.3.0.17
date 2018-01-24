package open.dolphin.service;

import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import open.dolphin.dto.*;
import open.dolphin.infomodel.*;
import org.apache.log4j.Logger;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

/**
 * KarteServiceImpl.
 * 
 * @author pns
 */
@Stateless
public class KarteServiceImpl extends DolphinService implements KarteService {
    private static final long serialVersionUID = 1L;
    private final Logger logger = Logger.getLogger(KarteServiceImpl.class);

    /**
     * カルテの基礎的な情報をまとめて返す.
     * @Param spec KarteBeanSpec
     * @return 基礎的な情報をフェッチした KarteBean
     */
    @Override
    public KarteBean getKarte(KarteBeanSpec spec) {
        long patientPk = spec.getPatientPk();
        Date fromDate = spec.getFromDate();

        try {
            // 最初に患者のカルテを取得する
            KarteBean karte = em.createQuery("select k from KarteBean k where k.patient.id = :patientPk", KarteBean.class)
                .setParameter("patientPk", patientPk).getSingleResult();

            // カルテの PK を得る
            long karteId = karte.getId();

            // アレルギーデータを取得する
            List<AllergyModel> allergyList = getAllergyList(karteId);
            karte.setAllergyEntry(allergyList);

            // 身長データを取得する
            List<PhysicalModel> physList = getPhysicalList(karteId);
            karte.setPhysicalEntry(physList);

            // 直近の来院日エントリーを取得しカルテに設定する
            List<String> pvtList = getPvtList(spec);
            karte.setPvtDateEntry(pvtList);

            // 文書履歴エントリーを取得しカルテに設定する
            DocumentSearchSpec docSpec = new DocumentSearchSpec();
            docSpec.setKarteId(karteId);
            docSpec.setFromDate(fromDate);
            docSpec.setIncludeModifid(false);
            List<DocInfoModel> docList = getDocInfoList(docSpec);
            karte.setDocInfoEntry(docList);

            // 患者Memoを取得する
            PatientMemoModel memo = getPatientMemo(karteId);
            karte.setPatientMemo(memo);

            return karte;

        } catch (NoResultException e) {
            // 患者登録の際にカルテも生成してある
            logger.info(e.getMessage(), e.getCause());
        }

        return null;
    }

    /**
     * アレルギーリストを返す.
     * @param karteId
     * @return
     */
    @Override
    public List<AllergyModel> getAllergyList(Long karteId) {
        List<ObservationModel> observations = em.createQuery("select o from ObservationModel o where o.karte.id=:karteId and o.observation='Allergy'", ObservationModel.class)
            .setParameter("karteId", karteId).getResultList();

        List<AllergyModel> allergies = observations.stream().map(observation -> {
            AllergyModel allergy = new AllergyModel();
            allergy.setObservationId(observation.getId());
            allergy.setFactor(observation.getPhenomenon());
            allergy.setSeverity(observation.getCategoryValue());
            allergy.setIdentifiedDate(observation.confirmDateAsString());
            return allergy;
        }).collect(Collectors.toList());

        return allergies;
    }

    /**
     * 身長・体重（PhysicalModel）リストを返す.
     * @param karteId
     * @return
     */
    @Override
    public List<PhysicalModel> getPhysicalList(Long karteId) {
        // 身長・体重データを取得
        List<ObservationModel> observations =
            em.createQuery("select o from ObservationModel o where o.karte.id=:karteId and o.observation='PhysicalExam' and (o.phenomenon='bodyHeight' or o.phenomenon='bodyWeight')", ObservationModel.class)
            .setParameter("karteId", karteId).getResultList();

        List<PhysicalModel> listH = new ArrayList<>();
        List<PhysicalModel> listW = new ArrayList<>();

        for(ObservationModel observation : observations) {
            PhysicalModel physical = new PhysicalModel();
            physical.setIdentifiedDate(observation.confirmDateAsString());
            physical.setMemo(ModelUtils.getDateAsString(observation.getRecorded()));
            if (observation.getPhenomenon().equals("bodyWeight")) {
                physical.setWeightId(observation.getId());
                physical.setWeight(observation.getValue());
                // 体重
                listW.add(physical);
            } else {
                physical.setHeightId(observation.getId());
                physical.setHeight(observation.getValue());
                // 身長
                listH.add(physical);
            }
        }

        // 同じ Recorded date の身長と体重をまとめる
        List<PhysicalModel> list = new ArrayList<>();
        // 身長体重ともある場合
        if (! listH.isEmpty() && ! listW.isEmpty()) {
            for (PhysicalModel h : listH) {
                String memoH = (h.getMemo() == null)? h.getIdentifiedDate() : h.getMemo();
                // 体重のメモ(=recorded date)が一致するものを見つける
                PhysicalModel found = null;
                for (PhysicalModel w : listW) {
                    String memoW = (w.getMemo() == null)? w.getIdentifiedDate() : w.getMemo();
                    if (memoW.equals(memoH)) {
                        found = w;
                        // 見つかったら体重データを加える
                        h.setWeightId(w.getWeightId());
                        h.setWeight(w.getWeight());
                        break;
                    }
                }
                list.add(h);
                // 一致した体重は h に加えたのでリストから除く
                if (found != null) { listW.remove(found); }
            }
            // 体重のリストが残っていれば加える
            list.addAll(listW);
        }
        // 身長だけの場合
        else if (! listH.isEmpty()) { list.addAll(listH); }
        // 体重だけの場合
        else if (! listW.isEmpty()) { list.addAll(listW); }

        return list;
    }

    /**
     * Karte に関連した PatientVisitModel.pvtDate のリストを返す
     * @param spec
     * @return
     */
    @Override
    public List<String> getPvtList(KarteBeanSpec spec) {
        long patientPk = spec.getPatientPk();
        Date fromDate = spec.getFromDate();

        List<PatientVisitModel> latestVisits = em.createQuery("select p from PatientVisitModel p where p.patient.id = :patientPk and p.pvtDate >= :fromDate", PatientVisitModel.class)
            .setParameter("patientPk", patientPk)
            .setParameter("fromDate", ModelUtils.getDateAsString(fromDate)).getResultList();

        List<String> visits = new ArrayList<>();
        for (PatientVisitModel bean : latestVisits) {
            // キャンセルされた受付は無視する
            if (bean.getState() != KarteState.CANCEL_PVT) {
                visits.add(bean.getPvtDate());
            }
        }
        return visits;
    }

    /**
     * KarteId の関連する PatientMemoModel を返す
     * @param karteId
     * @return
     */
    @Override
    public PatientMemoModel getPatientMemo(Long karteId) {
        List<PatientMemoModel> memo = em.createQuery("select p from PatientMemoModel p where p.karte.id = :karteId", PatientMemoModel.class)
            .setParameter("karteId", karteId).getResultList();
        return memo.isEmpty()? null : memo.get(0);
    }

    /**
     * 文書履歴エントリを取得する
     * @param spec
     * @return DocInfo のコレクション
     */
    @Override
    public List<DocInfoModel> getDocInfoList(DocumentSearchSpec spec) {

        List<DocumentModel> documents;

        if (spec.isIncludeModifid()) {
            documents = em.createQuery("select d from DocumentModel d where d.karte.id = :karteId and d.started >= :fromDate and d.status !='D'", DocumentModel.class)
                .setParameter("karteId", spec.getKarteId())
                .setParameter("fromDate", spec.getFromDate()).getResultList();
        } else {
            documents = em.createQuery("select d from DocumentModel d where d.karte.id = :karteId and d.started >= :fromDate and (d.status='F' or d.status='T')", DocumentModel.class)
                .setParameter("karteId", spec.getKarteId())
                .setParameter("fromDate", spec.getFromDate()).getResultList();
        }

        List<DocInfoModel> result = new ArrayList<>();
        for (DocumentModel docBean : documents) {
            // モデルからDocInfo へ必要なデータを移す
            // クライアントが DocInfo だけを利用するケースがあるため
            docBean.toDetach();
            result.add(docBean.getDocInfo());
        }
        return result;
    }

    /**
     * 文書(DocumentModel Object)を取得する.
     * @param ids DocumentModel の pkコレクション
     * @return DocumentModelのコレクション
     */
    @Override
    public List<DocumentModel> getDocumentList(List<Long> ids) {
        //long t = System.currentTimeMillis();
        List<DocumentModel> ret = new ArrayList<>();

        // まとめて query
        List<ModuleModel> mods = em.createQuery("select m from ModuleModel m where m.document.id in (:ids)", ModuleModel.class)
                .setParameter("ids", ids)
                .getResultList();
        List<SchemaModel> imgs = em.createQuery("select m from SchemaModel m where m.document.id in (:ids)", SchemaModel.class)
                .setParameter("ids", ids)
                .getResultList();

        // とってきた ModuleModel を id 毎に分ける
        HashMap<Long, List<ModuleModel>> modsMap = new HashMap<>();
        for (ModuleModel m : mods) {
            // beanBytes をいじるために detach する
            em.detach(m);
            // beanBytes をデコードする
            m.setModel((IInfoModel) ModelUtils.xmlDecode(m.getBeanBytes()));
            m.setBeanBytes(null);
            // id 毎に分類
            Long id = m.getDocument().getId();
            List<ModuleModel> list = modsMap.get(id);
            if (list == null) {
                list = new ArrayList<>();
                modsMap.put(id, list);
            }
            list.add(m);
        }
        // とってきた SchemaModel を id 毎に分ける
        HashMap<Long, List<SchemaModel>> imgsMap = new HashMap<>();
        for (SchemaModel m : imgs) {
            Long id = m.getDocument().getId();
            List<SchemaModel> list = imgsMap.get(id);
            if (list == null) {
                list = new ArrayList<>();
                imgsMap.put(id, list);
            }
            list.add(m);
        }

        // とってきた list を DocumentModel に分配
        for (Long id : ids) {

            // DocuentBean を取得する
            DocumentModel document = em.find(DocumentModel.class, id);
            // detach しないと org.hibernate.PersistentObjectException: detached entity passed to persist
            em.detach(document);

            // ModuleBean を登録
            List<ModuleModel> modules = modsMap.get(id);
            if (modules == null) { modules = new ArrayList<>(); }
            document.setModules(modules);

            // SchemaModel を登録
            List<SchemaModel> images = imgsMap.get(id);
            if (images == null) { images = new ArrayList<>(); }
            document.setSchema(images);

            ret.add(document);
        }
        //System.out.println("---- lap= " + (System.currentTimeMillis() - t));
        return ret;
    }

    /**
     * ドキュメント DocumentModel オブジェクトを保存する.
     * @param document 追加するDocumentModel オブジェクト
     * @return 追加した document の primary key
     */
    @Override
    public long addDocument(DocumentModel document) {
        // ModuleModel の永続化 beanBytes を作成する
        for (ModuleModel m : document.getModules()) {
            m.setBeanBytes(ModelUtils.xmlEncode(m.getModel()));
            m.setModel(null);
        }
        // 永続化する
        em.persist(document);

        // ID
        long id = document.getId();

        // 修正版の処理は非同期処理させる by masuda-sensei
        processPostAddDocument(document);

        return id;
    }

    /**
     * 後処置は asynchronous にしてクライアントを待たせない by masuda-sensei
     * @param document
     */
    @Asynchronous
    private void processPostAddDocument(DocumentModel document) {
        // 修正版の処理を行う
        DocInfoModel docInfo = document.getDocInfo();
        long parentPk = document.getDocInfo().getParentPk();

        // 親がないならリターン
        if (parentPk == 0L) {
            return ;
        }

        DocumentModel old = em.find(DocumentModel.class, parentPk);
        if (old == null) {
            return;
        }

        // 親文書が仮保存文書なら残す必要なし. なぜならそれは仮保存だから. by masuda-sensei
        if (IInfoModel.STATUS_TMP.equals(old.getStatus())) {
            // 編集元文書の情報を引き継ぐ
            DocInfoModel pInfo = old.getDocInfo();
            document.setLinkId(old.getLinkId());
            document.setLinkRelation(old.getLinkRelation());
            //docInfo.setParentPk(pInfo.getParentPk());   // parentPk = linkId
            docInfo.setParentId(pInfo.getParentId());
            docInfo.setParentIdRelation(pInfo.getParentIdRelation());
            docInfo.setVersionNumber(pInfo.getVersionNumber());

            // 編集元は削除
            em.remove(old);

        } else {
            // 適合終了日を新しい版の確定日にする
            Date ended = document.getConfirmed();

            // オリジナルを取得し 終了日と status = M を設定する
            old.setEnded(ended);
            old.setStatus(InfoModel.STATUS_MODIFIED);

            // HibernateSearchのFulTextEntityManagerを用意. 修正済みのものはインデックスから削除する by masuda-sensei
            final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
            fullTextEntityManager.purge(DocumentModel.class, parentPk);

            // 関連するモジュールとイメージに同じ処理を実行する
            List<ModuleModel> oldModules = em.createQuery("select m from ModuleModel m where m.document.id = :id", ModuleModel.class)
                .setParameter("id", parentPk).getResultList();
            for (ModuleModel model : oldModules) {
                model.setEnded(ended);
                model.setStatus(InfoModel.STATUS_MODIFIED);
            }

            List<SchemaModel> oldImages = em.createQuery("select s from SchemaModel s where s.document.id = :id", SchemaModel.class)
                .setParameter("id", parentPk).getResultList();
            for (SchemaModel model : oldImages) {
                model.setEnded(ended);
                model.setStatus(InfoModel.STATUS_MODIFIED);
            }
        }
    }

    /**
     * ドキュメントを論理削除する.
     * @param id 論理削除するドキュメントの primary key
     * @return 削除した件数
     */
    @Override
    public int deleteDocument(Long id) {

        //オリジナルでは修正したり仮保存をした文書を削除できないので改変 by masuda-sensei

        // 対象 Document を取得する
        DocumentModel target = em.find(DocumentModel.class, id);
        // その親文書を取得
        DocumentModel parent = getParent(target);
        // 関連するDocumentModelを再帰で取得する
        Set<DocumentModel> delSet = getChildren(parent);

        Date ended = new Date();

         for (DocumentModel delete : delSet) {

            long delId = delete.getId();

            // HibernateSearchのFulTextEntityManagerを用意. 削除済みのものはインデックスから削除する
            final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
            fullTextEntityManager.purge(DocumentModel.class, delId);

            if (InfoModel.STATUS_TMP.equals(delete.getStatus())) {
                // 仮文書の場合は抹消スル
                DocumentModel dm = em.find(DocumentModel.class, delId);
                em.remove(dm);

            } else {
                // 削除フラグをたてる
                delete.setStatus(InfoModel.STATUS_DELETE);
                delete.setEnded(ended);

                // 関連するモジュールに同じ処理を行う
                List<ModuleModel> deleteModules = em.createQuery("select m from ModuleModel m where m.document.id=:id", ModuleModel.class)
                    .setParameter("id", delId).getResultList();
                for (ModuleModel model : deleteModules) {
                    model.setStatus(InfoModel.STATUS_DELETE);
                    model.setEnded(ended);
                }

                // 関連する画像に同じ処理を行う
                List<SchemaModel> deleteImages = em.createQuery("select i from SchemaModel i where i.document.id=:id", SchemaModel.class)
                    .setParameter("id", delId).getResultList();
                for (SchemaModel model : deleteImages) {
                    model.setStatus(InfoModel.STATUS_DELETE);
                    model.setEnded(ended);
                }
            }
        }
        return 1;
    }

    /**
     * 親文書を追いかける by masuda-sensei
     * @param dm
     * @return
     */
    private DocumentModel getParent(DocumentModel dm) {

        long linkId = dm.getLinkId();
        DocumentModel model = dm;
        while (linkId != 0) {
            model = em.find(DocumentModel.class, linkId);
            linkId = model.getLinkId();
        }
        return model;
    }

    /**
     * 子文書を再帰で探す by masuda-sensei
     * @param parent
     * @return
     */
    private Set<DocumentModel> getChildren(DocumentModel parent) {

        Set<DocumentModel> ret = new HashSet<>();

        // 親を追加
        ret.add(parent);

        List<DocumentModel> children = em.createQuery("select d from DocumentModel d where d.linkId=:id", DocumentModel.class)
            .setParameter("id", parent.getId()).getResultList();

        // 子供の子供をリストに追加
        for (DocumentModel child : children) {
            ret.addAll(getChildren(child));
        }
        return ret;
    }

    /**
     * ドキュメントのタイトルを変更する.
     * @param spec 変更するドキュメントの primary key, title
     * @return 変更した件数
     */
    @Override
    public int updateTitle(DocumentTitleSpec spec) {
        DocumentModel update = em.find(DocumentModel.class, spec.getDocumentPk());
        update.getDocInfo().setTitle(spec.getTitle());
        return 1;
    }

    /**
     * ModuleModelエントリを取得する.
     * @param spec モジュール検索仕様
     * @return ModuleModelリストのリスト
     */
    @Override
    public List<List<ModuleModel>> getModuleList(ModuleSearchSpec spec) {

        // 抽出期間は別けられている
        Date[] fromDate = spec.getFromDate();
        Date[] toDate = spec.getToDate();
        int len = fromDate.length;
        List<List<ModuleModel>> ret = new ArrayList<>(len);

        // 抽出期間セットの数だけ繰り返す
        for (int i = 0; i < len; i++) {
            List<ModuleModel> modules = em.createQuery("select m from ModuleModel m where m.karte.id = :karteId and m.moduleInfo.entity = :entity and m.started between :fromDate and :toDate and m.status='F'", ModuleModel.class)
                .setParameter("karteId", spec.getKarteId())
                .setParameter("entity", spec.getEntity())
                .setParameter("fromDate", fromDate[i])
                .setParameter("toDate", toDate[i]).getResultList();
            // module 一つ一つ beanBytes を detach してから decode
            for (ModuleModel m : modules) {
                em.detach(m);
                m.setModel((IInfoModel) ModelUtils.xmlDecode(m.getBeanBytes()));
                m.setBeanBytes(null);
            }
            ret.add(modules);
        }
        return ret;
    }

    /**
     * SchemaModelエントリを取得する.
     * @param spec シェーマ検索仕様
     * @return SchemaModelエントリの配列
     */
    @Override
    public List<List<SchemaModel>> getImageList(ImageSearchSpec spec) {

        // 抽出期間は別けられている
        Date[] fromDate = spec.getFromDate();
        Date[] toDate = spec.getToDate();
        int len = fromDate.length;
        List<List<SchemaModel>> ret = new ArrayList<>(len);

        // 抽出期間セットの数だけ繰り返す
        for (int i = 0; i < len; i++) {
            List<SchemaModel> modules = em.createQuery("select i from SchemaModel i where i.karte.id = :karteId and i.started between :fromDate and :toDate and i.status='F'", SchemaModel.class)
                .setParameter("karteId", spec.getKarteId())
                .setParameter("fromDate", fromDate[i])
                .setParameter("toDate", toDate[i]).getResultList();
            ret.add(modules);
        }
        return ret;
    }

    /**
     * 画像を取得する.
     * @param id SchemaModel Id
     * @return SchemaModel
     */
    @Override
    public SchemaModel getImage(Long id) {
        SchemaModel image = em.find(SchemaModel.class, id);
        return image;
    }

    /**
     * 傷病名リストを取得する.
     * @param spec 検索仕様
     * @return 傷病名のリスト
     */
    @Override
    public List<RegisteredDiagnosisModel> getDiagnosisList(DiagnosisSearchSpec spec) {

        List<RegisteredDiagnosisModel> ret;

        // 疾患開始日を指定している
        if (spec.getFromDate() != null) {
            ret = em.createQuery("select r from RegisteredDiagnosisModel r where r.karte.id = :karteId and r.started >= :fromDate", RegisteredDiagnosisModel.class)
                .setParameter("karteId", spec.getKarteId())
                .setParameter("fromDate", spec.getFromDate()).getResultList();
        } else {
            // 全期間の傷病名を得る
            ret = em.createQuery("select r from RegisteredDiagnosisModel r where r.karte.id = :karteId", RegisteredDiagnosisModel.class)
                .setParameter("karteId", spec.getKarteId()).getResultList();
        }
        return ret;
    }

    /**
     * 傷病名を追加する.
     * @param addList 追加する傷病名のリスト
     * @return idのリスト
     */
    @Override
    public List<Long> addDiagnosisList(List<RegisteredDiagnosisModel> addList) {

        List<Long> ret = new ArrayList<>(addList.size());

        for (RegisteredDiagnosisModel bean : addList) {
            em.persist(bean);
            ret.add(bean.getId());
        }
        return ret;
    }

    /**
     * 傷病名を更新する.
     * @param updateList
     * @return 更新数
     */
    @Override
    public int updateDiagnosisList(List<RegisteredDiagnosisModel> updateList) {
        for (RegisteredDiagnosisModel bean : updateList) {
            em.merge(bean);
        }
        return updateList.size();
    }

    /**
     * 傷病名を削除する.
     * @param removeList 削除する傷病名のidリスト
     * @return 削除数
     */
    @Override
    public int removeDiagnosisList(List<Long> removeList) {
        for (Long id : removeList) {
            RegisteredDiagnosisModel bean = em.find(RegisteredDiagnosisModel.class, id);
            em.remove(bean);
        }
        return removeList.size();
    }

    /**
     * Observationを取得する.
     * @param spec 検索仕様
     * @return Observationのリスト
     */
    @Override
    public List<ObservationModel> getObservationList(ObservationSearchSpec spec) {

        List<ObservationModel> ret = null;
        String observation = spec.getObservation();
        String phenomenon = spec.getPhenomenon();
        Date firstConfirmed = spec.getFirstConfirmed();

        if (observation != null) {
            if (firstConfirmed != null) {
                ret = em.createQuery("select o from ObservationModel o where o.karte.id=:karteId and o.observation=:observation and o.started >= :firstConfirmed", ObservationModel.class)
                    .setParameter("karteId", spec.getKarteId())
                    .setParameter("observation", observation)
                    .setParameter("firstConfirmed", firstConfirmed).getResultList();

            } else {
                ret = em.createQuery("select o from ObservationModel o where o.karte.id=:karteId and o.observation=:observation", ObservationModel.class)
                    .setParameter("karteId", spec.getKarteId())
                    .setParameter("observation", observation).getResultList();
            }
        } else if (phenomenon != null) {
            if (firstConfirmed != null) {
                ret = em.createQuery("select o from ObservationModel o where o.karte.id=:karteId and o.phenomenon=:phenomenon and o.started >= :firstConfirmed", ObservationModel.class)
                    .setParameter("karteId", spec.getKarteId())
                    .setParameter("phenomenon", phenomenon)
                    .setParameter("firstConfirmed", firstConfirmed).getResultList();
            } else {
                ret = em.createQuery("select o from ObservationModel o where o.karte.id=:karteId and o.phenomenon=:phenomenon", ObservationModel.class)
                    .setParameter("karteId", spec.getKarteId())
                    .setParameter("phenomenon", phenomenon).getResultList();
            }
        }
        return ret;
    }

    /**
     * Observationを追加する.
     * @param observations 追加するObservationのリスト
     * @return 追加したObservationのIdリスト
     */
    @Override
    public List<Long> addObservationList(List<ObservationModel> observations) {

        if (observations != null && ! observations.isEmpty()) {

            List<Long> ret = new ArrayList<>(observations.size());

            for (ObservationModel model : observations) {
                em.persist(model);
                ret.add(model.getId());
            }
            return ret;
        }
        return null;
    }

    /**
     * Observationを更新する.
     * @param observations 更新するObservationのリスト
     * @return 更新した数
     */
    @Override
    public int updateObservationList(List<ObservationModel> observations) {

        if (observations != null && ! observations.isEmpty()) {
            for (ObservationModel model : observations) {
                em.merge(model);
            }
            return observations.size();
        }
        return 0;
    }

    /**
     * Observationを削除する.
     * @param ids 削除する Observation の primary key リスト
     * @return 削除した数
     */
    @Override
    public int removeObservationList(List<Long> ids) {
        if (ids != null && ! ids.isEmpty()) {
            for (Long id : ids) {
                ObservationModel model = em.find(ObservationModel.class, id);
                em.remove(model);
            }
            return ids.size();
        }
        return 0;
    }

    /**
     * 患者メモを更新する.
     * @param memo 更新するメモ
     * @return 更新した数 1
     */
    @Override
    public int updatePatientMemo(PatientMemoModel memo) {
        if (memo.getId() == 0L) {
            em.persist(memo);
        } else {
            em.merge(memo);
        }
        return 1;
    }

    /**
     * 予約を保存、更新、削除する.
     * @param spec 予約情報の DTO
     */
    @Override
    public int putAppointment(AppointSpec spec) {

        Collection<AppointmentModel> added = spec.getAdded();
        Collection<AppointmentModel> updated = spec.getUpdared();
        Collection<AppointmentModel> removed = spec.getRemoved();

        int cnt = 0;

        // 登録する
        if (added != null && ! added.isEmpty()) {
            for (AppointmentModel bean : added) {
                em.persist(bean);
            }
            cnt += added.size();
        }

        // 更新する
        if (updated != null && ! updated.isEmpty()) {
            for (AppointmentModel bean : updated) {
                // av は分離オブジェクトである
                em.merge(bean);
            }
            cnt += updated.size();
        }

        // 削除
        if (removed != null && ! removed.isEmpty()) {
            for (AppointmentModel bean : removed) {
                // 分離オブジェクトは remove に渡せないので対象を検索する
                AppointmentModel target = em.find(AppointmentModel.class, bean.getId());
                em.remove(target);
            }
            cnt += removed.size();
        }
        return cnt;
    }

    /**
     * 予約を検索する.
     * @param spec 検索仕様
     * @return 予約の List
     */
    @Override
    public List<List<AppointmentModel>> getAppointmentList(ModuleSearchSpec spec) {

        // 抽出期間は別けられている
        Date[] fromDate = spec.getFromDate();
        Date[] toDate = spec.getToDate();
        int len = fromDate.length;
        List<List<AppointmentModel>> ret = new ArrayList<>(len);

        // 抽出期間ごとに検索しコレクションに加える
        for (int i = 0; i < len; i++) {

            List<AppointmentModel> c = em.createQuery("select a from AppointmentModel a where a.karte.id = :karteId and a.date between :fromDate and :toDate", AppointmentModel.class)
                .setParameter("karteId", spec.getKarteId())
                .setParameter("fromDate", fromDate[i])
                .setParameter("toDate", toDate[i]).getResultList();
            ret.add(c);
        }
        return ret;
    }
}























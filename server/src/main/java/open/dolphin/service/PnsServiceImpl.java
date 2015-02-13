package open.dolphin.service;

import java.util.*;
import javax.ejb.Stateless;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ModuleModel;
import org.apache.log4j.Logger;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

/**
 * いろいろやってみる service
 * @author pns
 */
@Stateless
public class PnsServiceImpl extends DolphinService implements PnsService {
    private static final long serialVersionUID = 1L;
    private final Logger logger = Logger.getLogger(PnsServiceImpl.class);

    /**
     * patient_id から，今日のカルテ内容の module のリストを返す　カルテがなければ null
     * @param patientId
     * @return
     */
    @Override
    public List<ModuleModel> peekKarte(Long patientId) {
        try {

            Long karteId = em.createQuery("select k.id from KarteBean k where k.patient.id = :patientId", Long.class)
                .setParameter("patientId", patientId).getSingleResult();

            GregorianCalendar today = new GregorianCalendar();
            today.set(Calendar.HOUR_OF_DAY, 0);

            List<Long> docIdList = em.createQuery("select d.id from DocumentModel d where d.karte.id = :karteId and (d.status ='F' or d.status='T') and d.started >= :fromDate", Long.class)
                .setParameter("karteId", karteId)
                .setParameter("fromDate", today.getTime()).getResultList();

            if (docIdList.isEmpty()) {
                return null;

            } else {
                Long docId = docIdList.get(0);
                // m.document で DocumentModel がとれて，document.id で doc_id がとれる
                List<ModuleModel> modules = em.createQuery("select m from ModuleModel m where m.document.id = :id", ModuleModel.class)
                    .setParameter("id", docId).getResultList();

                // beanBytes を変換して新しいモデルにして返す
                List<ModuleModel> ret = new ArrayList<>();
                for (ModuleModel src : modules) {
                    ModuleModel dist = new ModuleModel();
                    dist.setDocument(src.getDocument());
                    dist.setModuleInfo(src.getModuleInfo());
                    dist.setModel((IInfoModel) ModelUtils.xmlDecode(src.getBeanBytes()));

                    ret.add(dist);
                }
                return ret;
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e.getCause());
        }
        return null;
    }

    /**
     * hibernate search のインデックスを作る
     * standalone.xml のトランザクション default-timeout 変更必要
     *  ex) 4 hrs = 14400 secs
     *      coordinator-environment default-timeout="14400"
     */
    @Override
    public void makeInitialIndex() {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        MassIndexer massIndexer = fullTextEntityManager.createIndexer(DocumentModel.class);

        massIndexer.purgeAllOnStart(true)
                .batchSizeToLoadObjects(30)
                .threadsToLoadObjects(4);

        massIndexer.start();
    }
}

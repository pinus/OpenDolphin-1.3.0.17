package open.dolphin.service;

import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.util.JsonUtils;
import open.dolphin.util.ModelUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.jboss.logging.Logger;

import jakarta.ejb.Stateless;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

/**
 * いろいろやってみる service.
 *
 * @author pns
 */
@Stateless
public class PnsServiceImpl extends DolphinService implements PnsService {
        private static final String CALENDAR_DATA = "calendar.data";

    private final Preferences prefs = Preferences.userNodeForPackage(PnsServiceImpl.class);
    private final Logger logger = Logger.getLogger(PnsServiceImpl.class);

    /**
     * patient_id から，今日のカルテ内容の module のリストを返す.　カルテがなければ null.
     *
     * @param patientId PatientModel の pk
     * @return List of ModuleModel
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
                return modules.stream().map(src -> {
                    ModuleModel dist = new ModuleModel();
                    dist.setDocument(src.getDocument());
                    dist.setModuleInfo(src.getModuleInfo());
                    dist.setModel((IInfoModel) ModelUtils.xmlDecode(src.getBeanBytes()));
                    return dist;
                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e.getCause());
        }
        return null;
    }

    /**
     * Preferences に保存したカレンダー情報をクライアントに知らせる.
     *
     * @return Calendar data array
     */
    @Override
    public String[][] getCalendarData() {
        String json = prefs.get(CALENDAR_DATA, null);
        return StringUtils.isEmpty(json)
            ? null
            : JsonUtils.fromJson(json, String[][].class);
    }

    /**
     * Calendar data を Preferences に保存する.
     *
     * @param data Calendar data array
     */
    public void saveCalendarData(String[][] data) {
        String json = JsonUtils.toJson(data);
        prefs.put(CALENDAR_DATA, json);
        logger.info("calendar data saved");
    }

    /**
     * hibernate search のインデックスを作る.
     * トランザクションタイムアウト延長が必要. (default = 300)
     * <pre>
     * $ jboss-cli.sh --connect
     * [pns@localhost:9990] /subsystem=transactions:write-attribute(name=default-timeout,value=14400)
     * </pre>
     */
    @Override
    public void makeInitialIndex() {
        final SearchSession searchSession = Search.session(em);

        int core = Runtime.getRuntime().availableProcessors();
        logger.info("processor number = " + core);

        MassIndexer massIndexer = searchSession.massIndexer(DocumentModel.class)
            .purgeAllOnStart(true)
            .transactionTimeout(14400)
            .threadsToLoadObjects(core);

        try {
            massIndexer.startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }
}

package open.dolphin.service;

import open.dolphin.dto.PatientSearchSpec;
import open.dolphin.infomodel.*;
import open.dolphin.util.ModelUtils;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.jboss.logging.Logger;

import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * PatientServiceImpl.
 *
 * @author pns
 */
@Stateless
public class PatientServiceImpl extends DolphinService implements PatientService {
    private static final long serialVersionUID = -456476244129106722L;
    private final Logger logger = Logger.getLogger(PatientService.class);

    /**
     * 患者オブジェクトを取得する.
     *
     * @param spec PatientSearchSpec 検索仕様
     * @return 患者オブジェクトの Collection
     */
    @Override
    public List<PatientModel> getPatientList(PatientSearchSpec spec) {

        String fid = getCallersFacilityId();

        // 戻り値
        List<PatientModel> ret = new ArrayList<>();
        // 絞り込み id
        List<Long> ids = spec.getNarrowingList();

        switch (spec.getType()) {
            case DATE -> {
                final String dateQuery = "select p from PatientVisitModel p where p.facilityId = :fid and p.pvtDate like :date";
                final String dateQueryNarrow = dateQuery + " and p.patient.id in (:ids)";

                List<PatientVisitModel> pvtList = ids.isEmpty() ?
                    em.createQuery(dateQuery, PatientVisitModel.class)
                        .setParameter("fid", fid)
                        .setParameter("date", spec.getDate() + "%").getResultList()
                    :
                    em.createQuery(dateQueryNarrow, PatientVisitModel.class)
                        .setParameter("fid", fid)
                        .setParameter("date", spec.getDate() + "%")
                        .setParameter("ids", ids).getResultList();

                ret.addAll(pvtList.stream().map(PatientVisitModel::getPatient).toList());
            }
            case ID -> {
                final String idQuery = "select p from PatientModel p where p.facilityId = :fid and p.patientId like :pid";
                final String idQueryNarrow = idQuery + " and p.id in (:ids)";

                String pid = spec.getPatientId();
                if (!pid.endsWith("%")) {
                    pid += "%";
                }

                ret = ids.isEmpty() ?
                    em.createQuery(idQuery, PatientModel.class)
                        .setMaxResults(50)
                        .setParameter("fid", fid)
                        .setParameter("pid", pid).getResultList()
                    :
                    em.createQuery(idQueryNarrow, PatientModel.class)
                        .setMaxResults(50)
                        .setParameter("fid", fid)
                        .setParameter("pid", pid)
                        .setParameter("ids", ids).getResultList();
            }
            case NAME, KANA, ROMAN -> {
                final String nameQuery = "select p from PatientModel p where p.facilityId = :fid and "
                    + "(p.fullName like :name or p.kanaName like :name or p.romanName like :name)";
                final String nameQueryNarrow = nameQuery + " and p.id in (:ids)";

                String name = spec.getName();
                if (!name.endsWith("%")) { name += "%"; }

                ret = ids.isEmpty() ?
                    em.createQuery(nameQuery, PatientModel.class)
                        .setParameter("fid", fid)
                        .setParameter("name", name).getResultList()
                    :
                    em.createQuery(nameQueryNarrow, PatientModel.class)
                        .setParameter("fid", fid)
                        .setParameter("name", name)
                        .setParameter("ids", ids).getResultList();
            }
            case BIRTHDAY -> {
                final String birthdayQuery = "select p from PatientModel p where p.facilityId = :fid and p.birthday like :birthday";
                final String birthdayQueryNarrow = birthdayQuery + " and p.id in (:ids)";

                String birthday = spec.getBirthday();
                if (!birthday.endsWith("%")) {
                    birthday += "%";
                }

                ret = ids.isEmpty() ?
                    em.createQuery(birthdayQuery, PatientModel.class)
                        .setMaxResults(50)
                        .setParameter("fid", fid)
                        .setParameter("birthday", birthday).getResultList()
                    :
                    em.createQuery(birthdayQueryNarrow, PatientModel.class)
                        .setMaxResults(50)
                        .setParameter("fid", fid)
                        .setParameter("birthday", birthday)
                        .setParameter("ids", ids).getResultList();
            }
            case MEMO -> {
                final String memoQuery = "select p.karte.patient from PatientMemoModel p where p.karte.patient.facilityId = :fid and p.memo like :memo";
                final String memoQueryNarrow = memoQuery + " and p.karte.patient.id in (:ids)";

                String test = spec.getSearchText();
                StringBuilder memo = new StringBuilder();
                if (!test.startsWith("%")) {
                    memo.append("%");
                }
                memo.append(test);
                if (!test.endsWith("%")) {
                    memo.append("%");
                }

                ret = ids.isEmpty() ?
                    em.createQuery(memoQuery, PatientModel.class)
                        .setParameter("fid", fid)
                        .setParameter("memo", memo.toString()).getResultList()
                    :
                    em.createQuery(memoQueryNarrow, PatientModel.class)
                        .setParameter("fid", fid)
                        .setParameter("memo", memo.toString())
                        .setParameter("ids", ids).getResultList();
            }
            case FULLTEXT, QUERY, REGEXP -> {
                final String searchText = spec.getSearchText();
                final SearchSession searchSession = Search.session(em);

                List<DocumentModel> hits = searchSession.search(DocumentModel.class)
                    .where(f -> f.bool(b -> {
                        b.must(switch (spec.getType()) {
                            case QUERY -> f.simpleQueryString().field("modules.fullText").matching(searchText);
                            case REGEXP -> f.regexp().field("modules.fullText").matching(searchText);
                            default -> f.phrase().field("modules.fullText").matching(searchText);
                        });
                        if (!ids.isEmpty()) {
                            b.must(f.terms().field("karte.patient.id").matchingAny(ids));
                        }
                    })).fetchHits(1000);

                ret = hits.stream().map(dm -> dm.getKarte().getPatient()).distinct().toList();
            }
        }

        if (!ret.isEmpty()) {
            // pvt をまとめて取得する（高速化のため）
            List<PatientVisitModel> pvts = em.createQuery("select p from PatientVisitModel p "
                    + "where p.facilityId = :fid and p.status != :status and (p.patient in (:pts)) order by p.pvtDate desc", PatientVisitModel.class)
                    .setParameter("fid", fid)
                    .setParameter("pts", ret)
                    .setParameter("status", KarteState.CANCEL_PVT).getResultList();

            // まとめて取った pvt から最新の日付を PatientModel にセット
            for (PatientModel pm : ret) {
                for (PatientVisitModel pvt : pvts) {
                    // 最初にマッチした pvt が最新 (last visit)
                    if (pm.getId() == pvt.getPatient().getId()) {
                        pm.setLastVisit(pvt.getPvtDate());
                        break;
                    }
                }
            }
        }
        return ret;
    }

    /**
     * HealthInsurance の beanBytes を PVTHealthInsurance に戻して返す.
     *
     * @param patientPk PatientModel の pk
     * @return PVTHealthInsuranceModel の List
     */
    @Override
    public List<PVTHealthInsuranceModel> getHealthInsuranceList(Long patientPk) {
        final String sql = "select h from HealthInsuranceModel h where h.patient.id = :pk";
        List<HealthInsuranceModel> insurances = em.createQuery(sql, HealthInsuranceModel.class)
                .setParameter("pk", patientPk).getResultList();

        return ModelUtils.decodeHealthInsurance(insurances);
    }

    /**
     * 患者ID("000001")を指定して患者オブジェクトを返す.
     *
     * @param patientId 施設内患者ID
     * @return 該当するPatientModel
     */
    @Override
    public PatientModel getPatient(String patientId) {

        String facilityId = getCallersFacilityId();

        // 患者レコードは FacilityId と patientId で複合キーになっている
        PatientModel bean = em.createQuery("select p from PatientModel p where p.facilityId = :fid and p.patientId = :pid", PatientModel.class)
                .setParameter("fid", facilityId)
                .setParameter("pid", patientId).getSingleResult();

        long pk = bean.getId();

        // Lazy Fetch の 基本属性を検索する
        // 患者の健康保険を取得する
        List<HealthInsuranceModel> insurances = em.createQuery("select h from HealthInsuranceModel h where h.patient.id = :pk", HealthInsuranceModel.class)
                .setParameter("pk", pk).getResultList();

        bean.setHealthInsurances(insurances);
        return bean;
    }

    /**
     * 患者を登録する.
     *
     * @param patient PatientModel
     * @return PatientModel の primary key
     */
    @Override
    public Long addPatient(PatientModel patient) {
        String facilityId = getCallersFacilityId();
        patient.setFacilityId(facilityId);
        em.persist(patient);
        return patient.getId();
    }

    /**
     * 患者情報を更新する.
     *
     * @param patient 更新する患者
     * @return 更新数 1
     */
    @Override
    public int update(PatientModel patient) {
        em.merge(patient);
        return 1;
    }
}

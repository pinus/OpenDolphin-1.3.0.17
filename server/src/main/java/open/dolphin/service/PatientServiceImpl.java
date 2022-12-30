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
import java.util.stream.Collectors;

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

        switch (spec.getCode()) {

            case PatientSearchSpec.DATE_SEARCH:
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
                break;

            case PatientSearchSpec.ID_SEARCH:
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
                break;

            case PatientSearchSpec.NAME_SEARCH:
            case PatientSearchSpec.KANA_SEARCH:
            case PatientSearchSpec.ROMAN_SEARCH:
                final String nameQuery = "select p from PatientModel p where p.facilityId = :fid and "
                        + "(p.fullName like :name or p.kanaName like :name or p.romanName like :name)";
                final String nameQueryNarrow = nameQuery + " and p.id in (:ids)";

                String name = spec.getName();
                if (!name.endsWith("%")) {
                    name += "%";
                }

                ret = ids.isEmpty() ?
                        em.createQuery(nameQuery, PatientModel.class)
                                .setParameter("fid", fid)
                                .setParameter("name", name).getResultList()
                        :
                        em.createQuery(nameQueryNarrow, PatientModel.class)
                                .setParameter("fid", fid)
                                .setParameter("name", name)
                                .setParameter("ids", ids).getResultList();
                break;

            case PatientSearchSpec.TELEPHONE_SEARCH:
                final String telQuery = "select p from PatientModel p where p.facilityId = :fid and (p.telephone like :number or p.mobilePhone like :number)";
                final String telQueryNarrow = telQuery + " and p.id in (:ids)";

                String number = spec.getTelephone();
                if (!number.endsWith("%")) {
                    number += "%";
                }

                ret = ids.isEmpty() ?
                        em.createQuery(telQuery, PatientModel.class)
                                .setParameter("fid", fid)
                                .setParameter("number", number).getResultList()
                        :
                        em.createQuery(telQueryNarrow, PatientModel.class)
                                .setParameter("fid", fid)
                                .setParameter("number", number)
                                .setParameter("ids", ids).getResultList();
                break;

            case PatientSearchSpec.ZIPCODE_SEARCH:
                final String zipQuery = "select p from PatientModel p where p.facilityId = :fid and p.address.zipCode like :zipCode";
                final String zipQueryNarrow = zipQuery + " and p.id in (:ids)";

                String zipCode = spec.getZipCode();
                if (!zipCode.endsWith("%")) {
                    zipCode += "%";
                }

                ret = ids.isEmpty() ?
                        em.createQuery(zipQuery, PatientModel.class)
                                .setParameter("fid", fid)
                                .setParameter("zipCode", zipCode).getResultList()
                        :
                        em.createQuery(zipQueryNarrow, PatientModel.class)
                                .setParameter("fid", fid)
                                .setParameter("zipCode", zipCode)
                                .setParameter("ids", ids).getResultList();
                break;

            case PatientSearchSpec.ADDRESS_SEARCH:
                final String addressQuery = "select p from PatientModel p where p.facilityId = :fid and p.address.address like :address";
                final String addressQueryNarrow = addressQuery + " and p.id in (:ids)";

                String address = spec.getAddress();
                if (!address.endsWith("%")) {
                    address += "%";
                }

                ret = ids.isEmpty() ?
                        em.createQuery(addressQuery, PatientModel.class)
                                .setParameter("fid", fid)
                                .setParameter("address", address).getResultList()
                        :
                        em.createQuery(addressQueryNarrow, PatientModel.class)
                                .setParameter("fid", fid)
                                .setParameter("address", address)
                                .setParameter("ids", ids).getResultList();
                break;

            case PatientSearchSpec.EMAIL_SEARCH:
                final String emailQuery = "select p from PatientModel p where p.facilityId = :fid and p.email like :address";
                final String emailQueryNarrow = emailQuery + " and p.id in (:ids)";

                address = spec.getEmail();
                if (!address.endsWith("%")) {
                    address += "%";
                }

                ret = ids.isEmpty() ?
                        em.createQuery(emailQuery, PatientModel.class)
                                .setParameter("fid", fid)
                                .setParameter("email", address).getResultList()
                        :
                        em.createQuery(emailQueryNarrow, PatientModel.class)
                                .setParameter("fid", fid)
                                .setParameter("email", address)
                                .setParameter("ids", ids).getResultList();
                break;

            case PatientSearchSpec.BIRTHDAY_SEARCH:
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
                break;

            case PatientSearchSpec.MEMO_SEARCH:
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
                break;

            case PatientSearchSpec.FULL_TEXT_SEARCH:
                final SearchSession searchSession = Search.session(em);

                String searchText = spec.getSearchText();

                List<DocumentModel> hits = searchSession.search(DocumentModel.class)
                    .where(f -> f.bool(b -> {
                            b.must( f.match().field("modules.fullText").matching(searchText));
                            if (!ids.isEmpty()) {
                                b.must( f.terms().field("karte.patient.id").matchingAny(ids));
                            }
                        })).fetchHits(1000);

                ret = hits.stream().map(dm -> dm.getKarte().getPatient()).distinct().toList();

                break;
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

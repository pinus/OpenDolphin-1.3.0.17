package open.dolphin.service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import open.dolphin.dto.PatientVisitSpec;
import open.dolphin.dto.PvtStateSpec;
import open.dolphin.infomodel.*;
import org.apache.log4j.Logger;

/**
 * PvtServiceImpl.
 *
 * @author pns
 */
@Stateless
public class  PvtServiceImpl extends DolphinService implements PvtService {
    private static final long serialVersionUID = -3889943133781444449L;
    private final Logger logger = Logger.getLogger(PvtServiceImpl.class);

    /**
     * 患者来院情報を登録する.
     * @param pvt PatientVisitModel
     * @return 登録個数 1
     */
    @Override
    public int addPvt(PatientVisitModel pvt) {
        return addPvt(pvt, getCallersFacilityId());
    }

    /**
     * 患者来院情報を登録する.
     * PvtServer から呼ぶとき用
     * @param pvt PatientVisitModel
     * @param facilityId Facility ID
     * @return 登録個数 1
     */
    @Override
    public int addPvt(PatientVisitModel pvt, String facilityId) {
        //logger.info("PvtServiceImpl.addPvt called: patient id = " + pvt.getPatientId());

        pvt.setFacilityId(facilityId);

        PatientModel pvtPatient = pvt.getPatient();
        pvtPatient.setFacilityId(facilityId);

        PatientModel existPatient;
        // 既存の患者かどうか調べる
        try {
            // 存在しなければ catch に落ちる
            existPatient = em.createQuery("select p from PatientModel p where p.facilityId = :fid and p.patientId = :pid", PatientModel.class)
                .setParameter("fid", facilityId)
                .setParameter("pid", pvtPatient.getPatientId()).getSingleResult();

            // 健康保険情報を更新する
            Collection<HealthInsuranceModel> ins = pvtPatient.getHealthInsurances();

            if (ins != null && ! ins.isEmpty()) {

                // 健康保険を更新する
                List<HealthInsuranceModel> old = em.createQuery("select h from HealthInsuranceModel h where h.patient.id = :pk", HealthInsuranceModel.class)
                    .setParameter("pk", existPatient.getId()).getResultList();

                // 現在の保険情報を削除する
                old.forEach(em::remove);

                // 新しい健康保険情報を登録する
                pvtPatient.getHealthInsurances().forEach(m -> {
                    m.setPatient(existPatient);
                    em.persist(m);
                });
            }

            // 名前を更新する 2007-04-12
            existPatient.setFamilyName(pvtPatient.getFamilyName());
            existPatient.setGivenName(pvtPatient.getGivenName());
            existPatient.setFullName(pvtPatient.getFullName());
            existPatient.setKanaFamilyName(pvtPatient.getKanaFamilyName());
            existPatient.setKanaGivenName(pvtPatient.getKanaGivenName());
            existPatient.setKanaName(pvtPatient.getKanaName());
            existPatient.setRomanFamilyName(pvtPatient.getRomanFamilyName());
            existPatient.setRomanGivenName(pvtPatient.getRomanGivenName());
            existPatient.setRomanName(pvtPatient.getRomanName());

            // 性別
            existPatient.setGender(pvtPatient.getGender());
            existPatient.setGenderDesc(pvtPatient.getGenderDesc());
            existPatient.setGenderCodeSys(pvtPatient.getGenderCodeSys());

            // Birthday
            existPatient.setBirthday(pvtPatient.getBirthday());

            // 住所、電話を更新する
            existPatient.setAddress(pvtPatient.getAddress());
            existPatient.setTelephone(pvtPatient.getTelephone());
            //exist.setMobilePhone(patient.getMobilePhone());

            // 既存データを更新する
            em.merge(existPatient);

            // PatientVisit との関係を設定する
            pvt.setPatient(existPatient);
            // トータルの病名数をセット
            pvt.setByomeiCount(getByomeiCount(existPatient.getId()));
            // 今日の病名数をセット
            pvt.setByomeiCountToday(getByomeiCountToday(existPatient.getId()));

        } catch (NoResultException e) {
            // 新規患者であれば登録する
            // 患者属性は cascade=PERSIST で自動的に保存される
            em.persist(pvtPatient);

            // この患者のカルテを生成する
            KarteBean karte = new KarteBean();
            karte.setPatient(pvtPatient);
            karte.setCreated(new Date());
            em.persist(karte);
        }

        // 来院情報 pvt を登録する
        // CLAIM の仕様により患者情報のみを登録し、来院情報はない場合がある
        // それを pvtDate の属性で判断している

        // 同じ pvtDate の pvt がすでに登録されていないかどうかチェック
        // pvtDate が違えば，同じ patient id でも新たな pvt と判断する
        List<PatientVisitModel> result = em.createQuery(
            "select p from PatientVisitModel p where p.facilityId = :fid and p.patient.patientId = :pid and p.pvtDate = :date", PatientVisitModel.class)
            .setParameter("fid", pvt.getFacilityId())
            .setParameter("pid", pvt.getPatientId())
            .setParameter("date", pvt.getPvtDate())
            .getResultList();

        if (! result.isEmpty()) {
            // 重複がある場合は既存の id をコピーして新しい pvt にすげ替える
            PatientVisitModel exist = result.get(0);
            pvt.setId(exist.getId());
        }

        em.merge(pvt); // record がなければ persist 動作になる

        return 1;
    }

    /**
     * トータル病名数 - addPvt で使う.
     * @param patientPk PatientModel pk
     * @return 病名数
     */
    private int getByomeiCount(long patientPk) {
        return em.createQuery(
            "select r from RegisteredDiagnosisModel r where r.karte.id = (select k.id from KarteBean k where k.patient.id = :pk)", RegisteredDiagnosisModel.class)
            .setParameter("pk", patientPk)
            .getResultList().size();
    }

    /**
     * 今日の病名数 - addPvt で使う.
     * @param patientPk PatientModel pk
     * @return 病名数
     */
    private int getByomeiCountToday(long patientPk) {
        // 昨日の夜11時
        GregorianCalendar yesterday = new GregorianCalendar();
        yesterday.add(GregorianCalendar.DATE, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 23);

        return em.createQuery(
            "select r from RegisteredDiagnosisModel r where r.karte.id = (select k.id from KarteBean k where k.patient.id = :pk) and r.started >= :fromDate", RegisteredDiagnosisModel.class)
            .setParameter("pk", patientPk)
            .setParameter("fromDate", yesterday.getTime())
            .getResultList().size();
    }

    /**
     * 既存の pvt を update する.
     * @param pvt PatientVisitModel
     * @return アップデートした数 1
     */
    @Override
    public int updatePvt(PatientVisitModel pvt) {
        em.merge(pvt);
        return 1;
    }

    /**
     * 施設の患者来院情報を取得する.
     * @param spec 検索仕様 DTO オブジェクト
     * @return 来院情報の Collection
     */
    @Override
    public List<PatientVisitModel> getPvtList(PatientVisitSpec spec) {
        //System.out.println("getPvt start at " + new Date());

        String date = spec.getDate();
        if (!date.endsWith("%")) { date += "%"; }
        int index = date.indexOf('%');

        Date theDate = ModelUtils.getDateAsObject(date.substring(0, index));
        int firstResult = spec.getSkipCount();
        String fid = getCallersFacilityId();

        String appoDateFrom = spec.getAppodateFrom();
        String appoDateTo = spec.getAppodateTo();
        boolean searchAppo = (appoDateFrom != null && appoDateTo != null);

        // PatientVisitModel を施設 ID で検索する
        List<PatientVisitModel> result = em.createQuery(
            "select p from PatientVisitModel p where p.facilityId = :fid and p.pvtDate >= :date order by p.pvtDate", PatientVisitModel.class)
            .setFirstResult(firstResult)
            .setParameter("fid", fid)
            .setParameter("date", date).getResultList();

        // 患者の基本データを取得する
        // 来院情報と患者は ManyToOne の関係である
        if (searchAppo) {
            result.forEach(pvt -> {
                PatientModel patient = pvt.getPatient();
                List<AppointmentModel> c = em.createQuery(
                        "select a from AppointmentModel a where a.date = :date and a.karte.id = (select k.id from KarteBean k where k.patient.id = :pk)", AppointmentModel.class)
                        .setParameter("pk", patient.getId())
                        .setParameter("date", theDate).getResultList();
                if (! c.isEmpty()) {
                    AppointmentModel appo = c.get(0);
                    pvt.setAppointment(appo.getName());
                }
            });
        }
        //System.out.println("getPvt end at " + new Date());
        return result;
    }

    /**
     * 受付情報を削除する.
     * @param id PatientVisitModel の primary key
     * @return 削除件数
     */
    @Override
    public int removePvt(Long id) {
        try {
            PatientVisitModel exist = em.find(PatientVisitModel.class, id);
            em.remove(exist);
            return 1;
        } catch (IllegalArgumentException e) {
        }
        return 0;
    }

    /**
     * pvt state だけスピーディーにとってくる.
     * @return State 番号のリスト
     */
    @Override
    public List<PvtStateSpec> getPvtStateList() {

        String fid = getCallersFacilityId();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<PatientVisitModel> result = em.createQuery(
            "select p from PatientVisitModel p where p.facilityId = :fid and p.pvtDate >= :date order by p.pvtDate", PatientVisitModel.class)
            .setParameter("fid", fid)
            .setParameter("date", date).getResultList();

        return result.stream().map(pvt -> {
            PvtStateSpec spec = new PvtStateSpec();
            spec.setPk(pvt.getId());
            spec.setState(pvt.getState());
            spec.setByomeiCount(pvt.getByomeiCount());
            spec.setByomeiCountToday(pvt.getByomeiCountToday());
            return spec;
        }).collect(Collectors.toList());
    }
    /**
     * pk の PatientVisitModel の state を取ってくる.
     * @param pk PatientVisitModel の pk
     * @return state
     */
    @Override
    public int getPvtState(Long pk) {
        PatientVisitModel exist = em.find(PatientVisitModel.class, pk);
        return exist.getState();
    }

    /**
     * 引数の PatientModel をもつ今日の PatientVisitModel があれば取ってくる.
     * @param patient PatientModel
     * @return みつかった PatientVisitModel. ないばあいは null.
     */
    @Override
    public PatientVisitModel getPvtOf(PatientModel patient) {
        String fid = getCallersFacilityId();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<PatientVisitModel> result = em.createQuery(
            "select p from PatientVisitModel p where p.facilityId = :fid and p.pvtDate >= :date and p.patient = :patient", PatientVisitModel.class)
            .setParameter("fid", fid)
            .setParameter("date", date)
            .setParameter("patient", patient).getResultList();

        return result.isEmpty()? null : result.get(0);
    }
}

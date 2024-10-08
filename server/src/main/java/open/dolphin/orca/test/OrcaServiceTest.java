package open.dolphin.orca.test;

import open.dolphin.dto.ApiResult;
import open.dolphin.dto.DiagnosisSearchSpec;
import open.dolphin.dto.OrcaEntry;
import open.dolphin.dto.PatientVisitSpec;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.orca.orcadao.bean.OnshiKenshin;
import open.dolphin.orca.orcadao.bean.OnshiYakuzai;
import open.dolphin.orca.orcadao.bean.Syskanri;
import open.dolphin.orca.orcadao.bean.Wksryact;
import open.dolphin.service.OrcaServiceApi;
import open.dolphin.service.OrcaServiceDao;
import open.dolphin.util.JsonUtils;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrcaServiceTest {

    private long lap;

    public static void main(String[] argv) throws ReflectiveOperationException {
        String userDir = System.getProperty("user.dir");
        System.setProperty("jboss.server.base.dir", userDir);
        OrcaServiceTest test = new OrcaServiceTest();
        test.executeTest();
    }

    // api 316 msec, dao 42 msec
    private void getWksryact(Object orcaService) throws ReflectiveOperationException {
        PatientVisitSpec spec = new PatientVisitSpec();
        spec.setPatientId("000001");
        spec.setDate("2019-02-16");
        Wksryact wksryact = (Wksryact) invoke(orcaService, "getWksryact", spec);

        System.out.println("Medical_Uid = " + wksryact.getMedicalUid());
        System.out.println("Medical_Mode = " + wksryact.getMedicalMode());
        System.out.println("Medical_Mode2 = " + wksryact.getMedicalMode2());

        Boolean exist = (Boolean) invoke(orcaService, "existsOrcaWorkingData", "000001");
        System.out.println("中途データあり? " + exist);
    }

    // api 288 msec, dao 23 msec
    private void getSyskanri(Object orcaService) throws ReflectiveOperationException {
        List<Syskanri> syskanri = (List<Syskanri>) invoke(orcaService, "getSyskanri", null);
        syskanri.stream().filter(spec -> Objects.nonNull(spec.getCode())).forEach(spec -> {
            System.out.println(String.join(",", spec.getCode(), spec.getWholeName(), spec.getKanaName()));
        });
    }

    // only dao 709 msec
    private void findTensu(Object orcaService) throws ReflectiveOperationException {
        List<OrcaEntry> tensu = (List<OrcaEntry>) invoke(orcaService, "findTensu", "リンデロン");
        tensu.stream().forEach(spec -> {
            System.out.println(String.join(",", spec.getCode(), spec.getName()));
        });
    }

    // only dao 128 msec
    private void findDiagnosis(Object orcaService) throws ReflectiveOperationException {
        List<OrcaEntry> rds = (List<OrcaEntry>) invoke(orcaService, "findDiagnosisByKeyword", "多形");
        rds.stream().forEach(rd -> {
            System.out.println(String.join(",", rd.getCode(), rd.getName(), rd.getIcd10()));
        });

        List<String> codes = new ArrayList<>();
        codes.add("ZZZ5110");
        codes.add("8842799");
        codes.add("7018006");
        codes.add("ZZZ5109");
        codes.add("9490004");
        rds = (List<OrcaEntry>) invoke(orcaService, "findDiagnosisByKeyword", codes);
        rds.stream().forEach(rd -> {
            System.out.println(String.join(",", rd.getCode(), rd.getName(), rd.getIcd10()));
        });

        List<String> ikou = (List<String>) invoke(orcaService, "findIkouByomei", codes);
        System.out.println("移行病名");
        ikou.stream().forEach(System.out::println);
    }

    // api+dao 592 msec, dao 75 msec
    private void getOrcaInputCdList(Object orcaService) throws ReflectiveOperationException {
        // getOrcaInputCdList は DAO のみ
        List<ModuleInfoBean> stampInfo = (List<ModuleInfoBean>) invoke(new OrcaServiceDao(), "getOrcaInputCdList", null);
        for (int i = 0; i < stampInfo.size(); i++) {
            //System.out.println(i + ":" + stampInfo.get(i).getStampId());
        }

        // getStamp
        List<ModuleModel> m = (List<ModuleModel>) invoke(orcaService, "getStamp", stampInfo.get(20));
        m.forEach(module -> {
            System.out.println(JsonUtils.toJson(module));
        });
    }

    // api 377 msec, dao 53 msec
    private void getOrcaDisease(Object orcaService) throws ReflectiveOperationException {
        DiagnosisSearchSpec spec = new DiagnosisSearchSpec();
        spec.setPatientId("000001");
        //spec.setToDate();
        spec.setFromDate(new GregorianCalendar(2008, Calendar.FEBRUARY, 1).getTime());
        List<RegisteredDiagnosisModel> rds = (List<RegisteredDiagnosisModel>) invoke(orcaService, "getOrcaDisease", spec);
        for (RegisteredDiagnosisModel rd : rds) {
            System.out.println(String.join(" : ", rd.getDiagnosis(), rd.getDiagnosisCode(), rd.getStartDate(), rd.getEndDate(), rd.getDiagnosisOutcomeModel().getOutcomeDesc()));
        }
    }

    private void sendDoc(Object orcaService) {
        DocumentModel doc = SampleDocument.getDocumentModel("SampleDocumentModel.json");
        ApiResult result = ((OrcaServiceApi) orcaService).sendDocument(doc);

        System.out.println(JsonUtils.toJson(result));
    }

    private void sendDiag(Object orcaService) {
        List<RegisteredDiagnosisModel> diagnoses = SampleDocument.getRegisteredDiagnosisModel("SampleRegisteredDiagnosisModels.json");
        ApiResult result = ((OrcaServiceApi) orcaService).sendDiagnoses(diagnoses);

        System.out.println(JsonUtils.toJson(result));
    }

    private void getDrugHistory(OrcaServiceDao orcaService) throws ReflectiveOperationException {
        // 040910 40896 040123 039328 038030 036311 034890 034889 034374 031015 023148 022024 011832
        List<OnshiYakuzai> onshiYakuzai = (List<OnshiYakuzai>) invoke(orcaService, "getDrugHistory", "040123");
        //List<OnshiYakuzai> onshiYakuzai = (List<OnshiYakuzai>) invoke(orcaService, "getDrugHistory", "008617");
        //List<OnshiYakuzai> onshiYakuzai = (List<OnshiYakuzai>) invoke(orcaService, "getDrugHistory", "012773");

        StringBuilder sb = new StringBuilder();

        // 日付順
        sb.append("日付順\n");
        String date = "";
        String prevLabel = "";

        for (OnshiYakuzai o : onshiYakuzai) {
            String hosp = o.getHosp().getFacilityName();
            String chozai = o.getChozai().getFacilityName();
            if (o.getHosp().isPharmacy()) {
                chozai = o.getHosp().getFacilityName();
                hosp = o.getChozai().getFacilityName();
            }
            String label = hosp + "／" + chozai;

            if (!date.equals(o.getIsoDate()) || !prevLabel.equals(label)) {
                date = o.getIsoDate();
                prevLabel = label;
                sb.append(String.format("\n%s %s\n", date, label));
            }

            String yakuzainame = o.getYakuzainame();
            sb.append(String.format("    %s ", yakuzainame));
            String suryo = Float.toString(o.getSuryo()).replaceAll(".0$", "");
            if (o.getYohoname().equals("")) {
                // 外用剤
                sb.append(String.format("%s%s %s\n", suryo, o.getTaniname(), o.getShiji()));
            } else {
                sb.append(String.format("%s%s %s %s日分\n", suryo, o.getTaniname(), o.getYohoname(), o.getKaisu()));
            }
        }

        System.out.println(sb.toString());
    }

    private void hasDrugHistory(OrcaServiceDao orcaService) {
        boolean hasDrugHistory = orcaService.hasDrugHistory("037145");
        System.out.println("has drug history = " + hasDrugHistory);
    }

    private void getKenshin(OrcaServiceDao orcaService) {
        List<OnshiKenshin> kenshin = orcaService.getKenshin("037145");

        StringBuilder sb = new StringBuilder();
        String date = "";

        for (OnshiKenshin k : kenshin) {
            if (!date.equals(k.getIsoDate())) {
                date = k.getIsoDate();
                sb.append(date); sb.append("\n");
            }
            String v = k.getDataValue();
            switch (k.getKomokuname()) {
                case "尿糖":
                case "尿蛋白":
                    k.setDataValue(v.equals("1")? "-" : v.equals("2")? "±" : v.equals("3")? "+" : v.equals("4")? "++" : "+++");
                case "既往歴":
                case "自覚症状":
                case "他覚症状":
                    k.setDataValue(v.equals("1")? "特記すべきことあり" : "特記すべきことなし");
                case "メタボリックシンドローム判定":
                    k.setDataValue(v.equals("1")? "基準該当" : v.equals("2")? "予備軍該当" : v.equals("3")? "非該当" : "判定不能");
                case "服薬1(血圧)":
                case "服薬2(血糖)":
                case "服薬3(脂質)":
                    k.setDataValue(v.equals("1")? "服薬あり" : "服薬なし");
                case "喫煙":
                    k.setDataValue(v.equals("1")? "はい" : "いいえ");
            }

            sb.append(String.format("%s %s %s\n", k.getKomokuname(), k.getDataValue(), k.getDataTani()));
        }
        System.out.println(sb.toString());
    }

    private void hasKenshin(OrcaServiceDao orcaService) {
        boolean hasKenshin = orcaService.hasKenshin("037145");
        System.out.println("has kensihin = " + hasKenshin);
    }

    private void getChokisenteikbn(OrcaServiceDao orcaService) {
        int hirudoido = orcaService.getChokisenteikbn("620009046");
        System.out.println("chokisenteikbn = " + hirudoido);
        int heparin = orcaService.getChokisenteikbn("622332500");
        System.out.println("chokisenteikbn = " + heparin);
    }

    private void executeTest() throws ReflectiveOperationException {
        OrcaServiceApi api = new OrcaServiceApi();
        OrcaServiceDao dao = new OrcaServiceDao();

        lap = System.currentTimeMillis();

        getChokisenteikbn(dao);

        lap = showLap(lap);
    }

    private Object invoke(Object obj, String methodName, Object param) throws ReflectiveOperationException {
        if (param == null) {
            return obj.getClass().getMethod(methodName).invoke(obj);
        } else {
            Class paramClass = (param instanceof ArrayList) ? List.class : param.getClass();
            Method m = obj.getClass().getMethod(methodName, paramClass);
            return m.invoke(obj, param);
        }
    }

    private long showLap(long t) {
        System.out.println("lap = " + (System.currentTimeMillis() - t) + " msec");
        return System.currentTimeMillis();
    }
}

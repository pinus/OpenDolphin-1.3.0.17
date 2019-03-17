package open.dolphin.orca;

import open.dolphin.infomodel.IInfoModel;

import java.util.HashMap;

public class ClaimConst {

    public static final String DISEASE_MASTER_TABLE_ID = "ICD10_2001-10-03MEDIS";
    public static final String SUBCLASS_CODE_ID = "Claim003";    // 手技、材料、薬剤区分テーブルID
    public static final String NUMBER_CODE_ID = "Claim004";    // 数量コードテーブルID
    public static final String CLASS_CODE_ID = "Claim007";    // 診療行為区分テーブルID

    // Claim 003　診療種別区分
    public static final int SYUGI = 0; // 手技
    public static final int ZAIRYO = 1; // 材料
    public static final int YAKUZAI = 2; // 薬剤
    public static final int ADMIN = 3; // 用法

    // Claim 004　数量コード
    public static final String YAKUZAI_TOYORYO = "10";   // 薬剤投与量
    public static final String YAKUZAI_TOYORYO_1KAI = "11";    // 薬剤投与量１回
    public static final String YAKUZAI_TOYORYO_1NICHI = "12";    // 薬剤投与量１日
    public static final String ZAIRYO_KOSU = "21";    // 材料個数

    // Claim 007 レセ電診療行為区分コード＝ ORCA の srysyukbn 診療種別区分
    public static final String RECEIPT_CODE_NAIYO = "210"; // 内服
    public static final String RECEIPT_CODE_TONYO = "220"; // 頓服
    public static final String RECEIPT_CODE_GAIYO = "230"; // 外用
    public static final String INJECTION_310 = "310"; // 皮下筋注
    public static final String INJECTION_320 = "320"; // 静注
    public static final String INJECTION_330 = "330"; // その他注射
    public static final String INJECTION_311 = "311";
    public static final String INJECTION_321 = "321";
    public static final String INJECTION_331 = "331";

    // ORCA TBL_TENSU の srycd（9桁のコード） の頭番号
    public static final String ADMIN_CODE_START = "001"; // 用法，部位(0010008)，コメント(0010000)
    public static final String RBUI_CODE_START = "002"; // 放射線部位
    public static final String SYUGI_CODE_START = "1"; // 手技 -- この中に ORCA での診療行為区分 srysyukbn がある
    public static final String YAKUZAI_CODE_START = "6"; // 薬剤
    public static final String ZAIRYO_CODE_START = "7"; // 材料
    public static final String COMMENT_CODE_START = "8"; // コメントコード

    // ORCA TBL_TENSU の薬剤区分番号(ykzkbn)
    public static final String YKZ_KBN_NAIYO = "1"; // 内服薬 code 61〜
    public static final String YKZ_KBN_INJECTION = "4"; // 注射薬 code 64〜
    public static final String YKZ_KBN_GAIYO = "6"; // 外用薬 code 66〜

    // 院内処方/院外処方
    public static final String IN_MEDICINE = "院内処方";
    public static final String EXT_MEDICINE = "院外処方";

    /**
     * entity をキーとした entity の名称マップ.
     */
    public static final HashMap<String, String> EntityNameMap = new HashMap<>();
    /**
     * entity をキーとした，SearchCode（srysyukbn 番号範囲）のマップ.
     */
    public static final HashMap<String, String> SearchCodeMap = new HashMap<>();
    /**
     * entity をキーとした，ClaimClassCode (Claim 007) のマップ.
     */
    public static final HashMap<String, String> ClaimClassCodeMap = new HashMap<>();
    /**
     * 診療種別区分 (srysyukbn) のマップ.
     */
    private static final HashMap<String, String> SrysyukbnMap = new HashMap<>();
    /**
     * 診療科名をキーとした診療科コードマップ.
     */
    private static final HashMap<String, String> DepartmentCodeMap = new HashMap<>();
    /**
     * 詳記区分. see {@link open.dolphin.orca.orcaapi.bean.Subjectivesmodreq Subjectivesmodreq}
     */
    public static HashMap<String, String> SubjectivesCodeMap = new HashMap<>();

    static {
        EntityNameMap.put(IInfoModel.ENTITY_BACTERIA_ORDER, IInfoModel.TABNAME_BACTERIA);
        EntityNameMap.put(IInfoModel.ENTITY_BASE_CHARGE_ORDER, IInfoModel.TABNAME_BASE_CHARGE);
        EntityNameMap.put(IInfoModel.ENTITY_DIAGNOSIS, IInfoModel.TABNAME_DIAGNOSIS);
        EntityNameMap.put(IInfoModel.ENTITY_GENERAL_ORDER, IInfoModel.TABNAME_GENERAL);
        EntityNameMap.put(IInfoModel.ENTITY_INJECTION_ORDER, IInfoModel.TABNAME_INJECTION);
        EntityNameMap.put(IInfoModel.ENTITY_INSTRACTION_CHARGE_ORDER, IInfoModel.TABNAME_INSTRACTION);
        EntityNameMap.put(IInfoModel.ENTITY_MED_ORDER, IInfoModel.TABNAME_MED);
        EntityNameMap.put(IInfoModel.ENTITY_OTHER_ORDER, IInfoModel.TABNAME_OTHER);
        EntityNameMap.put(IInfoModel.ENTITY_PHYSIOLOGY_ORDER, IInfoModel.TABNAME_PHYSIOLOGY);
        EntityNameMap.put(IInfoModel.ENTITY_RADIOLOGY_ORDER, IInfoModel.TABNAME_RADIOLOGY);
        EntityNameMap.put(IInfoModel.ENTITY_SURGERY_ORDER, IInfoModel.TABNAME_SURGERY);
        EntityNameMap.put(IInfoModel.ENTITY_LABO_TEST, IInfoModel.TABNAME_LABO);
        EntityNameMap.put(IInfoModel.ENTITY_TEXT, IInfoModel.TABNAME_TEXT);
        EntityNameMap.put(IInfoModel.ENTITY_TREATMENT, IInfoModel.TABNAME_TREATMENT);
    }

    static {
        SearchCodeMap.put(IInfoModel.ENTITY_BACTERIA_ORDER, "600-699");
        SearchCodeMap.put(IInfoModel.ENTITY_BASE_CHARGE_ORDER, "110-125");
        SearchCodeMap.put(IInfoModel.ENTITY_DIAGNOSIS, null);
        SearchCodeMap.put(IInfoModel.ENTITY_GENERAL_ORDER, "100-999");
        SearchCodeMap.put(IInfoModel.ENTITY_INJECTION_ORDER, "300-331");
        SearchCodeMap.put(IInfoModel.ENTITY_INSTRACTION_CHARGE_ORDER, "130-140");
        SearchCodeMap.put(IInfoModel.ENTITY_MED_ORDER, "200-299");
        SearchCodeMap.put(IInfoModel.ENTITY_OTHER_ORDER, "800-899");
        SearchCodeMap.put(IInfoModel.ENTITY_PHYSIOLOGY_ORDER, "600-699");
        SearchCodeMap.put(IInfoModel.ENTITY_RADIOLOGY_ORDER, "700-799");
        SearchCodeMap.put(IInfoModel.ENTITY_SURGERY_ORDER, "500-599");
        SearchCodeMap.put(IInfoModel.ENTITY_LABO_TEST, "600-699");
        SearchCodeMap.put(IInfoModel.ENTITY_TEXT, null);
        SearchCodeMap.put(IInfoModel.ENTITY_TREATMENT, "400-499");
    }

    static {
        ClaimClassCodeMap.put(IInfoModel.ENTITY_BACTERIA_ORDER, "600");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_BASE_CHARGE_ORDER, null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_DIAGNOSIS, null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_GENERAL_ORDER, null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_INJECTION_ORDER, null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_INSTRACTION_CHARGE_ORDER, null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_MED_ORDER, null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_OTHER_ORDER, "800");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_PHYSIOLOGY_ORDER, "600");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_RADIOLOGY_ORDER, "700");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_SURGERY_ORDER, "500");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_LABO_TEST, "600");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_TEXT, null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_TREATMENT, "400");
    }

    static {
        SrysyukbnMap.put("110", "初診");
        SrysyukbnMap.put("120", "再診(再診)");
        SrysyukbnMap.put("122", "再診(外来管理加算)");
        SrysyukbnMap.put("123", "再診(時間外)");
        SrysyukbnMap.put("124", "再診(休日)");
        SrysyukbnMap.put("125", "再診(深夜)");
        SrysyukbnMap.put("130", "指導");
        SrysyukbnMap.put("140", "在宅");
        SrysyukbnMap.put("210", "投薬(内服・頓服・調剤)(入院外)");
        SrysyukbnMap.put("211", "投薬(内服・頓服・調剤)(院内)");
        SrysyukbnMap.put("212", "投薬(内服・頓服・調剤)(院外)");
        SrysyukbnMap.put("230", "投薬(外用・調剤)(入院外)");
        SrysyukbnMap.put("231", "投薬(外用・調剤)(院内)");
        SrysyukbnMap.put("232", "投薬(外用・調剤)(院外)");
        SrysyukbnMap.put("240", "投薬(調剤)(入院)");
        SrysyukbnMap.put("250", "投薬(処方)");
        SrysyukbnMap.put("260", "投薬(麻毒)");
        SrysyukbnMap.put("270", "投薬(調基)");
        SrysyukbnMap.put("300", "注射(生物学的製剤・精密持続点滴・麻薬)");
        SrysyukbnMap.put("310", "注射(皮下筋肉内)");
        SrysyukbnMap.put("320", "注射(静脈内)");
        SrysyukbnMap.put("330", "注射(その他)");
        SrysyukbnMap.put("311", "注射(皮下筋肉内)");
        SrysyukbnMap.put("321", "注射(静脈内)");
        SrysyukbnMap.put("331", "注射(その他)");
        SrysyukbnMap.put("400", "処置");
        SrysyukbnMap.put("500", "手術(手術)");
        SrysyukbnMap.put("502", "手術(輸血)");
        SrysyukbnMap.put("503", "手術(ギプス)");
        SrysyukbnMap.put("540", "麻酔");
        SrysyukbnMap.put("600", "検査");
        SrysyukbnMap.put("700", "画像診断");
        SrysyukbnMap.put("800", "その他");
        SrysyukbnMap.put("903", "入院(入院料)");
        SrysyukbnMap.put("906", "入院(外泊)");
        SrysyukbnMap.put("910", "入院(入院時医学管理料)");
        SrysyukbnMap.put("920", "入院(特定入院料・その他)");
        SrysyukbnMap.put("970", "入院(食事療養)");
        SrysyukbnMap.put("971", "入院(標準負担額)");
        SrysyukbnMap.put("980", "コメント（処方せん備考）");
        SrysyukbnMap.put("990", "コメント");
        SrysyukbnMap.put("991", "コメント（摘要欄下部表示）");
    }

    static {
        DepartmentCodeMap.put("内科", "01");
        DepartmentCodeMap.put("精神科", "02");
        DepartmentCodeMap.put("神経科", "03");
        DepartmentCodeMap.put("神経内科", "04");
        DepartmentCodeMap.put("呼吸器科", "05");
        DepartmentCodeMap.put("消化器科", "06");
        DepartmentCodeMap.put("胃腸科", "07");
        DepartmentCodeMap.put("循環器科", "08");
        DepartmentCodeMap.put("小児科", "09");
        DepartmentCodeMap.put("外科", "10");
        DepartmentCodeMap.put("整形外科", "11");
        DepartmentCodeMap.put("形成外科", "12");
        DepartmentCodeMap.put("美容外科", "13");
        DepartmentCodeMap.put("脳神経外科", "14");
        DepartmentCodeMap.put("呼吸器外科", "15");
        DepartmentCodeMap.put("心臓血管外科", "16");
        DepartmentCodeMap.put("小児外科", "17");
        DepartmentCodeMap.put("皮膚ひ尿器科", "18");
        DepartmentCodeMap.put("皮膚科", "19");
        DepartmentCodeMap.put("ひ尿器科", "20");
        DepartmentCodeMap.put("泌尿器", "20");
        DepartmentCodeMap.put("性病科", "21");
        DepartmentCodeMap.put("こう門科", "22");
        DepartmentCodeMap.put("産婦人科", "23");
        DepartmentCodeMap.put("産科", "24");
        DepartmentCodeMap.put("婦人科", "25");
        DepartmentCodeMap.put("眼科", "26");
        DepartmentCodeMap.put("耳鼻いんこう科", "27");
        DepartmentCodeMap.put("気管食道科", "28");
        DepartmentCodeMap.put("理学診療科", "29");
        DepartmentCodeMap.put("放射線科", "30");
        DepartmentCodeMap.put("麻酔科", "31");
        DepartmentCodeMap.put("人工透析科", "32");
        DepartmentCodeMap.put("心療内科", "33");
        DepartmentCodeMap.put("アレルギー", "34");
        DepartmentCodeMap.put("リウマチ ", "35");
        DepartmentCodeMap.put("リハビリ", "36");
        DepartmentCodeMap.put("鍼灸", "A1");
    }

    static {
        SubjectivesCodeMap.put("主たる疾患の臨床症状", "01");
        SubjectivesCodeMap.put("主たる疾患の診療・検査所見", "02");
        SubjectivesCodeMap.put("主な治療行為の必要性", "03");
        SubjectivesCodeMap.put("主な治療行為の経過", "04");
        SubjectivesCodeMap.put("治験概要", "50");
        SubjectivesCodeMap.put("上記以外", "90");
        SubjectivesCodeMap.put("レセプト摘要欄コメント", "99");
        SubjectivesCodeMap.put("労災レセプト\"傷病の経過\"", "AA");
        SubjectivesCodeMap.put("自賠責(第三者行為)レセプト摘要欄コメント", "BB");
    }

    /**
     * srysyukbn の属している entity を返す.
     *
     * @param code srysyukbn 210, 600, etc
     * @return ENTITY_*
     */
    public static String getEntity(String code) {
        for (String entity : SearchCodeMap.keySet()) {
            String val = SearchCodeMap.get(entity);
            if (val == null
                    || entity.equals(IInfoModel.ENTITY_GENERAL_ORDER)) {
                continue;
            }

            String[] range = val.split("-");
            int min = Integer.parseInt(range[0]);
            int max = Integer.parseInt(range[1]);
            int kbn = Integer.parseInt(code);
            if (min <= kbn && kbn <= max) {
                return entity;
            }
        }
        // どれにも属さないものは General とする
        return IInfoModel.ENTITY_GENERAL_ORDER;
    }

    /**
     * 診療種別区分コードの名称を返す.
     *
     * @param srysyukbn 診療種別区分
     * @return 診療種別区分名
     */
    public static String getSrysyukbnName(String srysyukbn) {
        return SrysyukbnMap.get(srysyukbn);
    }

    /**
     * 診療科名のコードを返す.
     *
     * @param departmentName 診療科名
     * @return 診療科コード
     */
    public static String getDepartmentCode(String departmentName) {
        return DepartmentCodeMap.get(departmentName);
    }

    public static void main(String[] argv) {
        String entity = getEntity("145");
        System.out.println("entity=" + entity + " name=" + EntityNameMap.get(entity));
    }
}

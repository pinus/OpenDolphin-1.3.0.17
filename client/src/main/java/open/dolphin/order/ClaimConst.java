package open.dolphin.order;

import open.dolphin.infomodel.IInfoModel;

import java.util.HashMap;

public class ClaimConst {

    /**
     * entity をキーとした entity の名称マップ
     */
    public static final HashMap<String, String> EntityNameMap;
    static {
        EntityNameMap = new HashMap<String, String>();
        EntityNameMap.put(IInfoModel.ENTITY_BACTERIA_ORDER,            IInfoModel.TABNAME_BACTERIA);
        EntityNameMap.put(IInfoModel.ENTITY_BASE_CHARGE_ORDER,         IInfoModel.TABNAME_BASE_CHARGE);
        EntityNameMap.put(IInfoModel.ENTITY_DIAGNOSIS,                 IInfoModel.TABNAME_DIAGNOSIS);
        EntityNameMap.put(IInfoModel.ENTITY_GENERAL_ORDER,             IInfoModel.TABNAME_GENERAL);
        EntityNameMap.put(IInfoModel.ENTITY_INJECTION_ORDER,           IInfoModel.TABNAME_INJECTION);
        EntityNameMap.put(IInfoModel.ENTITY_INSTRACTION_CHARGE_ORDER,  IInfoModel.TABNAME_INSTRACTION);
        EntityNameMap.put(IInfoModel.ENTITY_MED_ORDER,                 IInfoModel.TABNAME_MED);
        EntityNameMap.put(IInfoModel.ENTITY_OTHER_ORDER,               IInfoModel.TABNAME_OTHER);
        EntityNameMap.put(IInfoModel.ENTITY_PHYSIOLOGY_ORDER,          IInfoModel.TABNAME_PHYSIOLOGY);
        EntityNameMap.put(IInfoModel.ENTITY_RADIOLOGY_ORDER,           IInfoModel.TABNAME_RADIOLOGY);
        EntityNameMap.put(IInfoModel.ENTITY_SURGERY_ORDER,             IInfoModel.TABNAME_SURGERY);
        EntityNameMap.put(IInfoModel.ENTITY_LABO_TEST,                 IInfoModel.TABNAME_LABO);
        EntityNameMap.put(IInfoModel.ENTITY_TEXT,                      IInfoModel.TABNAME_TEXT);
        EntityNameMap.put(IInfoModel.ENTITY_TREATMENT,                 IInfoModel.TABNAME_TREATMENT);
    }

    /**
     * entity をキーとした，SearchCode（srysyukbn 番号範囲）のマップ
     */
    public static final HashMap<String, String> SearchCodeMap;
    static {
        SearchCodeMap = new HashMap<String, String>();
        SearchCodeMap.put(IInfoModel.ENTITY_BACTERIA_ORDER,            "600-699");
        SearchCodeMap.put(IInfoModel.ENTITY_BASE_CHARGE_ORDER,         "110-125");
        SearchCodeMap.put(IInfoModel.ENTITY_DIAGNOSIS,                      null);
        SearchCodeMap.put(IInfoModel.ENTITY_GENERAL_ORDER,             "100-999");
        SearchCodeMap.put(IInfoModel.ENTITY_INJECTION_ORDER,           "300-331");
        SearchCodeMap.put(IInfoModel.ENTITY_INSTRACTION_CHARGE_ORDER,  "130-140");
        SearchCodeMap.put(IInfoModel.ENTITY_MED_ORDER,                 "200-299");
        SearchCodeMap.put(IInfoModel.ENTITY_OTHER_ORDER,               "800-899");
        SearchCodeMap.put(IInfoModel.ENTITY_PHYSIOLOGY_ORDER,          "600-699");
        SearchCodeMap.put(IInfoModel.ENTITY_RADIOLOGY_ORDER,           "700-799");
        SearchCodeMap.put(IInfoModel.ENTITY_SURGERY_ORDER,             "500-599");
        SearchCodeMap.put(IInfoModel.ENTITY_LABO_TEST,                 "600-699");
        SearchCodeMap.put(IInfoModel.ENTITY_TEXT,                           null);
        SearchCodeMap.put(IInfoModel.ENTITY_TREATMENT,                 "400-499");
    }

    /**
     * srysyukbn の属している entity を返す
     * @param code
     * @return
     */
    public static String getEntity(String code) {
        for(String entity : SearchCodeMap.keySet()) {
            String val = SearchCodeMap.get(entity);
            if (val == null
                    || entity.equals(IInfoModel.ENTITY_GENERAL_ORDER)) continue;

            String[] range = val.split("-");
            int min = Integer.parseInt(range[0]);
            int max = Integer.parseInt(range[1]);
            int kbn = Integer.parseInt(code);
            if (min <= kbn && kbn <= max) return entity;
        }
        // どれにも属さないものは General とする
        return IInfoModel.ENTITY_GENERAL_ORDER;
    }

    /**
     * entity をキーとした，ClaimClassCode (Claim 007) のマップ
     */
    public static final HashMap<String, String> ClaimClassCodeMap;
    static {
        ClaimClassCodeMap = new HashMap<String, String>();
        ClaimClassCodeMap.put(IInfoModel.ENTITY_BACTERIA_ORDER,            "600");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_BASE_CHARGE_ORDER,          null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_DIAGNOSIS,                  null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_GENERAL_ORDER,              null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_INJECTION_ORDER,            null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_INSTRACTION_CHARGE_ORDER,   null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_MED_ORDER,                  null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_OTHER_ORDER,               "800");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_PHYSIOLOGY_ORDER,          "600");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_RADIOLOGY_ORDER,           "700");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_SURGERY_ORDER,             "500");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_LABO_TEST,                 "600");
        ClaimClassCodeMap.put(IInfoModel.ENTITY_TEXT,                       null);
        ClaimClassCodeMap.put(IInfoModel.ENTITY_TREATMENT,                 "400");
    }

    public enum MasterSet {

        DIAGNOSIS("disease", "傷病名"),
        TREATMENT("treatment", "診療行為"),
        MEDICAL_SUPPLY("medicine", "内用・外用薬"),
        ADMINISTRATION("admin", "用法"),
        INJECTION_MEDICINE("medicine", "注射薬"),
        TOOL_MATERIAL("tool_material", "特定器材");
        private String name;
        private String dispName;

        MasterSet(String name, String dispName) {
            setName(name);
            setDispName(dispName);
        }

        public String getDispName() {
            return dispName;
        }

        public void setDispName(String dispName) {
            this.dispName = dispName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public static final String DISEASE_MASTER_TABLE_ID = "ICD10_2001-10-03MEDIS";
    public static final String MASTER_FLAG_MEDICICE = "20";     // 薬剤の searchClass (これ以外は srysyukbn の範囲）
    public static final String MASTER_FLAG_INJECTION = "40";
    public static final String SUBCLASS_CODE_ID = "Claim003";	// 手技、材料、薬剤区分テーブルID
    public static final String NUMBER_CODE_ID = "Claim004";	// 数量コードテーブルID
    public static final String CLASS_CODE_ID = "Claim007";	// 診療行為区分テーブルID

    // Claim 003　診療種別区分
    public static final int SYUGI = 0; // 手技
    public static final int ZAIRYO = 1; // 材料
    public static final int YAKUZAI = 2; // 薬剤
    public static final int ADMIN = 3; // 用法

    // Claim 004　数量コード
    public static final String YAKUZAI_TOYORYO        = "10";   // 薬剤投与量
    public static final String YAKUZAI_TOYORYO_1KAI   = "11";	// 薬剤投与量１回
    public static final String YAKUZAI_TOYORYO_1NICHI = "12";	// 薬剤投与量１日
    public static final String ZAIRYO_KOSU            = "21";	// 材料個数

    // Claim 007 レセ電診療行為区分コード＝ ORCA の srysyukbn http://www.sky.sh/orca/sinryo.htm
    public static final String RECEIPT_CODE_NAIYO = "210"; // 内服
    public static final String RECEIPT_CODE_TONYO = "220"; // 頓服
    public static final String RECEIPT_CODE_GAIYO = "230"; // 外用
    public static final String INJECTION_310      = "310"; // 皮下筋注
    public static final String INJECTION_320      = "320"; // 静注
    public static final String INJECTION_330      = "330"; // その他注射
    public static final String INJECTION_311      = "311";
    public static final String INJECTION_321      = "321";
    public static final String INJECTION_331      = "331";

    // ORCA TBL_TENSU の srycd（9桁のコード） の頭番号
    public static final String ADMIN_CODE_START   = "001"; // 用法，部位(0010008)，コメント(0010000)
    public static final String RBUI_CODE_START    = "002"; // 放射線部位
    public static final String SYUGI_CODE_START   = "1"; // 手技 -- この中に ORCA での診療行為区分 srysyukbn がある
    public static final String YAKUZAI_CODE_START = "6"; // 薬剤
    public static final String ZAIRYO_CODE_START  = "7"; // 材料
    public static final String COMMENT_CODE_START = "8"; // コメントコード

    // ORCA TBL_TENSU の薬剤区分番号(ykzkbn)
    public static final String YKZ_KBN_NAIYO      = "1"; // 内服薬 code 61〜
    public static final String YKZ_KBN_INJECTION  = "4"; // 注射薬 code 64〜
    public static final String YKZ_KBN_GAIYO      = "6"; // 外用薬 code 66〜

    // その他
    public static final String IN_MEDICINE = "院内処方";
    public static final String EXT_MEDICINE = "院外処方";


    public static void main(String[] argv) {
        String entity = getEntity("145");
        System.out.println("entity="+entity+" name=" + EntityNameMap.get(entity));
    }
}

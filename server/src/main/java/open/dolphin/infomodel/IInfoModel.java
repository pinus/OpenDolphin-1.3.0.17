package open.dolphin.infomodel;

import java.io.Serializable;

/**
 * IInfoModel.
 *
 * @author Minagawa, Kazushi
 */
public interface IInfoModel extends Serializable {

    /** default facility id */
    String DEFAULT_FACILITY_OID = "1.3.6.1.4.1.9414.10.1";

    /** ISO 8601 style date format */
    String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /** カルテの確定日表示用のフォーマット */
    //String KARTE_DATE_FORMAT = "yyyy年M月d日'（'EEE'）'H時m分";
    String KARTE_DATE_FORMAT = "yyyy年M月d日(E) HH:mm";

    /** 時間部分のない Date */
    String DATE_WITHOUT_TIME = "yyyy-MM-dd";

    /** 複合キーにするための結合子 */
    String COMPOSITE_KEY_MAKER = ":";

    /** 複合キーとパスワードを区切るための結合子 */
    String PASSWORD_SEPARATOR = ";";

    /** 管理者ロール */
    String ADMIN_ROLE = "admin";

    /** 利用者ロール */
    String USER_ROLE = "user";

    String MALE = "male";

    String MALE_DISP = "男";

    String FEMALE = "female";

    String FEMALE_DISP = "女";

    String UNKNOWN = "不明";

    String AGE = "歳";

    String DOCTYPE_KARTE = "karte";

    String DOCTYPE_DIAGNOSIS = "diagnosis";

    String PURPOSE_RECORD = "recode";

    String PARENT_OLD_EDITION = "oldEdition";

    String DEFAULT_DIAGNOSIS_TITLE = "病名登録";

    String DEFAULT_DIAGNOSIS_CATEGORY = "mainDiagnosis";

    String DEFAULT_DIAGNOSIS_CATEGORY_DESC = "主病名";

    String DEFAULT_DIAGNOSIS_CATEGORY_CODESYS = "MML0012";

    String ORCA_OUTCOME_RECOVERED ="治癒";

    String ORCA_OUTCOME_DIED = "死亡";

    String ORCA_OUTCOME_END = "中止";

    String ORCA_OUTCOME_TRANSFERED = "移行";

    //
    // Stamp Roles
    //
    /** ProgressCourse */
    String MODULE_PROGRESS_COURSE = "progressCourse";

    /** SOA stamp */
    String ROLE_SOA = "soa";

    /** P stamp */
    String ROLE_P = "p";

    /** SOA spec */
    String ROLE_SOA_SPEC = "soaSpec";

    /** P spec */
    String ROLE_P_SPEC = "pSpec";

    /** Text stamp */
    String ROLE_TEXT = "text";

    /** ORCA 入力セット */
    String ROLE_ORCA_SET = "orcaSet";

    String STATUS_FINAL = "F";
    String STATUS_MODIFIED = "M";
    String STATUS_TMP = "T";
    String STATUS_NONE = "N";
    String STATUS_DELETE = "D";

    String PERMISSION_ALL = "all";
    String PERMISSION_READ = "read";
    String ACCES_RIGHT_PATIENT = "patient";
    String ACCES_RIGHT_CREATOR = "creator";
    String ACCES_RIGHT_EXPERIENCE = "experience";
    String ACCES_RIGHT_PATIENT_DISP = "被記載者(患者)";
    String ACCES_RIGHT_CREATOR_DISP = "記載者施設";
    String ACCES_RIGHT_EXPERIENCE_DISP = "診療歴のある施設";
    String ACCES_RIGHT_PERSON_CODE = "personCode";
    String ACCES_RIGHT_FACILITY_CODE = "facilityCode";
    String ACCES_RIGHT_EXPERIENCE_CODE = "facilityCode";

    /** レセ電算コード 内用 */
    String RECEIPT_CODE_NAIYO = "210";
    /** レセ電算コード 頓用 */
    String RECEIPT_CODE_TONYO = "220";
    /** レセ電算コード 外用*/
    String RECEIPT_CODE_GAIYO = "230";

    String INSURANCE_SELF = "自費";
    String INSURANCE_SELF_CODE = "Z1";
    String INSURANCE_SYS = "MML031";

    //
    // StampTreeのエンティティ（情報の実体）名
    //
    /** 傷病名 */
    String ENTITY_DIAGNOSIS = "diagnosis";

    /** テキスト */
    String ENTITY_TEXT = "text";

    /** パ ス */
    String ENTITY_PATH = "path";

    /** 汎用 */
    String ENTITY_GENERAL_ORDER = "generalOrder";

    /** その他 */
    String ENTITY_OTHER_ORDER = "otherOrder";

    /** 処 置 */
    String ENTITY_TREATMENT = "treatmentOrder";

    /** 手 術 */
    String ENTITY_SURGERY_ORDER = "surgeryOrder";

    /** 放射線 */
    String ENTITY_RADIOLOGY_ORDER = "radiologyOrder";

    /** ラボテスト */
    String ENTITY_LABO_TEST = "testOrder";

    /** 生体検査 */
    String ENTITY_PHYSIOLOGY_ORDER = "physiologyOrder";

    /** 細菌検査 */
    String ENTITY_BACTERIA_ORDER = "bacteriaOrder";

    /** 注 射 */
    String ENTITY_INJECTION_ORDER = "injectionOrder";

    /** 処 方 */
    String ENTITY_MED_ORDER = "medOrder";

    /** 診 断 */
    String ENTITY_BASE_CHARGE_ORDER = "baseChargeOrder";

    /** 指 導 */
    String ENTITY_INSTRACTION_CHARGE_ORDER = "instractionChargeOrder";

    /** ORCA セット */
    String ENTITY_ORCA = "orcaSet";

    /** Entity の配列 */
    String[] STAMP_ENTITIES = new String[] {
        ENTITY_DIAGNOSIS, ENTITY_TEXT, ENTITY_PATH, ENTITY_ORCA, ENTITY_GENERAL_ORDER, ENTITY_OTHER_ORDER, ENTITY_TREATMENT,
        ENTITY_SURGERY_ORDER, ENTITY_RADIOLOGY_ORDER, ENTITY_LABO_TEST, ENTITY_PHYSIOLOGY_ORDER,
        ENTITY_BACTERIA_ORDER, ENTITY_INJECTION_ORDER, ENTITY_MED_ORDER, ENTITY_BASE_CHARGE_ORDER, ENTITY_INSTRACTION_CHARGE_ORDER
    };

    //
    // StampTreeのタブ名
    //
    /** 傷病名 */
    String TABNAME_DIAGNOSIS = "傷病名";

    /** テキスト */
    String TABNAME_TEXT = "テキスト";

    /** パ ス */
    String TABNAME_PATH = "パ ス";

    /** ORCA セット */
    String TABNAME_ORCA = "ORCA";

    /** 汎 用 */
    String TABNAME_GENERAL = "汎 用";

    /** その他 */
    String TABNAME_OTHER = "その他";

    /** 処 置 */
    String TABNAME_TREATMENT = "処 置";

    /** 手 術 */
    String TABNAME_SURGERY = "手 術";

    /** 放射線 */
    String TABNAME_RADIOLOGY = "放射線";

    /** 検体検査 */
    String TABNAME_LABO = "検体検査";

    /** 生体検査 */
    String TABNAME_PHYSIOLOGY = "生体検査";

    /** 細菌検査 */
    String TABNAME_BACTERIA = "細菌検査";

    /** 注 射 */
    String TABNAME_INJECTION = "注 射";

    /** 処 方 */
    String TABNAME_MED = "処 方";

    /** 初診・再診 */
    String TABNAME_BASE_CHARGE = "初診・再診";

    /** 指導・在宅 */
    String TABNAME_INSTRACTION = "指導・在宅";

    /** スタンプのタブ名配列 */
    String[] STAMP_NAMES = {
        TABNAME_DIAGNOSIS, TABNAME_TEXT, TABNAME_PATH, TABNAME_ORCA,
        TABNAME_GENERAL, TABNAME_OTHER, TABNAME_TREATMENT, TABNAME_SURGERY,
        TABNAME_RADIOLOGY, TABNAME_LABO, TABNAME_PHYSIOLOGY, TABNAME_BACTERIA,
        TABNAME_INJECTION, TABNAME_MED, TABNAME_BASE_CHARGE, TABNAME_INSTRACTION
    };

    String OBSERVATION_ALLERGY = "Allergy";
    String OBSERVATION_PHYSICAL_EXAM = "PhysicalExam";

    String PHENOMENON_BODY_HEIGHT = "bodyHeight";
    String PHENOMENON_BODY_WEIGHT = "bodyWeight";

    String UNIT_BODY_WEIGHT = "Kg";
    String UNIT_BODY_HEIGHT = "cm";

    String PUBLISH_TREE_LOCAL = "院内";
    String PUBLISH_TREE_PUBLIC = "グローバル";
    String PUBLISHED_TYPE_GLOBAL = "global";

}

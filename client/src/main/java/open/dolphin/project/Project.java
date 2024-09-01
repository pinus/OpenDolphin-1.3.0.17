package open.dolphin.project;

import open.dolphin.client.SaveParams;
import open.dolphin.infomodel.ID;
import open.dolphin.infomodel.UserModel;

import java.awt.*;
import java.io.OutputStream;
import java.util.prefs.Preferences;

/**
 * プロジェクト情報管理クラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class Project {

    // Prpject Name
    public static final String PROJECT_NAME = "name";

    // USER
    public static final String USER_TYPE = "userType";
    public static final String USER_ID = "userId";
    public static final String USER_PASSWORD = "userPassword";
    public static final String FACILITY_ID = "facilityId";
    public static final String SAVE_PASSWORD = "savepassword";

    // SERVER
    public static final String HOST_ADDRESS = "hostAddress";
    public static final String HOST_PORT = "hostPort";

    // CLAIM
    public static final String SEND_CLAIM = "sendClaim";
    public static final String SEND_CLAIM_SAVE = "sendClaimSave";
    public static final String SEND_CLAIM_TMP = "sendClaimTmp";
    public static final String SEND_CLAIM_MODIFY = "sendClaimModify";
    public static final String SEND_DIAGNOSIS = "sendDiagnosis";

    // ソフトウェア更新
    public static final String USE_PROXY = "useProxy";
    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";
    public static final String LAST_MODIFIED = "lastModify";

    // インスペクタのメモ位置
    public static final String INSPECTOR_MEMO_LOCATION = "inspectorMemoLocation";

    // インスペクタの locationByPlatform 属性
    public static final String LOCATION_BY_PLATFORM = "locationByPlatform";

    // 文書履歴
    public static final String DOC_HISTORY_ASCENDING = "docHistory.ascending";
    public static final String DOC_HISTORY_SHOWMODIFIED = "docHistory.showModified";
    public static final String DOC_HISTORY_FETCHCOUNT = "docHistory.fetchCount";
    public static final String DOC_HISTORY_PERIOD = "docHistory.period";
    public static final String KARTE_SCROLL_DIRECTION = "karte.scroll.direction";
    public static final String DOUBLE_KARTE = "karte.double";

    // 病名
    public static final String DIAGNOSIS_ASCENDING = "diagnosis.ascending";
    public static final String DIAGNOSIS_PERIOD = "diagnosis.period";
    public static final String OFFSET_OUTCOME_DATE = "diagnosis.offsetOutcomeDate";

    // 検体検査
    public static final String LABOTEST_PERIOD = "laboTest.period";

    // 処方
    public static final String RP_OUT = "rp.out";

    // 確認ダイアログ
    public static final String KARTE_SHOW_CONFIRM_AT_NEW = "karte.showConfirmAtNew";
    public static final String KARTE_CREATE_MODE = "karte.createMode";
    public static final String KARTE_PLACE_MODE = "karte.placeMode";
    public static final String KARTE_SHOW_CONFIRM_AT_SAVE = "karte.showConfirmAtSave";
    public static final String KARTE_PRINT_COUNT = "karte.printCount";
    public static final String KARTE_SAVE_ACTION = "karte.saveAction";
    //スクロール速度設定
    public static final String SCROLL_UNIT_KARTE = "scroll.unit.karte";
    public static final String SCROLL_UNIT_TABLE = "scroll.unit.table";
    public static final String SCROLL_UNIT_STAMP = "scroll.unit.stamp";
    // pvt チェック間隔
    public static final String PVT_CHECK_INTERVAL = "pvt.check.interval";
    // ウィンドウ整列 in WindowSupport
    public static final String ARRANGE_INSPECTOR_X = "arrange.inspector.x";
    public static final String ARRANGE_INSPECTOR_Y = "arrange.inspector.y";
    public static final String ARRANGE_INSPECTOR_DX = "arrange.inspector.dx";
    public static final String ARRANGE_INSPECTOR_DY = "arrange.inspector.dy";
    // コンソールのログファイル出力
    public static final String REDIRECT_CONSOLE = "redirect.console";
    // ATOK 文字種切換キー
    public static final String ATOK_TO_EIJI_KEY = "atok.to.eiji.key";
    public static final String ATOK_TO_HIRAGANA_KEY = "atok.to.hiragana.key";
    public static final String ATOK_JAPANESE_KEY = "atok.japanese.key";
    public static final String ATOK_KATAKANA_KEY = "atok.katakana.key";
    public static final String ATOK_ROMAN_KEY = "atok.roman.key";

    private static ProjectStub stub;

    public Project() {
    }

    public static ProjectStub getProjectStub() {
        return stub;
    }

    public static void setProjectStub(ProjectStub p) {
        stub = p;
    }

    public static UserType getUserType() {
        return stub.getUserType();
    }

    public static boolean isValid() {
        return stub.isValid();
    }

    public static Preferences getPreferences() {
        return stub.getPreferences();
    }

    public static DolphinPrincipal getDolphinPrincipal() {
        return stub.getDolphinPrincipal();
    }

    public static String getProviderURL() {
        return stub.getProviderURL();
    }

    public static UserModel getUserModel() {
        return stub.getUserModel();
    }

    public static void setUserModel(UserModel value) {
        stub.setUserModel(value);
    }

    public static boolean isReadOnly() {
        String licenseCode = stub.getUserModel().getLicenseModel().getLicense();
        String userId = stub.getUserModel().getUserId();
        return !licenseCode.equals("doctor") && !userId.equals("lasmanager");
    }

    public static String getUserId() {
        return stub.getUserId();
    }

    public static String getUserPassword() {
        return stub.getUserPassword();
    }

    public static String getFacilityId() {
        return stub.getFacilityId();
    }

    public static String getOrcaVersion() {
        return stub.getOrcaVersion();
    }

    public static String getJMARICode() {
        return stub.getJMARICode();
    }

    public static String getHostAddress() {
        return stub.getHostAddress();
    }

    public static int getHostPort() {
        return stub.getHostPort();
    }

    /**
     * 診療行為の送信を行うかどうかを返す.
     *
     * @return 行うとき true
     */
    public static boolean getSendClaim() {
        return stub.getSendClaim();
    }

    //
    // CLAIM
    //

    /**
     * 保存時に送信を行うかどうかを返す.
     *
     * @return 行うとき true
     */
    public static boolean getSendClaimSave() {
        return stub.getSendClaimSave();
    }

    /**
     * 仮保存時に診療行為の送信を行うかどうかを返す.
     *
     * @return 行うとき true
     */
    public static boolean getSendClaimTmp() {
        return stub.getSendClaimTmp();
    }

    /**
     * 修正時に診療行為の送信を行うかどうかを返す.
     *
     * @return 行うとき true
     */
    public static boolean getSendClaimModify() {
        return stub.getSendClaimModify();
    }

    /**
     * 病名の送信を行うかどうかを返す.
     *
     * @return 行うとき true
     */
    public static boolean getSendDiagnosis() {
        return stub.getSendDiagnosis();
    }

    public static String getProxyHost() {
        return stub.getProxyHost();
    }

    public static int getProxyPort() {
        return stub.getProxyPort();
    }

    public static long getLastModify() {
        return stub.getLastModify();
    }

    public static void setLastModify(long val) {
        stub.setLastModify(val);
    }

    /**
     * ProjectFactoryを返す.
     *
     * @return Project毎に異なる部分の情報を生成するためのFactory
     */
    public static AbstractProjectFactory getProjectFactory() {
        return AbstractProjectFactory.getProjectFactory(stub.getName());
    }

    /**
     * CLAIM送信に使用する患者MasterIdを返す.
     * 地域連携ルールと異なるため.
     */
    public static ID getClaimMasterId(String pid) {
        return new ID(pid, "facility", "MML0024");
    }

    public static Object createSaveDialog(Window parent, SaveParams params) {
        return getProjectFactory().createSaveDialog(parent, params);
    }

    public static void exportSubtree(OutputStream os) {
        stub.exportSubtree(os);
    }

    public static void clear() {
        stub.clear();
    }

    // ユーザの利用形式
    public enum UserType {
        ASP_MEMBER, ASP_TESTER, ASP_DEV, FACILITY_USER, UNKNOWN, EXPIRED
    }
}

package open.dolphin.client;

import java.util.Calendar;
import java.util.GregorianCalendar;
import open.dolphin.delegater.StampDelegater;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.StampModel;

/**
 * StampTree から ModuleModel を取り出す.
 * @author pns
 */
public class StampModulePicker {

    private enum StampItem {
        SHOSHIN ("初診"),
        SHOSHIN_RYOTAN ("初診（療養担当）"),
        SHOSHIN_YAKAN ("初診（夜間・早朝等）"),
        SHOSHIN_RYOTAN_YAKAN ("初診（療養担当）（夜間・早朝等）"),
        SAISHIN ("再診（診療所）"),
        SAISHIN_YAKAN ("再診（診療所）（夜間・早朝等）"),
        ;
        private final String title;
        private StampItem(String t) { title = t; }
        public String stampTitle() { return title; }
    }

    private final ChartMediator mediator;
    private final GregorianCalendar today;
    private final LastVisit lv;

    public StampModulePicker(ChartImpl context) {
        mediator = context.getChartMediator();
        today = new GregorianCalendar();
        lv = context.getLastVisit();
    }

    /**
     * entity のスタンプ箱から key に一致する module を探して返す. 無ければ null が返る.
     * ただし，フォルダの中身までは検索しない.
     * @param entity
     * @param key
     * @return ModuleInfoBean or null
     */
    private ModuleModel getStampModule(String entity, String key) {
        StampTree tree =  mediator.getStampTree(entity);
        ModuleInfoBean info = null;
        ModuleModel module = null;

        for (int i=0; i<tree.getRowCount(); i++) {
            StampTreeNode sn = (StampTreeNode) tree.getPathForRow(i).getLastPathComponent();
            if (sn.isLeaf()) {
                ModuleInfoBean bean = sn.getStampInfo();
                String name = bean.getStampName();
                if (name.equals(key)) {
                    info = bean;
                    break;
                }
            }
        }
        if (info != null) {
            StampDelegater sdl = new StampDelegater();
            // Stamp モデルをデータベースから取ってくる
            StampModel stampModel = sdl.getStamp(info.getStampId());
            // 実体 getStamp と情報 info を module model にセット
            module = new ModuleModel();
            module.setModel(stampModel.getStamp());
            module.setModuleInfo(info);
        }
        return module;
    }

    /**
     * テキストスタンプから title のスタンプを取ってきて返す.
     * @param title
     * @return ModuleModel
     */
    public ModuleModel getTextStamp(String title) {

        ModuleModel module = getStampModule(IInfoModel.ENTITY_TEXT, title);
        if (module != null) {
            module.getModuleInfo().setStampRole(IInfoModel.ROLE_SOA);
        }
        return module;
    }

    /**
     * エディタを開いたときに最初に挿入するスタンプ名.
     * @return
     */
    public ModuleModel getInitialStamp() {
        return isShoshin()?
            getTextStamp("テンプレート（初診）"):
            getTextStamp("テンプレート（再診）");
    }

    /**
     * 初診・再診を判定して，module として返す.
     * @return
     */
    public ModuleModel getBaseCharge() {
        /*
         * 初診か再診か判定
         * 初診，初診（療養担当），初診（夜間・早朝等），初診（療養担当）（夜間・早朝等），再診（診療所），再診（診療所）（夜間・早朝等）
         */

        // 初診・再診の情報判定
        StampItem item;

        if (isShoshin()) {
            if (isRyotan()) {
                if (isYakan()) { item = StampItem.SHOSHIN_RYOTAN_YAKAN; }
                else { item = StampItem.SHOSHIN_RYOTAN; }
            } else {
                if (isYakan()) { item = StampItem.SHOSHIN_YAKAN; }
                else { item = StampItem.SHOSHIN; }
            }
        } else {
            if (isYakan()) { item = StampItem.SAISHIN_YAKAN; }
            else { item = StampItem.SAISHIN; }
        }

        // スタンプ選択
        return getStampModule(IInfoModel.ENTITY_BASE_CHARGE_ORDER, item.stampTitle());
    }

    public boolean isShoshin() {
        int[] lastVisit = lv.getLastVisitInHistoryYmd();

        // 受診歴が無ければ初診で返す
        if (lastVisit == null) { return true; }

        // 受診歴がある場合は，３ヶ月たったかどうか判定
        int dif_year = today.get(Calendar.YEAR) - lastVisit[0];
        int dif_month = today.get(Calendar.MONTH) - lastVisit[1];
        int dif_day = today.get(Calendar.DATE) - lastVisit[2];

        // ２年以上離れていたら初診
        if (dif_year >= 2) { return true; }
        // １年差の場合は月差を調整
        if (dif_year == 1) { dif_month += 12; }
        // 4ヶ月以上離れていたら初診
        if (dif_month >= 4) { return true; }
        // 3ヶ月離れている場合は，日付を比較
        else if (dif_month == 3) {
            return dif_day > 0;
            // それ以外は再診
        } else { return false; }
    }

    private boolean isRyotan() {
        int month = today.get(Calendar.MONTH);
        // 1~4月，11~12月は療養担当手当あり month = 0~11
        return month <= 3 || month >= 10;
    }

    private boolean isYakan() {
        // 土曜日で 12時以降なら夜間
        GregorianCalendar gc = lv.getLastVisitGc();
        // 今日受診していないのに新規カルテを作った場合は false に決め打ち（実際はあり得ない操作）
        if (gc == null) { return false; }

        return (gc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) &&
                (gc.get(Calendar.AM_PM) == Calendar.PM);
    }
}

package open.dolphin.codehelper;

import open.dolphin.client.ChartMediator;
import open.dolphin.client.KartePane;
import open.dolphin.helper.StringTool;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.stampbox.StampBoxPlugin;
import open.dolphin.stampbox.StampTree;

import java.util.List;
import java.util.prefs.Preferences;

/**
 * Pペインのコードヘルパークラス.
 *
 * @author Kazyshi Minagawa
 * @author pns
 */
public class PCodeHelper extends AbstractCodeHelper {

    public PCodeHelper(KartePane pPane, ChartMediator mediator) {
        super(pPane, mediator);
    }

    /**
     * CodeHelperSettingPanel のキーワードで呼ばれた場合は，それぞれの ENTITY のメニューを作る.
     * それ以外の文字列の場合は，その文字列を含むスタンプからメニューを作る.
     *
     * @param text
     */
    @Override
    protected void buildPopup(String text) {

        String test = StringTool.toHankakuUpperLower(text).toLowerCase();
        String entity = null;

        //
        // StampTree のキーワードに一致しているかどうかを判定する
        //
        Preferences prefs = getPreferences();

        if (prefs.get(IInfoModel.ENTITY_TEXT, "tx").startsWith(test)) {
            entity = IInfoModel.ENTITY_TEXT;

        } else if (prefs.get(IInfoModel.ENTITY_PATH, "pat").startsWith(test)) {
            entity = IInfoModel.ENTITY_PATH;

        } else if (prefs.get(IInfoModel.ENTITY_GENERAL_ORDER, "gen").startsWith(test)) {
            entity = IInfoModel.ENTITY_GENERAL_ORDER;

        } else if (prefs.get(IInfoModel.ENTITY_OTHER_ORDER, "oth").startsWith(test)) {
            entity = IInfoModel.ENTITY_OTHER_ORDER;

        } else if (prefs.get(IInfoModel.ENTITY_TREATMENT, "tr").startsWith(test)) {
            entity = IInfoModel.ENTITY_TREATMENT;

        } else if (prefs.get(IInfoModel.ENTITY_SURGERY_ORDER, "sur").startsWith(test)) {
            entity = IInfoModel.ENTITY_SURGERY_ORDER;

        } else if (prefs.get(IInfoModel.ENTITY_RADIOLOGY_ORDER, "rad").startsWith(test)) {
            entity = IInfoModel.ENTITY_RADIOLOGY_ORDER;

        } else if (prefs.get(IInfoModel.ENTITY_LABO_TEST, "lab").startsWith(test)) {
            entity = IInfoModel.ENTITY_LABO_TEST;

        } else if (prefs.get(IInfoModel.ENTITY_PHYSIOLOGY_ORDER, "phy").startsWith(test)) {
            entity = IInfoModel.ENTITY_PHYSIOLOGY_ORDER;

        } else if (prefs.get(IInfoModel.ENTITY_BACTERIA_ORDER, "bac").startsWith(test)) {
            entity = IInfoModel.ENTITY_BACTERIA_ORDER;

        } else if (prefs.get(IInfoModel.ENTITY_INJECTION_ORDER, "inj").startsWith(test)) {
            entity = IInfoModel.ENTITY_INJECTION_ORDER;

        } else if (prefs.get(IInfoModel.ENTITY_MED_ORDER, "rp").startsWith(test)) {
            entity = IInfoModel.ENTITY_MED_ORDER;

        } else if (prefs.get(IInfoModel.ENTITY_BASE_CHARGE_ORDER, "base").startsWith(test)) {
            entity = IInfoModel.ENTITY_BASE_CHARGE_ORDER;

        } else if (prefs.get(IInfoModel.ENTITY_INSTRACTION_CHARGE_ORDER, "ins").startsWith(test)) {
            entity = IInfoModel.ENTITY_INSTRACTION_CHARGE_ORDER;

        } else if (prefs.get(IInfoModel.ENTITY_ORCA, "orca").startsWith(test)) {
            entity = IInfoModel.ENTITY_ORCA;
        }

        if (entity != null) {
            buildEntityPopup(entity);

        } else {
            // current StampBoxのP関連 StampTree を取得する
            StampBoxPlugin stampBox = getMediator().getStampBox();
            List<StampTree> allTree = stampBox.getAllPTrees();

            buildMatchedPopup(allTree, text);
        }
    }
}

package open.dolphin.codehelper;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import open.dolphin.client.ChartMediator;
import open.dolphin.client.KartePane;
import open.dolphin.stampbox.StampBoxPlugin;
import open.dolphin.stampbox.StampTree;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.helper.StringTool;

/**
 * SOAペインのコードヘルパークラス.
 * テキストスタンプと病名スタンプの入力補助.
 *
 * @author Kazyshi Minagawa
 * @author pns
 */
public class SOACodeHelper extends AbstractCodeHelper {

    public SOACodeHelper(KartePane pPane, ChartMediator mediator) {
        super(pPane, mediator);
    }

    /**
     * tx, dx キーワードで呼ばれた場合は ENTITY_TEXT or DIAGNOSIS のメニューを作る.
     * それ以外の文字列の場合は，その文字列を含むスタンプからメニューを作る.
     * @param text
     */
    @Override
    protected void buildPopup(String text) {

        Preferences prefs = getPreferences();
        String test = StringTool.toHankakuUpperLower(text).toLowerCase();

        if (prefs.get(IInfoModel.ENTITY_TEXT, "tx").startsWith(test)) {
            buildEntityPopup(IInfoModel.ENTITY_TEXT);

        } else if (prefs.get(IInfoModel.ENTITY_DIAGNOSIS, "dx").startsWith(test)) {
            buildEntityPopup(IInfoModel.ENTITY_DIAGNOSIS);

        } else {

            List<StampTree> trees = new ArrayList<>();
            StampBoxPlugin stampBox = getMediator().getStampBox();
            trees.add(stampBox.getStampTree(IInfoModel.ENTITY_TEXT));
            trees.add(stampBox.getStampTree(IInfoModel.ENTITY_DIAGNOSIS));

            buildMatchedPopup(trees, text);
        }
    }
}

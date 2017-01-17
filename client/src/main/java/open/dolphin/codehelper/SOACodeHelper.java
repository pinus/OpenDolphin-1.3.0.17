package open.dolphin.codehelper;

import java.util.prefs.Preferences;
import javax.swing.JPopupMenu;
import open.dolphin.client.ChartMediator;
import open.dolphin.client.KartePane;
import open.dolphin.client.StampBoxPlugin;
import open.dolphin.client.StampTree;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.util.StringTool;

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
     * tx キーワードで呼ばれた場合は ENTITY_TEXT のメニューを作る.
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
            buildMatchPopup(text);
        }
    }

    /**
     * キーワードを含む stamp から popup を作ってセットする.
     * @param text
     */
    protected void buildMatchPopup(String text) {

        StampBoxPlugin stampBox = getMediator().getStampBox();
        StampTree textTree = stampBox.getStampTree(IInfoModel.ENTITY_TEXT);
        StampTree diagTree = stampBox.getStampTree(IInfoModel.ENTITY_DIAGNOSIS);
        if (textTree == null || diagTree == null) { return; }

        // Stamp を検索する検索文字列
        String searchText = ".*" + text + ".*";

        MenuModel modelText = createMenu(textTree, searchText);
        MenuModel modelDiag = createMenu(diagTree, searchText);

        JPopupMenu popup = new JPopupMenu();

        modelText.getSubMenus().forEach(menu -> popup.add(menu));
        modelText.getRootItems().forEach(item -> popup.add(item));
        modelDiag.getSubMenus().forEach(menu -> popup.add(menu));
        modelDiag.getRootItems().forEach(item -> popup.add(item));

        setPopup(popup);
    }
}

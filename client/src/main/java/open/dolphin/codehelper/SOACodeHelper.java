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

        Preferences prefs = Preferences.userNodeForPackage(AbstractCodeHelper.class);

        if (prefs.get(IInfoModel.ENTITY_TEXT, "tx").startsWith(StringTool.toHankakuUpperLower(text).toLowerCase())) {
            buildEntityPopup(IInfoModel.ENTITY_TEXT);

        } else if (prefs.get(IInfoModel.ENTITY_DIAGNOSIS, "dx").startsWith(StringTool.toHankakuUpperLower(text).toLowerCase())) {
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
        StampTree tree = stampBox.getStampTree(IInfoModel.ENTITY_TEXT);
        if (tree == null) { return; }

        // Stamp を検索する検索文字列
        String searchText = ".*" + text + ".*";

        MenuModel model = createMenu(tree, searchText);

        JPopupMenu popup = new JPopupMenu();
        model.getSubMenus().forEach(menu -> popup.add(menu));
        model.getRootItems().forEach(item -> popup.add(item));

        setPopup(popup);
    }
}

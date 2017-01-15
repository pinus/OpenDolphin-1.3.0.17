package open.dolphin.client;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.util.StringTool;

/**
 * Pペインのコードヘルパークラス.
 *
 * @author Kazyshi Minagawa
 */
public class PCodeHelper extends AbstractCodeHelper {

    public PCodeHelper(KartePane pPane, ChartMediator mediator) {
        super(pPane, mediator);
    }

    @Override
    protected void buildPopup(String text) {

        String test = StringTool.toHankakuUpperLower(text).toLowerCase();
        String entity = null;

        //
        // StampTree のキーワードに一致しているかどうかを判定する
        //
        Preferences prefs = Preferences.userNodeForPackage(AbstractCodeHelper.class);

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
            //
            // 全てのスタンプツリーをなめる
            //
            buildMatchPopup(text);
        }
    }


    /**
     * text に合致する stamp を集めて popup にする.
     * @param text
     */
    protected void buildMatchPopup(String text) {
        //
        // current StampBoxのP関連 StampTree を取得する
        //
        StampBoxPlugin stampBox = mediator.getStampBox();
        List<StampTree> allTree = stampBox.getAllPTrees();
        if (allTree == null || allTree.isEmpty()) { return; }

        popup = new JPopupMenu();

        // メニューのスタックを生成する
        LinkedList<JMenu> menus = new LinkedList<>();

        // 親ノードのスタックを生成する - インデックスが menus と一致するようにする
        LinkedList<StampTreeNode> parents = new LinkedList<>();

        // Stamp を検索する pattern
        pattern = Pattern.compile(".*" + text + ".*");

        for (StampTree tree : allTree) {

            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
            Enumeration e = rootNode.preorderEnumeration();

            // スタンプをスキャンする
            if (e != null) {
                e.nextElement(); // consume root

                while (e.hasMoreElements()) {
                    // 調査対象のノードを得る
                    StampTreeNode node = (StampTreeNode) e.nextElement();
                    // その親を得る
                    StampTreeNode parent = (StampTreeNode) node.getParent();
                    // 親がリストに含まれているかどうか
                    int index = parents.indexOf(parent);

                    if (index > -1) {
                        // 既に親が登録されている場合
                        if (!node.isLeaf()) {
                            // フォルダの場合は，新たに親として parents に加える
                            String folderName = node.getUserObject().toString();
                            JMenu subMenu = new JMenu(folderName);
                            // 頭から入れていく
                            menus.addFirst(subMenu);
                            parents.addFirst(node);
                            // フォルダ item を作って menu 中に入れておく
                            JMenuItem item = new JMenuItem(folderName);
                            item.setIcon(ICON);
                            subMenu.add(item);
                            addActionListner(item, node);

                        } else {
                            // 親のいる item の場合は，親のもとに入れる
                            ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                            String completion = info.getStampName();
                            JMenuItem item = new JMenuItem(completion);
                            addActionListner(item, node);
                            // parents と index は一致しているので，これで対応 menu 下に item が入る
                            menus.get(index).add(item);
                        }

                    } else {
                        // 親がいない場合は検索する
                        if (!node.isLeaf()) {
                            // フォルダの場合
                            String completion = node.getUserObject().toString();
                            Matcher matcher = pattern.matcher(completion);
                            if (matcher.matches()) {
                                String folderName = node.getUserObject().toString();
                                JMenu subMenu = new JMenu(folderName);
                                // 親として加える
                                menus.addFirst(subMenu);
                                parents.addFirst(node);

                                // フォルダ item を作って menu 中に入れておく
                                JMenuItem item = new JMenuItem(folderName);
                                item.setIcon(ICON);
                                subMenu.add(item);
                                addActionListner(item, node);
                            }

                        } else {
                            // 親のない item の場合
                            ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                            String completion = info.getStampName();
                            Matcher matcher = pattern.matcher(completion);

                            if (matcher.matches()) {
                                // 一致した場合
                                JMenuItem item = new JMenuItem(completion);
                                addActionListner(item, node);
                                // 親の居ない item は popup のルートに直に入れていく
                                popup.add(item);
                            }
                        }
                    }
                }
            }
            // この時点で親のいない item だけが popup に入っている
            // 親の folder をその頭に加えていく
            // addFirst で入れて順番は保持されているので，上から取って popu の上に入れていけばいい
            menus.forEach(menu -> popup.insert(menu,0));
        }
    }
}

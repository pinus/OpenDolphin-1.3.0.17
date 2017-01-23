package open.dolphin.stampbox;

import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import open.dolphin.client.GUIConst;
import open.dolphin.helper.MenuActionManager;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.ui.MyJSheet;
import open.dolphin.helper.MenuActionManager.MenuAction;

/**
 * StampTree に PopupMenu を表示する.
 * StampTreePopupAdapter から切換.
 * @author pns
 */
public class StampTreePopupMenu extends JPopupMenu {
    private static final long serialVersionUID = 1L;

    private final StampTree tree;

    public StampTreePopupMenu(StampTree tree) {
        super();
        this.tree = tree;
        buildPopupMenu();
    }

    private void buildPopupMenu() {
        MenuActionManager m = new MenuActionManager(this);

        add(m.getMenuItem("createNewFolder", "新規フォルダ", GUIConst.ICON_FOLDER_NEW_16));
        add(m.getMenuItem("deleteNode", "削　除", GUIConst.ICON_REMOVE_16));
        add(m.getMenuItem("renameNode", "名前を変更", GUIConst.ICON_EMPTY_16));
        addSeparator();

        add(m.getMenuItem("collapseAll", "フォルダを全て閉じる", GUIConst.ICON_TREE_COLLAPSED_16));
        add(m.getMenuItem("expandAll", "フォルダを全て展開する", GUIConst.ICON_TREE_EXPANDED_16));
    }

    @MenuAction
    public void createNewFolder() {
        tree.createNewFolder();
    }
    @MenuAction
    public void deleteNode() {
        int ans = MyJSheet.showConfirmDialog(SwingUtilities.getWindowAncestor(tree),
                "本当に削除しますか", "スタンプ削除", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ans == JOptionPane.OK_OPTION) {
            if (isEditable()) { tree.deleteNode(); }
            else { Toolkit.getDefaultToolkit().beep(); }
        }
    }
    @MenuAction
    public void renameNode() {
        if (isEditable()) { tree.renameNode(); }
        else { Toolkit.getDefaultToolkit().beep(); }
    }
    @MenuAction
    public void expandAll() {
        tree.expandAll();
    }
    @MenuAction
    public void collapseAll() {
        tree.collapseAll();
    }

    private boolean isEditable() {
        StampTreeNode node = tree.getSelectedNode();
        if (node.isLeaf()) {
            // Leaf なので StampInfo 　を得る
            ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
            return info.isEditable();
        }
        return true;
    }
}

package open.dolphin.stampbox;

import open.dolphin.infomodel.ModuleInfoBean;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * StampTree のノードクラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class StampTreeNode extends DefaultMutableTreeNode {
    
    /**
     * コンストラクタ
     *
     * @param userObject
     */
    public StampTreeNode(Object userObject) {

        super(userObject);

        // StampInfo で初期化された場合は葉ノードにする
        if (userObject instanceof open.dolphin.infomodel.ModuleInfoBean) {
            this.allowsChildren = false;
        }
    }

    /**
     * 葉かどうかを返す.
     *
     * @return
     */
    @Override
    public boolean isLeaf() {
        return (!this.allowsChildren);
    }

    /**
     * StampInfo を返す.
     *
     * @return
     */
    public ModuleInfoBean getStampInfo() {
        return (ModuleInfoBean) userObject;
    }
}

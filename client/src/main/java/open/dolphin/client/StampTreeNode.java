package open.dolphin.client;

import javax.swing.tree.*;
import open.dolphin.infomodel.ModuleInfoBean;

/**
 * StampTree のノードクラス.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class StampTreeNode extends DefaultMutableTreeNode {
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
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
     * 葉かどうかを返す
     * @return
     */
    @Override
    public boolean isLeaf() {
        return (! this.allowsChildren);
    }

    /**
     * StampInfo を返す
     * @return
     */
    public ModuleInfoBean getStampInfo() {
        return (ModuleInfoBean) userObject;
    }
}

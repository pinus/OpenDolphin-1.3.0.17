package open.dolphin.stampbox;

import open.dolphin.client.GUIConst;
import open.dolphin.ui.PNSBorder;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * StampBox をレンダリングするクラス.（StampTree から分離）
 *
 * @author pns
 */
public class StampTreeRenderer extends DefaultTreeCellRenderer {
    public static final int SQUARE = 0;
    public static final int UNDER_LINE = 1;
    public static final int UPPER_LINE = 2;
        private static final ImageIcon ASP_ICON = GUIConst.ICON_DOCUMENT_ATTRIBUTE_16;
    private static final ImageIcon LOCAL_ICON = GUIConst.ICON_DOCUMENT_CONVERT_16;
    private Object targetNode;
    private int drawMode;
    private boolean isTargetNode;
    private boolean isUserTree;
    private boolean isEditable;

    @Override
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        isUserTree = ((StampTree) tree).isUserTree();
        isTargetNode = (targetNode == value);

        return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    }

    @Override
    public Icon getLeafIcon() {
        if (isUserTree) {
            return LOCAL_ICON;
        }
        return ASP_ICON;
    }

    /**
     * TransferHandler の repaint() を受けて，Drop 場所をマークする
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isTargetNode) {
            switch (drawMode) {
                case UNDER_LINE:
                    if (isEditable) {
                        PNSBorder.drawSelectedBlueLine(null, g, 0, getSize().height, getSize().width, getSize().height);
                    } else {
                        PNSBorder.drawSelectedGrayLine(null, g, 0, getSize().height, getSize().width, getSize().height);
                    }
                    break;

                case UPPER_LINE:
                    if (isEditable) {
                        PNSBorder.drawSelectedBlueLine(null, g, 0, 0, getSize().width, 0);
                    } else {
                        PNSBorder.drawSelectedGrayLine(null, g, 0, 0, getSize().width, 0);
                    }
                    break;

                default:
                    if (isEditable) {
                        PNSBorder.drawSelectedBlueRect(null, g, 0, 0, getSize().width, getSize().height);
                    } else {
                        PNSBorder.drawSelectedGrayRect(null, g, 0, 0, getSize().width, getSize().height);
                    }
            }
        }
    }

    /**
     * DropTargetListener から Drop 場所の node を受け取る
     *
     * @param node
     */
    public void setTargetNode(Object node) {
        targetNode = node;
    }

    /**
     * DropTargetListener から Drop 位置にどんなマークを出すかをセット
     *
     * @param mode
     */
    public void setDrawMode(int mode) {
        drawMode = mode;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}

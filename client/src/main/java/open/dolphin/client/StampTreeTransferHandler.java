package open.dolphin.client;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.InfoModelTransferable;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.ui.PatchedTransferHandler;

/**
 * StampTreeTransferHandler
 *
 * @author Minagawa,Kazushi
 * @author pns
 *
 */
public class StampTreeTransferHandler extends PatchedTransferHandler {
    private static final long serialVersionUID = 1205897976539749194L;

    // StampTreeNode Flavor
    private DataFlavor stampTreeNodeFlavor = LocalStampTreeNodeTransferable.localStampTreeNodeFlavor;

    // KartePaneからDropされるオーダのFlavor
    private DataFlavor orderFlavor = OrderListTransferable.orderListFlavor;

    // KartePaneからDropされるテキストFlavor
    private DataFlavor stringFlavor = DataFlavor.stringFlavor;

    // 病名エディタからDropされるRegisteredDiagnosis Flavor
    private DataFlavor infoModelFlavor = InfoModelTransferable.infoModelFlavor;

    // Drop する target の path
    private TreePath targetPath;

    // target の中に入れるか，前に挿入するか，後ろに挿入するかの情報（StampTreeDropTargetListener でセットする）
    public enum Insert { AFTER, BEFORE, INTO_FOLDER };
    private Insert insertPosition;

    // 半透明ドラッグのために保存する
    private JComponent draggedComp = null;

    public void setTargetPath(TreePath t) {
        targetPath = t;
    }
    public void setPosition(Insert p) {
        insertPosition = p;
    }
    public Insert getInsertPosition() {
        return insertPosition;
    }

    /**
     * 半透明 drag の為に，draggedComp と mousePosition を保存する
     * @param comp
     * @param e
     * @param action
     */
    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        insertPosition = null;

        JTree tree = (JTree)comp;
        TreePath path = tree.getSelectionPath();
        if (path == null) return; // １回ヌルポがでた

        TreeNode node = (TreeNode) path.getLastPathComponent();
        StampTreeRenderer r = (StampTreeRenderer) tree.getCellRenderer();

        boolean selected = false;
        boolean expanded = tree.isExpanded(path);
        boolean isLeaf = node.isLeaf();
        int row = tree.getRowForPath(path);
        boolean hasFocus = true;

        draggedComp = (JComponent) r.getTreeCellRendererComponent(tree, node, selected, expanded, isLeaf, row, hasFocus);
        draggedComp.setSize(draggedComp.getPreferredSize());
        // calculate MousePosition
        Rectangle pathBounds = tree.getPathBounds(path);
        mousePosition = tree.getMousePosition();
        // mousePosition may be null
        if (mousePosition != null) {
            mousePosition.x -= pathBounds.x;
            mousePosition.y -= pathBounds.y;
        }
        super.exportAsDrag(comp, e, action);
    }
    /**
     * 選択されたノードでDragを開始する。
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        StampTree sourceTree = (StampTree) c;
        StampTreeNode dragNode = (StampTreeNode) sourceTree.getLastSelectedPathComponent();
        return new LocalStampTreeNodeTransferable(dragNode);
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /**
     * DropされたFlavorをStampTreeにインポートする。
     */
    @Override
    public boolean importData(JComponent c, Transferable tr) {

        if (targetPath == null) {
            System.out.println("StampTreeTransferHandler: targetPath is null");
            return false;
        }
        if (insertPosition == null) {
            System.out.println("StampTreeTransferHandler: insertPosition is null");
            return false;
        }

        if (canImport(c, tr.getTransferDataFlavors())) {

            try {
                // Dropを受けるStampTreeを取得する
                StampTree tree = (StampTree) c;
                String targetEntity = tree.getEntity();

                // Drop位置のノードを取得する
                StampTreeNode targetNode = (StampTreeNode) targetPath.getLastPathComponent();

                // StampTree 内の DnD
                if (tr.isDataFlavorSupported(stampTreeNodeFlavor)) {

                    // ソースのノードを取得する
                    StampTreeNode sourceNode = (StampTreeNode) tr.getTransferData(stampTreeNodeFlavor);

                    // Drop 位置の親
                    StampTreeNode newParent = (StampTreeNode) targetNode.getParent();

                    // root までの親のパスのなかに自分がいるかどうかを判定する
                    // Drop先が DragNode の子である時は DnD できない i.e 親が自分の子になることはできない
                    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                    TreeNode[] parents = model.getPathToRoot(targetNode);
                    boolean exist = false;
                    for (TreeNode parent : parents) {
                        if (parent == (TreeNode) sourceNode) {
                            exist = true;
                            Toolkit.getDefaultToolkit().beep();
                            //System.out.println("new Child is ancestor");
                            break;
                        }
                    }

                    // 親が自分の子になろうとしているわけではない場合，処理開始
                    if (!exist) {
                        int index = newParent.getIndex(targetNode);
                        if (index == -1) {
                            System.out.println("StampTreeTransferHandler: index is -1");
                            return false;
                        }

                        // 同じ親の場合，下に移動した場合 index が変わる
                        if (sourceNode.getParent() == newParent && insertPosition != Insert.INTO_FOLDER) {
                            int sourceRow = tree.getRowForPath(new TreePath(sourceNode.getPath()));
                            int targetRow = tree.getRowForPath(targetPath);
                            if (sourceRow < targetRow) index--;
                        }

                        switch(insertPosition) {
                            case BEFORE:
                                model.removeNodeFromParent(sourceNode);
                                model.insertNodeInto(sourceNode, newParent, index);
                                break;
                            case AFTER:
                                model.removeNodeFromParent(sourceNode);
                                model.insertNodeInto(sourceNode, newParent, index+1);
                                break;
                            case INTO_FOLDER: //最後の子として挿入
                                model.removeNodeFromParent(sourceNode);
                                model.insertNodeInto(sourceNode, targetNode, targetNode.getChildCount());
                        }
                        TreeNode[] path = model.getPathToRoot(sourceNode);
                        ((JTree) tree).setSelectionPath(new TreePath(path));
                    }
                    return true;

                // KartePaneからDropされたオーダをインポートする
                } else if (tr.isDataFlavorSupported(orderFlavor)) {

                    OrderList list = (OrderList) tr.getTransferData(OrderListTransferable.orderListFlavor);
                    ModuleModel droppedStamp = list.orderList[0];

                    // 同一エンティティの場合、選択は必ず起っている
                    if (droppedStamp.getModuleInfo().getEntity().equals(targetEntity)) {
                        return tree.addStamp(droppedStamp, targetNode);

                    // パス Tree の場合
                    } else if (targetEntity.equals(IInfoModel.ENTITY_PATH)) {
                        if (targetNode == null) {
                            targetNode = (StampTreeNode) tree.getModel().getRoot();
                        }
                        return tree.addStamp(droppedStamp, targetNode);

                    } else {
                        // Rootの最後に追加する
                        return tree.addStamp(droppedStamp, null);
                    }

                // KartePaneからDropされたテキストをインポートする
                } else if (tr.isDataFlavorSupported(stringFlavor)) {

                    String text = (String) tr.getTransferData(DataFlavor.stringFlavor);
                    if (targetEntity.equals(IInfoModel.ENTITY_TEXT)) {
                        return tree.addTextStamp(text, targetNode);
                    } else {
                        return tree.addTextStamp(text, null);
                    }

                // DiagnosisEditorからDropされた病名をインポートする
                } else if (tr.isDataFlavorSupported(infoModelFlavor)) {

                    RegisteredDiagnosisModel rd = (RegisteredDiagnosisModel) tr.getTransferData(InfoModelTransferable.infoModelFlavor);
                    if (targetEntity.equals(IInfoModel.ENTITY_DIAGNOSIS)) {
                        return tree.addDiagnosis(rd, targetNode);
                    } else {
                        return tree.addDiagnosis(rd, null);
                    }
                } else {
                    return false;
                }

            } catch (IOException ex) {
                System.out.println("StampTreeTransferHandler.java: "+ ex);
                ex.printStackTrace();
            } catch (UnsupportedFlavorException ex) {
                System.out.println("StampTreeTransferHandler.java: "+ ex);
            }
        }
        return false;
    }

    /**
     * DnD後、Dragしたノードを元のStamptreeから削除する。
     */
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
    }

    /**
     * インポート可能かどうかを返す。
     */
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {

        boolean isLocked = ((StampTree)c).getStampBox().isLocked();
        return isFlavorMatched(flavors) && !isLocked && targetPath != null;
    }

    private boolean isFlavorMatched(DataFlavor[] flavors) {
        for (DataFlavor flavor : flavors) {
            if (stampTreeNodeFlavor.equals(flavor)) return true;
            if (orderFlavor.equals(flavor)) return true;
            if (stringFlavor.equals(flavor)) return true;
            if (infoModelFlavor.equals(flavor)) return true;
        }
        return false;
    }

    /**
     * 半透明のフィードバックを返す
     * @param t
     * @return
     */
    @Override
    public Icon getVisualRepresentation(Transferable t) {
        if (draggedComp == null) return null;

        int width = draggedComp.getWidth();
        int height = draggedComp.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        draggedComp.paint(image.getGraphics());
        return new ImageIcon(image);
    }
}

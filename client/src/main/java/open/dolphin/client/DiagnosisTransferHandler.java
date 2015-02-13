package open.dolphin.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.InfoModelTransferable;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.PatchedTransferHandler;

/**
 * DiagnosisTransferHandler
 *
 * @author Minagawa,Kazushi
 *
 */
public class DiagnosisTransferHandler extends PatchedTransferHandler {
    private static final long serialVersionUID = 1L;

    private JTable sourceTable;

    private RegisteredDiagnosisModel dragItem;

    private boolean shouldRemove;

    private DiagnosisDocument parent;

    private JComponent draggedComp = null;

    public DiagnosisTransferHandler(DiagnosisDocument parent) {
        super();
        this.parent = parent;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        sourceTable = (JTable) c;
        ObjectReflectTableModel tableModel = (ObjectReflectTableModel) sourceTable.getModel();
        dragItem = (RegisteredDiagnosisModel) tableModel.getObject(sourceTable.getSelectedRow());
        return dragItem != null ? new InfoModelTransferable(dragItem) : null;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    public boolean importData(JComponent c, Transferable t) {

        try {
            // 病名の挿入位置を決めておく
            // canImport で得た選択行に挿入（DiagnosisDocument#importStamp では使ってないんだけど）
            JTable dropTable = (JTable) c;
            int index = dropTable.getSelectedRow();
            index = 0;
            if (index < 0) {
                index = 0;
            }

            // Dropされたノードを取得する
            StampTreeNode droppedNode = (StampTreeNode) t.getTransferData(LocalStampTreeNodeTransferable.localStampTreeNodeFlavor);

            // Import するイストを生成する
            ArrayList<ModuleInfoBean> importList = new ArrayList<ModuleInfoBean>(3);

            // 葉の場合
            if (droppedNode.isLeaf()) {
                ModuleInfoBean stampInfo = (ModuleInfoBean) droppedNode.getStampInfo();
                if (stampInfo.getEntity().equals(IInfoModel.ENTITY_DIAGNOSIS)) {
                    if (stampInfo.isSerialized()) {
                        importList.add(stampInfo);
                    } else {
                        parent.openEditor2();
                        shouldRemove = false;
                        return true;
                    }

                } else {
                    Toolkit.getDefaultToolkit().beep();
                    return false;
                }

            } else {
                // Dropされたノードの葉を列挙する
                Enumeration e = droppedNode.preorderEnumeration();
                while (e.hasMoreElements()) {
                    StampTreeNode node = (StampTreeNode) e.nextElement();
                    if (node.isLeaf()) {
                        ModuleInfoBean stampInfo = (ModuleInfoBean) node.getStampInfo();
                        if (stampInfo.isSerialized() && (stampInfo.getEntity().equals(IInfoModel.ENTITY_DIAGNOSIS)) ) {
                            importList.add(stampInfo);
                            //System.out.println("StampId " + stampInfo.getStampId());
                        }
                    }
                }
            }
            // まとめてデータベースからフェッチしインポートする
            if (importList.size() > 0) {
                // index は今のところ機能していない
                parent.importStampList(importList, index);
                return true;

            } else {
                return false;
            }

        } catch (UnsupportedFlavorException ex) {
            System.out.println("DiagnosisTransferHandler.java: " + ex);
        } catch (IOException ioe) {
            System.out.println("DiagnosisTransferHandler.java: " + ioe);
            ioe.printStackTrace();
        }

        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        if (action == MOVE && shouldRemove) {
            ObjectReflectTableModel tableModel = (ObjectReflectTableModel) sourceTable.getModel();
            tableModel.deleteRow(dragItem);
        }
    }

    @Override
    //public boolean canImport(JComponent c, DataFlavor[] flavors) {
    public boolean canImport(TransferSupport support) {
        // drop position の選択をしないようにする
        support.setShowDropLocation(false);

        DataFlavor[] flavors = support.getDataFlavors();
        for (int i = 0; i < flavors.length; i++) {
            if (LocalStampTreeNodeTransferable.localStampTreeNodeFlavor.equals(flavors[i])) {
//                JTable t = (JTable) c;
//                t.getSelectionModel().setSelectionInterval(0,0);
                return true;
            }
        }
        return false;
    }

    /**
     * 半透明 drag のために dragged component とマウス位置を保存する
     * @param comp
     * @param e
     * @param action
     */
    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        JTable table = (JTable) comp;
        int row = table.getSelectedRow();
        int column = 0;
        TableCellRenderer r = table.getCellRenderer(row, column);

        Object value = table.getValueAt(row, column);
        boolean isSelected = false;
        boolean hasFocus = true;

        draggedComp = (JComponent) r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        draggedComp.setSize(table.getColumnModel().getColumn(column).getWidth(), table.getRowHeight(row));

        // calculate MousePosition
        Rectangle cellBounds = table.getCellRect(row, column, true);
        mousePosition = table.getMousePosition();
        if (mousePosition != null) {
            mousePosition.x -= cellBounds.x;
            mousePosition.y -= cellBounds.y;
        }

        super.exportAsDrag(comp, e, action);
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
        Graphics g = image.getGraphics();
        draggedComp.paint(g);

        // 文字列の長さに応じて幅を調節する
        int stringWidth = g.getFontMetrics().stringWidth(((DefaultTableCellRenderer)draggedComp).getText());
        if (stringWidth + 16 < width) { // 16 ドット余裕を持たせる
            width = stringWidth + 16;
            image = image.getSubimage(0, 0, width, height);
        }
        g.setColor(Color.gray);
        g.drawRect(0, 0, width-1, height-1);
        return new ImageIcon(image);
    }
}

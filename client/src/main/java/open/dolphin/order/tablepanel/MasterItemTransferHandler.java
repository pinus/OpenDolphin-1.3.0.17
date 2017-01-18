package open.dolphin.order.tablepanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import open.dolphin.order.MasterItem;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.PatchedTransferHandler;

/**
 * MasterItemTransferHandler.
 * @author Minagawa,Kazushi
 * @author pns
 */
public class MasterItemTransferHandler extends PatchedTransferHandler {
    private static final long serialVersionUID = 4871088750931696219L;

    private final DataFlavor masterItemFlavor = MasterItemTransferable.masterItemFlavor;

    private JTable sourceTable;
    private boolean shouldRemove;
    private int fromIndex;
    private int toIndex;

    private JComponent draggedComp = null;

    @Override
    protected Transferable createTransferable(JComponent c) {
        sourceTable = (JTable) c;
        ObjectReflectTableModel tableModel = (ObjectReflectTableModel) sourceTable.getModel();
        fromIndex = sourceTable.getSelectedRow();
        MasterItem dragItem = (MasterItem) tableModel.getObject(fromIndex);
        return dragItem != null ? new MasterItemTransferable(dragItem) : null;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (canImport(support)) {
            try {
                MasterItem dropItem = (MasterItem) support.getTransferable().getTransferData(masterItemFlavor);
                JTable dropTable = (JTable) support.getComponent();
                ObjectReflectTableModel tableModel = (ObjectReflectTableModel) dropTable.getModel();
                JTable.DropLocation dropLocation = (JTable.DropLocation) support.getDropLocation();

                toIndex = dropLocation.getRow();
                shouldRemove = (dropTable == sourceTable);
                if (shouldRemove) {
                    tableModel.moveRow(fromIndex, (toIndex>fromIndex)? --toIndex : toIndex);
                } else {
                    tableModel.addRow(toIndex, dropItem);
                }
                sourceTable.getSelectionModel().setSelectionInterval(toIndex, toIndex);
                return true;
            } catch (IOException | UnsupportedFlavorException e) {
                System.out.println("MasterItemTransferHandler.java: " + e);
            }
        }

        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        // doesn't work
        // JTable table = (JTable) c;
        // table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        shouldRemove = false;
        fromIndex = -1;
        toIndex = -1;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        JTable dropTable = (JTable) support.getComponent();
        boolean isDropable = false;

        DataFlavor[] flavors = support.getDataFlavors();
        ObjectReflectTableModel tableModel = (ObjectReflectTableModel) dropTable.getModel();
        if (tableModel.getObject(dropTable.getSelectedRow()) != null) {
            for (DataFlavor flavor : flavors) {
                if (masterItemFlavor.equals(flavor)) {
                    isDropable = true;
                    break;
                }
            }
        }
        // doesn't work
        // dropTable.setCursor(isDropable? DragSource.DefaultMoveDrop: DragSource.DefaultMoveNoDrop);
        return isDropable;
    }

    /**
     * 半透明 drag のために dragged component とマウス位置を保存する.
     * @param comp
     * @param e
     * @param action
     */
    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        JTable table = (JTable) comp;
        int row = table.getSelectedRow();
        int column = 1;
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
     * 半透明のフィードバックを返す.
     * @param t
     * @return
     */
    @Override
    public Icon getVisualRepresentation(Transferable t) {
        if (draggedComp == null) { return null; }

        int width = draggedComp.getWidth();
        int height = draggedComp.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = image.getGraphics();
        draggedComp.paint(g);

        // 文字列の長さに応じて幅を調節する
        int stringWidth = g.getFontMetrics().stringWidth(((DefaultTableCellRenderer)draggedComp).getText());
        if (stringWidth + 8< width) { // 8ドット余裕
            width = stringWidth +8;
            image = image.getSubimage(0, 0, width, height);
        }
        g.setColor(Color.gray);
        g.drawRect(0, 0, width-1, height-1);

        return new ImageIcon(image);
    }
}

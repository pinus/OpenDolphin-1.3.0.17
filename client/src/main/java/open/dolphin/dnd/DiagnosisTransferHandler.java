package open.dolphin.dnd;

import open.dolphin.client.DiagnosisDocument;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.stampbox.StampTreeNode;
import open.dolphin.ui.ObjectReflectTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

/**
 * DiagnosisTransferHandler
 *
 * @author Minagawa, Kazushi
 * @author pns
 */
public class DiagnosisTransferHandler extends DolphinTransferHandler {
        private Logger logger = LoggerFactory.getLogger(DiagnosisTransferHandler.class);

    private final DiagnosisDocument parent;
    private JTable sourceTable;
    private RegisteredDiagnosisModel dragItem;
    private boolean shouldRemove;

    public DiagnosisTransferHandler(DiagnosisDocument parent) {
        super();
        this.parent = parent;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        sourceTable = (JTable) c;
        ObjectReflectTableModel<RegisteredDiagnosisModel> tableModel
                = (ObjectReflectTableModel<RegisteredDiagnosisModel>) sourceTable.getModel();
        dragItem = tableModel.getObject(sourceTable.getSelectedRow());
        return dragItem != null ? new DiagnosisTransferable(dragItem) : null;
    }

    @Override
    public int getSourceActions(JComponent c) {
        JTable table = (JTable) c;
        int row = table.getSelectedRow();
        int column = 0;
        TableCellRenderer r = table.getCellRenderer(row, column);

        Object value = table.getValueAt(row, column);
        boolean isSelected = false;
        boolean hasFocus = true;

        JLabel draggedComp = (JLabel) r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        draggedComp.setSize(table.getColumnModel().getColumn(column).getWidth(), table.getRowHeight(row));

        // ?????????????????????????????????
        setDragImage(draggedComp, true);

        return COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) { return false; }

        try {
            // ???????????????????????????????????????
            // canImport ??????????????????????????????DiagnosisDocument#importStamp ????????????????????????????????????
            JTable dropTable = (JTable) support.getComponent();
            int index = dropTable.getSelectedRow();
            index = Math.max(index, 0);

            // Drop?????????????????????????????????
            StampTreeNode droppedNode =
                (StampTreeNode) support.getTransferable().getTransferData(DolphinDataFlavor.stampTreeNodeFlavor);

            // Import ??????????????????????????????
            List<ModuleInfoBean> importList = new ArrayList<>(3);

            // ????????????
            if (droppedNode.isLeaf()) {
                ModuleInfoBean stampInfo = droppedNode.getStampInfo();
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
                // Drop???????????????????????????????????????
                Enumeration e = droppedNode.preorderEnumeration();
                while (e.hasMoreElements()) {
                    StampTreeNode node = (StampTreeNode) e.nextElement();
                    if (node.isLeaf()) {
                        ModuleInfoBean stampInfo = node.getStampInfo();
                        if (stampInfo.isSerialized() && (stampInfo.getEntity().equals(IInfoModel.ENTITY_DIAGNOSIS))) {
                            importList.add(stampInfo);
                            //System.out.println("StampId " + stampInfo.getStampId());
                        }
                    }
                }
            }
            // ????????????????????????????????????????????????????????????????????????
            if (importList.size() > 0) {
                // index ???????????????????????????????????????
                parent.importStampList(importList, index);
                return true;

            } else {
                return false;
            }

        } catch (UnsupportedFlavorException | IOException ex) {
            ex.printStackTrace(System.err);
        }

        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        if (action == MOVE && shouldRemove) {
            ObjectReflectTableModel<RegisteredDiagnosisModel> tableModel
                    = (ObjectReflectTableModel<RegisteredDiagnosisModel>) sourceTable.getModel();
            tableModel.deleteRow(dragItem);
        }
    }

    @Override
    public boolean canImport(TransferSupport support) {
        // drop position ????????????????????????????????????
        if (support.isDrop()) { support.setShowDropLocation(false); }
        return Stream.of(support.getDataFlavors())
                .anyMatch(DolphinDataFlavor.stampTreeNodeFlavor::equals);
    }
}

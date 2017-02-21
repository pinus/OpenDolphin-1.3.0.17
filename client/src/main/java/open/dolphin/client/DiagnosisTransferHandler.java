package open.dolphin.client;

import open.dolphin.stampbox.LocalStampTreeNodeTransferable;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.InfoModelTransferable;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.stampbox.StampTreeNode;
import open.dolphin.ui.PNSTransferHandler;

/**
 * DiagnosisTransferHandler
 *
 * @author Minagawa,Kazushi
 * @author pns
 */
public class DiagnosisTransferHandler extends PNSTransferHandler {
    private static final long serialVersionUID = 1L;

    private JTable sourceTable;
    private RegisteredDiagnosisModel dragItem;
    private boolean shouldRemove;
    private final DiagnosisDocument parent;

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
            index = index < 0? 0 : index;

            // Dropされたノードを取得する
            StampTreeNode droppedNode = (StampTreeNode) t.getTransferData(LocalStampTreeNodeTransferable.localStampTreeNodeFlavor);

            // Import するイストを生成する
            List<ModuleInfoBean> importList = new ArrayList<>(3);

            // 葉の場合
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
                // Dropされたノードの葉を列挙する
                Enumeration e = droppedNode.preorderEnumeration();
                while (e.hasMoreElements()) {
                    StampTreeNode node = (StampTreeNode) e.nextElement();
                    if (node.isLeaf()) {
                        ModuleInfoBean stampInfo = node.getStampInfo();
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
            ioe.printStackTrace(System.err);
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
        // drop position の選択をしないようにする
        support.setShowDropLocation(false);

        return Arrays.asList(support.getDataFlavors()).stream()
                .anyMatch(flavor -> LocalStampTreeNodeTransferable.localStampTreeNodeFlavor.equals(flavor));
    }

    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        JTable table = (JTable) comp;
        int row = table.getSelectedRow();
        int column = 0;
        TableCellRenderer r = table.getCellRenderer(row, column);

        Object value = table.getValueAt(row, column);
        boolean isSelected = false;
        boolean hasFocus = true;

        JLabel draggedComp = (JLabel) r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        draggedComp.setSize(table.getColumnModel().getColumn(column).getWidth(), table.getRowHeight(row));

        // クリッピングありで設定
        setDragImage(draggedComp, true);

        super.exportAsDrag(comp, e, action);
    }
}

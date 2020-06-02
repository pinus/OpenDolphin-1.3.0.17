package open.dolphin.dnd;

import open.dolphin.stampbox.StampTree;
import open.dolphin.stampbox.StampTreeNode;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * AspStampTreeTransferHandler.
 *
 * @author Minagawa, Kazushi
 */
public class AspStampTreeNodeTransferHandler extends StampTreeNodeTransferHandler {
    private static final long serialVersionUID = 1L;

    @Override
    protected Transferable createTransferable(JComponent c) {
        StampTree sourceTree = (StampTree) c;
        StampTreeNode dragNode = (StampTreeNode) sourceTree.getLastSelectedPathComponent();
        return new StampTreeNodeTransferable(dragNode);
    }

    @Override
    public boolean importData(JComponent c, Transferable tr) {
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) { }

    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return false;
    }
}

package open.dolphin.stampbox;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * AspStampTreeTransferHandler.
 *
 * @author Minagawa, Kazushi
 */
public class AspStampTreeTransferHandler extends StampTreeTransferHandler {
    private static final long serialVersionUID = 1L;

    @Override
    protected Transferable createTransferable(JComponent c) {
        StampTree sourceTree = (StampTree) c;
        StampTreeNode dragNode = (StampTreeNode) sourceTree.getLastSelectedPathComponent();
        return new LocalStampTreeNodeTransferable(dragNode);
    }

    @Override
    public boolean importData(JComponent c, Transferable tr) {
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
    }

    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return false;
    }
}

package open.dolphin.stampbox;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Tranferable class of the StampTreeNode.
 *
 * @author  Kazushi Minagawa
 */
public class LocalStampTreeNodeTransferable implements Transferable {

    /** Data Flavor of this class */
    public static final DataFlavor localStampTreeNodeFlavor = new DataFlavor(LocalStampTreeNodeTransferable.class, "Local StampTree");
    private static final DataFlavor[] flavors = { LocalStampTreeNodeTransferable.localStampTreeNodeFlavor };

    private final StampTreeNode node;

    /**
     * Creates new StampTreeTransferable.
     * @param node
     */
    public LocalStampTreeNodeTransferable(StampTreeNode node) {
        this.node = node;
    }

    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)  {
        return flavor.equals(localStampTreeNodeFlavor);
    }

    @Override
    public synchronized Object getTransferData(DataFlavor flavor)
	    throws UnsupportedFlavorException, IOException {

        if (flavor.equals(localStampTreeNodeFlavor)) {
            return node;

        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}

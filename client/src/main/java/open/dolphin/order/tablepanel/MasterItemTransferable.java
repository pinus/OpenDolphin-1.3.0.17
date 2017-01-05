package open.dolphin.order.tablepanel;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import open.dolphin.order.MasterItem;

/**
 * マスタアイテム Transferable クラス.
 * @author kazm
 */
public class MasterItemTransferable implements Transferable {

    public static final DataFlavor MASTER_ITEM_FLAVOR = new DataFlavor(MasterItem.class, "MasterItem");
    private static final DataFlavor[] FLAVORS = { MASTER_ITEM_FLAVOR };
    private final MasterItem masterItem;

    public MasterItemTransferable(MasterItem masterItem) {
        this.masterItem = masterItem;
    }

    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(MASTER_ITEM_FLAVOR);
    }

    @Override
    public synchronized Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {

        if (flavor.equals(MASTER_ITEM_FLAVOR)) {
            return masterItem;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}

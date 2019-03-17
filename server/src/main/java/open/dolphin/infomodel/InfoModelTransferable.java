package open.dolphin.infomodel;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * Transferable class of the IInfoModel.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class InfoModelTransferable implements Transferable, ClipboardOwner {

    /**
     * Data Flavor of this class
     */
    public static final DataFlavor infoModelFlavor = new DataFlavor(open.dolphin.infomodel.IInfoModel.class, "Info Model");

    private static final DataFlavor[] flavors = {InfoModelTransferable.infoModelFlavor};

    private final IInfoModel model;

    public InfoModelTransferable(IInfoModel model) {
        this.model = model;
    }

    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(infoModelFlavor);
    }

    @Override
    public synchronized Object getTransferData(DataFlavor flavor) {
        return (flavor.equals(infoModelFlavor)) ? model : null;
    }

    @Override
    public String toString() {
        return "InfoModelTransferable";
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}

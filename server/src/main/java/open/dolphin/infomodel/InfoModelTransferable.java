package open.dolphin.infomodel;

import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Transferable class of the IInfoModel.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class InfoModelTransferable implements Transferable, ClipboardOwner {

    /** Data Flavor of this class */
    public static final DataFlavor infoModelFlavor = new DataFlavor(open.dolphin.infomodel.IInfoModel.class, "Info Model");

    public static final DataFlavor[] flavors = {InfoModelTransferable.infoModelFlavor};

    private final IInfoModel model;

    public InfoModelTransferable(IInfoModel model) {
        this.model = model;
    }

    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
    	return flavors;
    }

    @Override
    public boolean isDataFlavorSupported( DataFlavor flavor ) {
    	return flavor.equals(infoModelFlavor);
    }

    @Override
    public synchronized Object getTransferData(DataFlavor flavor)
	    throws UnsupportedFlavorException, IOException {

        if (flavor.equals(infoModelFlavor)) {
            return model;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public String toString() {
        return "InfoModelTransferable";
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
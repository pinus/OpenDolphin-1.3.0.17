package open.dolphin.client;

import javax.swing.*;
import java.awt.datatransfer.*;
import java.io.IOException;


/**
 * Transferable class of the ImageIcon.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class ImageIconTransferable implements Transferable, ClipboardOwner {

    /** Data Flavor of this class */
    public static DataFlavor imageIconFlavor = new DataFlavor(javax.swing.ImageIcon.class, "Image Icon");

    public static final DataFlavor[] flavors = {ImageIconTransferable.imageIconFlavor};

    private ImageIcon icon;

    /** Creates new ImgeIconTransferable */
    public ImageIconTransferable(ImageIcon icon) {
        this.icon = icon;
    }

    public synchronized DataFlavor[] getTransferDataFlavors() {
	return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
	return flavor.equals(imageIconFlavor) ? true : false;
    }

    public synchronized Object getTransferData(DataFlavor flavor)
	    throws UnsupportedFlavorException, IOException {

        if (flavor.equals(imageIconFlavor)) {
            return icon;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public String toString() {
        return "Image Icon Transferable";
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}

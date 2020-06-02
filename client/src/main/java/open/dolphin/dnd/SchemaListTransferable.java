package open.dolphin.dnd;

import open.dolphin.client.SchemaList;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Transferable class of the Schema list.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class SchemaListTransferable implements Transferable, ClipboardOwner {
    private static final DataFlavor[] flavors = { DolphinDataFlavor.schemaListFlavor };

    private SchemaList list;

    public SchemaListTransferable(SchemaList list) {
        this.list = list;
    }

    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(DolphinDataFlavor.schemaListFlavor);
    }

    @NotNull
    @Override
    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {

        if (flavor.equals(DolphinDataFlavor.schemaListFlavor)) {
            return list;

        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public String toString() {
        return "Icon List Transferable";
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        list = null;
    }
}

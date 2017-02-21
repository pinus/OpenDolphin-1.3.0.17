package open.dolphin.client;

import java.awt.datatransfer.*;
import javax.swing.*;
import open.dolphin.infomodel.SchemaModel;
import open.dolphin.ui.PNSTransferHandler;

/**
 * SchemaHolderTransferHandler
 *
 * @author Kazushi Minagawa
 * @author pns
 */
public class SchemaHolderTransferHandler extends PNSTransferHandler {
    private static final long serialVersionUID = -1293765478832142035L;

    public SchemaHolderTransferHandler() {
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        SchemaHolder source = (SchemaHolder) c;
        KartePane context = source.getKartePane();
        context.setDraggedStamp(new ComponentHolder[]{source});
        context.setDraggedCount(1);
        SchemaModel schema = source.getSchema();
        SchemaList list = new SchemaList();
        list.setSchemaList(new SchemaModel[]{ schema });
        Transferable tr = new SchemaListTransferable(list);
        return tr;
    }

    @Override
    public int getSourceActions(JComponent c) {
        setDragImage((JLabel)c);
        return COPY_OR_MOVE;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        if (action == NONE) {
            return;
        }

        if (action == MOVE) {
            SchemaHolder test = (SchemaHolder) c;
            KartePane context = test.getKartePane();

            if (context.getComponent().isEditable()) {
                context.removeSchema(test);
            }
            context.setDraggedStamp(null);
            context.setDraggedCount(0);
            context.setDroppedCount(0);
        }
    }

    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return false;
    }

    /**
     * スタンプをクリップボードへ転送する.
     */
    @Override
    public void exportToClipboard(JComponent comp, Clipboard clip, int action) {
        SchemaHolder sh = (SchemaHolder) comp;
        Transferable tr = createTransferable(comp);
        clip.setContents(tr, null);

        if (action == MOVE) {
            KartePane kartePane = sh.getKartePane();
            if (kartePane.getTextPane().isEditable()) {
                kartePane.removeSchema(sh);
            }
        }
    }
}

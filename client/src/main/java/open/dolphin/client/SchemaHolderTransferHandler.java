package open.dolphin.client;

import java.awt.datatransfer.*;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

import open.dolphin.infomodel.SchemaModel;
import open.dolphin.ui.PatchedTransferHandler;


/**
 * SchemaHolderTransferHandler
 *
 * @author Kazushi Minagawa
 *
 */
public class SchemaHolderTransferHandler extends PatchedTransferHandler {
    private static final long serialVersionUID = -1293765478832142035L;

    private JComponent draggedComp = null; // drag の際に透明フィードバックをかけるために使う

    public SchemaHolderTransferHandler() {
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        SchemaHolder source = (SchemaHolder) c;
        KartePane context = source.getKartePane();
        context.setDrragedStamp(new ComponentHolder[]{source});
        context.setDraggedCount(1);
        SchemaModel schema = source.getSchema();
        SchemaList list = new SchemaList();
        list.schemaList = new SchemaModel[]{schema};
        Transferable tr = new SchemaListTransferable(list);
        return tr;
    }

    @Override
	public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        SchemaHolder test = (SchemaHolder) c;
        KartePane context = test.getKartePane();
        if (action == MOVE &&
                context.getDrragedStamp() != null &&
                context.getDraggedCount() == context.getDroppedCount()) {
            context.removeSchema(test); // TODO
        }
        context.setDrragedStamp(null);
        context.setDraggedCount(0);
        context.setDroppedCount(0);
    }

    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return false;
    }

    /**
     * スタンプをクリップボードへ転送する。
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

    /**
     * 半透明 drag のために dragged component とマウス位置を保存する
     * @param comp
     * @param e
     * @param action
     */
    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        draggedComp = comp;
        mousePosition = ((MouseEvent)e).getPoint();
        super.exportAsDrag(comp, e, action);
    }

    /**
     * 半透明のフィードバックを返す
     * @param t
     * @return
     */
    @Override
    public Icon getVisualRepresentation(Transferable t) {
        if (draggedComp == null) return null;

        int width = draggedComp.getWidth();
        int height = draggedComp.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        draggedComp.paint(image.getGraphics());
        return new ImageIcon(image);
    }
}

package open.dolphin.dnd;

import open.dolphin.client.KartePane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class KartePaneTransferHandler extends DolphinTransferHandler {
    private static final long serialVersionUID = 1L;

    public enum Pane { SOA, P };
    private Logger logger = LoggerFactory.getLogger(SOATransferHandler.class);

    private KartePane kartePane;
    private Pane pane;
    private JTextPane source;
    private boolean shouldRemove;

    // Start and end position in the source text.
    // We need this information when performing a MOVE
    // in order to remove the dragged text from the source.
    private Position p0 = null, p1 = null;

    public KartePaneTransferHandler(KartePane kp, Pane p) {
        kartePane = kp;
        pane = p;
    }
tochu
    /**
     * Create a Transferable implementation that contains the selected text.
     *
     * @param c source JTextPane
     * @return Transferable of selected text
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        source = (JTextPane) c;
        int start = source.getSelectionStart();
        int end = source.getSelectionEnd();
        Document doc = source.getDocument();
        if (start == end) {
            return null;
        }
        try {
            p0 = doc.createPosition(start);
            p1 = doc.createPosition(end);
        } catch (BadLocationException e) {
            logger.error(e.getMessage());
        }
        String data = source.getSelectedText();
        return new StringSelection(data);
    }



}

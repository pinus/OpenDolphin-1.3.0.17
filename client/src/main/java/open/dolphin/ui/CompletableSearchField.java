package open.dolphin.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author pns
 */
public class CompletableSearchField extends CompletableJTextField {
    private static final long serialVersionUID = 1L;

    public CompletableSearchField(int col) {
        super(col);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();


        g.dispose();
    }
}

package open.dolphin.ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JProgressBar;

/**
 * 最下段のステータスパネル
 * +---------------+
 * | Command Panel |
 * |---------------|
 * |               |
 * | Main Panel    |
 * |               |
 * |---------------|
 * | Status Panel  |
 * +---------------+
 * @author pns
 */
public class StatusPanel extends HorizontalPanel {
    private static final long serialVersionUID = 1L;
    private static final int STATUS_PANEL_HEIGHT = 24;

    private final JProgressBar progressBar;

    public StatusPanel() {
        setPreferredSize(new Dimension(100, STATUS_PANEL_HEIGHT));
        setMinimumSize(new Dimension(0, STATUS_PANEL_HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, STATUS_PANEL_HEIGHT));
        setFontSize(11);

        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(100, 12));
        progressBar.setMinimumSize(new Dimension(100, 12));
        progressBar.setMaximumSize(new Dimension(100, 12));
    }

    public void addProgressBar() {
        this.add(progressBar);
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }
}

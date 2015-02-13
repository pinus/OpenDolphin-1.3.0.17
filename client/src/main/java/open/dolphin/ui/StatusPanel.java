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

    private JProgressBar progressBar;

    public StatusPanel() {
        this.setPreferredSize(new Dimension(100, STATUS_PANEL_HEIGHT));
        this.setMinimumSize(new Dimension(0, STATUS_PANEL_HEIGHT));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, STATUS_PANEL_HEIGHT));
        this.setFontSize(11);
        this.setBackgroundColor(Color.black, 0.0f, DEFAULT_STATUS_PANEL_END_ALPHA);

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

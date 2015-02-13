package open.dolphin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

/**
 * ３段構造フレーム
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
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    // Panels
    private CommandPanel commandPanel;
    private MainPanel mainPanel;
    private StatusPanel statusPanel;

    public MainFrame() {
        //setBackground(new Color(0,0,0,0));
        //AWTAccessor.getWindowAccessor().setOpacity(this, 0.5f);
        this(true,true);
    }

    public MainFrame(String title) {
        this(true,true);
        this.setTitle(title);
    }

    public MainFrame(boolean commandPanelNeeded, boolean statusPanelNeeded) {
        initComponents(commandPanelNeeded, statusPanelNeeded);
    }

    private void initComponents(boolean commandPanelNeeded, boolean statusPanelNeeded) {

        // コマンドパネル
        if (commandPanelNeeded) commandPanel = new CommandPanel();

        // メインパネル
        mainPanel = new MainPanel();

        // ステータスパネル
        if (statusPanelNeeded) statusPanel = new StatusPanel();

        // 全体をレイアウトする
        this.setLayout(new BorderLayout(0,0));
        if (commandPanelNeeded) this.add(commandPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        if (statusPanelNeeded) this.add(statusPanel, BorderLayout.SOUTH);

        // フォーカスに応じてパネルの影を変える
        this.addWindowFocusListener(new WindowFocusListener(){
            public void windowGainedFocus(WindowEvent e) {
                setFocusedRecursive(getComponents(), true);
                repaint();
            }

            public void windowLostFocus(WindowEvent e) {
                setFocusedRecursive(getComponents(), false);
                repaint();
            }
        });
    }

    /**
     * 含んでいるすべての HorizontalPanel に isFocused を通知する
     * @param components
     * @param isFocused
     */
    private static void setFocusedRecursive(Component[] components, boolean isFocused) {
        for (Component c : components) {
            if (c instanceof HorizontalPanel) ((HorizontalPanel)c).setFocused(isFocused);
            if (c instanceof Container)
                setFocusedRecursive(((Container)c).getComponents(), isFocused);
        }
    }

    /**
     * 上部のコマンドパネル
     */
    public class CommandPanel extends HorizontalPanel {
        private static final long serialVersionUID = 1L;

        public CommandPanel() {
            this.setBackgroundColor(Color.BLACK, 0.0f, DEFAULT_COMMAND_PANEL_END_ALPHA);
        }
    }
    /**
     * メインパネル
     */
    public class MainPanel extends JPanel {
        public MainPanel() {
            super();
        }
    }

    /** CommandPanel を返す */
    public CommandPanel getCommandPanel() {
        return commandPanel;
    }
    /** MainPanel を返す */
    public MainPanel getMainPanel() {
        return mainPanel;
    }
    /** StatusPanel を返す */
    public StatusPanel getStatusPanel() {
        return statusPanel;
    }
    /** この frame を返す */
    public MainFrame getFrame() {
        return this;
    }
    /** StatusPanel を作る */
    public void createStatusPanel() {
        if (statusPanel != null) this.remove(statusPanel);
        statusPanel = new StatusPanel();
        this.add(commandPanel, BorderLayout.NORTH);
    }
    /** StatusPanel を消す */
    public void removeStatusPanel() {
        this.remove(statusPanel);
        statusPanel = null;
    }
    /** CommandPanel を作る */
    public void createCommandPanel() {
        if (commandPanel != null) this.remove(commandPanel);
        commandPanel = new CommandPanel();
        this.add(commandPanel, BorderLayout.NORTH);
    }
    /** StatusPanel を消す */
    public void removeCommandPanel() {
        this.remove(commandPanel);
        commandPanel = null;
    }

    public static void main(String[] argv) {

        MainFrame f = new MainFrame();
        f.setSize(600, 700);

        f.setTitle("テスト〜タイトル");

        CommandPanel commandPanel = f.getCommandPanel();
        commandPanel.setPanelHeight(56);
        for (int i=0; i<5; i++) {
            JButton b = new JButton("TEST" + String.valueOf(i));
            commandPanel.add(b);
        }

        commandPanel.addGlue();
        commandPanel.add(new JButton("TEST"));
        commandPanel.setBottomLineAlpha(0.4f);

        MainPanel mainPanel = f.getMainPanel();
        mainPanel.setLayout(new BorderLayout(0,0));
        mainPanel.add(new JTextPane(), BorderLayout.CENTER);

        StatusPanel statusPanel = f.getStatusPanel();
        statusPanel.add("TEST");
        statusPanel.addSeparator();
        statusPanel.add("test2");
        statusPanel.addGlue();
        statusPanel.addProgressBar();
        statusPanel.addSeparator();
        statusPanel.add("2011-10-21", "3rdLabel");
        statusPanel.setMargin(8);
        statusPanel.setTopLineAlpha(0.4f);

        statusPanel.setText("ラベル変更", "0");
        statusPanel.setText("ラベル", "1");
        statusPanel.setText("2011-10-27", "3rdLabel");

        f.setVisible(true);
    }
}

package open.dolphin.ui;

import open.dolphin.client.Dolphin;
import open.dolphin.client.GUIConst;

import javax.swing.*;
import java.awt.*;

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
 *
 * @author pns
 */
public class PNSFrame extends JFrame {
    // Panels
    private CommandPanel commandPanel;
    private MainPanel mainPanel;
    private StatusPanel statusPanel;

    public PNSFrame() {
        this("", true, true);
    }

    public PNSFrame(String title) {
        this(title, true, true);
    }

    public PNSFrame(String title, boolean commandPanelNeeded, boolean statusPanelNeeded) {
        initComponents(title, commandPanelNeeded, statusPanelNeeded);
    }

    private void initComponents(String title, boolean commandPanelNeeded, boolean statusPanelNeeded) {
        getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);
        setTitle(title);
        setGlassPane(new BlockGlass2());

        if (Dolphin.forWin) { setIconImage(GUIConst.ICON_DOLPHIN.getImage()); }

        // コマンドパネル
        if (commandPanelNeeded) { commandPanel = new CommandPanel(); }

        // メインパネル
        mainPanel = new MainPanel();

        // ステータスパネル
        if (statusPanelNeeded) { statusPanel = new StatusPanel(); }

        // 全体をレイアウトする
        this.setLayout(new BorderLayout(0, 0));
        if (commandPanelNeeded) {
            this.add(commandPanel, BorderLayout.NORTH);
        }
        this.add(mainPanel, BorderLayout.CENTER);
        if (statusPanelNeeded) {
            this.add(statusPanel, BorderLayout.SOUTH);
        }
    }

    /**
     * CommandPanel を返す
     *
     * @return Command Panel
     */
    public CommandPanel getCommandPanel() {
        return commandPanel;
    }

    /**
     * MainPanel を返す
     *
     * @return Main Panel
     */
    public MainPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * StatusPanel を返す
     *
     * @return Status Panel
     */
    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    /**
     * StatusPanel を作る
     */
    public void createStatusPanel() {
        if (statusPanel != null) {
            this.remove(statusPanel);
        }
        statusPanel = new StatusPanel();
        this.add(commandPanel, BorderLayout.NORTH);
    }

    /**
     * StatusPanel を消す
     */
    public void removeStatusPanel() {
        this.remove(statusPanel);
        statusPanel = null;
    }

    /**
     * CommandPanel を作る
     */
    public void createCommandPanel() {
        if (commandPanel != null) {
            this.remove(commandPanel);
        }
        commandPanel = new CommandPanel();
        this.add(commandPanel, BorderLayout.NORTH);
    }

    /**
     * StatusPanel を消す
     */
    public void removeCommandPanel() {
        this.remove(commandPanel);
        commandPanel = null;
    }

    /**
     * 上部のコマンドパネル
     */
    public static class CommandPanel extends HorizontalPanel {

        public CommandPanel() {
        }
    }

    /**
     * メインパネル
     */
    public static class MainPanel extends JPanel {

        public MainPanel() {
            super();
        }
    }

    public static void main(String[] argv) {
        open.dolphin.client.ClientContext.setClientContextStub(new open.dolphin.client.ClientContextStub());

        PNSFrame f = new PNSFrame();
        f.setSize(600, 700);

        f.setTitle("テスト〜タイトル");

        CommandPanel commandPanel = f.getCommandPanel();
        commandPanel.setPanelHeight(56);
        for (int i = 0; i < 5; i++) {
            JButton b = new JButton("TEST" + i);
            commandPanel.add(b);
        }

        commandPanel.addGlue();
        commandPanel.add(new JButton("TEST"));

        MainPanel mainPanel = f.getMainPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
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

        statusPanel.setText("ラベル変更", "0");
        statusPanel.setText("ラベル", "1");
        statusPanel.setText("2011-10-27", "3rdLabel");

        f.setVisible(true);
        Component bg = f.getGlassPane();
        bg.setVisible(true);
        try { Thread.sleep(3000); } catch (Exception e) {}
        bg.setVisible(false);
    }
}

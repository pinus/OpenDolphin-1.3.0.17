package open.dolphin.client;

import open.dolphin.ui.HorizontalPanel;
import open.dolphin.ui.MainFrame;
import open.dolphin.ui.StatusPanel;

import javax.swing.*;
import java.awt.*;

/**
 * WaitingList, PatientSearch, LaboTextImporter で使う共通パネル骨格
 * MainFrame に格納される TabbedPane の中に入る
 * +-----------------+
 * | commandPanel    |
 * |-----------------|
 * |                 |
 * | mainPanel       |
 * |                 |
 * |-----------------|
 * | statusPanel     |
 * +-----------------+
 *
 * @author pns
 */
public class MainComponentPanel extends JPanel {

    private static final int COMMAND_PANEL_HEIGHT = 36;

    private CommandPanel commandPanel;
    private MainPanel mainPanel;
    private StatusPanel statusPanel;

    public MainComponentPanel() {
        initComponents();
    }

    private void initComponents() {
        commandPanel = new CommandPanel();
        mainPanel = new MainPanel();
        statusPanel = new StatusPanel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(commandPanel);
        add(mainPanel);
        add(statusPanel);
    }

    public CommandPanel getCommandPanel() {
        return commandPanel;
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    /**
     * ボタン等が入るコマンドパネル
     */
    public class CommandPanel extends HorizontalPanel {

        public CommandPanel() {
            super();
            this.setPanelHeight(COMMAND_PANEL_HEIGHT);
        }
    }

    /**
     * Table の入るメインパネル
     */
    public class MainPanel extends JPanel {
        public MainPanel() {
            setLayout(new BorderLayout(0, 0));
        }

        @Override
        public Component add(Component c) {
            add(c, BorderLayout.CENTER);
            return c;
        }
    }

    public static void main(String[] argv) {
        MainFrame f = new MainFrame("", false, false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(600, 800);

        MainComponentPanel p = new MainComponentPanel();
        MainComponentPanel.CommandPanel com = p.getCommandPanel();
        StatusPanel status = p.getStatusPanel();

        JLabel l1 = new JLabel("TEST1");
        JLabel l2 = new JLabel("TEST2");
        JLabel l3 = new JLabel("TEST3");
        JButton b1 = new JButton("TEST4");
        JButton b2 = new JButton("TEST4");
        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
        tf.setPreferredSize(new Dimension(100, Integer.MAX_VALUE));
        JProgressBar bar = new JProgressBar();

        status.add(l1);
        status.addSeparator();
        status.add(l2);
        status.addGlue();
        status.add(bar);
        status.addSeparator();
        status.add(l3);
        status.setMargin(8);

        com.add(b1);
        com.addGlue();
        com.add(tf);
        com.add(b2);

        f.add(p);

        f.setVisible(true);
    }
}

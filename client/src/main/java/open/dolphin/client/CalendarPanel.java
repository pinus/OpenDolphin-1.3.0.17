package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.plaf.LayerUI;

/**
 * コントローラー付きの CalendarPanel.
 * @author pns
 */
public class CalendarPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private int monthDiff;

    public CalendarPanel() {
        this(0);
    }

    public CalendarPanel(int differential) {
        monthDiff = differential;
        initComponents();
    }

    private void initComponents() {
        CalendarTable table = new CalendarTable(monthDiff);
        CalendarTableModel tableModel = (CalendarTableModel) table.getModel();

        // control panel 生成
        JButton nextWeek = createButton(GUIConst.ICON_MD_FORWARD_16, e -> tableModel.nextWeek());
        JButton nextMonth = createButton(GUIConst.ICON_MD_FAST_FORWARD_16, e -> tableModel.nextMonth());
        JButton prevWeek = createButton(GUIConst.ICON_MD_BACKWARD_16, e -> tableModel.previousWeek());
        JButton prevMonth = createButton(GUIConst.ICON_MD_FAST_BACKWARD_16, e -> tableModel.previousMonth());
        JButton reset = createButton(GUIConst.ICON_MD_STOP_16, e -> tableModel.reset());

        JPanel controlPanel = new JPanel();
        // 境界線を描く
        LayerUI<JPanel> layerUI = new LayerUI<JPanel>() {
            private static final long serialVersionUID = 1L;
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(0, 0, 0, c.getHeight()-1);
            }
        };
        JLayer<JPanel> controlLayer = new JLayer<>(controlPanel, layerUI);

        // Control Panel を作る
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(prevMonth);
        controlPanel.add(prevWeek);
        controlPanel.add(Box.createVerticalGlue());
        controlPanel.add(reset);
        controlPanel.add(Box.createVerticalGlue());
        controlPanel.add(nextWeek);
        controlPanel.add(nextMonth);
        Dimension size = controlPanel.getPreferredSize();
        size.width = 16;
        controlPanel.setPreferredSize(size);
        controlPanel.setMaximumSize(size);
        controlPanel.setMinimumSize(size);

        // CalendarPanel 生成
        setLayout(new BorderLayout());
        add(table.getPanel(), BorderLayout.CENTER);
        add(controlLayer, BorderLayout.EAST);
    }

    private JButton createButton(ImageIcon icon, ActionListener l) {
        JButton button = new JButton(icon);
        button.setBorderPainted(false);
        button.addActionListener(l);
        return button;
    }


    public static void main (String[] arg) {
        open.dolphin.client.ClientContext.setClientContextStub(new open.dolphin.client.ClientContextStub());

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CalendarPanel panel = new CalendarPanel();

        f.add(panel);
        f.pack();
        f.setVisible(true);
    }
}

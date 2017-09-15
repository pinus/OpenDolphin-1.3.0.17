package open.dolphin.inspector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author pns
 */
public class InspectorPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Insets BORDER_INSETS = new Insets(0,10,0,10);
    private JPanel titlePanel;
    private JPanel contentPanel;

    public InspectorPanel() {
        initComponents();
    }

    private void initComponents() {
        titlePanel = new JPanel();
        titlePanel.setOpaque(true);
        titlePanel.setBackground(Color.GRAY);

        contentPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, getWidth(), getHeight());
            }
        };

        setBorder(new EmptyBorder(BORDER_INSETS));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(titlePanel);
        add(contentPanel);


    }

    public void setTitle(String title) {
        JLabel label = new JLabel(title);
        titlePanel.add(label, BorderLayout.WEST);
    }

    public void setContent(JComponent c) {
        contentPanel.add(c, BorderLayout.CENTER);
    }

    public JPanel getTitlePanel() {
        return titlePanel;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}

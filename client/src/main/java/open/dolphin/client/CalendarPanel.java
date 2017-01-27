package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.plaf.LayerUI;
import open.dolphin.infomodel.SimpleDate;

/**
 * CalendarTable にコントローラーを付けたパネル.
 * @author pns
 */
public class CalendarPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final int BUTTON_WIDTH = 12;

    private CalendarTable table;
    private CalendarTableModel tableModel;
    private CalendarListener listener;

    public CalendarPanel() {
        initComponents();
    }

    private void initComponents() {
        table = new CalendarTable();
        tableModel = (CalendarTableModel) table.getModel();

        // control panel 生成
        JButton expand = new JButton(GUIConst.ICON_MD_EJECT_16);
        expand.setBorderPainted(false);
        expand.addActionListener(e -> expand());
        expand.setPreferredSize(new Dimension(12,16));
        expand.setMinimumSize(new Dimension(12,16));
        expand.setMaximumSize(new Dimension(12,16));

        JButton nextWeek = createButton(GUIConst.ICON_MD_FORWARD_16, e -> tableModel.nextWeek());
        JButton nextMonth = createButton(GUIConst.ICON_MD_FAST_FORWARD_16, e -> tableModel.nextMonth());
        JButton prevWeek = createButton(GUIConst.ICON_MD_BACKWARD_16, e -> tableModel.previousWeek());
        JButton prevMonth = createButton(GUIConst.ICON_MD_FAST_BACKWARD_16, e -> tableModel.previousMonth());
        JButton reset = createButton(GUIConst.ICON_MD_STOP_16, e -> tableModel.reset());

        // Control Panel を作る
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(true);
        controlPanel.setBackground(table.getBackground());
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(expand);
        controlPanel.add(prevMonth);
        controlPanel.add(prevWeek);
        controlPanel.add(Box.createVerticalGlue());
        controlPanel.add(reset);
        controlPanel.add(Box.createVerticalGlue());
        controlPanel.add(nextWeek);
        controlPanel.add(nextMonth);
        Dimension size = controlPanel.getPreferredSize();
        size.width = BUTTON_WIDTH;
        controlPanel.setPreferredSize(size);
        controlPanel.setMaximumSize(size);
        controlPanel.setMinimumSize(size);

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

        // CalendarPanel 生成
        setLayout(new BorderLayout());
        add(table.getPanel(), BorderLayout.CENTER);
        add(controlLayer, BorderLayout.EAST);
    }

    private JButton createButton(ImageIcon icon, ActionListener l) {
        JButton button = new JButton(icon);

        Dimension size = new Dimension(BUTTON_WIDTH, 18);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);

        button.setBorderPainted(false);
        button.addActionListener(e -> {
            l.actionPerformed(e);
            fireCalendarChanged();
        });
        return button;
    }

    /**
     * リスナに月が切り替わったことを知らせる.
     */
    private void fireCalendarChanged() {
        if (listener != null) {
            listener.dateSelected(new SimpleDate(tableModel.getYear(), tableModel.getMonth(), 1));
        }
    }

    /**
     * 3ヶ月分のカレンダーをポップアップする.
     */
    private void expand() {
        JPopupMenu popup = new JPopupMenu();
        GregorianCalendar gc = new GregorianCalendar(tableModel.getYear(), tableModel.getMonth(), 1);

        for (int i=0; i<3; i++) {
            CalendarTable tbl = new CalendarTable(gc);
            ((CalendarTableModel)tbl.getModel()).setMarkDates(tableModel.getMarkDates());

            Dimension size = new Dimension(table.getPanel().getWidth(), table.getPanel().getHeight());
            size.width -= 16;
            tbl.getPanel().setPreferredSize(size);

            tbl.addCalendarListener(date -> {
                // リスナのブリッジ
                CalendarListener l = table.getCalendarListener();
                if (l != null) { l.dateSelected(date); }
            });

            popup.add(tbl.getPanel(), 0);
            gc.add(Calendar.MONTH, -1);
        }
        Point p = getLocation();
        popup.show(this.getParent(), p.x, p.y - table.getPanel().getHeight()*2);
    }

    /**
     * 表示年月変更のリスナ.
     * @param l
     */
    public void addCalendarListener(CalendarListener l) {
        listener = l;
    }

    /**
     * CalendarTable を返す.
     * @return
     */
    public CalendarTable getTable() {
        return table;
    }

    /**
     * CalendarTableModel を返す.
     * @return
     */
    public CalendarTableModel getModel() {
        return tableModel;
    }

    public static void main (String[] arg) {
        open.dolphin.client.ClientContext.setClientContextStub(new open.dolphin.client.ClientContextStub());

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CalendarPanel panel = new CalendarPanel();

        panel.getTable().addCalendarListener(date -> {
            System.out.println(date.getYear() + " / " + date.getMonth() + " / " + date.getDay());
        });

        f.add(panel);
        f.pack();
        f.setVisible(true);
    }
}
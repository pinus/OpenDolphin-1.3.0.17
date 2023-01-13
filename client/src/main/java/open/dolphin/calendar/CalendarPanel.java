package open.dolphin.calendar;

import open.dolphin.client.Dolphin;
import open.dolphin.client.GUIConst;
import open.dolphin.infomodel.SimpleDate;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * CalendarTable にコントローラーを付けたパネル.
 *
 * @author pns
 */
public class CalendarPanel extends JPanel {

    private static final int BUTTON_WIDTH = 12;

    private CalendarTable table;
    private JPanel controlPanel;
    private CalendarTableModel tableModel;
    private CalendarListener listener;

    public CalendarPanel() {
        initComponents();
    }

    private void initComponents() {
        table = new CalendarTable();
        tableModel = (CalendarTableModel) table.getModel();

        // mouse wheel でカレンダースクロール
        table.addMouseWheelListener(e -> {
            int r = e.getWheelRotation();
            if (r > 0) {
                tableModel.nextWeek();
            } else if (r < 0) {
                tableModel.previousWeek();
            }
            fireCalendarChanged();
        });

        // control panel 生成
        JButton expand = new JButton(GUIConst.ICON_MD_EJECT_16);
        if (Dolphin.forWin) {
            expand.setContentAreaFilled(false);
        } else {
            expand.setBorderPainted(false);
        }
        expand.addActionListener(e -> expand());
        expand.setPreferredSize(new Dimension(12, 16));
        expand.setMinimumSize(new Dimension(12, 16));
        expand.setMaximumSize(new Dimension(12, 16));

        JButton nextWeek = createButton(GUIConst.ICON_MD_FORWARD_16, e -> tableModel.nextWeek());
        JButton nextMonth = createButton(GUIConst.ICON_MD_FAST_FORWARD_16, e -> tableModel.nextMonth());
        JButton prevWeek = createButton(GUIConst.ICON_MD_BACKWARD_16, e -> tableModel.previousWeek());
        JButton prevMonth = createButton(GUIConst.ICON_MD_FAST_BACKWARD_16, e -> tableModel.previousMonth());
        JButton reset = createButton(GUIConst.ICON_MD_STOP_16, e -> tableModel.reset());

        // Control Panel を作る
        controlPanel = new JPanel();
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
        LayerUI<JPanel> layerUI = new LayerUI<>() {

            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(0, 0, 0, c.getHeight() - 1);
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

        if (Dolphin.forWin) {
            button.setContentAreaFilled(false);
        } else {
            button.setBorderPainted(false);
        }
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
     * １年分のカレンダーをポップアップする.
     */
    private void expand() {
        JDialog dialog = new JDialog();
        dialog.getRootPane().putClientProperty("Window.style", "small");
        dialog.getRootPane().putClientProperty("apple.awt.transparentTitleBar", Boolean.TRUE);
        dialog.setIconImage(GUIConst.ICON_DOLPHIN.getImage());

        dialog.setLayout(new GridLayout(4, 3));
        GregorianCalendar gc = new GregorianCalendar(tableModel.getYear(), tableModel.getMonth(), 1);
        gc.add(Calendar.MONTH, -6);

        for (int i = 0; i < 12; i++) {
            CalendarTable tbl = new CalendarTable(gc);
            CalendarTableModel mdl = (CalendarTableModel) tbl.getModel();
            mdl.setMarkDates(tableModel.getMarkDates());

            tbl.addCalendarListener(date -> {
                // リスナのブリッジ
                CalendarListener l = table.getCalendarListener();
                if (l != null) {
                    l.dateSelected(date);
                }
            });

            // 内容をつかんでドラッグできるようにする
            MouseAdapter ma = new MouseAdapter() {
                private final Point startPt = new Point();
                private final Point windowPt = new Point();

                @Override
                public void mousePressed(MouseEvent e) {
                    startPt.setLocation(e.getLocationOnScreen());
                    windowPt.setLocation(dialog.getLocation());
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    Point pt = e.getLocationOnScreen();
                    dialog.setLocation(windowPt.x + pt.x - startPt.x, windowPt.y + pt.y - startPt.y);
                }
            };
            tbl.getTitledPanel().addMouseListener(ma);
            tbl.getTitledPanel().addMouseMotionListener(ma);

            dialog.add(tbl.getTitledPanel());
            gc.add(Calendar.MONTH, 1);
        }
        Point p = getLocationOnScreen();
        dialog.pack();
        dialog.setLocation(p);
        dialog.setVisible(true);
    }

    /**
     * バックグラウンド色を設定する.
     *
     * @param bg background
     */
    public void setCalendarBackground(Color bg) {
        table.setBackground(bg);
        controlPanel.setBackground(bg);
    }

    /**
     * 表示年月変更のリスナ.
     *
     * @param l listener
     */
    public void addCalendarListener(CalendarListener l) {
        listener = l;
    }

    /**
     * CalendarTable を返す.
     *
     * @return CalendarTable
     */
    public CalendarTable getTable() {
        return table;
    }

    /**
     * CalendarTableModel を返す.
     *
     * @return CalendarTableModel
     */
    public CalendarTableModel getModel() {
        return tableModel;
    }

    public static void main(String[] arg) {
        open.dolphin.client.ClientContext.setClientContextStub(new open.dolphin.client.ClientContextStub());

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CalendarPanel panel = new CalendarPanel();

        panel.getTable().addCalendarListener(date
            -> System.out.println(date.getYear() + " / " + date.getMonth() + " / " + date.getDay()));

        f.add(panel);
        f.pack();
        f.setVisible(true);
    }


}

package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.ui.MyBorderFactory;

/**
 * CalendarCardPanel
 * @author Minagawa,Kazushi, modified by pns
 */
public class CalendarCardPanel extends JPanel  {

    public static final String PICKED_DATE = "pickedDate";

    private JPanel cardPanel = new JPanel();
    private CardLayout cardLayout = new CardLayout();
    private ImageIcon backIcon = GUIConst.ICON_ARROW_LEFT_16;
    private ImageIcon stopIcon = GUIConst.ICON_ARROW_DOWN_16;
    private ImageIcon forwardIcon = GUIConst.ICON_ARROW_RIGHT_16;
    private ImageIcon upIcon = GUIConst.ICON_ARROW_UP_16;
    private JButton backBtn = new JButton(backIcon);
    private JButton stopBtn = new JButton(stopIcon);
    private JButton forwardBtn = new JButton(forwardIcon);
    private JButton upBtn = new JButton(upIcon);
    private int current;
    private int minMonth = -24;
    private int maxMonth = 24;
    private HashMap<String, LiteCalendarPanel> calendars;
    private HashMap colorTable;
    private List markList;
    private PropertyChangeSupport boundSupport;
    private PropertyChangeListener calendarListener;
    private JPanel cmdEastPanel;
    private JPanel cmdWestPanel;

    public CalendarCardPanel(HashMap colorTable) {
        this(colorTable, 0);
    }

    public CalendarCardPanel(HashMap colorTable, int month) {
        current = month;
        this.colorTable = colorTable;

        calendarListener = new CalendarListener(this);
        calendars = new HashMap<String, LiteCalendarPanel>(12);
        boundSupport = new PropertyChangeSupport(this);

        LiteCalendarPanel lc = new LiteCalendarPanel(current, true);
        lc.addPropertyChangeListener(LiteCalendarPanel.SELECTED_DATE_PROP, calendarListener);
        lc.setEventColorTable(colorTable);
        SimpleDate today = new SimpleDate(new GregorianCalendar());
        lc.setToday(today);
        String name = String.valueOf(current);
        calendars.put(name, lc);
        cardPanel.setLayout(cardLayout);
        cardPanel.add(lc, name);

        JButton[] btns = { backBtn, stopBtn, forwardBtn, upBtn };
        for (JButton b: btns) {
            b.setPreferredSize(new Dimension(15,15));
            b.setBorderPainted(false);
            b.setContentAreaFilled(false);
        }

        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current -= 1;
                controlNavigation();
                showCalendar();
            }
        });

        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current = 0;
                controlNavigation();
                showCalendar();
            }
        });

        forwardBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current+=1;
                controlNavigation();
                showCalendar();
            }
        });

        upBtn.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                JPopupMenu popup = new JPopupMenu();
                for (int i=2; i>=0; i--) {
                    LiteCalendarPanel lc = getLiteCalendarPanel(current - i);
                    lc.setOutOfMonthFont(null);
                    popup.add(lc);
                }
                int y = e.getYOnScreen() - 300; // 300ドット上に出す
                y = (y>0)? 300 : (300 + y); // 上にはみ出ないように
                y -= 24; // メニューバーの分調整
                popup.show(e.getComponent(), e.getX(), e.getY() - y);
            }
        });

        cmdEastPanel = createEastCommnadPanel();
        cmdEastPanel.setBackground(ClientContext.getColor("color.calendar.title.back"));
        cmdEastPanel.setOpaque(true);

        cmdWestPanel = createWestCommandPanel();
        cmdWestPanel.setPreferredSize(cmdEastPanel.getPreferredSize());
        cmdWestPanel.setBackground(ClientContext.getColor("color.calendar.title.back"));
        cmdWestPanel.setOpaque(true);
        composeTitlePanel(lc);

        this.setLayout(new BorderLayout(0,0));
        this.add(cardPanel, BorderLayout.CENTER);
        controlNavigation();
    }

    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.addPropertyChangeListener(prop, l);
    }

    @Override
    public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.removePropertyChangeListener(prop, l);
    }

    public void notifyPickedDate(SimpleDate picked) {
        boundSupport.firePropertyChange(PICKED_DATE, null, picked);
    }

    public int[] getRange() {
        int[] ret = { minMonth, maxMonth };
        return ret;
    }

    public void setCalendarRange(int[] range) {
        minMonth = range[0];
        maxMonth = range[1];
        controlNavigation();
    }

    public void setMarkList(List newMark) {

        if (markList != newMark) {
            markList = newMark;
        }
        LiteCalendarPanel lc = calendars.get(String.valueOf(current));
        lc.getTableModel().setMarkDates(markList);
    }

    private void controlNavigation() {
        if (current == minMonth) {
            backBtn.setEnabled(false);
            forwardBtn.setEnabled(true);
        } else if (current == maxMonth) {
            backBtn.setEnabled(true);
            forwardBtn.setEnabled(false);
        } else {
            backBtn.setEnabled(true);
            forwardBtn.setEnabled(true);
        }
    }

    private void showCalendar() {

        String key = String.valueOf(current);
        LiteCalendarPanel lc = calendars.get(key);
        if (lc == null) {
            lc = getLiteCalendarPanel(current);
            calendars.put(key, lc);
            cardPanel.add(lc, key);
        } else {
            lc.getTableModel().setMarkDates(markList);
        }
        composeTitlePanel(lc);

        // popup メニューに表示されているときは，カレンダーの大きさに合わせて大きさを変える
        int n = lc.getTableModel().getRowCount();
        cardPanel.setPreferredSize(new Dimension(193,18*n+45));
        Container con = this.getParent();
        if (con instanceof JPopupMenu) ((JPopupMenu) con).pack();
        cardLayout.show(cardPanel, key);
    }

    public LiteCalendarPanel getLiteCalendarPanel(int n) {
        LiteCalendarPanel lc = new LiteCalendarPanel(n, true);
        lc.addPropertyChangeListener(LiteCalendarPanel.SELECTED_DATE_PROP, calendarListener);
        lc.setEventColorTable(colorTable);
        lc.getTableModel().setMarkDates(markList);
        return lc;
    }

    private JPanel createEastCommnadPanel() {
        JPanel cmd = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,0,0));
        cmd.add(backBtn);
        cmd.add(stopBtn);
        cmd.add(forwardBtn);
        cmd.setBorder(MyBorderFactory.createTitleBorder(new Insets(0,0,0,0)));
        backBtn.setOpaque(false);
        stopBtn.setOpaque(false);
        forwardBtn.setOpaque(false);
        return cmd;
    }

    private JPanel createWestCommandPanel() {
        JPanel cmd = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT,0,0));
        cmd.add(upBtn);
        cmd.setBorder(MyBorderFactory.createTitleBorder(new Insets(0,0,0,0)));
        upBtn.setOpaque(false);
        return cmd;
    }

    private void composeTitlePanel(LiteCalendarPanel lc) {
        JPanel title = lc.getTitlePanel();
        title.add(cmdEastPanel, BorderLayout.EAST);
        title.add(cmdWestPanel, BorderLayout.WEST);
    }

    class CalendarListener implements PropertyChangeListener {

        private CalendarCardPanel owner;

        public CalendarListener(CalendarCardPanel owner) {
            this.owner = owner;
        }

        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName().equals(LiteCalendarPanel.SELECTED_DATE_PROP)) {
                SimpleDate sd = (SimpleDate)e.getNewValue();
                owner.notifyPickedDate(sd);
            }
        }
    }
}

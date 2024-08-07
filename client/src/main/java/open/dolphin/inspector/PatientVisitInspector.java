package open.dolphin.inspector;

import open.dolphin.calendar.CalendarEvent;
import open.dolphin.calendar.CalendarPanel;
import open.dolphin.calendar.CalendarTableModel;
import open.dolphin.client.ChartImpl;
import open.dolphin.infomodel.SimpleDate;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * PatientVisitInspector.
 *
 * @author kazm
 * @author pns
 */
public class PatientVisitInspector implements IInspector {
    public static final InspectorCategory CATEGORY = InspectorCategory.カレンダー;
    private final ChartImpl context;
    private CalendarPanel calendarPanel;
    private String pvtCode; // PVT
    private String titleText;
    private InspectorBorder border;

    /**
     * PatientVisitInspector を生成する.
     *
     * @param parent parent inspector
     */
    public PatientVisitInspector(PatientInspector parent) {
        context = parent.getContext();
        initComponent();
    }

    /**
     * レイアウトパネルを返す.
     *
     * @return レイアウトパネル
     */
    @Override
    public JPanel getPanel() {
        return calendarPanel;
    }

    @Override
    public String getName() {
        return CATEGORY.name();
    }

    @Override
    public String getTitle() {
        return CATEGORY.title();
    }

    /**
     * GUIコンポーネントを初期化する.
     */
    private void initComponent() {
        pvtCode = CalendarEvent.PVT.name();
        calendarPanel = new CalendarPanel();
        calendarPanel.setName(CATEGORY.name());
        calendarPanel.setCalendarBackground(Color.WHITE);

        calendarPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        calendarPanel.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        calendarPanel.setMaximumSize(new Dimension(1024, DEFAULT_HEIGHT));

        calendarPanel.addCalendarListener(this::calendarUpdated);
        // タイトル
        CalendarTableModel tableModel = (CalendarTableModel) calendarPanel.getTable().getModel();
        int year = tableModel.getYear();
        int month = tableModel.getMonth();
        titleText = String.format("%s %d年%d月", CATEGORY.title(), year, month + 1);

        // 月の第1週だった場合、2週戻す、第2週だった場合、1週戻す
        Calendar today = Calendar.getInstance();
        int wom = today.get(Calendar.WEEK_OF_MONTH);
        for (int i=wom; i<3; i++) {
            tableModel.previousWeek();
        }
    }

    @Override
    public void update() {
        List<SimpleDate> markDates = new ArrayList<>();

        // 来院歴を取り出す
        List<String> latestVisit = context.getKarte().getPvtDateEntry();

        // 来院歴
        if (latestVisit != null) {
            List<SimpleDate> visits = new ArrayList<>();
            latestVisit.forEach(pvtDate -> {
                SimpleDate sd = SimpleDate.mmlDateToSimpleDate(pvtDate);
                sd.setEventCode(pvtCode);
                visits.add(sd);
            });
            markDates.addAll(visits);
        }

        // 誕生日
        String mmlBirthday = context.getPatient().getBirthday();
        calendarPanel.getModel().setBirthday(mmlBirthday);

        // CardCalendarに通知する
        calendarPanel.getModel().setMarkDates(markDates);
    }

    /**
     * タイトルに年月をつけたボーダーを返す.
     *
     * @return border
     */
    @Override
    public Border getBorder() {
        border = new InspectorBorder(titleText);
        return border;
    }

    /**
     * CalendarPanel から月変更の通知を受ける.
     *
     * @param date date
     */
    public void calendarUpdated(SimpleDate date) {
        if (border != null) {
            titleText = String.format("%s %d年%d月", CATEGORY.title(), date.getYear(), date.getMonth() + 1);
            border.setTitle(titleText);
            context.getFrame().repaint();
        }
    }
}

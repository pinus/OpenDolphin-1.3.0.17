package open.dolphin.inspector;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import open.dolphin.client.CalendarEvent;
import open.dolphin.client.CalendarPanel;
import open.dolphin.client.ChartImpl;
import open.dolphin.infomodel.SimpleDate;

/**
 *
 * @author kazm
 */
public class PatientVisitInspector implements IInspector {
    public static final InspectorCategory CATEGORY = InspectorCategory.カレンダー;

    private CalendarPanel calendarPanel;
    private String pvtCode; // PVT
    private final ChartImpl context;

    /**
     * PatientVisitInspector を生成する.
     * @param parent
     */
    public PatientVisitInspector(PatientInspector parent) {
        context = parent.getContext();
        initComponent();
    }

    /**
     * レイアウトパネルを返す.
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

        calendarPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        calendarPanel.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        calendarPanel.setMaximumSize(new Dimension(1024, DEFAULT_HEIGHT));

        calendarPanel.addCalendarListener(this::calendarUpdated);
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
        SimpleDate birthday = SimpleDate.mmlDateToSimpleDate(mmlBirthday);
        birthday.setEventCode(CalendarEvent.BIRTHDAY.name());
        markDates.add(birthday);

        // CardCalendarに通知する
        calendarPanel.getModel().setMarkDates(markDates);
    }

    public void calendarUpdated(SimpleDate date) {
        System.out.println("updated calendar " + date.getYear() + " / " + date.getMonth());
    }
}

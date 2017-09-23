package open.dolphin.inspector;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.Border;
import open.dolphin.calendar.CalendarEvent;
import open.dolphin.calendar.CalendarPanel;
import open.dolphin.calendar.CalendarTableModel;
import open.dolphin.client.ChartImpl;
import open.dolphin.infomodel.SimpleDate;

/**
 * PatientVisitInspector.
 * @author kazm
 * @author pns
 */
public class PatientVisitInspector implements IInspector {
    public static final InspectorCategory CATEGORY = InspectorCategory.カレンダー;

    private CalendarPanel calendarPanel;
    private String pvtCode; // PVT
    private final ChartImpl context;
    private String titleText;
    private InspectorBorder border;

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
        calendarPanel.setCalendarBackground(Color.WHITE);

        calendarPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        calendarPanel.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        calendarPanel.setMaximumSize(new Dimension(1024, DEFAULT_HEIGHT));

        calendarPanel.addCalendarListener(this::calendarUpdated);
        // タイトル
        CalendarTableModel tableModel = (CalendarTableModel)calendarPanel.getTable().getModel();
        int year = tableModel.getYear();
        int month = tableModel.getMonth();
        titleText = String.format("%s %d年%d月", CATEGORY.title(), year, month+1);
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
     * @return
     */
    @Override
    public Border getBorder() {
        border = new InspectorBorder(titleText);
        return border;
    }

    /**
     * CalendarPanel から月変更の通知を受ける.
     * @param date
     */
    public void calendarUpdated(SimpleDate date) {
        if (border != null) {
            titleText = String.format("%s %d年%d月", CATEGORY.title(), date.getYear(), date.getMonth()+1);
            border.setTitle(titleText);
            context.getFrame().repaint();
        }
    }
}

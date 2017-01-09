package open.dolphin.inspector;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import open.dolphin.client.CalendarCardPanel;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.ClientContext;
import open.dolphin.infomodel.SimpleDate;

/**
 *
 * @author kazm
 */
public class PatientVisitInspector implements IInspector {

    private CalendarCardPanel calendarCardPanel;
    private String pvtCode; // PVT
    private final ChartImpl context;

    /**
     * PatientVisitInspector を生成する.
     * @param context
     */
    public PatientVisitInspector(ChartImpl context) {
        this.context = context;
        initComponent();
    }

    /**
     * レイアウトパネルを返す.
     * @return レイアウトパネル
     */
    @Override
    public JPanel getPanel() {
        return calendarCardPanel;
    }

    /**
     * GUIコンポーネントを初期化する.
     */
    private void initComponent() {
        pvtCode = ClientContext.getString("eventCode.pvt"); // "PVT"
        calendarCardPanel = new CalendarCardPanel(ClientContext.getEventColorTable());
        calendarCardPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        calendarCardPanel.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        calendarCardPanel.setMaximumSize(new Dimension(1024, DEFAULT_HEIGHT));
    }

    @Override
    public void update() {

        // 来院歴を取り出す
        List<String> latestVisit = context.getKarte().getPvtDateEntry();

        // 来院歴
        if (latestVisit != null && !latestVisit.isEmpty()) {
            List<SimpleDate> simpleDates = new ArrayList<>(latestVisit.size());
            latestVisit.forEach(pvtDate -> {
                SimpleDate sd = SimpleDate.mmlDateToSimpleDate(pvtDate);
                sd.setEventCode(pvtCode);
                simpleDates.add(sd);
            });
            // CardCalendarに通知する
            calendarCardPanel.setMarkList(simpleDates);
        }
    }
}

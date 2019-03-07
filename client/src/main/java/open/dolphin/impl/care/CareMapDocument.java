package open.dolphin.impl.care;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import open.dolphin.calendar.CalendarEvent;
import open.dolphin.client.*;
import open.dolphin.delegater.AppointmentDelegater;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.dto.ImageSearchSpec;
import open.dolphin.dto.ModuleSearchSpec;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.util.ModelUtils;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.project.Project;
import open.dolphin.ui.PNSBorderFactory;
import open.dolphin.helper.PNSPair;

/**
 * CareMapDocument.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public final class CareMapDocument extends AbstractChartDocument {

    private static final int IMAGE_WIDTH = 128;
    private static final int IMAGE_HEIGHT = 128;
    private static final String TITLE = "治療履歴";
    private static final String IMAGE_EVENT = "image";

    private static final List<PNSPair<String,String>> ORDERS = Arrays.asList(
            new PNSPair<>("処方", IInfoModel.ENTITY_MED_ORDER),
            new PNSPair<>("処置", IInfoModel.ENTITY_TREATMENT),
            new PNSPair<>("指導", IInfoModel.ENTITY_INSTRACTION_CHARGE_ORDER),
            new PNSPair<>("ラボテスト", IInfoModel.ENTITY_LABO_TEST),
            new PNSPair<>("生体検査", IInfoModel.ENTITY_PHYSIOLOGY_ORDER),
            new PNSPair<>("放射線", IInfoModel.ENTITY_RADIOLOGY_ORDER),
            new PNSPair<>("画像", IMAGE_EVENT)
    );

    private static final List<PNSPair<String,Color>> APPOINT_NAME_COLORS = Arrays.asList(
            new PNSPair<>("再診", CalendarEvent.EXAM_APPO.color()),
            new PNSPair<>("検体検査", CalendarEvent.TEST_APPO.color()),
            new PNSPair<>("画像検査", CalendarEvent.IMAGE_APPO.color()),
            new PNSPair<>("その他", CalendarEvent.MISC_APPO.color())
    );

    private final HashMap<Integer,SimpleCalendarPanel> calendarMap = new HashMap<>();
    private int origin;
    private Period selectedPeriod;
    private String selectedEvent;

    private JComboBox<PNSPair<String,String>> orderCombo;
    private OrderHistoryPanel orderHistoryPanel;
    private AppointTablePanel appointTablePanel;
    private ImageHistoryPanel imagePanel;
    private JPanel historyContainer;
    private JButton updateAppoBtn; // 予約の更新はこのボタンで行う

    /**
     * Creates new CareMapDocument.
     */
    public CareMapDocument() {
        setTitle(TITLE);
    }

    /**
     * 初期化する.
     */
    private void initialize() {

        Chart chartCtx = getContext();

        JPanel threeMonthPanel = new JPanel();
        threeMonthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        calendarMap.put(-1, new SimpleCalendarPanel(origin-1));
        calendarMap.put(0, new SimpleCalendarPanel(origin));
        calendarMap.put(1, new SimpleCalendarPanel(origin+1));

        calendarMap.values().forEach(calendar -> {
            calendar.setChartContext(chartCtx);
            calendar.setParent(this);
            calendar.addCalendarListener(date -> updatePanel(calendar, date));
            threeMonthPanel.add(calendar);
        });

        // カレンダーの範囲を１ケ月以に戻すボタン
        JButton prevBtn = new JButton(GUIConst.ICON_ARROW1_LEFT_16);
        prevBtn.addActionListener(e -> {
            origin --;
            calendarMap.values().forEach(calendar -> calendar.getTableModel().previousMonth());

            // オーダ履歴の抽出期間全体が変化したので通知する
            Period p = new Period(this);
            p.setStartDate(calendarMap.get(-1).getFirstDate());
            p.setEndDate(calendarMap.get(1).getLastDate());
            setSelectedPeriod(p);
        });

        // カレンダーの範囲を１ケ月送るボタン
        JButton nextBtn = new JButton(GUIConst.ICON_ARROW1_RIGHT_16);
        nextBtn.addActionListener(e -> {
            origin ++;
            calendarMap.values().forEach(calendar -> calendar.getTableModel().nextMonth());

            // オーダ履歴の抽出期間全体が変化したので通知する
            Period p = new Period(this);
            p.setStartDate(calendarMap.get(-1).getFirstDate());
            p.setEndDate(calendarMap.get(1).getLastDate());
            setSelectedPeriod(p);
        });

        // 更新の保存ボタン -> dirty 状況によって enable/disable される
        updateAppoBtn = new JButton(GUIConst.ICON_SAVE_16);
        updateAppoBtn.setEnabled(false);
        updateAppoBtn.addActionListener(e -> save());

        // 予約表テーブルを生成する
        appointTablePanel = new AppointTablePanel(updateAppoBtn);
        appointTablePanel.setParent(this);
        appointTablePanel.setBorder(PNSBorderFactory.createTitledBorder("予約表"));
        appointTablePanel.setPreferredSize(new Dimension(500, 260));

        // オーダ履歴表示用テーブルを生成する
        orderHistoryPanel = new OrderHistoryPanel();
        orderHistoryPanel.setPid(chartCtx.getPatient().getPatientId());

        // 画像履歴用のパネルを生成する
        imagePanel = new ImageHistoryPanel();
        imagePanel.setMyParent(this);
        imagePanel.setPid(chartCtx.getPatient().getPatientId());

        // 検査履歴と画像歴の切り替えコンテナ
        historyContainer = new JPanel(new BorderLayout());
        historyContainer.add(orderHistoryPanel, BorderLayout.CENTER);
        historyContainer.setBorder(PNSBorderFactory.createTitledBorder("履 歴"));

        // オーダ選択用のコンボボックス
        orderCombo = new JComboBox<>();
        ORDERS.forEach(item -> orderCombo.addItem(item));

        Dimension dim = new Dimension(100, 26);
        orderCombo.setPreferredSize(dim);
        orderCombo.setMaximumSize(dim);
        ComboBoxRenderer r = new ComboBoxRenderer();
        orderCombo.setRenderer(r);
        orderCombo.addItemListener(e -> {
            // オーダ選択が変更されたら
            if (e.getStateChange() == ItemEvent.SELECTED) {

                String entity = getEntityName();

                if (entity.equals(IMAGE_EVENT)) {
                    // 画像履歴が選択された場合 Image Panel に変更する
                    historyContainer.removeAll();
                    historyContainer.add(imagePanel, BorderLayout.CENTER);
                    historyContainer.revalidate();
                    getUI().repaint();

                } else if (selectedEvent.equals(IMAGE_EVENT)) {
                    // 現在のイベントが Image の場合は オーダ履歴用と入れ替える
                    historyContainer.removeAll();
                    historyContainer.add(orderHistoryPanel, BorderLayout.CENTER);
                    historyContainer.revalidate();
                    getUI().repaint();
                }

                // 選択されたオーダをイベント属性に設定する
                setSelectedEvent(entity);
            }
        });

        // 凡例パネル
        JPanel han = new JPanel();
        han.setLayout(new BoxLayout(han, BoxLayout.X_AXIS));

        han.add(new JLabel("予約 ( "));
        APPOINT_NAME_COLORS.forEach(pair -> {
            han.add(new AppointLabel(pair.getName(), new ColorFillIcon(pair.getValue(), 10, 10, 1), SwingConstants.CENTER));
            han.add(Box.createHorizontalStrut(7));
        });
        han.remove(han.getComponentCount()-1); // 最後の Strut を削除
        han.add(new JLabel(" )"));

        han.add(Box.createHorizontalStrut(7));
        Color birthdayColor = CalendarEvent.BIRTHDAY.color();
        han.add(new JLabel("誕生日", new ColorFillIcon(birthdayColor, 10, 10, 1), SwingConstants.CENTER));
        han.add(Box.createHorizontalStrut(11));

        // 表示するオーダを選択する Combo, カレンダーの送る，戻るボタンを配置するパネル
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.X_AXIS));
        commandPanel.add(Box.createHorizontalGlue());
        commandPanel.add(prevBtn);
        commandPanel.add(Box.createHorizontalStrut(5));
        commandPanel.add(orderCombo);
        commandPanel.add(Box.createHorizontalStrut(5));
        commandPanel.add(nextBtn);
        commandPanel.add(Box.createHorizontalGlue());
        commandPanel.add(han);

        // 全体のパネル
        JPanel myPanel = getUI();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(threeMonthPanel);
        myPanel.add(Box.createVerticalStrut(7));

        myPanel.add(commandPanel);
        myPanel.add(Box.createVerticalStrut(7));

        myPanel.add(historyContainer);
        myPanel.add(Box.createVerticalStrut(7));

        myPanel.add(appointTablePanel);
        myPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
    }

    /**
     * SimpleCalendarPanel の日付選択で呼ばれる.
     * @param target
     * @param date
     */
    private void updatePanel(SimpleCalendarPanel target, SimpleDate date) {
        String code = date.getEventCode();
        String mmlDate = SimpleDate.simpleDateToMmldate(date);
        AppointmentModel appoint = target.getAppointmentModel(mmlDate);
        boolean cancelledAppoint = appoint != null && appoint.getName() == null;

        if (cancelledAppoint || CalendarEvent.isAppoint(code)) {
            appointTablePanel.updateAppoint(appoint);

        } else if (CalendarEvent.isModule(code)) {
            if (selectedEvent.equals(IMAGE_EVENT)) {
                imagePanel.findDate(date);

            } else {
                orderHistoryPanel.findDate(date);
            }
        }
    }

    @Override
    public void start() {
        initialize();
        enter();
        // 最初に選択されているオーダの履歴を表示する
        setSelectedEvent(getEntityName());
        Period period = new Period(this);
        period.setStartDate(calendarMap.get(-1).getFirstDate());
        period.setEndDate(calendarMap.get(1).getLastDate());
        setSelectedPeriod(period);
    }

    @Override
    public void stop() {
    }

    /**
     * 予約のカラーを返す.
     *
     * @param appoint 予約名
     * @return カラー
     */
    public Color getAppointColor(String appoint) {
        if (appoint != null) {
            for (PNSPair<String,Color> pair : APPOINT_NAME_COLORS) {
                if (appoint.equals(pair.getName())) { return pair.getValue(); }
            }
        }
        return Color.WHITE;
    }

    /**
     * 表示している期間内にあるモジュールの日をマークする.
     * @param newModules  表示している期間内にあるモジュールのリスト
     */
    public void setAllModules(List<List<ModuleModel>> newModules) {

        if (newModules == null || newModules.isEmpty()) { return; }

        calendarMap.get(-1).setModuleList(selectedEvent, newModules.get(0));
        calendarMap.get(0).setModuleList(selectedEvent, newModules.get(1));
        calendarMap.get(1).setModuleList(selectedEvent, newModules.get(2));

        orderHistoryPanel.setModuleList(newModules);
    }

    /**
     * 表示している期間内にある予約日をマークする.
     * @param allAppo 表示している期間内にある予約日のリスト
     */
    public void setAllAppointments(List<List<AppointmentModel>> allAppo) {

        if (allAppo == null || allAppo.isEmpty()) { return; }

        calendarMap.get(-1).setAppointmentList(allAppo.get(0));
        calendarMap.get(0).setAppointmentList(allAppo.get(1));
        calendarMap.get(1).setAppointmentList(allAppo.get(2));

        List<AppointmentModel> list = new ArrayList<>();
        allAppo.forEach(list::addAll);

        appointTablePanel.setAppointmentList(list);
    }

    /**
     * 表示している期間内にある画像をマークする.
     * @param images
     */
    public void setAllImages(List<List<ImageEntry>> images) {

        if (images == null || images.isEmpty()) { return; }

        calendarMap.get(-1).setImageList(selectedEvent, images.get(0));
        calendarMap.get(0).setImageList(selectedEvent, images.get(1));
        calendarMap.get(1).setImageList(selectedEvent, images.get(2));

        imagePanel.setImageList(images);
    }

    /**
     * 抽出期間が変更された場合，現在選択されているイベントに応じ， モジュールまたは画像履歴を取得する.
     * @param p
     */
    public void setSelectedPeriod(Period p) {

        selectedPeriod = p;

        if (getSelectedEvent().equals(IMAGE_EVENT)) {
            getImageList();

        } else {
            getModuleList(true); // with appoint
        }
    }

    public String getSelectedEvent() {
        return selectedEvent;
    }

    /**
     * 表示するオーダが変更された場合，選択されたイベントに応じ， モジュールまたは画像履歴を取得する.
     * @param code
     */
    public void setSelectedEvent(String code) {
        // 登録前に以前の event はクリアする
        calendarMap.values().forEach(calendar -> {
            calendar.getTableModel().clearMarkDates(selectedEvent);
            calendar.getTableModel().clearMarkDates(code);
        });

        selectedEvent = code;

        if (getSelectedEvent().equals(IMAGE_EVENT)) {
            getImageList();

        } else {
            getModuleList(false); // without appoint
        }
    }

    /**
     * 設定されている curEvent と抽出期間からモジュールのリストを取得する.
     */
    private void getModuleList(final boolean appo) {

        if (selectedEvent == null || selectedPeriod == null) { return; }

        final ModuleSearchSpec spec = new ModuleSearchSpec();
        spec.setCode(ModuleSearchSpec.ENTITY_SEARCH);
        spec.setKarteId(getContext().getKarte().getId());
        spec.setEntity(selectedEvent);
        spec.setStatus("F");

        // カレンダ別に検索する
        Date[] fromDate = new Date[3];
        for (int i=0; i<3; i++) {
            fromDate[i] = ModelUtils.getDateTimeAsObject(calendarMap.get(i-1).getFirstDate() + "T00:00:00");
        }
        spec.setFromDate(fromDate);

        Date[] toDate = new Date[3];
        for (int i=0; i<3; i++) {
            toDate[i] = ModelUtils.getDateTimeAsObject(calendarMap.get(i-1).getLastDate() + "T23:59:59");
        }
        spec.setToDate(toDate);

        final DocumentDelegater ddl = new DocumentDelegater();

        DBTask<PNSPair<List<List<ModuleModel>>, List<List<AppointmentModel>>>> task =
                new DBTask<PNSPair<List<List<ModuleModel>>, List<List<AppointmentModel>>>>(getContext()) {

            @Override
            public PNSPair<List<List<ModuleModel>>, List<List<AppointmentModel>>> doInBackground() {
                List<List<ModuleModel>> modules = ddl.getModuleList(spec);
                List<List<AppointmentModel>> appoints = appo? ddl.getAppoinmentList(spec) : null;
                return new PNSPair<>(modules, appoints);
            }

            @Override
            public void succeeded(PNSPair<List<List<ModuleModel>>, List<List<AppointmentModel>>> result) {
                setAllModules(result.getName());
                if (appo) {
                    setAllAppointments(result.getValue());
                }
            }
        };

        task.execute();
    }

    /**
     * 設定されている抽出期間から画像履歴を取得する.
     */
    private void getImageList() {

        if (selectedPeriod == null) {
            return;
        }

        final ImageSearchSpec spec = new ImageSearchSpec();
        spec.setCode(ImageSearchSpec.PATIENT_SEARCH);
        spec.setKarteId(getContext().getKarte().getId());
        spec.setStatus("F");

        // カレンダ別に検索する
        Date[] fromDate = new Date[3];
        for (int i=0; i<3; i++) {
            fromDate[i] = ModelUtils.getDateTimeAsObject(calendarMap.get(i-1).getFirstDate() + "T00:00:00");
        }
        spec.setFromDate(fromDate);

        Date[] toDate = new Date[3];
        for (int i=0; i<3; i++) {
            toDate[i] = ModelUtils.getDateTimeAsObject(calendarMap.get(i-1).getLastDate() + "T23:59:59");
        }
        spec.setToDate(toDate);
        spec.setIconSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

        final DocumentDelegater ddl = new DocumentDelegater();

        DBTask<List<List<ImageEntry>>> task = new DBTask<List<List<ImageEntry>>>(getContext()) {

            @Override
            public List<List<ImageEntry>> doInBackground() {
                return ddl.getImageList(spec);
            }

            @Override
            public void succeeded(List<List<ImageEntry>> result) {
                setAllImages(result);
            }
        };

        task.execute();
    }

    @Override
    public void setDirty(boolean dirty) {
        if (isDirty() != dirty) {
            super.setDirty(dirty);
            updateAppoBtn.setEnabled(isDirty());
        }
    }

    /**
     * 新規及び変更された予約を保存する.
     */
    @Override
    public void save() {

        final List<AppointmentModel> results = new ArrayList<>();

        // カレンダー単位に抽出する
        for (SimpleCalendarPanel c : calendarMap.values()) {

            if (c.getRelativeMonth() >= 0) {

                List<AppointmentModel> list = c.getUpdatedAppoints();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    AppointmentModel appo = list.get(i);

                    // 新規予約のみEJB3.0の関係を設定する
                    if (appo.getKarte() == null) {
                        appo.setKarte(getContext().getKarte());
                    }
                    appo.setCreator(Project.getUserModel());

                    // 確定日，記録日，開始日
                    // 現状の実装はここまで
                    Date confirmed = new Date();
                    appo.setConfirmed(confirmed);
                    appo.setRecorded(confirmed);
                    if (appo.getStarted() == null) {
                        appo.setStarted(confirmed);
                    }
                    // 常にFINAL
                    appo.setStatus(IInfoModel.STATUS_FINAL);

                    results.add(list.get(i));
                }
            }
        }

        if (results.isEmpty()) {
            return;
        }

        final AppointmentDelegater adl = new AppointmentDelegater();

        DBTask<Void> task = new DBTask<Void>(getContext()) {

            @Override
            protected Void doInBackground() {
                adl.putAppointments(results);
                return null;
            }

            @Override
            public void succeeded(Void result) {
                setDirty(false);
            }
        };

        task.execute();
    }

    /**
     * ComboBox で選択された EntityName を返す.
     * @return
     */
    private String getEntityName() {
        int index = orderCombo.getSelectedIndex();
        return ORDERS.get(index).getValue();
    }

    /**
     * ComboBoxRenderer.
     */
    private class ComboBoxRenderer extends JLabel implements ListCellRenderer<PNSPair<String,String>> {
        private static final long serialVersionUID = 4661822065789099499L;

        public ComboBoxRenderer() {
            init();
        }

        private void init() {
            setOpaque(true);
            // setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the image and text corresponding to the selected
         * value and returns the label, set up to display the text and image.
         */
        @Override
        public Component getListCellRendererComponent(JList<? extends PNSPair<String, String>> list,
                PNSPair<String, String> value, int index, boolean isSelected, boolean cellHasFocus) {

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            // Set the icon and text. If icon was null, say so.
            Icon icon = getOrderIcon(value.getValue());

            if (icon != null) {
                setIcon(icon);
                setText(value.getName());
            } else {
                setText(value.getName());
            }

            return this;
        }

        private Icon getOrderIcon(String name) {
            Icon ret = null;
            return ret;
        }
    }
}

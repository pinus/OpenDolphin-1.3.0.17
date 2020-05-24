package open.dolphin.setting;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.Event;

import open.dolphin.client.GUIConst;
import open.dolphin.helper.GridBagBuilder;
import open.dolphin.helper.Task;
import open.dolphin.ui.CompletableJTextField;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.prefs.Preferences;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;
import java.awt.*;

/**
 * Calendar Setting Panel.
 *
 * @author pns
 */
public class CalendarSettingPanel extends AbstractSettingPanel {
    private static final String ID = "calendarSetting";
    private static final String TITLE = "カレンダー";
    private static final ImageIcon ICON = GUIConst.ICON_CALENDAR_32;
    private static final int TEXTFIELD_WIDTH = 40;

    // Google Calendar
    private static final String APPLICATION_NAME = "open.dolphin";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    private static final String TOKENS_DIRECTORY_PATH = TMP_DIR + "google.calendar.tokens";

    // Keys for preferences
    private static String CALENDAR_ID = "calendarId";
    private static String HOLIDAY_CALENDAR_ID = "holidayCalendarId";
    private static String CREDENTIAL = "calendarCredential";
    private static String CALENDAR_DATA = "calendarData";

    // GUI
    private CompletableJTextField holidayCalendarIdField;
    private CompletableJTextField calendarIdField;
    private JTextField credentialField;

    private String calendarData = "";
    private final Preferences prefs = Preferences.userNodeForPackage(CalendarSettingPanel.class);
    private final Logger logger = LoggerFactory.getLogger(CalendarSettingPanel.class);

    public CalendarSettingPanel() { init(); }

    private void init() {
        setId(ID);
        setTitle(TITLE);
        setIcon(ICON);
    }

    @Override
    public void start() {
        initComponents();
        bindModelToView();
    }

    private void initComponents() {
        // init panel
        GridBagBuilder gbb = new GridBagBuilder("Google Calendar 設定");
        int row = 0;

        JLabel calendarLabel = new JLabel("カレンダーID");
        calendarIdField = new CompletableJTextField(TEXTFIELD_WIDTH);
        JLabel holidayCalendarLabel = new JLabel("休日カレンダーID");
        holidayCalendarIdField = new CompletableJTextField(TEXTFIELD_WIDTH);
        JLabel credentialLabel = new JLabel("証明書データ");
        credentialField = new JTextField(TEXTFIELD_WIDTH);

        JButton updateButton = new JButton("アップデート");

        gbb.add(calendarLabel, 0, row++, 1, 1, GridBagConstraints.WEST);
        gbb.add(calendarIdField, 0, row++, 1, 1, GridBagConstraints.WEST);
        gbb.add(holidayCalendarLabel, 0, row++, 1, 1, GridBagConstraints.WEST);
        gbb.add(holidayCalendarIdField, 0, row++, 1, 1, GridBagConstraints.WEST);
        gbb.add(credentialLabel, 0, row++, 1, 1, GridBagConstraints.WEST);
        gbb.add(credentialField, 0, row++, 1, 1, GridBagConstraints.WEST);
        gbb.add(updateButton, 0, row++, 1, 1, GridBagConstraints.CENTER);

        getUI().add(gbb.getProduct());

        // connect
        updateButton.addActionListener(this::updateAction);
    }

    private boolean isValid() {
        return !StringUtils.isEmpty(holidayCalendarIdField.getText())
            && !StringUtils.isEmpty(calendarIdField.getText())
            && !StringUtils.isEmpty(credentialField.getText());
    }

    public void updateAction(ActionEvent e) {
        if (!isValid()) {
            showError("必要項目が入力されていません");
            return;
        }

        GoogleTask task = new GoogleTask(getUI(), "Google カレンダーに", "問い合わせ中...");
        task.setTimeOut(5000);
        task.execute();
    }

    private class GoogleTask extends Task<String> {

        public GoogleTask(Component parent, Object message, String note) {
            super(parent, message, note);
        }

        @Override
        protected String doInBackground() throws Exception {
            try (InputStream in = new ByteArrayInputStream(credentialField.getText().getBytes());
                 InputStreamReader reader = new InputStreamReader(in)) {

                final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);

                // Build flow and trigger user authorization request.
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline").build();

                LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
                Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

                Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

                LocalDate today = LocalDate.now();
                Date nextYear = Date.from(today.plusYears(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date prevYear = Date.from(today.minusYears(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());

                Events events = service.events().list(calendarIdField.getText())
                    .setTimeMin(new DateTime(prevYear.getTime()))
                    .setTimeMax(new DateTime(nextYear.getTime()))
                    .setOrderBy("startTime")
                    .setSingleEvents(true).execute();

                Events holidays = service.events().list(holidayCalendarIdField.getText())
                    .setTimeMin(new DateTime(prevYear.getTime()))
                    .setTimeMax(new DateTime(nextYear.getTime()))
                    .setOrderBy("startTime")
                    .setSingleEvents(true).execute();

                List<Event> items = events.getItems();
                items.addAll(holidays.getItems());

                if (items.isEmpty()) {
                    logger.info("No upcoming events found.");

                } else {
                    String[][] dataArray = new String[items.size()][2];

                    for (Event event : items) {
                        DateTime start = event.getStart().getDateTime();
                        if (start == null) {
                            start = event.getStart().getDate();
                        }
                        System.out.printf("%s (%s)\n", event.getSummary(), start);
                    }
                }
            }
            return null;
        }

        @Override
        protected void succeeded(String result) {

        }

        @Override
        protected void cancelled() {
            System.out.println("Canceled");
        }

        @Override
        protected void failed(Throwable cause) {
            System.out.println("failed " + cause);
        }

        @Override
        protected void interrupted(InterruptedException ex) {
            System.out.println("interrupted " + ex);
        }
    }

    @Override
    public void save() {
        bindViewToModel();
    }

    private void showInfo(String message) {
        showMessage(message, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        showMessage(message, JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message, int type) {
        JOptionPane.showMessageDialog(getUI(), message, "", type);
    }

    private void bindModelToView() {
        calendarIdField.setText(prefs.get(CALENDAR_ID, ""));
        holidayCalendarIdField.setText(prefs.get(HOLIDAY_CALENDAR_ID, ""));
        credentialField.setText(prefs.get(CREDENTIAL, ""));
    }

    private void bindViewToModel() {
        prefs.put(CALENDAR_ID, calendarIdField.getText());
        prefs.put(HOLIDAY_CALENDAR_ID, holidayCalendarIdField.getText());
        prefs.put(CREDENTIAL, credentialField.getText());
        prefs.put(CALENDAR_DATA, calendarData);
    }
}

package open.dolphin.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import open.dolphin.JsonConverter;
import org.apache.log4j.Logger;

/**
 * KafrteEditor の編集中 DocumentModel を定期的に一時保存する.
 * @author pns
 */
public class Autosave implements Runnable {
    private final static int INTERVAL = 1;
    private final static int INITIAL_DELAY = 1;
    private final static String SUFFIX = ".open.dolphin";
    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    private File tmpFile;
    private final KarteEditor editor;

    // 自動セーブのタイマー
    private ScheduledExecutorService executor;
    // Logger
    private final Logger logger = ClientContext.getBootLogger();

    public Autosave(KarteEditor e) {
        editor = e;
    }

    public void start() {
        String patientId = editor.getContext().getPatient().getPatientId();
        tmpFile = new File(TMP_DIR + patientId + SUFFIX);

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(this, INITIAL_DELAY, INTERVAL, TimeUnit.SECONDS);
    }

    public void save(String str) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile))) {
            bw.write(str);

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public String load() {
        StringBuilder str = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(tmpFile))) {
            String s;
            while ( (s = br.readLine()) != null) {
                str.append(s);
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        return str.toString();
    }

    public void stop () {
        tmpFile.delete();
        executor.shutdown();
    }

    public static boolean exists(String patientId) {
        File test = new File(TMP_DIR + patientId + SUFFIX);
        return test.exists();
    }

    @Override
    public void run() {
        System.out.println("============= TIMER ======");
        String json = JsonConverter.toJson(editor.getModel());
        save(json);
    }
}
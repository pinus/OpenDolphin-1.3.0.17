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
import open.dolphin.infomodel.DocumentModel;
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

    private static File getTempFile(String patientId) {
        return new File(TMP_DIR + patientId + SUFFIX);
    }

    public void start() {
        String patientId = editor.getContext().getPatient().getPatientId();
        tmpFile = getTempFile(patientId);

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(this, INITIAL_DELAY, INTERVAL, TimeUnit.SECONDS);
    }

    public void stop () {
        tmpFile.delete();
        executor.shutdown();
    }

    /**
     * Temporary file をロードする. ファイルが存在しない等，ロードできない場合は null が返る.
     * @param patientId
     * @return DocumentModel or null
     */
    public static DocumentModel load(String patientId) {
        File test = getTempFile(patientId);
        if (! test.exists()) { return null; }

        StringBuilder str = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(test))) {
            String s;
            while ( (s = br.readLine()) != null) {
                str.append(s);
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            return null;
        }
        return JsonConverter.fromJson(str.toString(), DocumentModel.class);
    }

    @Override
    public void run() {
        System.out.println("============= TIMER ======");

        String json = JsonConverter.toJson(editor.getModel());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile))) {
            bw.write(json);

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
package open.dolphin.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import open.dolphin.JsonConverter;
import open.dolphin.ui.sheet.JSheet;
import org.apache.log4j.Logger;

/**
 * 編集中の KafrteEditor を定期的に一時保存する.
 *
 * @author pns
 */
public class Autosave implements Runnable {
    private final static int INTERVAL = 1;
    private final static int INITIAL_DELAY = 1;
    private final static String SUFFIX = "open.dolphin";
    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    private File tmpFile;
    private final KarteEditor editor;
    private boolean dirty = true;

    // 自動セーブのタイマー
    private ScheduledExecutorService executor;
    // Logger
    private final Logger logger = Logger.getLogger(Autosave.class);

    public Autosave(KarteEditor e) {
        editor = e;
    }

    /**
     * pid と doc id から temporary file を作る.
     * @param patientId
     * @param docId
     * @return
     */
    private static File getTemporaryFile(String patientId, String docId) {
        String path = String.format("%s%s-%s.%s", TMP_DIR, patientId, docId, SUFFIX);
        return new File(path);
    }

    /**
     * 編集中カルテの記録を開始する.
     */
    public void start() {
        String patientId = editor.getContext().getPatient().getPatientId();
        String docId = editor.getModel().getDocInfo().getDocId();

        tmpFile = getTemporaryFile(patientId, docId);
        logger.info(tmpFile);

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(this, INITIAL_DELAY, INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 編集中カルテの記録を終了し, TemporaryFile を消去する.
     */
    public void stop () {
        tmpFile.delete();
        executor.shutdown();
    }

    /**
     * KarteEditor から Dirty 情報を受け取る.
     * @param newDirty
     */
    public void setDirty(boolean newDirty) {
        dirty = newDirty;
    }

    /**
     * 保存された Temporary file を読み込んで AutosaveModel を生成する.
     * @return
     */
    private static List<AutosaveModel> load() {
        List<AutosaveModel> ret = new ArrayList<>();

        File dir = new File(TMP_DIR);

        Arrays.asList(dir.listFiles(fn -> fn.getName().endsWith(SUFFIX))).stream().forEach(f -> {
            StringBuilder str = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String s;
                while ( (s = br.readLine()) != null) {
                    str.append(s);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
            ret.add(JsonConverter.fromJson(str.toString(), AutosaveModel.class));
        });

        return ret;
    }

    /**
     * 保存されていない編集中カルテをチェックして編集するかどうか決める.
     * @param chart
     */
    public static void checkForTemporaryFile(final ChartImpl chart) {

        List<AutosaveModel> targetModels = new ArrayList<>();
        String chartPid = chart.getKarte().getPatient().getPatientId();

        load().stream()
                .filter(m -> chartPid.equals(m.getPatientId()))
                .forEach(targetModels::add);

        if (targetModels.isEmpty()) { return; }

        SwingUtilities.invokeLater(() -> {

            String message = "保存されていない編集中カルテがみつかりました. \nエディタで開きますか?";
            String[] selection = {"開く", "破棄"};

            int opt = JSheet.showOptionDialog(chart.getFrame(), message, "保存されていないカルテ",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, selection, selection[0]);

            switch(opt) {
                case JOptionPane.YES_OPTION:

                    targetModels.stream().map(a -> {
                        a.composeDocumentModel();
                        return a.getDocumentModel();

                    }).forEach(m -> {
                        KarteEditor editor = chart.createEditor();
                        editor.setModel(m);

                        editor.setEditable(true);
                        editor.setModify(true);
                        editor.setMode(KarteEditor.DOUBLE_MODE);

                        EditorFrame editorFrame = new EditorFrame();
                        editorFrame.setChart(chart);
                        editorFrame.setKarteEditor(editor);
                        editorFrame.start();

                        editor.setDirty(true);
                    });
                    break;

                case JOptionPane.NO_OPTION:
                case JOptionPane.CLOSED_OPTION: // ESC key

                    targetModels.stream()
                            .map(a -> getTemporaryFile(a.getPatientId(), a.getDocumentModel().getDocInfo().getDocId()))
                            .forEach(f -> f.delete());
                    break;
            }
        });
    }

    /**
     * 編集中の KarteEditor を AutosaveModel にしてファイル保存する.
     */
    @Override
    public void run() {

        if (dirty) {
            long l = System.currentTimeMillis();

            AutosaveModel model = new AutosaveModel();
            model.dump(editor);

            String json = JsonConverter.toJson(model);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile))) {
                bw.write(json);

            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }

            dirty = false;
            logger.info("autosave took " + (System.currentTimeMillis() - l) + " ms");
        }
    }
}

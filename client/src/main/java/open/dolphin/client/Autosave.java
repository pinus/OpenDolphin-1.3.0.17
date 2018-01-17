package open.dolphin.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import open.dolphin.JsonConverter;
import open.dolphin.event.ProxyAction;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.ui.sheet.JSheet;
import org.apache.log4j.Logger;

/**
 * KafrteEditor の編集中 DocumentModel を定期的に一時保存する.
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

    private static File getTempFile(String patientId, String docId) {
        String path = String.format("%s%s-%s.%s", TMP_DIR, patientId, docId, SUFFIX);
        return new File(path);
    }

    public void start() {
        String patientId = editor.getContext().getPatient().getPatientId();
        String docId = editor.getModel().getDocInfo().getDocId();

        tmpFile = getTempFile(patientId, docId);
        logger.info(tmpFile);

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(this, INITIAL_DELAY, INTERVAL, TimeUnit.SECONDS);
    }

    public void stop () {
        tmpFile.delete();
        executor.shutdown();
    }

    private static List<DocumentModel> load() {
        List<DocumentModel> ret = new ArrayList<>();

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
            ret.add(JsonConverter.fromJson(str.toString(), DocumentModel.class));
        });

        return ret;
    }

    public static PatientModel getPatientModel(DocumentModel model) {
        PatientModel pm = null;

        Collection<ModuleModel> moduleModels = model.getModules();
        Iterator iter = moduleModels.iterator();
        if (iter.hasNext()) {
            ModuleModel m = (ModuleModel) iter.next();
            pm = m.getKarte().getPatient();
        }

        return pm;
    }

    public static void checkForTempFile(final ChartImpl chart) {

        List<DocumentModel> targetModels = new ArrayList<>();

        load().forEach(model -> {
            String chartPid = chart.getKarte().getPatient().getPatientId();
            String savedPid = getPatientModel(model).getPatientId();

            if (chartPid.equals(savedPid)) {
                targetModels.add(model);
            }
        });

        if (targetModels.isEmpty()) { return; }

        SwingUtilities.invokeLater(() -> {

            String message = "保存されていない編集中カルテがみつかりました. \nエディタで開きますか?";

            JButton open = new JButton("開く");
            JButton dispose = new JButton("破棄");

            JOptionPane optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION, (Icon)null, new JButton[] { open, dispose }, open);
            JSheet sheet = JSheet.createDialog(optionPane, chart.getFrame());
            sheet.addSheetListener(se -> {
                // Escape を押したときだけ JOptionPane.CLOSED_OPTION (=-1) が返る
                System.out.println("option = " + se.getOption());

                switch (se.getOption()) {
                    case JOptionPane.OK_OPTION:
                        System.out.println("------- OK");

                        targetModels.forEach(editModel -> {
                            KarteEditor editor = chart.createEditor();
                            editor.setModel(editModel);
                            editor.setEditable(true);
                            editor.setModify(true);

                            String docType = editModel.getDocInfo().getDocType();
                            int mode = docType.equals(IInfoModel.DOCTYPE_KARTE) ? KarteEditor.DOUBLE_MODE : KarteEditor.SINGLE_MODE;
                            editor.setMode(mode);

                            EditorFrame editorFrame = new EditorFrame();
                            editorFrame.setChart(chart);
                            editorFrame.setKarteEditor(editor);
                            editorFrame.start();
                        });
                        break;

                    case JOptionPane.NO_OPTION:
                    case JOptionPane.CLOSED_OPTION:
                        System.out.println("------- Cancel");
                        break;
                }
            });

            open.setAction(new ProxyAction("開く", () -> sheet.setVisible(false)));
            dispose.setAction(new ProxyAction("破棄", () -> sheet.setVisible(false)));

            sheet.setVisible(true);
        });
    }

    @Override
    public void run() {
        System.out.println("============= TIMER ======");

        String json = JsonConverter.toJson(editor.getModel());

        if (dirty) {
            logger.info("dirty");

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile))) {
                bw.write(json);

            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
}

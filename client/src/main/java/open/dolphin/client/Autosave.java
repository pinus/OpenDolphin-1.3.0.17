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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import open.dolphin.JsonConverter;
import open.dolphin.helper.ImageHelper;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.SchemaModel;
import open.dolphin.ui.sheet.JSheet;
import org.apache.log4j.Logger;

/**
 * KafrteEditor の編集中 DocumentModel を定期的に一時保存する.
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
    private final SaveParams tmpParams;
    private boolean dirty = true;

    // 自動セーブのタイマー
    private ScheduledExecutorService executor;
    // Logger
    private final Logger logger = Logger.getLogger(Autosave.class);

    public Autosave(KarteEditor e) {
        editor = e;
        tmpParams = new SaveParams();
        tmpParams.setTmpSave(true);
        tmpParams.setTitle("");
        tmpParams.setAllowPatientRef(false);
        tmpParams.setAllowClinicRef(false);
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
     * 保存された Temporary file を読み込んで DocumentModel を生成する.
     * @return
     */
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

    /**
     * DocumentModel から PatientModel を抽出する.
     * model.getKarte() は null を返すので，ModuleModel から PatientModel を取り出す.
     * @param model
     * @return
     */
    public static PatientModel getPatientModel(DocumentModel model) {
        PatientModel pm = null;

        Iterator<ModuleModel> iter = model.getModules().iterator();
        if (iter.hasNext()) {
            ModuleModel m = iter.next();
            pm = m.getKarte().getPatient();
        }

        return pm;
    }

    /**
     * 保存されていない編集中カルテをチェックして編集するかどうか決める.
     * @param chart
     */
    public static void checkForTemporaryFile(final ChartImpl chart) {

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
            String[] selection = {"開く", "破棄"};

            int opt = JSheet.showOptionDialog(chart.getFrame(), message, "保存されていないカルテ",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, selection, selection[0]);

            switch(opt) {
                case JOptionPane.YES_OPTION:

                    targetModels.forEach(editModel -> {
                        KarteEditor editor = chart.createEditor();
                        editor.setModel(editModel);
                        editor.setEditable(true);
                        editor.setModify(true);

                        String docType = editModel.getDocInfo().getDocType();
                        int mode = docType.equals(IInfoModel.DOCTYPE_KARTE) ? KarteEditor.DOUBLE_MODE : KarteEditor.SINGLE_MODE;
                        editor.setMode(mode);

                        // ByteArray を Icon に戻す
                        Collection<SchemaModel> schemas = editModel.getSchema();
                        if (schemas != null) {
                            schemas.forEach(schema -> {
                                ImageIcon icon = new ImageIcon(schema.getJpegByte());
                                schema.setIcon(icon);
                                schema.setJpegByte(null);
                            });
                        }

                        EditorFrame editorFrame = new EditorFrame();
                        editorFrame.setChart(chart);
                        editorFrame.setKarteEditor(editor);
                        editorFrame.start();

                        editor.setDirty(true);
                    });
                    break;

                case JOptionPane.NO_OPTION:
                case JOptionPane.CLOSED_OPTION: // ESC key

                    targetModels.forEach(editModel ->{
                        String pid = getPatientModel(editModel).getPatientId();
                        String docId = editModel.getDocInfo().getDocId();
                        File target = getTemporaryFile(pid, docId);
                        target.delete();
                    });
                    break;
            }
        });
    }

    /**
     * 編集中の DocumentModel を json にしてファイル保存する.
     */
    @Override
    public void run() {
        logger.info("=== TIMER ===");

        if (dirty) {
            logger.info("dirty");

            //editor.composeModel(tmpParams);

            DocumentModel model = editor.getModel();
            // convert Icon to ByteArray
            Collection<SchemaModel> schemas = model.getSchema();
            if (schemas != null) {
                schemas.stream().forEach(schema -> {
                    ImageIcon icon = schema.getIcon();
                    byte[] jpegByte = ImageHelper.imageToByteArray(icon.getImage());
                    schema.setJpegByte(jpegByte);
                    schema.setIcon(null);
                });
            }

            String json = JsonConverter.toJson(model);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile))) {
                bw.write(json);

            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }

            dirty = false;
        }
    }
}

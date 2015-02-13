package open.dolphin.orcaapi;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import open.dolphin.client.Chart;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.project.Project;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Orca Api で診療内容送信
 * orca 4.6 バージョン
 * @author pns
 */
public class OrcaApi46 extends OrcaApi {
    public static final String MEDICALMOD = "/api21/medicalmod";
    public static final String MEDICALMOD_ADD = MEDICALMOD + "?class=01";
    public static final String MEDICALMOD_DELETE = MEDICALMOD + "?class=02";
    //public static final String MEDICALMOD_REPLACE = MEDICALMOD + "?class=03"; // 置換処理は，入院のみ可能です

    private Chart context;
    private URI medicalModAdd, medicalModDelete; //, medicalModReplace;
    private SAXBuilder builder = new SAXBuilder();
    private XMLOutputter outputter = new XMLOutputter();
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    public OrcaApi46() {
        //URI 作成
        try {
            String host = Project.getClaimAddress();
            String http = "http://" + host + ":8000";

            medicalModAdd = new URI(http + MEDICALMOD_ADD);
            medicalModDelete = new URI(http + MEDICALMOD_DELETE);
            //medicalModReplace = new URI(http + MEDICALMOD_REPLACE);

        } catch (URISyntaxException ex) {
            System.out.println("OrcaApi.java: " + ex);
        }

        Format format = outputter.getFormat();
        format.setEncoding("UTF-8");
        format.setLineSeparator("\n");
        format.setIndent("  ");
        outputter.setFormat(format);
    }

    /**
     * OrcaApi に Chart を登録する
     * @param ctx
     */
    @Override
    public void setContext(Chart ctx) {
        context = ctx;
    }

    /**
     * JDOM Document から，指定した attribute を持つ最初の Element を返す
     * @param doc
     * @param attr
     * @return
     */
    private Element getElement(Document doc, String attr) {
        Element ret = null;

        Iterator iter = doc.getDescendants(new ElementFilter("string"));
        while(iter.hasNext()) {

            Element e = (Element) iter.next();
            Attribute a = e.getAttribute("name");

            if (a != null && attr.equals(a.getValue())) {
                ret = e;
                break;
            }
        }
        return ret;
    }

    /**
     * authenticate
     * ID，パスワード情報は途中で変わっている可能性があるので
     * 毎回 GET/POST の前に authenticate する
     */
    private void authenticate() {
        Authenticator.setDefault(new Authenticator(){
            @Override protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(
                        Project.getProjectStub().getOrcaUserId(),
                        Project.getProjectStub().getOrcaPassword().toCharArray());
            }
        });
    }

    /**
     * URI に対して JDOM Document を POST して，レスポンスを JDOM Document として返す
     * @param uri
     * @param doc
     * @return
     */
    public Document post(URI uri, Document doc) {
        Document responce = null;
        try {
            authenticate();
            URLConnection con = uri.toURL().openConnection();

            con.setDoOutput(true);
            outputter.output(doc, con.getOutputStream());
            responce = builder.build(con.getInputStream());

        } catch (MalformedURLException ex) {
            System.out.println("OrcaApi.java: " + ex);
        } catch (IOException ex) {
            System.out.println("OrcaApi.java: " + ex);
            //ex.printStackTrace();
        } catch (JDOMException ex) {
            System.out.println("OrcaApi.java: " + ex);
            //ex.printStackTrace();
        }

        return responce;
    }

    /**
     * URI から GET する
     * @param uri
     * @return
     */
    public Document get(URI uri) {
        Document responce = null;
        try {
            authenticate();
            URLConnection con = uri.toURL().openConnection();
            responce = builder.build(con.getInputStream());

        } catch (MalformedURLException ex) {
            System.out.println("OrcaApi.java: " + ex);
        } catch (IOException ex) {
            System.out.println("OrcaApi.java: " + ex);
            //ex.printStackTrace();
        } catch (JDOMException ex) {
            System.out.println("OrcaApi.java: " + ex);
            //ex.printStackTrace();
        }

        return responce;
    }

    /**
     * 診療内容を ORCA に送る
     * @param documentModel
     */
    @Override
    public void send(final DocumentModel documentModel) {
        final PVTHealthInsuranceModel insuranceModel = context.getHealthInsuranceToApply(documentModel.getDocInfo().getHealthInsuranceGUID());

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Document post = new Document(new OrcaApiElement.MedicalMod(documentModel, insuranceModel));
                //System.out.printf("post\n%s", outputter.outputString(post));
                Document responce = post(medicalModAdd, post);

                showMessage("病歴", responce);
                //System.out.printf("responce\n%s", outputter.outputString(responce));

                workaround(post, responce);
            }
        };
        executor.submit(r);
    }

    /**
     * 病名を ORCA に送る
     * @param diagnoses
     */
    @Override
    public void send(final List<RegisteredDiagnosisModel> diagnoses) {
        final PatientVisitModel pvt = context.getPatientVisit();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Document post = new Document(new OrcaApiElement.MedicalMod(diagnoses, pvt));
                //System.out.printf("post\n%s", outputter.outputString(post));
                Document responce = post(medicalModAdd, post);

                showMessage("病名", responce);
            }
        };
        executor.submit(r);
    }

    /**
     * Orca Api からのメッセージを表示
     *  00 登録処理終了
     *  22 登録対象のデータがありません
     *  80 内容を書き換えました【再送（外来）受診 有】
     *  80 既に同日の診療データが登録されています【再送（外来）受信 無】
     *  90 他端末使用中
     *
     * @param category
     * @param id
     * @param responce
     */
    private void showMessage(String category, Document responce) {
        String id = context.getPatient().getPatientId();
        Element resultMessage = getElement(responce, "Api_Result_Message");
        Element resultCode = getElement(responce, "Api_Result");
        Element insurance = getElement(responce, "InsuranceProvider_Class");
        String message = "", code = "";

        if (resultMessage != null && resultCode != null) {
            message = resultMessage.getText();
            code = resultCode.getText();
        }
        // 病名だけ送った場合，登録対象のデータが無いと言われる
        if ("22".equals(code)) {
            System.out.printf("[%s]%s %s(%s)\n", id, category, "病名登録処理終了", code);

        } else if ("90".equals(code)) {
            JOptionPane.showMessageDialog(context.getFrame(), "他端末で使用中のため送信できません", "ORCA API", JOptionPane.ERROR_MESSAGE);
            System.out.printf("[%s]%s %s(%s)\n", id, category, message, code);

        } else {
            System.out.printf("[%s]%s %s(%s)\n", id, category, message, code);
            if ("病名".equals(category)) workaround(responce);
        }
        //System.out.println("Insurance: " + insurance.getText());
    }

    // HealthInsurance が登録されていない場合，病名を送ると中途データがクリアされるのの workaround
    // post をキャッシュしておいて，再送する
    private Document postCache;
    private void workaround(Document post, Document responce) {
        Element insurance = getElement(responce, "InsuranceProvider_Class");
        if ("".equals(insurance.getText())) {
            // 再送できるように cache しておく
            postCache = post;
        } else {
            postCache = null;
        }
    }
    private void workaround(Document responce) {
        if (postCache != null) {

            String patientId = getElement(responce, "Patient_ID").getText();
            String cachedPatientId = getElement(postCache, "Patient_ID").getText();

            if (cachedPatientId.equals(patientId)) {
                Document newResponce = post(medicalModAdd, postCache);
                Element resultMessage = getElement(newResponce, "Api_Result_Message");
                Element resultCode = getElement(responce, "Api_Result");
                System.out.printf("[%s]病歴再送：%s(%s)\n", patientId, resultMessage.getText(), resultCode.getText());
            }
        }
    }
}

package open.dolphin.orcaapi;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import open.dolphin.client.Chart;
import open.dolphin.client.ClientContext;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.project.Project;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Orca Api で診療内容送信
 * orca 4.7 xml2 バージョン
 * @author pns
 */
public class OrcaApi47 extends OrcaApi {
    public static final String MEDICALMOD = "/api21/medicalmodv2";
    public static final String MEDICALMOD_ADD = MEDICALMOD + "?class=01";
    public static final String MEDICALMOD_DELETE = MEDICALMOD + "?class=02";
    //public static final String MEDICALMOD_REPLACE = MEDICALMOD + "?class=03"; // 置換処理は，入院のみ可能です

    private Chart context;
    private URI medicalModAdd, medicalModDelete; //, medicalModReplace;
    private SAXBuilder builder = new SAXBuilder();
    private XMLOutputter outputter = new XMLOutputter();
    //private ExecutorService executor = Executors.newSingleThreadExecutor();
    // singlethread だと，busy 処理中に次の send が受けられなくなる
    private ExecutorService executor = Executors.newCachedThreadPool();

    private Logger logger;

    public OrcaApi47() {
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

        logger = ClientContext.getBootLogger();
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
     * JDOM Document から，指定した名前の Element を返す
     * @param doc
     * @param attr
     * @return
     */
    private Element getElement(Document doc, String name) {
        Element ret = null;

        Iterator iter = doc.getDescendants(new ElementFilter(name));
        while(iter.hasNext()) {

            Element e = (Element) iter.next();

            if (e.getName().equals(name)) {
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
                Document post = new Document(new OrcaApiElementXml2.MedicalMod(documentModel, insuranceModel));
                Document responce = post(medicalModAdd, post);
                String resultCode = getElement(responce, "Api_Result").getTextTrim();
                //System.out.printf("病歴 post\n%s", outputter.outputString(post));

                // 他端末で使用中(90)の場合は，手動でリトライする
                while("90".equals(resultCode)) {
                    logger.info("OrcaApi47: busy, waiting for retrial (" + resultCode + ")");
                    if (JOptionPane.NO_OPTION ==
                            JOptionPane.showConfirmDialog(context.getFrame(),
                            "ORCA で使用中のため送信できません。リトライしますか？", "ORCA 送信エラー", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE)){
                        break;
                    }

                    post = new Document(new OrcaApiElementXml2.MedicalMod(documentModel, insuranceModel));
                    responce = post(medicalModAdd, post);
                    resultCode = getElement(responce, "Api_Result").getTextTrim();
                }

                showMessage("病歴", responce);
                //System.out.printf("病歴 responce\n%s", outputter.outputString(responce));
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
                Document post = new Document(new OrcaApiElementXml2.MedicalMod(diagnoses, pvt));
                Document responce = post(medicalModAdd, post);
                String resultCode = getElement(responce, "Api_Result").getTextTrim();
                //System.out.printf("病名 post\n%s", outputter.outputString(post));

                // 他端末で使用中(90)の場合は，リトライを繰り返す
                int count = 0;
                while(count++ < 20 && "90".equals(resultCode)) {
                    logger.info("OrcaApi47: [" + pvt.getPatientId() + "] busy, waiting for retrial (" + resultCode + ")");
                    try{Thread.sleep(15000);}catch(Exception e){}

                    post = new Document(new OrcaApiElementXml2.MedicalMod(diagnoses, pvt));
                    responce = post(medicalModAdd, post);
                    resultCode = getElement(responce, "Api_Result").getTextTrim();
                }
                //logger.info("OrcaApi47: retry count = " + count);

                showMessage("病名", responce);
                //System.out.printf("病名 responce\n%s", outputter.outputString(responce));
            }
        };
        executor.submit(r);
    }

    /**
     * Orca Api からのメッセージを表示
     * Api_Result
     *  00 登録処理終了
     *  22 登録対象のデータがありません
     *  80 内容を書き換えました【再送（外来）受診 有】
     *  80 既に同日の診療データが登録されています【再送（外来）受信 無】
     *  90 他端末使用中
     * Disease_Result
     *  01 登録出来ない病名が存在します
     * Disease_Warning
     *  E01 同名の病名が平成XX年X月X日に存在します（転帰日等を確認して下さい）
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
        Element diseaseResultCode = getElement(responce, "Disease_Result");
        Element diseaseResultMessage = getElement(responce, "Disease_Result_Message");
        Element diseaseWarningMessage = getElement(responce, "Disease_Warning_Message");

        String message = "", code = "";
        String disMessage = "", disCode = "";

        if (resultMessage != null && resultCode != null) {
            message = resultMessage.getText();
            code = resultCode.getTextTrim();
        }

        if (diseaseResultMessage != null && diseaseResultCode != null) {
            disMessage = diseaseResultMessage.getText();
            disCode = diseaseResultCode.getText();
        }

        if ("90".equals(code)) {
            //JOptionPane.showMessageDialog(context.getFrame(),
            JOptionPane.showMessageDialog(null,
                    String.format("カルテ [%s] は ORCA 端末で使用中のため\n「%s」を送信できませんでした", id, category), "ORCA 送信エラー", JOptionPane.ERROR_MESSAGE);
            logger.warn(String.format("[%s]%s %s(%s)", id, category, message, code));

        } else {
            logger.info(String.format("[%s]%s %s(%s)", id, category, message, code));
            if (! disCode.equals("")) {
                logger.info(String.format("[%s]%s %s(%s)", id, category, disMessage, disCode));
            }
        }
        //System.out.println("Insurance: " + insurance.getText());
    }
}

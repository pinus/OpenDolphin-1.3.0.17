package open.dolphin.client;

import java.util.Calendar;
import java.util.GregorianCalendar;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.delegater.PnsDelegater;
import open.dolphin.dto.DiagnosisSearchSpec;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.PatientVisitModel;
import org.apache.log4j.Logger;

/**
 * ちょっとカルテ内容をチェックして pvt に必要な情報をセットする
 */
public class DocumentPeeker {

    public static final int LEAST_KARTE_SIZE = 15; // カルテに必要な最低文字数

    private DocumentDelegater ddl;
    private PnsDelegater pnsdl;
    private DiagnosisSearchSpec spec;
    private KarteBean karte;
    private PatientVisitModel pvt;
    private GregorianCalendar today;
    private GregorianCalendar yesterday;

    private Logger logger;

    public DocumentPeeker() {
        //使い回す Object
        today = new GregorianCalendar();
        ddl = new DocumentDelegater();
        pnsdl = new PnsDelegater();
        spec = new DiagnosisSearchSpec();
        yesterday = new GregorianCalendar();
        yesterday.add(GregorianCalendar.DATE, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 23);

        logger = ClientContext.getBootLogger();
    }

    public DocumentPeeker(PatientVisitModel pvt) {
        this();
        setPatientVisitModel(pvt);
    }

    public void setPatientVisitModel(PatientVisitModel pvt) {
        this.pvt = pvt;
        karte = ddl.getKarte(pvt.getPatient().getId(), today.getTime());
    }

    public boolean isKarteEmpty() {
        if (pvt == null) {
            logger.info("pvt null -- set pvt!");
            return false;
        }
        String text = pnsdl.peekKarte(pvt.getPatient().getId());
        return isKarteEmpty(text);
    }

    public static boolean isKarteEmpty(String text) {
        boolean maybe;
        if (text == null) maybe = false;
        else if (text.length() < LEAST_KARTE_SIZE) maybe = true;
        else if (text.indexOf("+++") != -1) maybe = true; // karte のどこかに "+++" が書いてある場合，書きかけカルテと判断する
        else maybe = false;
        return maybe;
        //logger.info(text);
    }
}

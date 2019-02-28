package open.dolphin.orca;

import open.dolphin.dto.PatientSearchSpec;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.InfoModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.orca.pushapi.PushApi;
import open.dolphin.orca.pushapi.SubscriptionEvent;
import open.dolphin.orca.pushapi.bean.Body;
import open.dolphin.orca.pushapi.bean.Data;
import open.dolphin.orca.pushapi.bean.Response;
import open.dolphin.service.PatientService;
import open.dolphin.service.PvtService;
import org.apache.log4j.Logger;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.util.Base64;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.security.RunAs;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * PvtClient.
 * ORCA PushApi で受付情報を受け取り，PvtBuilder で PatientVisitModel を作って PvtService に渡す.
 *
 * @author pns
 */
@Singleton
@Startup
@DependsOn({"OrcaHostInfo", "OrcaUserInfo"})
@RunAs("user")
public class PvtClient {
    private PushApi pushApi;
    private Response subscriptionRes;
    private PvtBuilder pvtBuilder;
    private final Logger logger = Logger.getLogger(PvtClient.class);

    @Inject
    private PvtService pvtService;

    @Inject
    private PatientService patientService;

    public PvtClient() {
        pvtBuilder = new PvtBuilder();
        pushApi = PushApi.getInstance();
        pushApi.addResponseListener(this::onResponse);
    }

    @PostConstruct
    public void subscribe() {
        pushApi.subscribe(SubscriptionEvent.ACCEPT);
    }

    @PreDestroy
    public void unsubscribe() {
        pushApi.unsubscribe(subscriptionRes);
    }

    private void onResponse(Response res) {
        String command = res.getCommand();

        switch (command) {
            case "subscribed":
                subscriptionRes = res;
            case "unsubscribed":
                logger.info(String.format("command = %s\nreq.id = %s\nsub.id = %s\n\n", command, res.getReqId(), res.getSubId()));
                break;

            case "event":
                Data data = res.getData();
                Body body = data.getBody();
                String event = data.getEvent();
                logger.info("event = " + event + ", mode = " + body.getPatient_Mode());

                // 受付通知: mode = add / modify / delete
                if (event.equals(SubscriptionEvent.ACCEPT.eventName())) {
                    DummyHeader.set();
                    switch(body.getPatient_Mode()) {
                        case "delete":
                            String ptId = body.getPatient_ID(); // 患者番号 002906
                            PatientModel patientModel = patientService.getPatient(ptId);
                            PatientVisitModel pvt = pvtService.getPvtOf(patientModel);
                            pvtService.removePvt(pvt.getId());
                            logger.info(("PvtClient: pvt removed [" + data.getBody().getPatient_ID() + "]"));
                            break;

                        case "add":
                        case "modify":
                            pvtBuilder.build(body);
                            PatientVisitModel model = pvtBuilder.getProduct();
                            pvtService.addPvt(model);
                            logger.info("PvtClient: addPvt [" + model.getPatient().getPatientId() + "]");
                            break;
                    }
                }
                break;
        }
    }

    /**
     * 内部から PvtService を呼ぶためのダミーヘッダ
     */
    private static class DummyHeader implements HttpHeaders {
        private static DummyHeader dummyHeader = new DummyHeader();
        private static String header;

        private DummyHeader() { }

        public static void set() {
            String str = IInfoModel.DEFAULT_FACILITY_OID + InfoModel.COMPOSITE_KEY_MAKER
                    + "dummyUser" + InfoModel.PASSWORD_SEPARATOR + "dummyPass";
            header = Base64.encodeBytes(str.getBytes());
            Map<Class<?>, Object> contextDataMap = ResteasyProviderFactory.getContextDataMap();
            contextDataMap.put(HttpHeaders.class, dummyHeader);
        }

        @Override
        public List<String> getRequestHeader(String s) { return null; }
        @Override
        public String getHeaderString(String s) { return header; }
        @Override
        public MultivaluedMap<String, String> getRequestHeaders() { return null; }
        @Override
        public List<MediaType> getAcceptableMediaTypes() { return null; }
        @Override
        public List<Locale> getAcceptableLanguages() { return null; }
        @Override
        public MediaType getMediaType() { return null; }
        @Override
        public Locale getLanguage() { return null; }
        @Override
        public Map<String, Cookie> getCookies() { return null; }
        @Override
        public Date getDate() { return null; }
        @Override
        public int getLength() { return 0; }
    }
}

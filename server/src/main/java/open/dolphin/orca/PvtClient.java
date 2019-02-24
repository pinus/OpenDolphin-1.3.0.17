package open.dolphin.orca;

import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.orca.pushapi.PushApi;
import open.dolphin.orca.pushapi.SubscriptionEvent;
import open.dolphin.orca.pushapi.bean.Data;
import open.dolphin.orca.pushapi.bean.Response;
import open.dolphin.service.PvtService;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * PvtClient.
 * ORCA PushApi で受付情報を受け取り，PvtBuilder で PatientVisitModel を作って PvtService に渡す.
 * @author pns
 */
@Singleton
@Startup
@RunAs("user")
public class PvtClient {
    private PushApi pushApi;
    private Response subscriptionRes;
    private PvtBuilderApi pvtBuilder;
    private final Logger logger = Logger.getLogger(PvtClient.class);

    @EJB
    private PvtService pvtService;

    public PvtClient() {
        pvtBuilder = new PvtBuilderApi();
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
                String event = data.getEvent();
                logger.info("event = " + event + ", mode = " + data.getBody().getPatient_Mode());

                // 受付通知
                if (event.equals(SubscriptionEvent.ACCEPT.eventName())) {
                    pvtBuilder.build(data.getBody());
                    PatientVisitModel model = pvtBuilder.getProduct();
                    pvtService.addPvt(model, IInfoModel.DEFAULT_FACILITY_OID);
                    logger.info("PvtClient: addPvt [" + model.getPatient().getPatientId() + "]");
                }
                break;
        }
    }
}

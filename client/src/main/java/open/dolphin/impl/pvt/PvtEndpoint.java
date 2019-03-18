package open.dolphin.impl.pvt;

import open.dolphin.JsonConverter;
import open.dolphin.infomodel.PatientVisitModel;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

/**
 * @author pns
 */
public class PvtEndpoint extends Endpoint {

    private PvtListener pvtListener;

    public PvtEndpoint() {
        super();
    }

    public void addPvtListener(PvtListener listener) {
        pvtListener = listener;
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        session.addMessageHandler((PvtMessageHandler) message -> {
            PatientVisitModel hostPvt = JsonConverter.fromJson(message, PatientVisitModel.class);
            pvtListener.pvtChanged(hostPvt);
        });
    }

    @Override
    public void onError(Session session, Throwable t) {
        System.out.println("WaitingListImp: WebSocket error: " + t.toString());
    }

    @Override
    public void onClose(Session session, CloseReason reason) {
        System.out.println("WaitingListImpl: WebSocket colosed: " + reason.getReasonPhrase());
    }
}

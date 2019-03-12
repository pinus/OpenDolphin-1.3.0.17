package open.dolphin.orca.pushapi;

import open.dolphin.JsonConverter;
import open.dolphin.orca.pushapi.bean.Response;
import org.apache.log4j.Logger;

import javax.websocket.*;
import java.util.HashSet;

/**
 * WebSocket の Endpoint (Tyrus).
 *
 * @author pns
 */
@ClientEndpoint(configurator = PushApiEndopointConfigurator.class)
public class PushApiEndpoint {

    /**
     * 受け取った message を伝える listener.
     */
    private final HashSet<ResponseListener> responseListeners;

    private final Logger logger = Logger.getLogger(PushApiEndpoint.class);

    public PushApiEndpoint() {
        responseListeners = new HashSet<>();
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        logger.info("open id = " + session.getId());
    }

    @OnMessage
    public void onMessage(String str) {
        Response response = JsonConverter.fromJson(str, Response.class);
        responseListeners.forEach(listener -> listener.onResponse(response));
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info("closeReason = " + closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        thr.printStackTrace(System.err);
    }

    /**
     * ResponseListener を登録する.
     *
     * @param l ResponseListener
     */
    public void addResponseListener(ResponseListener l) {
        responseListeners.add(l);
    }

    /**
     * ResponseListener を削除する.
     *
     * @param l ResponseListener
     */
    public void removeResponseListener(ResponseListener l) {
        responseListeners.remove(l);
    }
}

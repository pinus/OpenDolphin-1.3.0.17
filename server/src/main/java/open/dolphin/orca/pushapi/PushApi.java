package open.dolphin.orca.pushapi;

import open.dolphin.orca.OrcaHostInfo;
import open.dolphin.orca.pushapi.bean.Command;
import open.dolphin.orca.pushapi.bean.Response;
import open.dolphin.orca.pushapi.bean.Subscribe;
import open.dolphin.orca.pushapi.bean.Unsubscribe;
import open.dolphin.util.JsonUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.logging.Logger;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

/**
 * PushAPI.
 * <ol>
 * <li>jma-receipt-pusher パッケージのインストールが必要
 * <li>WebSocket接続先は、 ws://localhost:9400/ws
 * <li>Header に "X-GINBEE-TENANT-ID: 1" を設定する
 * </ol>
 *
 * @author pns
 */
public class PushApi {
    private static final PushApi PUSH_API = new PushApi();

    private final OrcaHostInfo hostInfo;
    private final PushApiEndpoint endpoint;
    private Session session;

    private final Logger logger;

    private PushApi() {
        hostInfo = OrcaHostInfo.getInstance();
        endpoint = new PushApiEndpoint();
        logger = Logger.getLogger(PushApi.class);
        logger.info("PusApi created");
    }

    /**
     * PushApi のインスタンス.
     *
     * @return PushApi
     */
    public static PushApi getInstance() {
        return PUSH_API;
    }

    /**
     * Event を購読.
     *
     * @param event 購読する Event
     */
    public void subscribe(SubscriptionEvent event) {

        String reqId = RandomStringUtils.randomAlphabetic(10);

        Subscribe command = new Subscribe(event);
        command.setReqId(reqId);

        URI uri = hostInfo.getPushApiUri();

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        try {
            session = container.connectToServer(endpoint, uri);
            send(command);

        } catch (DeploymentException | IOException ex) {
            logger.info(ex.getMessage());
        }
    }

    /**
     * 購読を停止する.
     *
     * @param res 購読の時に返ってきた response
     */
    public void unsubscribe(Response res) {

        Unsubscribe command = new Unsubscribe();
        command.setReqId(res.getReqId());
        command.setSubId(res.getSubId());

        send(command);
    }

    /**
     * WebSocket に command を送る.
     *
     * @param command コマンド
     */
    private void send(Command command) {
        try {
            String text = JsonUtils.toJson(command);
            session.getBasicRemote().sendText(text);

        } catch (IOException ex) {
            logger.info(ex.getMessage());
        }
    }

    /**
     * ResponseListener を登録する.
     *
     * @param l ResponseListener
     */
    public void addResponseListener(ResponseListener l) {
        endpoint.addResponseListener(l);
    }
}

package open.dolphin.orca.pushapi;

import java.io.IOException;
import java.net.URI;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import open.dolphin.orca.pushapi.bean.Command;
import open.dolphin.orca.pushapi.bean.Response;
import open.dolphin.orca.pushapi.bean.Subscribe;
import open.dolphin.orca.pushapi.bean.Unsubscribe;
import open.dolphin.JsonConverter;
import open.dolphin.orca.OrcaHostInfo;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * PushAPI.
 * <ol>
 * <li>jma-receipt-pusher パッケージのインストールが必要
 * <li>WebSocket接続先は、 ws://localhost:9400/ws
 * <li>Header に "X-GINBEE-TENANT-ID: 1" を設定する
 * </ol>
 * @author pns
 */
public class PushApi {

    private enum ProcessResult { RETRY, ABORT, DONE };
    private static final int MAX_CONNECTION_RETRY = 10;
    private int retryCounter =0;

    private static final PushApi PUSH_API = new PushApi();
    private final OrcaHostInfo hostInfo = OrcaHostInfo.getInstance();
    private final PushApiEndpoint endpoint = new PushApiEndpoint();
    private Session session;

    private Logger logger = Logger.getLogger(PushApi.class);

    private PushApi() { }

    /**
     * PushApi のインスタンス.
     * @return PushApi
     */
    public static PushApi getInstance() {
        return PUSH_API;
    }

    /**
     * Event を購読.
     * @param event 購読する Event
     */
    public void subscribe(SubscriptionEvent event) {

        String reqId = RandomStringUtils.randomAlphabetic(10);

        Subscribe command = new Subscribe(event);
        command.setReqId(reqId);

        URI uri = hostInfo.getPushApiUri();

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        ProcessResult result = ProcessResult.RETRY;
        while (result.equals(ProcessResult.RETRY)) {
            try {
                session = container.connectToServer(endpoint, uri);
                send(command);
                result = ProcessResult.DONE;

            } catch (DeploymentException | IOException ex) {
                result = processError(ex);
            }
        }
    }

    /**
     * 購読を停止する.
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
     * @param command コマンド
     */
    private void send(Command command) {
        try {
            String text = JsonConverter.toJson(command);
            session.getBasicRemote().sendText(text);

        } catch (IOException ex) {
            processError(ex);
        }
    }

    /**
     * ResponseListener を登録する.
     * @param l ResponseListener
     */
    public void addResponseListener(ResponseListener l) {
        endpoint.addResponseListener(l);
    }

    /**
     * エラー処理.
     * @param e Exception
     */
    private ProcessResult processError(Exception e) {
        logger.info(e.getCause());
        logger.info("retry counter = " + retryCounter);

        switch (e.getCause().getMessage()) {

            case "Operation timed out":
                if (retryCounter < MAX_CONNECTION_RETRY) {
                    retryCounter++;
                    return ProcessResult.RETRY;
                }
                break;

            case "Connection refused":
                if (retryCounter < MAX_CONNECTION_RETRY) {
                    retryCounter++;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException iex) {
                    }
                    return ProcessResult.RETRY;
                }
                break;
        }
        retryCounter = 0;
        return ProcessResult.ABORT;
    }
}

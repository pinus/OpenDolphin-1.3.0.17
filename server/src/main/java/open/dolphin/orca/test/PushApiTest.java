package open.dolphin.orca.test;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.WebSocketContainer;
import jakarta.websocket.Session;
import open.dolphin.orca.OrcaHostInfo;
import open.dolphin.orca.pushapi.PushApiEndpoint;
import open.dolphin.orca.pushapi.SubscriptionEvent;
import open.dolphin.orca.pushapi.bean.Response;
import open.dolphin.orca.pushapi.bean.Subscribe;
import open.dolphin.util.JsonUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.URI;

/**
 * run with module -cp open.dolphin.client
 */
public class PushApiTest {
    public PushApiTest() {
    }

    public static void main(String[] argv) {
        PushApiTest test = new PushApiTest();
        test.start();
    }

    private synchronized void start() {
        System.setProperty("jboss.server.base.dir", System.getProperty("user.dir"));

        PushApiEndpoint endpoint = new PushApiEndpoint();
        endpoint.addResponseListener(this::onResponse);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = OrcaHostInfo.getInstance().getPushApiUri();
        System.out.println("session url = " + uri);

        String reqId = RandomStringUtils.randomAlphabetic(10);
        Subscribe command = new Subscribe(SubscriptionEvent.ALL);
        command.setReqId(reqId);
        String text = JsonUtils.toJson(command);
        System.out.println("command = " + text);

        try {
            Session session = container.connectToServer(endpoint, uri);
            session.getBasicRemote().sendText(text);

        } catch (DeploymentException | IOException ex) {
            System.out.println("exception = " + ex.getMessage());
        }

        try {
            System.out.println("waiting response... ");
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }

    public void onResponse(Response res) {
        System.out.println("res = " + JsonUtils.toJson(res));
    }
}

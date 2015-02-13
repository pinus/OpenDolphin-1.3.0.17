package open.dolphin.delegater;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 *
 * @author pns
 */
@ClientEndpoint
public class ClientWebSocket {

    @OnMessage
    public void onMessage(String message) {
        System.out.println("---- message = " + message);
        System.out.println("----pvt pk=  " + Long.valueOf(message));
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket open: id = " + session.getId());
    }

    @OnClose
    public void onClose(CloseReason reason) {
        System.out.println("WebSocket closed: " + reason.getReasonPhrase());
    }

}

package open.dolphin.delegater;

import jakarta.websocket.*;

/**
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

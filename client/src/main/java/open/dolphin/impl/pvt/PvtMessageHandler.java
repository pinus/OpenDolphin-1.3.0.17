package open.dolphin.impl.pvt;

import jakarta.websocket.MessageHandler;

/**
 * @author pns
 */
public interface PvtMessageHandler extends MessageHandler.Whole<String> {

    @Override
    public void onMessage(String message);
}

package open.dolphin.orca.pushapi;

import open.dolphin.orca.pushapi.bean.Response;

import java.util.EventListener;

/**
 * WebSocket 接続開始時に返ってくる response のリスナ.
 *
 * @author pns
 */
public interface ResponseListener extends EventListener {

    public void onResponse(Response response);
}

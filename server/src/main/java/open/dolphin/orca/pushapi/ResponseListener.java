package open.dolphin.orca.pushapi;

import java.util.EventListener;

import open.dolphin.orca.pushapi.bean.Response;

/**
 * WebSocket 接続開始時に返ってくる response のリスナ.
 *
 * @author pns
 */
public interface ResponseListener extends EventListener {

    public void onResponse(Response response);
}

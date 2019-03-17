package open.dolphin.orca.pushapi.bean;

/**
 * Response として送られてくる Data.
 *
 * @author pns
 */
public class Data {
    /**
     * 通知UUID (例: 86bcfafd-18b3-4a3d-931c-29dc250141ad)
     */
    private String uuid;

    /**
     * 通知ID (例: 53)
     */
    private String id;

    /**
     * イベント名.
     */
    private String event;

    /**
     * 接続ユーザ (例: ormaster)
     */
    private String user;

    /**
     * 通知時間 (例: 2017-07-10T16:42:15+0900)
     */
    private String time;

    /**
     * 明細.
     */
    private Body body;

    /**
     * 通知UUID (例: 86bcfafd-18b3-4a3d-931c-29dc250141ad)
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 通知ID (例: 53)
     */
    public String getId() {
        return id;
    }

    /**
     * イベント名.
     *
     * @return the event
     */
    public String getEvent() {
        return event;
    }

    /**
     * 接続ユーザ (例: ormaster)
     *
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * 通知時間 (例: 2017-07-10T16:42:15+0900)
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * 明細.
     *
     * @return the body
     */
    public Body getBody() {
        return body;
    }
}

package open.dolphin.orca.pushapi.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Websocket 接続時の command / response
 *
 * @author pns
 */
public class Command {

    /**
     * コマンド.
     */
    private String command;

    /**
     * 購読リクエスト ID
     */
    @JsonProperty(value = "req.id")
    private String reqId;

    /**
     * 購読者 ID
     */
    @JsonProperty(value = "sub.id")
    private String subId;

    /**
     * 購読するイベント名.
     */
    private String event;

    /**
     * コマンド.
     *
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * コマンド.
     *
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * 購読リクエスト ID
     *
     * @return the reqId
     */
    public String getReqId() {
        return reqId;
    }

    /**
     * 購読リクエスト ID
     *
     * @param reqId the reqId to set
     */
    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    /**
     * 購読者 ID
     *
     * @return the subId
     */
    public String getSubId() {
        return subId;
    }

    /**
     * 購読者 ID
     *
     * @param subId the subId to set
     */
    public void setSubId(String subId) {
        this.subId = subId;
    }

    /**
     * 購読するイベント名.
     *
     * @return the event
     */
    public String getEvent() {
        return event;
    }

    /**
     * 購読するイベント名.
     *
     * @param event the event to set
     */
    public void setEvent(String event) {
        this.event = event;
    }
}

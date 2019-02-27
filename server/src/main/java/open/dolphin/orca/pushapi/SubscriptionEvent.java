package open.dolphin.orca.pushapi;

/**
 * PushAPI subscription events.
 *
 * @author pns
 */
public enum SubscriptionEvent {
    ACCEPT("patient_accept"), // 受付通知
    INFORMATION("patient_information"), // 患者登録通知
    ACCOUNT("patient_account"), // 診療行為通知
    MEMO("patient_memo"), // メモ登録通知
    ;

    private String eventName;

    SubscriptionEvent(String str) {
        eventName = str;
    }

    public String eventName() {
        return eventName;
    }
}

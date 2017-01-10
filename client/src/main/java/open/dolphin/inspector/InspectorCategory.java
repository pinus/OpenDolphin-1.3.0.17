package open.dolphin.inspector;

/**
 *
 * @author pns
 */
public enum InspectorCategory {
    メモ("メモ"),
    カレンダー("来院歴"),
    文書履歴("文書履歴"),
    アレルギー("アレルギー"),
    身長体重("身長体重"),
    病名("病名"),
    関連文書("関連文書"),
    なし("")
    ;

    private final String title;

    private InspectorCategory(String t) {
        title = t;
    }

    public String title() {
        return title;
    }
}

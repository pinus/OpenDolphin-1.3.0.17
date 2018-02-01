package open.dolphin.infomodel;

/**
 * PatientVisitModel の status.
 * ChartImpl の status が複雑になりすぎたので独立させた.
 *
 * @author pns
 */
public class KarteState {
    private KarteState() {}

    /** 診察未終了で閉じている状態 */
    public static final int CLOSE_NONE          = 0;
    /** 診察が終了し閉じている状態  */
    public static final int CLOSE_SAVE          = 1;
    /** 診察未終了でオープンしている状態 */
    public static final int OPEN_NONE           = 2;
    /** 診察が終了しオープンしている状態 */
    public static final int OPEN_SAVE           = 3;
    /** 診察が終了でカルテ未記載でオープンの状態 */
    public static final int OPEN_UNFINISHED     = 4;
    /** 診察が終了でカルテ未記載で閉じている状態 */
    public static final int CLOSE_UNFINISHED    = 5;
    /** 仮保存でオープンしている状態 */
    public static final int OPEN_TEMP           = 6;
    /** 仮保存で閉じている状態 */
    public static final int CLOSE_TEMP          = 7;
    /** 受付キャンセル */
    public static final int CANCEL_PVT          = -1;
    /** ReadOnly */
    public static final int READ_ONLY           = -2;

    /**
     * OPEN かどうかを判定.
     * @param state 判定される state
     * @return 判定結果
     */
    public static boolean isOpen(int state) {
        return (state == OPEN_NONE || state == OPEN_SAVE ||
                state == OPEN_UNFINISHED || state == OPEN_TEMP);
    }

    /**
     * CLOSE かどうかを判定.
     * @param state 判定される state
     * @return 判定結果
     */
    public static boolean isClosed(int state) {
        return (state == CLOSE_NONE || state == CLOSE_SAVE ||
                state == CLOSE_UNFINISHED || state == CLOSE_TEMP);
    }

    /**
     * NONE かどうかを判定.
     * @param state 判定される state
     * @return 判定結果
     */
    public static boolean isNone(int state) {
        return (state == OPEN_NONE || state == CLOSE_NONE);
    }

    /**
     * CLOSE を OPEN に変換.
     * @param state 判定される state
     * @return 判定結果
     */
    public static int toOpenState(int state) {
        if (state == CLOSE_NONE) return OPEN_NONE;
        if (state == CLOSE_SAVE) return OPEN_SAVE;
        if (state == CLOSE_UNFINISHED) return OPEN_UNFINISHED;
        if (state == CLOSE_TEMP) return OPEN_TEMP;

        return state;
    }

    /**
     * OPEN を CLOSE に変換 〜単純バージョン.
     * @param state 変換する state
     * @return 変換された state
     */
    public static int toClosedState(int state) {
        if (state == OPEN_NONE) return CLOSE_NONE;
        if (state == OPEN_TEMP) return CLOSE_TEMP;
        if (state == OPEN_SAVE) return CLOSE_SAVE;
        if (state == OPEN_UNFINISHED) return CLOSE_UNFINISHED;

        return state;
    }

    /**
     * OPEN を CLOSE に変換 〜SAVE/UNFINISHED 判定バージョン.
     * カルテが未記載の場合は CLOSE_UNFINISHED にする.
     * @param state 変換する state
     * @param isEmpty カルテが未記載かどうかのフラグ
     * @return 変換された state
     */
    public static int toClosedState(int state, boolean isEmpty) {
        if (state == OPEN_NONE) return CLOSE_NONE;
        if (state == OPEN_TEMP) return CLOSE_TEMP;

        if (state == OPEN_SAVE || state == OPEN_UNFINISHED) {
            return (isEmpty)? CLOSE_UNFINISHED: CLOSE_SAVE;
        }
        return state;
    }
}

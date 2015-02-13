package open.dolphin.impl.pvt;

/**
 * PVT state をできるだけスピーディーに取り出すために，server から
 * pvtState + 100*byomeiCount + 10000*byomeiCountToday の形で送信するようにした
 * それを decode するためのクラス
 * @author pns
 */
class PvtStateDecoder {
    public final int BYOMEI_COUNT_MASK = 100;
    public final int BYOMEI_COUNT_TODAY_MASK = 10000;
    private int state;
    private int byomeiCount;
    private int byomeiCountToday;

    public void decode(int encoded) {
        state = encoded + 10;

        byomeiCountToday = state/BYOMEI_COUNT_TODAY_MASK;
        state = state - byomeiCountToday * BYOMEI_COUNT_TODAY_MASK;

        byomeiCount = state/BYOMEI_COUNT_MASK;
        state = state - byomeiCount * BYOMEI_COUNT_MASK;

        state = state - 10;
    }
    public int getState() {
        return state;
    }
    public int getByomeiCount() {
        return byomeiCount;
    }
    public int getByomeiCountToday() {
        return byomeiCountToday;
    }
}

package open.dolphin.dto;

/**
 *
 * @author pns
 */
public class PvtStateSpec {
    private long pk;
    private int state;
    private int byomeiCount;
    private int byomeiCountToday;

    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getByomeiCount() {
        return byomeiCount;
    }

    public void setByomeiCount(int totalByomeiCount) {
        this.byomeiCount = totalByomeiCount;
    }

    public int getByomeiCountToday() {
        return byomeiCountToday;
    }

    public void setByomeiCountToday(int byomeiCountToday) {
        this.byomeiCountToday = byomeiCountToday;
    }
}

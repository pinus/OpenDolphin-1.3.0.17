package open.dolphin.orca.orcaapi.bean;

/**
 * Visitptlstres.
 * contains response to visitptlstreq (either visitptlst01res/02res)
 * @author pns
 */
public class Visitptlstres {
    /**
     * 来院日一覧.
     */
    private Visitptlst01res visitptlst01res;

    /**
     * 来院年月一覧.
     */
    private Visitptlst02res visitptlst02res;

    /**
     * visitptlst01res
     *
     * @return visitptlst01res
     */
    public Visitptlst01res getVisitptlst01res() {
        return visitptlst01res;
    }

    /**
     * visitptlst01res
     *
     * @param visitptlst01res to set
     */
    public void setVisitptlst01res(Visitptlst01res visitptlst01res) {
        this.visitptlst01res = visitptlst01res;
    }

    /**
     * visitptlst02res
     *
     * @return visitptlst02res
     */
    public Visitptlst02res getVisitptlst02res() {
        return visitptlst02res;
    }

    /**
     * visitptlst02res
     *
     * @param visitptlst02res to set
     */
    public void setVisitptlst02res(Visitptlst02res visitptlst02res) {
        this.visitptlst02res = visitptlst02res;
    }
}

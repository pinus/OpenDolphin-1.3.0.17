package open.dolphin.orca.orcaapi.bean;

/**
 * Medicalgetres.
 * contains response to medicalgetreq (either medicalget01res/02res/03res/04res)
 *
 * @author pns
 */
public class Medicalgetres {
    /**
     * 受診履歴一覧取得.
     */
    private Medicalget01res medicalget01res;

    /**
     * 診療行為剤内容詳細取得.
     */
    private Medicalget02res medicalget02res;

    /**
     * 診療月診療コード情報取得.
     */
    private Medicalget03res medicalget03res;

    /**
     * 診療区分別剤点数取.
     */
    private Medicalget04res medicalget04res;

    /**
     * medicalget01res
     *
     * @return medicalget01res
     */
    public Medicalget01res getMedicalget01res() {
        return medicalget01res;
    }

    /**
     * medicalget01res
     *
     * @param medicalget01res to set
     */
    public void setMedicalget01res(Medicalget01res medicalget01res) {
        this.medicalget01res = medicalget01res;
    }

    /**
     * medicalget02res
     *
     * @return medicalget02res
     */
    public Medicalget02res getMedicalget02res() {
        return medicalget02res;
    }

    /**
     * medicalget02res
     *
     * @param medicalget02res to set
     */
    public void setMedicalget02res(Medicalget02res medicalget02res) {
        this.medicalget02res = medicalget02res;
    }

    /**
     * medicalget03res
     *
     * @return medicalget03res
     */
    public Medicalget03res getMedicalget03res() {
        return medicalget03res;
    }

    /**
     * medicalget03res
     *
     * @param medicalget03res to set
     */
    public void setMedicalget03res(Medicalget03res medicalget03res) {
        this.medicalget03res = medicalget03res;
    }

    /**
     * medicalget04res
     *
     * @return medicalget04res
     */
    public Medicalget04res getMedicalget04res() {
        return medicalget04res;
    }

    /**
     * medicalget04res
     *
     * @param medicalget04res to set
     */
    public void setMedicalget04res(Medicalget04res medicalget04res) {
        this.medicalget04res = medicalget04res;
    }
}

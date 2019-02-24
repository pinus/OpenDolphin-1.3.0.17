package open.dolphin.orca.orcaapi.bean;

/**
 * patientlst6v2 専用 Response.
 * @author pns
 */
public class Response2 {

    /**
     * 全保険組合せ一覧取得. https://www.orca.med.or.jp/receipt/tec/api/insurancecombi.html
     */
    private Patientlst6res patientlst2res;

    /**
     * 全保険組合せ一覧取得. https://www.orca.med.or.jp/receipt/tec/api/insurancecombi.html
     * @return the patientlst2res
     */
    public Patientlst6res getPatientlst6res() {
        return patientlst2res;
    }

    /**
     * 全保険組合せ一覧取得. https://www.orca.med.or.jp/receipt/tec/api/insurancecombi.html
     * @param patientlst2res the patientlst2res to set
     */
    public void setPatientlst6res(Patientlst6res patientlst2res) {
        this.patientlst2res = patientlst2res;
    }
}

package open.dolphin.orca.orcaapi.bean;

/**
 * Care_Information. 介護情報
 *
 * @author pns
 */
public class CareInformation {
    /**
     * 介護保険情報（繰り返し　１０） (例:  )
     */
    private Insurance[] Insurance;

    /**
     * 介護認定情報（繰り返し　５０） (例:  )
     */
    private Certification[] Certification;

    /**
     * 地域包括診療対象疾病（繰り返し　４） (例:  )
     */
    private CommunityDisease[] Community_Disease;

    /**
     * 介護保険情報（繰り返し　１０） (例:  )
     *
     * @return the Insurance
     */
    public Insurance[] getInsurance() {
        return Insurance;
    }

    /**
     * 介護保険情報（繰り返し　１０） (例:  )
     *
     * @param Insurance the Insurance to set
     */
    public void setInsurance(Insurance[] Insurance) {
        this.Insurance = Insurance;
    }

    /**
     * 介護認定情報（繰り返し　５０） (例:  )
     *
     * @return the Certification
     */
    public Certification[] getCertification() {
        return Certification;
    }

    /**
     * 介護認定情報（繰り返し　５０） (例:  )
     *
     * @param Certification the Certification to set
     */
    public void setCertification(Certification[] Certification) {
        this.Certification = Certification;
    }

    /**
     * 地域包括診療対象疾病（繰り返し　４） (例:  )
     *
     * @return the Community_Disease
     */
    public CommunityDisease[] getCommunity_Disease() {
        return Community_Disease;
    }

    /**
     * 地域包括診療対象疾病（繰り返し　４） (例:  )
     *
     * @param Community_Disease the Community_Disease to set
     */
    public void setCommunity_Disease(CommunityDisease[] Community_Disease) {
        this.Community_Disease = Community_Disease;
    }
}

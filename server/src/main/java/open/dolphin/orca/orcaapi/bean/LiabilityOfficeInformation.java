package open.dolphin.orca.orcaapi.bean;

/**
 * Liability_Office_Information. 事業所情報
 *
 * @author pns
 */
public class LiabilityOfficeInformation {
    /**
     * 事業所名称 (例: オルカ建設)
     */
    private String L_WholeName;

    /**
     * 所在地都道府県情報 (例:  )
     */
    private PrefectureInformation Prefecture_Information;

    /**
     * 所在地郡市区情報 (例:  )
     */
    private CityInformation City_Information;

    /**
     * 事業所名称 (例: オルカ建設)
     *
     * @return the L_WholeName
     */
    public String getL_WholeName() {
        return L_WholeName;
    }

    /**
     * 事業所名称 (例: オルカ建設)
     *
     * @param L_WholeName the L_WholeName to set
     */
    public void setL_WholeName(String L_WholeName) {
        this.L_WholeName = L_WholeName;
    }

    /**
     * 所在地都道府県情報 (例:  )
     *
     * @return the Prefecture_Information
     */
    public PrefectureInformation getPrefecture_Information() {
        return Prefecture_Information;
    }

    /**
     * 所在地都道府県情報 (例:  )
     *
     * @param Prefecture_Information the Prefecture_Information to set
     */
    public void setPrefecture_Information(PrefectureInformation Prefecture_Information) {
        this.Prefecture_Information = Prefecture_Information;
    }

    /**
     * 所在地郡市区情報 (例:  )
     *
     * @return the City_Information
     */
    public CityInformation getCity_Information() {
        return City_Information;
    }

    /**
     * 所在地郡市区情報 (例:  )
     *
     * @param City_Information the City_Information to set
     */
    public void setCity_Information(CityInformation City_Information) {
        this.City_Information = City_Information;
    }
}

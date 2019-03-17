package open.dolphin.orca.orcaapi.bean;

/**
 * Community_Disease. 地域包括診療対象疾病（繰り返し　４）
 *
 * @author pns
 */
public class CommunityDisease {
    /**
     * 高血圧症、糖尿病、脂質異常症、認知症の順に内容を表示（True：対象病名である、False：対象でない） (例: False)
     */
    private String Target_Disease;

    /**
     * 高血圧症、糖尿病、脂質異常症、認知症の順に内容を表示（True：対象病名である、False：対象でない） (例: False)
     *
     * @return the Target_Disease
     */
    public String getTarget_Disease() {
        return Target_Disease;
    }

    /**
     * 高血圧症、糖尿病、脂質異常症、認知症の順に内容を表示（True：対象病名である、False：対象でない） (例: False)
     *
     * @param Target_Disease the Target_Disease to set
     */
    public void setTarget_Disease(String Target_Disease) {
        this.Target_Disease = Target_Disease;
    }
}


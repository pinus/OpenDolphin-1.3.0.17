package open.dolphin.orca.orcaapi.bean;

/**
 * Disease_Unmatch_Information. 不一致病名情報
 * @author pns
 */
public class DiseaseUnmatchInformation {
    /**
     * 不一致病名情報オーバーフラグ (例:  )
     */
    private String Disease_Unmatch_Information_Overflow;

    /**
     * 不一致病名一覧(繰り返し　５０) (例:  )
     */
    private DiseaseUnmatchInfo[] Disease_Unmatch_Info;

    /**
     * 不一致病名情報オーバーフラグ (例:  )
     * @return the Disease_Unmatch_Information_Overflow
     */
    public String getDisease_Unmatch_Information_Overflow() {
        return Disease_Unmatch_Information_Overflow;
    }

    /**
     * 不一致病名情報オーバーフラグ (例:  )
     * @param Disease_Unmatch_Information_Overflow the Disease_Unmatch_Information_Overflow to set
     */
    public void setDisease_Unmatch_Information_Overflow(String Disease_Unmatch_Information_Overflow) {
        this.Disease_Unmatch_Information_Overflow = Disease_Unmatch_Information_Overflow;
    }

    /**
     * 不一致病名一覧(繰り返し　５０) (例:  )
     * @return the Disease_Unmatch_Info
     */
    public DiseaseUnmatchInfo[] getDisease_Unmatch_Info() {
        return Disease_Unmatch_Info;
    }

    /**
     * 不一致病名一覧(繰り返し　５０) (例:  )
     * @param Disease_Unmatch_Info the Disease_Unmatch_Info to set
     */
    public void setDisease_Unmatch_Info(DiseaseUnmatchInfo[] Disease_Unmatch_Info) {
        this.Disease_Unmatch_Info = Disease_Unmatch_Info;
    }
}

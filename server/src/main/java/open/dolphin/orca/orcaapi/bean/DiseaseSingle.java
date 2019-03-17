package open.dolphin.orca.orcaapi.bean;

/**
 * Disease_Single. 単独病名情報(繰り返し6)
 *
 * @author pns
 */
public class DiseaseSingle {
    /**
     * 単独病名コード (例: 4309001)
     */
    private String Disease_Single_Code;

    /**
     * 単独病名 (例: くも膜下出血)
     */
    private String Disease_Single_Name;

    /**
     * 単独病名状態(空白:通常、2:削除、3:廃止(実施日時点での)) (例:  )
     */
    private String Disease_Single_Condition;

    /**
     * 単独病名コード (例: 4309001)
     *
     * @return the Disease_Single_Code
     */
    public String getDisease_Single_Code() {
        return Disease_Single_Code;
    }

    /**
     * 単独病名コード (例: 4309001)
     *
     * @param Disease_Single_Code the Disease_Single_Code to set
     */
    public void setDisease_Single_Code(String Disease_Single_Code) {
        this.Disease_Single_Code = Disease_Single_Code;
    }

    /**
     * 単独病名 (例: くも膜下出血)
     *
     * @return the Disease_Single_Name
     */
    public String getDisease_Single_Name() {
        return Disease_Single_Name;
    }

    /**
     * 単独病名 (例: くも膜下出血)
     *
     * @param Disease_Single_Name the Disease_Single_Name to set
     */
    public void setDisease_Single_Name(String Disease_Single_Name) {
        this.Disease_Single_Name = Disease_Single_Name;
    }

    /**
     * 単独病名状態(空白:通常、2:削除、3:廃止(実施日時点での)) (例:  )
     *
     * @return the Disease_Single_Condition
     */
    public String getDisease_Single_Condition() {
        return Disease_Single_Condition;
    }

    /**
     * 単独病名状態(空白:通常、2:削除、3:廃止(実施日時点での)) (例:  )
     *
     * @param Disease_Single_Condition the Disease_Single_Condition to set
     */
    public void setDisease_Single_Condition(String Disease_Single_Condition) {
        this.Disease_Single_Condition = Disease_Single_Condition;
    }
}
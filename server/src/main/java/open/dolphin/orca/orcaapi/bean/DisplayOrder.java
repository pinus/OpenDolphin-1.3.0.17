package open.dolphin.orca.orcaapi.bean;

/**
 * Display_Order. 並び順情報
 *
 * @author pns
 */
public class DisplayOrder {
    /**
     * 画面上の並び順の値 (例:  )
     */
    private String Display_Order_Number;

    /**
     * 並びを変更した場合は、「True」を設定 (例:  )
     */
    private String Display_Order_Mark;

    /**
     * Display_Order_Number
     *
     * @return Display_Order_Number
     */
    public String getDisplay_Order_Number() {
        return Display_Order_Number;
    }

    /**
     * Display_Order_Number
     *
     * @param Display_Order_Number to set
     */
    public void setDisplay_Order_Number(String Display_Order_Number) {
        this.Display_Order_Number = Display_Order_Number;
    }

    /**
     * Display_Order_Mark
     *
     * @return Display_Order_Mark
     */
    public String getDisplay_Order_Mark() {
        return Display_Order_Mark;
    }

    /**
     * Display_Order_Mark
     *
     * @param Display_Order_Mark to set
     */
    public void setDisplay_Order_Mark(String Display_Order_Mark) {
        this.Display_Order_Mark = Display_Order_Mark;
    }
}

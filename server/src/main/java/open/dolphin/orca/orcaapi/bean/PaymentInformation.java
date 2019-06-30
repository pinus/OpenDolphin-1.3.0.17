package open.dolphin.orca.orcaapi.bean;

/**
 * Payment_Information. 支払情報
 *
 * @author pns
 */
public class PaymentInformation {
    /**
     * 減免事由 (例: 01)
     */
    private String Reduction_Reason;

    /**
     * 割引率 (例: 01)
     */
    private String Discount;

    /**
     * 入金方法区分 (例: 02)
     */
    private String Ic_Code;

    /**
     * Reduction_Reason
     *
     * @return Reduction_Reason
     */
    public String getReduction_Reason() {
        return Reduction_Reason;
    }

    /**
     * Reduction_Reason
     *
     * @param Reduction_Reason to set
     */
    public void setReduction_Reason(String Reduction_Reason) {
        this.Reduction_Reason = Reduction_Reason;
    }

    /**
     * Discount
     *
     * @return Discount
     */
    public String getDiscount() {
        return Discount;
    }

    /**
     * Discount
     *
     * @param Discount to set
     */
    public void setDiscount(String Discount) {
        this.Discount = Discount;
    }

    /**
     * Ic_Code
     *
     * @return Ic_Code
     */
    public String getIc_Code() {
        return Ic_Code;
    }

    /**
     * Ic_Code
     *
     * @param Ic_Code to set
     */
    public void setIc_Code(String Ic_Code) {
        this.Ic_Code = Ic_Code;
    }
}
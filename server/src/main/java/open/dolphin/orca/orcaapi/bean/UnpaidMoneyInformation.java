package open.dolphin.orca.orcaapi.bean;

/**
 * Unpaid_Money_Information. 個別の未収金情報（繰り返し　５０）（診療日の新しい順）
 * @author pns
 */
public class UnpaidMoneyInformation {
    /**
     * 診療日 (例: )
     */
    private String Perform_Date;

    /**
     * 入外区分（1：入院、2：入院外） (例: )
     */
    private String InOut;

    /**
     * 伝票番号 (例: )
     */
    private String Invoice_Number;

    /**
     * 未収金額 (例: )
     */
    private String Unpaid_Money;

    /**
     * 診療日 (例: )
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療日 (例: )
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 入外区分（1：入院、2：入院外） (例: )
     * @return the InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * 入外区分（1：入院、2：入院外） (例: )
     * @param InOut the InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
    }

    /**
     * 伝票番号 (例: )
     * @return the Invoice_Number
     */
    public String getInvoice_Number() {
        return Invoice_Number;
    }

    /**
     * 伝票番号 (例: )
     * @param Invoice_Number the Invoice_Number to set
     */
    public void setInvoice_Number(String Invoice_Number) {
        this.Invoice_Number = Invoice_Number;
    }

    /**
     * 未収金額 (例: )
     * @return the Unpaid_Money
     */
    public String getUnpaid_Money() {
        return Unpaid_Money;
    }

    /**
     * 未収金額 (例: )
     * @param Unpaid_Money the Unpaid_Money to set
     */
    public void setUnpaid_Money(String Unpaid_Money) {
        this.Unpaid_Money = Unpaid_Money;
    }
}
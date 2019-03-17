package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Set04_Information. 診療行為請求確認設定
 *
 * @author pns
 */
public class MedicalSet04Information {
    /**
     * 入金の取り扱いシステム管理「1038診療行為機能情報」 (例: )
     */
    private String Ic_Request_Code;

    /**
     * 入金の取り扱い名称 (例: )
     */
    private String Ic_Request_Code_Name;

    /**
     * 入金・返金額設定システム管理「1038診療行為機能情報」 (例: )
     */
    private String Ic_Re_Code;

    /**
     * 入金・返金額設定名称 (例: )
     */
    private String Ic_Re_Code_Name;

    /**
     * 訂正時の請求書金額システム管理「1038診療行為機能情報」 (例: )
     */
    private String Modify_Invoice_Receipt_Code;

    /**
     * 訂正時の請求書金額名称 (例: )
     */
    private String Modify_Invoice_Receipt_Code_Name;

    /**
     * Ic_Request_Code
     *
     * @return Ic_Request_Code
     */
    public String getIc_Request_Code() {
        return Ic_Request_Code;
    }

    /**
     * Ic_Request_Code
     *
     * @param Ic_Request_Code to set
     */
    public void setIc_Request_Code(String Ic_Request_Code) {
        this.Ic_Request_Code = Ic_Request_Code;
    }

    /**
     * Ic_Request_Code_Name
     *
     * @return Ic_Request_Code_Name
     */
    public String getIc_Request_Code_Name() {
        return Ic_Request_Code_Name;
    }

    /**
     * Ic_Request_Code_Name
     *
     * @param Ic_Request_Code_Name to set
     */
    public void setIc_Request_Code_Name(String Ic_Request_Code_Name) {
        this.Ic_Request_Code_Name = Ic_Request_Code_Name;
    }

    /**
     * Ic_Re_Code
     *
     * @return Ic_Re_Code
     */
    public String getIc_Re_Code() {
        return Ic_Re_Code;
    }

    /**
     * Ic_Re_Code
     *
     * @param Ic_Re_Code to set
     */
    public void setIc_Re_Code(String Ic_Re_Code) {
        this.Ic_Re_Code = Ic_Re_Code;
    }

    /**
     * Ic_Re_Code_Name
     *
     * @return Ic_Re_Code_Name
     */
    public String getIc_Re_Code_Name() {
        return Ic_Re_Code_Name;
    }

    /**
     * Ic_Re_Code_Name
     *
     * @param Ic_Re_Code_Name to set
     */
    public void setIc_Re_Code_Name(String Ic_Re_Code_Name) {
        this.Ic_Re_Code_Name = Ic_Re_Code_Name;
    }

    /**
     * Modify_Invoice_Receipt_Code
     *
     * @return Modify_Invoice_Receipt_Code
     */
    public String getModify_Invoice_Receipt_Code() {
        return Modify_Invoice_Receipt_Code;
    }

    /**
     * Modify_Invoice_Receipt_Code
     *
     * @param Modify_Invoice_Receipt_Code to set
     */
    public void setModify_Invoice_Receipt_Code(String Modify_Invoice_Receipt_Code) {
        this.Modify_Invoice_Receipt_Code = Modify_Invoice_Receipt_Code;
    }

    /**
     * Modify_Invoice_Receipt_Code_Name
     *
     * @return Modify_Invoice_Receipt_Code_Name
     */
    public String getModify_Invoice_Receipt_Code_Name() {
        return Modify_Invoice_Receipt_Code_Name;
    }

    /**
     * Modify_Invoice_Receipt_Code_Name
     *
     * @param Modify_Invoice_Receipt_Code_Name to set
     */
    public void setModify_Invoice_Receipt_Code_Name(String Modify_Invoice_Receipt_Code_Name) {
        this.Modify_Invoice_Receipt_Code_Name = Modify_Invoice_Receipt_Code_Name;
    }
}

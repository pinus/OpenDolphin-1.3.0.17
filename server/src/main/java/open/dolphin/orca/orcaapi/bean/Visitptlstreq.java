package open.dolphin.orca.orcaapi.bean;

/**
 * visitptlstreq.
 *
 * @author pns
 */
public class Visitptlstreq {
    /**
     * リクエスト番号 (例: 01).
     * 01: 来院日の受診履歴取得, 02: 来院年月の受診履歴取得
     */
    private String Request_Number;

    /**
     * 来院日付 (例: 2003-01-14)
     */
    private String Visit_Date;

    /**
     * 診療科コード※１(01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * リクエスト番号 (例: 01)
     *
     * @return the Request_Number
     */
    public String getRequest_Number() {
        return Request_Number;
    }

    /**
     * リクエスト番号 (例: 01)
     *
     * @param Request_Number the Request_Number to set
     */
    public void setRequest_Number(String Request_Number) {
        this.Request_Number = Request_Number;
    }

    /**
     * 来院日付 (例: 2003-01-14)
     *
     * @return the Visit_Date
     */
    public String getVisit_Date() {
        return Visit_Date;
    }

    /**
     * 来院日付 (例: 2003-01-14)
     *
     * @param Visit_Date the Visit_Date to set
     */
    public void setVisit_Date(String Visit_Date) {
        this.Visit_Date = Visit_Date;
    }

    /**
     * 診療科コード※１(01:内科) (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード※１(01:内科) (例: 01)
     *
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }
}
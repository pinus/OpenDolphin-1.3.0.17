package open.dolphin.orca.orcaapi.bean;

/**
 * Perform_Day_Information. 薬剤・器材数量（繰り返し　３１）
 *
 * @author pns
 */
public class PerformDayInformation {
    /**
     * 算定日 (例: 18)
     */
    private String Perform_Day;

    /**
     * １日の合計数量 (例: 2)
     */
    private String Perform_Day_Number;

    /**
     * 算定日 (例: 18)
     *
     * @return the Perform_Day
     */
    public String getPerform_Day() {
        return Perform_Day;
    }

    /**
     * 算定日 (例: 18)
     *
     * @param Perform_Day the Perform_Day to set
     */
    public void setPerform_Day(String Perform_Day) {
        this.Perform_Day = Perform_Day;
    }

    /**
     * １日の合計数量 (例: 2)
     *
     * @return the Perform_Day_Number
     */
    public String getPerform_Day_Number() {
        return Perform_Day_Number;
    }

    /**
     * １日の合計数量 (例: 2)
     *
     * @param Perform_Day_Number the Perform_Day_Number to set
     */
    public void setPerform_Day_Number(String Perform_Day_Number) {
        this.Perform_Day_Number = Perform_Day_Number;
    }
}

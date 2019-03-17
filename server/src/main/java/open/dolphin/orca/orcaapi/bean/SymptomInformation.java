package open.dolphin.orca.orcaapi.bean;

/**
 * Symptom_Information. 症状詳記内容※4（繰り返し50）
 *
 * @author pns
 */
public class SymptomInformation {
    /**
     * 症状詳記区分 (例: )
     */
    private String Symptom_Code;

    /**
     * 症状 (例: )
     */
    private String Symptom_Content;

    /**
     * 作用機与 (例: )
     */
    private String Symptom_Detail;

    /**
     * Symptom_Code
     *
     * @return Symptom_Code
     */
    public String getSymptom_Code() {
        return Symptom_Code;
    }

    /**
     * Symptom_Code
     *
     * @param Symptom_Code to set
     */
    public void setSymptom_Code(String Symptom_Code) {
        this.Symptom_Code = Symptom_Code;
    }

    /**
     * Symptom_Content
     *
     * @return Symptom_Content
     */
    public String getSymptom_Content() {
        return Symptom_Content;
    }

    /**
     * Symptom_Content
     *
     * @param Symptom_Content to set
     */
    public void setSymptom_Content(String Symptom_Content) {
        this.Symptom_Content = Symptom_Content;
    }

    /**
     * Symptom_Detail
     *
     * @return Symptom_Detail
     */
    public String getSymptom_Detail() {
        return Symptom_Detail;
    }

    /**
     * Symptom_Detail
     *
     * @param Symptom_Detail to set
     */
    public void setSymptom_Detail(String Symptom_Detail) {
        this.Symptom_Detail = Symptom_Detail;
    }
}

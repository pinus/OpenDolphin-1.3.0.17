package open.dolphin.infomodel;

/**
 * AllergyModel.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class AllergyModel extends InfoModel implements Comparable<AllergyModel> {

    private long observationId;
    // 要因
    private String factor;
    // 反応程度
    private String severity;
    // コード体系
    private String severityTableId;
    // 同定日
    private String identifiedDate;
    // メモ
    private String memo;

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getIdentifiedDate() {
        return identifiedDate;
    }

    public void setIdentifiedDate(String identifiedDate) {
        this.identifiedDate = identifiedDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSeverityTableId() {
        return severityTableId;
    }

    public void setSeverityTableId(String severityTableId) {
        this.severityTableId = severityTableId;
    }

    public long getObservationId() {
        return observationId;
    }

    public void setObservationId(long observationId) {
        this.observationId = observationId;
    }

    /**
     * 同定日で比較する.
     *
     * @param other 比較対象オブジェクト
     * @return 比較値
     */
    @Override
    public int compareTo(AllergyModel other) {
        if (other != null) {
            String val1 = getIdentifiedDate();
            String val2 = other.getIdentifiedDate();
            return val1.compareTo(val2);
        }
        return 1;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof AllergyModel) && compareTo((AllergyModel) other) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.factor != null ? this.factor.hashCode() : 0);
        hash = 31 * hash + (this.identifiedDate != null ? this.identifiedDate.hashCode() : 0);
        return hash;
    }
}

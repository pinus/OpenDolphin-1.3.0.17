package open.dolphin.dto;

/**
 * SubjectivesSpec.
 * ORCA API の　subjectivesv2 に送る情報の DTO.
 */
public class SubjectivesSpec {
    /**
     * クラス番号.
     * 01:症状詳記登録, 02:症状詳記削除
     */
    private String requestNumber;

    /**
     * 患者番号.
     */
    private String patientId;

    /**
     * 診療年月 (yyyy-MM 型式).
     */
    private String performDate;

    /**
     * 診療科コード.
     */
    private String departmentCode;

    /**
     * 保険組合せ番号.
     */
    private String insuranceCombinationNumber;

    /**
     * 詳記区分.
     */
    private String code;

    /**
     * 症状詳記内容.
     */
    private String record;

    /**
     * requestNumber
     *
     * @return requestNumber
     */
    public String getRequestNumber() {
        return requestNumber;
    }

    /**
     * requestNumber
     *
     * @param requestNumber to set
     */
    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    /**
     * patientId
     *
     * @return patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * patientId
     *
     * @param patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * performDate
     *
     * @return performDate
     */
    public String getPerformDate() {
        return performDate;
    }

    /**
     * performDate
     *
     * @param performDate to set
     */
    public void setPerformDate(String performDate) {
        this.performDate = performDate;
    }

    /**
     * departmentCode
     *
     * @return departmentCode
     */
    public String getDepartmentCode() {
        return departmentCode;
    }

    /**
     * departmentCode
     *
     * @param departmentCode to set
     */
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    /**
     * insuranceCombinationNumber
     *
     * @return insuranceCombinationNumber
     */
    public String getInsuranceCombinationNumber() {
        return insuranceCombinationNumber;
    }

    /**
     * insuranceCombinationNumber
     *
     * @param insuranceCombinationNumber to set
     */
    public void setInsuranceCombinationNumber(String insuranceCombinationNumber) {
        this.insuranceCombinationNumber = insuranceCombinationNumber;
    }

    /**
     * code
     *
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * code
     *
     * @param code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * record
     *
     * @return record
     */
    public String getRecord() {
        return record;
    }

    /**
     * record
     *
     * @param record to set
     */
    public void setRecord(String record) {
        this.record = record;
    }
}

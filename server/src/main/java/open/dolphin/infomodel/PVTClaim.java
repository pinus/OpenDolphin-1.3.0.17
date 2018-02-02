package open.dolphin.infomodel;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Simple Claim　Class used for PVT.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 * Modified by Mirror-I corp for adding 'claimDeptName' and related function to store/get Department name
 */
public class PVTClaim extends InfoModel {
    private static final long serialVersionUID = -8573272136025043849L;

    private String claimStatus;
    private String claimRegistTime;
    private String claimAdmitFlag;
    private String claimDeptName;
    private String claimDeptCode;
    private String assignedDoctorId;
    private String assignedDoctorName;
    private List<String> claimAppName;
    private String claimAppMemo;
    private String claimItemCode;
    private String claimItemName;
    private String insuranceUid;
    private String jmariCode;

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String val) {
        claimStatus = val;
    }

    public String getClaimRegistTime() {
        return claimRegistTime;
    }

    public void setClaimRegistTime(String val) {
        claimRegistTime = val;
    }

    public String getClaimAdmitFlag() {
        return claimAdmitFlag;
    }

    public void setClaimAdmitFlag(String val) {
        claimAdmitFlag = val;
    }

    public String getClaimDeptName() {
        return claimDeptName;
    }

    public void setClaimDeptName(String val) {
        claimDeptName = val;
    }

    public String getClaimDeptCode() {
        return claimDeptCode;
    }

    public void setClaimDeptCode(String val) {
        claimDeptCode = val;
    }

    public List<String> getClaimAppName() {
        return claimAppName;
    }

    public void addClaimAppName(String val) {
        if (claimAppName == null) {
            claimAppName = new ArrayList<>();
        }
        claimAppName.add(val);
    }

    public String getClaimAppMemo() {
        return claimAppMemo;
    }

    public void setClaimAppMemo(String val) {
        claimAppMemo = val;
    }

    public String getClaimItemCode() {
        return claimItemCode;
    }

    public void setClaimItemCode(String val) {
        claimItemCode = val;
    }

    public String getClaimItemName() {
        return claimItemName;
    }

    public void setClaimItemName(String val) {
        claimItemName = val;
    }

    @Override
    public String toString() {

        StringJoiner sj = new StringJoiner("\n");

        if (claimStatus != null) { sj.add("ClaimStatus: " + claimStatus); }
        if (claimRegistTime != null) { sj.add("ClaimRegistTime: " + claimRegistTime); }
        if (claimAdmitFlag != null) { sj.add("ClaimAdmitFlag: " + claimAdmitFlag); }
        // Mirror-I start
        if (claimDeptName != null) { sj.add("ClaimDeptName: " + claimDeptName); }
        // Mirror-I end
        if (claimAppName != null) { claimAppName.forEach(n -> sj.add("ClaimAppName: " + n)); }
        if (claimAppMemo != null) { sj.add("ClaimAppointMemo: " + claimAppMemo); }
        if (claimItemCode != null) { sj.add("ClaimItemCode: " + claimItemCode); }
        if (claimItemName != null) { sj.add("ClaimItemName: " + claimItemName); }

        return sj.toString();
    }

    public void setInsuranceUid(String insuranceUid) {
        this.insuranceUid = insuranceUid;
    }

    public String getInsuranceUid() {
        return insuranceUid;
    }

    public String getJmariCode() {
        return jmariCode;
    }

    public void setJmariCode(String jmariCode) {
        this.jmariCode = jmariCode;
    }

    public String getAssignedDoctorName() {
        return assignedDoctorName;
    }

    public void setAssignedDoctorName(String assignedDoctorName) {
        this.assignedDoctorName = assignedDoctorName;
    }

    public String getAssignedDoctorId() {
        return assignedDoctorId;
    }

    public void setAssignedDoctorId(String assignedDoctorId) {
        this.assignedDoctorId = assignedDoctorId;
    }
}

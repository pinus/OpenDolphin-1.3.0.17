package open.dolphin.infomodel;

/**
 * ClaimBundle 要素クラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class ClaimBundle extends InfoModel {
    private static final long serialVersionUID = -7332175271243905003L;

    private String className;           // 診療行為名
    private String classCode;           // 診療行為コード
    private String classCodeSystem;     // コード体系
    private String admin;               // 用法
    private String adminCode;           // 用法コード
    private String adminCodeSystem;     // 用法コード体系
    private String adminMemo;           // 用法メモ
    private String bundleNumber;        // バンドル数
    private ClaimItem[] claimItem;      // バンドル構成品目
    private String memo;                // メモ

    public String getClassName() {
        return className;
    }

    public void setClassName(String val) {
        className = val;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String val) {
        classCode = val;
    }

    public String getClassCodeSystem() {
        return classCodeSystem;
    }

    public void setClassCodeSystem(String val) {
        classCodeSystem = val;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String val) {
        admin = val;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String val) {
        adminCode = val;
    }

    public String getAdminCodeSystem() {
        return adminCodeSystem;
    }

    public void setAdminCodeSystem(String val) {
        adminCodeSystem = val;
    }

    public String getAdminMemo() {
        return adminMemo;
    }

    public void setAdminMemo(String val) {
        adminMemo = val;
    }

    public String getBundleNumber() {
        return bundleNumber;
    }

    public void setBundleNumber(String val) {
        bundleNumber = val;
    }

    public ClaimItem[] getClaimItem() {
        return claimItem;
    }

    public void setClaimItem(ClaimItem[] val) {
        claimItem = val;
    }

    public void addClaimItem(ClaimItem val) {
        if (claimItem == null) {
            claimItem = new ClaimItem[1];
            claimItem[0] = val;
            return;
        }
        int len = claimItem.length;
        ClaimItem[] dest = new ClaimItem[len + 1];
        System.arraycopy(claimItem, 0, dest, 0, len);
        claimItem = dest;
        claimItem[len] = val;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String val) {
        memo = val;
    }
}

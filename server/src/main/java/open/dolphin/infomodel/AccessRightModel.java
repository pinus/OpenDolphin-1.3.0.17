package open.dolphin.infomodel;

/**
 * AccessRightModel.
 *
 * @author Kazushi Minagawa
 */
public class AccessRightModel extends InfoModel {
    private static final long serialVersionUID = -90888255738195101L;

    private String permission;
    private String startDate;
    private String endDate;
    private String licenseeCode;
    private String licenseeName;
    private String licenseeCodeType;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String val) {
        permission = val;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String val) {
        startDate = val;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String val) {
        endDate = val;
    }

    public String getLicenseeCode() {
        return licenseeCode;
    }

    public void setLicenseeCode(String licenseeCode) {
        this.licenseeCode = licenseeCode;
    }

    public String getLicenseeName() {
        return licenseeName;
    }

    public void setLicenseeName(String licenseeName) {
        this.licenseeName = licenseeName;
    }

    public String getLicenseeCodeType() {
        return licenseeCodeType;
    }

    public void setLicenseeCodeType(String licenseeCodeType) {
        this.licenseeCodeType = licenseeCodeType;
    }
}

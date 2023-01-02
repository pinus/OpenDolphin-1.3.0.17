package open.dolphin.infomodel;

/**
 * UserLiteModel.
 *
 * @author Minagawa, Kazushi
 */
public class UserLiteModel extends InfoModel {
    
    private String userId;
    private String commonName;
    private LicenseModel licenseModel;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String creatorId) {
        this.userId = creatorId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String name) {
        this.commonName = name;
    }

    public LicenseModel getLicenseModel() {
        return licenseModel;
    }

    public void setLicenseModel(LicenseModel licenseModel) {
        this.licenseModel = licenseModel;
    }
}

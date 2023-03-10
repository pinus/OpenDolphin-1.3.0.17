package open.dolphin.infomodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * UserModel.
 *
 * @author Minagawa, Kazushi
 */
@Entity
@Table(name = "d_users")
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
public class UserModel extends InfoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * composite businnes key
     */
    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    private String sirName;

    private String givenName;

    @Column(nullable = false)
    private String commonName;

    @Embedded
    private LicenseModel licenseModel;

    @Embedded
    private DepartmentModel departmentModel;

    @Column(nullable = false)
    private String memberType;

    private String memo;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date registeredDate;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    private FacilityModel facilityModel;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<RoleModel> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String idAsLocal() {
        int index = userId.indexOf(COMPOSITE_KEY_MAKER);
        return userId.substring(index + 1);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSirName() {
        return sirName;
    }

    public void setSirName(String sirName) {
        this.sirName = sirName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public LicenseModel getLicenseModel() {
        return licenseModel;
    }

    public void setLicenseModel(LicenseModel licenseValue) {
        this.licenseModel = licenseValue;
    }

    public FacilityModel getFacilityModel() {
        return facilityModel;
    }

    public void setFacilityModel(FacilityModel facility) {
        this.facilityModel = facility;
    }

    public DepartmentModel getDepartmentModel() {
        return departmentModel;
    }

    public void setDepartmentModel(DepartmentModel departmentValue) {
        this.departmentModel = departmentValue;
    }

    public Collection<RoleModel> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleModel> roles) {
        this.roles = roles;
    }

    public void addRole(RoleModel value) {

        if (roles == null) {
            roles = new ArrayList<>(1);
        }
        roles.add(value);
    }

    public UserLiteModel getLiteModel() {

        UserLiteModel model = new UserLiteModel();
        model.setUserId(getUserId());
        model.setCommonName(getCommonName());
        LicenseModel lm = new LicenseModel();
        lm.setLicense(getLicenseModel().getLicense());
        lm.setLicenseDesc(getLicenseModel().getLicenseDesc());
        lm.setLicenseCodeSys(getLicenseModel().getLicenseCodeSys());
        model.setLicenseModel(lm);
        return model;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UserModel other = (UserModel) obj;
        return (id == other.getId());
    }
}

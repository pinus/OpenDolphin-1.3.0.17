package open.dolphin.infomodel;

import javax.persistence.*;
import java.util.Date;

/**
 * FacilityModel.
 *
 * @author Minagawa,Kazushi
 */
@Entity
@Table(name = "d_facility")
public class FacilityModel extends InfoModel {
    private static final long serialVersionUID = 3142760011378628588L;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable=false, unique=true)
    private String facilityId;

    @Column(nullable=false)
    private String facilityName;

    @Column(nullable=false)
    private String zipCode;

    @Column(nullable=false)
    private String address;

    @Column(nullable=false)
    private String telephone;

    private String url;

    @Column(nullable=false)
    @Temporal(value = TemporalType.DATE)
    private Date registeredDate;

    @Column(nullable= false)
    private String memberType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityName(String name) {
        this.facilityName = name;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMemberType() {
        return memberType;
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
        final FacilityModel other = (FacilityModel) obj;
        return (id == other.getId());
    }
}

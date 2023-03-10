package open.dolphin.infomodel;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.*;

/**
 * StampModel.
 * Field 'model' contains any of BundleMed, BundleDolphin, RegisteredDiagnosisModel, or TextStampModel
 *
 * @author Minagawa, Kazushi
 */
@Entity
@Table(name = "d_stamp")
public class StampModel extends InfoModel {
    
    @Id
    private String id;

    // UserPK
    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private String entity;

    @Column(nullable = false)
    @Lob
    private byte[] stampBytes;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
        @JsonSubTypes.Type(BundleMed.class),
        @JsonSubTypes.Type(BundleDolphin.class),
        @JsonSubTypes.Type(TextStampModel.class),
        @JsonSubTypes.Type(RegisteredDiagnosisModel.class)
    })
    @Transient
    private IInfoModel stamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public byte[] getStampBytes() {
        return stampBytes;
    }

    public void setStampBytes(byte[] stampBytes) {
        this.stampBytes = stampBytes;
    }

    public IInfoModel getStamp() {
        return stamp;
    }

    public void setStamp(IInfoModel stamp) { this.stamp = stamp; }

    @Override
    public int hashCode() {
        return (id == null) ? "".hashCode() : id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StampModel other = (StampModel) obj;
        return id.equals(other.getId());
    }
}

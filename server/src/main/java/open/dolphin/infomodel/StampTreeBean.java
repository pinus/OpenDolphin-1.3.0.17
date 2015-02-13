package open.dolphin.infomodel;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * PersonalTreeModel と PublishedStampTreeModel の共通親.
 * @author pns
 */
@MappedSuperclass
public abstract class StampTreeBean extends InfoModel {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserModel user;

    // TreeSetの名称
    @Column(nullable=false)
    private String name;

    // OID or Public
    // OID の時は施設用
    @Column(nullable=false)
    private String publishType;

    // Treeのカテゴリ
    @Column(nullable=false)
    private String category;

    // 団体名等
    @Column(nullable=false)
    private String partyName;

    // URL
    @Column(nullable=false)
    private String url;

    // 説明
    @Column(nullable=false)
    private String description;

    // 公開した日
    @Column(nullable=false)
    @Temporal(value = TemporalType.DATE)
    private Date publishedDate;

    @Transient
    private String treeXml;

    @Column(nullable=false)
    @Lob
    private byte[] treeBytes;

    // 更新した日
    @Column(nullable=false)
    @Temporal(value = TemporalType.DATE)
    private Date lastUpdated;

    // PersonalTreeModel, PublishedTreeModel のそれぞれで実装する
    public abstract void setId(long id);
    public abstract long getId();

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishType() {
        return publishType;
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public byte[] getTreeBytes() {
        return treeBytes;
    }

    public void setTreeBytes(byte[] treeBytes) {
        this.treeBytes = treeBytes;
    }

    public String getTreeXml() {
        return treeXml;
    }

    public void setTreeXml(String treeXml) {
        this.treeXml = treeXml;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date updatedDate) {
        this.lastUpdated = updatedDate;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + (int) (getId() ^ (getId() >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final StampTreeBean other = (StampTreeBean) obj;
        return (getId() == other.getId());
    }
}

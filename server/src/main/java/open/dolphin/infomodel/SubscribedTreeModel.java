package open.dolphin.infomodel;

import jakarta.persistence.*;

/**
 * StampTreeXML のホルダクラス.
 * ユーザがインポートしているTreeクラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Entity
@Table(name = "d_subscribed_tree")
public class SubscribedTreeModel extends InfoModel {
    private static final long serialVersionUID = -881452344593635871L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(nullable = false)
    private long treeId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTreeId() {
        return treeId;
    }

    public void setTreeId(long treeId) {
        this.treeId = treeId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
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
        final SubscribedTreeModel other = (SubscribedTreeModel) obj;
        return (id == other.getId());
    }
}

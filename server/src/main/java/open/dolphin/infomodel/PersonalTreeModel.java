package open.dolphin.infomodel;

import jakarta.persistence.*;

/**
 * PersonalTreeModel.
 * User のパーソナルツリークラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Entity
@Table(name = "d_stamp_tree")
public class PersonalTreeModel extends StampTreeBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // 公開しているtreeのエンティティ
    private String published;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }
}

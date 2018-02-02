package open.dolphin.infomodel;

import javax.persistence.*;

/**
 * PublishedTreeModel.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Entity
@Table(name="d_published_tree")
public class PublishedTreeModel extends StampTreeBean {
    private static final long serialVersionUID = -1402248987372246092L;

    // この id は generate された値ではなく，
    //コピー元の PersonalTreeModel と同じ Id に設定される
    @Id
    private long id;

    @Transient
    private boolean imported;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }
}

package open.dolphin.stampbox;

import java.awt.datatransfer.Transferable;
import java.util.EventObject;

/**
 * @author pns
 */
public class StampTreeMenuEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    private String entity;
    private Transferable tr;

    public StampTreeMenuEvent(Object source) {
        super(source);
    }

    /**
     * @return the entity
     */
    public String getEntity() {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * @return the transferable
     */
    public Transferable getTransferable() {
        return tr;
    }

    /**
     * @param tr the transferable to set
     */
    public void setTransferable(Transferable tr) {
        this.tr = tr;
    }
}

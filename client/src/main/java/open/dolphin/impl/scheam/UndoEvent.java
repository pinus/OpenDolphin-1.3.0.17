package open.dolphin.impl.scheam;

/**
 * UndoEvent.
 *
 * @author pns
 */
public interface UndoEvent {
    /**
     * 元に戻して redraw する.
     */
    public void rollback();
}

package open.dolphin.client;

import javax.swing.text.Position;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ComponentHolder.
 * StampHolder と SchemaHolder （いずれも JLabel）
 *
 * @param <T> KarteComposite の Component
 * @author Kauzshi Minagawa
 */
public interface ComponentHolder<T> extends PropertyChangeListener, KarteComposite<T> {
    ContentType getContentType();
    KartePane getKartePane();
    boolean isSelected();
    void setSelected(boolean b);
    void edit();
    @Override
    void propertyChange(PropertyChangeEvent e);
    void setEntry(Position start, Position end);
    int getStartPos();
    int getEndPos();
    enum ContentType {TT_STAMP, TT_IMAGE}
}

package open.dolphin.client;

import javax.swing.text.Position;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ComponentHolder.
 * StampHolder と SchemHolder （いずれも JLabel）
 *
 * @param <T>
 * @author Kauzshi Minagawa
 */
public interface ComponentHolder<T> extends PropertyChangeListener, KarteComposite<T> {

    public ContentType getContentType();

    public KartePane getKartePane();

    public boolean isSelected();

    public void setSelected(boolean b);

    public void edit();

    @Override
    public void propertyChange(PropertyChangeEvent e);

    public void setEntry(Position start, Position end);

    public int getStartPos();

    public int getEndPos();

    public static enum ContentType {TT_STAMP, TT_IMAGE}

}

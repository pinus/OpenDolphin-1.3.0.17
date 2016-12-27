package open.dolphin.order;

import java.beans.PropertyChangeListener;

/**
 * Stamp Model Editor が実装するインターフェイス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 * @param <T>
 **/
public interface IStampEditor <T> {

    public void start();

    public void enter();

    public String getTitle();

    public void setEntity(String entity);

    public void setTitle(String val);

    public T getValue();

    public void setValue(T o);

    public void addPropertyChangeListener(String prop, PropertyChangeListener l);

    public void removePropertyChangeListener(String prop, PropertyChangeListener l);

    public boolean isValidModel();

    public void setValidModel(boolean b);

    public void dispose();
}

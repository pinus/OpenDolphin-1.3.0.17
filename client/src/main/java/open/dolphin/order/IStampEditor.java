package open.dolphin.order;

import java.beans.PropertyChangeListener;

/**
 * Stamp Model Editor が実装するインターフェイス.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 **/
public interface IStampEditor {

    public void start();

    public void enter();

    public String getTitle();

    public void setEntity(String entity);

    public void setTitle(String val);

    public Object getValue();

    public void setValue(Object o);

    public void addPropertyChangeListener(String prop, PropertyChangeListener l);

    public void removePropertyChangeListener(String prop, PropertyChangeListener l);

    public boolean isValidModel();

    public void setValidModel(boolean b);

    public void dispose();
}

package open.dolphin.client;

import javax.swing.ActionMap;

/**
 * カルテの構成要素.
 * CompositArea, KartePane, SchemaHolder, StampHolder
 * @author kazm
 * @param <T>
 */
public interface KarteComposite<T> {

    public void enter(ActionMap map);

    public void exit(ActionMap map);

    public T getComponent();

}

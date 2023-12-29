package open.dolphin.client;

import javax.swing.*;

/**
 * カルテの構成要素.
 * CompositeArea, KartePane, SchemaHolder, StampHolder
 *
 * @param <T> Component
 * @author kazm
 */
public interface KarteComposite<T> {
    /**
     * フォーカスを取ったときに ChartMediator から呼ばれる.
     * 必要に応じて Action を enable/disable する.
     *
     * @param map action map
     */
    void enter(ActionMap map);

    /**
     * フォーカスを失うときに ChartMediator から呼ばれる.
     * 必要に応じて Action を enable/disable する.
     *
     * @param map action map
     */
    void exit(ActionMap map);

    /**
     * KarteComposite の実務をしている JComponent を返す
     *
     * @return JComponent
     */
    T getComponent();
}

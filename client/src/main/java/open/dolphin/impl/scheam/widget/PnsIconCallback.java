package open.dolphin.impl.scheam.widget;

/**
 * widget に選択項目から icon を callback するための interface
 * @author pns
 * @param <K> 選択項目
 * @param <N> アイコン
 */
public interface PnsIconCallback<K, N>  {
    /**
     * 通常のアイコン
     * @param item
     * @return
     */
    public N call(K item);
    /**
     * 選択時のアイコン
     * @param item
     * @return
     */
    public N callSelected(K item);
}

package open.dolphin.order;

import open.dolphin.event.ValidListener;

/**
 * StampEditor が実装するインターフェイス.
 *
 * @param <T>
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 **/
public interface IStampEditor<T> {

    public void start();

    public void enter();

    /**
     * ダイアログのタイトルを返す.
     *
     * @return
     */
    public String getTitle();

    /**
     * ダイアログのタイトルトルをセットする.
     *
     * @param val
     */
    public void setTitle(String val);

    public void setEntity(String entity);

    public T getValue();

    public void setValue(T o);

    /**
     * 編集中のデータが valid かどうかをリスンするリスナ.
     * これによりカルテに展開するボタンの制御を行う.
     *
     * @param listener
     */
    public void addValidListener(ValidListener listener);

    public boolean isValidModel();

    public void setValid(boolean b);

    public void dispose();
}

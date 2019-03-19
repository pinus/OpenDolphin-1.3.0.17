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

    void start();

    void enter();

    /**
     * ダイアログのタイトルを返す.
     *
     * @return タイトル
     */
    String getTitle();

    /**
     * ダイアログのタイトルトルをセットする.
     *
     * @param title タイトル
     */
    void setTitle(String title);

    void setEntity(String entity);

    T getValue();

    void setValue(T o);

    /**
     * 編集中のデータが valid かどうかをリスンするリスナ.
     * これによりカルテに展開するボタンの制御を行う.
     *
     * @param listener ValidListener
     */
    void addValidListener(ValidListener listener);

    boolean isValidModel();

    void setValid(boolean b);

    void dispose();
}

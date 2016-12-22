package open.dolphin.impl.scheam.widget;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 * Customized ComboBox.
 * <ul>
 * <li> 選択項目にチェックマークをつける：css で list-cell に graphic を付ける. </li>
 * <li> ボタンクリックしたときに選択項目がボタン上の位置に一致するようにする：
 *      createDefaultSkin をフックして，カスタム ComboBoxListViewSkin を作り，
 *      その中で PopupWindow の位置を調節している. </li>
 * <li> SelectionModel の selectedItemProperty, selectedIndexProperty は ReadOnly で
 *      BindBidrectional ができないので BindBidrectional できる Property を提供する. </li>
 * </ul>
 * @author pns
 * @param <T>
 */
public class PnsComboBox<T> extends ComboBox<T> {
    private final ObjectProperty<Integer> selectedIndexProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<T> selectedItemProperty = new SimpleObjectProperty<>();

    public PnsComboBox() {
        // スクロールバーを出さないように多めに
        setVisibleRowCount(50);

        // selction が変更されたら Property を変更する
        getSelectionModel().selectedIndexProperty().addListener((ov, oldValue, newValue) -> {
            selectedIndexProperty.set((Integer) newValue);
        });
        getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            selectedItemProperty.set(newValue);
        });

        // Property が変更されたら select する
        selectedIndexProperty.addListener((ov, oldValue, newValue) -> {
            getSelectionModel().select(newValue);
        });
        selectedItemProperty.addListener((ov, oldValue, newValue) -> {
            getSelectionModel().select(newValue);
        });

        // Default CellFactory
        Callback<ListView<T>, ListCell<T>> cellFactory = v -> new PnsListCell<>();
        setCellFactory(cellFactory);
    }

    /**
     * BindBidrectional 可能な selectionIndexProperty
     * @return
     */
    public ObjectProperty<Integer> selectedIndexProperty() { return selectedIndexProperty; }

    /**
     * BindBidrectional 可能な selectionItemProperty
     * @return
     */
    public ObjectProperty<T> selectedItemProperty() { return selectedItemProperty; }

    /**
     * PopupWindow のインスタンスに操作を加えるために，ここを hook する
     * @return
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        Skin<?> skin = new ModifiedComboBoxListViewSkin<>(this);
        return skin;
    }

    /**
     * PopupWindow を出す位置を調整する機能を持った ComboBoxListViewSkin
     * @param <S>
     */
    private class ModifiedComboBoxListViewSkin<S> extends ComboBoxListViewSkin<S> {
        private final ComboBox<S> comboBox;

        public ModifiedComboBoxListViewSkin(ComboBox<S> comboBox) {
            super(comboBox);
            this.comboBox = comboBox;
            // 最初に開いた popup window が正しく layout されない workaround
            show();
            hide();
        }

        @Override
        public void show() {

            super.show();

            /*
             * popup を出すときに，選択項目と ComboBox Button Cell の位置を一致させるための操作.
             * getPrefPopupPosition() を Override できれば良いのだが，残念ながら private.
             * そこで，show() を Hook して表示位置を調節ずらす.
             */
            final PopupControl _popup = getPopup();
            final ListView<S> listView = getListView();
            double itemCount = (double) listView.getItems().size();
            double listViewHeight = listView.getHeight();
            double cellHeight = listViewHeight / itemCount;
            int selectedIndex = comboBox.getSelectionModel().getSelectedIndex();

            _popup.setY(_popup.getY() - (selectedIndex + 1) * cellHeight);
        }
    }
}

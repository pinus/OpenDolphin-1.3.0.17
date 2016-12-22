package open.dolphin.impl.scheam.widget;

import java.util.HashMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

/**
 * ToggleButton をセットにしたもの.
 * ToggleGroup を設定して，セットの中の１つのボタンのみ選択できる.
 * <ul>
 * <li>BindBidirectional できる selectedItemProperty を持つ.</li>
 * <li>removeFromGroup(item) で ToggleGroup から外すこともできる.
 *     以下のようにして外した ToggleButton を Button のように扱うことができる.
 *     <pre>
 *     toggleSet.removeFromGroup(item);
 *     toggleSet.getButton(item).setOnMouseClicked(e -> {
 *         -- process --
 *         ((ToggleButton)e.getSource()).setSelected(false);
 *     });
 *     </pre></li>
 * <li>アイコンはデフォルトでは Label. css で操作できるように setStyleClass("button-cell")してある.
 *     setCellFactory(Callback) でカスタマイズすることもできる.</li>
 * </ul>
 *
 * @author pns
 * @param <T>
 */
public class PnsToggleSet<T> extends PnsFlowPane {
    /** ToggleButton の ToggleGroup */
    private final ToggleGroup group = new ToggleGroup();
    /** (T)item を入れる配列 */
    private final ObservableList<T> items = FXCollections.observableArrayList();
    /** BindBidirectinal 可能な selectedIndexProperty */
    private final ObjectProperty<Integer> selectedIndexProperty = new SimpleObjectProperty<>();
    /** BindBidirectinal 可能な selectedItemProperty */
    private final ObjectProperty<T> selectedItemProperty = new SimpleObjectProperty<>();
    /** 使用する SingleSelectionModel. 変更不可 */
    private final SingleSelectionModel<T> selectionModel = new ToggleSetSelectionModel();
    /** (T)item をキーとして対応する Toggle を入れた Map */
    private final HashMap<T, ToggleButton> toggleMap = new HashMap<>();
    /** Toggle から item を逆引きする Map */
    private final HashMap<ToggleButton, T> itemMap = new HashMap<>();
    /** button cell のアイコンを作る cell factory */
    private Callback<T, Node> cellFactory = new DefaultCellFactory();

    public PnsToggleSet() {
        getStyleClass().add("pns-toggle-set");

        // items 追加時の処理
        items.addListener((ListChangeListener.Change<? extends T> c) -> {
            while(c.next()) {
                // 加わった item から Toggle を作成
                c.getAddedSubList().forEach(item -> {
                    ToggleButton toggle = new ToggleButton();
                    toggle.setToggleGroup(group);
                    toggle.setFocusTraversable(false);
                    toggle.setOnAction(t -> selectionModel.select(item));
                    toggle.setGraphic(cellFactory.call(item));
                    // Map に登録
                    toggleMap.put(item, toggle);
                    itemMap.put(toggle, item);
                });
            }

            // items をもとに ToggleButton を children に登録
            getChildren().clear();
            items.forEach(item -> getChildren().add(toggleMap.get(item)));
        });

        // SelctionModel の selection が変更されたら，このクラスの Property を変更する
        selectionModel.selectedIndexProperty().addListener((ov, oldIndex, newIndex) -> {
            selectedIndexProperty.set((Integer) newIndex);
        });
        selectionModel.selectedItemProperty().addListener((ov, oldItem, newItem) -> {
            selectedItemProperty.set(newItem);
            // ToggleButton も選択する
            toggleMap.get(newItem).setSelected(true);
        });

        // このクラスの Property が変更されたら SelectionModel を select する
        selectedIndexProperty.addListener((ov, oldIndex, newIndex) -> {
            selectionModel.select(newIndex);
        });
        selectedItemProperty.addListener((ov, oldItem, newItem) -> {
            selectionModel.select(newItem);
        });

        // ToggleGroup の選択が変わったら SelectionModel の selection 変更
        group.selectedToggleProperty().addListener((ov, oldValue, newValue) -> {
            ToggleButton newToggle = (ToggleButton) newValue;
            ToggleButton oldToggle = (ToggleButton) oldValue;

            if (newToggle == null) {
                // 選択された Toggle をもう一回押すとオフになって newToggle == null でここに入ってくる
                oldToggle.setSelected(true);
            } else {
                selectionModel.select(itemMap.get(newToggle));
            }
            // update button icon
            PnsToggleSet.this.updateCell(oldToggle);
            PnsToggleSet.this.updateCell(newToggle);
       });
    }

    /**
     * 保持する配列データ.
     * @return
     */
    public ObservableList<T> getItems() { return items; }

    /**
     * 固定 width を設定するヘルパーメソッド.
     * @param w
     */
    private void setFixedWidth(ToggleButton b, double w) {
        b.setPrefWidth(w); b.setMaxWidth(w); b.setMinWidth(w);
    }

    /**
     * ボタンの高さを固定する.
     * @param h
     */
    public void setAllButtonHeight(double h) {
        items.forEach(item -> {
            ToggleButton b = toggleMap.get(item);
            b.setPrefHeight(h); b.setMinHeight(h); b.setMaxHeight(h);
        });
    }

    /**
     * 全ての ToggleButton の幅を一定値に指定する.
     * @param width
     */
    public void setAllButtonWidth(double width) {
        items.forEach(item -> setButtonWidth(item, width));
    }

    /**
     * item の項目の ToggleButton の幅を指定する.
     * @param item
     * @param width
     */
    public void setButtonWidth(T item, double width) { setFixedWidth(toggleMap.get(item), width); }

    /**
     * item に対応するボタンを返す.
     * @param item
     * @return
     */
    public ToggleButton getButton(T item) { return toggleMap.get(item); }

    /**
     * item に対応するボタンを ToggleGroup から外す.
     * @param item
     */
    public void removeFromGroup(T item) {
        ToggleButton button = toggleMap.get(item);
        button.setToggleGroup(null);
        button.setOnAction(null);
    }

    /**
     * 外部から cell factory を登録する.
     * @param callback
     */
    public void setCellFactory(Callback<T, Node> callback) { cellFactory = callback; }

    /**
     * ToggleButton のアイコンを更新する.
     * @param toggle
     */
    public final void updateCell(ToggleButton toggle) {
        if (toggle != null) {
            T item = itemMap.get(toggle);
            toggle.setGraphic(cellFactory.call(item));
        }
    }

    /**
     * ToggleButton のアイコンを更新する.
     * @param item
     */
    public void updateCell(T item) {
        ToggleButton toggle = toggleMap.get(item);
        toggle.setGraphic(cellFactory.call(item));
    }

    /**
     * 指定した Toggle を disable/enable する.
     * @param item
     * @param disabled
     */
    public void setDisable(T item, boolean disabled) { toggleMap.get(item).setDisable(disabled); }

    /**
     * 指定した Toggle に Tooltip を加える.
     * @param item
     * @param text
     */
    public void setTooltip(T item, String text) { toggleMap.get(item).setTooltip(new Tooltip(text)); }

    /**
     * BindBidirectional 可能な selectedIndexProperty.
     * @return
     */
    public ObjectProperty<Integer> selectedIndexProperty() { return selectedIndexProperty; }

    /**
     * BindBidirectional 可能な selectedItemProperty.
     * @return
     */
    public ObjectProperty<T> selectedItemProperty() { return selectedItemProperty; }

    /**
     * SelectionModel を返す.
     * SelectionModel は変更不可.
     * @return
     */
    public SingleSelectionModel<T> getSelectionModel() { return selectionModel; }

    /**
     * カスタム SingleSelectionModel.
     */
    private class ToggleSetSelectionModel extends SingleSelectionModel<T> {

        @Override
        protected T getModelItem(int index) { return items.get(index); }

        @Override
        protected int getItemCount() { return items.size(); }

    }

    /**
     * button cell を表示する default の cell factory.
     */
    private class DefaultCellFactory implements Callback<T, Node> {
        @Override
        public Node call(T item) {
            Label l = new Label(item.toString());
            l.getStyleClass().add("button-cell");
            return l;
        }
    }
}

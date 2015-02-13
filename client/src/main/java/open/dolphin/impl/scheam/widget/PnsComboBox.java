package open.dolphin.impl.scheam.widget;

import java.util.HashMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.stage.Popup;
import open.dolphin.impl.scheam.constant.Const;
import open.dolphin.impl.scheam.constant.StyleClass;
import open.dolphin.impl.scheam.helper.SchemaUtils;
import open.dolphin.impl.scheam.helper.ShapeIcon;

/**
 * Mac OS 風の ComboBox
 * ボタンを押すと Popup が現れる，項目のクラスは <T> で指定，View は ToggleButton(ViewToggle) を使っている
 * 項目と対応する ToggleButton の対応は，項目をキーとした HashMap なので，同じ項目があったらだめ
 * Toggle に表示されるアイコンは PnsIconCallback で指定する
 * @author pns
 * @param <T>
 */
public class PnsComboBox<T> extends Button {
    /** ボタン右側の三角マーク */
    private static final double ARROW_WIDTH = 12;
    private final Shape arrow = ShapeIcon.getComboBoxArrow();
    /** 選択を１つにするための group */
    private final ToggleGroup group;
    /** (T)item を追加したかどうか等のプロパティー */
    private final ObjectProperty<ObservableList<T>> itemsProperty;
    /** 選択される (T)item を入れるリスト */
    private final ObservableList<T> items;
    /** どの (T)item が選択されているか */
    private final ObjectProperty<T> selectionProperty;
    /** (T)item をキーとして対応するリストの Toggle を入れる */
    private final HashMap<T, ListCellToggle> toggleMap;
    /** ボタンクリックで出る popup */
    private final ViewPopup popup;
    /** (T)item から icon 用 graphic への callback */
    private PnsIconCallback<T, Node> iconCallback;

    public PnsComboBox() {
        getStyleClass().add(StyleClass.PNS_COMBO_BOX);

        group = new ToggleGroup();
        itemsProperty = new SimpleObjectProperty<>();
        selectionProperty = new SimpleObjectProperty<>();
        toggleMap = new HashMap<>();
        items = FXCollections.observableArrayList();
        itemsProperty.set(items);
        popup = new ViewPopup();
        iconCallback = new DefaultCallback();

        // ボタンに表示するラベル
        final Label buttonLabel = new Label();
        buttonLabel.setAlignment(Pos.BASELINE_LEFT);
        buttonLabel.setMaxWidth(Double.MAX_VALUE);

        // ボタン右側の三角矢印
        final Label arrowLabel = new Label();
        arrowLabel.setAlignment(Pos.CENTER_RIGHT);
        arrowLabel.setPrefWidth(ARROW_WIDTH); arrowLabel.setMaxWidth(ARROW_WIDTH); arrowLabel.setMinWidth(ARROW_WIDTH);
        arrowLabel.setGraphic(arrow);

        // ラベル表示する labelPane ：　左側にラベル，右端に arrow
        HBox labelPane = new HBox();
        HBox.setHgrow(buttonLabel, Priority.ALWAYS);
        HBox.setHgrow(arrowLabel, Priority.NEVER);
        labelPane.setAlignment(Pos.CENTER_LEFT);
        labelPane.getChildren().addAll(buttonLabel, arrowLabel);
        setGraphic(labelPane);

        // bind
        // ボタンが押されたときのアクション 〜　フックできるように公開 showPopup を呼ぶ
        setOnMouseReleased(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                showPopup(t);
            }
        });

        // item が設定されたときに，対応する toggle を使って登録
        items.addListener(new ListChangeListener<T>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> change) {
                while(change.next()) {
                    for (final T added : change.getAddedSubList()) {
                        ListCellToggle toggle = new ListCellToggle(added);
                        toggle.setPrefWidth(getPrefWidth());
                        toggle.setPrefHeight(getPrefHeight()-4);
                        toggle.setToggleGroup(group);
                        toggle.setFocusTraversable(false);

                        toggle.setOnAction(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent t) {
                                selectionProperty.set(added);
                            }
                        });
                        toggle.setIcon(iconCallback.call(added));
                        toggleMap.put(added, toggle);
                        popup.add(toggle);
                    }
                }
            }
        });
        // toggle が押された場合の処理
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                // 選択された toggle を再選択した場合の処理
                if (t1 == null) { t.setSelected(true); }
                popup.hide();
            }
        });
        // 選択が変化した場合の処理
        selectionProperty.addListener(new ChangeListener<T>(){
            @Override
            public void changed(ObservableValue<? extends T> ov, T t, T t1) {
                group.selectToggle(toggleMap.get(t1));
                buttonLabel.setGraphic(iconCallback.call(t1));
            }
        });
    }

    public ObservableList<T> getItems() { return itemsProperty.get(); }
    public ObjectProperty<T> selectionProperty() { return selectionProperty; }
    public void setSelection(T selection) { selectionProperty.set(selection); }
    public T getSelection() { return selectionProperty.get(); }
    public int getSelectionIndex() { return items.indexOf(getSelection()); }

    /**
     * アイコン画像への Callback を外部からセットする
     * @param callback */
    public void setIconCallback(PnsIconCallback<T, Node> callback) {
        iconCallback = callback;
        for (T item : items) {
            toggleMap.get(item).setIcon(iconCallback.call(item));
        }
    }

    /**
     * アイコン表示のためのデフォルト Callback
     */
    private class DefaultCallback implements PnsIconCallback<T, Node> {
        // 通常のアイコン
        @Override
        public Node call(T item) {
            Label l = new Label(item.toString());
            l.setTextFill(Const.PNS_BLACK);
            return l;
        }
        // mouse hover 時のアイコン
        @Override
        public Node callSelected(T item) {
            Label l = new Label(item.toString());
            l.setTextFill(Const.PNS_WHITE);
            return l;
        }
    }

    /**
     * Popup を表示する　〜　フック用に公開
     * @param e
     */
    public void showPopup(MouseEvent e) {
        popup.show(this);
    }

    /**
     * 選択をチェックマークで表示する ToggleButton
     */
    private class ListCellToggle extends ToggleButton {
        private final double checkMarkWidth = 12;
        private final Shape checkMark;
        private final Label checkMarkLabel;
        private final Label itemLabel;
        /** この toggle が保持する item */
        private final T item;

        public ListCellToggle(T t) {
            getStyleClass().add(StyleClass.PNS_COMBO_BOX_LIST_CELL);
            item = t;
            checkMark = ShapeIcon.getCheckMark();
            checkMark.setFill(Const.PNS_BLACK);
            checkMarkLabel = new Label();
            checkMarkLabel.setAlignment(Pos.CENTER_LEFT);
            checkMarkLabel.setPrefWidth(checkMarkWidth); checkMarkLabel.setMaxWidth(checkMarkWidth); checkMarkLabel.setMinWidth(checkMarkWidth);
            itemLabel = new Label();
            itemLabel.setAlignment(Pos.CENTER_LEFT);
            itemLabel.setMaxWidth(Double.MAX_VALUE);
            HBox labelPane = new HBox();
            HBox.setHgrow(checkMarkLabel, Priority.NEVER);
            HBox.setHgrow(itemLabel, Priority.ALWAYS);
            labelPane.setAlignment(Pos.CENTER_LEFT);
            labelPane.getChildren().addAll(checkMarkLabel, itemLabel);
            setGraphic(labelPane);
            // 選択されたときに check mark をつける
            selectedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    if (t1) {
                        checkMarkLabel.setGraphic(checkMark);
                    } else {
                        checkMarkLabel.setGraphic(null);
                    }
                }
            });
            // mouse hover 時に文字を白くするため。これは css ではできない
            addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent t) {
                    if (MouseEvent.MOUSE_ENTERED.equals(t.getEventType())) {
                        checkMark.setFill(Const.PNS_WHITE);
                        itemLabel.setGraphic(iconCallback.callSelected(item));
                    } else if (MouseEvent.MOUSE_EXITED.equals(t.getEventType())) {
                        checkMark.setFill(Const.PNS_BLACK);
                        itemLabel.setGraphic(iconCallback.call(item));
                    }
                }
            });
        }
        public void setIcon(Node n) { itemLabel.setGraphic(n); }
    }
    /**
     * ボタンクリックで表示される Popup
     */
    private class ViewPopup extends Popup {
        private final VBox content;

        public ViewPopup() {
            setAutoHide(true);
            content = new VBox();
            content.getStyleClass().add(StyleClass.PNS_COMBO_BOX_POPUP);
            getContent().add(content);
        }

        public void add(ListCellToggle toggle) {
            content.getChildren().add(toggle);
        }

        /**
         * 位置補正して showPopup
         * @param source
         */
        public void show(Node source) {
            if (isShowing()) {
                hide();

            } else {
                Point2D p = SchemaUtils.getScreenLocation(source);
                // 位置補正
                // shadow の分 左16, 上12 シフト，チェックボックスの分 左12 シフト
                // 選択された項目の分だけ上シフト，微調整
                show(source, p.getX()-16-12+2, p.getY()-12 - getSelectionIndex()*(PnsComboBox.this.getPrefHeight()-6) -2);

                // Popup された時に初期表示させるために mouse hover のイベントをでっち上げる
                //com.sun.glass.ui.Robot r = com.sun.glass.ui.Application.GetApplication().createRobot();
                //int x = r.getMouseX(); int y = r.getMouseY();
                //r.mouseMove(x,y);

            }
        }
    }
}

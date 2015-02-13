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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import open.dolphin.impl.scheam.constant.StyleClass;

/**
 * ToggleButton をセットにしたもの
 * @author pns
 * @param <T>
 */
public class PnsToggleSet<T> extends HBox {
    private static final double HEIGHT = 22;
    private static final double BUTTON_WIDTH = 28;

    private final ToggleGroup group;
    private final ObjectProperty<ObservableList<T>> itemsProperty;
    /** 選択される (T)item を入れる */
    private final ObservableList<T> items;
    /** どの (T)item が選択されているか */
    private final ObjectProperty<T> selectionProperty;
    /** (T)item をキーとして対応する Toggle を入れる */
    private final HashMap<T, Toggle> toggleMap;
    /** (T)item から icon 用 graphic への callback */
    private PnsIconCallback<T, Node> iconCallback;

    public PnsToggleSet() {
        this(BUTTON_WIDTH, HEIGHT);
    }

    public PnsToggleSet(final double w, final double h) {

        getStyleClass().add(StyleClass.PNS_TOGGLE_SET);
        setPrefHeight(h); setMinHeight(h); setMaxHeight(h);
        setAlignment(Pos.CENTER);

        group = new ToggleGroup();
        itemsProperty = new SimpleObjectProperty<>();
        selectionProperty = new SimpleObjectProperty<>();
        toggleMap = new HashMap<>();
        items = FXCollections.observableArrayList();
        itemsProperty.set(items);

        iconCallback = new DefaultCallback();

        // binds
        items.addListener(new ListChangeListener<T>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> change) {
                int size = change.getList().size();
                int count = 0;

                for (final T item : change.getList()) {
                    ToggleButton toggle = new ToggleButton();
                    toggle.setToggleGroup(group);
                    //toggle.setPrefWidth(w); toggle.setMaxWidth(w); toggle.setMinWidth(w);
                    toggle.setPrefSize(w,h+1); toggle.setMaxSize(w,h); toggle.setMinSize(w,h);
                    toggle.setFocusTraversable(false);

                    toggle.setOnAction(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent t) {
                            selectionProperty.set(item);
                        }
                    });
                    if (count == 0) {
                        // 左端
                        toggle.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_LEFT);
                    } else if (count == size -1) {
                        // 右端
                        toggle.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_RIGHT);
                    } else {
                        // 真ん中
                        toggle.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_MIDDLE);
                    }

                    if (count != 0) {
                        Pane separator = new Pane();
                        separator.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_SEPARATOR);
                        separator.setPrefSize(1, h-6); separator.setMinSize(1, h-6); separator.setMaxSize(1, h-6);
                        getChildren().add(separator);
                    }
                    count ++;
                    getChildren().add(toggle);
                    toggleMap.put(item, toggle);
                }
                resetIcon();
            }
        });
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                // 選択された toggle を再選択した場合の処理
                if (t1 == null) { t.setSelected(true); }
            }
        });
        selectionProperty.addListener(new ChangeListener<T>(){
            @Override
            public void changed(ObservableValue<? extends T> ov, T t, T t1) {
                Toggle selected = toggleMap.get(t1);
                group.selectToggle(selected);
                // separator の表示／非表示の切り替え
                if (t != null) {
                    Toggle unselected = toggleMap.get(t);
                    // 選択が解除されたら separator を出す
                    setSeparatorVisible(unselected, true);
                    // icon 表示
                    ((ToggleButton)unselected).setGraphic(iconCallback.call(t));
                }
                // 選択されたら両端の separator は消す
                setSeparatorVisible(selected, false);
                // icon 表示
                ((ToggleButton)toggleMap.get(t1)).setGraphic(iconCallback.callSelected(t1));
            }
        });
    }
    /**
     * ToggleButton の両脇の separator を出したり隠したりする
     * @param toggle
     * @param visible
     */
    private void setSeparatorVisible(Toggle toggle, boolean visible) {
        int size = getChildren().size();
        int index = getChildren().indexOf(toggle);

        if (index == 0) { getChildren().get(1).setVisible(visible); }
        else if (index == size-1) { getChildren().get(size-2).setVisible(visible);}
        else { getChildren().get(index-1).setVisible(visible); getChildren().get(index+1).setVisible(visible); }
    }
    /**
     * 非選択時のアイコン画像への callback
     * @param callback */
    public void setIconCallback(PnsIconCallback<T, Node> callback) {
        iconCallback = callback;
        resetIcon();
    }

    private void resetIcon() {
        for (T item : getItems()) {
            ToggleButton t = (ToggleButton) toggleMap.get(item);
            if (t.isSelected()) { t.setGraphic(iconCallback.callSelected(item)); }
            else { t.setGraphic(iconCallback.call(item)); }
        }
    }

    public ObservableList<T> getItems() { return itemsProperty.get(); }
    public ObjectProperty<T> selectionProperty() { return selectionProperty; }
    public void setSelection(T selection) { selectionProperty.set(selection); }

    /**
     * アイコン表示のためのデフォルト callback
     */
    private class DefaultCallback implements PnsIconCallback<T, Node> {
        @Override
        public Node call(T item) {
            Label l = new Label(item.toString());
            l.setTextFill(Color.BLACK);
            return l;
        }
        @Override
        public Node callSelected(T item) {
            Label l = new Label(item.toString());
            l.setTextFill(Color.WHITE);
            return l;
        }
    }

    /**
     * 指定した Toggle を enable する
     * @param item
     */
    public void setEnable(T item) {
        ToggleButton t = (ToggleButton) toggleMap.get(item);
        t.setDisable(false);
    }

    /**
     * 指定した Toggle を disable する
     * @param item
     */
    public void setDisable(T item) {
        ToggleButton t = (ToggleButton) toggleMap.get(item);
        t.setDisable(true);
    }

    /**
     * 指定した Toggle に Tooltip を加える
     * @param item
     * @param text
     */
    public void setTooltip(T item, String text) {
        ToggleButton t = (ToggleButton) toggleMap.get(item);
        t.setTooltip(new Tooltip(text));
    }


/*
    //ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
    public static void main(String[] s) {
        // Mac OS X needs this to avoid HeadlessException
        System.setProperty("java.awt.headless", "false");

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                final JFrame frame = new JFrame();
                frame.setUndecorated(false);
                final JFXPanel fxp = new JFXPanel();
                frame.add(fxp);

                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        HBox pane = new HBox();
                        pane.setPrefSize(150, 100);
                        PnsToggleSet<String> set = new PnsToggleSet<>();
                        set.getItems().addAll("TEST", "TEST", "TEST");
                        pane.getChildren().add(set);
                        Scene scene = new Scene(pane);
                        scene.getStylesheets().add(StyleClass.CSS_FILE);
                        fxp.setScene(scene);
                    }
                });

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }*/
}

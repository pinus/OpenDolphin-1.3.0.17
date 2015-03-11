package open.dolphin.impl.scheam.widget;

import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * mac os x like stage.
 * <ul>
 * <li>ToolPane - ツール部分
 * <li>MenuPane - メニュー部分
 * <li>ContentPane - 内容
 * <li>FooterPane - ボタンなどをおく Bottom 部分
 * </ul>
 * @author pns
 */
public class PnsStage extends Stage {
    /** Tool Pane */
    private final StackPane toolPane;
    /** Menu Pane */
    private final StackPane menuPane;
    /** 内容を入れる Content Pane */
    private final StackPane contentPane;
    /** 最下部に OK ボタンなどを貼るための Footer Pane */
    private final StackPane footerPane;
    /** Window inactivated の時の PseudoClass */
    private final PseudoClass inactivatedPseudoClass = PseudoClass.getPseudoClass("inactivated");

    public PnsStage() {
        super();
        initStyle(StageStyle.UNIFIED);
        // タイトル部分の pane
        toolPane = new StackPane();
        toolPane.getStyleClass().add("tool-pane");
        // Menu を入れる pane
        menuPane = new StackPane();
        menuPane.getStyleClass().add("menu-pane");
        // 実際の内容を入れる pane
        contentPane = new StackPane();
        contentPane.getStyleClass().add("content-pane");
        // Bottom 部分に OK ボタンなどをつけるのに使う pane
        footerPane = new StackPane();
        footerPane.getStyleClass().add("footer-pane");

        // Layout
        VBox layout = new VBox();
        VBox.setVgrow(toolPane, Priority.NEVER);
        VBox.setVgrow(menuPane, Priority.NEVER);
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        VBox.setVgrow(footerPane, Priority.NEVER);
        layout.getChildren().addAll(toolPane, menuPane, contentPane, footerPane);

        // ツールペインをつかんで移動できるようにする
        toolPane.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
            private double x, y;
            private double toX = -1, toY = -1;

            @Override
            public void handle(MouseEvent t) {
                if (MouseEvent.MOUSE_RELEASED.equals(t.getEventType())) {
                    toX = -1;

                } else if (MouseEvent.MOUSE_DRAGGED.equals(t.getEventType())) {

                    if (toX == -1) {
                        toX = PnsStage.this.getX();
                        toY = PnsStage.this.getY();

                    } else {
                        toX += ( t.getScreenX() - x );
                        toY += ( t.getScreenY() - y );
                        PnsStage.this.setX(toX);
                        PnsStage.this.setY(toY);
                    }
                    x = t.getScreenX();
                    y = t.getScreenY();
                }
            }
        });

        // ステージが inactivate されたら全ての node で inactivatedPseudoClass をセットする
        // :inactivated を指定した css が採用されるようになる
        focusedProperty().addListener((ov, oldValue, newValue) -> {
            setInactivatedPseudoClassRecurrsive(newValue, layout);
        });

        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("css/pns-stage.css");
        setScene(scene);
    }
    /**
     * Tool をセットする
     * @param n
     */
    public void addTool(Node n) { toolPane.getChildren().add(n); }
    /**
     * Menu をセットする
     * @param n
     */
    public void addMenu(Node n) { menuPane.getChildren().add(n); }
    /**
     * Content 部分に node を追加する
     * @param n
     */
    public void addContent(Node n) { contentPane.getChildren().add(n); }
    /**
     * Bottom 部分に node を追加する
     * @param n
     */
    public void addFooter(Node n) { footerPane.getChildren().add(n); }
    /**
     * Tool をのせる StackPane を返す
     * @return
     */
    public StackPane getToolPane() { return toolPane; }
    /**
     * Content をのせる StackPane を返す
     * @return
     */
    public StackPane getContentPane() { return contentPane; }
    /**
     * Footer をのせる StackPane を返す
     * @return
     */
    public StackPane getFooterPane() { return footerPane; }

    /**
     * window が inactive になったら inactivatedPseudoClass を全ての node にセットする
     * @param node
     */
    private void setInactivatedPseudoClassRecurrsive (boolean isActive, Node node) {
        node.pseudoClassStateChanged(inactivatedPseudoClass, ! isActive);

        if (node instanceof Region) {
            Region r = (Region) node;
            r.requestLayout();
            r.getChildrenUnmodifiable().forEach( n -> setInactivatedPseudoClassRecurrsive(isActive, n));
        }
    }
}

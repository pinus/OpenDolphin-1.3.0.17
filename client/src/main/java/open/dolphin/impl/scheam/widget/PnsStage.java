package open.dolphin.impl.scheam.widget;

import java.util.HashMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import open.dolphin.impl.scheam.constant.Const;
import open.dolphin.impl.scheam.constant.StyleClass;

/**
 * mac os x like stage
 *
 * ToolPane - タイトルバーと連続したツール部分
 * ContentPane - 内容
 * FooterPane - ボタンなどをおく Bottom 部分
 *
 * @author pns
 */
public class PnsStage extends Stage {
    /** タイトル部分の高さ */
    private static final double TITLE_HEIGHT = 22;
    /**  window inactivate 時の style */
    private static final String INACTIVE_BACKGROUND
            = "-fx-background-color: linear-gradient(#F0F0F0FF, #E0E0E0FF); ";
    /** 角を丸く clipping するための rectangle */
    private final Rectangle clippingRect;
    /** 全体を gray の線で囲うための rectangle */
    private final Rectangle borderRect;
    /** この stage が focus を取っているかどうか */
    private boolean isActive;
    /** タイトルバーの代わりに使う toolPane */
    private final VBox toolPane;
    /** 内容を入れる pane */
    private final StackPane contentPane;
    /** 最下部に OK ボタンなどを貼るための pane */
    private final StackPane footerPane;
    /** tool pane と content pane を入れる全体の pane */
    private final VBox layoutPane;
    /** 影，border，resize box を貼るための pane */
    private final StackPane shadowPane;
    /** tool pane 内の node の disable の状態を保存しておくための map */
    private final HashMap<Node, Boolean> isNodeDisable = new HashMap<>();
    /** resize のために mouse event を listen する rectangles */
    private final Rectangle topLeftRect;
    private final Rectangle topRightRect;
    private final Rectangle bottomLeftRect;
    private final Rectangle bottomRightRect;
    private final Rectangle topCenterRect;
    private final Rectangle bottomCenterRect;
    private final Rectangle centerLeftRect;
    private final Rectangle centerRightRect;
    /** 全ての resize rectangles を入れる配列 */
    private final Rectangle[] resizeRects;
    /** title bar button の green button の処理用 */
    private Rectangle2D prevBounds;
    private boolean isMaximized = false;

    public PnsStage() {
        super();
        initStyle(StageStyle.TRANSPARENT);
        // タイトル部分の pane
        toolPane = new VBox();
        // 実際の内容を入れる pane
        contentPane = new StackPane();
        // Bottom 部分に OK ボタンなどをつけるのに使う pane
        footerPane = new StackPane();
        // 全体のレイアウト
        layoutPane = new VBox();
        // 影，border，resize rectangles を貼る
        shadowPane = new StackPane();
        // layout pane を角丸に clipping するための rectangle
        clippingRect = RectangleBuilder.create().arcHeight(12).arcWidth(12).build();
        // 角丸の border line を描くための rectangel
        borderRect = RectangleBuilder.create().arcHeight(12).arcWidth(12).fill(Color.TRANSPARENT).stroke(Color.web("#B8B8B8")).build();
        // resize のための 8 個の rectangle を作る。４隅と４辺
        topLeftRect = RectangleBuilder.create().width(8).height(8).fill(Color.TRANSPARENT).id("TL").build();
        topRightRect = RectangleBuilder.create().width(16).height(16).fill(Color.TRANSPARENT).id("TR").build();
        bottomLeftRect = RectangleBuilder.create().width(16).height(16).fill(Color.TRANSPARENT).id("BL").build();
        bottomRightRect = RectangleBuilder.create().width(16).height(16).fill(Color.TRANSPARENT).id("BR").build();
        topCenterRect = RectangleBuilder.create().width(8).height(8).fill(Color.TRANSPARENT).id("TC").build();
        bottomCenterRect = RectangleBuilder.create().width(8).height(8).fill(Color.TRANSPARENT).id("BC").build();
        centerLeftRect = RectangleBuilder.create().width(8).height(8).fill(Color.TRANSPARENT).id("CL").build();
        centerRightRect = RectangleBuilder.create().width(8).height(8).fill(Color.TRANSPARENT).id("CR").build();
        // resize rectangle を入れる配列
        resizeRects = new Rectangle[]{topLeftRect, topRightRect, bottomLeftRect, bottomRightRect,
            topCenterRect, bottomCenterRect, centerLeftRect, centerRightRect};

        initComponents();
    }
    /**
     * Tool をセットする
     * @param n
     */
    public void setTool(Node n) {
        toolPane.getChildren().add(n);
    }
    /**
     * Content 部分に node を追加する
     * @param n
     */
    public void addContent(Node n) { contentPane.getChildren().add(n); }
    /**
     * Content をのせる StackPane を返す
     * @return
     */
    public StackPane getContentPane() { return contentPane; }
    /**
     * Bottom 部分に node を追加する
     * @param n
     */
    public void addFooter(Node n) {
        footerPane.getChildren().add(n);
    }

    private void initComponents() {

        // tool pane を入れてグラデーションをつける pane
        final StackPane titlePane = new StackPane();
        titlePane.getStyleClass().add(StyleClass.PNS_STAGE_TITLE_PANE);

        // タイトルラベル　あとで titleProperty と connect する
        final Label titleLabel = new Label();
        titleLabel.setPrefHeight(0);
        titleLabel.setMaxHeight(0);
        titleLabel.setMinHeight(0);
        titleLabel.setAlignment(Pos.CENTER);
        // タイトルラベルは toolPane に組み込んでおく
        toolPane.setAlignment(Pos.CENTER);
        toolPane.getChildren().add(titleLabel);
        titlePane.getChildren().add(toolPane);

        // title bar buttons 　これも表示 On/Off は後で titleProperty で調節
        final Label titleBarButtons = new Label();
        titleBarButtons.setGraphic(Const.IMAGE_BAR_BUTTONS);
        titleBarButtons.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                if (MouseEvent.MOUSE_ENTERED.equals(t.getEventType())) {
                    titleBarButtons.setGraphic(Const.IMAGE_BAR_BUTTONS_HOVER);

                } else if (MouseEvent.MOUSE_EXITED.equals(t.getEventType())) {
                    if (isActive) { titleBarButtons.setGraphic(Const.IMAGE_BAR_BUTTONS);}
                    else { titleBarButtons.setGraphic(Const.IMAGE_BAR_BUTTONS_DISABLE); }

                } else if (MouseEvent.MOUSE_CLICKED.equals(t.getEventType())) {
                    // 幅 0-11:赤，20-31:黄，40-51:緑
                    if (t.getX() <= 11) {
                        hide();
                    } else if (20 <= t.getX() && t.getX() <= 31) {
                        setIconified(true);

                    } else if (40 <= t.getX()) {
                        if (isMaximized) {
                            setX(prevBounds.getMinX());
                            setY(prevBounds.getMinY());
                            setWidth(prevBounds.getWidth());
                            setHeight(prevBounds.getHeight());
                            isMaximized = false;
                        } else {
                            prevBounds = new Rectangle2D(getX(), getY(), getWidth(), getHeight());
                            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                            setX(bounds.getMinX());
                            setY(bounds.getMinY());
                            setWidth(bounds.getWidth());
                            setHeight(bounds.getHeight()-100);
                            isMaximized = true;
                        }
                    }
                }
            }
        });
        // 初期値は「表示しない」　titleProperty で表示を制御する
        titleBarButtons.setVisible(false);

        titlePane.getChildren().add(titleBarButtons);
        StackPane.setAlignment(titleBarButtons, Pos.TOP_LEFT);
        StackPane.setMargin(titleBarButtons, new Insets(2,0,0,8));

        // タイトルペインをつかんで移動できるようにする
        titlePane.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
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

        // 全体のレイアウト
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        layoutPane.getChildren().addAll(titlePane, contentPane, spacer, footerPane);
        layoutPane.setStyle("-fx-background-color: #E0E0E0FF; ");
        // 角丸に clipping
        layoutPane.setClip(clippingRect);

        // さらに影や border をつけるのに shadow pane を用意する
        shadowPane.getStyleClass().add(StyleClass.PNS_STAGE);
        shadowPane.getChildren().addAll(layoutPane, borderRect);
        StackPane.setAlignment(layoutPane, Pos.TOP_LEFT);
        StackPane.setAlignment(borderRect, Pos.TOP_LEFT);
        // mouse event を素通し
        borderRect.setMouseTransparent(true);

        // resize rectangle の鍼付け位置を指定
        StackPane.setAlignment(topLeftRect, Pos.TOP_LEFT);
        StackPane.setAlignment(topRightRect, Pos.TOP_RIGHT);
        StackPane.setAlignment(bottomLeftRect, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(bottomRightRect, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(topCenterRect, Pos.TOP_CENTER);
        StackPane.setAlignment(bottomCenterRect, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(centerLeftRect, Pos.CENTER_LEFT);
        StackPane.setAlignment(centerRightRect, Pos.CENTER_RIGHT);

        // resize rect にカーソルを設定
        topLeftRect.setCursor(Const.IMAGE_CURSOR_NWSE);
        topRightRect.setCursor(Const.IMAGE_CURSOR_NESW);
        bottomLeftRect.setCursor(Const.IMAGE_CURSOR_NESW);
        bottomRightRect.setCursor(Const.IMAGE_CURSOR_NWSE);
        topCenterRect.setCursor(Const.IMAGE_CURSOR_NS);
        bottomCenterRect.setCursor(Const.IMAGE_CURSOR_NS);
        centerLeftRect.setCursor(Const.IMAGE_CURSOR_EW);
        centerRightRect.setCursor(Const.IMAGE_CURSOR_EW);

        // resize rect を shadow pane に登録し，listener をつける
        if (isResizable()) { addResizeRects(); }
        // resizable によって listener を付けたり取ったりする
        resizableProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (!t && t1) { addResizeRects(); }
                if (t && !t1) { removeResizeRects(); }
            }
        });

        // window focus listener: focus に応じて背景を変える
        focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                isActive = t1;
                if (isActive) {
                    titlePane.setStyle("");
                    titleBarButtons.setGraphic(Const.IMAGE_BAR_BUTTONS);
                } else {
                    titlePane.setStyle(INACTIVE_BACKGROUND);
                    titleBarButtons.setGraphic(Const.IMAGE_BAR_BUTTONS_DISABLE);
                }

                setBackgroundRecurrsive(toolPane);
            }
        });
        // stage の大きさが変わったときに関連する node の大きさを合わせる
        // stage と layoutPane の大きさの差は 32 (dropShadow 16x2 の分）
        widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                double w = getWidth() - 32;
                clippingRect.setWidth(w);
                borderRect.setWidth(w);
                topCenterRect.setWidth(w-32);
                bottomCenterRect.setWidth(w-32);
            }
        });
        heightProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                double h = getHeight() - 32;
                clippingRect.setHeight(h);
                borderRect.setHeight(h);
                centerLeftRect.setHeight(h-32);
                centerRightRect.setHeight(h-32);

            }
        });
        // タイトル，タイトルバーボタン処理
        titleProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                double h = (t1 == null || t1.isEmpty())? 0 : TITLE_HEIGHT;
                titleLabel.setText(t1);
                titleLabel.setPrefHeight(h);
                titleLabel.setMinHeight(h);
                titleLabel.setMaxHeight(h);
                titleBarButtons.setVisible(h != 0);
            }
        });

        // scene に登録
        Scene scene = new Scene(shadowPane);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
            }
    /**
     * window が inactive になったら node に含まれる node を再帰的に全て disable にして active になったら元に戻す
     * @param node
     */
    private void setBackgroundRecurrsive (Node node) {
        if (isActive) {
            if (isNodeDisable.get(node) != null) {
                node.setDisable(isNodeDisable.get(node));
            }

        } else {
            isNodeDisable.put(node, node.isDisable());
            node.setDisable(true);
        }

        if (node instanceof Parent) {
            Parent p = (Parent) node;
            for (Node n : p.getChildrenUnmodifiable()) {
                setBackgroundRecurrsive(n);
            }
        }
    }
    /**
     * resize rect に mouse listener を登録
     */
    private void addResizeRects() {
        for (Rectangle r : resizeRects) {
            shadowPane.getChildren().add(r);
            r.addEventHandler(MouseEvent.ANY, new ResizeMouseListener(r.getId()));
        }
    }
    /**
     * resize rect から mouse listener を除去
     */
    private void removeResizeRects() {
        for (Rectangle r : resizeRects) {
            shadowPane.getChildren().remove(r);
            r.removeEventHandler(MouseEvent.ANY, new ResizeMouseListener(r.getId()));
        }
    }

    /**
     * resize のための mouse listener
     * id は TL, TR, BL, BR, TC, BC, CL, CR のいずれか
     */
    private class ResizeMouseListener implements EventHandler<MouseEvent> {
        private final String id;
        private double x, y;
        private double toX = -1, toY = -1, toW = -1, toH = -1;
        private double divX, divY;

        public ResizeMouseListener(String id) {
            this.id = id;
        }

        @Override
        public void handle(MouseEvent t) {
            if (MouseEvent.MOUSE_RELEASED.equals(t.getEventType())) {
                toX = -1;

            } else if (MouseEvent.MOUSE_DRAGGED.equals(t.getEventType())) {

                if (toX == -1) {
                    toX = PnsStage.this.getX();
                    toY = PnsStage.this.getY();
                    toW = PnsStage.this.getWidth();
                    toH = PnsStage.this.getHeight();

                } else {
                    divX = ( t.getScreenX() - x );
                    divY = ( t.getScreenY() - y );

                    if (id.equals("TL")) {
                        toX += divX; toY += divY;
                        toW -= divX; toH -= divY;
                    } else if (id.equals("TR")) {
                        toY += divY;
                        toW += divX; toH -= divY;
                    } else if (id.equals("BL")) {
                        toX += divX;
                        toW -= divX; toH += divY;
                    } else if (id.equals("BR")) {
                        toW += divX; toH += divY;
                    } else if (id.equals("TC")) {
                        toY += divY;
                        toH -= divY;
                    } else if (id.equals("BC")) {
                        toH += divY;
                    } else if (id.equals("CL")) {
                        toX += divX;
                        toW -= divX;
                    } else if (id.equals("CR")) {
                        toW += divX;
                    }

                    PnsStage.this.setX(toX);
                    PnsStage.this.setY(toY);

                    double minw = PnsStage.this.getMinWidth();
                    double minh = PnsStage.this.getMinHeight();

                    if (toW > minw) { PnsStage.this.setWidth(toW); }
                    else { PnsStage.this.setWidth(minw); }
                    if (toH > minh) { PnsStage.this.setHeight(toH); }
                    else { PnsStage.this.setWidth(minh); }
                }
                x = t.getScreenX();
                y = t.getScreenY();
            }
        }
    }
}

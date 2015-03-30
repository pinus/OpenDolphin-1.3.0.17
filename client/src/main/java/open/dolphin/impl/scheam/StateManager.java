package open.dolphin.impl.scheam;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import open.dolphin.impl.scheam.stateeditor.*;

/**
 * DraftLayer から MouseEvent, CanvasStage から KeyEvent を
 * 受け取って，State に応じて対応する StateEditor に渡す.
 * 完成したら新たな DrawLayer を作って CanvasPane に積む.
 * @author pns
 */
public class StateManager {
    // MOUSE_PRESSED なしで MOUSE_DRAGGED や MOUSE_RELEASED が発生することがあるのの workaround
    private static boolean mousePressed = false;
    // 最後の MouseEvent を保存
    private static Event mouseDragEvent;
    // Base 画像を入れる SchemaLayer
    private final SchemaLayer baseLayer;
    // DrawLayer を積むための StackPane
    private final StackPane canvasPane;
    // State 変化のプロパティー
    private final ObjectProperty<StateEditor> stateEditorProperty;
    // DolphinProperties
    private final SchemaEditorProperties properties;
    // context
    private final SchemaEditorImpl context;

    public StateManager(final SchemaEditorImpl context) {
        this.context = context;
        baseLayer = context.getBaseLayer();
        canvasPane = context.getCanvasPane();
        properties = SchemaEditorImpl.getProperties();
        stateEditorProperty = new SimpleObjectProperty<>();

        // 選択可能な StateEditor
        final StateEditor penEditor = new PenEditor(context);
        final StateEditor lineEditor = new LineEditor(context);
        final StateEditor ovalEditor = new OvalEditor(context);
        final StateEditor rectangleEditor = new RectangleEditor(context);
        final StateEditor polygonEditor = new PolygonEditor(context);
        final StateEditor dotsEditor = new DotsEditor(context);
        final StateEditor netEditor = new NetEditor(context);
        final StateEditor textEditor = new TextEditor(context);
        final StateEditor eraserEditor = new EraserEditor(context);
        final StateEditor translateEditor = new TranslateEditor(context);
        final StateEditor scaleEditor = new ScaleEditor(context);
        final StateEditor rotateEditor = new RotateEditor(context);
        final StateEditor clipEditor = new ClipEditor(context);

        // StateEditorProperty と StateProperty を bind して，State に応じて StateEditor が切り替わるようにする
        stateEditorProperty.bind(new ObjectBinding<StateEditor>() {
            { super.bind(properties.stateProperty()); }

            @Override
            protected StateEditor computeValue() {
                switch(properties.getState()) {
                    case Pen:
                        return penEditor;
                    case Line:
                        return lineEditor;
                    case Oval:
                        return ovalEditor;
                    case Rectangle:
                        return rectangleEditor;
                    case Polygon:
                        return polygonEditor;
                    case Dots:
                        return dotsEditor;
                    case Net:
                        return netEditor;
                    case Text:
                        return textEditor;
                    case Eraser:
                        return eraserEditor;
                    case Translate:
                        return translateEditor;
                    case Scale:
                        return scaleEditor;
                    case Rotate:
                        return rotateEditor;
                    case Clip:
                        return clipEditor;
                }
                return null;
            }
        });

        // StateEditor が切り替わった時点で，Start / End を送る
        stateEditorProperty.addListener((ObservableValue<? extends StateEditor> ov, StateEditor t, StateEditor t1) -> {
            if (t != null) {
                // Mouse drug 中に State の切換があったら，編集を完了させてしまう
                // TrackPad で 3本指ドラッグをしていると，手を放してもすぐには MOUSE_RELEASED が発生しないので
                // 編集完了する前にショートカットキーで State 切り替えが発生してしまうことがあるのの対策
                if (mouseDragEvent != null) {
                    t.mouseUp((MouseEvent)mouseDragEvent);
                    addDrawLayer(t.getHolder());
                    mousePressed = false;
                    mouseDragEvent = null;
                }
                t.end();
            }
            if (t1 != null) { t1.start(); }
        });
    }

    public ObjectProperty<StateEditor> stateProperty() { return stateEditorProperty; }
    public StateEditor getStateEditor() { return stateEditorProperty.get(); }
    public static boolean isMousePressed() { return mousePressed; }


    /**
     * マウスが押された時の処理.
     * @param e
     */
    public void mousePressed(MouseEvent e) {
        if (mousePressed) {
            // mouse released を取りこぼした場合
            getStateEditor().mouseUp(e);

        } else {
            mousePressed = true;
            mouseDragEvent = null;
            getStateEditor().mouseDown(e);
        }
    }

    /**
     * マウスドラッグ処理.
     * 押される前にドラッグに入ってくることがある.
     * @param e
     */
    public void mouseDragged(MouseEvent e) {
        if (mousePressed) {
            mouseDragEvent = e;
            getStateEditor().mouseDragged(e);

        } else {
            mousePressed = true;
            mouseDragEvent = null;
            getStateEditor().mouseDown(e);
        }
    }

    /**
     * マウス移動処理.
     * @param e
     */
    public void mouseMoved(MouseEvent e) {
        mousePressed = false;
        mouseDragEvent = null;
        getStateEditor().mouseMoved(e);
    }

    /**
     * マウスを離した処理.
     * 押されてないのに入ってくることがある.
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
        if (mousePressed) {
            getStateEditor().mouseUp(e);
            addDrawLayer(getStateEditor().getHolder());
        }
        mousePressed = false;
        mouseDragEvent = null;
    }

    /**
     * ショートカットキー処理と StateEditor への KeyPressed 送信.
     * @param e
     */
    public void keyPressed(KeyEvent e) {

        // StateEditor へのイベント送信
        getStateEditor().keyPressed(e);

        // Short cut keys
        //
        // without combination
        //
        if (!e.isAltDown() && !e.isControlDown() && !e.isMetaDown() && !e.isShiftDown()) {
            switch (e.getCode()) {
                case ESCAPE:
                    // エスケープが押されたら end を呼ぶ
                    getStateEditor().end();
                    break;
                case F:
                    // Free Line
                    properties.stateProperty().set(State.Pen);
                    break;
                case L:
                    properties.stateProperty().set(State.Line);
                    break;
                case O:
                    properties.stateProperty().set(State.Oval);
                    break;
                case Q:
                    // Quadrilateral
                    properties.stateProperty().set(State.Rectangle);
                    break;
                case P:
                    properties.stateProperty().set(State.Polygon);
                    break;
                case D:
                    properties.stateProperty().set(State.Dots);
                    break;
                case N:
                    properties.stateProperty().set(State.Net);
                    break;
                case X:
                    // teXt
                    properties.stateProperty().set(State.Text);
                    break;
                case E:
                    properties.stateProperty().set(State.Eraser);
                    break;
                case T:
                    properties.stateProperty().set(State.Translate);
                    break;
                case R:
                    properties.stateProperty().set(State.Rotate);
                    break;
                case C:
                    properties.stateProperty().set(State.Clip);
                    break;
                case Z:
                    properties.stateProperty().set(State.Scale);
                    break;
                case DIGIT1:
                    properties.setLineWidth(1.0);
                    break;
                case DIGIT2:
                    properties.setLineWidth(2.0);
                    break;
                case DIGIT3:
                    properties.setLineWidth(3.0);
                    break;
                case DIGIT4:
                    properties.setLineWidth(4.0);
                    break;
                case DIGIT5:
                    properties.setLineWidth(5.0);
                    break;
                case DIGIT6:
                    properties.setLineWidth(6.0);
                    break;
                case DIGIT7:
                    properties.setLineWidth(7.0);
                    break;
                }
        }
        //
        // command key
        //
        else if (!e.isAltDown() && !e.isControlDown() && e.isMetaDown() && !e.isShiftDown()) {
            switch (e.getCode()) {
//                case W:
//                    // command-W で終了
//                    if (e.isMetaDown()) {
//                        /*
//                        Without runLater causes fatal error!
//                        # A fatal error has been detected by the Java Runtime Environment:
//                        #  SIGSEGV (0xb) at pc=0x00007fff8d6b7250, pid=8943, tid=1799
//                        # JRE version: Java(TM) SE Runtime Environment (7.0_51-b13) (build 1.7.0_51-b13)
//                        # Java VM: Java HotSpot(TM) 64-Bit Server VM (24.51-b03 mixed mode bsd-amd64 compressed oops)
//                        # Problematic frame:
//                        # C  [libobjc.A.dylib+0x6250]  objc_msgSend+0x10
//                        */
//                        Platform.runLater(new Runnable(){
//                            @Override
//                            public void run() {
//                                context.firePropertyChange(null);
//                                context.getCanvasStage().hide();
//                            }
//                        });
//                    }
//                    break;
                case Z:
                    // undo ... マウスドラッグ途中の場合は無視
                    if (! mousePressed) { context.getUndoManager().undo(); }
                    break;
            }

        }
        //
        // command-shift key
        //
        else if (!e.isAltDown() && !e.isControlDown() && e.isMetaDown() && e.isShiftDown()) {
            switch (e.getCode()) {
                case Z:
                    // redo ... マウスドラッグ途中の場合は無視
                    if (! mousePressed) { context.getUndoManager().redo(); }
                    break;
            }
        }
        //
        // shift key
        //
        else if (!e.isAltDown() && !e.isControlDown() && !e.isMetaDown() && e.isShiftDown()) {
            switch (e.getCode()) {
                case DIGIT0:
                    properties.setFillBlur(0.0);
                    break;
                case DIGIT1:
                    properties.setFillBlur(0.1);
                    break;
                case DIGIT2:
                    properties.setFillBlur(0.2);
                    break;
                case DIGIT3:
                    properties.setFillBlur(0.3);
                    break;
                case DIGIT4:
                    properties.setFillBlur(0.4);
                    break;
                case DIGIT5:
                    properties.setFillBlur(0.5);
                    break;
                case DIGIT6:
                    properties.setFillBlur(0.6);
                    break;
                case DIGIT7:
                    properties.setFillBlur(0.7);
                    break;
                case DIGIT8:
                    properties.setFillBlur(0.8);
                    break;
                case DIGIT9:
                    properties.setFillBlur(0.9);
                    break;
                case L:
                    properties.setFillMode(FillMode.Line);
                    break;
                case F:
                    properties.setFillMode(FillMode.Fill);
                    break;
                case M:
                    properties.setFillMode(FillMode.Mixed);
                    break;
            }
        }
    }

    /**
     * StateEditor に KeyRelased を送る.
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        getStateEditor().keyReleased(e);
    }

    /**
     * できあがった SchemaHolder を新たな SchemaLayer を作ってセットする.
     */
    private void addDrawLayer(ShapeHolder holder) {
        if (holder == null) { return; }

        // 新しい Layer
        SchemaLayer layer = new SchemaLayer();
        // 大きさを BaseLayer に bind する
        layer.widthProperty().bind(baseLayer.widthProperty());
        layer.heightProperty().bind(baseLayer.heightProperty());
        // Holder セット
        layer.setHolder(holder);
        layer.draw();
        StackPane.setAlignment(layer, Pos.CENTER);
        canvasPane.getChildren().add(layer);
    }
}

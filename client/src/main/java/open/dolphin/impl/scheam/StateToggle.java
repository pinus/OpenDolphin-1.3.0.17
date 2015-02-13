package open.dolphin.impl.scheam;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import open.dolphin.impl.scheam.iconcallback.IconCallbackPolygon;
import open.dolphin.impl.scheam.widget.PnsIconCallback;
import open.dolphin.impl.scheam.widget.PnsToggleSet;
import open.dolphin.impl.scheam.iconcallback.*;

/**
 * State 切換のための Toggle
 * @author pns
 */
public class StateToggle extends PnsToggleSet<State> {

    public StateToggle(double w, double h) {
        super(w, h);

        setIconCallback(new StateIconCallback());
        final SchemaEditorProperties properties = SchemaEditorImpl.getProperties();

        //
        // Clear, Undo, Redo は State を保持しない
        //
        selectionProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> ov, State t, State t1) {
                if (t1.equals(State.Clear) || t1.equals(State.Undo) || t1.equals(State.Redo)) {
                    // 選択して StateEditor に選択があったことを伝えてから
                    properties.setState(t1);
                    // 選択をもとに戻す
                    selectionProperty().set(t);
                    properties.setState(t);
                }
            }
        });
    }

    /**
     * State 選択の ToggleBar に表示されるアイコン
     */
    private class StateIconCallback implements PnsIconCallback<State, Node> {
        private final PnsIconCallback<FillMode, Node>
                pen, line, oval, rectangle, polygon, dots, net, text, eraser,
                translate, scale, rotate, clip, clear,
                undo, redo;

        public StateIconCallback() {
            pen = new IconCallbackPen();
            line = new IconCallbackLine();
            oval = new IconCallbackOval();
            rectangle = new IconCallbackRectangle();
            polygon = new IconCallbackPolygon();
            dots = new IconCallbackDots();
            net = new IconCallbackNet();
            text = new IconCallbackText();
            eraser = new IconCallbackEraser();
            translate = new IconCallbackTranslate();
            scale = new IconCallbackLoupe();
            rotate = new IconCallbackRotate();
            clip = new IconCallbackClip();
            clear = new IconCallbackClear();
            undo = new IconCallbackUndo();
            redo = new IconCallbackRedo();
        }

        @Override
        public Node call(State item) {
            switch(item) {
                case Pen:
                    return pen.call(FillMode.Line);
                case Line:
                    return line.call(FillMode.Line);
                case Oval:
                    return oval.call(FillMode.Line);
                case Rectangle:
                    return rectangle.call(FillMode.Line);
                case Polygon:
                    return polygon.call(FillMode.Line);
                case Dots:
                    return dots.call(FillMode.Line);
                case Net:
                    return net.call(FillMode.Line);
                case Text:
                    return text.call(FillMode.Line);
                case Eraser:
                    return eraser.call(FillMode.Line);
                case Translate:
                    return translate.call(FillMode.Line);
                case Scale:
                    return scale.call(FillMode.Line);
                case Rotate:
                    return rotate.call(FillMode.Line);
                case Clip:
                    return clip.call(FillMode.Line);
                case Clear:
                    return clear.call(FillMode.Line);
                case Undo:
                    return undo.call(FillMode.Line);
                case Redo:
                    return redo.call(FillMode.Line);
            }
            return null;
        }

        @Override
        public Node callSelected(State item) {
            switch(item) {
                case Pen:
                    return pen.callSelected(FillMode.Line);
                case Line:
                    return line.callSelected(FillMode.Line);
                case Oval:
                    return oval.callSelected(FillMode.Line);
                case Rectangle:
                    return rectangle.callSelected(FillMode.Line);
                case Polygon:
                    return polygon.callSelected(FillMode.Line);
                case Dots:
                    return dots.callSelected(FillMode.Line);
                case Net:
                    return net.callSelected(FillMode.Line);
                case Text:
                    return text.callSelected(FillMode.Line);
                case Eraser:
                    return eraser.callSelected(FillMode.Line);
                case Translate:
                    return translate.callSelected(FillMode.Line);
                case Scale:
                    return scale.callSelected(FillMode.Line);
                case Rotate:
                    return rotate.callSelected(FillMode.Line);
                case Clip:
                    return clip.callSelected(FillMode.Line);
                case Clear:
                    return clear.callSelected(FillMode.Line);
                case Undo:
                    return undo.callSelected(FillMode.Line);
                case Redo:
                    return redo.callSelected(FillMode.Line);
            }
            return null;
        }
    }
}

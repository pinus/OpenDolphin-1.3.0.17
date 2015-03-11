package open.dolphin.impl.scheam;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Callback;
import open.dolphin.impl.scheam.constant.Const;
import open.dolphin.impl.scheam.constant.ShapeIcon;
import open.dolphin.impl.scheam.widget.PnsToggleSet;

/**
 * State 切換のための Toggle.
 * @author pns
 */
public class StateToggle extends PnsToggleSet<State> {

    public StateToggle() {
        // icon callback
        setCellFactory(new IconFactory());

        getItems().addAll(State.values());

        // Clear, Undo, Redo はトグル動作をしない
        removeFromGroup(State.Clear);
        removeFromGroup(State.Undo);
        removeFromGroup(State.Redo);
        // 最初は disable
        setDisable(State.Undo, true);
        setDisable(State.Redo, true);
        // disable icon に update しておく
        updateCell(State.Undo);
        updateCell(State.Redo);

        // Tooltip
        setTooltip(State.Pen, "F  ");
        setTooltip(State.Line, "L  ");
        setTooltip(State.Oval, "O  ");
        setTooltip(State.Rectangle, "Q  ");
        setTooltip(State.Polygon, "P  ");
        setTooltip(State.Dots, "D  ");
        setTooltip(State.Net, "N  ");
        setTooltip(State.Text, "X  ");
        setTooltip(State.Eraser, "E  ");
        setTooltip(State.Translate, "T  ");
        setTooltip(State.Rotate, "R  ");
        setTooltip(State.Clip, "C  ");
        setTooltip(State.Scale, "Z  ");
    }

    /**
     * Icon 表示のための callback.
     */
    private class IconFactory implements Callback<State, Node> {
        private final Shape pen, line, oval, rectangle, polygon, dots, net, text, eraser, translate, loupe, rotate, clip, clear, undo, redo;

        private boolean selected;
        private boolean disabled;

        public IconFactory() {
            pen = ShapeIcon.getOpenPath();
            line = ShapeIcon.getLine();
            oval = ShapeIcon.getCircle();
            rectangle = ShapeIcon.getRectangle();
            polygon = ShapeIcon.getPolygon();
            dots = ShapeIcon.getDots();
            net = ShapeIcon.getNet();
            text = ShapeIcon.getText();
            eraser = ShapeIcon.getEraser();
            translate = ShapeIcon.getTranslatePointer();
            loupe = ShapeIcon.getLoupePlus();
            rotate = ShapeIcon.getRotate();
            clip = ShapeIcon.getClip();
            clear = ShapeIcon.getClear();
            undo = ShapeIcon.getUndo();
            redo = ShapeIcon.getRedo();
        }

        @Override
        public Node call(State s) {
            selected = (s == getSelectionModel().getSelectedItem());
            disabled = (getButton(s) == null)? false : getButton(s).isDisabled();

            switch(s) {
                case Pen:
                    setStroke(pen);
                    return pen;
                case Line:
                    setStroke(line);
                    return line;
                case Oval:
                    setStroke(oval);
                    return oval;
                case Rectangle:
                    setStroke(rectangle);
                    return rectangle;
                case Polygon:
                    setStroke(polygon);
                    return polygon;
                case Dots:
                    setStroke(dots);
                    return dots;
                case Net:
                    setStroke(net);
                    return net;
                case Text:
                    setFill(text);
                    return text;
                case Eraser:
                    if (selected) {
                        eraser.setStroke(Const.PNS_WHITE);
                        eraser.setFill(Color.GRAY);
                    } else {
                        eraser.setStroke(Const.PNS_BLACK);
                        eraser.setFill(Color.LIGHTGRAY);
                    }
                    return eraser;
                case Translate:
                    return translate;
                case Scale:
                    setStroke(loupe);
                    return loupe;
                case Rotate:
                    setStroke(rotate);
                    return rotate;
                case Clip:
                    setStroke(clip);
                    return clip;
                case Clear:
                    setStroke(clear);
                    return clear;
                case Undo:
                    setFill(undo);
                    setStroke(undo);
                    return undo;
                case Redo:
                    setFill(redo);
                    setStroke(redo);
                    return redo;
            }
            return null;
        }

        private void setStroke(Shape shape) {
            if (disabled) {
                shape.setStroke(Const.PNS_LIGHT_GLAY);
            } else {
                if (selected) { shape.setStroke(Const.PNS_WHITE); }
                else { shape.setStroke(Const.PNS_BLACK); }
            }
        }

        private void setFill(Shape shape) {
            if (disabled) {
                shape.setFill(Const.PNS_LIGHT_GLAY);
            } else {
            if (selected) { shape.setFill(Const.PNS_WHITE); }
            else { shape.setFill(Const.PNS_BLACK); }
            }
        }
    }
}

package open.dolphin.impl.scheam.stateeditor;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import open.dolphin.impl.scheam.FillMode;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaEditorProperties;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.State;
import open.dolphin.impl.scheam.shapeholder.LineHolder;
import open.dolphin.impl.scheam.shapeholder.ShapeHolderBase;

/**
 * 各々の ShapeHolder を選択して，移動，拡大／縮小，回転させる StateEditor.
 * Shape をドラッグすると移動.
 * 四隅をドラッグすると拡大／縮小.
 * Option を押しながら Shape 内を上下ドラッグすると回転.
 * Delete キーを押すと削除.
 *
 * @author pns
 */
public class TranslateEditor extends StateEditorBase {
    private final SchemaEditorProperties properties;
    private final StackPane canvasPane;
    private final SchemaLayer draftLayer;
    // 選択された Layer　〜選択があるかどうかの flag にもなる
    private SchemaLayer selectedLayer;
    // 選択された Holder　
    private ShapeHolderBase selectedHolder;
    // Drag 途中の x, y 座標を保持
    private double startx, starty;
    // selectedHolder の枠表示
    private final RubberBand band;
    // RubberBand のどこがクリックされたか　〜これにより動作が変わる
    private RubberBand.Pos pos;
    // Holder の選択が外れたら bind 前のオリジナルのプロパティーに戻すためにオリジナルのプロパティーを保存しておく
    private double prevLineWidth;
    private Color prevLineColor;
    private Color prevFillColor;
    private double prevFillBlur;
    private FillMode prevFillMode;
    private State prevPreviewState;

    public TranslateEditor(SchemaEditorImpl context) {
        properties = SchemaEditorImpl.getProperties();
        draftLayer = context.getDraftLayer();
        canvasPane = context.getCanvasPane();
        band = new RubberBand(draftLayer);
    }

    /**
     * Holder 選択してから DELETE で消去.
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (selectedLayer != null) {
            switch(e.getCode()) {
                case BACK_SPACE:
                    // デリートキーで削除
                    draftLayer.clear();
                    selectedLayer.clear();
                    unbind(selectedHolder);
                    band.setHolder(null);
                    canvasPane.getChildren().remove(selectedLayer);
                    selectedLayer = null;
                    selectedHolder = null;
                    break;

                case ALT:
                    // Line で Option Key を押すと矢印を付けたり取ったりする
                    if (band.isLine()) {
                        LineHolder lineHolder = (LineHolder) selectedHolder;
                        lineHolder.setDrawArrow(! lineHolder.getDrawArrow());
                        selectedLayer.redraw();
                    }
                    break;

                // 矢印キーで１ドット移動
                case DOWN:
                    translateDots(0, 1);
                    break;
                case UP:
                    translateDots(0, -1);
                    break;
                case RIGHT:
                    translateDots(1, 0);
                    break;
                case LEFT:
                    translateDots(-1, 0);
                    break;
           }
        }
    }

    /**
     * 指定ドット移動処理をして UndoManager に登録する.
     * @param x
     * @param y
     */
    private void translateDots(double x, double y) {
        properties.valueChangingProperty().set(true);
        selectedHolder.translate(x, y);
        selectedLayer.redraw();
        band.redraw();
        properties.valueChangingProperty().set(false);
    }

    @Override
    public void mouseDown(MouseEvent e) {

        if (band.contains(e.getX(), e.getY())) {
            startx = e.getX(); starty = e.getY();
            pos = band.getPos(startx, starty);

        } else {
            // 現在のマウス位置に Holder があるかどうかスキャン
            boolean found = false;
            ObservableList<Node> children = canvasPane.getChildren();

            for (int i=children.size()-1; i>=0; i--) {

                SchemaLayer layer = (SchemaLayer) children.get(i);

                if (layer.getHolder().contains(e.getX(), e.getY())) {
                    // 見つかった場合
                    startx = e.getX(); starty = e.getY();

                    // 選択された状態で他の Holder をクリックした場合などで現在選択されている Holder がああれば，unbind する
                    if (selectedHolder != null) { unbind(selectedHolder); }

                    // selectedLayer の Hoder を取り出して bind する
                    selectedLayer = layer;
                    selectedHolder = (ShapeHolderBase) selectedLayer.getHolder();
                    bind(selectedHolder);

                    // Rubber Band を描く
                    band.setHolder(selectedHolder);
                    band.redraw();
                    // pos は redraw しないと決まらない
                    pos = band.getPos(startx, starty);

                    found = true;
                    break;
                }
            }
            // 見つからない場合は選択解除処理をする
            if (! found) {
                if (selectedHolder != null) { unbind(selectedHolder); }
                draftLayer.clear();
                band.setHolder(null);
                selectedLayer = null;
                selectedHolder = null;
                pos = null;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedLayer != null) {
            properties.valueChangingProperty().set(true);
            selectedLayer.clear();

            double dx = e.getX() - startx;
            double dy = e.getY() - starty;

            if (e.isAltDown()) {
                // Option Key で回転　上下ドラッグで回転
                selectedHolder.rotate(dy/100);

            } else {
                // 通常の translate 処理
                if (pos == null) {
                    selectedHolder.translate(dx, dy);

                // pos に応じて拡大・縮小処理
                } else {
                    switch (pos) {
                        case xSyS:
                            selectedHolder.scale(-dx, -dy);
                            selectedHolder.translate(dx, dy);
                            break;
                        case xSyE:
                            selectedHolder.scale(-dx, dy);
                            selectedHolder.translate(dx, 0);
                            break;
                        case xEyS:
                            selectedHolder.scale(dx, -dy);
                            selectedHolder.translate(0, dy);
                            break;
                        case xEyE:
                            selectedHolder.scale(dx, dy);
                            break;
                    }
                }
            }

            selectedLayer.draw();
            startx = e.getX(); starty = e.getY();
            band.redraw();
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if (selectedLayer != null) {
            properties.valueChangingProperty().set(false);
        }
    }

    @Override
    public void end() {
        // State が他に切り替わるときに後始末
        if (selectedLayer != null) {
            unbind(selectedHolder);
            properties.valueChangingProperty().set(false);
        }

        draftLayer.clear();
        draftLayer.setBlendMode(null);
        band.setHolder(null);
    }

    @Override
    public ShapeHolder getHolder() {
        return null;
    }

    /**
     * 選択された Holder を Properties と Bind する.
     */
    private void bind(ShapeHolderBase h) {
        // RubberBand 前のプロパティーを保存
        prevLineWidth = properties.getLineWidth();
        prevLineColor = properties.getLineColor();
        prevFillColor = properties.getFillColor();
        prevFillBlur = properties.getFillBlur();
        prevFillMode = properties.getFillMode();
        prevPreviewState = properties.getPreviewState();

        // 選択された Holder のプロパティーを SchemaEditorProperties に逆セット
        properties.setLineWidth(h.getLineWidth());
        properties.setLineColor(h.getLineColor());
        properties.setFillColor(h.getFillColor());
        properties.setFillBlur(h.getFillBlur());
        properties.setFillMode(h.getFillMode());

        properties.setPreviewState(h.getState());

        // それから bind する
        h.bind();
    }

    private void unbind(ShapeHolderBase h) {
        h.unbind();
        // プロパティーを保存していたものに戻す
        properties.setLineWidth(prevLineWidth);
        properties.setLineColor(prevLineColor);
        properties.setFillColor(prevFillColor);
        properties.setFillBlur(prevFillBlur);
        properties.setFillMode(prevFillMode);

        properties.setPreviewState(prevPreviewState);
    }
}

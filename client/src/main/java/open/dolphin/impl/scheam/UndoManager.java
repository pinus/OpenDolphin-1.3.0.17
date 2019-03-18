package open.dolphin.impl.scheam;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import open.dolphin.impl.scheam.helper.ObservableDeque;
import open.dolphin.impl.scheam.shapeholder.ShapeHolderBase;
import open.dolphin.impl.scheam.undoevent.*;

/**
 * UndoManager.
 * CanvasLayer に DrawLayer を追加・削除する操作はリスナで監視して UndoLayerEvent を作る.
 * Holder を選択して色や線の太さを変える操作は Holder の valueChangingProperty を監視して UndoHolderEvent を作る.
 * Scale, Rotate, Clip 操作は offerScale, offerRotate, offerClip を呼んでもらって UndoEvent を作る.
 *
 * @author pns
 */
public class UndoManager {
    private final ObservableDeque<UndoEvent> undoQueue;
    private final ObservableDeque<UndoEvent> redoQueue;

    private final SchemaEditorImpl context;
    private final SchemaEditorProperties properties;
    private final StackPane canvasPane;

    private boolean isInRollback;

    public UndoManager(SchemaEditorImpl ctx) {
        context = ctx;
        canvasPane = context.getCanvasPane();
        properties = SchemaEditorImpl.getProperties();
        undoQueue = new ObservableDeque<>();
        redoQueue = new ObservableDeque<>();

        canvasPane.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            while (c.next()) {
                // Undo 作業中は UndoEvent を offer しない
                if (isInRollback) {
                    continue;
                }

                // DrawLayers の変化を検出して UndoEvent の offer　〜　新たな offer があった場合，redo は忘れる
                undoQueue.offerLast(new UndoLayerEvent(canvasPane, c));
                redoQueue.clear();

                // 新たな SchemaLayer が追加された場合，その Holder に Undo 用の Listener を追加
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(node -> {
                        ShapeHolderBase h = (ShapeHolderBase) ((SchemaLayer) node).getHolder();
                        h.valueChangingProperty().addListener(new ValueChangingListener(h));
                        h.lineWidthProperty().addListener(new LineWidthListener(h));
                    });
                }
            }
        });
    }

    /**
     * RotateEditor でここを呼ぶと Undo できる.
     *
     * @param r
     */
    public void offerRotate(double r) {
        UndoEvent undoEvent = new UndoRotateEvent(context, r);
        undoQueue.offerLast(undoEvent);
        redoQueue.clear();
    }

    /**
     * ScaleEditor でここを呼ぶと Undo できる.
     *
     * @param dx
     * @param dy
     */
    public void offerScale(double dx, double dy) {
        UndoEvent undoEvent = new UndoScaleEvent(context, dx, dy);
        undoQueue.offerLast(undoEvent);
        redoQueue.clear();
    }

    /**
     * ClipEditor で元の値でここを呼ぶと Undo できる.
     *
     * @param w
     * @param h
     * @param dx
     * @param dy
     */
    public void offerClip(double w, double h, double dx, double dy) {
        UndoEvent undoEvent = new UndoClipEvent(context, w, h, dx, dy);
        undoQueue.offerLast(undoEvent);
        redoQueue.clear();
    }

    /**
     * Undo: UndoEvent を undoQueue から取り出して rollback して redoQueue に積む.
     * 編集中の StateEditor はリセットする.
     */
    public void undo() {
        StateEditor editor = context.getStateManager().getStateEditor();
        editor.end();
        if (!undoQueue.isEmpty()) {
            rollback(undoQueue, redoQueue);
        }
        editor.start();
    }

    /**
     * Redo: UndoEvent を redoQueue から取り出して rollback して undoQueue に積む.
     * 編集中の StateEditor はリセットする.
     */
    public void redo() {
        StateEditor editor = context.getStateManager().getStateEditor();
        editor.end();
        if (!redoQueue.isEmpty()) {
            rollback(redoQueue, undoQueue);
        }
        editor.start();
    }

    /**
     * Recover queue から UndoEvent をとりだして再現する.
     * 現在の状態は StoreQueue に保存する.
     *
     * @param recover
     * @param store
     */
    private void rollback(ObservableDeque<UndoEvent> recover, ObservableDeque<UndoEvent> store) {
        isInRollback = true;

        UndoEvent undoEvent = recover.pollLast();
        undoEvent.rollback();
        store.offerLast(undoEvent);

        isInRollback = false;
    }

    public IntegerProperty undoQueueSizeProperty() {
        return undoQueue.sizeProperty();
    }

    public IntegerProperty redoQueueSizeProperty() {
        return redoQueue.sizeProperty();
    }

    public void clearQueue() {
        undoQueue.clear();
        redoQueue.clear();
    }

    /**
     * Holder の valueChangingProperty を監視して UndoEvent を作成して UndoQueue に積む Listener.
     * TranslateEditor での移動・拡大・回転，ColorPalette, PresetColorCombo でのパラメータ変更はまとめて変更になるので，
     * valueChangingProperty でひとかたまりにして処理する.
     */
    private class ValueChangingListener implements ChangeListener<Boolean> {
        private final ShapeHolderBase holder;
        private UndoEvent undoEvent;

        public ValueChangingListener(ShapeHolderBase h) {
            holder = h;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean endChanging, Boolean startChanging) {
            if (!isInRollback) {
                if (startChanging) {
                    // 変更前に Holder の UndoEvent を作っておく
                    undoEvent = new UndoHolderEvent(holder);

                } else if (endChanging) {
                    // 変更後は Holder は更新されているが，UndoEvent のパラメータは上記で保存されたものになっている
                    undoQueue.offerLast(undoEvent);
                    redoQueue.clear();
                }
            }
        }
    }

    /**
     * LineWidth の変更は単一の変更で，しかも LineWidthCombo とショートカットキーの両方から変更されるので
     * それぞれに valueChangingProperty を管理させるより lineWidthProperty を監視した方がわかりやすい.
     */
    private class LineWidthListener implements ChangeListener<Number> {
        private final ShapeHolderBase holder;

        public LineWidthListener(ShapeHolderBase h) {
            holder = h;
        }

        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
            // Undo 作業中，あるいは valueChanging の途中は UndoEvent を offer しない
            if (!isInRollback && !properties.valueChangingProperty().get()) {
                UndoLineWidthEvent undoEvent = new UndoLineWidthEvent(holder);
                // 変わる前の値をセット
                undoEvent.setPreviousLineWidth((double) oldValue);
                undoQueue.offerLast(undoEvent);
                redoQueue.clear();
            }
        }
    }
}

package open.dolphin.impl.scheam.undoevent;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import open.dolphin.impl.scheam.UndoEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pns
 */
public class UndoLayerEvent implements UndoEvent {
    private final StackPane canvasPane;
    private final List<Node> previousDrawLayers;


    public UndoLayerEvent(StackPane pane, ListChangeListener.Change<? extends Node> change) {
        canvasPane = pane;

        // 現在の List を Undo して保持する
        previousDrawLayers = new ArrayList<>();
        previousDrawLayers.addAll(change.getList());

        if (change.wasAdded()) {
            previousDrawLayers.removeAll(change.getAddedSubList());
        } else if (change.wasRemoved()) {
            previousDrawLayers.addAll(change.getFrom(), change.getRemoved());
        }
    }

    @Override
    public void rollback() {
        List<Node> tmp = new ArrayList<>();
        List<Node> drawLayers = canvasPane.getChildren();

        tmp.addAll(drawLayers);
        drawLayers.clear();
        drawLayers.addAll(previousDrawLayers);
        previousDrawLayers.clear();
        previousDrawLayers.addAll(tmp);
    }

}

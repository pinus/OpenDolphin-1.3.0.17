package open.dolphin.impl.scheam.widget;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author pns
 */
public class PnsButton extends Button {
    private Node icon;
    private Node selectedIcon;

    public PnsButton() {
        setFocusTraversable(false);

        setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                setGraphic(selectedIcon);
            }
        });
        setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                setGraphic(icon);
            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                setGraphic(icon);
            }
        });
    }

    public void setIcon(Node n) {
        icon = n;
        setGraphic(icon);
    }

    public void setSelectedIcon(Node n) {
        selectedIcon = n;
    }
}

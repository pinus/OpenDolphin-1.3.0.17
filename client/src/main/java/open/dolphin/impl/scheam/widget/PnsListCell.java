package open.dolphin.impl.scheam.widget;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

/**
 * Check Mark を付ける ListCell.
 * getCell を Override して使う
 *
 * @param <T>
 * @author pns
 */
public class PnsListCell<T> extends ListCell<T> {
    private final HBox box;
    private final Label checkMark;
    private final Label cell;

    public PnsListCell() {
        box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        checkMark = new Label();
        checkMark.getStyleClass().add("check-mark");
        cell = new Label();
        setGraphicTextGap(0);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setGraphic(null);

        } else {
            Label label = getCell(item);

            box.getChildren().clear();
            box.getChildren().add(checkMark);

            if (label.getGraphic() != null) {
                box.getChildren().add(label.getGraphic());
            }

            if (label.getText() != null) {
                setText(label.getText());
            }

            setGraphic(box);
        }
    }

    /**
     * ここをオーバーライドして使う.
     *
     * @param item
     * @return
     */
    public Label getCell(T item) {
        cell.setText(item.toString());
        return cell;
    }
}

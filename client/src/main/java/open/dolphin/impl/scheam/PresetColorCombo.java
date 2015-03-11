package open.dolphin.impl.scheam;

import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import open.dolphin.impl.scheam.constant.DefaultPresetColor;
import open.dolphin.impl.scheam.widget.PnsComboBox;
import open.dolphin.impl.scheam.widget.PnsListCell;

/**
 * Preset Color を選択するための PnsComboBox.
 * @author pns
 */
public class PresetColorCombo extends PnsComboBox<List<ColorModel>> {

    /** これを Listen すると選択された ColorModel がとれる */
    private final ObjectProperty<ColorModel> selectionProxyProperty;

    public PresetColorCombo() {
        super();
        setFocusTraversable(false);
        selectionProxyProperty = new SimpleObjectProperty<>();

        List<ColorModel> redFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.Red, FillMode.Fill);
        List<ColorModel> deepRedFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.DeepRed, FillMode.Fill);
        List<ColorModel> deeperRedFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.DeeperRed, FillMode.Fill);
        List<ColorModel> brownFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.Brown, FillMode.Fill);
        List<ColorModel> deepBrownFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.DeepBrown, FillMode.Fill);
        List<ColorModel> purpleFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.Purple, FillMode.Fill);
        List<ColorModel> deepPurpleFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.DeepPurple, FillMode.Fill);
        List<ColorModel> grayLine = DefaultPresetColor.getSeries(DefaultPresetColor.Series.Gray, FillMode.Line);
        List<ColorModel> redLine = DefaultPresetColor.getSeries(DefaultPresetColor.Series.Red, FillMode.Line);
        List<ColorModel> special0 = DefaultPresetColor.getSpecialSeries(0);
        List<ColorModel> special1 = DefaultPresetColor.getSpecialSeries(1);
        List<ColorModel> special2 = DefaultPresetColor.getSpecialSeries(2);

        getItems().add(redFill);
        getItems().add(deepRedFill);
        getItems().add(deeperRedFill);
        getItems().add(brownFill);
        getItems().add(deepBrownFill);
        getItems().add(purpleFill);
        getItems().add(deepPurpleFill);
        getItems().add(grayLine);
        getItems().add(redLine);
        getItems().add(special0);
        getItems().add(special1);
        getItems().add(special2);

        // MouseEvent で popup を show する
        setOnMousePressed(this::superShow);

        // アイコン callback
        Callback<ListView<List<ColorModel>>, ListCell<List<ColorModel>>> cellFactory = p -> new ColorCell();
        setCellFactory(cellFactory);

        // ComboBoxListViewSkin.updateButtonCell で setMouseTransparent(true) されてしまっているため，
        // ButtonCell は MouseEvent を受け付けることができない。しかも updateButtonCell は private。
        // 仕方がないので，むりやり false に戻す。
        ListCell<List<ColorModel>> buttonCell = new ColorCell();
        buttonCell.mouseTransparentProperty().addListener(o -> buttonCell.setMouseTransparent(false));
        setButtonCell(buttonCell);

        // 初期値
        getSelectionModel().select(redFill);
    }

    /**
     * ColorModel のリストから ListCell を作る.
     * ColorModelLabel を並べて作る.
     */
    private class ColorCell extends PnsListCell<List<ColorModel>> {
        private final HBox box;
        private final Label cell;

        public ColorCell() {
            box = new HBox();
            box.setSpacing(2);
            box.setAlignment(Pos.CENTER);
            cell = new Label();
        }

        @Override
        public Label getCell(List<ColorModel> list) {
            if (list == null) { return null; }

            box.getChildren().clear();
            list.forEach( model -> box.getChildren().add(new ColorModelLabel(model)));
            cell.setGraphic(box);

            return cell;
        }
    }

    /**
     * ColorModel を表示するラベル.
     * ColorCell はこれを HBox に並べたもの.
     * クリックで Property に ColorModel をセットする.
     */
    private class ColorModelLabel extends Rectangle {
        private ColorModel model;

        public ColorModelLabel(ColorModel m) {
            model = m;
            setWidth(20);
            setHeight(12);
            //setStrokeWidth(model.getLineWidth());
            setStrokeWidth(2);
            setStroke(model.getLineColor());
            if (model.getFillMode().equals(FillMode.Line)) {
                setFill(Color.TRANSPARENT);
            } else {
                setFill(model.getFillColor());
            }

            setOnMouseReleased(e -> selectionProxyProperty.set(model));
        }
    }

    @Override
    public void show() {
        // ActionEvent での popup はブロックする
        // MouseEvent で下の superPopup で popup する
    }

    /**
     * 親の show を呼ぶ.
     * ComboBox の右側の矢印を押したとき: popup を出す
     * ComboBox の ColorModeLabel を押したとき: ColorModelLabel にイベントを渡す
     * @param e
     */
    public void superShow(MouseEvent e) {
        // 右側の矢印をクリックした場合 popup する
        if (e.getX() > getWidth() - 14) {
            super.show();
        }
    }

    /**
     * ColorModel のプロパティ.
     * ColorModelLabel がクリックされるとセットされる
     * @return
     */
    public ObjectProperty<ColorModel> selectionProxyProperty() { return selectionProxyProperty; }

}

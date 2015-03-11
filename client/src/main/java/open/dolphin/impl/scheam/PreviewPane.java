package open.dolphin.impl.scheam;

import javafx.beans.binding.ObjectBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import open.dolphin.impl.scheam.colorchooser.ColorChooser;
import open.dolphin.impl.scheam.constant.ShapeIcon;
import open.dolphin.impl.scheam.shapeholder.OvalHolder;
import open.dolphin.impl.scheam.shapeholder.PolygonHolder;

/**
 * State，FillMode に合わせて Prevew を表示する.
 * ボタンになっていて，押すと ColorChooser を起動する.
 * @author pns
 */
public class PreviewPane extends StackPane {
    private static final double HEIGHT = 48;
    private static final double INSETS = 8;
    private SchemaEditorProperties properties;

    public PreviewPane() {
        properties = SchemaEditorImpl.getProperties();

        // サンプル表示用 Holder を用意
        final Shape penSample = ShapeIcon.getOpenPath();
        penSample.setScaleX(2); penSample.setScaleY(2);
        penSample.strokeWidthProperty().bind(properties.lineWidthProperty().divide(2.0));
        penSample.strokeProperty().bind(properties.lineColorProperty());

        final Shape lineSample = ShapeIcon.getLine();
        lineSample.setScaleX(2); lineSample.setScaleY(2);
        lineSample.strokeWidthProperty().bind(properties.lineWidthProperty().divide(2.0));
        lineSample.strokeProperty().bind(properties.lineColorProperty());

        final SchemaLayer ovalSample = new SchemaLayer();
        ovalSample.setWidth(HEIGHT); ovalSample.setHeight(HEIGHT);
        OvalHolder ovalHolder = new OvalHolder();
        ovalHolder.setStartX(INSETS); ovalHolder.setStartY(INSETS);
        ovalHolder.setEndX(HEIGHT-INSETS); ovalHolder.setEndY(HEIGHT-INSETS);
        ovalSample.setHolder(ovalHolder);
        ovalHolder.bind();
        ovalSample.draw();

        final SchemaLayer rectangleSample = new SchemaLayer();
        rectangleSample.setWidth(HEIGHT); rectangleSample.setHeight(HEIGHT);
        PolygonHolder rectangleHolder = new PolygonHolder();
        rectangleHolder.addPathX(INSETS); rectangleHolder.addPathY(INSETS);
        rectangleHolder.addPathX(HEIGHT-INSETS); rectangleHolder.addPathY(INSETS);
        rectangleHolder.addPathX(HEIGHT-INSETS); rectangleHolder.addPathY(HEIGHT-INSETS);
        rectangleHolder.addPathX(INSETS); rectangleHolder.addPathY(HEIGHT-INSETS);
        rectangleSample.setHolder(rectangleHolder);
        rectangleHolder.bind();
        rectangleSample.draw();

        final SchemaLayer polygonSample = new SchemaLayer();
        polygonSample.setWidth(HEIGHT); polygonSample.setHeight(HEIGHT);
        final PolygonHolder polygonHolder = new PolygonHolder();
        ShapeIcon.getPolygonPath(HEIGHT-INSETS*2, HEIGHT-INSETS*2).forEach(path -> {
            polygonHolder.addPathX(path.getX()+INSETS);
            polygonHolder.addPathY(path.getY()+INSETS);
        });
        polygonSample.setHolder(polygonHolder);
        polygonHolder.bind();
        polygonSample.draw();

        // preview button
        final Button button = new Button();
        button.setFocusTraversable(false);
        button.setPrefSize(HEIGHT, HEIGHT);
        button.setMinSize(HEIGHT, HEIGHT);
        button.setMaxSize(HEIGHT, HEIGHT);
        button.getStyleClass().add("schema-preview-button");

        // preview button を押したときの action
        final ColorChooser chooser = new ColorChooser();
        button.setOnAction(e -> chooser.show(button));

        // preview button の graphic を関連する properties に bind
        button.graphicProperty().bind(new ObjectBinding<Node>() {

            { super.bind(properties.lineWidthProperty(),
                    properties.lineColorProperty(),
                    properties.fillColorProperty(),
                    properties.fillBlurProperty(),
                    properties.fillModeProperty(),
                    properties.previewStateProperty());
            }

            @Override
            protected Node computeValue() {

                switch(properties.getPreviewState()) {
                    case Pen:
                        return penSample;
                    case Line:
                        return lineSample;
                    case Oval:
                        ovalSample.redraw();
                        return ovalSample;
                    case Rectangle:
                        rectangleSample.redraw();
                        return rectangleSample;
                    case Polygon:
                    case Dots:
                    case Net:
                    default:
                        polygonSample.redraw();
                        return polygonSample;
                }
            }
        });

        getChildren().add(button);
    }
}

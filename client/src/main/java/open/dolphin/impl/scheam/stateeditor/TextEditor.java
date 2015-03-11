package open.dolphin.impl.scheam.stateeditor;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.shapeholder.TextHolder;

/**
 * Text 入力用の StateEditor.
 * @author pns
 */
public class TextEditor extends StateEditorBase {

    private static final double TEXTFIELD_WIDTH = 100;

    // 大元の Stage の Content を入れる Pane
    // TextField はここに出すことで，DraftLayer より前に置くことができる
    private final StackPane contentPane;
    // 新しい Holder を入れる Pane
    private final StackPane canvasPane;

    private final SchemaLayer draftLayer;
    private final SchemaLayer baseLayer;
    private final TextField textField;
    private double startx, starty;

    public TextEditor(SchemaEditorImpl context) {
        contentPane = context.getContentPane();
        canvasPane = context.getCanvasPane();
        draftLayer = context.getDraftLayer();
        baseLayer = context.getBaseLayer();

        textField = new TextField();
        StackPane.setAlignment(textField, Pos.TOP_LEFT);
        textField.setMaxWidth(TEXTFIELD_WIDTH);

        textField.setOnKeyPressed(this::keyPressed);
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if (!contentPane.getChildren().contains(textField)) {
            contentPane.getChildren().add(textField);
        }

        startx = e.getX();
        starty = e.getY();
        // textField の位置は margine で設定
        Point2D p = draftLayer.localToParent(e.getX(), e.getY());
        StackPane.setMargin(textField, new Insets(p.getY(), 0, 0, p.getX()));

        textField.requestFocus();

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseUp(MouseEvent e) {
    }

    @Override
    public void end() {
        if (textField != null) {
            contentPane.getChildren().remove(textField);
            textField.setText(null);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getCode()) {
            case ESCAPE:
                end();
                break;

            case ENTER:
                // 新しい Layer を加える
                // 他の StateEditor の場合は，
                // StateManager で MouseReleased でこの仕事をしている
                TextHolder holder = new TextHolder();
                holder.setStartX(startx);
                holder.setStartY(starty);
                holder.setProperties();
                // setText で TextHolder で終点の path が設定される
                holder.setText(textField.getText());
                SchemaLayer layer = new SchemaLayer();
                layer.widthProperty().bind(baseLayer.widthProperty());
                layer.heightProperty().bind(baseLayer.heightProperty());
                // Holder セット
                layer.setHolder(holder);
                layer.draw();
                canvasPane.getChildren().add(layer);

                end();
                break;
        }
    }

    @Override
    public ShapeHolder getHolder() {
        return null;
    }
}

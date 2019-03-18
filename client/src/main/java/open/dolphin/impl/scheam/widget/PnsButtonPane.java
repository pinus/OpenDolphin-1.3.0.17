package open.dolphin.impl.scheam.widget;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * OK, Cancel, Option ボタンを載せる HBox.
 * 各ボタンは setButtonText すると visible になる.
 * Option ボタンが左側，スペーサーを挟んで右側に Cancel ボタン，OK ボタンというレイアウト.
 *
 * @author pns
 */
public class PnsButtonPane extends HBox {
    private final Button okButton;
    private final Button cancelButton;
    private final Button optionButton;

    public PnsButtonPane() {
        setSpacing(10);

        okButton = new Button();
        okButton.setDefaultButton(true);
        okButton.setFocusTraversable(false);
        okButton.setVisible(false);

        cancelButton = new Button();
        cancelButton.setFocusTraversable(false);
        cancelButton.setVisible(false);

        optionButton = new Button();
        optionButton.setFocusTraversable(false);
        optionButton.setVisible(false);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        getChildren().addAll(optionButton, spacer, cancelButton, okButton);
    }

    /**
     * OK ボタンのテキストを設定する
     *
     * @param text
     */
    public void setOkButtonText(String text) {
        okButton.setText(text);
        okButton.setVisible(true);
    }

    /**
     * Cancel ボタンのテキストを設定する
     *
     * @param text
     */
    public void setCancelButtonText(String text) {
        cancelButton.setText(text);
        cancelButton.setVisible(true);
    }

    /**
     * Option ボタンのテキストを設定する
     *
     * @param text
     */
    public void setOptionButtonText(String text) {
        optionButton.setText(text);
        optionButton.setVisible(true);
    }

    /**
     * OK ボタンの enable/disable
     *
     * @param b
     */
    public void setOkButtonDisable(boolean b) {
        okButton.setDisable(b);
    }

    /**
     * Cancel ボタンの enable/disable
     *
     * @param b
     */
    public void setCancelButtonDisable(boolean b) {
        cancelButton.setDisable(b);
    }

    /**
     * Option ボタンの enable/disable
     *
     * @param b
     */
    public void setOptionButtonDisable(boolean b) {
        optionButton.setDisable(b);
    }

    /**
     * OK ボタンの EventHandler を登録
     *
     * @param handler
     */
    public void setOnOK(EventHandler<ActionEvent> handler) {
        okButton.setOnAction(handler);
    }

    /**
     * Cancel ボタンの EventHandler を登録
     *
     * @param handler
     */
    public void setOnCancel(EventHandler<ActionEvent> handler) {
        cancelButton.setOnAction(handler);
    }

    /**
     * Option ボタンの EventHandler を登録
     *
     * @param handler
     */
    public void setOnOption(EventHandler<ActionEvent> handler) {
        optionButton.setOnAction(handler);
    }
}

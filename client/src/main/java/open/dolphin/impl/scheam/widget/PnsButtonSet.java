package open.dolphin.impl.scheam.widget;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import open.dolphin.impl.scheam.constant.StyleClass;

/**
 *
 * @author pns
 */
public class PnsButtonSet extends HBox {
    private static final double HEIGHT = 22;
    private static final double BUTTON_WIDTH = 28;

    private ObservableList<Button> buttonList;

    public PnsButtonSet() {
        this(BUTTON_WIDTH, HEIGHT);
    }

    public PnsButtonSet(final double w, final double h) {
       getStyleClass().add(StyleClass.PNS_TOGGLE_SET);
       setPrefHeight(h); setMinHeight(h); setMaxHeight(h);
       setAlignment(Pos.CENTER);

       buttonList = FXCollections.observableArrayList();

        buttonList.addListener(new ListChangeListener<Button>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends Button> change) {
                int n = buttonList.size();
                Button b;
                Pane separator;

                if (n == 1) {
                    b = buttonList.get(0);
                    b.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_SINGLE);
                    b.setPrefSize(w, h+1); b.setMaxSize(w, h); b.setMinSize(w, h);
                    getChildren().add(b);

                } else if (n > 1) {
                    b = buttonList.get(0);
                    b.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_LEFT);
                    b.setPrefSize(w, h+1); b.setMaxSize(w, h); b.setMinSize(w, h);

                    separator = new Pane();
                    separator.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_SEPARATOR);
                    separator.setPrefSize(1, h-6); separator.setMinSize(1, h-6); separator.setMaxSize(1, h-6);

                    getChildren().add(b);
                    getChildren().add(separator);

                    if (n > 2) {
                        for (int i=1; i<n-1; i++) {
                            b = buttonList.get(i);
                            b.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_MIDDLE);
                            b.setPrefSize(w, h+1); b.setMaxSize(w, h); b.setMinSize(w, h);

                            separator = new Pane();
                            separator.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_SEPARATOR);
                            separator.setPrefSize(1, h-6); separator.setMinSize(1, h-6); separator.setMaxSize(1, h-6);

                            getChildren().add(b);
                            getChildren().add(separator);
                        }
                    }

                    b = buttonList.get(n-1);
                    b.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_RIGHT);
                    b.getStyleClass().add(StyleClass.PNS_TOGGLE_SET_LEFT);
                    b.setPrefSize(w, h+1); b.setMaxSize(w, h); b.setMinSize(w, h);

                    getChildren().add(b);
                }
            }
        });
    }

    public ObservableList<Button> getButtonList() {
        return buttonList;
    }

    //ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

/*
    public static void main(String[] s) {
        // Mac OS X needs this to avoid HeadlessException
        System.setProperty("java.awt.headless", "false");

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                final JFrame frame = new JFrame();
                frame.setUndecorated(false);
                final JFXPanel fxp = new JFXPanel();
                frame.add(fxp);

                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        HBox pane = new HBox();
                        pane.setPrefSize(150, 100);

                        PnsButton b1 = new PnsButton();
                        Shape clearIcon = ShapeIcon.getClear();
                        Shape clearSelectedIcon = ShapeIcon.getClear();
                        clearSelectedIcon.setStroke(Const.PNS_WHITE);
                        b1.setIcon(clearIcon);
                        b1.setSelectedIcon(clearSelectedIcon);

                        PnsButton b2 = new PnsButton();
                        Shape undoIcon = ShapeIcon.getUndo();
                        Shape undoSelectedIcon = ShapeIcon.getUndo();
                        undoSelectedIcon.setStroke(Const.PNS_WHITE);
                        undoSelectedIcon.setFill(Const.PNS_WHITE);
                        b2.setIcon(undoIcon);
                        b2.setSelectedIcon(undoSelectedIcon);

                        PnsButton b3 = new PnsButton();
                        Shape redoIcon = ShapeIcon.getRedo();
                        Shape redoSelectedIcon = ShapeIcon.getRedo();
                        redoSelectedIcon.setStroke(Const.PNS_WHITE);
                        redoSelectedIcon.setFill(Const.PNS_WHITE);
                        b3.setIcon(redoIcon);
                        b3.setSelectedIcon(redoSelectedIcon);

                        PnsButtonSet set = new PnsButtonSet();
                        //set.getButtonList().addAll(b1, b2, b3);
                        set.getButtonList().addAll(b1);

                        pane.getChildren().add(set);
                        Scene scene = new Scene(pane);
                        scene.getStylesheets().add(StyleClass.CSS_FILE);
                        fxp.setScene(scene);
                    }
                });

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
    */
}

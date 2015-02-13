package open.dolphin.impl.scheam;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import open.dolphin.infomodel.ExtRefModel;
import open.dolphin.infomodel.SchemaModel;

/**
 *
 * @author pns
 */
public class DrawTest {

    // ******************** JFXPanel version *******************
    public static void main (String[] arg) {
        new DrawTest().start();
    }

    public void start() {
        // Mac OS X needs this to avoid HeadlessException
        System.setProperty("java.awt.headless", "false");

        // necessary to initialize Toolkit
        new JFXPanel();

        Platform.runLater(new Runnable(){
            @Override
            public void run() {

                final SchemaEditorImpl editor = new SchemaEditorImpl();

                SchemaModel schema = new SchemaModel();
                String sample1 = "/open/dolphin/impl/scheam/resources/Sample-square.JPG";
                String sample2 = "/open/dolphin/impl/scheam/resources/Sample-large.JPG";
                String sample3 = "/open/dolphin/impl/scheam/resources/Sample-landscape.JPG";
                String sample4 = "/open/dolphin/impl/scheam/resources/Sample-portrait.JPG";

                InputStream in = getClass().getResourceAsStream(sample2);

                byte[] buf = null;
                try {
                    int n = in.available();
                    buf = new byte[n];
                    for(int i=0; i<n; i++) buf[i] = (byte) in.read();
                } catch (IOException ex) {
                }
                schema.setIcon(new ImageIcon(buf));

                ExtRefModel ref = new ExtRefModel();
                ref.setContentType("image/jpeg");
                ref.setTitle("Schema Image");
                schema.setExtRef(ref);
                schema.setFileName("Test");
                ref.setHref("Test");

                editor.addPropertyChangeListener(new PropertyChangeListener(){
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        System.out.println("oldValue = " + evt.getOldValue());
                        System.out.println("newValue = " + evt.getNewValue());
                        if (evt.getNewValue() != null) {
                            // 送られてきた画像を Swing で表示
                            SchemaModel m = (SchemaModel) evt.getNewValue();
                            ImageIcon icon = m.getIcon();
                            JLabel label = new JLabel(icon);
                            JFrame f = new JFrame();
                            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            f.add(label);
                            f.pack();
                            f.setVisible(true);
                        }
                    }
                });

                editor.setSchema(schema);
                editor.setEditable(true);
                editor.start();
            }
        });
    }
}

/*
public class DrawTest extends Application {

    // ******************  JavaFX version  ***********************
    public static void main (String[] arg) {
        Application.launch(arg);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SchemaEditorImpl editor = new SchemaEditorImpl();

        SchemaModel schema = new SchemaModel();
        String sample1 = "/open/dolphin/impl/scheam/resources/Sample-square.JPG";
        String sample2 = "/open/dolphin/impl/scheam/resources/Sample-large.JPG";
        String sample3 = "/open/dolphin/impl/scheam/resources/Sample-landscape.JPG";
        String sample4 = "/open/dolphin/impl/scheam/resources/Sample-portrait.JPG";

        InputStream in = getClass().getResourceAsStream(sample2);

        byte[] buf = null;
        try {
            int n = in.available();
            buf = new byte[n];
            for(int i=0; i<n; i++) buf[i] = (byte) in.read();
        } catch (IOException ex) {
        }
        schema.setIcon(new ImageIcon(buf));

        ExtRefModel ref = new ExtRefModel();
        ref.setContentType("image/jpeg");
        ref.setTitle("Schema Image");
        schema.setExtRef(ref);
        schema.setFileName("Test");
        ref.setHref("Test");

        editor.addPropertyChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("oldValue = " + evt.getOldValue());
                System.out.println("newValue = " + evt.getNewValue());
            }
        });

        editor.setSchema(schema);
        editor.setEditable(true);
        editor.start();
    }
}
*/

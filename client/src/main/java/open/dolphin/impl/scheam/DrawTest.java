package open.dolphin.impl.scheam;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.ImageIcon;
import open.dolphin.infomodel.ExtRefModel;
import open.dolphin.infomodel.SchemaModel;

/**
 *
 * @author pns
 */
public class DrawTest extends Application {

    // ******************  JavaFX version  ***********************
    public static void main (String[] arg) {
        Application.launch(arg);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SchemaEditorImpl editor = new SchemaEditorImpl();

        SchemaModel schema = new SchemaModel();
        String sample1 = "/schemaeditor/Sample-square.JPG";
        String sample2 = "/schemaeditor/Sample-large.JPG";
        String sample3 = "/schemaeditor/Sample-landscape.JPG";
        String sample4 = "/schemaeditor/Sample-portrait.JPG";

        InputStream in = getClass().getResourceAsStream(sample1);

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

        editor.addPropertyChangeListener(evt -> {
            System.out.println("oldValue = " + evt.getOldValue());
            System.out.println("newValue = " + evt.getNewValue());
        });

        editor.setSchema(schema);
        editor.setEditable(true);
        editor.start();
    }
}

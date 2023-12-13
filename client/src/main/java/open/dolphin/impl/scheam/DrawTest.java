package open.dolphin.impl.scheam;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import open.dolphin.infomodel.ExtRefModel;
import open.dolphin.infomodel.SchemaModel;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author pns
 */
public class DrawTest {

    // ******************  JavaFX version  ***********************
    public static void main(String[] arg) {
        System.out.println("version = " + com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());

        // JavaFX settings
        // Mac OS X needs this to avoid HeadlessException
        System.setProperty("java.awt.headless", "false");
        // necessary to initialize JavaFX Toolkit
        new JFXPanel();
        // JavaFX thread が SchemaEditor 終了後に shutdown してしまわないようにする
        Platform.setImplicitExit(false);
        // JavaFX ClassLoader
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println("ClassLoader in Swing Thread = " + contextClassLoader);

        Platform.runLater(() -> {
            ClassLoader jfx = Thread.currentThread().getContextClassLoader();
            System.out.println("ClassLoader in JFX Thread = " + jfx);
            if (jfx == null) {
                System.out.println("ClassLoader in JFX Thread is null; set " + contextClassLoader + " instead.");
                Thread.currentThread().setContextClassLoader(contextClassLoader);
            }
        });

        Platform.runLater(new DrawTest()::start);
        JOptionPane.showMessageDialog(null, "Keep this window to keep thread active");
    }

    public void start() {
        SchemaEditorImpl editor = new SchemaEditorImpl();

        SchemaModel schema = new SchemaModel();
        String sample1 = "/schemaeditor/Sample-square.JPG";
        String sample2 = "/schemaeditor/Sample-large.JPG";
        String sample3 = "/schemaeditor/Sample-landscape.JPG";
        String sample4 = "/schemaeditor/Sample-portrait.JPG";

        byte[] buf;
        try (InputStream in = getClass().getResourceAsStream(sample1)) {
            int n = in.available();
            buf = new byte[n];
            for (int i = 0; i < n; i++) buf[i] = (byte) in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
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

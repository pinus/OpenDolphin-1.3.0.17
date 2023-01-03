package open.dolphin.ui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import open.dolphin.client.Dolphin;
import open.dolphin.laf.*;

import javax.swing.*;
import java.awt.*;

/**
 * Mac 用のセッティング.
 *
 * @author pns
 */
public class SettingForMac {
    private SettingForMac() { }

    public static void set(final Dolphin context) {

        // apple settings
        System.setProperty("apple.laf.useScreenMenuBar", "true"); // ClientContextStub でも設定しているが，そこだと遅いようだ
        // com.apple.eawt.Application の設定
        setMacApplication(context);

        // laf replacement
        UIManager.put("TextFieldUI", MyTextFieldUI.class.getName());
        UIManager.put("PasswordFieldUI", MyPasswordFieldUI.class.getName());
        UIManager.put("TableUI", MyTableUI.class.getName());
        UIManager.put("ListUI", MyListUI.class.getName());
        UIManager.put("TreeUI", MyTreeUI.class.getName());
        //UIManager.put("OptionPane.background", UIHelper.DEFAULT_TITLE_BACKGROUND_COLOR);
        //UIManager.put("Panel.background", UIHelper.DEFAULT_TITLE_BACKGROUND_COLOR);

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
    }

    private static void setMacApplication(final Dolphin context) {
        // ...について
        Desktop.getDesktop().setAboutHandler(ae -> context.showAbout());
        // 終了
        Desktop.getDesktop().setQuitHandler((qe, qr) -> {
            context.processExit();
            qr.cancelQuit();
        });
        // 環境設定
        Desktop.getDesktop().setPreferencesHandler(pe -> context.doPreference());
    }
}

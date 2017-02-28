package open.dolphin.ui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javax.swing.UIManager;
import open.dolphin.client.Dolphin;
import open.dolphin.laf.MyButtonUI;
import open.dolphin.laf.MyComboBoxUI;
import open.dolphin.laf.MyListUI;
import open.dolphin.laf.MyPasswordFieldUI;
import open.dolphin.laf.MyTableUI;
import open.dolphin.laf.MyTextFieldUI;
import open.dolphin.laf.MyToggleButtonUI;
import open.dolphin.laf.MyTreeUI;

/**
 * Mac 用のセッティング.
 * @author pns
 */
public class SettingForMac {
    private SettingForMac() {}

    public static void set(final Dolphin context) {
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
            if (jfx == null) {
                System.out.println("ClassLoader in JFX Thread is null; set " + contextClassLoader + " instead.");
                Thread.currentThread().setContextClassLoader(contextClassLoader);
            }
        });

        // apple settings
        System.setProperty("apple.laf.useScreenMenuBar","true"); // ClientContextStub でも設定しているが，そこだと遅いようだ
        // Look and Feel のセットアップ
        setLaf(context);
        // com.apple.eawt.Application の設定
        setMacApplication(context);

        TouchpadTest.startListening();
    }

    public static void setLaf(Dolphin context) {
        UIManager.put("TextFieldUI", MyTextFieldUI.class.getName());
        UIManager.put("PasswordFieldUI", MyPasswordFieldUI.class.getName());
        UIManager.put("TableUI", MyTableUI.class.getName());
        UIManager.put("ListUI", MyListUI.class.getName());
        UIManager.put("TreeUI", MyTreeUI.class.getName());
        UIManager.put("ButtonUI", MyButtonUI.class.getName());
        UIManager.put("ToggleButtonUI", MyToggleButtonUI.class.getName());
        UIManager.put("ComboBoxUI", MyComboBoxUI.class.getName());
    }

    private static void setMacApplication(final Dolphin context) {

        // Mac Application Menu
        com.apple.eawt.Application fApplication = com.apple.eawt.Application.getApplication();

        // ...について
        fApplication.setAboutHandler(ae -> context.showAbout());
        // 終了
        fApplication.setQuitHandler((qe, qr) -> {
            context.processExit();
            qr.cancelQuit();
        });
        // 環境設定
        fApplication.setPreferencesHandler(pe -> context.doPreference());
    }
}

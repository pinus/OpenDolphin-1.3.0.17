package open.dolphin.ui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import open.dolphin.client.Dolphin;
import open.dolphin.laf.*;

import javax.swing.*;

/**
 * Mac 用のセッティング.
 * @author pns
 */
public class SettingForMac {
    private SettingForMac() {}

    public static void set(final Dolphin context) {

        // apple settings
        System.setProperty("apple.laf.useScreenMenuBar","true"); // ClientContextStub でも設定しているが，そこだと遅いようだ
        // com.apple.eawt.Application の設定
        setMacApplication(context);

        // Look and Feel のセットアップ
        try {
            UIManager.setLookAndFeel(new MyLookAndFeel());

        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace(System.err);
        }

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

        TouchpadTest.startListening();
    }

    private static class MyLookAndFeel extends com.apple.laf.AquaLookAndFeel {
        private static final long serialVersionUID = 1L;

        @Override
        public UIDefaults getDefaults() {
            UIDefaults uiDefaults = super.getDefaults();

            uiDefaults.putDefaults(new Object[] {
                "TextFieldUI", MyTextFieldUI.class.getName(),
                "PasswordFieldUI", MyPasswordFieldUI.class.getName(),
                "TableUI", MyTableUI.class.getName(),
                "ListUI", MyListUI.class.getName(),
                "TreeUI", MyTreeUI.class.getName(),
                "ButtonUI", MyButtonUI.class.getName(),
                "ToggleButtonUI", MyToggleButtonUI.class.getName(),
                "ComboBoxUI", MyComboBoxUI.class.getName(),
            });

            return uiDefaults;
        }
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

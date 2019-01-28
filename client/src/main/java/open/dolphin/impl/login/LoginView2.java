package open.dolphin.impl.login;

import open.dolphin.client.GUIConst;
import open.dolphin.ui.CompletableJTextField;

import javax.swing.*;

public class LoginView2 extends JFrame {

    private JButton loginBtn;
    private JButton cancelBtn;
    private JButton settingBtn;

    private CompletableJTextField hostField;
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JCheckBox savePasswordCbx;

    private ImageIcon splash = GUIConst.ICON_SPLASH_DOLPHIN;
    private ImageIcon splash2 = GUIConst.ICON_SPLASH_USAGI;


    public LoginView2() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);

        initComponents();
    }

    private void initComponents() {



    }

}


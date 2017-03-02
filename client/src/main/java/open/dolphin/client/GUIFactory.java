package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import open.dolphin.ui.PNSBorderFactory;

/**
 *
 * @author Kazushi Minagawa Digital Globe, Inc.
 */
public class GUIFactory {

    private static final int BUTTON_GAP 		= 5;
    //private static final int LABEL_ITEM_GAP 		= 7;
    private static final int TF_MARGIN_TOP  		= 0;
    private static final int TF_MARGIN_LEFT  		= 0;
    private static final int TF_MARGIN_BOTTOM  		= 0;
    private static final int TF_MARGIN_RIGHT  		= 0;
    private static final int TF_LENGTH          	= 30;
    private static final int TF_HEIGHT                  = 26;
    private static final int TITLE_SPACE_TOP  		= 6;
    private static final int TITLE_SPACE_LEFT  		= 6;
    private static final int TITLE_SPACE_BOTTOM 	= 5;
    private static final int TITLE_SPACE_RIGHT  	= 5;
    private static final Color DROP_OK_COLOR = new Color(0, 12, 156);

    public static Font createSmallFont() {
        return new Font("Dialog", Font.PLAIN, 10);
    }

    public static JButton createOkButton() {
        return new JButton((String)UIManager.get("OptionPane.okButtonText"));
    }

    public static JButton createCancelButton() {
        return new JButton((String)UIManager.get("OptionPane.cancelButtonText"));
    }

    public static JButton createButton(String text, String mnemonic, ActionListener al) {
        JButton ret = new JButton(text);
        if (al != null) { ret.addActionListener(al); }
        return ret;
    }

    public static JRadioButton createRadioButton(String text, ActionListener al, ButtonGroup bg) {
        JRadioButton radio = new JRadioButton(text);
        if (al != null) { radio.addActionListener(al); }
        if (bg != null) { bg.add(radio); }
        return radio;
    }

    public static JCheckBox createCheckBox(String text, ActionListener al) {
        JCheckBox ret = new JCheckBox(text);
        if (al != null) { ret.addActionListener(al); }
        return ret;
    }

    public static JTextField createTextField(int val, Insets margin, FocusListener fl, DocumentListener dl) {
        JTextField tf = new JTextField(val==0? TF_LENGTH : val);
        Dimension d = tf.getPreferredSize();
        tf.setPreferredSize(new Dimension(d.width, TF_HEIGHT));
        tf.setMinimumSize(new Dimension(36,TF_HEIGHT));
        tf.setMargin(margin==null? new Insets(TF_MARGIN_TOP, TF_MARGIN_LEFT, TF_MARGIN_BOTTOM, TF_MARGIN_RIGHT) : margin);
        if (dl != null) { tf.getDocument().addDocumentListener(dl); }
        if (fl != null) { tf.addFocusListener(fl); }
        return tf;
    }

    public static JPasswordField createPassField(int val, Insets margin, FocusListener fa, DocumentListener dl) {
        JPasswordField pf = new JPasswordField(val==0? TF_LENGTH : val);
        Dimension d = pf.getPreferredSize();
        pf.setPreferredSize(new Dimension(d.width, TF_HEIGHT));
        pf.setMinimumSize(new Dimension(36,TF_HEIGHT));
        pf.setMargin(margin==null? new Insets(TF_MARGIN_TOP, TF_MARGIN_LEFT, TF_MARGIN_BOTTOM, TF_MARGIN_RIGHT) : margin);
        if (dl != null) { pf.getDocument().addDocumentListener(dl); }
        if (fa != null) { pf.addFocusListener(fa); }
        return pf;
    }

    /**
     * FlowLayout にボタンを配置したパネルを生成する.
     * @param btns 配置する Button の配列
     * @param align 配置する方向（FlowLayout.RIGHT/LEFT）
     * @return 5 ピクセル間隔でボタンが配置されたパネル
     */
    public static JPanel createButtonPanel(JButton[] btns, int align) {
        JPanel p = new JPanel(new FlowLayout(align, BUTTON_GAP, 0));
        for (JButton btn : btns) { p.add(btn); }
        return p;
    }

    /**
     * 右づめにボタンを配置したパネルを生成する.
     * @param btns 配置する Button の配列
     * @return ボタンが配列されたパネル（左に水平 Glue，右はマージンなし）
     */
    public static JPanel createCommandButtonPanel(JButton[] btns) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(Box.createHorizontalGlue());
        p.add(btns[0]);
        for (int i = 1; i < btns.length; i++) {
            p.add(Box.createHorizontalStrut(BUTTON_GAP));
            p.add(btns[i]);
        }
        return p;
    }

    public static JPanel createRadioPanel(JRadioButton[] rbs) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, BUTTON_GAP, 0));
        for (JRadioButton rb : rbs) { p.add(rb); }
        return p;
    }

    public static JPanel createCheckBoxPanel(JCheckBox[] boxes) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,BUTTON_GAP, 0));
        for (JCheckBox boxe : boxes) { p.add(boxe); }
        return p;
    }

    public static JPanel createTitledPanel(JComponent c, String title) {
        c.setBorder(BorderFactory.createEmptyBorder(TITLE_SPACE_TOP, TITLE_SPACE_LEFT, TITLE_SPACE_BOTTOM, TITLE_SPACE_RIGHT));
        JPanel p = new JPanel(new BorderLayout());
        p.add(c, BorderLayout.CENTER);
        p.setBorder(PNSBorderFactory.createTitledBorder(title));
        return p;
    }

    public static JPanel createZipCodePanel(JTextField tf1, JTextField tf2) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        p.add(tf1);
        p.add(new JLabel(" - "));
        p.add(tf2);
        return p;
    }

    public static JPanel createPhonePanel(JTextField tf1, JTextField tf2, JTextField tf3) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
        p.add(tf1);
        p.add(new JLabel(" - "));
        p.add(tf2);
        p.add(new JLabel(" - "));
        p.add(tf3);
        return p;
    }

    public static JScrollPane createVScrollPane(JComponent c) {
        JScrollPane scroller = new JScrollPane(c);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroller;
    }

    public static JScrollPane createHScrollPane(JComponent c) {
        JScrollPane scroller = new JScrollPane(c);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scroller;
    }

    public static JPanel createZeroPanel(JComponent jc) {
        JPanel ret = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        ret.add(jc);
        return ret;
    }

    public static String getCancelButtonText() {
        return (String) UIManager.get("OptionPane.cancelButtonText");
    }

    public static Point getCenterLoc(int width, int height) {
        Dimension screen = Toolkit.getDefaultToolkit ().getScreenSize ();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height ) / 3;
        return new Point(x, y);
    }

    public static Color getDropOkColor() {
        return DROP_OK_COLOR;
    }

    /**
     * Slider のパネルを作る
     * @param min
     * @param max
     * @param initValue
     * @return
     */
    public static JPanel createSliderPanel(int min, int max, final int initValue) {
        JPanel ret = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        // スライダー
        final JSlider slider = new JSlider(min, max, initValue);
        slider.setFont(new Font("SansSerif", Font.PLAIN, 9));

        // スピナー
        SpinnerModel fetchModel = new SpinnerNumberModel(initValue, min, max, 1);
        final JSpinner spinner = new JSpinner(fetchModel);
        spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));

        // お互いにリスン
        slider.addChangeListener(e -> spinner.setValue(slider.getValue()));
        spinner.addChangeListener(e -> slider.setValue((Integer)spinner.getValue()));

        ret.add(slider);
        ret.add(spinner);

        return ret;
    }
}

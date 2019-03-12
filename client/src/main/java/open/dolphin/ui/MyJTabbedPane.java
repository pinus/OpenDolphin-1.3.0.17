package open.dolphin.ui;

import javax.swing.*;
import java.awt.*;

/**
 * quaqua で JTabbedPane を隙間無くレイアウトする
 * @author pinus
 */
public class MyJTabbedPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;

    public static final boolean PACK = true;
    public static final boolean UNPACK = false;

    private static final Insets EMPTY_MARGIN = new Insets(0,0,0,0);
    private Insets visualMargin;
    private Insets systemVisualMargin;
    private int tabLayoutPolicy;

    /**
     * JTabbedPane の回りに全く border を付けない
     */
    public MyJTabbedPane() {
        this(EMPTY_MARGIN);
    }

    public MyJTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        this(EMPTY_MARGIN);
        this.setTabLayoutPolicy(tabLayoutPolicy);
        this.setTabPlacement(tabPlacement);
        this.tabLayoutPolicy = tabLayoutPolicy;
    }

    /**
     * JTabbedPane の回りに visual margin だけマージンが付く
     * デフォルトと同じにするには，visualMargin を Insets(3,3,3,3) とする
     * @param visualMargin
     */
    public MyJTabbedPane(Insets visualMargin) {
        super();
        putClientProperty("Quaqua.TabbedPane.contentBorderPainted", false);
        putClientProperty("Quaqua.Component.visualMargin", visualMargin);
        this.visualMargin = visualMargin;
        this.tabLayoutPolicy = UIManager.getInt("TabbedPane.tabLayoutPolicy");
        this.systemVisualMargin = UIManager.getInsets("Component.visualMargin");
    }

    @Override
    public void addTab(String title, Component component) {
        if (component instanceof JComponent) {
            addTab(title, (JComponent) component, true);
        } else {
            super.addTab(title, component);
        }
    }

    public void addTab(String title, JComponent component, boolean pack) {
        if (pack) {
            component.putClientProperty("Quaqua.TabbedPaneChild.contentInsets", getContentInsetsToFit());
            //component.setBorder(EMPTY_BORDER);
        }
        super.addTab(title, component);
    }

    public void addTab(String title, JComponent component, int index) {
        addTab(title, component, index, true);
    }

    public void addTab(String title, JComponent component, int index, boolean pack) {
        if (pack) {
            component.putClientProperty("Quaqua.TabbedPaneChild.contentInsets", getContentInsetsToFit());
            //component.setBorder(EMPTY_BORDER);
        }
        super.add(component, title, index);
    }

    private Insets getContentInsetsToFit() {
        if (tabLayoutPolicy == JTabbedPane.WRAP_TAB_LAYOUT && systemVisualMargin != null) {
            return new Insets(
                    visualMargin.top - systemVisualMargin.top,
                    visualMargin.left - systemVisualMargin.left,
                    visualMargin.bottom - systemVisualMargin.bottom,
                    visualMargin.right - systemVisualMargin.right);
        }

        return visualMargin;
    }
}

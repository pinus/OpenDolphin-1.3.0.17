package open.dolphin.ui;

import com.apple.eawt.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import open.dolphin.client.GUIConst;

/**
 * Command Panel，Status Panel のベースになるパネル.
 * 横長のパネルで，左から要素を詰めていく
 * Window のタイトルバーと連続するように active/deactive に合わせて Background 色を変える
 * @author pns
 */
public class HorizontalPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int SEPARATOR_WIDTH = 16;
    private static final int DEFAULT_PANEL_HEIGHT = 24;

    /** パネルの高さ */
    private int panelHeight = DEFAULT_PANEL_HEIGHT;
    /** フォントサイズ */
    private int fontSize = 0;
    /** 文字列を挿入した場合は，後からそこに setText できるようにする */
    private final HashMap<String, JLabel> labelMap = new HashMap<>();
    /** 親の Window */
    private Window parent = null;
    /** アプリケーションとして前面に居るか後面に回っているか */
    private boolean isAppForeground = true;

    public HorizontalPanel() {
        initComponent();
    }

    private void initComponent() {
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(GUIConst.BACKGROUND_OFF_FOCUS);

        // アプリケーションとして前面にいるかどうかをセットする
        // https://coderanch.com/how-to/javadoc/appledoc/api/com/apple/eawt/package-summary.html
        com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
        application.addAppEventListener(new AppForegroundListener() {
            @Override
            public void appRaisedToForeground(AppEvent.AppForegroundEvent afe) {
                isAppForeground = true;
                if (parent != null) {
                    if (parent.isActive()) { setActive(true); }
                    else { setActive(false); }
                }
            }
            @Override
            public void appMovedToBackground(AppEvent.AppForegroundEvent afe) {
                isAppForeground = false;
            }
        });
    }

    /**
     * 左右のマージンを入れる。必ず最後に呼ぶ。
     * @param margin
     */
    public void setMargin(int margin) {
        this.add(Box.createHorizontalStrut(margin),0);
        this.add(Box.createHorizontalStrut(margin));
    }

    /**
     * 組み込まれるときに addNotify が呼ばれるのを利用して parent に WindowAdapter を付ける
     */
    @Override
    public void addNotify() {
        super.addNotify();

        if (parent == null) {
            parent = SwingUtilities.windowForComponent(this);
            if (parent != null) {
                parent.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowActivated(WindowEvent e) {
                        // アプリケーションとしてバックに回っているのに windowActivated が来てしまうことがある対策
                        if (isAppForeground) { setActive(true); }
                        //else { System.out.println("windowActivated ignored while appMovedToBackground"); }
                    }
                    @Override
                    public void windowDeactivated(WindowEvent e) {
                        setActive(false);
                    }
                });
            }
        }
    }

    /**
     * Parent Window に合わせて active/deactive する
     * @param b
     */
    public void setActive(boolean b) {
        if (b) { setBackground(GUIConst.BACKGROUND_FOCUSED); }
        else { setBackground(GUIConst.BACKGROUND_OFF_FOCUS); }
    }

    /**
     * パネルの高さをセットする
     * @param height
     */
    public void setPanelHeight(int height) {
        panelHeight = height;
        this.setPreferredSize(new Dimension(100,height));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,height));
        this.setMinimumSize(new Dimension(0,height));
    }

    /**
     * BoxLayout 部分に縦線のセパレーターを入れる
     */
    public void addSeparator() {
        add(new SeparatorPanel());
    }

    /**
     * BoxLayout 部分に透明なグルーを入れる
     */
    public void addGlue() {
        add(Box.createGlue());
    }

    /**
     * 隙間を入れる
     * @param witdh
     */
    public void addSpace(int witdh) {
        add(Box.createHorizontalStrut(witdh));
    }

    /**
     * BoxLayout 部分にコンポネントを加える
     * @param c
     * @return
     */
    @Override
    public Component add(Component c) {
        // statusPanel でフォントサイズを小さくしたかった
        if (fontSize != 0) {
            Font f = c.getFont();
            if (f != null) c.setFont(new Font(f.getFontName(), f.getStyle(), fontSize));
        }
        return super.add(c);
    }

    /**
     * BoxLayout の場合，index オプションで NORTH または SOUTH に他の Panel を加える
     * FlowLayout の場合は，挿入位置を指定
     * @param c
     * @param index
     */
    public void add(JComponent c, int index) {
        super.add(c, index);
    }

    /**
     * String を add した場合は，JLabel として挿入
     * あとから，setText(String key) でその JLabel の文字列をセットできる
     * @param text
     * @param key
     */
    public void add(String text, String key) {
        JLabel label = new JLabel(text);
        if (text != null && !text.isEmpty()) label.setToolTipText(text);
        this.add(label);
        labelMap.put(key, label);
    }

    /**
     * String の add で key を省略した場合は，ラベルの順番（0〜）をストリングにしたキーとなる
     * @param text
     */
    public void add(String text) {
        int count = 0;
        for(Component c : this.getComponents()) {
            if (c instanceof JLabel) count++;
        }
        this.add(text, String.valueOf(count));
    }

    /**
     * 後から挿入したラベルのテキストを変更する
     * @param text
     * @param key
     */
    public void setText(String text, String key) {
        JLabel label = labelMap.get(key);
        if (text != null && !text.isEmpty()) label.setToolTipText(text);
        if (label != null) label.setText(text);

        else System.out.println("HorizontalPanel#setText: null label (key=" + key + ")");
    }

    /**
     * BoxLayout 部分のフォントサイズをセットする
     * @param size
     */
    public void setFontSize(int size) {
        fontSize = size;
    }

    /**
     * 縦線のセパレーターパネル
     */
    private class SeparatorPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private final Color leftColor = Color.GRAY;
        private final Color rightColor = Color.WHITE;

        public SeparatorPanel() {
            initComponent();
        }

        private void initComponent() {
            Dimension d = new Dimension(SEPARATOR_WIDTH, panelHeight);
            setPreferredSize(d);
            setMaximumSize(d);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics g = graphics.create();
            int center = SEPARATOR_WIDTH/2;
            g.setColor(leftColor);
            g.drawLine(center, 5, center, getHeight() - 5);
            g.setColor(rightColor);
            g.drawLine(center+1, 5, center+1, getHeight() - 5);
            g.dispose();
        }
    }
}

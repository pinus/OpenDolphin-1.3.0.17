package open.dolphin.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Command Panel，Status Panel のベースになるパネル
 * 横長のパネルで，左から要素を詰めていく
 * @author pns
 */
public class HorizontalPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int SEPARATOR_WIDTH = 16;
    private static final int DEFAULT_PANEL_HEIGHT = 24;
    public static final float DEFAULT_COMMAND_PANEL_END_ALPHA = 0.1f;
    public static final float DEFAULT_STATUS_PANEL_END_ALPHA = 0.2f;

    /** パネルの高さ */
    private int panelHeight = DEFAULT_PANEL_HEIGHT;
    /** フォントサイズ */
    private int fontSize = 0;
    /** バックグランドグラデーションの影の始まりのアルファ値 */
    private float startAlpha = 0f;
    /** バックグランドグラデーションの影の終わりのアルファ値 */
    private float endAlpha = DEFAULT_COMMAND_PANEL_END_ALPHA;
    /** バックグランドグラデーションの影の色 */
    private Color shadowColor = Color.BLACK;
    /** 最上線のアルファ値・境界線をつけるのに使う */
    private float topLineAlpha = startAlpha;
    /** 最下線のアルファ値・境界線をつけるのに使う */
    private float bottomLineAlpha = endAlpha;
    /** フォーカスを取ったかどうかを MainFrame から通知してもらう */
    private boolean isFocused = true;
    /** 文字列を挿入した場合は，後からそこに setText できるようにする */
    private HashMap<String, JLabel> labelMap = new HashMap<String, JLabel>();

    public HorizontalPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
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
     * バックグランドを描く色とアルファ値
     * @param startAlpha
     * @param endAlpha
     */
    public void setBackgroundColor(Color shadowColor, float startAlpha, float endAlpha) {
        this.shadowColor = shadowColor;
        this.startAlpha = startAlpha;
        this.endAlpha = endAlpha;
        this.topLineAlpha = startAlpha;
        this.bottomLineAlpha = endAlpha;
    }
    /**
     * フォーカスを取っているかどうかを返す
     * @return
     */
    public boolean isFocused() {
        return isFocused;
    }
    /**
     * フォーカスを取ったかどうかを，MainFrame からセットしてもらう
     * 同時に，shadowColor をセットする
     * @param isFocused
     */
    public void setFocused(boolean isFocused) {
        this.isFocused = isFocused;
        if (isFocused) shadowColor = Color.BLACK;
        else shadowColor = Color.WHITE;
    }
    /**
     * バックグランドの一番上の線
     * @param alpha
     */
    public void setTopLineAlpha(float alpha) {
        topLineAlpha = alpha;
    }
    /**
     * バックグランドの一番下の線
     * @param alpha
     */
    public void setBottomLineAlpha(float alpha) {
        bottomLineAlpha = alpha;
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
     */
    public void setFontSize(int size) {
        fontSize = size;
    }

    /**
     * startAlpha〜endAlpha でグラデーション影を描く
     * @param graphics
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int w = this.getWidth();
        int h = this.getHeight();
        float alpha = 0.2f;
        Graphics2D g = (Graphics2D) graphics.create();
        g.setColor(shadowColor);

        for(int y=1; y < h-1; y++) {

            if (startAlpha > endAlpha) {
                alpha = startAlpha - (float)y / (float)h * (startAlpha - endAlpha);
            } else {
                alpha = startAlpha + (float)y / (float)h * (endAlpha - startAlpha);
            }
            drawHorizontalLine(g, w, y, alpha);
        }

        // 上下の線
        drawHorizontalLine(g, w, 0, topLineAlpha);
        drawHorizontalLine(g, w, h-1, bottomLineAlpha);

        g.dispose();
    }

    /**
     * 陰影をつけるために線を書く
     * @param g
     * @param width
     * @param y
     * @param alpha
     */
    private void drawHorizontalLine(Graphics2D g, int width, int y, float alpha) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.drawLine(0, y, width, y);
    }

    /**
     * 縦線のセパレーターパネル
     */
    private class SeparatorPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private Color leftColor = Color.GRAY;
        private Color rightColor = Color.WHITE;

        public SeparatorPanel() {
            Dimension d = new Dimension(SEPARATOR_WIDTH, panelHeight);
            setPreferredSize(d);
            setMaximumSize(d);
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            int center = SEPARATOR_WIDTH/2;
            g.setColor(leftColor);
            g.drawLine(center, 5, center, getHeight() - 5);
            g.setColor(rightColor);
            g.drawLine(center+1, 5, center+1, getHeight() - 5);
        }
    }
}

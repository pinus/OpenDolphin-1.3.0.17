package open.dolphin.ui;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import open.dolphin.client.GUIConst;
import open.dolphin.helper.ImageHelper;

/**
 * CompletableSearchField.
 * 未入力の Text Field に，半透明の虫眼鏡アイコンと Label を表示する.
 * 入力が始まるとアイコンと Label は消える.
 * @author pns
 */
public class CompletableSearchField extends CompletableJTextField {
    private static final long serialVersionUID = 1L;
    private static final int ICON_TEXT_GAP = 5;

    private BufferedImage icon;
    private String label = "検索";
    private Font font;
    private int verticalDeviation = 0;

    public CompletableSearchField(int col) {
        super(col);
        init();
    }

    private void init() {
        putClientProperty("Quaqua.TextField.style", "search");
        icon = ImageHelper.imageToBufferedImage(GUIConst.ICON_SEARCH_16);
        font = new Font(getFont().getFontName(), Font.PLAIN, 12);
    }

    /**
     * 未入力の Text Field に表示する文字列を設定.
     * @param s
     */
    public void setLabel(String s) {
        label = s;
    }

    /**
     * 組み込む Container によって Label が上下方向にずれるのを修正する.
     * @param i
     */
    public void setVerticalDeviation(int i) {
        verticalDeviation = i;
    }

    /**
     * 虫眼鏡と Label 文字列を表示する.
     * @param graphics
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (getText() == null || getText().equals("")) {
            Graphics2D g = (Graphics2D) graphics.create();

            FontMetrics fm = g.getFontMetrics();
            int iconWidth = icon.getWidth();
            int labelWidth = iconWidth + fm.stringWidth(label) + ICON_TEXT_GAP;

            int x = (getWidth() - labelWidth) / 2;

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));

            g.setFont(font);
            g.drawImage(icon, null, x - ICON_TEXT_GAP, verticalDeviation + 8);
            g.drawString(label, x + iconWidth, fm.getHeight() + verticalDeviation + 5);

            g.dispose();
        }
    }

    public static void main(String[] arg) {
        open.dolphin.client.ClientContext.setClientContextStub(new open.dolphin.client.ClientContextStub());
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CompletableSearchField field = new CompletableSearchField(15);
        field.setLabel("病名検索");
        field.setVerticalDeviation(-3);
        f.add(field);
        f.pack();
        f.setVisible(true);
    }
}

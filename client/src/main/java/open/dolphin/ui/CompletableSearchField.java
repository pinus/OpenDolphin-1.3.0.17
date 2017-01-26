package open.dolphin.ui;

import java.awt.AlphaComposite;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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
    private BufferedImage clearButton;
    private String label = "検索";
    private Font font;

    public CompletableSearchField(int col) {
        super(col);
        init();
    }

    private void init() {
        putClientProperty("Quaqua.TextField.style", "search");
        icon = ImageHelper.imageToBufferedImage(GUIConst.ICON_SEARCH_16);
        clearButton = ImageHelper.imageToBufferedImage(GUIConst.ICON_CROSS_16);
        font = new Font(getFont().getFontName(), Font.PLAIN, 12);

        // Field の右端に X を出して，そこをクリックしたらテキストをクリアする.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (getWidth() - e.getX() <= 20) {
                    setText("");
                    // リターンキー入力されたことにする
                    fireActionPerformed();
                }
            }
        });

        // 右端の X のところでカーソルの形を変える
        addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {
                if (getText() != null && ! getText().equals("") && getWidth() - e.getX() <= 20) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                }
            }
        });
    }

    /**
     * 未入力の Text Field に表示する文字列を設定.
     * @param s
     */
    public void setLabel(String s) {
        label = s;
    }

    /**
     * 虫眼鏡と Label 文字列を表示する.
     * @param graphics
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));

        int verticalCentering = (getHeight() - icon.getHeight())/2;

        if (getText() == null || getText().equals("")) {
            // 虫眼鏡とラベル
            FontMetrics fm = g.getFontMetrics();
            int iconWidth = icon.getWidth();
            int labelWidth = iconWidth + fm.stringWidth(label) + ICON_TEXT_GAP;

            int x = (getWidth() - labelWidth) / 2;

            g.setFont(font);

            g.drawImage(icon, null, x - ICON_TEXT_GAP, verticalCentering);
            g.drawString(label, x + iconWidth, verticalCentering + fm.getAscent()); // height でセンタリングして ascent 分下げる

        } else {
            // 右端のクリアボタン（X マーク）
            g.drawImage(clearButton, null, getWidth()-22, verticalCentering);
        }

        g.dispose();
    }

    public static void main(String[] arg) {
        open.dolphin.client.ClientContext.setClientContextStub(new open.dolphin.client.ClientContextStub());
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CompletableSearchField field = new CompletableSearchField(15);
        field.setLabel("病名検索");
        f.add(field);
        f.pack();
        f.setVisible(true);
    }
}

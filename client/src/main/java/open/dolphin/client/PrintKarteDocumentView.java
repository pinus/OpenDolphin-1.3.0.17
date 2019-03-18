package open.dolphin.client;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

/**
 * カルテをまとめて印刷する
 * いつかやってくるであろう指導のために…　（´・ω・｀）
 * Mac で文字化けするので image にしてからプリントする by pns
 * <p>
 * Masuda Naika Clinic, VIVA! Wakayama-City!!
 *
 * @author masuda
 * @author pns
 */
public class PrintKarteDocumentView implements Printable {
    private static int FOOTER_HEIGHT = 24;
    private static String ptName;
    private static String ptId;
    private Component targetComponent;
    private List<Integer> yPosition;
    private int totalPageNumber;
    private BufferedImage printImage;

    public PrintKarteDocumentView(Component c) {
        this.targetComponent = c;
    }

    /**
     * KarteDocmentViewer.printComponent(scrollPane, name, id) で呼べる static method.
     *
     * @param scrollPanel KarteDocumentViewer の scrollPanel
     * @param name        患者名
     * @param id          患者ID
     */
    public static void printComponent(Component scrollPanel, String name, String id) {
        ptName = name;
        ptId = id;
        new PrintKarteDocumentView(scrollPanel).print();
    }

    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        printJob.setJobName(ptId + " by Dolphin");
        PageFormat pf = printJob.getPageFormat(null);

        JPanel scrollPanel = (JPanel) targetComponent; // KartePanel が component として入っている

        // イメージ作成
        printImage = new BufferedImage(scrollPanel.getWidth(), scrollPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = printImage.createGraphics();
        scrollPanel.paint(g2d);
        g2d.dispose();

        // １ページの imageable area の height/width の計算
        double imageableWidth = pf.getImageableWidth();
        double imageableHeight = pf.getImageableHeight() - FOOTER_HEIGHT;
        double heightPerWidth = imageableHeight / imageableWidth;

        // printImage の幅に換算したページの高さを求める
        int printWidth = scrollPanel.getWidth();
        int printHeight = (int) ((double) printWidth * heightPerWidth);

        // KartePanel を取り出して, ページ位置のインデックスを作る
        yPosition = new ArrayList<>();
        yPosition.add(0); // ページ頭のインデックス

        int sumTmp = 0;
        int sumTotal = 0;

        for (Component c : scrollPanel.getComponents()) {
            // page が一杯になるまで足していく
            sumTmp += c.getHeight();
            if (sumTmp > printHeight) {
                // page 境界を越えたらそこで改ページ
                yPosition.add(sumTotal);
                sumTmp = c.getHeight();
            }
            sumTotal += c.getHeight();
        }
        yPosition.add(sumTotal);

        totalPageNumber = yPosition.size() - 1;

        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (PrinterException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {

        if (pageIndex >= totalPageNumber) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        Font f = new Font("MS-PGothic", Font.ITALIC, 9);
        g2d.setFont(f);
        g2d.setPaint(Color.black);
        g2d.setColor(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Footer
        String footerStr = String.format("[%s] %s (%d/%s)", ptId, ptName, (pageIndex + 1), totalPageNumber);
        int strW = SwingUtilities.computeStringWidth(g2d.getFontMetrics(), footerStr);
        int strX = (int) (pageFormat.getImageableX() + pageFormat.getImageableWidth()) / 2 - strW / 2;
        int strY = (int) (pageFormat.getImageableY() + pageFormat.getImageableHeight()) - g2d.getFontMetrics().getMaxDescent();
        g2d.drawString(footerStr, strX, strY);

        // image ソースの四隅
        int sx1 = 0;
        int sx2 = printImage.getWidth();
        int sy1 = yPosition.get(pageIndex);
        int sy2 = yPosition.get(pageIndex + 1) - 1;

        // 出力先の四隅
        double pageWidth = pageFormat.getImageableWidth();
        double imageHeightPerWidth = (double) (sy2 - sy1 + 1) / (double) sx2;

        int dx1 = (int) pageFormat.getImageableX();
        int dx2 = dx1 + (int) pageFormat.getImageableWidth();
        int dy1 = (int) pageFormat.getImageableY();
        int dy2 = dy1 + (int) (pageFormat.getImageableWidth() * imageHeightPerWidth);

        // イメージ描画
        g2d.drawImage(printImage, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

        return PAGE_EXISTS;
    }
}

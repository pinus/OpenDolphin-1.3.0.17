package open.dolphin.client;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;

/**
 * カルテをまとめて印刷する
 * いつかやってくるであろう指導のために…　（´・ω・｀）
 * Mac で文字化けするので image にしてからプリントする by pns
 *
 * Masuda Naika Clinic, VIVA! Wakayama-City!!
 * @author masuda
 * @author pns
 */
public class PrintKarteDocumentView implements Printable {
    private static int HEADER_HEIGHT = 24;

    private Component targetComponent;
    private int totalPageNumber;
    private BufferedImage printImage;
    private int imageViewWidth;
    private int imageViewHeight;

    private static String ptName;
    private static String ptId;

    public static void printComponent(Component scrollPanel, String name, String id) {
        ptName = name;
        ptId = id;
        new PrintKarteDocumentView(scrollPanel).print();
    }

    public PrintKarteDocumentView(Component c) {
        this.targetComponent = c;
    }

    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        printJob.setJobName(ptId + " by Dolphin");

        printImage = new BufferedImage(targetComponent.getWidth(), targetComponent.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = printImage.createGraphics();
        targetComponent.paint(g2d);
        g2d.dispose();

        PageFormat pf = printJob.getPageFormat(null);
        double heightPerWidth = (pf.getImageableHeight() - HEADER_HEIGHT) /pf.getImageableWidth();

        imageViewWidth = printImage.getWidth();
        imageViewHeight = (int)((double)imageViewWidth * heightPerWidth);
        totalPageNumber = (int)Math.ceil((double)printImage.getHeight() / (double)imageViewHeight);

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

        if (pageIndex >= totalPageNumber) { return Printable.NO_SUCH_PAGE; }

        Graphics2D g2d = (Graphics2D) g;
        Font f = new Font("MS-PGothic", Font.ITALIC, 9);
        g2d.setFont(f);
        g2d.setPaint(Color.black);
        g2d.setColor(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Header
        String headerStr = String.format("[%s] %s (%d/%s)", ptId, ptName, (pageIndex+1), totalPageNumber);
        int strW = SwingUtilities.computeStringWidth(g2d.getFontMetrics(), headerStr);
        int strX = (int)pageFormat.getImageableX() + (int)pageFormat.getImageableWidth() - strW;
        int strY = (int)pageFormat.getImageableY() + g2d.getFontMetrics().getHeight();
        g2d.drawString(headerStr, strX, strY);

        // image ソースの四隅
        int sx1 = 0;
        int sx2 = imageViewWidth;
        int sy1 = imageViewHeight * pageIndex;
        int sy2 = sy1 + imageViewHeight;

        // 出力先の四隅
        double pageWidth = pageFormat.getImageableWidth();

        int dx1 = (int) pageFormat.getImageableX();
        int dx2 = dx1 + (int) pageFormat.getImageableWidth();
        int dy1 = (int)pageFormat.getImageableY() + HEADER_HEIGHT;

        int dy2;
        if (pageIndex < totalPageNumber - 1) {
            dy2 = (int)pageFormat.getImageableY() + (int) pageFormat.getImageableHeight();

        } else {
            // 最終ページ
            int residue = printImage.getHeight() % imageViewHeight;
            double heightPerWidth = (double)residue / (double)imageViewWidth;
            double pageHeight = pageWidth * heightPerWidth;
            dy2 = (int)pageFormat.getImageableY() + (int) pageHeight;
        }

        // イメージ描画
        g2d.drawImage(printImage, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

        return PAGE_EXISTS;
    }
}

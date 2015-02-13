/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client;

/**
 * カルテをまとめて印刷する
 * いつかやってくるであろう指導のために…　（´・ω・｀）
 *
 * Masuda Naika Clinic, VIVA! Wakayama-City!!
 * @author masuda
 */

import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

public class PrintKarteDocumentView implements Printable {

    private Component toBePrint;
    private static String patientName;
    private static String patientId;

    public static void printComponent(Component c, String name, String id) {
        patientName = name;
        patientId = id;
        new PrintKarteDocumentView(c).print();
    }

    public PrintKarteDocumentView(Component toBePrint) {
        this.toBePrint = toBePrint;
    }

    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        printJob.setJobName(patientId + " by Dolphin");

        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (PrinterException pe) {
                System.out.println("Error printing: " + pe);
            }
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {

        Graphics2D g2d = (Graphics2D) g;
        Font f = new Font("Courier", Font.ITALIC, 9);
        g2d.setFont(f);
        g2d.setPaint(Color.black);
        g2d.setColor(Color.black);

        int fontHeight = g2d.getFontMetrics().getHeight();
        int fontDescent = g2d.getFontMetrics().getDescent();
        double footerHeight = fontHeight;
        double pageHeight = pageFormat.getImageableHeight() - footerHeight;
        double pageWidth = pageFormat.getImageableWidth();
        double componentHeight = toBePrint.getHeight();
        double componentWidth = toBePrint.getWidth();

        // 大きかったら縮小
        double scale = 1;
        if (componentWidth >= pageWidth) {
            scale = pageWidth / componentWidth;// shrink
        }

        //
        double scaledComponentHeight = componentHeight * scale;
        int totalNumPages = (int) Math.ceil(scaledComponentHeight / pageHeight);

        if (pageIndex >= totalNumPages) {
            return Printable.NO_SUCH_PAGE;
        }

        // footer
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        String footerString = patientId + " " + patientName + "  Page: " + (pageIndex + 1) + " of " + totalNumPages;
        int strW = SwingUtilities.computeStringWidth(g2d.getFontMetrics(), footerString);
        g2d.drawString(
                footerString,
                (int) pageWidth / 2 - strW / 2,
                (int) (pageHeight + fontHeight - fontDescent));

        // page
        g2d.translate(0f, 0f);
        g2d.translate(0f, -pageIndex * pageHeight);

        if (pageIndex == totalNumPages - 1) {
            g2d.setClip(
                    0, (int) (pageHeight * pageIndex),
                    (int) Math.ceil(pageWidth),
                    (int) (scaledComponentHeight - pageHeight * (totalNumPages - 1)));
        } else {
            g2d.setClip(
                    0, (int) (pageHeight * pageIndex),
                    (int) Math.ceil(pageWidth),
                    (int) Math.ceil(pageHeight));
        }

        g2d.scale(scale, scale);

        disableDoubleBuffering(toBePrint);
        toBePrint.print(g2d);
        enableDoubleBuffering(toBePrint);
        return (PAGE_EXISTS);

    }

    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
}

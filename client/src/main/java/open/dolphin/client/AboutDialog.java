package open.dolphin.client;

import open.dolphin.ui.MoreInfoPanel;
import open.dolphin.ui.PNSButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * About dialog.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class AboutDialog extends JDialog {

    /**
     * Creates new AboutDialog.
     *
     * @param f parent frame
     * @param title title
     * @param imageFile image
     */
    public AboutDialog(Frame f, String title, String imageFile) {
        super(f, title, true);
        init();
    }

    private void init() {
        getRootPane().putClientProperty("apple.awt.transparentTitleBar", Boolean.TRUE);
        getRootPane().putClientProperty("apple.awt.fullWindowContent", true);
        getRootPane().putClientProperty( "apple.awt.windowTitleVisible", false );

        setIconImage(GUIConst.ICON_DOLPHIN.getImage());
        //ラベル作成
        final JLabel imageLabel = new JLabel();
        final Icon icon1 = GUIConst.ICON_SPLASH_DOLPHIN;
        final Icon icon2 = GUIConst.ICON_SPLASH_USAGI;
        imageLabel.setIcon(icon1);
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (imageLabel.getIcon().equals(icon1)) {
                    imageLabel.setIcon(icon2);
                } else {
                    imageLabel.setIcon(icon1);
                }
            }
        });

        // ビルド日時文字列作成. pom.xml で UTC の "20231212064212" という文字列をセットしてある.
        String timestamp = System.getProperty("open.dolphin.build.timestamp");
        if (Objects.nonNull(timestamp) && timestamp.length() == 14) {
            LocalDateTime buildDate = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            timestamp = buildDate.plusHours(9).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        String version = String.format("<html><center>%s Ver. %s (Java %s)<br><small>build %s</small></center></html>",
            ClientContext.getString("productString"), ClientContext.getString("version"), System.getProperty("java.version"), timestamp);
        // copyright 文字列作成
        String copyright = "<html>" + String.join("<br>", ClientContext.getStringArray("copyrightString")) + "</html>";

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(getTextLabel(version, new Font(Font.DIALOG, Font.BOLD, 14)), BorderLayout.NORTH);
        textPanel.add(getTextLabel(copyright, new Font(Font.DIALOG, Font.PLAIN, 12)), BorderLayout.CENTER);

        // 閉じるボタン作成
        JButton closeButton = new PNSButton("閉じる");
        this.getRootPane().setDefaultButton(closeButton);
        //closeButton.setSelected(true);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> close());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        // messagePanel 作成
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(imageLabel, BorderLayout.NORTH);
        messagePanel.add(textPanel, BorderLayout.CENTER);
        messagePanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel content = new MoreInfoPanel(messagePanel, getMoreInfoPane(), "More info...");

        content.setOpaque(true);

        setContentPane(content);
        pack();
        Point loc = GUIFactory.getCenterLoc(this.getWidth(), this.getHeight());
        setLocation(loc);
        setResizable(false);
        setVisible(true);
    }

    private void close() {
        setVisible(false);
        dispose();
    }

    private JScrollPane getMoreInfoPane() {
        JTextArea area =
            new JTextArea(
                "This product also contains copyrighted materials as follows: " +
                    "OpenDolphin 1.3-2.2 Copyright (C) Digital Globe Inc.,  " +
                    "OpenDolphin 1.4m-2.3m Copyright (C) Masuda Naika Clinic,  " +
                    "Fugue Icons 2.4.2 Copyright (C) Yusuke Kamiyamane, " +
                    "Aesthetica Icons 1.12 (http://dryicons.com), " +
                    "Icons from Tango Desktop Project, " +
                    "DefaultIcon ver 2.0 Copyright (c) 2010-2011 Apostolos Paschalidis interactivemania" +
                    "", 5, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return new JScrollPane(area,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private JLabel getTextLabel(String text, Font font) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setFont(font);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setHorizontalAlignment(JLabel.CENTER);

        return label;
    }
}

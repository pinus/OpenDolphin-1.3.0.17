package open.dolphin.client;

import open.dolphin.ui.MoreInfoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * About dialog.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class AboutDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    /**
     * Creates new AboutDialog.
     * @param f
     * @param title
     * @param imageFile
     */
    public AboutDialog(Frame f, String title, String imageFile) {
        super(f, title, true);
        init();
    }

    private void init() {
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
                } else  {
                    imageLabel.setIcon(icon1);
                }
            }
        });

        // version 文字列作成
        StringBuilder buf = new StringBuilder();
        buf.append("<html>");
        buf.append(ClientContext.getString("productString"));
        buf.append("  Ver.");
        buf.append(ClientContext.getString("version"));
        buf.append("</html>");
        String version = buf.toString();

        // copyright 文字列作成
        String[] copyrightList = ClientContext.getStringArray("copyrightString");
        buf = new StringBuilder();
        buf.append("<html>");
        for (int i=0; i<=4; i++) {
            buf.append((i==0)? "": "<br>");
            buf.append(copyrightList[i]);
        }
        buf.append("</html>");
        String copyright = buf.toString();

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(getTextLabel(version, new Font(Font.DIALOG, Font.BOLD, 16)), BorderLayout.NORTH);
        textPanel.add(getTextLabel(copyright, new Font(Font.DIALOG, Font.PLAIN, 12)), BorderLayout.CENTER);

        // 閉じるボタン作成
        JButton closeButton = new JButton("閉じる");
        this.getRootPane().setDefaultButton(closeButton);
        closeButton.setSelected(true);
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
                "This product also contains copyrighted materials as follows: "+
                "OpenDolphin 1.3-2.2 Copyright (C) Digital Globe Inc.,  " +
                "OpenDolphin 1.4m-2.3m Copyright (C) Masuda Naika Clinic,  " +
                "Fugue Icons 2.4.2 Copyright (C) Yusuke Kamiyamane, " +
                "Aesthetica Icons 1.12 (http://dryicons.com), " +
                "Icons from Tango Desktop Project, " +
                "DefaultIcon ver 2.0 Copyright (c) 2010-2011 Apostolos Paschalidis interactivemania" +
                "", 5, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane scroller =
                new JScrollPane(area,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroller;
    }

    private JLabel getTextLabel(String text, Font font) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setFont(font);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return label;
    }
}

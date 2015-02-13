package open.dolphin.impl.pvt;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import open.dolphin.client.GUIConst;

/**
 * 状態ラベルの説明を表示する popup
 * @author pns
 */
public class LegendPopup extends JPopupMenu {

    private static String SPACE = "　";

    public LegendPopup() {
        this(null);
    }

    public LegendPopup(String title) {
        super(title);
        initComponents();
    }

    private void initComponents() {

        int row = 0;

        insert(getPanel("診察終了　", WatingListImpl.DONE_ICON), row++);
        insert(getPanel("記載未完了", WatingListImpl.UNFINISHED_ICON), row++);
        insert(getPanel("オープン　", WatingListImpl.OPEN_ICON), row++);
        insert(getPanel("仮保存　　", WatingListImpl.TEMPORARY_ICON), row++);
        insert(getPanel("他で編集中", WatingListImpl.OPEN_USED_NONE), row++);
        insert(getPanel("診断未入力", WatingListImpl.DIAGNOSIS_EMPTY_COLOR), row++);
        insert(getPanel("初診　　　", WatingListImpl.SHOSHIN_COLOR), row++);

        pack();
    }

    private JPanel getPanel(String text, ImageIcon image) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(text);
        JLabel icon = new JLabel(SPACE);
        icon.setOpaque(true);
        icon.setIcon(image);
        icon.setHorizontalTextPosition(SwingConstants.CENTER);

        panel.add(icon, BorderLayout.WEST);
        panel.add(label, BorderLayout.EAST);

        return panel;
    }

    private JPanel getPanel(String text, Color color) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(text);
        JLabel icon = new JLabel(SPACE);
        icon.setIcon(GUIConst.ICON_EMPTY_16);
        icon.setOpaque(true);
        icon.setBackground(color);
        icon.setHorizontalTextPosition(SwingConstants.CENTER);

        panel.add(icon, BorderLayout.WEST);
        panel.add(label, BorderLayout.EAST);

        return panel;
    }

}


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
 * 状態ラベルの説明を表示する popup.
 * @author pns
 */
public class LegendPopup extends JPopupMenu {
    private static final long serialVersionUID = 1L;

    private static String SPACE = "　";

    public LegendPopup() {
        this(null);
    }

    public LegendPopup(String title) {
        super(title);
        initComponents();
    }

    private void initComponents() {

        insert(getPanel("診察終了　", WaitingListImpl.DONE_ICON),             0);
        insert(getPanel("記載未完了", WaitingListImpl.UNFINISHED_ICON),       1);
        insert(getPanel("オープン　", WaitingListImpl.OPEN_ICON),             2);
        insert(getPanel("仮保存　　", WaitingListImpl.TEMPORARY_ICON),        3);
        insert(getPanel("他で編集中", WaitingListImpl.OPEN_USED_NONE),        4);
        insert(getPanel("診断未入力", WaitingListImpl.DIAGNOSIS_EMPTY_COLOR), 5);
        insert(getPanel("初診　　　", WaitingListImpl.SHOSHIN_COLOR),         6);

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


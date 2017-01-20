package open.dolphin.impl.psearch;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import open.dolphin.client.GUIConst;
import open.dolphin.client.MainComponentPanel;
import open.dolphin.helper.TextComponentUndoManager;
import open.dolphin.ui.CompletableJTextField;
import open.dolphin.ui.Focuser;
import open.dolphin.ui.MyJScrollPane;
import open.dolphin.ui.StatusPanel;
import open.dolphin.util.PNSPair;

/**
 * PatientSearchView 互換 JPanel.
 * @author pns
 */
public class PatientSearchPanel extends MainComponentPanel {
    private static final long serialVersionUID = 1L;

    // narrowing search 時の text field の背景色
    public static final Color NORMAL_SEARCH_BACKGROUND_COLOR = Color.WHITE;
    //public static final Color NARROWING_SEARCH_BACKGROUND_COLOR = new Color(255,255,230);
    public static final Color NARROWING_SEARCH_BACKGROUND_COLOR = new Color(255,255,0);

    // command panel
    private JLabel searchLbl;
    //private JTextField keywordFld;
    private CompletableJTextField keywordFld;
    private JCheckBoxMenuItem narrowingSearchCb;
    private JButton clearBtn;
    // main panel
    private JTable table;
    // status panel
    private JLabel cntLbl;
    private JLabel dateLbl;
    private JProgressBar progressBar;
    private JMenuItem hibernateIndexItem;

    public PatientSearchPanel() {
        initComponents();
    }

    private void initComponents() {

        CommandPanel comPanel = getCommandPanel();

        final JPopupMenu popup = new JPopupMenu();
        final List<PNSPair<String,String>> menuPair = Arrays.asList(
                new PNSPair<>("名前検索", "N"),
                new PNSPair<>("カナ検索", "K"),
                new PNSPair<>("患者番号検索", "T"),
                new PNSPair<>("誕生日検索", "B"),
                new PNSPair<>("メモ検索", "M"),
                new PNSPair<>("全文検索", "F")
        );

        menuPair.forEach(pair -> {
            JMenuItem item = new JMenuItem(pair.getName());
            item.addActionListener(e -> {
                keywordFld.setText(pair.getValue() + " ");
                Focuser.requestFocus(keywordFld);
            });
            popup.add(item);
        });

        popup.addSeparator();

        narrowingSearchCb = new JCheckBoxMenuItem("絞り込みモード");
        narrowingSearchCb.setSelected(true);

        popup.add(narrowingSearchCb);
        popup.addSeparator();

        hibernateIndexItem = new JMenuItem("インデックス作成");
        popup.add(hibernateIndexItem);

        searchLbl = new JLabel("");
        searchLbl.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        keywordFld = new CompletableJTextField(20) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintBorder(Graphics g) {
                super.paintBorder(g);
                Graphics2D g2d = (Graphics2D) g;
                //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                g2d.setColor(getBackground());
                g2d.fillRect(5, 5, getWidth()-11, getHeight()-11);
            }
        };

        TextComponentUndoManager undoManager = TextComponentUndoManager.getManager(keywordFld);

        Dimension tfSize = new Dimension(280,32);
        keywordFld.setPreferredSize(tfSize);
        keywordFld.setMaximumSize(tfSize);
        keywordFld.putClientProperty("Quaqua.TextField.style", "search");

        clearBtn = new JButton();
        clearBtn.setFocusable(false);
        clearBtn.setBorder(BorderFactory.createEmptyBorder());
        clearBtn.setBorderPainted(false);
        clearBtn.setContentAreaFilled(false);
        clearBtn.setOpaque(false);
        clearBtn.setIcon(GUIConst.ICON_REMOVE_22);

        comPanel.addGlue();
        comPanel.add(searchLbl);
        comPanel.add(keywordFld);
        comPanel.add(clearBtn);
        comPanel.addSpace(5);

        MainPanel mainPanel = getMainPanel();

        table = new AddressTipsTable();
        MyJScrollPane scroller = new MyJScrollPane(table);
        mainPanel.add(scroller);

        StatusPanel statusPanel = getStatusPanel();

        progressBar = new JProgressBar();
        Dimension pbSize = new Dimension(100,14);
        progressBar.setMaximumSize(pbSize);
        progressBar.setPreferredSize(pbSize);
        cntLbl = new JLabel("0件");
        dateLbl = new JLabel("2011-11-11(土)");
        statusPanel.addGlue();
        statusPanel.add(progressBar);
        statusPanel.addSeparator();
        statusPanel.add(cntLbl);
        statusPanel.addSeparator();
        statusPanel.add(dateLbl);
        statusPanel.setMargin(4);
    }

    public JLabel getDateLbl() {
        return dateLbl;
    }

    public JLabel getCntLbl() {
        return cntLbl;
    }

    public JTable getTable() {
        return table;
    }

    public CompletableJTextField getKeywordFld() {
        return keywordFld;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getSearchLbl() {
        return searchLbl;
    }

    public JMenuItem getHibernateIndexItem() {
        return hibernateIndexItem;
    }

    public JCheckBoxMenuItem getNarrowingSearchCb() {
        return narrowingSearchCb;
    }

    public JButton getClearBtn() {
        return clearBtn;
    }
}

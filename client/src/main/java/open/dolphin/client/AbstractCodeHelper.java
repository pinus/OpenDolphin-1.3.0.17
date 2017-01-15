package open.dolphin.client;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import open.dolphin.infomodel.ModuleInfoBean;

/**
 * KartePane の抽象コードヘルパークラス.
 *
 * @author Kazyshi Minagawa
 * @author pns
 */
public abstract class AbstractCodeHelper {

    static final Icon ICON = GUIConst.ICON_FOLDER_16;

    /** キーワードの境界となる文字 */
    static final String[] WORD_SEPARATOR = {" ", " ", "，", "," , "、", "。", "\n", "\t"};

    static final String LISTENER_METHOD = "importStamp";


    /** 対象の KartePane */
    KartePane kartePane;

    /** KartePane の JTextPane */
    JTextPane textPane;

    /** 補完リストメニュー */
    JPopupMenu popup;

    /** キーワードパターン */
    Pattern pattern;

    /** キーワードの開始位置 */
    int start;

    /** キーワードの終了位置 */
    int end;

    /** ChartMediator */
    ChartMediator mediator;

    /**
     * Creates a new instance of CodeHelper.
     * @param kartePane
     * @param mediator
     */
    public AbstractCodeHelper(KartePane kartePane, ChartMediator mediator) {

        this.kartePane = kartePane;
        this.mediator = mediator;
        textPane = kartePane.getTextPane();

        Preferences prefs = Preferences.userNodeForPackage(AbstractCodeHelper.class);
        int modifier = prefs.get("modifier", "ctrl").equals("ctrl")? KeyEvent.CTRL_DOWN_MASK : KeyEvent.META_DOWN_MASK;

        textPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getModifiersEx() == modifier) && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buildAndShowPopup();
                }
            }
        });
    }

    /**
     * 単語の境界からキャレットの位置までのテキストを取得し，長さがゼロ以上でれば補完メニューをポップアップする.
     */
    protected void buildAndShowPopup() {

        end = textPane.getCaretPosition();
        start = end;
        boolean found = false;

        while (start > 0) {

            start--;

            try {
                String text = textPane.getText(start, 1);
                for (String test : WORD_SEPARATOR) {
                    if (test.equals(text)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    start++;
                    break;
                }

            } catch (BadLocationException e) {
                e.printStackTrace(System.err);
            }
        }

        try {
            String str = textPane.getText(start, end - start);

            if (str.length() > 0) {
                buildPopup(str);
                showPopup();
            }

        } catch (BadLocationException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * popup menu を構築する.
     * @param text キーワード
     */
    protected abstract void buildPopup(String text);

    /**
     * popup menu を表示する.
     */
    protected void showPopup() {

        if (popup == null || popup.getComponentCount() < 1) {
            return;
        }

        try {
            int pos = textPane.getCaretPosition();
            Rectangle r = textPane.modelToView(pos);

            popup.show (textPane, r.x, r.y);

        } catch (BadLocationException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 引数の entityに対応する StampTree を取得する.
     * @param entity
     */
    protected void buildEntityPopup(String entity) {

        StampBoxPlugin stampBox = mediator.getStampBox();
        StampTree tree = stampBox.getStampTree(entity);

        if (tree == null) { return; }

        popup = new JPopupMenu();

        HashMap<Object, Object> ht = new HashMap<>();

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        ht.put(rootNode, popup);

        Enumeration e = rootNode.preorderEnumeration();

        if (e != null) {

            e.nextElement(); // consume root

            while (e.hasMoreElements()) {

                StampTreeNode node = (StampTreeNode) e.nextElement();

                if (!node.isLeaf()) {

                    JMenu subMenu = new JMenu(node.getUserObject().toString());
                    if (node.getParent() == rootNode) {
                        JPopupMenu parent = (JPopupMenu) ht.get(node.getParent());
                        parent.add(subMenu);
                        ht.put(node, subMenu);
                    } else {
                        JMenu parent = (JMenu) ht.get(node.getParent());
                        parent.add(subMenu);
                        ht.put(node, subMenu);
                    }


                    // 配下の子を全て列挙しJmenuItemにまとめる
                    JMenuItem item = new JMenuItem(node.getUserObject().toString());
                    item.setIcon(ICON);
                    subMenu.add(item);

                    addActionListner(item, node);

                } else if (node.isLeaf()) {

                    ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                    String stampName = info.getStampName();

                    JMenuItem item = new JMenuItem(stampName);
                    addActionListner(item, node);

                    if (node.getParent() == rootNode) {
                        JPopupMenu parent = (JPopupMenu) ht.get(node.getParent());
                        parent.add(item);
                    } else {
                        JMenu parent = (JMenu) ht.get(node.getParent());
                        parent.add(item);
                    }
                }
            }
        }
    }

    /**
     * node を textPane に挿入するアクションを menuItem に登録する.
     * @param item
     * @param node
     */
    protected void addActionListner(JMenuItem item, StampTreeNode node) {
        item.addActionListener(e -> importStamp(textPane, textPane.getTransferHandler(), new LocalStampTreeNodeTransferable(node)));
    }

    /**
     * メニュー選択でここが呼ばれる.
     * @param comp
     * @param handler
     * @param tr
     */
    public void importStamp(JComponent comp, TransferHandler handler, LocalStampTreeNodeTransferable tr) {
        textPane.setSelectionStart(start);
        textPane.setSelectionEnd(end);
        textPane.replaceSelection("");
        handler.importData(comp, tr);
        closePopup();
    }

    protected void closePopup() {
        if (popup != null) {
            popup.removeAll();
            popup = null;
        }
    }
}

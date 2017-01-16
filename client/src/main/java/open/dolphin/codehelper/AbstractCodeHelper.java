package open.dolphin.codehelper;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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
import open.dolphin.client.ChartMediator;
import open.dolphin.client.GUIConst;
import open.dolphin.client.KartePane;
import open.dolphin.client.LocalStampTreeNodeTransferable;
import open.dolphin.client.StampBoxPlugin;
import open.dolphin.client.StampTree;
import open.dolphin.client.StampTreeNode;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;

/**
 * KartePane の抽象コードヘルパークラス.
 *
 * @author Kazyshi Minagawa
 * @author pns
 */
public abstract class AbstractCodeHelper {
    public static final Icon ICON = GUIConst.ICON_FOLDER_16;

    /** キーワードの境界となる文字 */
    public static final String[] WORD_SEPARATOR = {" ", " ", "，", "," , "、", "。", "\n", "\t"};
    /** KartePane の JTextPane */
    private final JTextPane textPane;
    /** 補完リストメニュー */
    private JPopupMenu popup;
    /** キーワードの開始位置 */
    private int start;
    /** キーワードの終了位置 */
    private int end;
    /** ChartMediator */
    private final ChartMediator mediator;

    /**
     * Creates a new instance of CodeHelper.
     * @param kartePane
     * @param chartMediator
     */
    public AbstractCodeHelper(KartePane kartePane, ChartMediator chartMediator) {

        mediator = chartMediator;
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
     * buildPopup で作った popup を表示のためにセットする.
     * @param p
     */
    protected void setPopup(JPopupMenu p) {
        popup = p;
    }

    /**
     * ChartMediator を返す.
     * @return
     */
    protected ChartMediator getMediator() {
        return mediator;
    }

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

        // searchText == null で入ると，tree 全体が返ってくる
        MenuModel model = createMenu(tree, null);

        popup = new JPopupMenu();
        model.getSubMenus().forEach(menu -> popup.add(menu));
        model.getRootItems().forEach(item -> popup.add(item));
    }

    /**
     * StampTree から searchText に合致するスタンプを検索してメニューを作成し，MenuModel に格納する.
     * @param tree
     * @param searchText - null の場合は全て一致と判断して ENTITY 全体を返す
     * @return
     */
    protected MenuModel createMenu(StampTree tree, String searchText) {
        MenuModel model = new MenuModel();
        boolean isDiagnosis = tree.getEntity().equals(IInfoModel.ENTITY_DIAGNOSIS);

        // 親メニューのスタックを生成する
        List<JMenu> subMenus = new ArrayList<>();
        // 親のない item のスタック
        List<JMenuItem> rootItems = new ArrayList<>();
        // 親ノードのスタックを生成する - インデックスが subMenus と一致するようにする
        List<StampTreeNode> parents = new ArrayList<>();
        // Stamp を検索する pattern - text が null の場合は pattern も null にしておく
        //Pattern pattern = searchText == null? null : Pattern.compile(searchText);

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        Enumeration e = rootNode.preorderEnumeration();

        // スタンプをスキャンする
        if (e != null) {
            e.nextElement(); // consume root

            while (e.hasMoreElements()) {
                // 調査対象のノードを得る
                StampTreeNode node = (StampTreeNode) e.nextElement();
                // その親を得る
                StampTreeNode parent = (StampTreeNode) node.getParent();
                // 親がリストに含まれているかどうか
                int index = parents.indexOf(parent);

                if (index > -1) {
                    // 既に親が登録されている場合
                    if (!node.isLeaf()) {
                        // フォルダの場合は，新たに親として parents に加える
                        String folderName = node.getUserObject().toString();
                        JMenu subMenu = new JMenu(folderName);
                        // 頭から入れていく
                        subMenus.add(subMenu);
                        parents.add(node);
                        // フォルダ item を作って menu 中に入れておく
                        JMenuItem item = new JMenuItem(folderName);
                        item.setIcon(ICON);
                        subMenu.add(item);
                        addActionListner(item, node, isDiagnosis);

                    } else {
                        // 親のいる item の場合は，親のもとに入れる
                        ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                        String completion = info.getStampName();
                        JMenuItem item = new JMenuItem(completion);
                        addActionListner(item, node, isDiagnosis);
                        // parents と index は一致しているので，これで対応 menu 下に item が入る
                        subMenus.get(index).add(item);
                    }

                } else {
                    // 親がいない場合は検索する
                    if (!node.isLeaf()) {
                        // フォルダの場合
                        String completion = node.getUserObject().toString();

                        // searchText が null の場合は合致と判断して全部返す
                        if (matches(searchText, completion)) {
                            String folderName = node.getUserObject().toString();
                            JMenu subMenu = new JMenu(folderName);
                            // 親として加える
                            subMenus.add(subMenu);
                            parents.add(node);

                            // フォルダ item を作って menu 中に入れておく
                            JMenuItem item = new JMenuItem(folderName);
                            item.setIcon(ICON);
                            subMenu.add(item);
                            addActionListner(item, node, isDiagnosis);
                        }

                    } else {
                        // 親のない item の場合
                        ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                        String completion = info.getStampName();

                        // searchText が null の場合は合致と判断して全部返す
                        if (matches(searchText, completion)) {
                            // 一致した場合
                            JMenuItem item = new JMenuItem(completion);
                            addActionListner(item, node, isDiagnosis);
                            // 親のない item
                            rootItems.add(item);
                        }
                    }
                }
            }
        }

        model.setRootItems(rootItems);
        model.setSubMenus(subMenus);
        return model;
    }

    /**
     * searchText が completion にマッチするかどうかを判断する.
     * @param searchText - null の場合は合致と判断
     * @param completion
     * @return
     */
    private boolean matches(String searchText, String completion) {
        // searchText が null の場合は合致と判断
        if (searchText == null) { return true; }

        // 大文字，小文字を無視するために小文字に変換して比較
        Pattern pattern = Pattern.compile(searchText.toLowerCase());
        return pattern.matcher(completion.toLowerCase()).matches();
    }

    /**
     * node を textPane に挿入するアクションを menuItem に登録する.
     * @param item
     * @param node
     */
    protected void addActionListner(JMenuItem item, StampTreeNode node, boolean isDiagnosis) {
        if (isDiagnosis) {
            System.out.println("----------- Diagnosis ------------");
        } else {
            item.addActionListener(e -> importStamp(textPane, textPane.getTransferHandler(), new LocalStampTreeNodeTransferable(node)));
        }
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

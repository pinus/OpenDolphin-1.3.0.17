package open.dolphin.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import javax.swing.event.MenuEvent;
import javax.swing.text.SimpleAttributeSet;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.helper.MenuSupport;
import open.dolphin.project.Project;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.List;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.text.StyledEditorKit;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.ui.MyJPopupMenu;
import org.apache.log4j.Logger;

/**
 * Mediator class to control Karte Window Menu.
 * KarteComposite インターフェースを持つクラスに対して，メニューをコントロールする enter / exit を送る.
 * ChartImpl, EditorFrame でインスタンスが作られる.
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class ChartMediator extends MenuSupport {

    // Font size
    private static final int[] FONT_SIZE = {10, 12, 14, 16, 18, 24, 36};
    private int curFontSize = 1;

    // ChartPlugin
    private final Chart chart;

    // current KarteComposit
    private KarteComposite<?> curKarteComposit;

    private final Logger logger;

    /**
     * Create ChartMediator.
     * @param owner
     */
    public ChartMediator(Chart owner) {

        super(owner);
        logger = ClientContext.getBootLogger();
        chart = owner;

        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

        // focus を取った KarteComposite に menu の activate/deactivate 情報を送る
        focusManager.addPropertyChangeListener("focusOwner", e -> {

            Window focusedWindow = ((KeyboardFocusManager) e.getSource()).getActiveWindow();

            if (chart.getFrame() == focusedWindow) {
                Component comp = (Component) e.getNewValue();
                if (comp instanceof JTextPane) {
                    // KartePane に入っている JTextPane は ClientProperty に親の KartePane を入れてある
                    Object obj = ((JComponent) comp).getClientProperty("kartePane");
                    if (obj != null && obj instanceof KartePane) {
                        // KartePane
                        setCurKarteComposit((KarteComposite) obj);
                    }
                } else if (comp instanceof KarteComposite) {
                    // StampHolder など
                    setCurKarteComposit((KarteComposite) comp);
                }
            }
        });
    }

    public void setCurKarteComposit(KarteComposite<?> newComposit) {

        KarteComposite<?> old = curKarteComposit;
        curKarteComposit = newComposit;
        addChain(curKarteComposit);

        if (old != curKarteComposit) {
            logger.debug("ChartMediator old != curKarteComposit");
            logger.debug("ChartMediator: composit changed in " + chart.getClass());

            if (old != null) {
                old.exit(getActions());
            }
            // KarteComposite.enter 内で enable/disable の初期化が必要な action
            enableAction(GUIConst.ACTION_CUT, false);
            enableAction(GUIConst.ACTION_COPY, false);
            enableAction(GUIConst.ACTION_PASTE, false);
            enableAction(GUIConst.ACTION_INSERT_TEXT, false);
            enableAction(GUIConst.ACTION_INSERT_SCHEMA, false);
            enableAction(GUIConst.ACTION_INSERT_STAMP, false);
            // undo/redo は基本 false で，あとは KarteComposite で制御されるべき
            enableAction(GUIConst.ACTION_UNDO, false);
            enableAction(GUIConst.ACTION_REDO, false);

            if (curKarteComposit != null) {
                logger.debug("ChartMediator curKarteComposit != null");
                // KarteComposite 内で enable/disable は初期化される
                curKarteComposit.enter(getActions());
            }
        }
    }

    @Override
    public void registerActions(ActionMap map) {

        super.registerActions(map);

        // 昇順降順を Preference から取得し設定しておく
        boolean asc = Project.getPreferences().getBoolean(Project.DOC_HISTORY_ASCENDING, false);
        Action action = asc? map.get(GUIConst.ACTION_ASCENDING) : map.get(GUIConst.ACTION_DESCENDING) ;
        JRadioButtonMenuItem rdi = (JRadioButtonMenuItem) action.getValue("menuItem");
        rdi.setSelected(true);
    }

    public void dispose() {}

    /**
     * 現在の KarteCompsite<T> の中身の T (JComponent) を返す.
     * @return
     */
    private JComponent getCurrentComponent() {
        if (curKarteComposit != null) {
            return (JComponent) curKarteComposit.getComponent();
        }
        return null;
    }

    /**
     * メニューリスナの実装.
     * 挿入及びテキストメニューが選択された時の処理を行う.
     * @param e
     */
    @Override
    public void menuSelected(MenuEvent e) {

        // 挿入とテキストメニューにリスナが登録されている
        JMenu selectedMenu = (JMenu) e.getSource();
        String cmd = selectedMenu.getActionCommand();

        //
        // 挿入メニューの時
        // StampBox のツリーをメニューにする
        //
        if (cmd.equals(GUIConst.MENU_INSERT)) {

            selectedMenu.removeAll();

            // StampBox の全ツリーを取得する
            List<StampTree> trees = getStampBox().getAllTrees();

            // ツリーをイテレートする
            trees.forEach(tree -> {

                switch (tree.getEntity()) {
                    case IInfoModel.ENTITY_DIAGNOSIS:
                        // 傷病名の時，傷病名メニューを構築し追加する
                        selectedMenu.add(createDiagnosisMenu(tree));
                        selectedMenu.addSeparator();
                        break;
                    case IInfoModel.ENTITY_TEXT:
                        // テキストの時，テキストメニューを構築し追加する
                        selectedMenu.add(createTextMenu(tree));
                        selectedMenu.addSeparator();
                        break;
                    default:
                        // 通常のPオーダの時
                        selectedMenu.add(createStampMenu(tree));
                        break;
                }
            });

        } else if (cmd.equals(GUIConst.MENU_TEXT)) {
            //
            // テキストメニューの場合，スタイルを制御する
            //
            adjustStyleMenu();
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {
    }

    @Override
    public void menuCanceled(MenuEvent e) {
    }

    /**
     * フォーマット関連メニューを調整する.
     * @param kartePane
     */
    private void adjustStyleMenu() {

        boolean enabled = false;
        KartePane kartePane;

        if (getChain() instanceof KarteEditor) {
            KarteEditor editor = (KarteEditor) getChain();
            kartePane = editor.getSOAPane();
            enabled = kartePane.getTextPane().isEditable();
        }

        // サブメニューを制御する
        getAction("size").setEnabled(enabled);
        getAction("style").setEnabled(enabled);
        getAction("justify").setEnabled(enabled);
        getAction("color").setEnabled(enabled);

        // メニューアイテムを制御する
        //getAction(GUIConst.ACTION_RESET_STYLE).setEnabled(enabled);

        getAction("fontRed").setEnabled(enabled);
        getAction("fontOrange").setEnabled(enabled);
        getAction("fontYellow").setEnabled(enabled);
        getAction("fontGreen").setEnabled(enabled);
        getAction("fontBlue").setEnabled(enabled);
        getAction("fontPurple").setEnabled(enabled);
        getAction("fontGray").setEnabled(enabled);

        getAction("fontLarger").setEnabled(enabled);
        getAction("fontSmaller").setEnabled(enabled);
        getAction("fontStandard").setEnabled(enabled);

        getAction("fontBold").setEnabled(enabled);
        getAction("fontItalic").setEnabled(enabled);
        getAction("fontUnderline").setEnabled(enabled);

        getAction("leftJustify").setEnabled(enabled);
        getAction("centerJustify").setEnabled(enabled);
        getAction("rightJustify").setEnabled(enabled);
    }

    /**
     * スタンプTreeから傷病名メニューを構築する.
     * @param insertMenu テキストメニュー
     */
    private JMenu createDiagnosisMenu(StampTree stampTree) {
        //
        // chain の先頭が DiagnosisDocument の時のみ使用可能とする
        //
        JMenu myMenu;
        Object obj = getChain(); // chain の先頭
        DiagnosisDocument diagnosis = obj instanceof DiagnosisDocument?
                (DiagnosisDocument) obj : null;

        if (diagnosis == null) {
            // cjainの先頭がDiagnosisでない場合はメニューを disable にする
            myMenu = new JMenu(stampTree.getTreeName());
            myMenu.setEnabled(false);

        } else {
            // 傷病名Tree，テーブル，ハンドラからメニューを構築する
            JComponent comp = diagnosis.getDiagnosisTable();
            TransferHandler handler = comp.getTransferHandler();
            StampTreeMenuBuilder builder = new StampTreeMenuBuilder();
            myMenu = builder.build(stampTree, comp, handler);
        }
        return myMenu;
    }

    /**
     * スタンプTreeからテキストメニューを構築する.
     * @param insertMenu テキストメニュー
     */
    private JMenu createTextMenu(StampTree stampTree) {
        //
        // chain の先頭が KarteEditor でかつ SOAane が編集可の場合のみメニューが使える
        //
        boolean enabled = false;

        KartePane kartePane = null;
        Object obj = getChain();

        if (obj instanceof KarteEditor) {
            KarteEditor editor = (KarteEditor) obj;
            kartePane = editor.getSOAPane();
            if (kartePane != null) {
                enabled = kartePane.getTextPane().isEditable();
            }
        }

        JMenu myMenu = null;

        if (!enabled) {
            myMenu = new JMenu(stampTree.getTreeName());
            myMenu.setEnabled(false);

        } else {
            //
            // TextTree，JTextPane，handler からメニューを構築する
            // PPane にも落とさなければならない TODO
            // JComponent comp = kartePane.getTextPane();
            // TransferHandler handler = comp.getTransferHandler();

            // 2007-03-31
            // 直近でフォーカスを得ているコンポーネント(JTextPan）へ挿入する
            //
            JComponent comp = getCurrentComponent();
            if (comp == null && kartePane != null) {
                comp = kartePane.getTextPane();
            }
            if (comp != null) {
                TransferHandler handler = comp.getTransferHandler();
                StampTreeMenuBuilder builder = new StampTreeMenuBuilder();
                myMenu = builder.build(stampTree, comp, handler);
            }
        }

        return myMenu;
    }

    /**
     * スタンプメニューを構築する.
     * @param insertMenu スタンプメニュー
     */
    private JMenu createStampMenu(StampTree stampTree) {
        //
        // chain の先頭が KarteEditor でかつ Pane が編集可の場合のみメニューが使える
        //
        boolean enabled = false;

        KartePane kartePane = null;
        Object obj = getChain();

        if (obj instanceof KarteEditor) {
            KarteEditor editor = (KarteEditor) obj;
            kartePane = editor.getPPane();
            if (kartePane != null) {
                enabled = kartePane.getTextPane().isEditable();
            }
        }

        JMenu myMenu = null;

        if (!enabled) {
            myMenu = new JMenu(stampTree.getTreeName());
            myMenu.setEnabled(false);

        } else if (kartePane != null) {
            // StampTree，JTextPane，Handler からメニューを構築する
            JComponent comp = kartePane.getTextPane();
            TransferHandler handler = comp.getTransferHandler();
            StampTreeMenuBuilder builder = new StampTreeMenuBuilder();
            myMenu = builder.build(stampTree, comp, handler);
        }

        return myMenu;
    }

    /**
     * 引数のポップアップメニューへ傷病名メニューを追加する.
     * @param popup 傷病名メニューを追加するポップアップメニュー
     */
    public void addDiseaseMenu(MyJPopupMenu popup) {
        //
        // Chain の先頭が DiagnosisDocument の時のみ追加する
        //
        DiagnosisDocument diagnosis = null;
        Object obj = getChain();

        if (obj instanceof DiagnosisDocument) {
            diagnosis = (DiagnosisDocument) obj;
        }

        StampTree stampTree = getStampBox().getStampTree(IInfoModel.ENTITY_DIAGNOSIS);

        if (stampTree != null) {

            if (diagnosis == null) {
                JMenu myMenu = new JMenu(stampTree.getTreeName());
                myMenu.setEnabled(false);
                popup.add(myMenu);

            } else {
                JComponent comp = diagnosis.getDiagnosisTable();
                TransferHandler handler = comp.getTransferHandler();
                StampTreePopupBuilder builder = new StampTreePopupBuilder();
                builder.build(stampTree, popup, comp, handler);
            }
        }
    }

    /**
     * 引数のポップアップメニューへテキストメニューを追加する.
     * @param popup テキストメニューを追加するポップアップメニュー
     */
    public void addTextMenu(MyJPopupMenu popup) {

        boolean enabled = false;

        KartePane kartePane = null;
        Object obj = getChain();

        if (obj instanceof KarteEditor) {
            KarteEditor editor = (KarteEditor) obj;
            kartePane = editor.getSOAPane();
            if (kartePane != null) {
                enabled = kartePane.getTextPane().isEditable();
            }
        }

        StampTree stampTree = getStampBox().getStampTree(IInfoModel.ENTITY_TEXT);

        // ASP スタンプボックスで entity に対応する Tree がない場合がある
        if (stampTree != null) {

            if (!enabled) {
                JMenu myMenu = new JMenu(stampTree.getTreeName());
                myMenu.setEnabled(false);
                popup.add(myMenu);

            } else {
                JComponent comp = getCurrentComponent();
                if (comp == null && kartePane != null) {
                    comp = kartePane.getTextPane();
                }
                if (comp != null) {
                    TransferHandler handler = comp.getTransferHandler();
                    StampTreePopupBuilder builder = new StampTreePopupBuilder();
                    builder.build(stampTree, popup, comp, handler);
                }
            }
        }
    }

    /**
     * PPane のコンテキストメニューまたはツールバーの stampIcon へスタンプメニューを追加する.
     * @param menu Ppane のコンテキストメニュー
     * @param kartePane PPnae
     */
    public void addStampMenu(MyJPopupMenu menu, final KartePane kartePane) {

        // 引数のPaneがPかつ編集可の時のみ追加する
        // コンテキストメニューなのでこれはOK
        if (kartePane != null && kartePane.getMyRole().equals(IInfoModel.ROLE_P) && kartePane.getTextPane().isEditable()) {

            StampBoxPlugin stampBox = getStampBox();

            List<StampTree> trees = stampBox.getAllTrees();

            StampTreeMenuBuilder builder = new StampTreeMenuBuilder();
            JComponent cmp = kartePane.getTextPane();
            TransferHandler handler = cmp.getTransferHandler();

            // StampBox内の全Treeをイテレートする
            trees.stream()
                    .filter(tree -> !tree.getEntity().equals(IInfoModel.ENTITY_DIAGNOSIS))
                    .filter(tree -> !tree.getEntity().equals(IInfoModel.ENTITY_TEXT))
                    .forEach(tree -> menu.add(builder.build(tree, cmp, handler)));
        }
    }

    /**
     * 引数のポップアップメニューへスタンプメニューを追加する.
     * このメソッドはツールバーの stamp icon の actionPerformed からコールされる.
     * @param popup
     */
    public void addStampMenu(MyJPopupMenu popup) {

        boolean enabled = false;

        KartePane kartePane = null;
        Object obj = getChain();

        if (obj instanceof KarteEditor) {
            KarteEditor editor = (KarteEditor) obj;
            kartePane = editor.getPPane();
            if (kartePane != null) {
                enabled = kartePane.getTextPane().isEditable();
            }
        }

        if (enabled) {
            addStampMenu(popup, kartePane);
        }
    }

    /**
     * 指定された entity の StampTree を返す.
     * @param entity
     * @return
     */
    public StampTree getStampTree(String entity) {
        return getStampBox().getStampTree(entity);
    }

    /**
     * StampBoxPlugin を返す.
     * @return
     */
    public StampBoxPlugin getStampBox() {
        return (StampBoxPlugin) chart.getContext().getPlugin("stampBox");
    }

    /**
     * 指定された entity の StampTree が存在するかどうか.
     * @param entity
     * @return
     */
    public boolean hasTree(String entity) {
        StampBoxPlugin stBox = (StampBoxPlugin)chart.getContext().getPlugin("stampBox");
        StampTree tree = stBox.getStampTree(entity);
        return tree != null;
    }

    /**
     * chain のトップの Object に applyInsurance(PVTHealthInsuranceModel hm) メソッドを発行する.
     * @param hm
     */
    public void applyInsurance(PVTHealthInsuranceModel hm) {

        Object target = getChain();
        if (target != null) {
            try {
                Method m = target.getClass().getMethod("applyInsurance", new Class[]{hm.getClass()});
                m.invoke(target, new Object[]{hm});

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                System.out.println("ChartMediator.java: " + ex);
            }
        }
    }

    //
    // メニューのアクション (MenuSupport から reflection で実行される
    //
    public void cut() {
        if (curKarteComposit != null) {
            JComponent focusOwner = getCurrentComponent();
            if (focusOwner != null) {
                Action a = focusOwner.getActionMap().get(TransferHandler.getCutAction().getValue(Action.NAME));
                if (a != null) {
                    a.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
                    setCurKarteComposit(null);
                }
            }
        }
    }

    public void copy() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = focusOwner.getActionMap().get(TransferHandler.getCopyAction().getValue(Action.NAME));
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
            }
        }
    }

    public void paste() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = focusOwner.getActionMap().get(TransferHandler.getPasteAction().getValue(Action.NAME));
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
            }
        }
    }

    public void delete() {
    }

    public void resetStyle() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null && focusOwner instanceof JTextPane) {
            JTextPane pane = (JTextPane) focusOwner;
            pane.setCharacterAttributes(SimpleAttributeSet.EMPTY, true);
        }
    }

    public void fontLarger() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            if (curFontSize < 6) {
                curFontSize++;
            }
            int size = FONT_SIZE[curFontSize];
            Action a = focusOwner.getActionMap().get("font-size-" + size);
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
            if (curFontSize == 6) {
                enableAction("fontLarger", false);
            }
        }
    }

    public void fontSmaller() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            if (curFontSize > 0) {
                curFontSize--;
            }
            int size = FONT_SIZE[curFontSize];
            Action a = focusOwner.getActionMap().get("font-size-" + size);
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
            if (curFontSize == 0) {
                enableAction("fontSmaller", false);
            }
        }
    }

    public void fontStandard() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            curFontSize = 1;
            int size = FONT_SIZE[curFontSize];
            Action a = focusOwner.getActionMap().get("font-size-" + size);
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
            enableAction("fontSmaller", true);
            enableAction("fontLarger", true);
        }
    }

    public void fontBold() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = focusOwner.getActionMap().get("font-bold");
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
        }
    }

    public void fontItalic() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = focusOwner.getActionMap().get("font-italic");
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
        }
    }


    public void fontUnderline() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = focusOwner.getActionMap().get("font-underline");
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
        }
    }

    public void leftJustify() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = focusOwner.getActionMap().get("left-justify");
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
        }
    }

    public void centerJustify() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = focusOwner.getActionMap().get("center-justify");
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
        }
    }

    public void rightJustify() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = focusOwner.getActionMap().get("right-justify");
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
        }
    }

    private void colorAction(Color color) {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = new StyledEditorKit.ForegroundAction("color", color);
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        "foreground"));
            }
        }
    }

    public void fontRed() {
        colorAction(ClientContext.getColor("color.set.default.red"));
    }

    public void fontOrange() {
       colorAction(ClientContext.getColor("color.set.default.orange"));
    }

    public void fontYellow() {
        colorAction(ClientContext.getColor("color.set.default.yellow"));
    }

    public void fontGreen() {
        colorAction(ClientContext.getColor("color.set.default.green"));
    }

    public void fontBlue() {
        colorAction(ClientContext.getColor("color.set.default.blue"));
    }

    public void fontPurple() {
        colorAction(ClientContext.getColor("color.set.default.purpule"));
    }

    public void fontGray() {
        colorAction(ClientContext.getColor("color.set.default.gray"));
    }

    public void fontBlack() {
        colorAction(Color.BLACK);
    }
}
























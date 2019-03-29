package open.dolphin.client;

import open.dolphin.helper.MenuActionManager;
import open.dolphin.helper.MenuActionManager.MenuAction;
import open.dolphin.helper.MenuSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**
 * Menu Factory.
 *
 * @author Minagawa, Kazushi
 * @author pns
 */
public class MenuFactory {
    private static final String SHORT_CUT_KEY_MASK = ClientContext.isMac() ? "meta" : "ctrl";

    private MenuSupport main;
    private MenuSupport chart; // ChartMediator
    private JMenuBar menuBar;
    private JPanel toolPanel;
    private ActionMap actionMap;

    public MenuFactory() {
    }

    public void setMenuSupports(MenuSupport main, MenuSupport chart) {
        this.main = main;
        this.chart = chart;
    }

    public JMenuBar getMenuBarProduct() {
        return menuBar;
    }

    public JPanel getToolPanelProduct() {
        return toolPanel;
    }

    public ActionMap getActionMap() { return actionMap; }

    @MenuAction
    public void newKarte() {
        chart.sendToChain(GUIConst.ACTION_NEW_KARTE);
    }

    @MenuAction
    public void newDocument() {
        chart.sendToChain(GUIConst.ACTION_NEW_DOCUMENT);
    }

    @MenuAction
    public void openKarte() {
        main.sendToChain(GUIConst.ACTION_OPEN_KARTE);
    }

    @MenuAction
    public void close() {
        chart.sendToChain(GUIConst.ACTION_CLOSE);
    }

    @MenuAction
    public void save() {
        chart.sendToChain(GUIConst.ACTION_SAVE);
    }

    @MenuAction
    public void delete() {
        chart.sendToChain(GUIConst.ACTION_DELETE);
    }

    @MenuAction
    public void printerSetup() {
        main.sendToChain(GUIConst.ACTION_PRINTER_SETUP);
    }

    @MenuAction
    public void print() {
        chart.sendToChain(GUIConst.ACTION_PRINT);
    }

    @MenuAction
    public void processExit() {
        main.sendToChain(GUIConst.ACTION_PROCESS_EXIT);
    }

    @MenuAction
    public void modifyKarte() {
        chart.sendToChain(GUIConst.ACTION_MODIFY_KARTE);
    }

    @MenuAction
    public void undo() {
        chart.sendToChain(GUIConst.ACTION_UNDO);
    }

    @MenuAction
    public void redo() {
        chart.sendToChain(GUIConst.ACTION_REDO);
    }

    @MenuAction
    public void cut() {
        chart.sendToChain(GUIConst.ACTION_CUT);
    }

    @MenuAction
    public void copy() { chart.sendToChain(GUIConst.ACTION_COPY); }

    @MenuAction
    public void paste() { chart.sendToChain(GUIConst.ACTION_PASTE); }

    @MenuAction
    public void selectAll() {
        chart.sendToChain(GUIConst.ACTION_SELECT_ALL);
    }

    @MenuAction
    public void sendClaim() {
        chart.sendToChain(GUIConst.ACTION_SEND_CLAIM);
    }

    @MenuAction
    public void ascending() {
        chart.sendToChain(GUIConst.ACTION_ASCENDING);
    }

    @MenuAction
    public void descending() {
        chart.sendToChain(GUIConst.ACTION_DESCENDING);
    }

    @MenuAction
    public void showModified() {
        chart.sendToChain(GUIConst.ACTION_SHOW_MODIFIED);
    }

    @MenuAction
    public void setKarteEnviroment() {
        main.sendToChain(GUIConst.ACTION_SET_KARTE_ENVIROMENT);
    }

    @MenuAction
    public void insertDisease() { }

    @MenuAction
    public void insertText() { }

    @MenuAction
    public void insertSchema() { }

    @MenuAction
    public void insertStamp() { }

    @MenuAction
    public void selectInsurance() { }

    @MenuAction
    public void size() { }

    @MenuAction
    public void fontLarger() {
        chart.sendToChain(GUIConst.ACTION_FONT_LARGER);
    }

    @MenuAction
    public void fontSmaller() {
        chart.sendToChain(GUIConst.ACTION_FONT_SMALLER);
    }

    @MenuAction
    public void fontStandard() {
        chart.sendToChain(GUIConst.ACTION_FONT_STANDARD);
    }

    @MenuAction
    public void style() { }

    @MenuAction
    public void fontBold() {
        chart.sendToChain(GUIConst.ACTION_FONT_BOLD);
    }

    @MenuAction
    public void fontItalic() {
        chart.sendToChain(GUIConst.ACTION_FONT_ITALIC);
    }

    @MenuAction
    public void fontUnderline() {
        chart.sendToChain(GUIConst.ACTION_FONT_UNDERLINE);
    }

    @MenuAction
    public void justify() { }

    @MenuAction
    public void leftJustify() {
        chart.sendToChain(GUIConst.ACTION_LEFT_JUSTIFY);
    }

    @MenuAction
    public void centerJustify() {
        chart.sendToChain(GUIConst.ACTION_CENTER_JUSTIFY);
    }

    @MenuAction
    public void rightJustify() {
        chart.sendToChain(GUIConst.ACTION_RIGHT_JUSTIFY);
    }

    @MenuAction
    public void color() { }

    @MenuAction
    public void fontRed() {
        chart.sendToChain(GUIConst.ACTION_FONT_RED);
    }

    @MenuAction
    public void fontOrange() {
        chart.sendToChain(GUIConst.ACTION_FONT_ORANGE);
    }

    @MenuAction
    public void fontYellow() {
        chart.sendToChain(GUIConst.ACTION_FONT_YELLOW);
    }

    @MenuAction
    public void fontGreen() {
        chart.sendToChain(GUIConst.ACTION_FONT_GREEN);
    }

    @MenuAction
    public void fontBlue() {
        chart.sendToChain(GUIConst.ACTION_FONT_BLUE);
    }

    @MenuAction
    public void fontPurple() {
        chart.sendToChain(GUIConst.ACTION_FONT_PURPLE);
    }

    @MenuAction
    public void fontGray() {
        chart.sendToChain(GUIConst.ACTION_FONT_GRAY);
    }

    @MenuAction
    public void fontBlack() {
        chart.sendToChain(GUIConst.ACTION_FONT_BLACK);
    }

    @MenuAction
    public void resetStyle() {
        chart.sendToChain(GUIConst.ACTION_RESET_STYLE);
    }

    @MenuAction
    public void showStampBox() {
        main.sendToChain(GUIConst.ACTION_SHOW_STAMPBOX);
    }

    @MenuAction
    public void showSchemaBox() {
        main.sendToChain(GUIConst.ACTION_SHOW_SCHEMABOX);
    }

    @MenuAction
    public void showWaitingList() {
        main.sendToChain(GUIConst.ACTION_SHOW_WAITING_LIST);
    }

    @MenuAction
    public void showPatientSearch() { main.sendToChain(GUIConst.ACTION_SHOW_PATIENT_SEARCH); }

    @MenuAction
    public void changePassword() {
        main.sendToChain(GUIConst.ACTION_CHANGE_PASSWORD);
    }

    @MenuAction
    public void addUser() {
        main.sendToChain(GUIConst.ACTION_ADD_USER);
    }

    @MenuAction
    public void showAbout() {
        main.sendToChain(GUIConst.ACTION_SHOW_ABOUT);
    }

    @MenuAction
    public void findFirst() {
        chart.sendToChain(GUIConst.ACTION_FIND_FIRST);
    }

    @MenuAction
    public void findNext() {
        chart.sendToChain(GUIConst.ACTION_FIND_NEXT);
    }

    @MenuAction
    public void findPrevious() {
        chart.sendToChain(GUIConst.ACTION_FIND_PREVIOUS);
    }

    @MenuAction
    public void searchStamp() {
        chart.sendToChain(GUIConst.ACTION_SEARCH_STAMP);
    }

    public void build(JMenuBar menuBar) {

        actionMap = MenuActionManager.createActionMap(this);
        this.menuBar = menuBar;

        // ToolBar
        JToolBar fileBar = null;
        JToolBar editBar = null;
        if (chart != null) {
            fileBar = new JToolBar();
            fileBar.setName("fileBar");
            fileBar.setFloatable(false);
            fileBar.setOpaque(false);
            fileBar.setBorderPainted(false);

            editBar = new JToolBar();
            editBar.setName("editBar");
            editBar.setFloatable(false);
            editBar.setOpaque(false);
            editBar.setBorderPainted(false);

            toolPanel = new JPanel();
            BoxLayout layout = new BoxLayout(toolPanel, BoxLayout.X_AXIS);
            toolPanel.setLayout(layout);
            toolPanel.add(editBar);
            toolPanel.add(fileBar);
        }

        //
        // File メニュー
        //
        JMenu file = createMenu("ファイル", "fileMenu");

        // 新規カルテ
        file.add(createMenuItem("新規カルテ...", GUIConst.ACTION_NEW_KARTE,
                "N", GUIConst.ICON_FILE_16, "新しいカルテを作成します"));
        // 開く
        file.add(createMenuItem("開く...", GUIConst.ACTION_OPEN_KARTE,
                "O", GUIConst.ICON_OPEN_16, "カルテを開きます"));

        file.addSeparator();

        // 閉じる
        file.add(createMenuItem("閉じる", GUIConst.ACTION_CLOSE,
                "W", GUIConst.ICON_EMPTY_16, "カルテを閉じます"));
        // 保存
        file.add(createMenuItem("保存...", GUIConst.ACTION_SAVE,
                "S", GUIConst.ICON_SAVE_16, "カルテを保存します"));

        file.add(new JSeparator());

        // 削除
        file.add(createMenuItem("削除", GUIConst.ACTION_DELETE,
                null, GUIConst.ICON_DELETE_16, "削除します"));

        file.addSeparator();

        // 印刷設定
        file.add(createMenuItem("ページ設定...", GUIConst.ACTION_PRINTER_SETUP,
                null, GUIConst.ICON_EMPTY_16, "プリンタの設定をします"));
        // 印刷
        file.add(createMenuItem("プリント...", GUIConst.ACTION_PRINT,
                "P", GUIConst.ICON_PRINT_16, "プリントします"));

        // 終了 - Window のみ
        if (ClientContext.isWin()) {
            file.add(createMenuItem("終了", GUIConst.ACTION_PROCESS_EXIT,
                    "Q", GUIConst.ICON_EMPTY_16, "プログラムを終了します"));
        }

        //
        // Edit メニュー
        //
        JMenu edit = createMenu("編集", "editMenu");

        // 修正
        edit.add(createMenuItem("修正", GUIConst.ACTION_MODIFY_KARTE,
                "M", GUIConst.ICON_FILE_EDIT_16, "表示されているカルテを編集します"));

        edit.addSeparator();

        // Undo
        edit.add(createMenuItem("取り消す", GUIConst.ACTION_UNDO,
                "Z", GUIConst.ICON_UNDO_16, "作業を取り消します"));

        // Redo
        edit.add(createMenuItem("やり直す", GUIConst.ACTION_REDO,
                "shift Z", GUIConst.ICON_REDO_16, "作業をやり直します"));

        edit.addSeparator();

        // Cut
        edit.add(createMenuItem("カット", GUIConst.ACTION_CUT,
                "X", GUIConst.ICON_CUT_16, "選択部分をカットします"));
        // Copy
        edit.add(createMenuItem("コピー", GUIConst.ACTION_COPY,
                "C", GUIConst.ICON_COPY_16, "選択部分をコピーします"));
        // Paste
        edit.add(createMenuItem("ペースト", GUIConst.ACTION_PASTE,
                "V", GUIConst.ICON_PASTE_16, "カーソル位置にペーストします"));
        // SelectAll
        edit.add(createMenuItem("全てを選択", GUIConst.ACTION_SELECT_ALL,
                "A", GUIConst.ICON_EMPTY_16, "全てを選択します"));

        edit.addSeparator();

        // Find
        edit.add(createMenuItem("検索", GUIConst.ACTION_FIND_FIRST,
                "F", GUIConst.ICON_SEARCH_16, "表示されているカルテを検索します"));
        // Find Next
        edit.add(createMenuItem("次を検索", GUIConst.ACTION_FIND_NEXT,
                "G", GUIConst.ICON_EMPTY_16, "次を検索します"));
        // Find Previous
        edit.add(createMenuItem("前を検索", GUIConst.ACTION_FIND_PREVIOUS,
                "shift G", GUIConst.ICON_EMPTY_16, "前を検索します"));

        //
        // Karte メニュー
        //
        JMenu karte = createMenu("カルテ", "karteMenu");

        // CLAIM 送信
        karte.add(createMenuItem("CLAIM 送信", GUIConst.ACTION_SEND_CLAIM,
                "L", null, "CLAIM 送信します"));
        // 昇順/降順
        JRadioButtonMenuItem ascending = createRadioButtonMenuItem("昇順", GUIConst.ACTION_ASCENDING);
        JRadioButtonMenuItem descending = createRadioButtonMenuItem("降順", GUIConst.ACTION_DESCENDING);
        karte.add(ascending);
        karte.add(descending);

        ButtonGroup bg = new ButtonGroup();
        bg.add(ascending);
        bg.add(descending);

        // 修正履歴表示
        karte.add(createRadioButtonMenuItem("修正履歴表示", GUIConst.ACTION_SHOW_MODIFIED));

        // 環境設定. Mac の場合は SettingForMac で設定済み.
        if (ClientContext.isWin()) {
            karte.add(createMenuItem("環境設定...", GUIConst.ACTION_SET_KARTE_ENVIROMENT));
        }

        //
        // 挿入メニュー
        //
        JMenu insert = new JMenu();
        insert.setName("insertMenu");
        insert.setText(GUIConst.MENU_INSERT);
        if (chart != null) {
            insert.addMenuListener(chart); // ChartMediator
        }
        insert.add(createMenuItem("傷病名", "insertDisease"));
        insert.add(createMenuItem("テキスト", "insertText"));
        insert.add(createMenuItem("スタンプ", "insertStamp"));
        insert.add(createMenuItem("シェーマ", "insertSchema"));

        //
        // Text メニュー
        //
        JMenu text = new JMenu();
        text.setName("textMenu");
        text.setText(GUIConst.MENU_TEXT);
        if (chart != null) {
            text.addMenuListener(chart); // ChartMediator
        }
        // size
        JMenu size = new JMenu();
        setAction(size, "size", "サイズ");
        text.add(size);

        JMenuItem fontLarger = new JMenuItem();
        setAction(fontLarger, "fontLarger", "大きく");
        size.add(fontLarger);

        JMenuItem fontSmaller = new JMenuItem();
        setAction(fontSmaller, "fontSmaller", "小さく");
        size.add(fontSmaller);

        JMenuItem fontStandard = new JMenuItem();
        setAction(fontStandard, "fontStandard", "標準サイズ");
        size.add(fontStandard);

        // style
        JMenu style = new JMenu();
        setAction(style, "style", "スタイル");

        JMenuItem fontBold = new JMenuItem();
        setAction(fontBold, "fontBold", "ボールド");
        setAccelerator(fontBold, KeyEvent.VK_B);
        style.add(fontBold);

        JMenuItem fontItalic = new JMenuItem();
        setAction(fontItalic, "fontItalic", "イタリック");
        setAccelerator(fontItalic, KeyEvent.VK_I);
        style.add(fontItalic);

        JMenuItem fontUnderline = new JMenuItem();
        setAction(fontUnderline, "fontUnderline", "アンダーライン");
        setAccelerator(fontUnderline, KeyEvent.VK_U);
        style.add(fontUnderline);

        // justify
        JMenu justify = new JMenu();
        setAction(justify, "justify", "行揃え");
        text.add(justify);

        JMenuItem leftJustify = new JMenuItem();
        leftJustify.setName("leftJustify");
        leftJustify.setAction(actionMap.get("leftJustify"));
        leftJustify.setText("左揃え");
        //setAccelerator(leftJustify, KeyEvent.VK_OPEN_BRACKET);
        justify.add(leftJustify);

        JMenuItem centerJustify = new JMenuItem();
        centerJustify.setName("centerJustify");
        centerJustify.setAction(actionMap.get("centerJustify"));
        centerJustify.setText("中央揃え");
        //setAccelerator(centerJustify, KeyEvent.VK_CIRCUMFLEX);
        justify.add(centerJustify);

        JMenuItem rightJustify = new JMenuItem();
        rightJustify.setName("rightJustify");
        rightJustify.setAction(actionMap.get("rightJustify"));
        rightJustify.setText("右揃え");
        //setAccelerator(rightJustify, KeyEvent.VK_CLOSE_BRACKET);
        justify.add(rightJustify);

        //// Color ////
        JMenu color = new JMenu();
        color.setName("color");
        color.setAction(actionMap.get("color"));
        color.setText("カラー");
        text.add(color);

        JMenuItem fontRed = new JMenuItem();
        fontRed.setName("fontRed");
        fontRed.setAction(actionMap.get("fontRed"));
        fontRed.setText("赤");
        color.add(fontRed);

        JMenuItem fontOrange = new JMenuItem();
        fontOrange.setName("fontOrange");
        fontOrange.setAction(actionMap.get("fontOrange"));
        fontOrange.setText("橙");
        color.add(fontOrange);

        JMenuItem fontYellow = new JMenuItem();
        fontYellow.setName("fontYellow");
        fontYellow.setAction(actionMap.get("fontYellow"));
        fontYellow.setText("黄");
        color.add(fontYellow);

        JMenuItem fontGreen = new JMenuItem();
        fontGreen.setName("fontGreen");
        fontGreen.setAction(actionMap.get("fontGreen"));
        fontGreen.setText("緑");
        color.add(fontGreen);

        JMenuItem fontBlue = new JMenuItem();
        fontBlue.setName("fontBlue");
        fontBlue.setAction(actionMap.get("fontBlue"));
        fontBlue.setText("青");
        color.add(fontBlue);

        JMenuItem fontPurple = new JMenuItem();
        fontPurple.setName("fontPurple");
        fontPurple.setAction(actionMap.get("fontPurple"));
        fontPurple.setText("紫");
        color.add(fontPurple);

        JMenuItem fontGray = new JMenuItem();
        fontGray.setName("fontGray");
        fontGray.setAction(actionMap.get("fontGray"));
        fontGray.setText("灰色");
        color.add(fontGray);

        JMenuItem fontBlack = new JMenuItem();
        fontBlack.setName("fontBlack");
        fontBlack.setAction(actionMap.get("fontBlack"));
        fontBlack.setText("黒");
        color.add(fontBlack);

        // Tool
        JMenu tool = new JMenu();
        tool.setName("toolMenu");
        tool.setText("ツール");

        JMenuItem searchStamp = new JMenuItem();
        searchStamp.setName(GUIConst.ACTION_SEARCH_STAMP);
        searchStamp.setAction(actionMap.get(GUIConst.ACTION_SEARCH_STAMP));
        searchStamp.setText("スタンプ検索");
        setAccelerator(searchStamp, KeyEvent.VK_F, true);
        tool.add(searchStamp);

        JMenuItem showStampBox = new JMenuItem();
        showStampBox.setName("showStampBox");
        showStampBox.setAction(actionMap.get("showStampBox"));
        showStampBox.setText("スタンプ箱");
        setAccelerator(showStampBox, KeyEvent.VK_1);
        tool.add(showStampBox);

        JMenuItem showSchemaBox = new JMenuItem();
        showSchemaBox.setName("showSchemaBox");
        showSchemaBox.setAction(actionMap.get("showSchemaBox"));
        showSchemaBox.setText("シェーマ箱");
        setAccelerator(showSchemaBox, KeyEvent.VK_2);
        tool.add(showSchemaBox);

        JMenuItem showWaitingList = new JMenuItem();
        showWaitingList.setName("showWaitingList");
        showWaitingList.setAction(actionMap.get("showWaitingList"));
        showWaitingList.setText("受付リスト");
        setAccelerator(showWaitingList, KeyEvent.VK_3);
        tool.add(showWaitingList);

        JMenuItem showPatientSearch = new JMenuItem();
        showPatientSearch.setName("showPatientSearch");
        showPatientSearch.setAction(actionMap.get("showPatientSearch"));
        showPatientSearch.setText("患者検索");
        setAccelerator(showPatientSearch, KeyEvent.VK_4);
        tool.add(showPatientSearch);

        tool.add(new JSeparator());

        JMenuItem changePassword = new JMenuItem();
        changePassword.setName("changePassword");
        changePassword.setAction(actionMap.get("changePassword"));
        changePassword.setText("プロフィール変更");
        tool.add(changePassword);

        JMenuItem addUser = new JMenuItem();
        addUser.setName("addUser");
        addUser.setAction(actionMap.get("addUser"));
        addUser.setText("院内ユーザー登録");
        tool.add(addUser);

        // Help
        JMenu help = new JMenu();
        help.setName("helpMenu");
        help.setText("ヘルプ");

        if (ClientContext.isWin()) {
            JMenuItem showAbout = new JMenuItem();
            showAbout.setName("showAbout");
            showAbout.setAction(actionMap.get(GUIConst.ACTION_SHOW_ABOUT));
            help.add(showAbout);
        }


        setToolBar(fileBar, GUIConst.ACTION_SAVE, GUIConst.ICON_SAVE_32);
        setToolBar(fileBar, GUIConst.ACTION_PRINT, GUIConst.ICON_PRINT_32);
        setToolBar(editBar, GUIConst.ACTION_NEW_KARTE, GUIConst.ICON_FILE_32);
        setToolBar(editBar, GUIConst.ACTION_MODIFY_KARTE, GUIConst.ICON_FILE_EDIT_32);
        setToolBar(editBar, GUIConst.ACTION_UNDO, GUIConst.ICON_UNDO_32);
        setToolBar(editBar, GUIConst.ACTION_REDO, GUIConst.ICON_REDO_32);


        /******************************************************/
        // この時点で menuBar には既に window メニューが入っている (menuCount = 1 となっている)
        menuBar.add(file, 0);
        menuBar.add(edit, 1);
        menuBar.add(karte, 2);
        menuBar.add(text, 3);
        menuBar.add(tool, 4);
        // window menu = 5
        menuBar.add(help, 6);
    }

    private void setAccelerator(JMenuItem item, int key) {
        setAccelerator(item, key, false);
    }

    private void setAccelerator(JMenuItem item, int key, boolean shiftMask) {
        if (shiftMask) {
            item.setAccelerator(KeyStroke.getKeyStroke(key, (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
        } else {
            item.setAccelerator(KeyStroke.getKeyStroke(key, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
    }

    private void setAction(JMenuItem item, String key, String name) {
        setAction(item, key, name, null, null);
    }

    private void setAction(JMenuItem item, String key, String name, ImageIcon icon, String toolTipText) {
        item.setName(key);
        Action action = actionMap.get(key);
        action.putValue(Action.NAME, name);
        action.putValue(Action.SMALL_ICON, icon);
        action.putValue(Action.SHORT_DESCRIPTION, toolTipText);
        action.putValue("menuItem", item); // action から JMenuItem を取得できるようにする
        item.setAction(action);
    }

    /**
     * JMenu を作る.
     *
     * @param menuName メニュー表示名
     * @param actionKey action key
     * @return
     */
    private JMenu createMenu(String menuName, String actionKey) {
        JMenu menu = new JMenu();
        menu.setName(actionKey);
        menu.setText(menuName);
        Action action = actionMap.get(actionKey);
        if (Objects.nonNull(action)) {
            action.putValue(Action.NAME, menuName);
            menu.setAction(action);
        }
        return menu;
    }

    /**
     * JMenuItem を作る.
     *
     * @param menuName メニュー表示名
     * @param actionKey action key
     * @return JMenuItem
     */
    private JMenuItem createMenuItem(String menuName, String actionKey) {
        return createMenuItem(menuName, actionKey, null, null, null);
    }

    /**
     * JMenuItem を作る.
     *
     * @param menuName メニュー表示名
     * @param actionKey action key
     * @param keyStroke ショートカットキー
     * @return JMenuItem
     */
    private JMenuItem createMenuItem(String menuName, String actionKey, String keyStroke) {
        return createMenuItem(menuName, actionKey, null, null, keyStroke);
    }

    /**
     * JMenuItem を作る.
     *
     * @param menuName メニュー表示名
     * @param actionKey action key
     * @param keyStroke ショートカットキー
     * @param icon アイコン
     * @param toolTipText ツールチップ
     * @return JMenuItem
     */
    private JMenuItem createMenuItem(String menuName, String actionKey, String keyStroke, ImageIcon icon, String toolTipText) {
        JMenuItem item = new JMenuItem();
        item.setName(actionKey);
        Action action = actionMap.get(actionKey);
        action.putValue(Action.NAME, menuName);
        action.putValue(Action.SMALL_ICON, icon);
        action.putValue(Action.SHORT_DESCRIPTION, toolTipText);
        action.putValue(GUIConst.MENU_ITEM, item); // action から JMenuItem を取得できるようにする
        item.setAction(action);
        if (Objects.nonNull(keyStroke)) {
            item.setAccelerator(KeyStroke.getKeyStroke(SHORT_CUT_KEY_MASK + " " + keyStroke));
        }
        return item;
    }

    /**
     * JRadioButtonMenuItem を作る.
     *
     * @param menuName メニュー表示名
     * @param actionKey action key
     * @return JRadioButtonMenuItem
     */
    private JRadioButtonMenuItem createRadioButtonMenuItem(String menuName, String actionKey) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem();
        item.setName(actionKey);

        Action action = actionMap.get(actionKey);
        action.putValue(Action.NAME, menuName);
        action.putValue(GUIConst.MENU_ITEM, item); // action から JMenuItem を取得できるようにする
        item.setAction(action);

        return item;
    }

    private void setToolBar(JToolBar toolBar, String text, ImageIcon icon) {
        if (chart != null) {
            JButton button = new JButton();
            button.setBorderPainted(false);
            button.setAction(actionMap.get(text));
            button.setText(null);
            button.setIcon(icon);
            button.setOpaque(false);
            toolBar.add(button);
        }
    }
}

package open.dolphin.client;

import open.dolphin.helper.MenuActionManager;
import open.dolphin.helper.MenuActionManager.MenuAction;
import open.dolphin.helper.MenuSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Menu Factory.
 *
 * @author Minagawa, Kazushi
 * @author pns
 */
public class MenuFactory {

    private static final boolean isMac = System.getProperty("os.name").toLowerCase().startsWith("mac");
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

    public ActionMap getActionMap() {
        return actionMap;
    }

    @MenuAction
    public void newKarte() {
        chart.sendToChain("newKarte");
    }

    @MenuAction
    public void newDocument() {
        chart.sendToChain("newDocument");
    }

    @MenuAction
    public void openKarte() {
        main.sendToChain("openKarte");
    }

    @MenuAction
    public void close() {
        chart.sendToChain("close");
    }

    @MenuAction
    public void save() {
        chart.sendToChain("save");
    }

    @MenuAction
    public void delete() {
        chart.sendToChain("delete");
    }

    @MenuAction
    public void printerSetup() {
        main.sendToChain("printerSetup");
    }

    @MenuAction
    public void print() {
        chart.sendToChain("print");
    }

    @MenuAction
    public void processExit() {
        main.sendToChain("processExit");
    }

    @MenuAction
    public void modifyKarte() {
        chart.sendToChain("modifyKarte");
    }

    @MenuAction
    public void undo() {
        chart.sendToChain("undo");
    }

    @MenuAction
    public void redo() {
        chart.sendToChain("redo");
    }

    @MenuAction
    public void cut() {
        chart.sendToChain("cut"); //cut();
    }

    @MenuAction
    public void copy() {
        chart.sendToChain("copy"); //copy();
    }

    @MenuAction
    public void paste() {
        chart.sendToChain("paste"); //paste();
    }

    @MenuAction
    public void selectAll() {
        chart.sendToChain("selectAll");
    }

    @MenuAction
    public void sendClaim() {
        chart.sendToChain("sendClaim");
    }

    @MenuAction
    public void ascending() {
        chart.sendToChain("ascending");
    }

    @MenuAction
    public void descending() {
        chart.sendToChain("descending");
    }

    @MenuAction
    public void showModified() {
        chart.sendToChain("showModified");
    }

    @MenuAction
    public void setKarteEnviroment() {
        main.sendToChain("setKarteEnviroment");
    }

    @MenuAction
    public void insertDisease() {
    }

    @MenuAction
    public void insertText() {
    }

    @MenuAction
    public void insertSchema() {
    }

    @MenuAction
    public void insertStamp() {
    }

    @MenuAction
    public void selectInsurance() {
    }

    @MenuAction
    public void size() {
    }

    @MenuAction
    public void fontLarger() {
        chart.sendToChain("fontLarger");
    }

    @MenuAction
    public void fontSmaller() {
        chart.sendToChain("fontSmaller");
    }

    @MenuAction
    public void fontStandard() {
        chart.sendToChain("fontStandard");
    }

    @MenuAction
    public void style() {
    }

    @MenuAction
    public void fontBold() {
        chart.sendToChain("fontBold");
    }

    @MenuAction
    public void fontItalic() {
        chart.sendToChain("fontItalic");
    }

    @MenuAction
    public void fontUnderline() {
        chart.sendToChain("fontUnderline");
    }

    @MenuAction
    public void justify() {
    }

    @MenuAction
    public void leftJustify() {
        chart.sendToChain("leftJustify");
    }

    @MenuAction
    public void centerJustify() {
        chart.sendToChain("centerJustify");
    }

    @MenuAction
    public void rightJustify() {
        chart.sendToChain("rightJustify");
    }

    @MenuAction
    public void color() {
    }

    @MenuAction
    public void fontRed() {
        chart.sendToChain("fontRed");
    }

    @MenuAction
    public void fontOrange() {
        chart.sendToChain("fontOrange");
    }

    @MenuAction
    public void fontYellow() {
        chart.sendToChain("fontYellow");
    }

    @MenuAction
    public void fontGreen() {
        chart.sendToChain("fontGreen");
    }

    @MenuAction
    public void fontBlue() {
        chart.sendToChain("fontBlue");
    }

    @MenuAction
    public void fontPurple() {
        chart.sendToChain("fontPurple");
    }

    @MenuAction
    public void fontGray() {
        chart.sendToChain("fontGray");
    }

    @MenuAction
    public void fontBlack() {
        chart.sendToChain("fontBlack");
    }

    @MenuAction
    public void resetStyle() {
        chart.sendToChain("resetStyle");
    }

    @MenuAction
    public void showStampBox() {
        main.sendToChain("showStampBox");
    }

    @MenuAction
    public void showSchemaBox() {
        main.sendToChain("showSchemaBox");
    }

    @MenuAction
    public void showWaitingList() {
        main.sendToChain("showWaitingList");
    }

    @MenuAction
    public void focusDiagnosisInspector() {
        chart.sendToChain("focusDiagnosisInspector");
    }

    @MenuAction
    public void showPatientSearch() {
        main.sendToChain("showPatientSearch");
    }

    @MenuAction
    public void changePassword() {
        main.sendToChain("changePassword");
    }

    @MenuAction
    public void addUser() {
        main.sendToChain("addUser");
    }

    @MenuAction
    public void update1() {
        main.sendToChain("update");
    }

    @MenuAction
    public void browseDolphinSupport() {
        main.sendToChain("browseDolphinSupport");
    }

    @MenuAction
    public void browseDolphinProject() {
        main.sendToChain("browseDolphinProject");
    }

    @MenuAction
    public void browseMedXml() {
        main.sendToChain("browseMedXml");
    }

    @MenuAction
    public void showAbout() {
        main.sendToChain("showAbout");
    }

    @MenuAction
    public void findFirst() {
        chart.sendToChain("findFirst");
    }

    @MenuAction
    public void findNext() {
        chart.sendToChain("findNext");
    }

    @MenuAction
    public void findPrevious() {
        chart.sendToChain("findPrevious");
    }

    public void build(JMenuBar menuBar) {

        this.actionMap = MenuActionManager.getActionMap(this);
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

        // File
        JMenu file = new JMenu();
        file.setName("fileMenu");
        file.setText("ファイル");

        // 新規カルテ
        JMenuItem newKarte = new JMenuItem();
        newKarte.setName("newKarte");
        //setAction(newKarte, "newKarte", "新規カルテ...", GUIConst.ICON_KARTE_NEW_22, "新しいカルテを作成します");
        setAction(newKarte, "newKarte", "新規カルテ...", GUIConst.ICON_FILE_16, "新しいカルテを作成します");
        setAccelerator(newKarte, KeyEvent.VK_N);
        file.add(newKarte);
        //setToolBar(editBar, "newKarte", GUIConst.ICON_KARTE_NEW_22);
        setToolBar(editBar, "newKarte", GUIConst.ICON_FILE_32);

        // 開く
        JMenuItem openKarte = new JMenuItem();
        openKarte.setName("openKarte");
        //setAction(openKarte, "openKarte", "開く...", GUIConst.ICON_DOCUMENT_OPEN_22, "カルテを開きます");
        setAction(openKarte, "openKarte", "開く...", GUIConst.ICON_OPEN_16, "カルテを開きます");
        setAccelerator(openKarte, KeyEvent.VK_O);
        file.add(openKarte);

        file.add(new JSeparator());

        // 閉じる
        JMenuItem close = new JMenuItem();
        close.setName("close");
        setAction(close, "close", "閉じる", GUIConst.ICON_EMPTY_16, "カルテを閉じます");
        setAccelerator(close, KeyEvent.VK_W);
        file.add(close);

        // 保存
        JMenuItem save = new JMenuItem();
        save.setName("save");
        //setAction(save, "save", "保存...", GUIConst.ICON_FLOPPY_22, "保存します");
        setAction(save, "save", "保存...", GUIConst.ICON_SAVE_16, "保存します");
        setAccelerator(save, KeyEvent.VK_S);
        file.add(save);
        //setToolBar(fileBar, "save", GUIConst.ICON_FLOPPY_22);
        setToolBar(fileBar, "save", GUIConst.ICON_SAVE_32);

        file.add(new JSeparator());

        // 削除
        JMenuItem delete = new JMenuItem();
        delete.setName("delete");
        //setAction(delete, "delete", "削除", GUIConst.ICON_EDIT_DELETE_22, "削除します");
        setAction(delete, "delete", "削除", GUIConst.ICON_DELETE_16, "削除します");
        file.add(delete);

        file.add(new JSeparator());

        // 印刷設定
        JMenuItem printerSetup = new JMenuItem();
        printerSetup.setName("printerSetup");
        setAction(printerSetup, "printerSetup", "ページ設定...", GUIConst.ICON_EMPTY_16, "プリンタの設定をします");
        file.add(printerSetup);

        // 印刷
        JMenuItem print = new JMenuItem();
        print.setName("print");
        //setAction(print, "print", "プリント...", GUIConst.ICON_DOCUMENT_PRINT_22, "プリントします");
        setAction(print, "print", "プリント...", GUIConst.ICON_PRINT_16, "プリントします");
        setAccelerator(print, KeyEvent.VK_P);
        file.add(print);
        //setToolBar(fileBar, "print", GUIConst.ICON_DOCUMENT_PRINT_22);
        setToolBar(fileBar, "print", GUIConst.ICON_PRINT_32);

        // 終了 - Window のみ
        if (!isMac) {
            JMenuItem exit = new JMenuItem();
            exit.setName("processExit");
            setAction(exit, "processExit", "終了", GUIConst.ICON_EMPTY_16, "プログラムを終了します");
            file.add(exit);
            setAccelerator(exit, KeyEvent.VK_Q);
        }
        /******************************************************/

        // Edit
        JMenu edit = new JMenu();
        edit.setName("editMenu");
        edit.setText("編集");

        // 修正
        JMenuItem modifyKarte = new JMenuItem();
        modifyKarte.setName("modifyKarte");
        //setAction(modifyKarte, "modifyKarte", "修正", GUIConst.ICON_KARTE_EDIT_22, "表示されているカルテを編集します");
        setAction(modifyKarte, "modifyKarte", "修正", GUIConst.ICON_FILE_EDIT_16, "表示されているカルテを編集します");
        setAccelerator(modifyKarte, KeyEvent.VK_M);
        edit.add(modifyKarte);
        //setToolBar(editBar, "modifyKarte", GUIConst.ICON_KARTE_EDIT_22);
        setToolBar(editBar, "modifyKarte", GUIConst.ICON_FILE_EDIT_32);

        edit.add(new JSeparator());

        // Undo
        JMenuItem undo = new JMenuItem();
        undo.setName("undo");
        setAction(undo, "undo", "取り消す", GUIConst.ICON_UNDO_16, "作業を取り消します");
        //setAction(undo, "undo", "取り消す", GUIConst.ICON_EDIT_UNDO_22, "作業を取り消します");
        setAccelerator(undo, KeyEvent.VK_Z);
        edit.add(undo);
        //setToolBar(editBar, "undo", GUIConst.ICON_EDIT_UNDO_22);
        setToolBar(editBar, "undo", GUIConst.ICON_UNDO_32);

        // Redo
        JMenuItem redo = new JMenuItem();
        redo.setName("redo");
        //setAction(redo, "redo", "やり直す", GUIConst.ICON_EDIT_REDO_22, "作業をやり直します");
        setAction(redo, "redo", "やり直す", GUIConst.ICON_REDO_16, "作業をやり直します");
        setAccelerator(redo, KeyEvent.VK_Z, true);
        edit.add(redo);
        //setToolBar(editBar, "redo", GUIConst.ICON_EDIT_REDO_22);
        setToolBar(editBar, "redo", GUIConst.ICON_REDO_32);

        edit.add(new JSeparator());

        // Cut
        JMenuItem cut = new JMenuItem();
        cut.setName("cut");
        //setAction(cut, "cut", "カット", GUIConst.ICON_EDIT_CUT_22, "選択部分をカットします");
        setAction(cut, "cut", "カット", GUIConst.ICON_CUT_16, "選択部分をカットします");
        setAccelerator(cut, KeyEvent.VK_X);
        edit.add(cut);

        // Copy
        JMenuItem copy = new JMenuItem();
        copy.setName("copy");
        //setAction(copy, "copy", "コピー", GUIConst.ICON_EDIT_COPY_22, "選択部分をコピーします");
        setAction(copy, "copy", "コピー", GUIConst.ICON_COPY_16, "選択部分をコピーします");
        setAccelerator(copy, KeyEvent.VK_C);
        edit.add(copy);

        // Paste
        JMenuItem paste = new JMenuItem();
        paste.setName("paste");
        //setAction(paste, "paste", "ペースト", GUIConst.ICON_EDIT_PASTE_22, "カーソル位置にペーストします");
        setAction(paste, "paste", "ペースト", GUIConst.ICON_PASTE_16, "カーソル位置にペーストします");
        setAccelerator(paste, KeyEvent.VK_V);
        edit.add(paste);

        // SelectAll
        JMenuItem selectAll = new JMenuItem();
        selectAll.setName("selectAll");
        //setAction(selectAll, "selectAll", "全てを選択", GUIConst.ICON_EDIT_SELECT_ALL_22, "全てを選択します");
        setAction(selectAll, "selectAll", "全てを選択", GUIConst.ICON_EMPTY_16, "全てを選択します");
        setAccelerator(selectAll, KeyEvent.VK_A);
        edit.add(selectAll);

        edit.add(new JSeparator());

        // Find
        JMenuItem findFirst = new JMenuItem();
        findFirst.setName("findFirst");
        //setAction(findFirst, "findFirst", "検索", GUIConst.ICON_EDIT_FIND_22, "表示されているカルテを検索します");
        setAction(findFirst, "findFirst", "検索", GUIConst.ICON_SEARCH_16, "表示されているカルテを検索します");
        setAccelerator(findFirst, KeyEvent.VK_F);
        edit.add(findFirst);
        //setToolBar(editBar, "findFirst", GUIConst.ICON_EDIT_FIND_22);

        // Find Next
        JMenuItem findNext = new JMenuItem();
        findNext.setName("findNext");
        setAction(findNext, "findNext", "次を検索", GUIConst.ICON_EMPTY_16, "次を検索します");
        setAccelerator(findNext, KeyEvent.VK_G);
        edit.add(findNext);

        // Find Previous
        JMenuItem findPrevious = new JMenuItem();
        findPrevious.setName("findPrevious");
        setAction(findPrevious, "findPrevious", "前を検索", GUIConst.ICON_EMPTY_16, "前を検索します");
        setAccelerator(findPrevious, KeyEvent.VK_G, true);
        edit.add(findPrevious);

        /******************************************************/

        // Karte
        JMenu karte = new JMenu();
        karte.setName("karteMenu");
        karte.setText("カルテ");

        // CLAIM 送信
        JMenuItem sendClaim = new JMenuItem();
        sendClaim.setName("sendClaim");
        setAction(sendClaim, "sendClaim", "CLAIM 送信", null, "CLAIM 送信します");
        setAccelerator(sendClaim, KeyEvent.VK_L);
        karte.add(sendClaim);

        // 昇順
        JRadioButtonMenuItem ascending = new JRadioButtonMenuItem();
        ascending.setName("ascending");
        ascending.setAction(actionMap.get("ascending"));
        ascending.setText("昇順");
        actionMap.get("ascending").putValue("menuItem", ascending);
        karte.add(ascending);

        // 降順
        JRadioButtonMenuItem descending = new JRadioButtonMenuItem();
        descending.setName("descending");
        descending.setAction(actionMap.get("descending"));
        descending.setText("降順");
        actionMap.get("descending").putValue("menuItem", descending);
        karte.add(descending);

        // RadiButtonGroup
        ButtonGroup bg = new ButtonGroup();
        bg.add(ascending);
        bg.add(descending);


        // 修正履歴表示
        JCheckBoxMenuItem showModified = new JCheckBoxMenuItem();
        showModified.setName("showModified");
        showModified.setAction(actionMap.get("showModified"));
        showModified.setText("修正履歴表示");
        actionMap.get("showModified").putValue("menuItem", showModified);
        karte.add(showModified);

        // 環境設定
        JMenuItem setKarteEnviroment = new JMenuItem();
        setKarteEnviroment.setName("setKarteEnviroment");
        setKarteEnviroment.setAction(actionMap.get("setKarteEnviroment"));
        setKarteEnviroment.setText("環境設定...");
        setAccelerator(setKarteEnviroment, KeyEvent.VK_E);
        karte.add(setKarteEnviroment);

        /******************************************************/

        // Insert
        JMenu insert = new JMenu();
        insert.setName("insertMenu");
        insert.setText("挿入");
        if (chart != null) {
            insert.addMenuListener(chart);
        }

        JMenu insertDisease = new JMenu();
        insertDisease.setName("insertDisease");
        insertDisease.setAction(actionMap.get("insertDisease"));
        insertDisease.setText("傷病名");
        insert.add(insertDisease);

        JMenu insertText = new JMenu();
        insertText.setName("insertText");
        insertText.setAction(actionMap.get("insertText"));
        insertText.setText("テキスト");
        insert.add(insertText);

        JMenu insertSchema = new JMenu();
        insertSchema.setName("insertSchema");
        insertSchema.setAction(actionMap.get("insertSchema"));
        insertSchema.setText("シェーマ");
        insert.add(insertSchema);

        JMenu insertStamp = new JMenu();
        insertStamp.setName("insertStamp");
        insertStamp.setAction(actionMap.get("insertStamp"));
        insertStamp.setText("スタンプ");
        insert.add(insertStamp);

        /******************************************************/

        // Text
        JMenu text = new JMenu();
        text.setName("textMenu");
        text.setText("テキスト");
        if (chart != null) {
            text.addMenuListener(chart);
        }

        //// size ////
        JMenu size = new JMenu();
        size.setName("size");
        size.setAction(actionMap.get("size"));
        size.setText("サイズ");
        text.add(size);

        JMenuItem fontLarger = new JMenuItem();
        fontLarger.setName("fontLarger");
        fontLarger.setAction(actionMap.get("fontLarger"));
        fontLarger.setText("大きく");
        //setAccelerator(fontLarger, KeyEvent.VK_PLUS, true);
        size.add(fontLarger);

        JMenuItem fontSmaller = new JMenuItem();
        fontSmaller.setName("fontSmaller");
        fontSmaller.setAction(actionMap.get("fontSmaller"));
        fontSmaller.setText("小さく");
        //setAccelerator(fontSmaller, KeyEvent.VK_MINUS);
        size.add(fontSmaller);

        JMenuItem fontStandard = new JMenuItem();
        fontStandard.setName("fontStandard");
        fontStandard.setAction(actionMap.get("fontStandard"));
        fontStandard.setText("標準サイズ");
        //setAccelerator(fontStandard, KeyEvent.VK_NUMBER_SIGN, true);
        size.add(fontStandard);

        //// style ////
        JMenu style = new JMenu();
        style.setName("style");
        style.setAction(actionMap.get("style"));
        style.setText("スタイル");
        text.add(style);

        JMenuItem fontBold = new JMenuItem();
        fontBold.setName("fontBold");
        fontBold.setAction(actionMap.get("fontBold"));
        fontBold.setText("ボールド");
        setAccelerator(fontBold, KeyEvent.VK_B);
        style.add(fontBold);

        JMenuItem fontItalic = new JMenuItem();
        fontItalic.setName("fontItalic");
        fontItalic.setAction(actionMap.get("fontItalic"));
        fontItalic.setText("イタリック");
        setAccelerator(fontItalic, KeyEvent.VK_I);
        style.add(fontItalic);

        JMenuItem fontUnderline = new JMenuItem();
        fontUnderline.setName("fontUnderline");
        fontUnderline.setAction(actionMap.get("fontUnderline"));
        fontUnderline.setText("アンダーライン");
        setAccelerator(fontUnderline, KeyEvent.VK_U);
        style.add(fontUnderline);

        //// justify ////
        JMenu justify = new JMenu();
        justify.setName("justify");
        justify.setAction(actionMap.get("justify"));
        justify.setText("行揃え");
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

        /******************************************************/

        // Tool
        JMenu tool = new JMenu();
        tool.setName("toolMenu");
        tool.setText("ツール");

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

        JMenuItem focusDiagnosisInspector = new JMenuItem();
        focusDiagnosisInspector.setName("focusDiagnosisInspector");
        focusDiagnosisInspector.setAction(actionMap.get("focusDiagnosisInspector"));
        focusDiagnosisInspector.setText("病名インスペクタ");
        setAccelerator(focusDiagnosisInspector, KeyEvent.VK_3);
        tool.add(focusDiagnosisInspector);

        JMenuItem showWaitingList = new JMenuItem();
        showWaitingList.setName("showWaitingList");
        showWaitingList.setAction(actionMap.get("showWaitingList"));
        showWaitingList.setText("受付リスト");
        setAccelerator(showWaitingList, KeyEvent.VK_1, true);
        tool.add(showWaitingList);

        JMenuItem showPatientSearch = new JMenuItem();
        showPatientSearch.setName("showPatientSearch");
        showPatientSearch.setAction(actionMap.get("showPatientSearch"));
        showPatientSearch.setText("患者検索");
        setAccelerator(showPatientSearch, KeyEvent.VK_2, true);
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

        JMenuItem browseMedXml = new JMenuItem();
        browseMedXml.setName("browseMedXml");
        browseMedXml.setAction(actionMap.get("browseMedXml"));
        browseMedXml.setText("CLAIM 規格");
        help.add(browseMedXml);

        if (!isMac) {
            help.add(new JSeparator());

            JMenuItem showAbout = new JMenuItem();
            showAbout.setName("showAbout");
            showAbout.setAction(actionMap.get("showAbout"));
            help.add(showAbout);
        }

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

    private void setAction(JMenuItem item, String key, String name, ImageIcon icon, String toolTipText) {
        Action action = actionMap.get(key);
        action.putValue(Action.NAME, name);
        action.putValue(Action.SMALL_ICON, icon);
        action.putValue(Action.SHORT_DESCRIPTION, toolTipText);
        item.setAction(action);
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

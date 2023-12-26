package open.dolphin.helper;

import open.dolphin.client.GUIConst;
import open.dolphin.project.Project;
import open.dolphin.ui.PNSFrame;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Window Menu をサポートするためのクラス.
 * Factory method で WindowMenu をもつ JFrame を生成する.
 *
 * @author Minagawa, Kazushi
 * @author pns
 */
public class WindowSupport implements MenuListener {
    // frame を整列させるときの初期位置と移動幅
    final public static int INITIAL_X = 256;
    final public static int INITIAL_Y = 40;
    final public static int INITIAL_DX = 96;
    final public static int INITIAL_DY = 48;
    final private static List<WindowSupport> allWindows = new ArrayList<>();
    private static final String WINDOW_MENU_NAME = "ウインドウ";
    // メニューバーの増えた分の高さをセットするプロパティ名
    final public static String MENUBAR_HEIGHT_OFFSET_PROP = "menubar.height.offset";

    // Window support が提供するスタッフ
    // フレーム
    final private PNSFrame frame;
    // メニューバー
    final private JMenuBar menuBar;
    // ウインドウメニュー
    final private JMenu windowMenu;
    // Window Action
    final private Action windowAction;

    // プライベートコンストラクタ
    private WindowSupport(PNSFrame frame, JMenuBar menuBar, JMenu windowMenu, Action windowAction) {
        this.frame = frame;
        this.menuBar = menuBar;
        this.windowMenu = windowMenu;
        this.windowAction = windowAction;

        // インスペクタを整列するアクションだけはあらかじめ入れておく
        // こうしておかないと，１回 window メニューを開かないと accelerator が効かないことになる
        windowMenu.add(new ArrangeInspectorAction());
    }

    /**
     * WindowSupportを生成する.
     *
     * @param title フレームタイトル
     * @return WindowSupport
     */
    public static WindowSupport create(String title) {
        // フレームを生成する
        final PNSFrame f = new PNSFrame(title);
        f.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        // メニューバーを生成する
        JMenuBar mBar = new JMenuBar();
        f.setJMenuBar(mBar);

        // Window メニューを生成する
        JMenu wMenu = new JMenu(WINDOW_MENU_NAME);
        mBar.add(wMenu);

        // Windowメニューのアクション
        // 選択されたらフレームを前面にする
        Action wAction = new AbstractAction(title) {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.toFront();
            }
        };

        // インスタンスを生成する
        final WindowSupport windowSupport = new WindowSupport(f, mBar, wMenu, wAction);
        allWindows.add(windowSupport);

        // windowMenu にメニューリスナを設定しこのクラスで処理をする
        wMenu.addMenuListener(windowSupport);
        return windowSupport;
    }

    public static List<WindowSupport> getAllWindows() {
        return Collections.unmodifiableList(allWindows);
    }

    private static ImageIcon getIcon(JFrame frame) {
        return frame.isActive() ? GUIConst.ICON_STATUS_BUSY_16 : GUIConst.ICON_STATUS_OFFLINE_16;
    }

    public PNSFrame getFrame() {
        return frame;
    }

    public JMenuBar getMenuBar() { return menuBar; }

    public JMenu getWindowMenu() {
        return windowMenu;
    }

    private Action getWindowAction() { return windowAction; }

    public void dispose() {
        allWindows.remove(this);
        windowMenu.removeMenuListener(this);
        menuBar.setVisible(false);
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * ウインドウメニューが選択された場合，現在オープンしているウインドウのリストを使用し，
     * それらを選択するための MenuItem を追加する.
     * リストをインスペクタとカルテに整理 by pns
     */
    @Override
    public void menuSelected(MenuEvent e) {

        // 全てリムーブする
        JMenu wm = (JMenu) e.getSource();
        wm.removeAll();
        // リストから新規に生成する
        Action action;
        String name;
        int count = 0;
        // まず，カルテとインスペクタ以外
        for (WindowSupport ws : allWindows) {
            action = ws.getWindowAction();
            name = action.getValue(Action.NAME).toString();
            if (!name.contains("インスペクタ") && !name.contains("カルテ")) {
                wm.add(action);
                count++;
            }
        }
        // カルテ，インスペクタが開いていない場合はリターン
        if (allWindows.size() == count) {
            return;
        }

        count = 0;
        wm.addSeparator();

        // 次にカルテ
        for (WindowSupport ws : allWindows) {
            action = ws.getWindowAction();
            name = action.getValue(Action.NAME).toString();
            if (name.contains("カルテ")) {
                action.putValue(Action.SMALL_ICON, getIcon(ws.getFrame()));
                wm.add(action);
                count++;
            }
        }
        if (count != 0) {
            wm.addSeparator();
            count = 0;
        }

        // 次にインスペクタ
        for (WindowSupport ws : allWindows) {
            action = ws.getWindowAction();
            name = action.getValue(Action.NAME).toString();
            if (name.contains("インスペクタ")) {
                action.putValue(Action.SMALL_ICON, getIcon(ws.getFrame()));
                wm.add(action);
                count++;
            }
        }

        // "インスペクタを整列する" 項目を最後に
        if (count != 0) {
            wm.addSeparator();
            Action a = new ArrangeInspectorAction();
            wm.add(a);
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {
    }

    @Override
    public void menuCanceled(MenuEvent e) {
    }

    /**
     * インスペクタを整列する action.
     */
    private class ArrangeInspectorAction extends AbstractAction {

        public ArrangeInspectorAction() {
            initComponent();
        }

        private void initComponent() {
            putValue(Action.NAME, "インスペクタを整列");
            //putValue(Action.SMALL_ICON, GUIConst.ICON_WINDOWS_22);
            putValue(Action.SMALL_ICON, GUIConst.ICON_WINDOW_STACK_16);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta UNDERSCORE"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Preferences prefs = Project.getPreferences();
            int x = prefs.getInt(Project.ARRANGE_INSPECTOR_X, INITIAL_X);
            int y = prefs.getInt(Project.ARRANGE_INSPECTOR_Y, INITIAL_Y);
            int diffX = prefs.getInt(Project.ARRANGE_INSPECTOR_DX, INITIAL_DX);
            int diffY = prefs.getInt(Project.ARRANGE_INSPECTOR_DY, INITIAL_DY);

            int width = 0;
            int height = 0;

            JFrame f;
            for (WindowSupport ws : allWindows) {
                f = ws.getFrame();
                if (f.getTitle().contains("インスペクタ")) {
                    if (width == 0) {
                        width = f.getBounds().width;
                    }
                    if (height == 0) {
                        height = f.getBounds().height;
                    }

                    f.setBounds(x, y, width, height);
                    f.toFront();
                    x += diffX;
                    y += diffY;
                }
            }
        }
    }
}

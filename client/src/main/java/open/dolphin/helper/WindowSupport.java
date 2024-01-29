package open.dolphin.helper;

import open.dolphin.client.Chart;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.EditorFrame;
import open.dolphin.client.GUIConst;
import open.dolphin.project.Project;
import open.dolphin.ui.PNSFrame;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

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
public class WindowSupport<T> implements MenuListener {
    final static Logger logger = LoggerFactory.getLogger(WindowSupport.class);

    // frame を整列させるときの初期位置と移動幅
    final public static int INITIAL_X = 256, INITIAL_Y = 40, INITIAL_DX = 96, INITIAL_DY = 48;
    final private static List<WindowSupport<?>> allWindows = new ArrayList<>();
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
    // 内容 Dolphin (MainWindow), ChartImpl, EditorFrame, etc
    final private T content;
    // component bounds manager
    private Preferences pref;
    private String key; // preference key
    private int x, y, width, height;

    // プライベートコンストラクタ
    private WindowSupport(PNSFrame frame, JMenuBar menuBar, JMenu windowMenu, Action windowAction, T content) {
        this.frame = frame;
        this.menuBar = menuBar;
        this.windowMenu = windowMenu;
        this.windowAction = windowAction;
        this.content = content;

        pref = Preferences.userNodeForPackage(content.getClass());


        // インスペクタを整列するアクションだけはあらかじめ入れておく
        // こうしておかないと，１回 window メニューを開かないと accelerator が効かないことになる
        windowMenu.add(new ArrangeInspectorAction());
    }

    /**
     * WindowSupport を生成する.
     *
     * @param title フレームタイトル
     * @param content 内容
     * @return WindowSupport
     */
    public static <K> WindowSupport<K> create(String title, K content) {
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
        final WindowSupport<K> windowSupport = new WindowSupport<>(f, mBar, wMenu, wAction, content);
        allWindows.add(windowSupport);
        logger.info(content.getClass().getName() + " created " + allWindows.size());

        // windowMenu にメニューリスナを設定しこのクラスで処理をする
        wMenu.addMenuListener(windowSupport);
        return windowSupport;
    }

    /**
     * この window の内容を返す.
     *
     * @return content
     */
    public T getContent() { return content; }

    /**
     * List of all WindowSupport instances.
     *
     * @return unmodifiableList
     */
    public static List<WindowSupport<?>> getAllWindows() {
        return Collections.unmodifiableList(allWindows);
    }

    /**
     * List of all EditorFrame instances.
     *
     * @return 存在しない場合、size 0 で、null にはならない
     */
    public static List<EditorFrame> getAllEditorFrames() {
        return getAllWindows().stream()
            .map(WindowSupport::getContent).filter(EditorFrame.class::isInstance).map(EditorFrame.class::cast).toList();
    }

    /**
     * List of all ChartImpl instances.
     *
     * @return 存在しない場合、size 0 で、null にはならない
     */
    public static List<ChartImpl> getAllCharts() {
        return getAllWindows().stream()
            .map(WindowSupport::getContent).filter(ChartImpl.class::isInstance).map(ChartImpl.class::cast).toList();
    }

    /**
     * 指定された WindowSupport を先頭に移動する.
     *
     * @param windowSupport 先頭に移動する WindowSupport
     */
    public static void toTop(WindowSupport<?> windowSupport) {
        if (allWindows.remove(windowSupport)) {
            allWindows.add(0, windowSupport);
        }
    }

    /**
     * Returns frame.
     *
     * @return 管理している frame
     */
    public PNSFrame getFrame() { return frame; }

    /**
     * Returns JMenuBar.
     *
     * @return 管理している JMenuBar
     */
    public JMenuBar getMenuBar() { return menuBar; }

    public void dispose() {
        allWindows.remove(this);
        windowMenu.removeMenuListener(this);
        menuBar.setVisible(false);
        frame.setVisible(false);
        frame.dispose();

        // メモリ状況ログ
        long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
        long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
        long totalMemory = Runtime.getRuntime().totalMemory() / 1048576L;
        logger.info(String.format("free/max/total %d/%d/%d MB", freeMemory, maxMemory, totalMemory));
        logger.info(content.getClass().getName() + " removed " + allWindows.size());
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
        int count = 0;

        // まず，カルテとインスペクタ以外
        for (WindowSupport<?> ws : allWindows) {
            if (!(ws.getContent() instanceof Chart)) {
                wm.add(ws.windowAction);
                count++;
            }
        }
        // カルテ，インスペクタが開いていない場合はリターン
        if (allWindows.size() == count) { return; }

        count = 0;
        wm.addSeparator();

        // 次にカルテ (EditorFrame)
        for (WindowSupport<?> ws : allWindows) {
            if (ws.getContent() instanceof EditorFrame) {
                Action action = ws.windowAction;
                action.putValue(Action.SMALL_ICON, ws.getFrame().isActive() ? GUIConst.ICON_STATUS_BUSY_16 : GUIConst.ICON_STATUS_OFFLINE_16);
                wm.add(action);
                count++;
            }
        }
        if (count != 0) {
            wm.addSeparator();
            count = 0;
        }

        // 次にインスペクタ (ChartImpl)
        for (WindowSupport<?> ws : allWindows) {
            if (ws.getContent() instanceof ChartImpl) {
                Action action = ws.windowAction;
                action.putValue(Action.SMALL_ICON, ws.getFrame().isActive() ? GUIConst.ICON_STATUS_BUSY_16 : GUIConst.ICON_STATUS_OFFLINE_16);
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
            for (WindowSupport<?> ws : allWindows) {
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

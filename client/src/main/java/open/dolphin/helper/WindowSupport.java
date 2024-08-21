package open.dolphin.helper;

import open.dolphin.client.*;
import open.dolphin.project.Project;
import open.dolphin.ui.PNSFrame;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import java.util.Timer;
import java.util.prefs.Preferences;

/**
 * Window Menu をサポートするためのクラス.
 *
 * @author Minagawa, Kazushi
 * @author pns
 */
public class WindowSupport<T> implements MenuListener, ComponentListener {
    final static Logger logger = LoggerFactory.getLogger(WindowSupport.class);

    // frame を整列させるときの初期位置と移動幅
    final public static int INITIAL_X = 256, INITIAL_Y = 40, INITIAL_DX = 96, INITIAL_DY = 48;
    final private static List<WindowSupport<?>> allWindows = new ArrayList<>();
    private static final String WINDOW_MENU_NAME = "ウインドウ";
    // メニューバーの増えた分の高さをセットするプロパティ名
    final public static String MENUBAR_HEIGHT_OFFSET_PROP = "menubar.height.offset";

    // Window support が提供するスタッフ
    // フレーム
    private PNSFrame frame;
    // メニューバー
    final private JMenuBar menuBar;
    // ウインドウメニュー
    final private JMenu windowMenu;
    // Window Action
    final private Action windowAction;
    // 内容 Dolphin (MainWindow), ChartImpl, EditorFrame, etc
    final private T content;
    // component bounds manager
    final private Preferences pref;
    final private String keyX, keyY, keyW, keyH; // preference keys
    final private Deque<Rectangle> undoHistory; // bounds history
    private Rectangle prevBounds;
    final private RevertBoundsAction revertBoundsAction;
    private boolean boundChanged;
    private Timer timer;

    public WindowSupport(String title, T content) {
        this.content = content;

        // フレームを生成する
        frame = new PNSFrame(title);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        // メニューバーを生成する
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // Window メニューを生成する
        windowMenu = new JMenu(WINDOW_MENU_NAME);
        menuBar.add(windowMenu);

        // こうしておかないと，１回 window メニューを開かないと accelerator が効かないことになる
        windowMenu.add(new ArrangeInspectorAction());
        windowMenu.add(new RevertBoundsAction());

        // Windowメニューのアクション
        // 選択されたらフレームを前面にする
        windowAction = new AbstractAction(title) {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.toFront();
            }
        };

        // bounds manager
        String key = content.getClass().getName();
        keyX = key + "_x";
        keyY = key + "_y";
        keyW = key + "_width";
        keyH = key + "_height";
        pref = Preferences.userNodeForPackage(content.getClass());
        undoHistory = new UndoHistory();
        prevBounds = new Rectangle(pref.getInt(keyX, 100), pref.getInt(keyY, 50), pref.getInt(keyW, 1280), pref.getInt(keyH, 760));
        revertBoundsAction = new RevertBoundsAction();
        revertBoundsAction.setEnabled(false);
        frame.setBounds(prevBounds);
        //logger.info(key + ":" + frame.getBounds());

        // リスナ
        frame.addComponentListener(this);
        windowMenu.addMenuListener(this);

        allWindows.add(this);
        //logger.info(content.getClass().getName() + " created " + allWindows.size());
    }

    /**
     * この window の内容を返す.
     *
     * @return content
     */
    public T getContent() {
        return content;
    }

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
    public PNSFrame getFrame() {
        return frame;
    }

    /**
     * Returns JMenuBar.
     *
     * @return 管理している JMenuBar
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * 終了処理
     */
    public void dispose() {
        if (Objects.nonNull(timer)) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        frame.removeComponentListener(WindowSupport.this);
        windowMenu.removeMenuListener(WindowSupport.this);

        // bounds 記録
        Rectangle r = frame.getBounds();
        pref.putInt(keyX, r.x);
        pref.putInt(keyY, r.y);
        pref.putInt(keyW, r.width);
        pref.putInt(keyH, r.height);

        // リソース解放
        allWindows.remove(WindowSupport.this);
        menuBar.setVisible(false);
        frame.setVisible(false);
        frame.dispose();
        frame = null;

        // 状況ログ
//        SwingUtilities.invokeLater(() -> {
//            long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
//            long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
//            long totalMemory = Runtime.getRuntime().totalMemory() / 1048576L;
//            logger.info(String.format("free/max/total %d/%d/%d MB", freeMemory, maxMemory, totalMemory));
//            logger.info(content.getClass().getName() + " removed " + allWindows.size() + "(" + Window.getOwnerlessWindows().length + ")");
//            int count = 0;
//            for (Window  w : Window.getOwnerlessWindows()) {
//                if (w instanceof PNSFrame f) {
//                    logger.info(++count + ":" + f.getTitle());
//                }
//            }
//        });
    }

    /**
     * save new bounds to preferences.
     */
    private class FlushTask extends TimerTask {
        @Override
        public void run() {
            if (boundChanged) {
                prevBounds = new Rectangle(frame.getBounds());
            }
            boundChanged = false;
            timer = null;
            //logger.info(content.getClass().getName() + ":: " + frame.getBounds());
        }
    }

    /**
     * 500 msec 以内の変更は記録しない処理.
     */
    private void restartTimer() {
        if (Objects.nonNull(timer)) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(new FlushTask(), 500);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        if (!boundChanged) {
            // この時点で、frame は既に少し動いている
            undoHistory.addLast(new Rectangle(prevBounds));
        }
        boundChanged = true;
        restartTimer();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        componentMoved(e);
    }

    @Override
    public void menuDeselected(MenuEvent e) {
    }

    @Override
    public void menuCanceled(MenuEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    /**
     * target frame を画面中央に設定する.
     */
    public void toCenter() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = frame.getSize();
        int x = (screenSize.width - size.width) / 2;
        int y = (screenSize.height - size.height) / (Dolphin.forMac? 3:2);
        frame.setBounds(x, y, size.width, size.height);
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

        // undo resize or move
        wm.add(revertBoundsAction);

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

    /**
     * ウインドウの大きさ・位置変更 undo のための deque.
     */
    private class UndoHistory extends ArrayDeque<Rectangle> {
        @Override
        public void addLast(@NotNull Rectangle r) {
            if (!frame.getBounds().equals(r)) {
                super.addLast(r);
                checkState();
            }
        }
        @NotNull
        @Override
        public Rectangle removeLast() {
            Rectangle r = super.removeLast();
            checkState();
            return r;
        }
        private void checkState() {
            revertBoundsAction.setEnabled(!this.isEmpty());
        }
    }

    /**
     * ウインドウの大きさ・位置変更 undo action.
     */
    private class RevertBoundsAction extends AbstractAction {
        public RevertBoundsAction() {
            putValue(Action.NAME, "ウインドウの位置を戻す");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift meta UNDERSCORE"));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!undoHistory.isEmpty()) {
                frame.setBounds(undoHistory.removeLast());
            }
        }
    }

    /**
     * インスペクタを整列する action.
     */
    private class ArrangeInspectorAction extends AbstractAction {

        public ArrangeInspectorAction() {
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

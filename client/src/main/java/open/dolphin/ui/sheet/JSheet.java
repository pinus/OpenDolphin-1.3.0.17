package open.dolphin.ui.sheet;

import open.dolphin.ui.PNSFrame;
import open.dolphin.ui.PNSOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * JSheet. Based on Java Swing Hacks #42.
 *
 * @author pns
 */
public class JSheet extends JWindow implements ActionListener, MouseListener {
    public static final Dimension FILE_CHOOSER_SIZE = new Dimension(500, 500);
    public static final int INCOMING = 1;
    public static final int OUTGOING = -1;
    public static final int ANIMATION_OFFSET = 48;
    // アニメーションする時間 msec
    public static final float ANIMATION_DURATION = 100;
    // 書き換えの周期 msec
    public static final int ANIMATION_SLEEP = 2;
    // Owner window
    private Window owner;
    // 表示する JOptionPane / JFileChooser を入れる JPanel
    private JComponent sourcePane;
    // sourcePane を作るための JDialog
    private JDialog sourceDialog;
    // JSheet の ContentPane
    private JPanel content;
    // glass pane
    private Component glassPane;
    // キー入力を横取りするための KeyEventDispatcher
    private static SheetKeyEventDispatcher sheetKeyEventDispatcher;
    // Modal にするための SecondaryLoop
    private SecondaryLoop secondaryLoop;
    // 徐々に大きくなる JPanel
    private AnimatingSheet animatingSheet;

    private boolean animating;
    private int animationDirection;
    private Timer animationTimer;
    private long animationStart;

    private SheetListener sheetListener;
    private Component focusOwner;

    private final Logger logger = LoggerFactory.getLogger(JSheet.class);

    public JSheet(Window owner) {
        super(owner);
        init(owner);
    }

    /**
     * JOptionPane を source とする JSheet を作成する.
     *
     * @param pane JOptionPane
     * @param parentComponent parent window or component
     * @return JSheet
     */
    public static JSheet createDialog(final JOptionPane pane, Component parentComponent) {
        // create corresponding dialog
        pane.setBorder(new SheetBorder());
        JDialog dialog = pane.createDialog(null);
        dialog.getRootPane().putClientProperty("JRootPane.useWindowDecorations", false);
        dialog.pack();

        // create JSheet
        Window owner = JOptionPane.getFrameForComponent(parentComponent);
        JSheet js = new JSheet(owner);
        js.setSourceDialog(dialog);

        if (pane.getInitialValue() instanceof JButton b) {
            js.getRootPane().setDefaultButton(b);
        }

        // SheetKeyEventDispatcher に JOptionPane を登録
        sheetKeyEventDispatcher.setJOptionPane(pane);

        return js;
    }

    /**
     * JOptionPane を Sheet で表示する. listener に通知されるまでブロックされる.
     *
     * @param pane            JOptionPane
     * @param parentComponent parent window or component
     * @param listener        SheetListener
     */
    public static void showSheet(JOptionPane pane, Component parentComponent, SheetListener listener) {
        JSheet js = createDialog(pane, parentComponent);
        js.addSheetListener(listener);
        js.setVisible(true);
    }

    /**
     * JFileChooser を Sheet で表示する.
     *
     * @param chooser JFileChooser
     * @param parentComponent parent window or component
     * @param approveButtonText "保存" "開く" 等
     * @param listener SheetListener
     */
    public static void showSheet(JFileChooser chooser, Component parentComponent, String approveButtonText, SheetListener listener) {
        // create corresponding dialog
        chooser.setApproveButtonText(approveButtonText);
        chooser.setPreferredSize(FILE_CHOOSER_SIZE);
        chooser.setMaximumSize(FILE_CHOOSER_SIZE);
        chooser.setMinimumSize(FILE_CHOOSER_SIZE);
        chooser.setBorder(new SheetBorder());

        JDialog dialog = new JDialog();
        dialog.getRootPane().putClientProperty("JRootPane.useWindowDecorations", false);
        dialog.add(chooser);
        dialog.pack();

        // create JSheet
        Window owner = JOptionPane.getFrameForComponent(parentComponent);
        JSheet js = new JSheet(owner);
        js.setSourceDialog(dialog);
        js.addSheetListener(listener);
        js.setVisible(true);
    }

    /**
     * 保存 JFileChooser を表示する.
     *
     * @param chooser JFileChooser
     * @param parent parent window or component
     * @param listener SheetListener
     */
    public static void showSaveSheet(JFileChooser chooser, Component parent, SheetListener listener) {
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        showSheet(chooser, parent, "保存", listener);
    }

    /**
     * 開く JFileChooser を表示する.
     *
     * @param chooser JFileChooser
     * @param parent parent window or component
     * @param listener SheetListener
     */
    public static void showOpenSheet(JFileChooser chooser, Component parent, SheetListener listener) {
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        showSheet(chooser, parent, "開く", listener);
    }

    /**
     * JOptionPane.showConfirmDialog 互換.
     *
     * @param parentComponent parent
     * @param message message
     * @param title title
     * @param optionType option type
     * @param messageType message type
     * @return answer int
     */
    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType) {
        return showOptionDialog(parentComponent, message, title, optionType, messageType, null, null, null);
    }

    /**
     * JOptionPane.showMessageDialog 互換.
     *
     * @param parentComponent parent
     * @param message message
     * @param title title
     * @param messageType message type
     */
    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        showOptionDialog(parentComponent, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, null, null);
    }

    /**
     * JOptionPane.showOptionDialog 互換.
     *
     * @param parentComponent parent
     * @param message message
     * @param title title
     * @param optionType option type
     * @param messageType message type
     * @param icon icon
     * @param options options
     * @param initialValue initial value
     * @return answer int
     */
    public static int showOptionDialog(Component parentComponent, Object message, String title,
                                       int optionType, int messageType, Icon icon, final Object[] options, Object initialValue) {

        JOptionPane pane = new PNSOptionPane(message, messageType, optionType, icon, options, initialValue);
        pane.setInitialValue(initialValue);
        pane.selectInitialValue();
        final int[] answer = new int[1];
        JSheet.showSheet(pane, parentComponent, se -> answer[0] = se.getOption());

        return answer[0];
    }

    /**
     * その component に既に JSheet が表示されているかどうか.
     *
     * @param parentComponent parent
     * @return true if already shown
     */
    public static boolean isAlreadyShown(Component parentComponent) {
        Window window = JOptionPane.getFrameForComponent(parentComponent);
        Window[] windowList = window.getOwnedWindows();
        for (Window w : windowList) {
            if (w instanceof JSheet sheet && sheet.isVisible() && !sheet.isHiding()) {
                // すでに JSheet が表示されている
                return true;
            }
        }
        return false;
    }

    private void init(Window owner) {
        this.owner = owner;
        sheetKeyEventDispatcher = new SheetKeyEventDispatcher();

        setBackground(new Color(0, 0, 0, 0));
        getRootPane().putClientProperty("Window.shadow", Boolean.FALSE);

        content = (JPanel) getContentPane();
        content.setLayout(new BorderLayout());
        animatingSheet = new AnimatingSheet();
        animationTimer = new Timer(ANIMATION_SLEEP, this);
        animationTimer.setInitialDelay(0);

        // リアルタイムの書き直しにならない.
        owner.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) { locateSheet(); }
            @Override
            public void componentMoved(ComponentEvent e) { locateSheet(); }
            @Override
            public void componentShown(ComponentEvent e) { }
            @Override
            public void componentHidden(ComponentEvent e) { }
        });

        // JFrame と JDialog で分けざるを得ない処理
        if (owner instanceof JFrame w) {
            glassPane = w.getGlassPane();
        } else {
            JDialog w = (JDialog) owner;
            glassPane = w.getGlassPane();
        }
        ((JComponent) glassPane).putClientProperty("blockglass.show.ticker", false);
    }

    /**
     * JSheet の表示位置を設定する.
     */
    private void locateSheet() {
        if (!owner.isShowing()) { return; }

        Point loc = owner.getLocationOnScreen();
        Dimension ownerSize = owner.getSize();
        Dimension sourcePaneSize = sourcePane.getSize();

        // Sheet をセンタリング
        loc.x += (ownerSize.width - sourcePaneSize.width) / 2;
        loc.y += (ownerSize.height - sourcePaneSize.height) / 2 - ANIMATION_OFFSET;

        // 右端，左端の処理
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (loc.x < 0) {
            loc.x = 0;
        }
        if (screenSize.width < loc.x + sourcePaneSize.width) {
            loc.x = screenSize.width - sourcePaneSize.width;
        }

        setBounds(loc.x, loc.y, sourcePaneSize.width, sourcePaneSize.height);
        toFront();
    }

    /**
     * SheetListener を登録する. JOptionPane の場合
     * <ul>
     * <li>YES_OPTION = 0
     * <li>NO_OPTION = 1
     * <li>CANCEL_OPTION = 2
     * <li>CLOSED_OPTION = -1
     * </ul>
     * JFileChooser の場合
     * <ul>
     * <li>APPROVE_OPTION = 0
     * <li>CANCEL_OPTION = 1
     * <li>ERROR_OPTION = -1;
     * </ul>
     *
     * @param listener SheetListener
     */
    public void addSheetListener(SheetListener listener) {
        sheetListener = listener;
    }

    /**
     * ソースとなる Dialog をセットしてリスナを付ける.
     *
     * @param dialog JDialog
     */
    public void setSourceDialog(JDialog dialog) {
        sourceDialog = dialog;
        sourcePane = (JComponent) dialog.getContentPane();
        connectListeners(sourcePane.getComponent(0)); // JOptionPane or JFileChooser
        setTransparent(sourcePane);
    }

    /**
     * SourcePane の背景を透明にする.
     *
     * @param c component to be transparent
     */
    public void setTransparent(Component c) {
        if (c instanceof JComponent jc) {
            jc.setOpaque(false);
        }
        if (c instanceof Container cont) {
            for (Component comp : cont.getComponents()) {
                setTransparent(comp);
            }
        }
    }

    /**
     * リスナを SheetListener にブリッジする.
     *
     * @param c component to listen
     */
    private void connectListeners(Component c) {
        if (c instanceof JOptionPane jop) {
            c.addPropertyChangeListener(e -> {
                if (e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) {
                    // SheetEvent の設定
                    SheetEvent se = new SheetEvent(e.getSource());

                    // 戻り値: int の場合と String の場合がある
                    Object val = e.getNewValue();
                    Object[] options = jop.getOptions();

                    if (val instanceof Integer num) {
                        // intで返ってきたとき
                        se.setOption(num);

                    } else {
                        // String で返ってきたときは, 何番目かを返す
                        for (int i = 0; i < options.length; i++) {
                            if (options[i].equals(val)) {
                                se.setOption(i);
                                break;
                            }
                        }
                    }
                    sheetListener.optionSelected(se);
                    setVisible(false);
                }
            });

        } else if (c instanceof JFileChooser jfc) {
            jfc.addActionListener(e -> {
                // CANCEL_SELECTION = "CancelSelection";
                // APPROVE_SELECTION = "ApproveSelection";
                SheetEvent se = new SheetEvent(e.getSource());
                if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                    se.setOption(JFileChooser.APPROVE_OPTION);
                } else {
                    se.setOption(JFileChooser.CANCEL_OPTION);
                }
                sheetListener.optionSelected(se);
                setVisible(false);
            });
        }
    }

    /**
     * Sheet を表示／消去する. SecondaryLoop を使って modal にする.
     *
     * @param visible true to show, false to hide
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            if (isVisible()) {
                logger.info("setVisible called, but is already visible");
                return;
            }
            owner.toFront();

            // アニメーションは EDT で表示される.
            SwingUtilities.invokeLater(this::showSheet);

            // フォーカス解除
            focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            // キー入力を横取りする
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(sheetKeyEventDispatcher);

            super.setVisible(true);
            glassPane.addMouseListener(this);
            glassPane.setVisible(true);

            // modal にするために SecondaryLoop に入る
            Toolkit tk = Toolkit.getDefaultToolkit();
            EventQueue eq = tk.getSystemEventQueue();
            secondaryLoop = eq.createSecondaryLoop();
            if (!secondaryLoop.enter()) {
                secondaryLoop = null;
                throw new RuntimeException("Could not enter secondary loop.");
            }

        } else {
            // GlassPane を元に戻す
            glassPane.setVisible(false);
            glassPane.removeMouseListener(this);

            // キー入力横取りの中止と, フォーカス返還
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(sheetKeyEventDispatcher);

            if (Objects.nonNull(focusOwner)) {
                focusOwner.requestFocusInWindow();
            }

            if (Objects.nonNull(secondaryLoop)) {
                secondaryLoop.exit();
                secondaryLoop = null;
            }
            // hideSheet -> animation -> super.setVisible(false) となる
            hideSheet();
        }
    }

    /**
     * Sheet を表示する.
     */
    public void showSheet() {
        animationDirection = INCOMING;
        startAnimation();
    }

    /**
     * JSheet を消す.
     */
    public void hideSheet() {
        animationDirection = OUTGOING;
        startAnimation();
    }

    /**
     * 消去アニメーション中かどうか.
     *
     * @return 消去アニメーション中の場合 true
     */
    public boolean isHiding() {
        return animationDirection == OUTGOING && animating;
    }

    /**
     * JSheet を出し入れするアニメーションを開始する.
     */
    private void startAnimation() {
        content.removeAll();

        animatingSheet.setSource(sourcePane);
        content.add(animatingSheet, BorderLayout.CENTER);

        locateSheet();

        // start animation timer
        animationStart = System.currentTimeMillis();
        animating = true;
        animationTimer.start();
    }

    /**
     * アニメーションを終了する.
     */
    private void stopAnimation() {
        animationTimer.stop();
        animating = false;
    }

    /**
     * アニメーション終了後の Sheet が表示された状態.
     */
    private void finishShowingSheet() {
        content.removeAll();
        content.add(sourcePane, BorderLayout.CENTER);
        repaint();
    }

    /**
     * isActive を偽装する.
     *
     * @return always true
     */
    @Override
    public boolean isActive() {
        return true;
    }

    /**
     * Timer で呼ばれて animatingSheet を描画する.
     *
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (animating) {
            // calculate height to show
            float animationPercent
                = (System.currentTimeMillis() - animationStart) / ANIMATION_DURATION;
            animationPercent = Math.min(1.0f, animationPercent);
            animatingSheet.setAnimationPercent(animationPercent, animationDirection);
            animatingSheet.repaint();

            // 終了処理
            if (animationPercent >= 1.0f) {
                stopAnimation();
                if (animationDirection == INCOMING) {
                    finishShowingSheet();
                } else {
                    content.removeAll();
                    super.setVisible(false);
                    sourceDialog.dispose();
                    sourceDialog = null;
                    dispose();
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) { toFront();}

    @Override
    public void mouseReleased(MouseEvent e) { toFront(); }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * 下から描いていく JPaenl.
     */
    private static class AnimatingSheet extends JPanel {
        private final Dimension animatingSize = new Dimension(1, 1);
        private BufferedImage offscreenImage;
        private float animationPercent;
        private int posY;
        private int direction;

        public AnimatingSheet() {
            super();
            setBackground(new Color(0,0,0,0));
        }

        public void setSource(JComponent src) {
            animatingSize.width = src.getWidth();
            animatingSize.height = src.getHeight();
            makeOffscreenImage(src);
        }

        public void setAnimationPercent(float percent, int dir) {
            animationPercent = percent;
            direction = dir;
            posY = (direction == INCOMING)
                ? (int) ( animationPercent * ANIMATION_OFFSET )
                : (int) ( (1.0f - animationPercent) * ANIMATION_OFFSET );
        }

        private void makeOffscreenImage(JComponent source) {
            offscreenImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
            source.paint(offscreenGraphics);
        }

        @Override
        public Dimension getPreferredSize() {
            return animatingSize;
        }

        @Override
        public Dimension getMinimumSize() {
            return animatingSize;
        }

        @Override
        public Dimension getMaximumSize() {
            return animatingSize;
        }

        @Override
        public void paint(Graphics gr) {
            Graphics2D g = (Graphics2D) gr;
            float alpha = direction == INCOMING
                ? animationPercent
                : 1.0f - animationPercent;
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g.setComposite(ac);
            g.drawImage(offscreenImage, 0, posY - ANIMATION_OFFSET, this);
        }
    }

    /**
     * キー入力を横取りして使う.
     */
    private class SheetKeyEventDispatcher implements KeyEventDispatcher {
        private JOptionPane optionPane;
        private JButton defaultButton;
        private final KeyStroke escapeKey = KeyStroke.getKeyStroke("ESCAPE");
        private final KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            // アニメーション途中でキー入力があった場合の処理
            if (JSheet.this.isActive() && animating && animationDirection == INCOMING) {
                stopAnimation();
                finishShowingSheet();
            }

            if (Objects.nonNull(optionPane)) {
                KeyStroke strokeForEvent = KeyStroke.getKeyStrokeForEvent(e);

                if (strokeForEvent.equals(escapeKey)) {
                    // ESCAPE
                    optionPane.setValue(JOptionPane.CLOSED_OPTION);
                } else if (strokeForEvent.equals(enterKey)) {
                    // ENTER
                    defaultButton.doClick();
                } else {
                    // InputMap に入っているキーは次に回す
                    InputMap map = JSheet.this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
                    KeyStroke[] allKeys = map.allKeys();
                    if (Objects.nonNull(allKeys)) {
                        for (KeyStroke key : allKeys) {
                            if (strokeForEvent.equals(key)) {
                                // false を返すと次の KeyEventDispatcher に回る
                                return false;
                            }
                        }
                    }
                }
            }
            // キーブロック
            return true;
        }

        /**
         * JOptionPane を登録する.
         *
         * @param pane JOptionPane
         */
        public void setJOptionPane(JOptionPane pane) {
            optionPane = pane;

            // JOptionPane からボタンを取り出して, default ボタンをセットする
            Window w = SwingUtilities.getWindowAncestor(pane);
            if (w instanceof JFrame f) {
                defaultButton = f.getRootPane().getDefaultButton();
            } else if (w instanceof JDialog d) {
                defaultButton = d.getRootPane().getDefaultButton();
            } else {
                Component[] components = pane.getComponents();
                java.util.List<Component> cc = Arrays.asList(components);
                while (!cc.isEmpty()) {
                    java.util.List<Component> stack = new ArrayList<>();
                    for (Component c : cc) {
                        if (c instanceof JButton button) {
                            if (button.isDefaultButton()) {
                                defaultButton = button;
                            }

                        } else if (c instanceof JComponent comp) {
                            components = comp.getComponents();
                            stack.addAll(Arrays.asList(components));
                        }
                    }
                    cc = stack;
                }
            }
        }
    }

    public static void main(String[] arg) {
        PNSFrame frame = new PNSFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel(new ImageIcon(JSheet.class.getResource("/schemaeditor/usagi.jpg")));

        JButton b1 = new JButton("Create Dialog");
        b1.addActionListener(e -> {
            JOptionPane optionPane = new JOptionPane("JSheet.createDialog", JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION, null, new String[]{"OK", "キャンセル", "破棄"}, "OK");

            JSheet sheet = JSheet.createDialog(optionPane, frame);
            sheet.addSheetListener(se -> {
                // Escape を押したときは JOptionPane.CLOSED_OPTION (=-1) が返る
                System.out.println("option = " + se.getOption());
            });
            sheet.setVisible(true);
            System.out.println("Create Dialog ended");
        });

        JButton b2 = new JButton("Show Sheet");
        b2.addActionListener(e -> {
            JOptionPane optionPane = new JOptionPane("JSheet.showSheet",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION);

            JSheet.showSheet(optionPane, frame, se -> System.out.println("option = " + se.getOption()));
            System.out.println("Show Sheet ended");
        });

        JButton b3 = new JButton("Show Save");
        b3.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            JSheet.showSaveSheet(chooser, frame, se -> {
                System.out.println("option = " + se.getOption());
                System.out.println("selected file = " + chooser.getSelectedFile());
            });
            System.out.println("Show Save ended");
        });

        JButton b4 = new JButton("Show Open");
        b4.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            JSheet.showOpenSheet(chooser, frame, se -> {
                System.out.println("option = " + se.getOption());
                System.out.println("selected file = " + chooser.getSelectedFile());
            });
            System.out.println("Show Open ended");
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalGlue());
        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(b4);
        panel.add(Box.createHorizontalGlue());

        frame.add(label, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocation(600, 200);
        frame.setVisible(true);
    }
}

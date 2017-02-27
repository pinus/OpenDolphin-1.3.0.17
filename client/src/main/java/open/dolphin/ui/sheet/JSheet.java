package open.dolphin.ui.sheet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import open.dolphin.ui.MyJSheet;

/**
 * JSheet. Based on Java Swing Hacks #42.
 *
 * @author pns
 */
public class JSheet extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    public static final int MENU_BAR_HEIGHT = 22;
    public static final Dimension FILE_CHOOSER_SIZE = new Dimension(500, 500);

    public static final int INCOMING = 1;
    public static final int OUTGOING = -1;
    // アニメーションする時間 msec
    public static final float ANIMATION_DURATION = 100;
    // 書き換えの周期 msec
    public static final int ANIMATION_SLEEP = 10;

    // Owner window
    private Window owner;

    // 表示する JOptionPane / JFileChooser を入れる JPanel
    private JComponent sourcePane;
    // JSheet の ContentPane
    private JPanel content;
    // 徐々に大きくなる JPanel
    private AnimatingSheet animatingSheet;

    private boolean animating;
    private int animationDirection;
    private Timer animationTimer;
    private long animationStart;

    private SheetListener sheetListener;

    public JSheet(Frame owner) {
        super(owner);
        init(owner);
    }

    public JSheet(Dialog owner) {
        super(owner);
        init(owner);
    }

    private void init(Window owner) {
        this.owner = owner;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setResizable(false);

        // modal にセット
        setModal(true);

        content = (JPanel) getContentPane();
        content.setLayout(new BorderLayout());
        animatingSheet = new AnimatingSheet();

        // リアルタイムの書き直しにならない.
        owner.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                locateSheet();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                locateSheet();
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    /**
     * JSheet の表示位置を設定する.
     */
    private void locateSheet() {
        Point loc = owner.getLocationOnScreen();
        Dimension ownerSize = owner.getSize();
        Dimension sourcePaneSize = sourcePane.getSize();

        // Sheet をセンタリング
        loc.x += (ownerSize.width - sourcePaneSize.width) / 2;
        loc.y += MENU_BAR_HEIGHT;

        // 右端，左端の処理
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (loc.x < 0) {
            loc.x = 0;
        }
        if (screenSize.width < loc.x + sourcePaneSize.width) {
            loc.x = screenSize.width - sourcePaneSize.width;
        }

        setBounds(loc.x, loc.y, sourcePaneSize.width, sourcePaneSize.height);
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
     * @param listener
     */
    public void addSheetListener(SheetListener listener) {
        sheetListener = listener;
    }

    /**
     * ソースとなる Dialog をセットしてリスナを付ける.
     *
     * @param dialog
     */
    public void setSourceDialog(JDialog dialog) {
        sourcePane = (JComponent) dialog.getContentPane();
        connectListeners(sourcePane.getComponent(0)); // JOptionPane or JFileChooser
        setTransparent(sourcePane);
    }

    /**
     * SourcePane の背景を透明にする.
     *
     * @param c
     */
    public void setTransparent(Component c) {
        if (c instanceof JComponent) {
            ((JComponent) c).setOpaque(false);
        }
        if (c instanceof Container) {
            for (Component comp : ((Container) c).getComponents()) {
                setTransparent(comp);
            }
        }
    }

    /**
     * リスナを SheetListener にブリッジする.
     *
     * @param c
     */
    private void connectListeners(Component c) {
        if (c instanceof JOptionPane) {
            c.addPropertyChangeListener(e -> {
                if (e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) {
                    // SheetEvent の設定
                    SheetEvent se = new SheetEvent(e.getSource());

                    // 戻り値
                    Object val = e.getNewValue();
                    Object[] options = ((JOptionPane) c).getOptions();

                    if (options == null) {
                        se.setOption((int) val);

                    } else {
                        // オプションがある場合は，何番目かを返す.
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

        } else if (c instanceof JFileChooser) {
            ((JFileChooser) c).addActionListener(e -> {
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
     * Sheet を表示／消去する. modal 動作のために synchronized(this) する.
     *
     * @param visible
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            SwingUtilities.invokeLater(() -> showSheet());
            // setModal(true) なのでここで止まるが，アニメーションは EDT で表示される.
            super.setVisible(true);

        } else {
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
     * JSheet を出し入れするアニメーションを開始する.
     */
    private void startAnimation() {
        content.removeAll();

        animatingSheet.setSource(sourcePane);
        content.add(animatingSheet, BorderLayout.CENTER);

        locateSheet();

        // start animation timer
        animationStart = System.currentTimeMillis();
        if (animationTimer == null) {
            animationTimer = new Timer(ANIMATION_SLEEP, this);
        }
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
     * Timer で呼ばれて animatingSheet を描画する.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (animating) {
            // calculate height to show
            float animationPercent
                    = (System.currentTimeMillis() - animationStart) / ANIMATION_DURATION;
            animationPercent = Math.min(1.0f, animationPercent);
            int animatingHeight = (animationDirection == INCOMING)
                    ? (int) (animationPercent * sourcePane.getHeight())
                    : (int) ((1.0f - animationPercent) * sourcePane.getHeight());
            // clip off that much from sheet and blit it into animatingSheet
            animatingSheet.setAnimatingHeight(animatingHeight);

            animatingSheet.repaint();

            // 終了処理
            if (animationPercent >= 1.0f) {
                stopAnimation();
                if (animationDirection == INCOMING) {
                    finishShowingSheet();
                } else {
                    content.removeAll();
                    super.setVisible(false);
                }
            }
        }
    }

    /**
     * 下から描いていく JPaenl.
     */
    private class AnimatingSheet extends JPanel {

        private static final long serialVersionUID = 1L;

        private final Dimension animatingSize = new Dimension(0, 1);
        private JComponent source;
        private BufferedImage offscreenImage;

        public AnimatingSheet() {
            super();
        }

        public void setSource(JComponent src) {
            source = src;
            animatingSize.width = src.getWidth();
            makeOffscreenImage(src);
        }

        public void setAnimatingHeight(int height) {
            animatingSize.height = height;
            setSize(animatingSize);
        }

        private void makeOffscreenImage(JComponent source) {
            GraphicsConfiguration gfxConfig
                    = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            offscreenImage = gfxConfig.createCompatibleImage(source.getWidth(), source.getHeight(), Transparency.TRANSLUCENT);
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
        public void paint(Graphics g) {
            // get the bottommost n pixels of source and
            // paint them into g, where n is height
            BufferedImage fragment
                    = offscreenImage.getSubimage(0, offscreenImage.getHeight() - animatingSize.height, source.getWidth(), animatingSize.height);
            g.drawImage(fragment, 0, 0, this);
        }
    }

    /**
     * JOptionPane を source とする JSheet を作成する.
     *
     * @param pane
     * @param parentComponent
     * @return
     */
    public static JSheet createDialog(final JOptionPane pane, Component parentComponent) {
        // create corresponding dialog
        pane.setBorder(new SheetBorder());
        JDialog dialog = pane.createDialog(null);
        dialog.pack();

        // create JSheet
        Window owner = JOptionPane.getFrameForComponent(parentComponent);
        JSheet js = (owner instanceof Frame) ? new JSheet((Frame) owner) : new JSheet((Dialog) owner);
        js.setSourceDialog(dialog);

        return js;
    }

    /**
     * JOptionPane を Sheet で表示する. listener に通知されるまでブロックされる.
     *
     * @param pane JOptionPane
     * @param parentComponent parent window or component
     * @param listener SheetListener
     */
    public static void showSheet(JOptionPane pane, Component parentComponent, SheetListener listener) {
        JSheet js = createDialog(pane, parentComponent);
        js.addSheetListener(se -> listener.optionSelected(se));
        js.setVisible(true);
    }

    /**
     * JFileChooser を Sheet で表示する.
     *
     * @param chooser
     * @param parentComponent
     * @param approveButtonText
     * @param listener
     */
    public static void showSheet(JFileChooser chooser, Component parentComponent, String approveButtonText, SheetListener listener) {
        // create corresponding dialog
        chooser.setApproveButtonText(approveButtonText);
        chooser.setPreferredSize(FILE_CHOOSER_SIZE);
        chooser.setMaximumSize(FILE_CHOOSER_SIZE);
        chooser.setMinimumSize(FILE_CHOOSER_SIZE);
        chooser.setBorder(new SheetBorder());

        JDialog dialog = new JDialog();
        dialog.add(chooser);
        dialog.pack();

        // create JSheet
        Window owner = JOptionPane.getFrameForComponent(parentComponent);
        JSheet js = (owner instanceof Frame) ? new JSheet((Frame) owner) : new JSheet((Dialog) owner);
        js.setSourceDialog(dialog);
        js.addSheetListener(se -> listener.optionSelected(se));
        js.setVisible(true);
    }

    /**
     * 保存 JFileChooser を表示する.
     *
     * @param chooser
     * @param parent
     * @param listener
     */
    public static void showSaveSheet(JFileChooser chooser, Component parent, SheetListener listener) {
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        showSheet(chooser, parent, "保存", listener);
    }

    /**
     * 開く JFileChooser を表示する.
     *
     * @param chooser
     * @param parent
     * @param listener
     */
    public static void showOpenSheet(JFileChooser chooser, Component parent, SheetListener listener) {
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        showSheet(chooser, parent, "開く", listener);
    }

    /**
     * JOptionPane.showConfirmDialog 互換.
     *
     * @param parentComponent
     * @param message
     * @param title
     * @param optionType
     * @param messageType
     * @return answer int
     */
    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType) {
        return showOptionDialog(parentComponent, message, title, optionType, messageType, null, null, null);
    }

    /**
     * JOptionPane.showMessageDialog 互換.
     *
     * @param parentComponent
     * @param message
     * @param title
     * @param messageType
     */
    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        showOptionDialog(parentComponent, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, null, null);
    }

    /**
     * JOptionPane.showOptionDialog 互換
     *
     * @param parentComponent
     * @param message
     * @param title
     * @param optionType
     * @param messageType
     * @param icon
     * @param options
     * @param initialValue
     * @return answer int
     */
    public static int showOptionDialog(Component parentComponent, Object message, String title,
            int optionType, int messageType, Icon icon, final Object[] options, Object initialValue) {

        JOptionPane pane = new JOptionPane(message, messageType, optionType, icon, options, initialValue);
        pane.setInitialValue(initialValue);
        pane.selectInitialValue();
        final int[] answer = new int[1];
        JSheet.showSheet(pane, parentComponent, se -> {
            answer[0] = se.getOption();
        });

        return answer[0];
    }

    /**
     * その component に既に JSheet が表示されているかどうか
     *
     * @param parentComponent
     * @return
     */
    public static boolean isAlreadyShown(Component parentComponent) {
        Window window = JOptionPane.getFrameForComponent(parentComponent);
        Window[] windowList = window.getOwnedWindows();
        for (Window w : windowList) {
            if (w instanceof MyJSheet && w.isVisible()) {
                // すでに JSheet が表示されている
                return true;
            }
        }
        return false;
    }

    public static void main(String[] arg) {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel(new ImageIcon(JSheet.class.getResource("/schemaeditor/usagi.jpg")));

        JButton b1 = new JButton("Create Dialog");
        b1.addActionListener(e -> {
            JOptionPane optionPane = new JOptionPane("JSheet.createDialog",
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_OPTION);

            JSheet sheet = JSheet.createDialog(optionPane, frame);
            sheet.addSheetListener(se -> {
                System.out.println("option = " + se.getOption());
            });
            sheet.setVisible(true);
            System.out.println("Create Dialog ended");
        });

        JButton b2 = new JButton("Show Sheet");
        b2.addActionListener(e -> {
            JOptionPane optionPane = new JOptionPane("JSheet.showSheet",
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_OPTION);

            JSheet.showSheet(optionPane, frame, se -> {
                System.out.println("option = " + se.getOption());
            });
            System.out.println("Show Sheet ended");
        });

        JButton b3 = new JButton("Show Save");
        b3.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            JSheet.showSaveSheet(chooser, frame, se -> {
                System.out.println("option = " + se.getOption());
            });
            System.out.println("Show Save ended");
        });

        JButton b4 = new JButton("Show Open");
        b4.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            JSheet.showOpenSheet(chooser, frame, se -> {
                System.out.println("option = " + se.getOption());
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

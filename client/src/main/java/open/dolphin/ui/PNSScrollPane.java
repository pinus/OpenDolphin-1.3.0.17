package open.dolphin.ui;

import open.dolphin.client.Dolphin;
import open.dolphin.project.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * ボーダーのない JScrollPane
 * Magic Mouse 用のスクロール速度調節もする
 * Lion スタイルのスクロールバーも表示する
 *
 * @author pns
 */
public class PNSScrollPane extends JScrollPane implements MouseListener, MouseMotionListener, MouseWheelListener {
        // マウスホイールを操作しているかどうかを判定するしきい値
    // laptime < THRESHOLD ならばスクロール操作中と考える
    private static final long THRESHOLD = 30;
    // スクロールバーの最低の長さ　短くなりすぎるとつかみにくくなるので
    private static final int MIN_SCROLLBAR_LENGTH = 50;
    private final static String VERTICAL_BAR_PANEL_NAME = "vbp";
    private final static String HORIZONTAL_BAR_PANEL_NAME = "hbp";
    // LionScrollBar 関連
    // 従来の ScrollBar にしたいときは true にする
    public boolean isClassicScrollBar;
    // ScrollBar を常に表示したい場合は true にする
    public boolean isPermanentScrollBar = false;
    // スクロールバー操作でのマウスキャプチャのためのダミーパネル.
    // JLayeredPane に透明な Panel を配置し，そこでスクロールバーに対するマウス動作をキャプチャする
    protected JLayeredPane parentLayer;
    // MouseWheelEvent 書き換え間隔の情報
    private long laptime = Integer.MAX_VALUE; // 時間間隔 msec
    private long prevTime = 0;
    // unitIncrement は 1,2,3 になるマシンと，1,2 にしかならないマシンがある
    private int maxUnitIncrement = 2;
    // ScrollBar を常に表示したときの fading につかうフラグ
    // false で paint に入ると，fading なしでスクロールバーを書く
    private boolean shouldFadeScrollBar = true;
    // スクロールバーの幅
    private final int scrollBarWidth = 12;
    // スクロールバーの大きさ
    private final Rectangle verticalBarRect = new Rectangle(0, 0, 0, 0);
    private final Rectangle horizontalBarRect = new Rectangle(0, 0, 0, 0);
    private final Rectangle verticalBarFrameRect = new Rectangle(0, 0, 0, 0);
    private final Rectangle horizontalBarFrameRect = new Rectangle(0, 0, 0, 0);
    // スクロールバーの枠表示の有無　マウスがスクロールバー領域に入ったら枠を表示する
    private boolean shouldShowScrollBarFrame = false;
    // スクロールバー表示のアルファ値
    private float alpha = 0.5f;
    // スクロールバーを消すアニメーションで使う変数
    private boolean isFadeScrollBarRequested = false;
    // スクロールバーを fade out させるアニメーションスレッド
    private Thread fader;
    private final FadeAnimator fadeAnimator = new FadeAnimator();
    // スクロールされたかどうかを検出するための変数
    private Rectangle prevViewRect = new Rectangle(0, 0, 0, 0);
    // マウスがドラッグ中かどうか
    private boolean mouseDragging = false;
    // drag する場合のマウスポイントと scrollBar value のオフセット
    private int mouseDragOffset = 0;
    private JPanel verticalBarPanel;
    private JPanel horizontalBarPanel;
    // overshoot animator 関連
    // overshoot animation を表示したくない場合は false にする
    private final boolean isOvershootAnimationEnabled = true;
    // overshoot アニメーション実行中かどうか
    private boolean isOvershootAnimating;
    private final OvershootAnimator overshootAnimator = new OvershootAnimator();
    private Timer overshootTimer;
    // overshoot アニメーション開始時間
    private long overshootStart;
    // overshoot の不応時間制御用フラグ
    private boolean overshootReady = true;
    private BufferedImage overshootImg;
    // overshoot アニメーション用の画像が用意できたかどうか
    private boolean isOvershootImgPrepared = false;
    // drop の時に，フィードバックを出すかどうか
    private boolean showDropFeedback = false;
    // MouseWheelEvent の不応期検出用
    private double prevPreciousWheelRotation;

    public PNSScrollPane() {
        this(null);
    }

    public PNSScrollPane(Component view) {
        this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public PNSScrollPane(Component view, int v, int h) {
        super(view, v, h);
        isClassicScrollBar = Dolphin.forWin;
        setBorder(BorderFactory.createEmptyBorder());
        addMouseWheelListener(this);
        // JScrollPane に MouseListener をつけても viewport の component に取られるので MouseEvent は拾えない
        // ↓ 無理矢理イベントを拾う方法も試したが，結局 component がイベントを受け取ってしまうのでダメだった
        // Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        // そこで，JLayeredPane にダミーパネルを用意してスクロールバーを覆うように配置，マウスイベントを横取りする方法にした
    }

    /*
     * Magic Mouse 用スクロール速度調節のために，デフォルトの ScrollBar をのっとる
     */
    @Override
    public JScrollBar createVerticalScrollBar() {
        return new MyScrollBar(JScrollBar.VERTICAL, this);
    }

    @Override
    public JScrollBar createHorizontalScrollBar() {
        return new MyScrollBar(JScrollBar.HORIZONTAL, this);
    }

    /**
     * paint を横取りして，Lion 風スクロールバーを表示する
     * paint は paintBorder→paintComponent→paintChildren を呼び出す
     * KarteScrollPane は paintChildren で描画するようにしたので
     * その後に，ここでスクロールバーを出す
     *
     * @param g Graphics
     */
    @Override
    public void paint(Graphics g) {
        //showFrameRate();
        super.paint(g);

        if (isOvershootImgPrepared) g.drawImage(overshootImg, 0, 0, null);

        if (!isClassicScrollBar) showScrollBar((Graphics2D) g);

        // Drop の際に，feedback を出す
        if (showDropFeedback) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            // cut and try
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            PNSBorder.drawSelectedBlueRoundRect(this, g2d, 0, 0, getWidth(), getHeight(), 6, 6);
            g2d.dispose();
        }
    }

    public void setShowDropFeedback(boolean b) {
        this.showDropFeedback = b;
        repaint();
    }

    /**
     * スクロールバーの大きさを決定して，スクロールバーを表示する
     * スクロールバーのつかむところの大きさ verticalBarRect，horizontalBarRect
     * スクロールバー表示領域全体の大きさ　verticalBarFrameRec，horizontalBarFrameRect
     *
     * @param g Graphics
     */
    protected void showScrollBar(Graphics2D g) {

        // viewport から見えている component の rectangle
        Rectangle viewRect = viewport.getViewRect();
        // viewport の component 全体の大きさ
        Dimension viewSize = viewport.getViewSize();

        // fade アニメーションすべきかどうか
        shouldFadeScrollBar = viewRect.x != prevViewRect.x || viewRect.y != prevViewRect.y
                || isFadeScrollBarRequested || isOvershootAnimating;

        // 縦スクロールバーを表示する必要がある条件
        if (viewSize.height > viewRect.height && (viewRect.y != prevViewRect.y || isFadeScrollBarRequested || isOvershootAnimating
                || (isPermanentScrollBar && verticalScrollBar.isVisible()))) {

            // scrollBarRect の決定
            int max = viewSize.height;
            int y = (viewRect.y <= 0) ? 0 : viewRect.y * viewRect.height / max; // スクロールバーの開始位置
            int len = viewRect.height * viewRect.height / max; // スクロールバーの長さ

            // scrollBar が短すぎないようにする
            if (len < MIN_SCROLLBAR_LENGTH) {
                int newLen = (viewRect.height / 2 < MIN_SCROLLBAR_LENGTH) ?
                        newLen = viewRect.height / 2 : MIN_SCROLLBAR_LENGTH;
                y = y * (viewRect.height - newLen) / (viewRect.height - len);
                len = newLen;
            }
            // 上下のマージン１ドットずつ入れる
            int margin = (columnHeader == null) ? 1 : columnHeader.getHeight() + 2; //header があるとき調節
            verticalBarRect.setBounds(viewRect.width - scrollBarWidth - 1, y + margin, scrollBarWidth, len - 2);
            verticalBarFrameRect.setBounds(verticalBarRect.x, margin, scrollBarWidth, viewRect.height - 2);

            // マウスキャプチャのためのダミーパネル挿入
            if (verticalBarPanel == null) {
                verticalBarPanel = createScrollBarPanel();
                verticalBarPanel.setName(VERTICAL_BAR_PANEL_NAME);
                //verticalBarPanel.setBorder(BorderFactory.createLineBorder(Color.red));
                getParentLayer().add(verticalBarPanel, JLayeredPane.DRAG_LAYER);
            }

        } else {
            verticalBarRect.setBounds(0, 0, 0, 0);
            verticalBarFrameRect.setBounds(0, 0, 0, 0);
        }

        // 横スクロールバーを表示する必要がある条件
        if (viewSize.width > viewRect.width && (viewRect.x != prevViewRect.x || isFadeScrollBarRequested || isOvershootAnimating
                || (isPermanentScrollBar && horizontalScrollBar.isVisible()))) {

            // scrollBarRect の決定
            int max = viewSize.width;
            int x = (viewRect.x <= 0) ? 0 : viewRect.x * viewRect.width / max; // スクロールバーの開始位置
            int margin = (rowHeader == null) ? 1 : rowHeader.getWidth() + 2;
            int len = viewRect.width * viewRect.width / max; // スクロールバーの長さ

            // scrollBar が短すぎないようにする
            if (len < MIN_SCROLLBAR_LENGTH) {
                int newLen = (viewRect.width / 2 < MIN_SCROLLBAR_LENGTH) ?
                        newLen = viewRect.width / 2 : MIN_SCROLLBAR_LENGTH;
                x = x * (viewRect.width - newLen) / (viewRect.width - len);
                len = newLen;
            }
            // 左右のマージン１ドットずつ入れる
            horizontalBarRect.setBounds(x + margin, viewRect.height - scrollBarWidth - 1, len - 2, scrollBarWidth);
            horizontalBarFrameRect.setBounds(margin, horizontalBarRect.y, viewRect.width - 2, scrollBarWidth);

            // マウスキャプチャのためのダミーパネル挿入
            if (horizontalBarPanel == null) {
                horizontalBarPanel = createScrollBarPanel();
                horizontalBarPanel.setName(HORIZONTAL_BAR_PANEL_NAME);
                //horizontalBarPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
                getParentLayer().add(horizontalBarPanel, JLayeredPane.DRAG_LAYER);
            }

        } else {
            horizontalBarRect.setBounds(0, 0, 0, 0);
            horizontalBarFrameRect.setBounds(0, 0, 0, 0);
        }

        // 縦横両方のスクロールバーが表示される場合は，交点の分だけ調節が必要
        if (verticalBarRect.height != 0 && horizontalBarRect.width != 0) {
            verticalBarRect.height -= scrollBarWidth;
            verticalBarFrameRect.height -= scrollBarWidth;
            horizontalBarRect.width -= scrollBarWidth;
            horizontalBarFrameRect.width -= scrollBarWidth;
        }

        // 描画メソッド呼び出し
        if (verticalBarRect.height != 0) {
            drawScrollBar(g, verticalBarRect, verticalBarFrameRect);
            // ダミーパネル表示
            Rectangle r = SwingUtilities.convertRectangle(this, verticalBarFrameRect, parentLayer);
            r.x -= 7;
            r.width += 10; //操作しやすいように少し広げる
            verticalBarPanel.setBounds(r);
            verticalBarPanel.setVisible(true);
        }
        if (horizontalBarRect.width != 0) {
            drawScrollBar(g, horizontalBarRect, horizontalBarFrameRect);
            // ダミーパネル表示
            Rectangle r = SwingUtilities.convertRectangle(this, horizontalBarFrameRect, parentLayer);
            r.y -= 7;
            r.height += 10; //操作しやすいように少し広げる
            horizontalBarPanel.setBounds(r);
            horizontalBarPanel.setVisible(true);
        }

        // スクロールしたかどうか判定するために今の位置を取っておく
        prevViewRect = viewRect;
    }
/*
    // paint のアニメーションが何コマ／秒になっているかを計測する
    private long prev;
    private int count;
    private long sum;
    private void showFrameRate() {
        long now = System.currentTimeMillis();
        long past = now - prev;
        prev = now;
        if (past < 200) {
            System.out.printf("%5d", past);
            sum += past;
            if (count++ % 10 == 0) {
                System.out.printf(" %5d/sec\n", 10000/sum);
                sum = 0;
            }
        }
    }
*/

    /**
     * 親フレームの JLayeredPane を返す
     *
     * @return JLayerdPane
     */
    public JLayeredPane getParentLayer() {
        if (parentLayer == null) {
            parentLayer = ((JFrame) SwingUtilities.getWindowAncestor(this)).getLayeredPane();
        }
        return parentLayer;
    }

    /**
     * スクロールバーを実際に描画する
     */
    private void drawScrollBar(Graphics2D g, Rectangle r, Rectangle fr) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isPermanentScrollBar && !shouldFadeScrollBar) {
            // ScrollBar を常に表示する場合，fade アニメーションが必要ない場合は単純にスクロールバーを描画する
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            g.setColor(Color.GRAY);
            g.fillRoundRect(fr.x, fr.y, fr.width, fr.height, scrollBarWidth, scrollBarWidth);
            g.setColor(Color.BLACK);
            g.fillRoundRect(r.x, r.y, r.width, r.height, scrollBarWidth, scrollBarWidth);

        } else {
            // fade アニメーションする場合こちらにくる
            // ScrollBar を常に表示する場合でも，スクロールの際は濃く表示して，fade アニメーションする
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            // 枠は，マウスがスクロールバー領域に乗ったときに表示. ScrollBar を常に表示する場合は常に表示する
            if (shouldShowScrollBarFrame || isPermanentScrollBar) {
                g.setColor(Color.GRAY);
                g.fillRoundRect(fr.x, fr.y, fr.width, fr.height, scrollBarWidth, scrollBarWidth);
            }
            g.setColor(Color.BLACK);
            g.fillRoundRect(r.x, r.y, r.width, r.height, scrollBarWidth, scrollBarWidth);

            fadeScrollBar();
        }
    }

    /**
     * LionScrollBar を fade させるアニメーションスレッドをスタートする
     */
    private void fadeScrollBar() {
        if (fader == null || !fader.isAlive()) {
            fader = new Thread(fadeAnimator);
            fader.start();
            isFadeScrollBarRequested = true;
        }
    }

    /**
     * overshoot のアニメーションをスタート
     *
     * @param direction direction
     * @param amplitude amplitude
     */
    private void showOvershoot(Overshoot direction, int amplitude) {
        if (overshootTimer == null) {
            overshootTimer = new Timer(10, overshootAnimator);
        }
        if (!isOvershootAnimating) {
            isOvershootAnimating = true;
            overshootStart = System.currentTimeMillis();
            overshootAnimator.setDirection(direction);
            overshootAnimator.setAmplitude(amplitude);
            overshootTimer.start();
        }
    }

    /**
     * スクロールバー操作のマウスイベントを取得するためのダミーパネルを作成する
     *
     * @return Dummy Panel
     */
    private JPanel createScrollBarPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.addMouseListener(this);
        p.addMouseMotionListener(this);
        p.addMouseWheelListener(this);
        return p;
    }

    // MouseListener の処理：スクロールバーダミーパネルから呼ばれる
    // クリックした場合はその位置までジャンプ
    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("mouse clicked");
        if (VERTICAL_BAR_PANEL_NAME.equals(getSourcePanelName(e))) {
            if (verticalBarFrameRect.height != 0) {
                verticalScrollBar.setValue(e.getY() * (viewport.getView().getHeight() / verticalBarFrameRect.height));
            }
        } else {
            if (horizontalBarFrameRect.width != 0) {
                horizontalScrollBar.setValue(e.getX() * (viewport.getView().getWidth() / horizontalBarFrameRect.width));
            }
        }
        prevTime = System.currentTimeMillis();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println("mouse pressed");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //System.out.println("mouse released");
        if (mouseDragging) {
            mouseDragging = false;
            shouldShowScrollBarFrame = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //System.out.println("mouse exited");
        if (!mouseDragging) shouldShowScrollBarFrame = false;
    }

    // ドラッグした場合はマウスの動きに応じてスクロールする
    @Override
    public void mouseDragged(MouseEvent e) {
        String panelName = getSourcePanelName(e);
        boolean isVertical = VERTICAL_BAR_PANEL_NAME.equals(panelName);
        Point p = e.getPoint();

        if (!mouseDragging) {
            if (isVertical) mouseDragOffset = p.y - verticalBarRect.y;
            else mouseDragOffset = p.x - horizontalBarRect.x;
        }

        if (isVertical) {
            mouseDragging = true;
            int max = verticalBarFrameRect.height - verticalBarRect.height;
            int value = p.y - mouseDragOffset;
            if (value < 0) {
                value = 0;
                mouseDragOffset = p.y;

            } else if (value > max) {
                value = max;
                mouseDragOffset = p.y - max;
            }
            if (max != 0) {
                verticalScrollBar.setValue(value * (viewport.getView().getHeight() - verticalBarFrameRect.height) / max);
            }

        } else {
            mouseDragging = true;
            int max = horizontalBarFrameRect.width - horizontalBarRect.width;
            int value = p.x - mouseDragOffset;
            if (value < 0) {
                value = 0;
                mouseDragOffset = p.x;

            } else if (value > max) {
                value = max;
                mouseDragOffset = p.x - max;
            }
            if (max != 0) {
                horizontalScrollBar.setValue(value * (viewport.getView().getWidth() - horizontalBarFrameRect.width) / max);
            }
        }

        // mouseWheel と drug を同時に操作はしないとの仮定
        prevTime = System.currentTimeMillis();
    }

    // スクロールバー領域でマウスを動かしたらフレームを表示する
    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.printf("mouse moved (%d,%d)" , e.getX(), e.getY());
        setShouldShowScrollBarFrame();
    }

    /**
     * この listener は PNSScrollPane とスクロールダミーパネルの両方からイベントを受け取る
     * MyJScrollBar では，WheelEvent の頻度を計測
     * スクロールダミーパネルでは，スクロールフレームの表示をしてから MyJScroppPane にイベントを投げる
     *
     * @param e MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        // mouseWheelEvent の頻度を計測. MyScrollBar で使う.
        long now = System.currentTimeMillis();
        laptime = now - prevTime;
        prevTime = now;

        // ダミーパネルからの mouse event かどうかを判定，PNSScrollPane に伝達し，フレーム表示をセット
        String panelName = getSourcePanelName(e);
        if (VERTICAL_BAR_PANEL_NAME.equals(panelName) || HORIZONTAL_BAR_PANEL_NAME.equals(panelName)) {
            setShouldShowScrollBarFrame();
            dispatchEvent(SwingUtilities.convertMouseEvent((JPanel) e.getSource(), e, this));
        }

        // 慣性移動の判断
        double preciseWheelRotation = e.getPreciseWheelRotation();

        // overshoot animation
        if (isOvershootAnimationEnabled) {
            // スクロール方向の検出
            int orientation = (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == 0 ? ScrollBar.VERTICAL : ScrollBar.HORIZONTAL;

            // overshoot 検出
            Rectangle r = viewport.getViewRect();
            Point p = viewport.getViewPosition();

            Dimension compSize = viewport.getView().getSize();
            int horizontalLimit = compSize.width - r.width;
            int verticalLimit = compSize.height - r.height;
            int unitIncrement = verticalScrollBar.getUnitIncrement(orientation);

            // overshoot 不応期の解除. Rotation差が少ない場合は慣性移動と判断.
            if (!overshootReady && Math.abs(preciseWheelRotation - prevPreciousWheelRotation) > 5) {
                overshootReady = true;
                maxUnitIncrement = 10;
            }

            p.x += e.getUnitsToScroll() * unitIncrement;
            p.y += e.getUnitsToScroll() * unitIncrement;

            int amplitude = maxUnitIncrement;

            if (orientation == ScrollBar.VERTICAL && viewport.getView().getHeight() > viewport.getExtentSize().getHeight()) {
                if (p.y < 0 && overshootReady) {
                    showOvershoot(Overshoot.TOP, amplitude);
                    overshootReady = false;
                    //System.out.println("overshoot on top");
                } else if (p.y > verticalLimit && overshootReady) {
                    showOvershoot(Overshoot.BOTTOM, amplitude);
                    overshootReady = false;
                    //System.out.println("overshoot on bottom");
                }

            } else if (orientation == ScrollBar.HORIZONTAL && viewport.getView().getWidth() > viewport.getExtentSize().getWidth()) {
                if (p.x < 0 && overshootReady) {
                    showOvershoot(Overshoot.LEFT, amplitude);
                    overshootReady = false;
                    //System.out.println("overshoot to left");
                } else if (p.x > horizontalLimit && overshootReady) {
                    showOvershoot(Overshoot.RIGHT, amplitude);
                    overshootReady = false;
                    //System.out.println("overshoot to right");
                }
            }
        }
        prevPreciousWheelRotation = e.getPreciseWheelRotation();
    }

    /**
     * スクロールバーのフレームを書く
     */
    private void setShouldShowScrollBarFrame() {
        if (!shouldShowScrollBarFrame) {
            shouldShowScrollBarFrame = true;
            repaint();
        }
    }

    /**
     * MouseEvent がスクロールダミーパネルのどちらから来たかを判定
     *
     * @param e MouseEvent
     * @return Source panel name
     */
    private String getSourcePanelName(MouseEvent e) {
        Object src = e.getSource();
        if (src instanceof JPanel) return ((JPanel) src).getName();
        else return null;
    }

    private enum Overshoot {TOP, BOTTOM, LEFT, RIGHT}

    /**
     * fade アニメーション実行スレッド
     */
    private class FadeAnimator implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    long now = System.currentTimeMillis();
                    //System.out.print(".");
                    // delay msec 経過後に fading animation 実行
                    // prevTime には最後のマウスホイール操作，最後のドラッグ操作，最後のクリック操作が記録される
                    // スクロール操作中は常に prevTime が最新時間に更新されるので fade アニメーションは開始待ちになる
                    int delay = 1000;
                    // ScrollBar フレームを表示している間（マウスがスクロールバー領域に乗っているとき）は fade しない
                    // スクロールバーをマウスでつかんで操作している間は fade しない
                    if (!shouldShowScrollBarFrame && !mouseDragging && now - prevTime > delay) {
                        //System.out.println("fading animation thread starts");
                        for (int i = 9; i >= 0; i--) {
                            if (isPermanentScrollBar) alpha = (float) i * 0.25f / 9f + 0.2f;
                            else alpha = (float) i * 0.05f;
                            repaint();
                            Thread.sleep(30); // 30コマ／秒のアニメーション
                        }
                        prevTime = now;
                        isFadeScrollBarRequested = false;
                        alpha = 0.5f;

                        // ダミーパネルを消去
                        if (verticalBarPanel != null) {
                            verticalBarPanel.setVisible(false);
                            verticalBarPanel.repaint();
                        }
                        if (horizontalBarPanel != null) {
                            horizontalBarPanel.setVisible(false);
                            horizontalBarPanel.repaint();
                        }

                        break;

                    } else {
                        // 少し待ってから，もう１回トライする
                        Thread.sleep(250);
                    }
                }
                //System.out.println("fading animation thread ends");
            } catch (InterruptedException ex) {
                System.out.println("PNSScrollPane: " + ex);
            }
        }
    }

    /**
     * overshoot アニメーション実行スレッド
     */
    private class OvershootAnimator implements ActionListener {
        private Overshoot direction;
        private int amplitude;
        private final float duration = 200f;

        public void setDirection(Overshoot direction) {
            this.direction = direction;
        }

        public void setAmplitude(int amplitude) {
            this.amplitude = amplitude;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            double percent = Math.min((System.currentTimeMillis() - overshootStart) / duration, 1.0f);
            double theta = Math.PI * percent;
            double d = amplitude * Math.sin(theta);

            Dimension viewSize = viewport.getExtentSize();
            int marginX = (rowHeader == null) ? 0 : rowHeader.getWidth();
            int marginY = (columnHeader == null) ? 0 : columnHeader.getHeight(); //header があるとき調節
            viewSize.width += marginX;
            viewSize.height += marginY;

            overshootImg = new BufferedImage(viewSize.width, viewSize.height, BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage tmpImg = new BufferedImage(viewSize.width, viewSize.height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) overshootImg.getGraphics();
            paintChildren(tmpImg.getGraphics());

            switch (direction) {
                case TOP -> {
                    g.drawImage(tmpImg.getSubimage(0, 0, tmpImg.getWidth(), tmpImg.getHeight() - (int) d),
                        0, (int) d, null);

                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, marginY, tmpImg.getWidth(), (int) d);

                    if (columnHeader != null) {
                        g.drawImage(tmpImg.getSubimage(0, 0, tmpImg.getWidth(), marginY),
                            0, 0, null);
                    }
                }

                case BOTTOM -> {
                    g.drawImage(tmpImg.getSubimage(0, (int) d, tmpImg.getWidth(), tmpImg.getHeight() - (int) d),
                        0, 0, null);

                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, viewSize.height - (int) d, tmpImg.getWidth(), (int) d);

                    if (columnHeader != null) {
                        g.drawImage(tmpImg.getSubimage(0, 0, tmpImg.getWidth(), marginY),
                            0, 0, null);
                    }
                }

                case LEFT -> {
                    g.drawImage(tmpImg.getSubimage(0, 0, tmpImg.getWidth() - (int) d, tmpImg.getHeight()),
                        (int) d, 0, null);

                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(marginX, 0, (int) d, tmpImg.getHeight());

                    if (rowHeader != null) {
                        g.drawImage(tmpImg.getSubimage(0, 0, marginX, tmpImg.getHeight()),
                            0, 0, null);
                    }
                }

                case RIGHT -> {
                    g.drawImage(tmpImg.getSubimage((int) d, 0, tmpImg.getWidth() - (int) d, tmpImg.getHeight()),
                        0, 0, null);

                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(viewSize.width - (int) d, 0, (int) d, tmpImg.getHeight());

                    if (rowHeader != null) {
                        g.drawImage(tmpImg.getSubimage(0, 0, marginX, tmpImg.getHeight()),
                            0, 0, null);
                    }
                }
            }

            isOvershootImgPrepared = true;
            repaint();

            if (percent >= 1.0f) {
                overshootTimer.stop();
                isOvershootImgPrepared = false;
                isOvershootAnimating = false;
                overshootImg.flush();
                tmpImg.flush();
                g.dispose();
            }
        }
    }

    /**
     * 慣性スクロールの止まり際に，スピードを落として滑らかに見せるスクロールバー
     * isClassicScrollBar = true  : 従来のスクロールバー表示
     * isClassicScrollBar = false : スクロールバー表示しない
     */
    private class MyScrollBar extends ScrollBar {

        PNSScrollPane context;

        private int scrollUnit;
        private int defaultScrollUnit;
        private final Dimension preferredSize = new Dimension(0, 0);

        // この scroll pane を使っている component
        private Component child = null;

        public MyScrollBar(int direction, PNSScrollPane context) {
            super(direction);
            this.context = context;
            this.putClientProperty("JScrollBar.fastWheelScrolling", null);
        }

        // Dimension(0,0) を返すと，isVisible=true のまま見えなくなる
        @Override
        public Dimension getPreferredSize() {
            if (isClassicScrollBar) {
                Object sizeVariant = ((JComponent) getParent()).getClientProperty("JComponent.sizeVariant");
                if (sizeVariant != null && sizeVariant.equals("small")) preferredSize.setSize(7, 7);
                else preferredSize.setSize(10, 10);
            } else {
                setSize(0, 0);
            }
            return preferredSize;
        }

        @Override
        public int getUnitIncrement(int direction) {

            if (child == null) {
                child = viewport.getView();
                // カルテ
                if (child instanceof JPanel || child instanceof JTextPane) {
                    defaultScrollUnit = Project.getPreferences().getInt(Project.SCROLL_UNIT_KARTE, 15);

                    // スタンプ
                } else if (child instanceof JTree) {
                    defaultScrollUnit = Project.getPreferences().getInt(Project.SCROLL_UNIT_STAMP, 15);

                    // テーブル
                } else if (child instanceof JTable || child instanceof JList) {
                    defaultScrollUnit = Project.getPreferences().getInt(Project.SCROLL_UNIT_TABLE, 15);

                    // unknown source
                } else {
                    defaultScrollUnit = super.getUnitIncrement(direction);
                }
            }

            // 慣性スクロールが止まるときの動きを滑らかにするため scrollUnit を動的に調整する
            // laptime が THRESHOLD 以上かかっていたら（＝スクロールが遅くなっていたら）ブレーキをかける
            scrollUnit = laptime > THRESHOLD ? 1 : defaultScrollUnit;

            return scrollUnit;
        }

        @Override
        public void paint(Graphics g) {
            if (isClassicScrollBar) super.paint(g);
        }

        /**
         * scrollable なものは，ここに value をセットするだけで viewport が書き換わるので，JScrollPane の paint は呼ばれない.
         * それだと LionScrollBar が書けないので，無理矢理 repaint して書かせる
         *
         * @param value value
         */
        @Override
        public void setValue(int value) {
            super.setValue(value);
            //if (!isClassicScrollBar && context.viewport.getView() instanceof Scrollable) context.repaint();
            if (!isClassicScrollBar) context.repaint();
        }
    }
}

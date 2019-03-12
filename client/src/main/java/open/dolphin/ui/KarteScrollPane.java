package open.dolphin.ui;

import open.dolphin.client.KartePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * カルテをスワイプスクロール
 * <p>
 * viewPos
 * +---------------+
 * (i-1)|       (i)     |  (i+1)
 * +----+--------+----------+-----
 * |    |<-off   |      |   | KartePanel
 * |    |   set->|      |   |
 * +----+--------+----------+-----
 * |               |
 * +---------------+ ViewPort
 * i=karePageNumber
 *
 * @author pns
 */
public class KarteScrollPane extends MyJScrollPane {
    private static final long serialVersionUID = 1L;

    // 最初は viewport.getView() を一気に BufferedImage にしようとしたが，カルテが多くなると OutOfMemorryError になってしまった
    // モニタの広さだけのバッファを１つ用意して，それを使い回すようにした. それでも，heap は -Xmx512m 以上にしないときつい
    // ここのメモリ使い方はもう少し工夫の余地がありそう
    // private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    // その後，メモリ節約バージョン作成：カルテサイズ変更しなければ大幅メモリ節約だがサイズ変更すると結構大変バージョン
    private BufferedImage snapImg = null;
    // カルテページの先頭の画像を入れるバッファ
    private BufferedImage headImg = null;
    // snapImg と headImg のオフセット
    private Dimension offset = new Dimension(0, 0);
    // カルテの各ページの先頭位置を保持する
    private List<Point> positionList = null;
    // 表示中のカルテページ番号
    private int kartePageNumber = 0;
    // スクロールの方向
    private int direction;
    // 現在の表示位置
    private Point viewPos = new Point(-1, -1);
    // 表示位置の変化を検出する
    private Point prevViewPos;
    // 表示している区画
    private Rectangle viewRect;
    // viewRect の変化を検出するための変数
    private Rectangle prevViewRect = new Rectangle(0, 0, 0, 0);
    // 不透明で塗りつぶすための alpha 値
    private static final AlphaComposite opaque = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
    // 影をつけるための半透明の alpha 値
    private static final AlphaComposite[] translucent = {
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f),
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f),
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.10f),
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.06f),
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.04f),
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.02f),
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.01f),
    };
    // mouse caputure するパネル
    private JPanel mouseCapturePanel = null;
    // 親フレームの Container
    // mouseCapturePanel のイベントを contentPane に伝えると，イベントを pass through できる
    private Container contentPane = null;
    // view component に変化があったかどうか  KarteDocumentViewer からセットされる
    private boolean viewComponentChanged = false;

    public KarteScrollPane() {
        super();
        // isClassicScrollBar = false;
    }

    // getPositionList で使う comparator
    private Comparator<Point> xComparator = new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            return o1.x - o2.x;
        }
    };
    private Comparator<Point> yComparator = new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            return o1.y - o2.y;
        }
    };

    /**
     * paintChildern で描画しておく
     * 後で，MyJScrollPane の paint で，スクロールバーを書いてもらう
     *
     * @param graphics Graphics
     */
    @Override
    protected void paintChildren(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        super.paintChildren(g);

        // null check
        if (viewport.getView() == null) {
            return;
        }

        // offset = viewRect の頭から次ページカルテの頭までの距離
        offset.width = 0;
        offset.height = 0;

        block:
        {
            // カルテの各ページの viewPosition を調べる
            // 表示位置によって動的に変わるので毎回新たに調べなくてはならない
            positionList = getPositionList();

            // 横スクロール
            if (direction == ScrollBar.HORIZONTAL) {
                // 現在の viewport のサイズ
                viewRect = viewport.getVisibleRect();
                // スクロールの必要がない場合は帰る
                if (viewRect.width >= viewport.getView().getWidth()) {
                    break block;
                }

                // 現在の表示位置
                prevViewPos = viewPos;
                viewPos = viewport.getViewPosition();

                // スクロールに応じて，必要なスナップショットを取る
                // ページ境界検出
                int currentPage = 0;
                for (int i = 0; i < positionList.size(); i++) {
                    int x = positionList.get(i).x;
                    if (x > viewPos.x) {
                        if (x < viewPos.x + viewRect.width) {
                            // viewport にページ境界がある場合
                            currentPage = i;
                        }
                        break;
                    }
                }
                // ページ境界がなければ，そのまま帰る
                if (currentPage == 0) {
                    break block;
                }

                offset.width = positionList.get(currentPage).x - viewPos.x;

                if (positionList.size() > 1
                        && (kartePageNumber != currentPage || viewRectChanged() || viewComponentChanged())) {

                    kartePageNumber = currentPage;
                    createBufferedImage();
                    snap();
                }

                // 現在の viewRect を保存
                prevViewRect = viewRect;

                // viewport の頭をスナップショットの頭で上書きしちゃう
                if (viewRect.height < snapImg.getHeight()) {
                    headImg = snapImg.getSubimage(0, 0, offset.width, viewRect.height);
                } else {
                    headImg = snapImg.getSubimage(0, 0, offset.width, snapImg.getHeight());
                }

                // 不透明で塗りつぶしてから
                //g.setComposite(opaque);
                //g.setColor(this.getBackground());
                //g.fillRect(0, 0, offset.width, viewRect.height);
                // スナップショットを上書き
                g.drawImage(headImg, 0, 0, null);
                // 素敵な shadow をつける
                g.setColor(Color.BLACK);
                for (int i = 0; i < translucent.length; i++) {
                    g.setComposite(translucent[i]);
                    g.drawLine(offset.width - i, 0, offset.width - i, viewRect.height);
                }

                // 縦スクロール
                // 同じコードの繰り返しでかっこわるいけどスピード優先
            } else if (direction == ScrollBar.VERTICAL) {
                // 現在の viewport のサイズ
                viewRect = viewport.getVisibleRect();
                // スクロールの必要がない場合は帰る
                if (viewRect.height >= viewport.getView().getHeight()) {
                    break block;
                }

                // 現在の表示位置
                prevViewPos = viewPos;
                viewPos = viewport.getViewPosition();

                // スクロールに応じて，必要なスナップショットを取る
                // ページ境界検出
                int currentPage = 0;
                for (int i = 0; i < positionList.size(); i++) {
                    int y = positionList.get(i).y;
                    // y が viewPos.y を越えたら，ページ境界が入っている可能性あり
                    if (y > viewPos.y) {
                        // viewRect 内に入っていれば，ページ境界あり
                        // ただし，前のページの大きさが viewRect.y より大きかったらスナップ撮らないことにする
                        if (y < viewPos.y + viewRect.height
                                && (i != 0 && (y - positionList.get(i - 1).y) < viewRect.height)) {
                            currentPage = i;
                        }
                        break;
                    }
                }

                // ページ境界がなければ，そのまま帰る
                if (currentPage == 0) {
                    break block;
                }
                // ページ境界と viewRect の頭との offset
                offset.height = positionList.get(currentPage).y - viewPos.y;
                if (positionList.size() > 1
                        && (kartePageNumber != currentPage || viewRectChanged() || viewComponentChanged())) {

                    kartePageNumber = currentPage;
                    createBufferedImage();
                    snap();
                }

                // 現在の viewRect を保存
                prevViewRect = viewRect;

                // カルテの頭をスナップショットで上書き
                if (viewRect.width < snapImg.getWidth()) {
                    headImg = snapImg.getSubimage(0, 0, viewRect.width, offset.height);
                } else {
                    headImg = snapImg.getSubimage(0, 0, snapImg.getWidth(), offset.height);
                }

                // 塗りつぶしてから
                //g.setComposite(opaque);
                //g.setColor(this.getBackground());
                //g.fillRect(0, 0, viewRect.width, offset.height);
                // 上書き
                g.drawImage(headImg, 0, 0, null);
                // shadow をつける
                g.setColor(Color.BLACK);
                for (int i = 0; i < translucent.length; i++) {
                    g.setComposite(translucent[i]);
                    g.drawLine(0, offset.height - i - 1, viewRect.width, offset.height - i - 1);
                }
            }
        }
        // break block でここまでくる
    }

    /**
     * カルテのサイズ変更があったかどうか
     *
     * @return
     */
    private boolean viewRectChanged() {
        return viewRect.height != prevViewRect.height || viewRect.width != prevViewRect.width;
    }

    /**
     * view component に変化があったかどうか
     *
     * @return
     */
    private boolean viewComponentChanged() {
        if (viewComponentChanged) {
            viewComponentChanged = false;
            return true;
        }
        return false;
    }

    /**
     * view component に変更があった場合に呼ぶ
     * KarteDocumentViewer からセットする
     */
    public void setViewComponentChanged() {
        viewComponentChanged = true;
    }

    /**
     * Viewport 変更に応じて，BufferedImage を作り直す
     */
    private void createBufferedImage() {
        // snapImage バッファを作る
        if (snapImg == null) {
            snapImg = new BufferedImage(viewRect.width, viewRect.height, BufferedImage.TYPE_3BYTE_BGR);

        } else if (viewRectChanged()) {
            snapImg.flush();
            snapImg = new BufferedImage(viewRect.width, viewRect.height, BufferedImage.TYPE_3BYTE_BGR);
        }
        // mouse capture panel を作る
        if (mouseCapturePanel == null) {
            createMouseCapturePanel();
        } else if (viewRectChanged()) {
            mouseCapturePanel.setBounds(SwingUtilities.convertRectangle(viewport, viewRect, parentLayer));
        }
    }

    /**
     * viewport.serViewPosition で使う座標をリストアップする
     *
     * @return
     */
    private List<Point> getPositionList() {
        List<Point> index = new ArrayList<Point>();
        JPanel child = (JPanel) viewport.getView();

        int checksumX = 0, checksumY = 0;

        for (Component c : child.getComponents()) {
            if (c instanceof KartePanel) {
                Point p = SwingUtilities.convertPoint(c, 0, 0, child);
                index.add(p);
                checksumX += p.x;
                checksumY += p.y;
            }
        }
        if (checksumX != 0) {
            // これは横スクロール
            direction = ScrollBar.HORIZONTAL;
            // 念のためソートしておく
            index.sort(xComparator);

        } else if (checksumY != 0) {
            // これは縦スクロール
            direction = ScrollBar.VERTICAL;
            // 念のためソートしておく
            index.sort(yComparator);

        } else {
            direction = ScrollBar.NO_ORIENTATION;
            index = null;
        }

        return index;
    }

    /**
     * マウスイベント捕獲のための JPanel を作る
     *
     * @return
     */
    private JPanel createMouseCapturePanel() {
        //
        if (parentLayer == null) {
            getParentLayer();
        }
        if (contentPane == null) {
            contentPane = ((JFrame) SwingUtilities.getWindowAncestor(this)).getContentPane();
        }

        mouseCapturePanel = new JPanel();
        parentLayer.add(mouseCapturePanel, JLayeredPane.DRAG_LAYER - 1); // MyJScrollPane より一歩下がる

        mouseCapturePanel.setBounds(SwingUtilities.convertRectangle(viewport, viewRect, parentLayer));
        mouseCapturePanel.setOpaque(false);
        //mouseCapturePanel.setBorder(BorderFactory.createLineBorder(Color.red));
        mouseCapturePanel.addMouseListener(new PassThroughMouseListener());
        mouseCapturePanel.addMouseMotionListener(new PassThroughMouseListener());
        mouseCapturePanel.addMouseWheelListener(new PassThroughMouseListener());

        return mouseCapturePanel;
    }

    /**
     * マウスイベント捕獲のための JPanel を破棄する
     */
    private void disposeMouseCapturePanel() {
        if (mouseCapturePanel != null) {
            parentLayer.remove(mouseCapturePanel);
            mouseCapturePanel = null;
        }
    }

    /**
     * 捕獲パネルのマウスイベントを，単純に contentPane に pass through する
     *
     * @param e
     */
    private void passThroughMouseEvent(MouseEvent e) {
        // 捕獲パネルでのマウス座標を，contentPane のマウス座標に変換
        Point p = SwingUtilities.convertPoint((JPanel) e.getSource(), e.getPoint(), contentPane);
        // contentPane のマウス場所のコンポネントを得る
        Component target = contentPane.findComponentAt(p);
        // そのコンポネントにイベントを送る
        if (target != null) {
            target.dispatchEvent(SwingUtilities.convertMouseEvent((JPanel) e.getSource(), e, target));
            //if (e.getID() != MouseEvent.MOUSE_MOVED) System.out.println("target=" + target.getClass());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    repaint();
                }
            });
        }
    }

    /**
     * 見せかけの部分を実体に変換して event dispatch する
     *
     * @param e
     */
    private void translateMouseEvent(MouseEvent e) {
        JComponent viewCompo = (JComponent) viewport.getView();
        // 表示されている部分
        // Point head = positionList.get(kartePageNumber);
        Point head = getHeadPoint();
        // viewCompo 上での実際のマウス位置
        Point p = new Point(head.x + e.getX(), head.y + e.getY());
        // viewCompo 上で，その位置の component (StampHolder) を取る
        Component target = viewCompo.findComponentAt(p);

        if (target != null) {
            // target 上でのマウスイベントに変換
            MouseEvent event = SwingUtilities.convertMouseEvent((JPanel) e.getSource(), e, target);
            // 実際の位置に変換
            event.translatePoint(head.x - viewPos.x, head.y - viewPos.y);
            // target に対して event 発行
            target.dispatchEvent(event);
            // viewPosition がずれることがあるので補正
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //viewport.setViewPosition(viewPos);
                    repaint();
                }
            });
        }
    }

    /**
     * 見せかけとして表示されている部分の先頭座標
     * kartePagenNumber は見せかけの部分の次のページを指している
     * viewRect より小さければ前のページの頭 positionsList.get(kartePageNumber-1) になるが，
     * viewRect より大きいと，はみ出した分を補正しなくてはならない
     * residue = はみ出した分 = 見せかけ部分のページの長さ - viewRect
     *
     * @return
     */
    private Point getHeadPoint() {
        if (positionList == null) {
            System.out.println("KarteScrollPane: positionList is null at getHeadPoint");
            return new Point(0, 0);
        }

        Point head = new Point(positionList.get(kartePageNumber - 1));
        Point current = new Point(positionList.get(kartePageNumber));
        Point residue = new Point(current.x - head.x - viewRect.width, current.y - head.y - viewRect.height);
        if (residue.x > 0) {
            head.x += residue.x;
        }
        if (residue.y > 0) {
            head.y += residue.y;
        }
        return head;
    }
    //long l;

    /**
     * スナップを撮る
     */
    private void snap() {
        //l = System.currentTimeMillis();

        int compCount = ((JComponent) viewport.getView()).getComponentCount();
        if (compCount <= kartePageNumber) {
            System.out.println("---- something is wrong");
            System.out.println("---- kartePageNumber = " + kartePageNumber);
            System.out.println("---- compCount = " + compCount);
            return;
        }

        final JPanel p = (JPanel) ((JComponent) viewport.getView()).getComponent(kartePageNumber - 1);
        if (!p.isValid()) {
            System.out.println("invalid component = " + (kartePageNumber - 1));
//            viewport.setViewPosition(getHeadPoint());
//            viewport.paint(snapImg.getGraphics());
//            viewport.setViewPosition(viewPos);
//        } else {
//            p.paint(snapImg.getGraphics());
        }
        p.paint(snapImg.getGraphics());
        //((JComponent)viewport.getView()).getComponent(kartePageNumber-1).paint(snapImg.getGraphics());
        //System.out.println("viewport.paint lap = " + (System.currentTimeMillis() -l));
    }

    /**
     * 見せかけの部分にいるかどうか
     *
     * @param e
     * @return
     */
    private boolean shouldTranslate(MouseEvent e) {
        // 縦スクロールなら offset.width は 0 になってる
        return (e.getX() < offset.width || e.getY() < offset.height)
                && (offset.width < viewRect.width && offset.height < viewRect.height);
    }

    /**
     * 見せかけの部分（headImg部分）で発生した event を，実体の event に変換して伝えるリスナ
     */
    private class PassThroughMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {

        /**
         * 捕獲したマウスイベントを処理する
         *
         * @param e
         */
        private void processMouseEvent(MouseEvent e) {
            // タブが切り替わって KarteScrollPane が見えなくなると isShowing == false となる
            // そしたら，今のイベントを pass through してから，マウス捕獲 JPanel を消去する
            if (isShowing()) {
                if (shouldTranslate(e) && !e.isPopupTrigger()) {
                    translateMouseEvent(e);
                    //System.out.println("translate: " + e.getID());
                } else {
                    passThroughMouseEvent(e);
                    //System.out.println("pass through: " + e.getID());
                }
                // マウスでクリックしたときスタンプの選択枠を再描画するに snap 必要
                if (positionList != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            snap();
                        }
                    });
                }

            } else {
                passThroughMouseEvent(e);
                disposeMouseCapturePanel();
                //System.out.println("dispose: " + e.getID());
            }
        }

        // process 必要なイベント
        @Override
        public void mouseClicked(MouseEvent e) {
            //System.out.println("clicked");
            processMouseEvent(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //System.out.println("pressed");
            processMouseEvent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            processMouseEvent(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            processMouseEvent(e);
        }

        // pass through でよいイベント
        @Override
        public void mouseEntered(MouseEvent e) {
            passThroughMouseEvent(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            passThroughMouseEvent(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            passThroughMouseEvent(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            passThroughMouseEvent(e);
        }
    }
}

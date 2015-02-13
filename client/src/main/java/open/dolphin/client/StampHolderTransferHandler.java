package open.dolphin.client;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import open.dolphin.delegater.StampDelegater;
import open.dolphin.infomodel.*;
import open.dolphin.order.ClaimConst;
import open.dolphin.project.Project;
import open.dolphin.ui.PatchedTransferHandler;

/**
 * StampHolderTransferHandler
 *
 * @author Kazushi Minagawa
 *
 */
public class StampHolderTransferHandler extends PatchedTransferHandler {
    private static final long serialVersionUID = -9182879162438446790L;

    private JComponent draggedComp = null; // drag の際に透明フィードバックをかけるために使う

    public StampHolderTransferHandler() {
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        StampHolder source = (StampHolder) c;
        KartePane context = source.getKartePane();
        context.setDrragedStamp(new ComponentHolder[]{source});
        context.setDraggedCount(1);
        ModuleModel stamp = source.getStamp();
        OrderList list = new OrderList(new ModuleModel[]{stamp});
        Transferable tr = new OrderListTransferable(list);
        return tr;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    private void replaceStamp(final StampHolder target, final ModuleInfoBean stampInfo) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                StampDelegater sdl = new StampDelegater();
                StampModel stampModel = sdl.getStamp(stampInfo.getStampId());
                final ModuleModel module = new ModuleModel();
                if (stampModel != null) {
                    module.setModel((IInfoModel) stampModel.getStamp());
                    module.setModuleInfo(stampInfo);
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // 薬の場合は操作する
                        if (module.getModel() instanceof BundleMed) {
                            // 内服薬同士で置き換えの場合，bundle number を保存する
                            BundleMed bundle = (BundleMed) module.getModel();
                            BundleMed orgBundle = (BundleMed) target.getStamp().getModel();
                            if ((ClaimConst.RECEIPT_CODE_NAIYO.equals(bundle.getClassCode()) &&
                                    ClaimConst.RECEIPT_CODE_NAIYO.equals(orgBundle.getClassCode())) ||
                                (ClaimConst.RECEIPT_CODE_TONYO.equals(bundle.getClassCode()) &&
                                    ClaimConst.RECEIPT_CODE_TONYO.equals(orgBundle.getClassCode()))) {

                                bundle.setBundleNumber(orgBundle.getBundleNumber());
                            }
                            // 外用剤同士で置き換えの場合は，量とコメントを保存
                            if (ClaimConst.RECEIPT_CODE_GAIYO.equals(bundle.getClassCode()) &&
                                    ClaimConst.RECEIPT_CODE_GAIYO.equals(orgBundle.getClassCode())) {
                                bundle.setBundleNumber(orgBundle.getBundleNumber());

                                // 元の量とコメントを取り出し
                                String dose = null;

                                for (ClaimItem c : orgBundle.getClaimItem()) {
                                    String code = c.getCode();
                                    // 量を保存
                                    if (dose == null && code.startsWith("6")) dose = c.getNumber();
                                    // コメントを追加
                                    if (code.matches("^[0,8,9].*") &&
                                            ! code.equals("001000001") && // 混合 は除外
                                            ! code.equals("099209908")    // 一般名処方は除外
                                            ) {
                                        // 重複していないコードを追加する
                                        boolean found = false;
                                        for(ClaimItem cc : bundle.getClaimItem()) {
                                            if (cc.getCode().equals(code)) {
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (!found) bundle.addClaimItem(c);
                                    }
                                }
                                // 量を設定
                                if (dose != null) {
                                    for (ClaimItem c : bundle.getClaimItem()) {
                                        if (c.getCode().startsWith("6")) c.setNumber(dose);
                                    }
                                }
                            }
                        }
                        target.importStamp(module);
                    }
                });
            }
        };
        new Thread(r).start();
    }

    private void confirmReplace(StampHolder target, ModuleInfoBean stampInfo) {

        Window w = SwingUtilities.getWindowAncestor(target);
        String replace = "置き換える";
        String cancel = "取消し";

        int option = JOptionPane.showOptionDialog(
                 w,
                 "スタンプを置き換えますか?",
                 "スタンプ Drag and Drop",
                 JOptionPane.DEFAULT_OPTION,
                 JOptionPane.QUESTION_MESSAGE,
                 null,
                 new String[]{replace, cancel}, replace);

         if (option == 0) {
             replaceStamp(target, stampInfo);
         }
    }

    @Override
    public boolean importData(JComponent c, Transferable tr) {

        if (canImport(c, tr.getTransferDataFlavors())) {

            final StampHolder target = (StampHolder) c;
            StampTreeNode droppedNode;

            try {
                droppedNode = (StampTreeNode) tr.getTransferData(LocalStampTreeNodeTransferable.localStampTreeNodeFlavor);

            } catch (UnsupportedFlavorException e) {
                System.out.println("StampHolderTransferHandler.java: " + e);
                e.printStackTrace(System.err);
                return false;
            } catch (IOException e) {
                System.out.println("StampHolderTransferHandler.java: " + e);
                return false;
            }

            if (droppedNode == null || (!droppedNode.isLeaf())) {
                return false;
            }

            final ModuleInfoBean stampInfo = droppedNode.getStampInfo();
            String role = stampInfo.getStampRole();

            if (!role.equals(IInfoModel.ROLE_P)) {
                return false;
            }

            if (Project.getPreferences().getBoolean("replaceStamp", false)) {
                replaceStamp(target, stampInfo);

            } else {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        confirmReplace(target, stampInfo);
                    }
                };
                EventQueue.invokeLater(r);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable tr, int action) {
        StampHolder test = (StampHolder) c;
        KartePane context = test.getKartePane();
        if (action == MOVE &&
                context.getDrragedStamp() != null &&
                context.getDraggedCount() == context.getDroppedCount()) {
            context.removeStamp(test); // TODO
        }
        context.setDrragedStamp(null);
        context.setDraggedCount(0);
        context.setDroppedCount(0);
    }

    /**
     * インポート可能かどうかを返す。
     * @param c
     * @param flavors
     * @return
     */
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        StampHolder test = (StampHolder) c;
        JTextPane tc = test.getKartePane().getTextPane();
        return tc.isEditable() && hasFlavor(flavors);
    }

    protected boolean hasFlavor(DataFlavor[] flavors) {
        for (DataFlavor flavor : flavors) {
            if (LocalStampTreeNodeTransferable.localStampTreeNodeFlavor.equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * スタンプをクリップボードへ転送する。
     */
    @Override
    public void exportToClipboard(JComponent comp, Clipboard clip, int action) {
        StampHolder sh = (StampHolder) comp;
        Transferable tr = createTransferable(comp);
        clip.setContents(tr, null);
        if (action == MOVE) {
            KartePane kartePane = sh.getKartePane();
            if (kartePane.getTextPane().isEditable()) {
                kartePane.removeStamp(sh);
            }
        }
    }

    /**
     * 半透明 drag のために dragged component とマウス位置を保存する
     * @param comp
     * @param e
     * @param action
     */
    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        draggedComp = comp;
        mousePosition = ((MouseEvent)e).getPoint();

        // StampHolder は大きさを 2/3 に縮小して表示する
        // mousePosition.x = mousePosition.x * 2/3;
        // mousePosition.y = mousePosition.y * 2/3;

        super.exportAsDrag(comp, e, action);
    }

    /**
     * 半透明のフィードバックを返す
     * @param t
     * @return
     */
    @Override
    public Icon getVisualRepresentation(Transferable t) {
        if (draggedComp == null) return null;

        int width = draggedComp.getWidth();
        int height = draggedComp.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        draggedComp.paint(image.getGraphics());

        // StampHolder は大きさを 2/3 に縮小して表示する
        // return new ImageIcon(changeSize(image, width*2/3, height*2/3));
        // 縮小すると，パタパタうざくなるのでやめた
        return new ImageIcon(image);
    }

    private BufferedImage changeSize(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width,height, image.getType());
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(image, 0, 0, width, height, null);
        return scaledImage;
    }
}

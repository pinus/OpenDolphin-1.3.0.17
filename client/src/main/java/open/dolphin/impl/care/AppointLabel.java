package open.dolphin.impl.care;

import java.awt.datatransfer.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.ui.PatchedTransferHandler;

/**
 * AppointLabel.
 * Draggable label.
 * @author Kauzshi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class AppointLabel extends JLabel {
    private static final long serialVersionUID = 2843710174202998473L;

    public AppointLabel(String text, Icon icon, int align) {
        super(text, icon, align);
        init();
    }

    private void init() {
        AppointTransferHandler th = new AppointTransferHandler();
        setTransferHandler(th);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                th.exportAsDrag(AppointLabel.this, e, AppointTransferHandler.COPY);
            }
        });
    }

    private class AppointTransferHandler extends PatchedTransferHandler {
        private static final long serialVersionUID = 1L;

        private Icon draggedComp;

        public AppointTransferHandler() {
            super();
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JLabel label = (JLabel)c;
            AppointmentModel appo = new AppointmentModel();
            appo.setName(label.getText());
            return new AppointEntryTransferable(appo);
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        /**
         * 半透明 drag のために dragged component とマウス位置を保存する
         * @param comp
         * @param e
         * @param action
         */
        @Override
        public void exportAsDrag(JComponent comp, InputEvent e, int action) {
            JLabel label = (JLabel) comp;

            draggedComp = label.getIcon();
            mousePosition = label.getMousePosition();

            super.exportAsDrag(comp, e, action);
        }

        /**
         * フィードバックを返す
         * @param t
         * @return
         */
        @Override
        public Icon getVisualRepresentation(Transferable t) {
            return draggedComp;
        }
    }
}

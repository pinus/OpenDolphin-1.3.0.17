package open.dolphin.inspector;

import open.dolphin.client.ChartImpl;
import open.dolphin.client.DiagnosisDocument;
import open.dolphin.client.DiagnosisTransferHandler;
import open.dolphin.stampbox.LocalStampTreeNodeTransferable;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;

/**
 * @author pns
 */
public class DiagnosisInspectorTransferHandler extends TransferHandler {
    private static final long serialVersionUID = 1L;
    private final ChartImpl context;

    public DiagnosisInspectorTransferHandler(ChartImpl context) {
        this.context = context;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }

        DiagnosisDocument doc = context.getDiagnosisDocument();
        DiagnosisTransferHandler handler = (DiagnosisTransferHandler) doc.getDiagnosisTable().getTransferHandler();
        JComponent target = doc.getDiagnosisTable();

        return handler.importData(target, support.getTransferable());
    }

    @Override
    public boolean canImport(TransferSupport support) {
        // drop position の選択をしないようにする
        support.setShowDropLocation(false);

        DataFlavor[] flavors = support.getDataFlavors();
        for (DataFlavor flavor : flavors) {
            if (LocalStampTreeNodeTransferable.localStampTreeNodeFlavor.equals(flavor)) {
                return true;
            }
        }
        return false;
    }
}

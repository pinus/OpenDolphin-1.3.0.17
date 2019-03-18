package open.dolphin.client;

import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.util.ModelUtils;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * object の hashCode で row が取れる JTable
 * 複数選択で作業中に行挿入があった場合にも対応できる
 *
 * @author pns
 */
public class DiagnosisDocumentTable extends JTable {
    private static final long serialVersionUID = 1L;

    private final DiagnosisDocumentTableModel model;

    public DiagnosisDocumentTable(DiagnosisDocumentTableModel model) {
        super(model);
        this.model = model;
    }

    public int[] convertViewRowsToHashArray(int[] viewRows) {
        int[] hashArray = new int[viewRows.length];

        for (int i = 0; i < viewRows.length; i++) {
            int row = convertRowIndexToModel(viewRows[i]);
            hashArray[i] = System.identityHashCode(model.getObject(row));
        }
        return hashArray;
    }

    public int convertHashToModelRow(int hash) {
        for (int row = 0; row < model.getObjectCount(); row++) {
            if (System.identityHashCode(model.getObject(row)) == hash) {
                return row;
            }
        }
        // 見つからなかった
        return -1;
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        int row = rowAtPoint(e.getPoint());

        RegisteredDiagnosisModel rd = model.getObject(row);
        String text = null;
        if (rd != null) {
            String startDate = rd.getStartDate();
            String endDate = rd.getEndDate();
            text = "開始日 " + ModelUtils.toNengo(startDate);
            if (endDate != null) {
                text += String.format(" (終了日 %s)", ModelUtils.toNengo(endDate));
            }
        }
        return text;
    }
}

package open.dolphin.client;

import javax.swing.JTable;

/**
 * object の hashCode で row が取れる JTable
 * 複数選択で作業中に行挿入があった場合にも対応できる
 * @author pns
 */
public class DiagnosisDocumentTable extends JTable {
    private static final long serialVersionUID = 1L;

    private DiagnosisDocumentTableModel model;

    public DiagnosisDocumentTable(DiagnosisDocumentTableModel model) {
        super(model);
        this.model = model;
    }

    public int[] convertViewRowsToHashArray(int[] viewRows) {
        int[] hashArray = new int[viewRows.length];

        for(int i=0; i < viewRows.length; i++) {
            int row = convertRowIndexToModel(viewRows[i]);
            hashArray[i] = System.identityHashCode(model.getObject(row));
        }
        return hashArray;
    }

    public int convertHashToModelRow(int hash) {
        for (int row = 0; row < model.getObjectCount(); row++) {
            if (System.identityHashCode(model.getObject(row)) == hash) return row;
        }
        // 見つからなかった
        return -1;
    }
}

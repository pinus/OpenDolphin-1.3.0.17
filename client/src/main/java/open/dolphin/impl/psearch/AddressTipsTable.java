package open.dolphin.impl.psearch;

import open.dolphin.infomodel.PatientModel;
import open.dolphin.helper.ObjectReflectTableModel;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

/**
 * @author kazm
 * @author pns
 */
public class AddressTipsTable extends JTable {
    private static final long serialVersionUID = -1150173229895931042L;

    public AddressTipsTable() {
        // フォーカス取ったら選択する
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getRowCount() > 0 && getSelectedRow() < 0) {
                    getSelectionModel().setSelectionInterval(0, 0);
                }
            }
        });
    }

    @Override
    public String getToolTipText(MouseEvent e) {

        ObjectReflectTableModel model = (ObjectReflectTableModel) getModel();

        int row = rowAtPoint(e.getPoint());
        int col = columnAtPoint(e.getPoint());
        PatientModel pvt = (PatientModel) model.getObject(row);

        String text = null;

        if (pvt != null) {
            switch (col) {
                case 4: // 生年月日
                    text = pvt.getBirthday();
                    break;

                case 5: // 最終受診日
                    text = pvt.getNengoFormattedLastVisit();
                    break;

                default:
                    text = pvt.contactAddress();
            }
        }

        return text;
    }
}

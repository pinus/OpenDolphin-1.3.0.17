
package open.dolphin.impl.psearch;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.table.ObjectReflectTableModel;

/**
 *
 * @author kazm
 */
public class AddressTipsTable extends JTable {
    private static final long serialVersionUID = -1150173229895931042L;

    @Override
    public String getToolTipText(MouseEvent e) {

//pns   JAVA 6 で sorter は標準装備になったので TableSorter の組込はやめた
        ObjectReflectTableModel model = (ObjectReflectTableModel) getModel();
        //ObjectReflectTableSorter model = (ObjectReflectTableSorter) getModel();

        int row = rowAtPoint(e.getPoint());
        PatientModel pvt = (PatientModel) model.getObject(row);
        return pvt != null ? pvt.contactAddress() : null;
    }
}

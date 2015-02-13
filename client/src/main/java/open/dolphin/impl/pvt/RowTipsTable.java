package open.dolphin.impl.pvt;

import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.JTable;
import open.dolphin.infomodel.KarteState;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.table.ObjectReflectTableModel;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 *
 * @author kazm
 */
public class RowTipsTable extends JTable {
    private static final long serialVersionUID = 1L;

    @Override
    public String getToolTipText(MouseEvent e) {

        ObjectReflectTableModel model = (ObjectReflectTableModel) getModel();
        int row = rowAtPoint(e.getPoint());
        PatientVisitModel pvt = (PatientVisitModel) model.getObject(row);

//pns^  待ち時間表示
        if (pvt == null) return null;
        Date pvtDate = ModelUtils.getDateTimeAsObject(pvt.getPvtDate());
        int pvtState = pvt.getState();
        String waitingTime = "";
        if (pvtDate != null &&
                (pvtState == KarteState.CLOSE_NONE || pvtState == KarteState.OPEN_NONE)) {
            waitingTime = " - 待ち時間 " + DurationFormatUtils.formatPeriod(pvtDate.getTime(), new Date().getTime(), "HH:mm");
        }

        return pvt.getPatient().getKanaName() + waitingTime;
//pns$

//      return pvt != null ? pvt.getPatient().getKanaName() : null;
    }
}

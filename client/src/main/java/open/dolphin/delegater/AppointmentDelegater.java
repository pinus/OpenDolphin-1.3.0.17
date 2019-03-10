package open.dolphin.delegater;

import java.util.ArrayList;
import java.util.List;

import open.dolphin.dto.AppointSpec;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.service.KarteService;

/**
 * AppointmentDelegater.
 *
 * @author Kazushi Minagawa
 * @author pns
 */
public class AppointmentDelegater extends BusinessDelegater<KarteService> {

    /**
     * 予約を登録する.
     *
     * @param appoints 新規/更新/取消 された予約のリスト
     * @return result code (常に 0)
     */
    public int putAppointments(List<AppointmentModel> appoints) {

        int size = appoints.size();
        List<AppointmentModel> added = new ArrayList<>();
        List<AppointmentModel> updated = new ArrayList<>();
        List<AppointmentModel> removed = new ArrayList<>();

        appoints.stream().forEach(appoint -> {
            int state = appoint.getState();
            String appoName = appoint.getName();

            if (state == AppointmentModel.TT_NEW) {
                // 新規予約
                added.add(appoint);

            } else if (state == AppointmentModel.TT_REPLACE && appoName != null) {
                // 変更された予約
                updated.add(appoint);

            } else if (state == AppointmentModel.TT_REPLACE && appoName == null) {
                // 取り消された予約
                removed.add(appoint);
            }
        });

        int retCode = 0;
        AppointSpec spec = new AppointSpec();
        spec.setAdded(added);
        spec.setUpdated(updated);
        spec.setRemoved(removed);

        getService().putAppointment(spec);
        return retCode;
    }
}

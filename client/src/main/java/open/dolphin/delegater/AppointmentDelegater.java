package open.dolphin.delegater;

import java.util.ArrayList;
import open.dolphin.dto.AppointSpec;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.service.KarteService;

/**
 * AppointmentDelegater
 *
 * @author Kazushi Minagawa
 */
public class AppointmentDelegater extends BusinessDelegater {

    public int putAppointments(ArrayList results) {

        int size = results.size();
        ArrayList<AppointmentModel> added = new ArrayList<>();
        ArrayList<AppointmentModel> updated = new ArrayList<>();
        ArrayList<AppointmentModel> removed = new ArrayList<>();

        for (int i = 0; i < size; i++) {

            AppointmentModel model = (AppointmentModel) results.get(i);
            int state = model.getState();
            String appoName = model.getName();

            if (state == AppointmentModel.TT_NEW) {
                // 新規予約
                added.add(model);

            } else if (state == AppointmentModel.TT_REPLACE && appoName != null) {
                // 変更された予約
                updated.add(model);

            } else if (state == AppointmentModel.TT_REPLACE && appoName == null) {
                // 取り消された予約
                removed.add(model);
            }
        }

        int retCode = 0;
        AppointSpec spec = new AppointSpec();
        spec.setAdded(added);
        spec.setUpdared(updated);
        spec.setRemoved(removed);

        getService().putAppointment(spec);
        return retCode;
    }

    private KarteService getService() {
        return getService(KarteService.class);
    }
}

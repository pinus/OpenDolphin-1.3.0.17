package open.dolphin.dto;

import open.dolphin.infomodel.AppointmentModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * AppointSpec.
 *
 * @author Minagawa, Kazushi
 */
public class AppointSpec implements Serializable {
    private static final long serialVersionUID = -2819531469105475380L;

    private Collection<AppointmentModel> added;
    private Collection<AppointmentModel> updated;
    private Collection<AppointmentModel> removed;

    public Collection<AppointmentModel> getAdded() {
        return Collections.unmodifiableCollection(added);
    }

    public void setAdded(Collection<AppointmentModel> added) {
        this.added = new ArrayList<>(added);
    }

    public void setUpdated(Collection<AppointmentModel> updated) {
        this.updated = new ArrayList<>(updated);
    }

    public Collection<AppointmentModel> getUpdated() {
        return Collections.unmodifiableCollection(updated);
    }

    public Collection<AppointmentModel> getRemoved() {
        return Collections.unmodifiableCollection(removed);
    }

    public void setRemoved(Collection<AppointmentModel> removed) {
        this.removed = new ArrayList<>(removed);
    }
}

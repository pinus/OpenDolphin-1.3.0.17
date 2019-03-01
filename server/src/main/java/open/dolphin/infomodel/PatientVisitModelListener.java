package open.dolphin.infomodel;

import open.dolphin.WebSocket;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import open.dolphin.JsonConverter;

/**
 * PatientVisitModel が変化したら通知を受ける Listener.
 * see PatientVisitModel @EntityListeners
 *
 * @author pns
 */
public class PatientVisitModelListener {

    /**
     * Inform pvt update to WebSocket clients.
     *
     * @param pvt PatientVisitModel
     */
    @PostPersist
    @PostUpdate
    public void pvtUpdated(final PatientVisitModel pvt) {
        WebSocket.getSessions().forEach(session -> session.getAsyncRemote().sendText(JsonConverter.toJson(pvt)));
    }

    @PostRemove
    public void pvtRemoved(final PatientVisitModel pvt) {
        pvt.setState(KarteState.CANCEL_PVT);
        pvtUpdated(pvt);
    }
}

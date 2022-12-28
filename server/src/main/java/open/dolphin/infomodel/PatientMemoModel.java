package open.dolphin.infomodel;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * PatientMemoModel.
 *
 * @author Minagawa, Kazushi
 */
@Entity
@Table(name = "d_patient_memo")
public class PatientMemoModel extends KarteEntryBean<PatientMemoModel> {
    private static final long serialVersionUID = 5125449675384830669L;

    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}

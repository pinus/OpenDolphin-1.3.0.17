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
    
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}

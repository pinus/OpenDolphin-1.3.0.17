package open.dolphin.infomodel;

import open.dolphin.util.ModelUtils;

import java.util.Date;
import javax.persistence.*;

/**
 * AppointmentModel.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Entity
@Table(name = "d_appo")
public class AppointmentModel extends KarteEntryBean<AppointmentModel> {
    private static final long serialVersionUID = 6166365309219504946L;

    public static final int TT_NONE = 0;
    public static final int TT_NEW = 1;
    public static final int TT_HAS = 2;
    public static final int TT_REPLACE = 3;

    /** 施設内の患者ID */
    private String patientId;

    @Transient
    private int state;

    @Column(name="c_name", nullable = false)
    private String name;

    private String memo;

    @Column(name="c_date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date date;

    public int getState() {
        return state;
    }

    public void setState(int val) {
        state = val;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date val) {
        date = val;
    }

    public String getName() {
        return name;
    }

    public void setName(String val) {
        name = val;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String val) {
        memo = val;
    }

    /**
     * The patientId to set.
     * @param patientId 施設内の患者 ID
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Returns the patientId.
     * @return 施設内の患者 ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * 予約日で比較する.
     * @param o AppointmentModel
     * @return 比較値
     */
    @Override
    public int compareTo(AppointmentModel o) {
        Date s1 = this.date;
        Date s2 = o.getDate();
        return s1.compareTo(s2);
    }

    @Override
    public String toString() {
        return ModelUtils.getDateAsString(getDate());
    }
}

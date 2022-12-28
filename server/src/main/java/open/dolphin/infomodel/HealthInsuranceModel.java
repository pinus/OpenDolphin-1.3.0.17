package open.dolphin.infomodel;

import jakarta.persistence.*;

/**
 * HealthInsuranceModel.
 *
 * @author Minagawa, kazushi
 */
@Entity
@Table(name = "d_health_insurance")
public class HealthInsuranceModel extends InfoModel {
    private static final long serialVersionUID = 3064687655700962022L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    @Column(nullable = false)
    private byte[] beanBytes;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientModel patient;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getBeanBytes() {
        return beanBytes;
    }

    public void setBeanBytes(byte[] beanBytes) {
        this.beanBytes = beanBytes;
    }

    public PatientModel getPatient() {
        return patient;
    }

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final HealthInsuranceModel other = (HealthInsuranceModel) obj;
        return (id == other.getId());
    }
}

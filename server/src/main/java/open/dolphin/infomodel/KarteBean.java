package open.dolphin.infomodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * KarteBean.
 *
 * @author Minagawa,Kazushi
 */
@Entity
@Table(name = "d_karte")
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
public class KarteBean extends InfoModel {
    private static final long serialVersionUID = 4658519288418950016L;

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @IndexedEmbedded
    @ManyToOne
    @JoinColumn(name="patient_id", nullable=false)
    private PatientModel patient;

    @Column(nullable=false)
    @Temporal(value = TemporalType.DATE)
    private Date created;

    // Entry fields -　KarteService.getKarte で付加する field:
    // オリジナルは Map<String,List> entries になってたのを分けた
    // allergy
    @Transient
    private List<AllergyModel> allergyEntry;
    // height / weight
    @Transient
    private List<PhysicalModel> physicalEntry;
    // visit
    @Transient
    private List<String> pvtDateEntry;
    // docInfo
    @Transient
    private List<DocInfoModel> docInfoEntry;
    // patientMemo
    @Transient
    private PatientMemoModel patientMemo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PatientModel getPatient() {
        return patient;
    }

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<AllergyModel> getAllergyEntry() {
        return allergyEntry;
    }

    public void setAllergyEntry(List<AllergyModel> allergyEntry) {
        this.allergyEntry = allergyEntry;
    }

    public List<PhysicalModel> getPhysicalEntry() {
        return physicalEntry;
    }

    public void setPhysicalEntry(List<PhysicalModel> physicalEntry) {
        this.physicalEntry = physicalEntry;
    }

    public List<String> getPvtDateEntry() {
        return pvtDateEntry;
    }

    public void setPvtDateEntry(List<String> visitEntry) {
        this.pvtDateEntry = visitEntry;
    }

    public List<DocInfoModel> getDocInfoEntry() {
        return docInfoEntry;
    }

    public void setDocInfoEntry(List<DocInfoModel> docInfoEntry) {
        this.docInfoEntry = docInfoEntry;
    }

    public PatientMemoModel getPatientMemo() {
        return patientMemo;
    }

    public void setPatientMemo(PatientMemoModel patientMemo) {
        this.patientMemo = patientMemo;
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
        final KarteBean other = (KarteBean) obj;
        return (id == other.getId());
    }
}

package open.dolphin.orca.orcaapi.bean;

/**
 * Medication_Information. 服用情報(繰り返し5)
 * @author pns
 */
public class MedicationInformation {
    /**
     * 服用時点(0:服用しない、1:服用する) (例:  )
     */
    private String Medication_Point;

    /**
     * 服用時点(0:服用しない、1:服用する) (例:  )
     * @return the Medication_Point
     */
    public String getMedication_Point() {
        return Medication_Point;
    }

    /**
     * 服用時点(0:服用しない、1:服用する) (例:  )
     * @param Medication_Point the Medication_Point to set
     */
    public void setMedication_Point(String Medication_Point) {
        this.Medication_Point = Medication_Point;
    }
}

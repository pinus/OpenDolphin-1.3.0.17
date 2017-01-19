package open.dolphin.order;

import java.util.List;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;

/**
 *
 * @author pns
 */
public class EditorProduct {
    private String entity;
    private List<RegisteredDiagnosisModel> diagnosisList;
    private ModuleModel moduleModel;

    /**
     * @return the entity
     */
    public String getEntity() {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * @return the diagnosisList
     */
    public List<RegisteredDiagnosisModel> getDiagnosisList() {
        return diagnosisList;
    }

    /**
     * @param diagnosisList the diagnosisList to set
     */
    public void setDiagnosisList(List<RegisteredDiagnosisModel> diagnosisList) {
        this.diagnosisList = diagnosisList;
    }

    /**
     * @return the moduleModel
     */
    public ModuleModel getModuleModel() {
        return moduleModel;
    }

    /**
     * @param moduleModel the moduleModel to set
     */
    public void setModuleModel(ModuleModel moduleModel) {
        this.moduleModel = moduleModel;
    }
}

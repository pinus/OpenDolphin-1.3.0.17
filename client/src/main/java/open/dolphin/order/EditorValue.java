package open.dolphin.order;

import java.util.List;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;

/**
 *
 * @author pns
 */
public class EditorValue {
    private String entity;
    private boolean isNew = false;
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

    /**
     * @return the isNew
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * @param isNew the isNew to set
     */
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
}
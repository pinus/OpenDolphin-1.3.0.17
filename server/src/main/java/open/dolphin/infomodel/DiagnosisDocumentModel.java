package open.dolphin.infomodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Diagnosisドキュメントのモデル
 *
 * @author Kazushi Minagawa
 */
public class DiagnosisDocumentModel extends InfoModel {
    private static final long serialVersionUID = 9160022188485390133L;

    private PatientLiteModel patientLiteModel;
    private UserLiteModel creatorLiteModel;
    private DocInfoModel docInfo;
    private ModuleModel[] moduleModel;

    public DiagnosisDocumentModel() {
        docInfo = new DocInfoModel();
        docInfo.setDocType(DOCTYPE_DIAGNOSIS);
    }

    public DocInfoModel getDocInfoModel() {
        return docInfo;
    }

    public void setDocInfoModel(DocInfoModel docInfo) {
        this.docInfo = docInfo;
    }

    public void setModuleModel(ModuleModel[] module) {
        this.moduleModel = module;
    }

    public ModuleModel[] getModuleModel() {
        return moduleModel;
    }

    public void addModule(ModuleModel[] moules) {
        if (moduleModel == null) {
            moduleModel = new ModuleModel[moules.length];
            System.arraycopy(moules, 0, moduleModel, 0, moules.length);
            return;
        }
        int len = moduleModel.length;
        ModuleModel[] dest = new ModuleModel[len + moules.length];
        System.arraycopy(moduleModel, 0, dest, 0, len);
        System.arraycopy(moules, 0, dest, len, moules.length);
        moduleModel = dest;
    }

    public void addModule(ModuleModel value) {
        if (moduleModel == null) {
            moduleModel = new ModuleModel[1];
            moduleModel[0] = value;
            return;
        }
        int len = moduleModel.length;
        ModuleModel[] dest = new ModuleModel[len + 1];
        System.arraycopy(moduleModel, 0, dest, 0, len);
        moduleModel = dest;
        moduleModel[len] = value;
    }

    public ModuleModel getModule(String entityName) {

        if (moduleModel != null) {

            ModuleModel ret = null;

            for (ModuleModel model : moduleModel) {
                if (model.getModuleInfo().getEntity().equals(entityName)) {
                    ret = model;
                    break;
                }
            }
            return ret;
        }

        return null;
    }

    public ModuleInfoBean[] getModuleInfo(String entityName) {

        if (moduleModel != null) {

            List<ModuleInfoBean> list = new ArrayList<>(2);

            Arrays.asList(moduleModel).stream()
                    .filter(model -> model.getModuleInfo().getEntity().equals(entityName))
                    .forEach(model -> list.add(model.getModuleInfo()));

            if (! list.isEmpty()) {
                return  list.toArray(new ModuleInfoBean[list.size()]);
            }
        }

        return null;
    }

    public void setPatientLiteModel(PatientLiteModel patientLiteModel) {
        this.patientLiteModel = patientLiteModel;
    }

    public PatientLiteModel getPatientLiteModel() {
        return patientLiteModel;
    }

    public void setCreatorLiteModel(UserLiteModel creatorLiteModel) {
        this.creatorLiteModel = creatorLiteModel;
    }

    public UserLiteModel getCreatorLiteModel() {
        return creatorLiteModel;
    }
}

package open.dolphin.delegater;

import java.util.*;
import open.dolphin.infomodel.*;
import open.dolphin.service.PnsService;

/**
 * いろいろやってみる delegater
 * @author pns
 */
public class PnsDelegater extends BusinessDelegater {

    /**
     * 今日のカルテの記載を返す（カルテ未記入をチェックするため）
     * @param patientId
     * @return
     */
    public String peekKarte(Long patientId) {
        List<ModuleModel> list = getService().peekKarte(patientId);
        // 今日のカルテがない場合
        if (list == null) { return null; }

        String text = null;

        // soa の記入を取り出す
        for (ModuleModel bean : list) {
            String role = bean.getModuleInfo().getStampRole();
            if (role.equals(IInfoModel.ROLE_SOA_SPEC)) {
                String xml = ((ProgressCourse) bean.getModel()).getFreeText();
                text = ModelUtils.extractText(xml);
                break;
            }
        }
        return text;
    }

    public void makeInitialIndex() {
        getService().makeInitialIndex();
    }

    private PnsService getService() {
        return getService(PnsService.class);
    }
}

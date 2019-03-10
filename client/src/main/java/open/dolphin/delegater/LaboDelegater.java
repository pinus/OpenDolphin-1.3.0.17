package open.dolphin.delegater;

import java.util.List;

import open.dolphin.dto.LaboSearchSpec;
import open.dolphin.infomodel.LaboModuleValue;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.service.LaboService;

/**
 * Labo 関連の Delegater クラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class LaboDelegater extends BusinessDelegater<LaboService> {

    /**
     * LaboModule を保存する.
     *
     * @param value
     * @return LaboModule を保存した PatientModel
     */
    public PatientModel putLaboModule(LaboModuleValue value) {
        return getService().putLaboModule(value);
    }

    /**
     * 患者の検体検査モジュールを取得する.
     *
     * @param spec LaboSearchSpec 検索仕様
     * @return laboModule の Collection
     */
    public List<LaboModuleValue> getLaboModules(LaboSearchSpec spec) {
        return getService().getLaboModuleList(spec);
    }
}

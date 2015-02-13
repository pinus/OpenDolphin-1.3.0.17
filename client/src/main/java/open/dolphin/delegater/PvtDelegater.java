package open.dolphin.delegater;

import java.util.List;
import open.dolphin.dto.PatientVisitSpec;
import open.dolphin.dto.PvtStateSpec;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.service.PvtService;

/**
 *
 * @author pns
 */
public class PvtDelegater extends BusinessDelegater {

    /**
     * 受付情報 PatientVisitModel をデータベースに登録する。
     * @param pvtModel   受付情報 PatientVisitModel
     * @return 保存に成功した個数
     */
    public int addPvt(PatientVisitModel pvtModel) {
        return getService().addPvt(pvtModel);
    }

    /**
     * 来院情報をデータベースから取得する。
     * @param date     検索する来院日
     * @param firstRecord 何番目のレコードから取得するか
     * @return PatientVisitModel の List
     */
    public List<PatientVisitModel> getPvt(String[] date, int firstRecord) {

        PatientVisitSpec spec = new PatientVisitSpec();
        spec.setDate(date[0]);
        spec.setAppodateFrom(date[1]);
        spec.setAppodateTo(date[2]);
        spec.setSkipCount(firstRecord);

        // HealthInsurance は lazy fetch なので，後でカルテオープン時に取りに行くこと
        return getService().getPvtList(spec);
    }

    /**
     * 受付情報を削除する。
     * @param id PatientVisitModel の primary key
     * @return 削除件数
     */
    public int removePvt(long id) {
        return getService().removePvt(id);
    }

    /**
     * 診察終了情報を書き込む。
     * @param id PatientVisitModel の primary key
     * @param state
     * @return
     */
    public int updatePvtState(long id, int state) {
        PvtStateSpec spec = new PvtStateSpec();
        spec.setPk(id);
        spec.setState(state);
        return getService().updatePvtState(spec);
    }

    /**
     * 付いている病名数を書き込む
     * @param pk
     * @param total 総病名数
     * @param today 今日付いた病名数
     * @return 1
     */
    public int setByomeiCount(long pk, int total, int today) {
        PvtStateSpec spec = new PvtStateSpec();
        spec.setPk(pk);
        spec.setByomeiCount(total);
        spec.setByomeiCountToday(today);
        return getService().setByomeiCount(spec);
    }

    /**
     * pvt state だけスピーディーにとってくる
     * @return State 番号のリスト
     */
    public List<PvtStateSpec> getPvtState() {
        return getService().getPvtStateList();
    }

    /**
     * PatientVisitModel pk の state を取ってくる
     * @param pk
     * @return
     */
    public int getPvtState(long pk) {
        return getService().getPvtState(pk);
    }

    /**
     * 引数の PatientModel をもつ今日の PatientVisitModel があれば取ってくる
     * @param patient
     * @return
     */
    public PatientVisitModel getPvt(PatientModel patient) {
        return getService().getPvtOf(patient);
    }

    private PvtService getService() {
        return getService(PvtService.class);
    }
}

package open.dolphin.orcaapi;

import java.util.List;
import open.dolphin.client.Chart;
import open.dolphin.dao.OrcaMasterDao;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;

/**
 * Orca Api で診療内容送信
 * @author pns
 */
public abstract class OrcaApi {

    private static final OrcaMasterDao dao = SqlDaoFactory.createOrcaMasterDao();
    private static final boolean is47 = OrcaMasterDao.ORCA_DB_VER_47.equals(dao.getDbVersion());
    private static OrcaApi orcaApi;

    static {
        if (is47) {
            orcaApi = new OrcaApi47();
            //System.out.println("OrcaApi: orca 4.7 detected");
        } else {
            orcaApi = new OrcaApi46();
            //System.out.println("OrcaApi: orca 4.6 detected");
        }
    }

    public static OrcaApi getInstance() {
        return orcaApi;
    }

    /**
     * OrcaApi に Chart を登録する
     * @param ctx
     */
    public abstract void setContext(Chart ctx);

    /**
     * 診療内容を ORCA に送る
     * @param documentModel
     */
    public abstract void send(final DocumentModel documentModel);

    /**
     * 病名を ORCA に送る
     * @param diagnoses
     */
    public abstract void send(final List<RegisteredDiagnosisModel> diagnoses);

}

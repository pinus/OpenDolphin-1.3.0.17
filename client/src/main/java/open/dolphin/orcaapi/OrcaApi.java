package open.dolphin.orcaapi;

import java.util.List;
import open.dolphin.client.Chart;
import open.dolphin.dao.OrcaDao;
import open.dolphin.dao.OrcaMasterDao;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import org.apache.log4j.Logger;

/**
 * Orca Api で診療内容送信
 * @author pns
 */
public abstract class OrcaApi {

    private static final OrcaMasterDao dao = SqlDaoFactory.createOrcaMasterDao();
    private static OrcaApi orcaApi;

    static {
        switch(dao.getDbVersion()) {
            case OrcaDao.ORCA_DB_VER_46:
                Logger.getLogger(OrcaApi.class).info("orca 4.6 detected");
                orcaApi = new OrcaApi46();
                break;
            case OrcaDao.ORCA_DB_VER_47:
                Logger.getLogger(OrcaApi.class).info("orca 4.7 detected");
                orcaApi = new OrcaApi47();
                break;
            case OrcaDao.ORCA_DB_VER_48:
                Logger.getLogger(OrcaApi.class).info("orca 4.8 detected");
                orcaApi = new OrcaApi47();
                break;
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

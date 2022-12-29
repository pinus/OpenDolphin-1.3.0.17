package open.dolphin.orca.orcadao;

import org.jboss.logging.Logger;

/**
 * ORCA Database にアクセスするための DAO.
 *
 * @author pns
 */
public class OrcaDao {
    private static final OrcaDao DAO = new OrcaDao();
    private final OrcaExtraInfo extraInfo;
    private final Logger logger;

    private OrcaDao() {
        extraInfo = new OrcaExtraInfo();
        logger = Logger.getLogger(OrcaDao.class);
        logger.info("OrcaDao created");
    }

    /**
     * OrcaDao のインスタンスを返す.
     *
     * @return OrcaDao のインスタンス
     */
    public static OrcaDao getInstance() {
        return DAO;
    }

    /**
     * OrcaDbConnection を返す.
     *
     * @param rsp ResultSetProcessor
     * @return OrcaDbConnection
     */
    public OrcaDbConnection getConnection(ResultSetProcessor rsp) {
        return new OrcaDbConnection(rsp);
    }

    /**
     * HospNum を返す.
     *
     * @return hospNum
     */
    public int getHospNum() {
        return extraInfo.getHospNum();
    }

    /**
     * OrcaExtraInfo を返す.
     *
     * @return OrcaExtraInfo
     */
    public OrcaExtraInfo getExtraInfo() {
        return extraInfo;
    }

    /**
     * Process error.
     *
     * @param e Exception
     */
    public void processError(Exception e) {
        e.printStackTrace(System.err);
    }
}

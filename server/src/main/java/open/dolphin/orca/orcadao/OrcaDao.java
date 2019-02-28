package open.dolphin.orca.orcadao;

/**
 * ORCA Database にアクセスするための DAO.
 *
 * @author pns
 */
public class OrcaDao {
    private static final OrcaDao DAO = new OrcaDao();
    private static final OrcaExtraInfo EXTRA_INFO = new OrcaExtraInfo();

    private OrcaDao() {
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
        return EXTRA_INFO.getHospNum();
    }

    /**
     * OrcaExtraInfo を返す.
     *
     * @return OrcaExtraInfo
     */
    public OrcaExtraInfo getExtraInfo() {
        return EXTRA_INFO;
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

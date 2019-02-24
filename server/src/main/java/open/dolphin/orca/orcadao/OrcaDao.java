package open.dolphin.orca.orcadao;

/**
 * ORCA Database にアクセスするための DAO.
 * @author pns
 */
public class OrcaDao {

    private static OrcaDao dao = new OrcaDao();
    private static OrcaExtraInfo extraInfo = new OrcaExtraInfo();

    private OrcaDao() {
    }

    /**
     * OrcaDao のインスタンスを返す.
     * @return OrcaDao のインスタンス
     */
    public static OrcaDao getInstance() {
        return dao;
    }

    /**
     * OrcaDbConnection を返す.
     * @param rsp ResultSetProcessor
     * @return OrcaDbConnection
     */
    public OrcaDbConnection getConnection(ResultSetProcessor rsp) {
        return new OrcaDbConnection(rsp);
    }

    /**
     * HospNum を返す.
     * @return hospNum
     */
    public int getHospNum() {
        return extraInfo.getHospNum();
    }

    /**
     * OrcaExtraInfo を返す.
     * @return OrcaExtraInfo
     */
    public OrcaExtraInfo getExtraInfo() {
        return extraInfo;
    }

    /**
     * Process error.
     * @param e Exception
     */
    public void processError(Exception e) {
        e.printStackTrace(System.err);
    }

    public static void main(String[] argv) {
    }
}

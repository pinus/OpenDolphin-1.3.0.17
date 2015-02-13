package open.dolphin.dao;

/**
 * SqlDaoFactory
 *
 * @author  pns
 */
public class SqlDaoFactory {

    private SqlDaoFactory() {}

    /**
     * OrcaMasterDao 作成
     * @return
     */
    public static OrcaMasterDao createOrcaMasterDao() {
        return new OrcaMasterDao();
    }
}

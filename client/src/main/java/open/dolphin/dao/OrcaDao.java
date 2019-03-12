package open.dolphin.dao;

import open.dolphin.project.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * OrcaDao
 * ORCA 基本レベル　HospNum 取得とバージョン確認
 * referring to v2.2 and masuda-sensei's dao codes
 * @author pns
 */

public class OrcaDao extends SqlDaoBean {

    // orca 接続情報
    private static final int ORCA_PORT = 5432;
    private static final String ORCA_DATABASE = "orca";
    private static final String ORCA_USER = "orca";
    private static final String ORCA_PASSWD = "";

    // orca version string
    //public static final String ORCA_DB_VER_45 = "040500-1";
    public static final String ORCA_DB_VER_46 = "040600-1";
    public static final String ORCA_DB_VER_47 = "040700-1";
    public static final String ORCA_DB_VER_48 = "040800-1";

    private static String dbVersion = null;
    private static int hospNum = 1;

    // hospNum を調べるためのクエリ
    private static final String QUERY_HOSPNUM = "select hospnum, kanritbl from tbl_syskanri where kanricd='1001' and kanritbl ~ ?";
    // dbVersion を調べるためのクエリ
    private static final String QUERY_DBVERSION = "select version from tbl_dbkanri where kanricd='ORCADB00'";

    public OrcaDao() {
        super();
        setDriver(PG_DRIVER);
        setHost(Project.getClaimAddress());
        setPort(ORCA_PORT) ;
        setDatabase(ORCA_DATABASE);
        setUser(ORCA_USER);
        setPasswd(ORCA_PASSWD);
        setHospNum();
    }

    /**
     * ORCA バージョンを返す
     * @return
     */
    public String getDbVersion() {
        return dbVersion;
    }

    /**
     * hospNum を返す
     * @return
     */
    public int getHospNum() {
        return hospNum;
    }

    /**
     * hospNum と dbVersion を調べてセットする
     */
    private void setHospNum() {
        if (dbVersion != null) { return; }

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(QUERY_HOSPNUM);
            ps.setString(1, Project.getJMARICode());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hospNum = rs.getInt(1);
                //System.out.println("hospNum=" + hospNum);
            }

            ps = con.prepareStatement(QUERY_DBVERSION);
            rs = ps.executeQuery();
            if (rs.next()) {
                dbVersion = rs.getString(1);
                //System.out.println("dbVersion=" + dbVersion);
            }

            rs.close();

        } catch (Exception e) {
            processError(e);
        }
        closeStatement(ps);
        closeConnection(con);
    }
}

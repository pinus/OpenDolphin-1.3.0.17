package open.dolphin.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.postgresql.ds.PGPoolingDataSource;

/**
 * SqlDaoBean
 * driver, database レベル
 * @author  Kazushi Minagawa
 * connection pooling 対応，対象データベースは postgres で決め打ちにした
 * modified by pns
 */
public class SqlDaoBean extends DaoBean {

    public static final String PG_DRIVER = "org.postgresql.Driver";
    private static PGPoolingDataSource datasource = null;

    private String database;
    private String protocol;
    private String driver;
    private boolean trace = true;

    public SqlDaoBean() {
        super();
    }

    /**
     * jdbc driver のクラス名を返す
     * @return
     */
    public String getDriver() {
        return driver;
    }

    /**
     * jdbc driver のクラス名をセットして，使えるかどうかチェック
     * @param driver
     */
    public void setDriver(String driver) {

        this.driver = driver;

        try {
            Class.forName(driver);
            protocol = "jdbc:postgresql";

        } catch (ClassNotFoundException cnfe) {
            System.out.println("SqlDaoBean.java: " + cnfe);
            logger.warn("Couldn't find the driver!");
            logger.warn("Let's print a stack trace, and exit.");
            cnfe.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * DataSrouce ベースのアクセスをする場合に DataSource の初期化
     */
    private void initializeDataSource() {
        // postgres 接続プール処理データソース
        datasource = new PGPoolingDataSource();
        datasource.setDataSourceName("postgresql");
        datasource.setServerName(getHost());
        datasource.setDatabaseName(database);
        datasource.setUser(getUser());
        datasource.setPassword(getPasswd());
        datasource.setMaxConnections(10);
        datasource.setInitialConnections(1);
    }

    /**
     * データベース名を返す
     * @return
     */
    public String getDatabase() {
        return database;
    }

    /**
     * データベース名をセットする
     * @param base
     */
    public void setDatabase(String base) {
        database = base;
    }

    /**
     * DriverManager ベースのアクセスをするときの URL を返す
     * @return
     */
    //private String getURL() {
    //    return String.format("%s://%s:%s/%s", protocol, getHost(), getPort(), getDatabase());
    //}

    /**
     * Connection を開始する
     * @return
     * @throws Exception
     */
    public Connection getConnection() throws Exception {
        if (datasource == null) initializeDataSource();
        return datasource.getConnection();
        //return DriverManager.getConnection(getURL(), getUser(), getPasswd());
    }

    /**
     * Connection を閉じる
     * @param con
     */
    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Statement を閉じる
     * @param st
     */
    public void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            }
            catch (SQLException e) {
            	e.printStackTrace();
            }
        }
    }

    /**
     * debug メッセージをログに出力
     * @param msg
     */
    protected void debug(String msg) {
        logger.debug(msg);
    }

    /**
     * trace メッセージをログに出力
     * @param msg
     */
    protected void printTrace(String msg) {
        if (trace) logger.debug(msg);
    }

    /**
     * Connection のトランザクション取り消し
     * @param con
     */
    protected void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

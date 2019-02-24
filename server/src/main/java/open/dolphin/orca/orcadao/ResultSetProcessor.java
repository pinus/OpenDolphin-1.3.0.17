package open.dolphin.orca.orcadao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSetProcessor.
 * ResultSet に対して行う手続きを記載する.
 * @author pns
 */
public interface ResultSetProcessor {

    public void process(ResultSet rs) throws SQLException;
}

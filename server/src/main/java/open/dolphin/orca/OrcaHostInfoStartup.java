package open.dolphin.orca;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import open.dolphin.orca.orcaapi.OrcaApi;
import open.dolphin.orca.orcaapi.OrcaApiHandler;
import open.dolphin.orca.orcadao.OrcaDao;
import open.dolphin.orca.pushapi.PushApi;
import org.jboss.logging.Logger;

import java.util.Map;

@Startup
@Singleton
public class OrcaHostInfoStartup {
    private final Logger logger = Logger.getLogger(OrcaHostInfoStartup.class);

    /**
     * Instantiate Orca-related singletons according to the dependencies.
     */
    public OrcaHostInfoStartup() {
        logger.info("OrcaHostInfoStartup start");

        OrcaHostInfo orcaHostInfo = OrcaHostInfo.getInstance();
        logger.info("OrcaHostInfo = " + orcaHostInfo);

        OrcaApiHandler orcaApiHandler = OrcaApiHandler.getInstance();
        logger.info("OrcaApiHandler = " + orcaApiHandler);

        OrcaApi orcaApi = OrcaApi.getInstance();
        logger.info("OrcaApi = " + orcaApi);

        Map<String,String> orcaUserInfoHashMap = OrcaUserInfo.getHashMap();
        orcaUserInfoHashMap.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).forEach(logger::info);

        PushApi pushApi = PushApi.getInstance();
        logger.info("PushApi = " + pushApi);

        OrcaDao orcaDao = OrcaDao.getInstance();
        logger.info("OrcaDao = " + orcaDao);

        logger.info("OrcaHostInfoStartup done");
    }
}

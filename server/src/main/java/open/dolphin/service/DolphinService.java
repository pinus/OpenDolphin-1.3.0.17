package open.dolphin.service;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import open.dolphin.infomodel.InfoModel;
import org.jboss.resteasy.util.Base64;

/**
 *
 * @author pns
 */
public abstract class DolphinService {
    @PersistenceContext(unitName = "DolphinPU")
    protected EntityManager em;

    @Context
    private HttpHeaders headers;

    /**
     * Header 情報から Caller の FacilityId:username 部分を取り出す
     * @return
     */
    private String getCallerId() {
        try {
            String encoded = headers.getHeaderString("Authorization");
            String usernameAndPassword = new String(Base64.decode(encoded));
            String[] split = usernameAndPassword.split(InfoModel.PASSWORD_SEPARATOR);
            return split[0];

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        return "";
    }

    /**
     * CallerId から FacilityId 部分を切り出す
     * @return
     */
    protected String getCallersFacilityId() {
        String[] split = getCallerId().split(InfoModel.COMPOSITE_KEY_MAKER);
        return split[0];
    }

    /**
     * 与えられた id からFacilityId 部分を切り出す
     * @param checkId
     * @return
     */
    protected String getRequestsFacilityId(String checkId) {
        String[] split = checkId.split(InfoModel.COMPOSITE_KEY_MAKER);
        return split[0];
    }

    /**
     * Caller と CheckId の FacilityId を比較して，一致していなければ SecurityException を出す
     * @param checkId
     * @return
     */
    protected String checkFacility(String checkId) {
        String callerKey = getCallersFacilityId();
        String requestKey = getRequestsFacilityId(checkId);
        if (! callerKey.equals(requestKey)) {
            throw new SecurityException(requestKey);
        }
        return checkId;
    }
}

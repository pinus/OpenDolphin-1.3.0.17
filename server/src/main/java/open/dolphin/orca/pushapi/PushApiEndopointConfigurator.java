package open.dolphin.orca.pushapi;

import jakarta.websocket.ClientEndpointConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Header に Tenant ID をのせる configurator.
 *
 * @author pns
 */
public class PushApiEndopointConfigurator extends ClientEndpointConfig.Configurator {

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        headers.put("X-GINBEE-TENANT-ID", Arrays.asList("1"));
    }
}

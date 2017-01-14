package open.dolphin.impl.login;

import java.util.EventListener;

/**
 *
 * @author pns
 */
public interface LoginListener extends EventListener {

    public void state(LoginState state);
}

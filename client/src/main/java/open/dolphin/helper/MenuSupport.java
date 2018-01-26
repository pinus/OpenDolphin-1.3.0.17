package open.dolphin.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import open.dolphin.client.ClientContext;
import org.apache.log4j.Logger;

/**
 * Object に対して順番にリフレクションで method を実行していく.
 * MainWindow 層では
 * <ul>
 * <li>chains[0] = MainWindow (WaitingListImpl/PatientSearchImpl/LaboTestImporter)
 * <li>chains[1] = null
 * <li>chains[2] = this
 * <li>chains[3] = Dolphin
 * </ul>
 * addChain するところは {@link open.dolphin.client.Dolphin#initComponents() Dolphin} のタブ切換設定の部分.
 * StampBoxPlugin, ImageBox もインスタンスを持っているが，メニュー表示だけで adChain は未実装.<br>
 * ChartDocument 層での使用は {@link open.dolphin.client.ChartMediator ChartMediator} で行う.
 *
 * @author Minagawa, Kazushi
 * @author pns
 */
public class MenuSupport implements MenuListener {

    private ActionMap actions;
    private final Object[] chains = new Object[4];
    private final Logger logger = ClientContext.getBootLogger();

    public MenuSupport(Object owner) {
        setDefaultChains(owner);
        //logger.setLevel(Level.DEBUG);
    }

    private void setDefaultChains(Object owner) {
        chains[2] = this;
        chains[3] = owner;
    }

    @Override
    public void menuSelected(MenuEvent e) {}

    @Override
    public void menuDeselected(MenuEvent e) {}

    @Override
    public void menuCanceled(MenuEvent e) {}

    /**
     * メニューの ActionMap.
     * @param actions
     */
    public void registerActions(ActionMap actions) {
        this.actions = actions;
    }

    public Action getAction(String name) {
        if (actions != null) {
            return actions.get(name);
        }
        return null;
    }

    public ActionMap getActions() {
        return actions;
    }

    public void disableAllMenus() {
        if (actions != null) {
            Arrays.asList(actions.keys()).forEach(obj -> actions.get(obj).setEnabled(false));
        }
    }

    public void disableMenus(String[] menus) {
        enableMenus(menus, false);
    }

    public void enableMenus(String[] menus) {
        enableMenus(menus, true);
    }

    public void enableMenus(String[] menus, boolean enable) {
        if (actions != null && menus != null) {
            Stream.of(menus)
                    .map(actions::get)
                    .filter(Objects::nonNull)
                    .forEach(action -> action.setEnabled(enable));
        }
    }

    public void enableAction(String name, boolean enabled) {
        if (actions != null) {
            Action action = actions.get(name);
            if (action != null) {
                action.setEnabled(enabled);
            }
        }
    }

    /**
     * 最初のターゲットに設定する.
     * @param obj
     */
    public void addChain(Object obj) {
        chains[0] = obj;
        logger.debug("MenuSupport: addChain = " + obj);
        logger.debug("chains[1] = " + chains[1]);
        logger.debug("chains[2] = " + chains[2]);
    }

    /**
     * 最初のターゲットを返す.
     * @return
     */
    public Object getChain() {
        return chains[0];
    }

    /**
     * 二番目のターゲットに設定する.
     * @param obj
     */
    public void addChain2(Object obj) {
        chains[1] = obj;
        logger.debug("MenuSupport: addChain2 = " + obj);
        logger.debug("chains[0] = " + chains[0]);
        logger.debug("chains[2] = " + chains[2]);
    }

    /**
     * ２番目のターゲットを返す.
     * @return
     */
    public Object getChain2() {
        return chains[1];
    }

    /**
     * chain にそってリフレクションでメソッドを実行する.
     * メソッドを実行するオブジェクトがあればそこで終了する.
     * メソッドを実行するオブジェクトが存在しない場合もそこで終了する.
     * コマンドチェインパターンのリフレクション版.
     * @param method
     * @return メソッドが実行された時 true
     */
    public boolean sendToChain(String method) {
        logger.debug(String.format("MenuSupport: sendToChain: %s: [0]=%s, [1]=%s,[2]=%s", method, chains[0], chains[1], chains[2]));

        boolean handled = false;

        for (Object target : chains) {

            if (target != null) {
                try {
                    Method mth = target.getClass().getMethod(method, (Class[])null);
                    // System.out.println("invoked: " + target.getClass() + "#" + method);
                    mth.invoke(target, (Object[])null);
                    handled = true;
                    break;

                } catch (IllegalAccessException | IllegalArgumentException | SecurityException ex) { System.out.println("MenuSupport.java: " + ex);
                } catch (InvocationTargetException ex) { System.out.println("MenuSupport.java: " + ex); ex.printStackTrace(System.err);

                // この target では実行できない. NoSuchMethodException が出るのは問題なし.
                } catch (NoSuchMethodException ex) { //System.out.println("MenuSupport.java: " + ex);
                }
            }
        }

        return handled;
    }
}

package open.dolphin.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * MenuSupport.
 * Object に対して順番にリフレクションで method を実行していく
 * chains[0] = addChain で登録した Object
 * chains[1] = this
 * chains[2] = コンストラクタで指定した owner
 *
 * @author Minagawa,Kazushi
 */
public class MenuSupport implements MenuListener {

    private ActionMap actions;
    private final Object[] chains = new Object[3];

    public MenuSupport(Object owner) {
        setInitialChains(owner);
    }

    private void setInitialChains(Object owner) {
        chains[1] = this;
        chains[2] = owner;
    }

    @Override
    public void menuSelected(MenuEvent e) {}

    @Override
    public void menuDeselected(MenuEvent e) {}

    @Override
    public void menuCanceled(MenuEvent e) {}

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
            Arrays.asList(menus).stream()
                    .map(name -> actions.get(name))
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

    public void addChain(Object obj) {
        // 最初のターゲットに設定する
        chains[0] = obj;
    }

    public Object getChain() {
        // 最初のターゲットを返す
        return chains[0];
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

        boolean handled = false;

        if (chains != null) {

            for (Object target : chains) {

                if (target != null) {
                    try {
                        Method mth = target.getClass().getMethod(method, (Class[])null);
                        // System.out.println("invoked: " + target.getClass() + "#" + method);
                        mth.invoke(target, (Object[])null);
                        handled = true;
                        break;
                    // この target では実行できない. NoSuchMethodException が出るのは問題なし.
                    } catch (IllegalAccessException | IllegalArgumentException | SecurityException ex) { System.out.println("MenuSupport.java: " + ex);
                    } catch (InvocationTargetException ex) { System.out.println("MenuSupport.java: " + ex); ex.printStackTrace(System.err);
                    } catch (NoSuchMethodException ex) { //System.out.println("MenuSupport.java: " + ex);
                    }
                }
            }
        }
        return handled;
    }

    public void cut() {}

    public void copy() {}

    public void paste() {}
}

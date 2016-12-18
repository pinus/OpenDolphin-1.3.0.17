package open.dolphin.helper;

import java.awt.event.ActionEvent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 * ＠Action で action を作っていろいろするクラス
 * @author pns
 */
public class ActionManager {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Action {}

    private final ActionMap actionMap;

    public ActionManager(Object obj) {
        actionMap = getActionMap(obj);
    }

    /**
     * メソッド名をキーとする ActionMap を作成する
     * @param obj
     * @return
     */
    public static ActionMap getActionMap(final Object obj) {

        ActionMap map = new ActionMap();

        Method[] methods = obj.getClass().getMethods();
        for (Method m : methods) {
            // @Action がついている medthod を ActionMap に追加
            if (m.getAnnotation(Action.class) != null) {
                final Method method = m;
                javax.swing.Action action = new AbstractAction() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            method.invoke(obj, (Object[]) null);

                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            System.out.println("ActionManager.java: " + ex);
                        }
                    }
                };
                map.put(m.getName(), action);
            }
        }
        return map;
    }
    /**
     * ActionMap に登録された action に名前とアイコンを付けて JMenuItem として取り出す
     * static バージョン
     * @param actionMap
     * @param key
     * @param name
     * @param icon
     * @return
     */
    public static JMenuItem getMenuItem(ActionMap actionMap, String key, String name, ImageIcon icon) {
        JMenuItem item = new JMenuItem();
        javax.swing.Action action = actionMap.get(key);
        action.putValue(javax.swing.Action.NAME, name);
        action.putValue(javax.swing.Action.SMALL_ICON, icon);
        item.setAction(action);

        return item;
    }

    /**
     * ActionMap に登録された action に名前とアイコンを付けて JMenuItem として取り出す
     * instance 作って使うバージョン
     * @param key
     * @param name
     * @param icon
     * @return
     */
    public JMenuItem getMenuItem(String key, String name, ImageIcon icon) {
        return getMenuItem(actionMap, key, name, icon);
    }

    /**
     * 生成した ActionMap を返す
     * @return
     */
    public ActionMap getActionMap() {
        return actionMap;
    }
}

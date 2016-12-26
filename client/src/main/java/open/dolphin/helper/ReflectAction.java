package open.dolphin.helper;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;

public class ReflectAction extends AbstractAction {
    private static final long serialVersionUID = 4592935637937407137L;

    private final Object target;
    private final String method;

    /**
     * Action which invokes target.method()
     * @param name
     * @param target
     * @param method
     */
    public ReflectAction(String name, Object target, String method) {
        super(name);
        this.target = target;
        this.method = method;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            Method mth = target.getClass().getMethod(method, (Class[]) null);
            mth.invoke(target, (Object[])null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException ex) {
            System.out.println("ReflectAction.java: " + ex);
        } catch (InvocationTargetException ex) {
            System.out.println("ReflectAction.java: " + ex); ex.printStackTrace(System.err);
        }
    }
}

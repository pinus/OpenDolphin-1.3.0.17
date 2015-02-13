package open.dolphin.helper;

import java.awt.event.ActionListener;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * CallbackDocumentListener
 *
 * @author Minagawa,Kazushi
 */
public class ProxyActionListener {

    /**
     * create AcitonListener without parameters
     * @param target
     * @param methodName
     * @return
     */
    public static ActionListener create(final Object target, final String methodName) {
        return create(target, methodName, null, null);
    }

    /**
     * crate ActionListener with parameters
     * @param target
     * @param methodName
     * @param argClass classes of parameters
     * @param args values of parameters
     * @return
     */
    public static ActionListener create(final Object target, final String methodName, final Class[] argClass, final Object[] args) {

        Class cls = ActionListener.class;
        ClassLoader cl = cls.getClassLoader();

        InvocationHandler handler = new InvocationHandler() {

            /**
             * invoke parameters are not used
             * p = Proxy, m = actionPerformed, o[0] = ActionEvent
             */
            @Override
            public Object invoke(Object p, Method m, Object[] o) {

                Object result = null;

                try {

                    Method targetMethod = target.getClass().getMethod(methodName, argClass);
                    result = targetMethod.invoke(target, args);

                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace(System.err);
                } catch (SecurityException ex) {
                    ex.printStackTrace(System.err);
                } catch (InvocationTargetException ex) {
                    System.out.println("ProxyActionListener: target class:" + target.getClass() + " method:" + methodName);
                    ex.printStackTrace(System.err);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace(System.err);
                } catch (NoSuchMethodException ex) {
                    ex.printStackTrace(System.err);
                }

                return result;
            }
        };

        return (ActionListener) Proxy.newProxyInstance(cl, new Class[]{cls}, handler);

    }
}

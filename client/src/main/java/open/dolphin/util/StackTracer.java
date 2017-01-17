package open.dolphin.util;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pns
 */
public class StackTracer {

    public static void showTrace() {
        Throwable t = new Throwable();
        StackTraceElement[] e = t.getStackTrace();

        for (int i=4; i<e.length; i++) {
            System.out.println("depth:" + i + " : " + e[i].getClassName() + "#" + e[i].getMethodName());
        }
    }

    public static List<StackTraceElement> getTrace() {
        return Arrays.asList(new Throwable().getStackTrace());
    }
}

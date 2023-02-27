package open.dolphin.helper;

import java.util.Arrays;
import java.util.List;

/**
 * StackTracer.
 *
 * @author pns
 */
public class StackTracer {

    public static void showTrace() {
        showTrace(6);
    }

    public static void showTrace(int depth) {
        Throwable t = new Throwable();
        StackTraceElement[] e = t.getStackTrace();
        int min = Math.min(e.length, depth);

        for (int i = 1; i < min; i++) {
            System.out.printf("%d:%s#%s:%d%n", i, e[i].getClassName(), e[i].getMethodName(), e[i].getLineNumber());
        }
    }

    public static List<StackTraceElement> getTrace() {
        return Arrays.asList(new Throwable().getStackTrace());
    }
}

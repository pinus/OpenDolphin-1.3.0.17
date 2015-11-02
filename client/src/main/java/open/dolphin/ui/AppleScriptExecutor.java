package open.dolphin.ui;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.swing.SwingWorker;

/**
 *
 * @author pns
 */
public class AppleScriptExecutor extends Thread {

    private String code;
    private int ptime;
    private long startTime;

    public AppleScriptExecutor(String code) {
        this.code = code;
        this.ptime = 100;
        this.startTime = new Date().getTime();
    }

    @Override
    public void run() {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                // protection time for waiting events to work before script starts
                Thread.sleep(ptime);

                return new ScriptEngineManager().getEngineByName("AppleScriptEngine").eval(code);
            }
        };
        worker.execute();

        try {
            // AppleScript から帰ってこなくなることがあるので，10秒戻ってこなかったら強制終了とする
            worker.get(10, TimeUnit.SECONDS);
            long past = new Date().getTime() - startTime;
            if (past > 500) {
                System.out.println("AppleScriptExecutor: Code below took more than 500msec!");
                System.out.println("AppleScriptExecutor: " +  code);
                System.out.println("AppleScriptExecutor: done in " + past + "msec");
            }

        // Exception が出たら，その engine は破棄する
        } catch (InterruptedException ex) {
            System.out.println("AppleScriptExecutor: " + ex);
        } catch (ExecutionException ex) {
            System.out.println("AppleScriptExecutor: " + ex);
        } catch (TimeoutException ex) {
            System.out.println("AppleScriptExecutor: " + ex);
        }
    }

    public static void main (String[] s) {
        ScriptEngineManager m = new ScriptEngineManager();

        for (ScriptEngineFactory f : m.getEngineFactories()) {
            System.out.println(f.getEngineName());
        }
    }
}

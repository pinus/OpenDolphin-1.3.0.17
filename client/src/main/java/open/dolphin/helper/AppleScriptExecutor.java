package open.dolphin.helper;

import open.dolphin.ui.sheet.JSheet;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.script.ScriptEngineManager;
import javax.swing.SwingWorker;

/**
 *
 * @author pns
 */
public class AppleScriptExecutor extends Thread {

    private final String code;
    private final int ptime;
    private final long startTime;
    private final Logger logger;

    public AppleScriptExecutor(String code) {
        this.logger = Logger.getLogger(getClass());
        this.code = code;
        this.ptime = 100;
        this.startTime = new Date().getTime();
    }

    @Override
    public void run() {
        logger.info("AppleScriptExecutor: Code = " + code);

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
                logger.info("AppleScriptExecutor: Code below took more than 500msec!" + "\n" +
                        code + "\n" +
                        "done in " + past + "msec");
            }

        // Exception が出たら，その engine は破棄する
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            System.out.println("AppleScriptExecutor: " + ex);
            ex.printStackTrace(System.err);
        }
    }

    public static void main (String[] s) {
        ScriptEngineManager m = new ScriptEngineManager();
        m.getEngineFactories().forEach(f -> System.out.println(f.getEngineName()));
    }
}

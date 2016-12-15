package open.dolphin.helper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import open.dolphin.client.Chart;
import open.dolphin.client.ClientContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @param <T>
 */
public abstract class DBTask<T> extends SwingWorker<T,Void> implements PropertyChangeListener {
    private static long TIMEOUT = 60*1000L;
    private Chart context;
    private Logger logger;

    protected DBTask() {
    }

    public DBTask(Chart context) {
        this.context = context;
        logger = ClientContext.getBootLogger();
        connect();
    }

    private void connect() {
        addPropertyChangeListener(this);
    }

    @Override
    protected abstract T doInBackground() throws Exception;

    protected void succeeded(T result) {
    }

    protected void cancelled() {
        logger.debug("DBTask cancelled");
    }

    protected void timeout() {
        JFrame parent = context.getFrame();
        StringBuilder sb = new StringBuilder();
        sb.append("データベースアクセスにタイムアウトが生じました。");
        sb.append("\n");
        sb.append("リトライをお願いします。");
        String title = "DBタスク";
        JOptionPane.showMessageDialog(
                parent,
                sb.toString(),
                ClientContext.getFrameTitle(title),
                JOptionPane.WARNING_MESSAGE);
        logger.debug("DBTask timeout");
    }

    protected void failed(Throwable cause) {
        logger.warn("DBTask failed");
        cause.printStackTrace(System.err);
    }

    private void startProgress() {
        context.getDocumentHistory().blockHistoryTable(true);
        //progressBar.setIndeterminate(true);
    }

    private void stopProgress() {
        //progressBar.setIndeterminate(false);
        //progressBar.setValue(0);
        context.getDocumentHistory().blockHistoryTable(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

        if ("state".equals(e.getPropertyName())) {
            switch ((StateValue) e.getNewValue()) {
                case STARTED:
                    startProgress();
                    break;

                case DONE:
                    stopProgress();
                    break;
            }
        }
    }

    @Override
    protected void done() {

        if (isCancelled()) {
            cancelled();

        } else {
            try {
                succeeded( get(TIMEOUT, TimeUnit.SECONDS) );

            } catch (InterruptedException ex) {
                cancelled();
            } catch (ExecutionException | RuntimeException ex) {
                failed(ex);
            } catch (TimeoutException ex) {
                timeout();
            }
        }

    }
}

package open.dolphin.helper;

import open.dolphin.client.Chart;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * ChartTask - database task for chart.
 *
 * @param <T> object to return
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public abstract class ChartTask<T> extends PNSTask<T> implements PropertyChangeListener {
    private static final int TIMEOUT = 10000;
    private final Chart context;

    public ChartTask(Chart context) {
        super(context.getFrame(), "", "");
        this.context = context;
        setInputBlocker(new Blocker());
        setTimeOut(TIMEOUT);
    }

    protected void succeeded(T result) { }

    private class Blocker implements PNSTask.InputBlocker {
        @Override
        public void block() {
            if (Objects.nonNull(context.getFrame())) {
                context.getDocumentHistory().blockHistoryTable(true);
            }
        }

        @Override
        public void unblock() {
            if (Objects.nonNull(context.getFrame())) {
                context.getDocumentHistory().blockHistoryTable(false);
            }
        }
    }
}

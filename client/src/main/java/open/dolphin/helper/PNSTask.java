package open.dolphin.helper;

import open.dolphin.ui.PNSProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Progress 表示付き Task.
 *
 * @param <T> Target
 * @author pns
 */
public abstract class PNSTask<T> extends SwingWorker<T, Integer> implements ActionListener, PropertyChangeListener {
    /**
     * ProgressMonitor のレンジは 0-100 で固定なので MAX は 100 で固定する.
     */
    private static final double MAX = 100;
    /**
     * 起動している PNSTask instance のリスト. 表示される ProgressMonitor を１つにするのに使う.
     */
    private static final List<PNSTask<?>> taskList = new ArrayList<>();
    /**
     * 進捗状況を表示する間隔 (msec).
     */
    private static final int INTERVAL = 500;

    private PNSProgressMonitor progressMonitor;
    private Timer timer;
    private InputBlocker blocker;
    private int timeout;
    /**
     * interval と timeout から計算される ProgressMonitor の増加量.
     */
    private double tick = 0;
    /**
     * 進捗値　0〜MAX の間を動く.
     */
    private double current = 0;
    /**
     * cancel の時に処理に interrupt を入れるかどうか.
     */
    private boolean interrupt = true;

    public PNSTask() {
        this(null);
    }

    public PNSTask(Component parent) {
        this(parent, "", "");
    }

    /**
     * 進捗状況を自分で setProgress する PNSTask を作る.
     * setProgress しないと，勝手に 1/10 ずつ count up していくが終了はしない.
     *
     * @param parent  ダイアログの親
     * @param message ダイアログに出すメッセージ（後から変更不可）
     * @param note    ダイアログに出すノート（後から変更可能）
     */
    public PNSTask(Component parent, Object message, String note) {
        this(parent, message, note, 0);
    }

    /**
     * 一定時間ごとに進捗状況を表示する PNSTask を作る.
     * maxEstimation=0 の場合は自分で setProgress して管理する必要あり.
     *
     * @param parent        ダイアログの親
     * @param message       ダイアログに出すメッセージ
     * @param note          ダイアログに出すノート（後から setNote で変更可能）
     * @param maxEstimation timeout までの時間（msec）
     */
    public PNSTask(Component parent, Object message, String note, int maxEstimation) {
        super();

        // interval (msec) ごとに interrupt して進捗状況を表示
        timeout = maxEstimation;
        if (maxEstimation != 0) {
            tick = MAX * INTERVAL / maxEstimation;
        }
        // this をコンストラクタから分離
        initialize(parent, message, note);
    }

    private void initialize(Component c, Object message, String note) {
        timer = new Timer(INTERVAL, this);
        progressMonitor = new PNSProgressMonitor(c, message, note, 0, 100);
        taskList.add(this);
        addPropertyChangeListener(this);
    }

    /**
     * ProgressMonitor を出す前に無条件で待つ時間 (default = 500 msec).
     *
     * @param msec 待ち時間
     */
    public void setMillisToDecidePopup(int msec) {
        progressMonitor.setMillisToDecideToPopup(msec);
    }

    /**
     * この時間が予想残り時間より短い場合に ProgressMonitor を出す.
     * 残り時間は，setValue の度に経過時間から計算される.
     *
     * @param msec 待ち時間
     */
    public void setMillisToPopup(int msec) {
        progressMonitor.setMillisToPopup(msec);
    }

    /**
     * Time out までの時間 (msec) 0=time out しない.
     *
     * @param msec Time out までの時間
     */
    public void setTimeOut(int msec) {
        this.timeout = msec;
    }

    /**
     * ProgressMonitor に note を表示.
     *
     * @param note Note
     */
    public void setNote(String note) {
        progressMonitor.setNote(note);
    }

    /**
     * ProgressMonitor を表示するのは，一番先に加わった task のみにする.
     * たくさん ProgressMonitor dialog が出るとうざいので.
     *
     * @param nv new value
     */
    private void setProgressMonitorProgress(int nv) {
        if (!taskList.isEmpty() && taskList.get(0).hashCode() == this.hashCode()) {
            progressMonitor.setProgress(nv);
        }
    }

    /**
     * PNSTask#InputBlocker をセットする.
     *
     * @param blocker InputBlocker
     */
    public void setInputBlocker(InputBlocker blocker) {
        this.blocker = blocker;
    }

    /**
     * Cancel の時に，強制 interrupt かけるかどうか.
     * doInBackground に自分でキャンセル処理入れた場合 false にする.
     *
     * @param b true to interrupt
     */
    public void setInterruptOnCancel(boolean b) {
        interrupt = b;
    }

    /**
     * interval 時間ごとに実行される action.
     * ProgressBar 表示処理，timeout 処理をする.
     *
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // interval ごとに cancel チェック
        if (progressMonitor.isCanceled()) {
            cancel(interrupt);
            return;
        }

        setProgressMonitorProgress((int) current);

        if (timeout == 0) {
            // timeout しない場合は残りの 1/10 を足す
            current += (MAX - current) / 10;

        } else {
            current += tick;
            // timeout した場合は強制的に cancel
            if (current >= MAX) {
                cancel(true);
            }
        }
    }

    /**
     * SwingWorker からの propertyChange を受け取る
     *
     * @param e PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case "state" -> {
                if (StateValue.STARTED.equals(e.getNewValue())) {
                    // 一定時間ごとに割り込んで進捗状況を表示するタイマーをスタート
                    if (!timer.isRunning()) { timer.start(); }
                    if (Objects.nonNull(blocker)) { blocker.block(); }
                }
            }
            case "progress" -> {
                // setProgress した場合呼ばれる
                int nv = (Integer) e.getNewValue();
                setProgressMonitorProgress(nv);
                current = nv;
            }
        }
    }

    @Override
    protected void done() {
        timer.stop();
        timer.removeActionListener(this);
        if (Objects.nonNull(blocker)) { blocker.unblock(); }
        progressMonitor.close();
        removePropertyChangeListener(this);
        taskList.remove(this);

        if (isCancelled()) {
            cancelled();
        } else {
            try {
                succeeded(get());
            } catch (InterruptedException e) {
                interrupted(e);
            } catch (ExecutionException e) {
                failed(e.getCause());
            }
        }
    }

    protected abstract void succeeded(T result);

    protected void cancelled() {
    }

    protected void failed(Throwable cause) {
    }

    protected void interrupted(InterruptedException ex) {
    }

    /**
     * 入力ブロッカー
     */
    public interface InputBlocker {
        void block();
        void unblock();
    }

    public static void main(String[] argv) {
        PNSTask<String> task = new PNSTask<>(null, "テスト１", "実行中...") {
            @Override
            protected String doInBackground() throws Exception {
                for (int i = 0; i < 4; i++) {
                    setProgress(i * 25);
                    Thread.sleep(1000);
                }
                return "test";
            }

            @Override
            protected void succeeded(String result) {
                System.out.println("ended: " + new java.util.Date());
                System.out.println("result=" + result);
            }

            @Override
            protected void cancelled() {
                System.out.println("Canceled");
            }

            @Override
            protected void failed(Throwable cause) {
                System.out.println("failed " + cause);
            }

            @Override
            protected void interrupted(InterruptedException ex) {
                System.out.println("interrupted " + ex);
            }

        };
        task.setTimeOut(3000);
        task.execute();
        System.out.println("PNSTask thread started: " + new java.util.Date());

        PNSTask<Object> task2 = new PNSTask<>(null, "テスト２", "実行中...") {
            @Override
            protected Object doInBackground() throws Exception {
                System.out.println("task2 start");
                for (int i = 0; i < 5; i++) {
                    setProgress(i * 20);
                    Thread.sleep(1000);
                }
                return null;
            }

            @Override
            protected void succeeded(Object result) {
                System.out.println("task2 done");
            }

        };
        task2.setTimeOut(5000);
        task2.execute();


        System.out.println("Main thread is waiting for the thread done.");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
        }
        System.out.println("Main thread ended.");
    }
}


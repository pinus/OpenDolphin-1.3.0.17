package open.dolphin.impl.server;

import open.dolphin.client.ClientContext;
import open.dolphin.delegater.PvtDelegater;
import open.dolphin.infomodel.PatientVisitModel;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kazushi Minagawa. Digital Globe, Inc.
 */
public final class PVTSender implements Runnable {

    private final List queue = new LinkedList();
    private Thread senderThread;

    public void startService() {
        senderThread = new Thread(this);
        senderThread.setPriority(Thread.NORM_PRIORITY);
        senderThread.start();
    }

    public void stopService() {
        if (senderThread!=null) {
            Thread t = senderThread;
            senderThread = null;
            t.interrupt();
        }
    }

    public void processPvt(String pvtXml) {
        synchronized (queue) {
            queue.add(pvtXml);
            queue.notify();
        }
    }

    private void addPvt(String pvtXml) {
        BufferedReader r = new BufferedReader(new StringReader(pvtXml));
        PVTBuilder builder = new PVTBuilder();
        builder.parse(r);
        PatientVisitModel model = builder.getProduct();

        PvtDelegater pdl = new PvtDelegater();
        pdl.addPvt(model);
    }

    private String getPvt() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
        }
        return (String) queue.remove(0);
    }

    @Override
    public void run() {

        Thread thisThread = Thread.currentThread();

        while (thisThread==senderThread) {
            try {
                String pvtXml = getPvt();
                addPvt(pvtXml);
            } catch (InterruptedException e) {
                ClientContext.getPvtLogger().warn("PVT Sender interrupted");
            }
        }
    }
}

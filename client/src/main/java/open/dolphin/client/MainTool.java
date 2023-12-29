package open.dolphin.client;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * MainTool. The lowest level interface for
 * Chart (ChartImpl, EditorFrame) and MainComponent (WaitingListImpl, PatientSearchImpl, LaboTestImporter)
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public interface MainTool {
    String getName();
    void setName(String name);
    MainWindow getContext();
    void setContext(MainWindow context);
    default JPanel getUI() { return null; }
    default void setUI(JPanel panel) { }
    void start();
    void stop();
    void enter();
    Callable<Boolean> getStartingTask();
    Callable<Boolean> getStoppingTask();
}

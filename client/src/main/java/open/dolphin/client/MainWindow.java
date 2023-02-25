package open.dolphin.client;

import open.dolphin.helper.MenuSupport;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.ui.PNSFrame;

import javax.swing.*;
import java.awt.print.PageFormat;
import java.util.HashMap;

/**
 * アプリケーションのメインウインドウインターフェイスクラス.
 *
 * @author Minagawa, Kazushi. Digital Globe, Inc.
 * @author pns
 */
public interface MainWindow {
    HashMap<String, MainService> getProviders();

    void setProviders(HashMap<String, MainService> providers);

    JMenuBar getMenuBar();

    MenuSupport getMenuSupport();

    void registerActions(ActionMap actions);

    Action getAction(String name);

    void enableAction(String name, boolean b);

    void openKarte(PatientVisitModel pvt);

    void addNewPatient();

    MainService getPlugin(String name);

    PageFormat getPageFormat();

    void showStampBox();

    void showSchemaBox();

    PNSFrame getFrame();
}

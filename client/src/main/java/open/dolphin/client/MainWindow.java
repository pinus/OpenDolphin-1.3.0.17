package open.dolphin.client;

import open.dolphin.helper.MenuSupport;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.ui.PNSFrame;

import java.awt.print.PageFormat;

/**
 * アプリケーションのメインウインドウインターフェイスクラス.
 *
 * @author Minagawa, Kazushi. Digital Globe, Inc.
 * @author pns
 */
public interface MainWindow {
    MenuSupport getMenuSupport();

    void enableAction(String name, boolean b);

    void openKarte(PatientVisitModel pvt);

    <T extends MainTool> T getPlugin(Class<T> clazz);

    PageFormat getPageFormat();

    void showStampBox();

    void showSchemaBox();

    PNSFrame getFrame();
}

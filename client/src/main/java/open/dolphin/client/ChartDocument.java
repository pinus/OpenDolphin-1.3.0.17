package open.dolphin.client;

import open.dolphin.infomodel.DocumentModel;

import javax.swing.*;


/**
 * チャートドキュメントが実装するインターフェイス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public interface ChartDocument {
    JPanel getUI();
    String getTitle();
    void setTitle(String title);
    Chart getContext();
    void setContext(Chart ctx);
    DocumentModel getDocument();
    void setDocument(DocumentModel documentModel);
    boolean isDirty();
    void setDirty(boolean dirty);

    default void start() { }
    default void stop() { }
    default void enter() { }
    default void save() { }
    default void print() { }
}

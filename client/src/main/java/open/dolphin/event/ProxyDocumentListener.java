package open.dolphin.event;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * ProxyDocumentListener.
 * 全ての update で anyUpdate だけを呼ぶ DocumentListener.
 * ProxyDocumentListener = e -> ... と書ける
 *
 * @author pns
 */
public interface ProxyDocumentListener extends DocumentListener {
    @Override
    default void insertUpdate(DocumentEvent e) {
        anyUpdate(e);
    }
    @Override
    default void removeUpdate(DocumentEvent e) {
        anyUpdate(e);
    }
    @Override
    default void changedUpdate(DocumentEvent e) {
        anyUpdate(e);
    }
    void anyUpdate(DocumentEvent e);
}

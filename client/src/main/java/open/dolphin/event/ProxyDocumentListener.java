package open.dolphin.event;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * ProxyDocumentListener.
 * 全ての update で anyUpdate だけを呼ぶ DocumentListener.
 * ProxyDocumentListener = e -> ... と書ける
 * @author pns
 */
public interface ProxyDocumentListener extends DocumentListener {

    @Override
    public default void insertUpdate(DocumentEvent e) { anyUpdate(e); }

    @Override
    public default void removeUpdate(DocumentEvent e) { anyUpdate(e); }

    @Override
    public default void changedUpdate(DocumentEvent e) { anyUpdate(e); }

    public void anyUpdate(DocumentEvent e);
}

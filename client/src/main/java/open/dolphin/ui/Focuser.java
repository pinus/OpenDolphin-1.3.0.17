package open.dolphin.ui;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 * Focuser.  Focus request を一元管理する.<br>
 * Request Focus を発行している場所のまとめ.
 * <ul>
 * <li>AbstractComponentHolder - mousePressed, mouseDragged
 * <li>AbstractMainComponent - ContextListener#mousePressed
 * <li>AddUser - text fields
 * <li>AllergyEditor - factorFld
 * <li>ChangePassword - text fields
 * <li>DiagnosisDocument - enter(), selectAll()
 * <li>DiagnosisDocumentPopupMenu - rightPressed(e)
 * <li>FindDialog - searchTextField
 * <li>HostSettingPanel - text fields
 * <li>MasterSearchPanel - requestFocusOnTextField() (called by StampEditor)
 * <li>NewKarteDialog - start()
 * <li>PatientSearchPanel - keywordField
 * <li>PatientSearchImpl - keywordField
 * <li>TextStampEditor - enter()
 * <li>StampBoxPlugin - EditorValueListener
 * <li>StampHolderPopupMenu - PutCommentAction
 * </ul>
 *
 * @author pns
 */
public class Focuser {
    private static final Logger logger = Logger.getLogger(Focuser.class);
    private static Component component;

    public static void requestFocus(Component c) {
        component = c;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
        SwingUtilities.invokeLater(Focuser::request);
    }

    private static void request() {
        boolean result = component.requestFocusInWindow();

        logger.info(component.getClass().toString() + ": request focus " + ((result)? "succeeded" : "failed"));
    }
}
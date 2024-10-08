package open.dolphin.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

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
    private static final Logger logger = LoggerFactory.getLogger(Focuser.class);
    //private static List<StackTraceElement> stackTrace;

    public static void requestFocus(Component c) {
        //stackTrace = StackTracer.getTrace();
        Component owner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
        if (!c.equals(owner)) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            request(c);
            //SwingUtilities.invokeLater(() -> request(c));
        }
    }

    private static void request(Component component) {
        boolean succeeded = component.requestFocusInWindow();
        //if (!succeeded) {
            // stackTrace.stream().map(e -> e.toString()).forEach(System.out::println);
        //    logger.info(component.getClass() + ": request focus failed");
        //}
    }
}

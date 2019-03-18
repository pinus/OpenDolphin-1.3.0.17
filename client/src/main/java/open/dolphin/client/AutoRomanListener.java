package open.dolphin.client;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author kazm
 */
public class AutoRomanListener implements FocusListener {

    private static AutoRomanListener instance = new AutoRomanListener();

    /**
     * Creates a new instance of AutoRomanListener
     */
    private AutoRomanListener() {
    }

//pns    public static AutoRomanListener getInstance() {
//pns        return instance;
//pns    }

    public void focusGained(FocusEvent e) {
        Object source = e.getSource();
        //System.out.println("---AutoRomanListener: focusGained: " + source.getClass());
        if (source != null && source instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) source;
            if (tc.getInputContext() != null) {
                tc.getInputContext().setCharacterSubsets(null);
            }
        }
    }

    public void focusLost(FocusEvent e) {
        //System.out.println("---AutoRomanListener: focusLost: " + e.getSource().getClass());
    }
}

package open.dolphin.client;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.im.InputSubset;

/**
 *
 * @author Minagawa, Kazushi
 */
public class AutoKanjiListener implements FocusListener {

    private static AutoKanjiListener instance = new AutoKanjiListener();

    /** Creates a new instance of AutoIMEListener */
    private AutoKanjiListener() {
    }

//pns    public static AutoKanjiListener getInstance() {
//pns        return instance;
//pns    }

    public void focusGained(FocusEvent e) {
        Object source = e.getSource();
        //System.out.println("---AutoKanjiListener: focusGained: " + source.getClass());
        if (source != null && source instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) source;
            if (tc.getInputContext() != null) {
                tc.getInputContext().setCharacterSubsets(new Character.Subset[] {InputSubset.KANJI});
            }
        }
    }

    public void focusLost(FocusEvent e) {
        Object source = e.getSource();
        //System.out.println("---AutoKanjiListener: focusLost: " + source.getClass());
        if (source != null && source instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) source;
            if (tc.getInputContext() != null) {
                tc.getInputContext().setCharacterSubsets(null);
            }
        }
    }
}

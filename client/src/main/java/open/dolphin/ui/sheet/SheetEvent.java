package open.dolphin.ui.sheet;

import java.util.EventObject;

/**
 *
 * @author pns
 */
public class SheetEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    private int option;

    public SheetEvent(Object source) {
        super(source);
    }

    public int getOption() {
        return option;
    }

    /**
     * @param option the option to set
     */
    public void setOption(int option) {
        this.option = option;
    }
}

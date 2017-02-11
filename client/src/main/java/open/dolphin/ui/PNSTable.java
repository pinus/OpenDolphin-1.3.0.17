package open.dolphin.ui;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.TableModel;

/**
 * JTable with fixed fill behavior.
 * @author pns
 */
public class PNSTable extends JTable {
    private static final long serialVersionUID = 1L;

    public PNSTable() {
        super();
    }

    public PNSTable(TableModel model) {
        super(model);
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return getParent() instanceof JViewport
                && getPreferredSize().height < getParent().getHeight();
    }
}

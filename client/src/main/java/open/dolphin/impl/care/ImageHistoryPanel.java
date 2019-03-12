package open.dolphin.impl.care;

import open.dolphin.client.ImageEntry;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.dto.ImageSearchSpec;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.SchemaModel;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.ui.MyJScrollPane;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ImageHistoryPanel.
 *
 * @author Kazushi Minagawa
 * @author pns
 */
public class ImageHistoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private String pid;
    private CareMapDocument myParent;
    private ImageTableModel tModel;
    private JTable table;
    private final int columnCount = 5;
    private final int imageWidth = 132;
    private final int imageHeight = 132;

    public ImageHistoryPanel() {
        super(new BorderLayout());
        init();
    }

    private void init() {

        tModel = new ImageTableModel();
        table = new JTable(tModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        Collections.list(table.getColumnModel().getColumns())
                .forEach(column -> column.setPreferredWidth(imageWidth));

        table.setRowHeight(imageHeight + 20);

        ImageRenderer imageRenderer = new ImageRenderer();
        imageRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, imageRenderer);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                Point loc = e.getPoint();
                int row = table.rowAtPoint(loc);
                int col = table.columnAtPoint(loc);
                if (row != -1 && col != -1) {
                    openImage(row, col);
                }
            }
        });

        MyJScrollPane scroller = new MyJScrollPane(table,
                MyJScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, MyJScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scroller, BorderLayout.CENTER);
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String val) {
        pid = val;
    }

    public void setMyParent(CareMapDocument doc) {
        myParent = doc;
    }

    public void setImageList(List<List<ImageEntry>> allImages) {

        if (allImages != null) {

            List<ImageEntry> list = new ArrayList<>();
            allImages.forEach(imageList -> list.addAll(imageList));
            tModel.setImageList(list);

        } else {
            tModel.setImageList(null);
        }
    }

    public void findDate(SimpleDate date) {
        String mmlDate = SimpleDate.simpleDateToMmldate(date);
        int index = tModel.findDate(mmlDate);

        if (index != -1) {
            int row = index / columnCount;
            int col = index % columnCount;
            table.setRowSelectionInterval(row, row);
            table.setColumnSelectionInterval(col, col);
        }
    }

    private void openImage(int row, int col) {

        ImageEntry entry = (ImageEntry) tModel.getValueAt(row, col);
        final ImageSearchSpec spec = new ImageSearchSpec();
        spec.setCode(ImageSearchSpec.ID_SEARCH);
        spec.setId(entry.getId());
        final DocumentDelegater ddl = new DocumentDelegater();

        DBTask<SchemaModel> task = new DBTask<SchemaModel>(myParent.getContext()) {

            @Override
            public SchemaModel doInBackground() throws Exception {
                return ddl.getImage(spec.getId());
            }

            @Override
            public void succeeded(SchemaModel result) {
                openDialog(result);
            }
        };

        task.execute();
    }

    private void openDialog(SchemaModel schema) {

    }

    private class ImageTableModel extends AbstractTableModel {
        private static final long serialVersionUID = -2683619747572366737L;

        private List<ImageEntry> imageList;

        @Override
        public int getColumnCount() {
            return columnCount;
        }

        @Override
        public int getRowCount() {
            if (imageList == null) {
                return 0;
            }

            int size = imageList.size();
            int rowCount = size / columnCount;

            return ((size % columnCount) != 0) ? rowCount + 1 : rowCount;
        }

        @Override
        public Object getValueAt(int row, int col) {
            int index = row * columnCount + col;
            if (!isValidIndex(index)) {
                return null;
            }

            ImageEntry s = imageList.get(index);
            return s;
        }

        public void setImageList(List<ImageEntry> list) {
            if (imageList != null) {
                int last = getRowCount();
                imageList.clear();
                fireTableRowsDeleted(0, last);
            }
            imageList = list;
            int last = getRowCount();
            fireTableRowsInserted(0, last);
        }

        private int findDate(String date) {

            int ret = -1;

            if (imageList == null) {
                return ret;
            }

            int size = imageList.size();
            for (int i = 0; i < size; i++) {
                ImageEntry entry = imageList.get(i);
                if (entry.getConfirmDate().startsWith(date)) {
                    ret = i;
                    break;
                }
            }
            return ret;
        }

        private boolean isValidIndex(int index) {
            return imageList != null && index >= 0 && index < imageList.size();
        }
    }

    private class ImageRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = -8136363583689791913L;

        public ImageRenderer() {
            init();
        }

        private void init() {
            setVerticalTextPosition(JLabel.BOTTOM);
            setHorizontalTextPosition(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean isFocused, int row,
                int col) {
            Component compo = super.getTableCellRendererComponent(table, value,
                    isSelected, isFocused, row, col);
            JLabel l = (JLabel) compo;

            if (value != null) {

                ImageEntry entry = (ImageEntry) value;
                l.setIcon(entry.getImageIcon());
                // String title = entry.getTitle();
                // if (title != null) {
                // l.setText(title);

                // } else {
                l.setText(entry.getConfirmDate().substring(0, 10));
            // }

            } else {
                l.setIcon(null);
            }
            return compo;
        }
    }
}

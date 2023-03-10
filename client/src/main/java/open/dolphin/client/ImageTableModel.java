package open.dolphin.client;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * ImageTableModel
 *
 * @author Minagawa, Kazushi
 */
public class ImageTableModel extends AbstractTableModel {

    private final String[] columnNames;
    private final int columnCount;
    private List<ImageEntry> imageList;

    public ImageTableModel(String[] columnNames, int columnCount) {
        this.columnNames = columnNames;
        this.columnCount = columnCount;
    }

    @Override
    public String getColumnName(int col) {
        return (columnNames != null && col < columnNames.length) ? columnNames[col] : null;
    }

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

        ImageEntry entry = imageList.get(index);
        return entry;
    }

    public List<ImageEntry> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageEntry> list) {
        imageList = list;
        fireTableDataChanged();
    }

    private boolean isValidIndex(int index) {
        return imageList != null && index >= 0 && index < imageList.size();
    }

    public void clear() {
        if (imageList != null) {
            imageList.clear();
            fireTableDataChanged();
        }
    }
}

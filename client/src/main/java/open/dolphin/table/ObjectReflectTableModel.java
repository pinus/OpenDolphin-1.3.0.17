package open.dolphin.table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * ObjectReflectTableModel.
 * Generics 対応 by pns
 * @author Minagawa,Kazushi
 * @param <T>
 */
public class ObjectReflectTableModel<T> extends AbstractTableModel {
    private static final long serialVersionUID = -8280948755982277457L;

    // カラム名配列
    private String[] columnNames;
    // カラムクラス配列
    private Class<?>[] columnClasses;
    // 属性値を取得するためのメソッド名
    private String[] methodNames;
    // カラム数
    private int columnCount;
    // データオブジェクトリスト
    private List<T> objectList;

    /**
     * カラム名から TableModel を生成する
     * @param columnNames
     */
    public ObjectReflectTableModel(String[] columnNames) {
        this(columnNames, 0, null, null);
    }

    /**
     * ObjectReflectTableModelを生成する。
     * @param columnNames カラム名配列
     * @param startNumRows	開始時行数
     * @param methodNames メソッド名配列
     * @param columnClasses カラムクラス配列
     */
    public ObjectReflectTableModel(String[] columnNames, int startNumRows, String[] methodNames, Class<?>[] columnClasses) {
        this.columnNames = columnNames;
        this.methodNames = methodNames;
        this.columnClasses = columnClasses;
        if (columnNames != null) {
            columnCount = columnNames.length;
        }
        objectList = new ArrayList<>();
    }

    /**
     * カラム名を返す。
     * @param index カラムインデックス
     * @return
     */
    @Override
    public String getColumnName(int index) {
        return (columnNames != null && index < columnNames.length)? columnNames[index] : null;
    }

    /**
     * カラム数を返す。
     * @return カラム数
     */
    @Override
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * 行数を返す。
     * @return 行数
     */
    @Override
    public int getRowCount() {
        return (objectList != null) ? objectList.size() : 0;
    }

    /**
     * カラムのクラス型を返す。
     * @param index
     * @return
     */
    @Override
    public Class<?> getColumnClass(int index) {
        return (columnClasses != null && index < columnClasses.length) ? columnClasses[index] : String.class;
    }

    /**
     * オブジェクトの値を返す。
     * @param row 行インデックス
     * @param col 列インデックス
     * @return
     */
    @Override
    public Object getValueAt(int row, int col) {

        T object = getObject(row);

        if (object != null && methodNames != null && col < methodNames.length) {
            try {
                Method targetMethod = object.getClass().getMethod(methodNames[col], (Class[])null);
                return targetMethod.invoke(object, (Object[])null);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                System.out.println("ObjectReflectTableModel.java: " + e);
            }
        }
        return null;
    }

    /**
     * データリストを設定する。
     * @param otherList データリスト
     */
    public void setObjectList(List<T> otherList) {
        if (objectList != null) {
            objectList.clear();
            objectList = null;
        }
        objectList = otherList; // 参照しているのみ
        fireTableDataChanged();
    }

    /**
     * Method 名を帰す
     * @param index
     * @return
     */
    public String getMethodName(int index) {
        return methodNames[index];
    }

    /**
     * コンストラクト後にカラム名を変更する。
     * @param columnName
     * @param col
     */
    public void setColumnName(String columnName, int col) {
        if (col >=0 && col < columnNames.length) {
            columnNames[col] = columnName;
            fireTableStructureChanged();
        }
    }

    /**
     * コンストラクト後にメソッドを変更する。
     * @param methodName
     * @param col
     */
    public void setMethodName(String methodName, int col) {
        if (col >=0 && col < methodNames.length) {
            methodNames[col] = methodName;
            if(objectList != null) {
                fireTableDataChanged();
            }
        }
    }

    /**
     * データリストを返す。
     * @return データリスト
     */
    public List<T> getObjectList() {
        return objectList;
    }

    /**
     * データリストをクリアする。
     */
    public void clear() {
        if (objectList != null) {
            objectList.clear();
            fireTableDataChanged();
        }
    }

    /**
     * 指定された行のオブジェクトを返す。
     * @param index 行インデックス
     * @return オブジェクト
     */
    public T getObject(int index) {
        return (objectList != null && index >= 0 && index < objectList.size()) ? objectList.get(index) : null;
    }

    /**
     * オブジェクト数(=データ数)を返す
     * @return オブジェクト数
     */
    public int getObjectCount() {
        return objectList != null ? objectList.size() : 0;
    }

    // ----------- データ追加削除の簡易サポート ---------------

    public void addRow(T item) {
        if (item != null) {
            if (objectList == null) {
                objectList = new ArrayList<>();
            }
            int index = objectList.size();
            objectList.add(item);
            fireTableRowsInserted(index, index);
        }
    }

    public void addRow(int index, T item) {
        if (item != null && index > -1 && objectList != null) {
            if ( (objectList.isEmpty() && index == 0) || (index < objectList.size()) ){
                objectList.add(index, item);
                fireTableRowsInserted(index, index);
            }
        }
    }

    public void insertRow(int index, T item) {
        addRow(index, item);
    }

    public void moveRow(int from, int to) {
        if (! isValidRow(from) || ! isValidRow(to) || from == to) {
            return;
        }
        T o = objectList.remove(from);
        objectList.add(to, o);
        fireTableRowsUpdated(0, getObjectCount());
    }

    public void addRows(Collection<T> c) {
        if (c != null && !c.isEmpty()) {
            if (objectList == null) {
                objectList = new ArrayList<>();
            }
            int first = objectList.size();
            c.forEach(item -> objectList.add(item));
            fireTableRowsInserted(first, first + c.size() - 1);
        }
    }

    public void deleteRow(int index) {
        if (index > -1 && index < objectList.size()) {
            objectList.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    public void deleteRow(T item) {
        if (objectList != null) {
            if (objectList.remove(item)) {
                fireTableDataChanged();
            }
        }
    }

    public void deleteRows(Collection<T> c) {
        if (objectList != null) {
            if (c != null) {
                objectList.removeAll(c);
                fireTableDataChanged();
            }
        }
    }

    public int getIndex(T item) {
        int index = 0;
        boolean found = false;
        if (objectList != null) {
            for (T obj : objectList) {
                if (obj.equals(item)) {
                    found = true;
                    break;
                } else {
                    index++;
                }
            }
        }
        return found ? index : -1;
    }

    public boolean isValidRow(int row) {
        return ( (objectList != null) && (row > -1) && (row < objectList.size()) );
    }
}

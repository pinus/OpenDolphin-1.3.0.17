package open.dolphin.impl.care;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import open.dolphin.ui.AdditionalTableSettings;
import open.dolphin.client.*;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.table.ObjectReflectTableModel;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import open.dolphin.table.IndentTableCellRenderer;
import open.dolphin.ui.MyJScrollPane;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * オーダ履歴を表示するパネルクラス.  表示するオーダと抽出期間は PropertyChange で通知される.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class OrderHistoryPanel extends JPanel implements PropertyChangeListener {
    private static final long serialVersionUID = -2302784717739085879L;

    private ObjectReflectTableModel<ModuleModel> tModel;
    private JTable table;
    private JLabel contents;
    private String pid;
    private final Dimension contentSize = new Dimension(240, 300);

    public OrderHistoryPanel() {
        super(new BorderLayout(5, 0));
        initComponents();
    }

    private void initComponents() {
        String[] columnNames = { "　実施日", "　内   容" };

        // オーダの履歴(確定日|スタンプ名)を表示する TableModel: 各行は ModuleModel
        tModel = new ObjectReflectTableModel<ModuleModel>(columnNames) {
            private static final long serialVersionUID = 1684645192401100170L;

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
            @Override
            public Object getValueAt(int row, int col) {
                ModuleModel module = getObject(row);

                switch (col) {
                    case 0:
                        return ModelUtils.getDateAsString(module.getConfirmed());
                    case 1:
                        return module.getModuleInfo().getStampName();
                }
                return null;
            }
        };

        table = new JTable(tModel);
        table.setDefaultRenderer(Object.class, new IndentTableCellRenderer());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 行クリックで内容を表示する
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        ListSelectionModel m = table.getSelectionModel();
        m.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() == false) {
                int index = table.getSelectedRow();
                displayOrder(index);
            }
        });
        setColumnWidth(new int[] { 50, 240 });

        MyJScrollPane scroller = new MyJScrollPane(table, MyJScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, MyJScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroller, BorderLayout.CENTER);

        // 内容表示用 TextArea
        contents = new JLabel();
        contents.setBackground(Color.white);
        MyJScrollPane cs = new MyJScrollPane(contents, MyJScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, MyJScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cs.setPreferredSize(contentSize);
        cs.setMaximumSize(contentSize);
        add(cs, BorderLayout.EAST);
        AdditionalTableSettings.setTable(table);
    }

    public void setColumnWidth(int[] columnWidth) {
        int len = columnWidth.length;
        for (int i = 0; i < len; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidth[i]);
        }
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String val) {
        pid = val;
    }

    /**
     * allModules から ModuleModel を全部抽出して tModel に加える.
     * allModules は List＜List＜ModuleModel＞＞
     * 抽出期間の数だけ List＜ModuleModel＞が List になっている
     * @param allModules
     */
    public void setModuleList(List<List<ModuleModel>> allModules) {
        tModel.clear();
        List<ModuleModel> moduleList = new ArrayList<>();
        allModules.forEach(list -> list.forEach(model -> moduleList.add(model)));
        tModel.setObjectList(moduleList);
    }

    /**
     * カレンダーの日が選択されたときに通知を受け，テーブルで日付が一致するオーダの行を選択する.
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (prop.equals(CareMapDocument.SELECTED_DATE_PROP)) {
            String date = (String) e.getNewValue();
            findDate(date);
        }
    }

    /**
     * オーダ履歴のテーブル行がクリックされたとき，データモデルの ModuleModel を表示する.
     */
    private void displayOrder(int index) {
        contents.setText("");

        ModuleModel stamp = tModel.getObject(index);
        if (stamp == null) { return; }

        try {
            IInfoModel model = stamp.getModel();

            VelocityContext context = ClientContext.getVelocityContext();
            context.put("model", model);
            context.put("stampName", stamp.getModuleInfo().getStampName());

            // このスタンプのテンプレートファイルを得る
            String templateFile = stamp.getModel().getClass().getName() + ".vm";
            // debug(templateFile);

            // Merge する
            StringWriter sw = new StringWriter();
            BufferedReader reader;
            try (BufferedWriter bw = new BufferedWriter(sw)) {
                InputStream instream = ClientContext.getTemplateAsStream(templateFile);
                reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                Velocity.evaluate(context, bw, "stmpHolder", reader);
                bw.flush();
            }
            reader.close();
            contents.setText(sw.toString());

        } catch (IOException | ParseErrorException | MethodInvocationException | ResourceNotFoundException e) {
            System.out.println("OrderHistoryPanel: Execption while setting the stamp text: " + e.toString());
        }
    }

    /**
     * date の行を選択する.
     * 日付は column 0 に String として入っている
     * @param date
     */
    private void findDate(String date) {
        int size = tModel.getObjectCount();
        for (int row = 0; row < size; row++) {
            String rowDate = (String) tModel.getValueAt(row, 0);
            if (rowDate.equals(date)) {
                table.setRowSelectionInterval(row, row);
                break;
            }
        }
    }
}

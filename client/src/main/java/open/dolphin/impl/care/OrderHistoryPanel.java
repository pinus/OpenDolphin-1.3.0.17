package open.dolphin.impl.care;

import open.dolphin.calendar.CalendarEvent;
import open.dolphin.client.ClientContext;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.ui.IndentTableCellRenderer;
import open.dolphin.ui.ObjectReflectTableModel;
import open.dolphin.ui.PNSScrollPane;
import open.dolphin.util.ModelUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * オーダ履歴を表示するパネルクラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public final class OrderHistoryPanel extends JPanel {
    private static final long serialVersionUID = -2302784717739085879L;
    private final Dimension contentSize = new Dimension(240, 300);
    private ObjectReflectTableModel<ModuleModel> tModel;
    private JTable table;
    private JLabel contents;
    private String pid;

    public OrderHistoryPanel() {
        super(new BorderLayout(5, 0));
        initComponents();
    }

    private void initComponents() {
        String[] columnNames = {"　実施日", "　内   容"};

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
        table.putClientProperty("Quaqua.Table.style", "striped");
        table.setDefaultRenderer(Object.class, new IndentTableCellRenderer());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 行クリックで内容を表示する
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        ListSelectionModel m = table.getSelectionModel();
        m.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = table.getSelectedRow();
                displayOrder(index);
            }
        });
        setColumnWidth(new int[]{50, 240});

        PNSScrollPane scroller = new PNSScrollPane(table, PNSScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PNSScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroller, BorderLayout.CENTER);

        // 内容表示用 TextArea
        contents = new JLabel();
        contents.setOpaque(true);
        contents.setBackground(Color.white);
        PNSScrollPane cs = new PNSScrollPane(contents, PNSScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PNSScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cs.setPreferredSize(contentSize);
        cs.setMaximumSize(contentSize);
        add(cs, BorderLayout.EAST);
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
     *
     * @param allModules
     */
    public void setModuleList(List<List<ModuleModel>> allModules) {
        tModel.clear();
        List<ModuleModel> moduleList = new ArrayList<>();
        //allModules.forEach(list -> list.forEach(model -> moduleList.add(model)));
        allModules.forEach(moduleList::addAll);
        tModel.setObjectList(moduleList);
    }

    /**
     * オーダ履歴のテーブル行がクリックされたとき，データモデルの ModuleModel を表示する.
     */
    private void displayOrder(int index) {
        contents.setText("");

        ModuleModel stamp = tModel.getObject(index);
        if (stamp == null) {
            return;
        }

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
                reader = new BufferedReader(new InputStreamReader(instream, StandardCharsets.UTF_8));
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
     * SimpleDate 型式の date の行を選択する.
     * 日付は column 0 に String として入っている
     *
     * @param date
     */
    public void findDate(SimpleDate date) {
        if (CalendarEvent.isModule(date.getEventCode())) {
            String mmlDate = SimpleDate.simpleDateToMmldate(date);

            for (int row = 0; row < tModel.getObjectCount(); row++) {
                String rowDate = (String) tModel.getValueAt(row, 0);
                if (rowDate.equals(mmlDate)) {
                    table.setRowSelectionInterval(row, row);
                    break;
                }
            }
        }
    }
}

package open.dolphin.order.tablepanel;

import open.dolphin.delegater.RadiologyDelegater;
import open.dolphin.infomodel.RadiologyMethodValue;
import open.dolphin.order.IStampEditor;
import open.dolphin.ui.PNSBorderFactory;
import open.dolphin.ui.PNSScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ItemTablePanel を extend して作った RadiologyTablePanel.
 *
 * @author pns
 */
public class RadiologyTablePanel extends ItemTablePanel {
    
    public RadiologyTablePanel(IStampEditor parent) {
        super(parent);
    }

    @Override
    public JComponent createCenterPanel() {
        JTable table = getTable();
        table.putClientProperty("Quaqua.Table.style", "striped");

        // スクローラ
        JScrollPane scroller = new JScrollPane(table);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // 放射線メソッドパネル
        RadiologyMethodPanel method = new RadiologyMethodPanel();

        JPanel center = new JPanel(new BorderLayout());
        center.add(method, BorderLayout.WEST);
        center.add(scroller, BorderLayout.CENTER);

        return center;
    }

    /**
     * RadiologyMedthodPanel.
     * テーブルの左側にくっつくパネル
     */
    private final class RadiologyMethodPanel extends JPanel {
        
        private static final int METHOD_CELL_WIDTH = 120;
        private static final int COMMENT_CELL_WIDTH = 140;

        private JList<RadiologyMethodValue> methodList;
        private JList<RadiologyMethodValue> commentList;

        public RadiologyMethodPanel() {
            initComponents();
        }

        private void initComponents() {

            RadiologyDelegater mdl = new RadiologyDelegater();

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            // Method panel
            JPanel p1 = new JPanel(new BorderLayout());
            methodList = new JList<>(mdl.getRadiologyMethod().toArray(new RadiologyMethodValue[0]));
            methodList.setFixedCellWidth(METHOD_CELL_WIDTH);
            methodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            methodList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    RadiologyMethodValue entry = methodList.getSelectedValue();
                    if (entry == null) {
                        return;
                    }
                    fetchComments(entry.getHierarchyCode1());
                }
            });

            PNSScrollPane scroller = new PNSScrollPane(methodList,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroller.isPermanentScrollBar = true;
            p1.add(scroller);
            p1.setBorder(PNSBorderFactory.createTitledBorder("撮影方法"));

            // Commet panel
            JPanel p2 = new JPanel(new BorderLayout());
            commentList = new JList<>();
            commentList.setFixedCellWidth(COMMENT_CELL_WIDTH);
            commentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            commentList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    RadiologyMethodValue entry = commentList.getSelectedValue();
                    if (entry == null) {
                        return;
                    }
                    notifyComment(entry.getMethodName());
                }
            });
            scroller = new PNSScrollPane(commentList,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroller.isPermanentScrollBar = true;
            p2.add(scroller);
            p2.setBorder(PNSBorderFactory.createTitledBorder("撮影コメント"));

            // Add p1 and p2
            add(p1);
            add(p2);
        }

        private void notifyComment(String cm) {
            setComment(cm);
        }

        private void fetchComments(String h1) {
            RadiologyDelegater mdl = new RadiologyDelegater();
            List<RadiologyMethodValue> method = mdl.getRadiologyComments(h1);
            commentList.setListData(method.toArray(new RadiologyMethodValue[0]));
        }
    }
}


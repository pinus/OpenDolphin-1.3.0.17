package open.dolphin.order.tablepanel;

import java.awt.BorderLayout;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import open.dolphin.delegater.RadiologyDelegater;
import open.dolphin.infomodel.RadiologyMethodValue;
import open.dolphin.order.IStampEditor;
import open.dolphin.ui.AdditionalTableSettings;

/**
 * ItemTablePanel を extend して作った RadiologyTablePanel
 * @author pns
 */
public class RadiologyTablePanel extends ItemTablePanel {
    private static final long serialVersionUID = 1L;

    public RadiologyTablePanel(IStampEditor parent) {
        super(parent);
    }

    @Override
    public JComponent createCenterPanel() {
        JTable table = getTable();

        // スクローラ
        JScrollPane scroller = new JScrollPane(table);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        AdditionalTableSettings.setOrderTable(table);

        // 放射線メソッドパネル
        RadiologyMethodPanel method = new RadiologyMethodPanel();

        JPanel center = new JPanel(new BorderLayout());
        center.add(method, BorderLayout.WEST);
        center.add(scroller, BorderLayout.CENTER);

        return center;
    }

    /**
     * RadiologyMedthodPanel
     * テーブルの左側にくっつくパネル
     */
    private final class RadiologyMethodPanel extends JPanel {
        private static final long serialVersionUID = 7002106454090449477L;

        public static final String RADIOLOGY_MEYTHOD_PROP = "radiologyProp";
        private static final int METHOD_CELL_WIDTH   = 120;
        private static final int COMMENT_CELL_WIDTH    = 140;

        private final JList methodList;
        private JList commentList;
        private List v2;
        private PropertyChangeSupport boundSupport;

        /**
        * Creates new AdminPanel
        */
        public RadiologyMethodPanel() {

            boundSupport = new PropertyChangeSupport(this);
            RadiologyDelegater mdl = new RadiologyDelegater();

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            // Method panel
            JPanel p1 = new JPanel(new BorderLayout());
            Object[] methods = mdl.getRadiologyMethod().toArray();
            methodList = new JList(methods);
            methodList.setFixedCellWidth(METHOD_CELL_WIDTH);
            methodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            methodList.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {

                    if (e.getValueIsAdjusting() == false) {
                            RadiologyMethodValue entry = (RadiologyMethodValue)methodList.getSelectedValue();
                        if (entry == null) {
                            return;
                        }
                        fetchComments(entry.getHierarchyCode1());
                    }
                }
            });

            JScrollPane scroller = new JScrollPane(methodList,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            p1.add(scroller);
            p1.setBorder(BorderFactory.createTitledBorder("撮影方法"));

            // Commet panel
            JPanel p2 = new JPanel(new BorderLayout());
            commentList = new JList();
            commentList.setFixedCellWidth(COMMENT_CELL_WIDTH);
            commentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            commentList.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {

                    if (e.getValueIsAdjusting() == false) {

                            RadiologyMethodValue entry = (RadiologyMethodValue)commentList.getSelectedValue();
                        if (entry == null) {
                            return;
                        }
                        notifyComment(entry.getMethodName());
                    }
                }
            });
            scroller = new JScrollPane(commentList,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            p2.add(scroller);
            p2.setBorder(BorderFactory.createTitledBorder("撮影コメント"));

            // Add p1 and p2
            add(p1);
            add(p2);
        }

        private void notifyComment(String cm) {
            setComment(cm);
        }

        private void fetchComments(String h1) {

            if (v2 != null) {
                v2.clear();
            }
            RadiologyDelegater mdl = new RadiologyDelegater();
            v2 = mdl.getRadiologyComments(h1);
            commentList.setListData(v2.toArray());
        }
    }
}


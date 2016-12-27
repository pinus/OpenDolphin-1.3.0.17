package open.dolphin.order.stampeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import open.dolphin.helper.TextComponentUndoManager;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.TextStampModel;
import open.dolphin.order.ClaimConst;
import open.dolphin.order.IStampEditor;
import open.dolphin.ui.HorizontalPanel;
import open.dolphin.ui.MyJScrollPane;

/**
 * TextStampEditor.
 * @author  pns
 */
public final class TextStampEditor extends JPanel implements IStampEditor<ModuleModel> {
    private static final long serialVersionUID = 1L;

    public static final String VALID_DATA_PROP = "validData";

    private final PropertyChangeSupport boundSupport;
    private String title;
    private JTextPane textPane;
    private JTextField titleField;
    private boolean isValidModel = false;
    private String entity;
    // ItemTablePanel.java の stampNameField.setBackground と同じ色
    private static final Color STAMP_NAME_FIELD_BACKGROUND = new Color(251,239,128);

    private TextComponentUndoManager paneUndoManager;
    private TextComponentUndoManager fieldUndoManager;

    public TextStampEditor() {
        entity = IInfoModel.ENTITY_TEXT;
        boundSupport = new PropertyChangeSupport(this);
    }

    /**
     * エディタを開始する.
     */
    @Override
    public void start() {
        setTitle(ClaimConst.EntityNameMap.get(entity));

        textPane = new JTextPane();
        textPane.setMargin(new Insets(7,7,7,7));
        textPane.getDocument().addDocumentListener(new StateListener());
        titleField = new JTextField();
        titleField.getDocument().addDocumentListener(new StateListener());
        titleField.setBackground(STAMP_NAME_FIELD_BACKGROUND);
        titleField.setOpaque(false);

        // Undo
        paneUndoManager = TextComponentUndoManager.getManager(textPane);
        fieldUndoManager = TextComponentUndoManager.getManager(titleField);

        HorizontalPanel titlePanel = new HorizontalPanel();

        JLabel label = new JLabel("スタンプ名：");

        titlePanel.add(label);
        titlePanel.add(titleField);

        MyJScrollPane scroller = new MyJScrollPane(textPane);
        scroller.setVerticalScrollBarPolicy(MyJScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(MyJScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.setLayout(new BorderLayout(0, 0));
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createTitledBorder(""));
    }

    private class StateListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) { checkState(); }
        @Override
        public void removeUpdate(DocumentEvent e) { checkState(); }
        @Override
        public void changedUpdate(DocumentEvent e) { checkState(); }

        private void checkState() {
            String title = titleField.getText();
            String text = textPane.getText();
            setValidModel(title != null && !title.equals("") && text != null && !text.equals(""));
        }
    }

    /**
     * フォーカスを取る
     */
    @Override
    public void enter() {
        EventQueue.invokeLater(textPane::requestFocusInWindow);
    }

    /**
     * このエディタではとくに dispose すべきリソースはない
     */
    @Override
    public void dispose() {
    }

    @Override
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }

    @Override
    public boolean isValidModel() {
        return isValidModel;
    }

    @Override
    public void setValidModel(boolean b) {
        boolean old = isValidModel;
        isValidModel = b;
        if (old != isValidModel) {
            boundSupport.firePropertyChange(VALID_DATA_PROP, old, isValidModel);
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.addPropertyChangeListener(prop, l);
    }

    @Override
    public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.removePropertyChangeListener(prop, l);
    }

    /**
     * 編集したテキストを返す
     * @return ModuleModel
     */
    @Override
    public ModuleModel getValue() {
        ModuleModel model = new ModuleModel();
        TextStampModel stamp = new TextStampModel();
        ModuleInfoBean info = new ModuleInfoBean();

        info.setStampName(titleField.getText().trim());
        info.setEntity(entity);
        info.setStampRole(IInfoModel.ROLE_TEXT);
        stamp.setText(textPane.getText());

        model.setModel(stamp);
        model.setModuleInfo(info);

        return model;
    }

    /**
     * 編集するテキストを設定する
     * @param val ModuleModel
     */
    @Override
    public void setValue(ModuleModel val) {
        // set text
        ModuleModel model = val;
        TextStampModel stamp = (TextStampModel) model.getModel();
        textPane.setText(stamp.getText());
        titleField.setText(model.getModuleInfo().getStampName());

        textPane.requestFocusInWindow();

        // reset undo
        paneUndoManager.discardAllEdits();
        fieldUndoManager.discardAllEdits();
    }
}

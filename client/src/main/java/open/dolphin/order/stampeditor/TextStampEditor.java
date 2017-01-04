package open.dolphin.order.stampeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.*;
import open.dolphin.event.ProxyDocumentListener;
import open.dolphin.event.ValidListener;
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

    private String title; // Dialog に表示されるタイトル

    private ValidListener validListener;
    private JTextPane textPane;
    private JTextField headerField;
    private boolean isValidModel = false;
    private String entity;
    // ItemTablePanel.java の stampNameField.setBackground と同じ色
    private static final Color STAMP_NAME_FIELD_BACKGROUND = new Color(251,239,128);

    private TextComponentUndoManager paneUndoManager;
    private TextComponentUndoManager fieldUndoManager;

    public TextStampEditor() {
        entity = IInfoModel.ENTITY_TEXT;
    }

    /**
     * エディタを開始する.
     */
    @Override
    public void start() {
        setTitle(ClaimConst.EntityNameMap.get(entity));

        textPane = new JTextPane();
        textPane.setMargin(new Insets(7,7,7,7));
        textPane.getDocument().addDocumentListener((ProxyDocumentListener) e -> checkState());
        headerField = new JTextField();
        headerField.getDocument().addDocumentListener((ProxyDocumentListener) e -> checkState());
        headerField.setBackground(STAMP_NAME_FIELD_BACKGROUND);
        headerField.setOpaque(true);

        // Undo
        paneUndoManager = TextComponentUndoManager.getManager(textPane);
        fieldUndoManager = TextComponentUndoManager.getManager(headerField);

        HorizontalPanel headerPanel = new HorizontalPanel();

        JLabel label = new JLabel("スタンプ名：");

        headerPanel.add(label);
        headerPanel.add(headerField);

        MyJScrollPane scroller = new MyJScrollPane(textPane);
        scroller.setVerticalScrollBarPolicy(MyJScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(MyJScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.setLayout(new BorderLayout(0, 0));
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    private void checkState() {
        String header = headerField.getText();
        String text = textPane.getText();
        setValid(header != null && !header.equals("") && text != null && !text.equals(""));
    }

    /**
     * フォーカスを取る
     */
    @Override
    public void enter() {
        SwingUtilities.invokeLater(textPane::requestFocusInWindow);
    }

    /**
     * このエディタではとくに dispose すべきリソースはない.
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
    public void setValid(boolean valid) {
        boolean old = isValidModel;
        isValidModel = valid;
        if (old != isValidModel) {
            validListener.validity(valid);
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
    public void addValidListener(ValidListener listener) {
        validListener = listener;
    }

    /**
     * 編集したテキストを返す.
     * @return ModuleModel
     */
    @Override
    public ModuleModel getValue() {
        ModuleModel model = new ModuleModel();
        TextStampModel stamp = new TextStampModel();
        ModuleInfoBean info = new ModuleInfoBean();

        info.setStampName(headerField.getText().trim());
        info.setEntity(entity);
        info.setStampRole(IInfoModel.ROLE_TEXT);
        stamp.setText(textPane.getText());

        model.setModel(stamp);
        model.setModuleInfo(info);

        return model;
    }

    /**
     * 編集するテキストを設定する.
     * @param val ModuleModel
     */
    @Override
    public void setValue(ModuleModel val) {
        // set text
        ModuleModel model = val;
        TextStampModel stamp = (TextStampModel) model.getModel();
        textPane.setText(stamp.getText());
        headerField.setText(model.getModuleInfo().getStampName());

        textPane.requestFocusInWindow();

        // reset undo
        paneUndoManager.discardAllEdits();
        fieldUndoManager.discardAllEdits();
    }
}

package open.dolphin.ui;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;
import open.dolphin.helper.TextComponentUndoManager;

/**
 * Mac っぽいボーダーの CellEditor.
 * undo 対応.
 * @author pns
 */
public class PNSCellEditor extends DefaultCellEditor {
    private static final long serialVersionUID = 1L;

    // JTextField の UndoManager
    private TextComponentUndoManager undoManager;

    /**
     * Constructs a DefaultCellEditor that uses a text field.
     *
     * @param textField  a JTextField object
     */
    public PNSCellEditor(JTextField textField) {
        super(textField);

        textField.setBorder(PNSBorderFactory.createTextFieldBorder());

        // selectAll on FocusGain
        textField.addFocusListener(new TextFieldFocusListener());

        // UndoManager 登録
        undoManager = TextComponentUndoManager.getManager(textField);
    }

    /**
     * Constructs a DefaultCellEditor object that uses
     * a check box.
     *
     * @param checkBox  a JCheckBox object
     */
    public PNSCellEditor(JCheckBox checkBox) {
        super(checkBox);
        //cellEditor = this;
        checkBox.setBorder(new LineBorder(Color.gray));
    }

    /**
     * Constructs a DefaultCellEditor object that uses a
     * combo box.
     *
     * @param comboBox  a JComboBox object
     */
    public PNSCellEditor(JComboBox<?> comboBox) {
        super(comboBox);
        //cellEditor = this;
        comboBox.setBorder(new LineBorder(Color.gray));
    }

    /**
     * selectAll in the JTextField on focusGained.
     */
    private class TextFieldFocusListener extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent e) {
            ((JTextComponent) e.getSource()).selectAll();
            // restart undoing
            undoManager.discardAllEdits();
        }

//      フォーカスとったまま，他のウインドウをクリックしたとき cell editor が残るのを何とかしようと思ったが
//      これだと，同じ column の他の row をクリックしたとき，フォーカスが取れない
//      public void focusLost(FocusEvent e) {
//          cellEditor.cancelCellEditing();
//      }
    }
}

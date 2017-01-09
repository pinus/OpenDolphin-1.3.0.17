package open.dolphin.ui;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 */
public class MyDefaultCellEditor extends DefaultCellEditor {
    private static final long serialVersionUID = 1L;
    // private DefaultCellEditor cellEditor;

    /**
     * Constructs a DefaultCellEditor that uses a text field.
     *
     * @param textField  a JTextField object
     */
    public MyDefaultCellEditor(JTextField textField) {
        super(textField);
        //cellEditor = this;

        textField.setBorder(PNSBorderFactory.createTextFieldBorder());

        // selectAll on FocusGain
        textField.addFocusListener(new TextFieldFocusListener());
    }

    /**
     * Constructs a DefaultCellEditor object that uses
     * a check box.
     *
     * @param checkBox  a JCheckBox object
     */
    public MyDefaultCellEditor(JCheckBox checkBox) {
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
    public MyDefaultCellEditor(JComboBox comboBox) {
        super(comboBox);
        //cellEditor = this;
        comboBox.setBorder(new LineBorder(Color.gray));
    }

    /**
     * selectAll in the JTextField on focusGained
     */
    class TextFieldFocusListener extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent e) {
            ((JTextField) e.getSource()).selectAll();
        }

//      フォーカスとったまま，他のウインドウをクリックしたとき cell editor が残るのを何とかしようと思ったが
//      これだと，同じ column の他の row をクリックしたとき，フォーカスが取れない
//      public void focusLost(FocusEvent e) {
//          cellEditor.cancelCellEditing();
//      }
    }
}

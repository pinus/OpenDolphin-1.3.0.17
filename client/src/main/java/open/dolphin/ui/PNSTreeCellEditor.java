package open.dolphin.ui;

import java.awt.Font;
import javax.swing.DefaultCellEditor;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import open.dolphin.helper.TextComponentUndoManager;

/**
 * Mac っぽい Border の TreeCellEditor.
 * undo 対応.
 * @author pns
 */
public final class PNSTreeCellEditor extends DefaultTreeCellEditor {

    // 編集が始まるまでの delay
    private static final int DELAY = 900; //in msec (default = 1200)
    // UndoManager
    private TextComponentUndoManager manager;

    public PNSTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
        realEditor = this.createTreeCellEditor();
    }

    @Override
    protected TreeCellEditor createTreeCellEditor() {
        final DefaultTextField textField = new DefaultTextField(PNSBorderFactory.createTextFieldBorder()) {
            private static final long serialVersionUID = 1L;
            @Override
            public void setText(String text) {
                super.setText(text);
                // start undoing
                manager.discardAllEdits();
            }
        };

        manager = TextComponentUndoManager.getManager(textField);

        textField.putClientProperty("Quaqua.TextComponent.showPopup", false); // 勝手に cut,copy,past の popup を作らせない
        textField.setOpaque(true); // これをしないと，編集時のバックグランドが白くならない
        textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        textField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                // editor の cell を５文字分大きめに作る
                textField.setColumns(e.getDocument().getLength() + 5);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        DefaultCellEditor editor = new DefaultCellEditor(textField);

	// One click to edit.
	editor.setClickCountToStart(1);
	return editor;
    }

    /**
     * 編集が始まるまでの delay を設定
     */
    @Override
    protected void startEditingTimer() {
	if(timer == null) {
	    timer = new Timer(DELAY, this);
	    timer.setRepeats(false);
	}
	timer.start();
    }
}

package open.dolphin.order;

import java.awt.CardLayout;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.JPanel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.order.stampeditor.StampEditor;
import open.dolphin.order.stampeditor.TextStampEditor;

/**
 * 全ての StampEditor を保持可能なパネル
 * valid data かどうかを確認するリスナを登録必要
 * @author pns
 */
public class StampEditorProxyPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout;
    // entity のエディタをロードするローダー
    private final StampEditorLoader loader;
    // 現在選択されているエディタ
    private IStampEditor curEditor;

    public StampEditorProxyPanel(PropertyChangeListener listener) {
        loader = new StampEditorLoader(listener, this);
        initComponents();
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
    }

    /**
     * editor の enter 処理を行う
     */
    public void enter() {
        curEditor.enter();
    }

    /**
     * entity のエディタを表示する
     * @param entity
     */
    public void show(String entity) {
        // load した際に editorCardPanel にも追加される
        curEditor = loader.load(entity);
        cardLayout.show(this, entity);
    }

    /**
     * リソースの解放
     */
    public void close() {
        loader.disposeAll();
    }

    /**
     * エディタにセットされているデータを取り出す
     * @return
     */
    public Object getValue() {
        return curEditor.getValue();
    }

    /**
     * エディタにデータをセットする
     * @param value
     */
    public void setValue(Object value) {
        curEditor.setValue(value);
    }

    /**
     * StampEditor をロードするための loader
     */
    private class StampEditorLoader {
        // load した editor を保持する HashMap
        private final HashMap<String, IStampEditor> editorMap = new HashMap<>(14);
        // load した editor につける PropertyChangeListener
        private final PropertyChangeListener listener;
        // load した editor を格納する panel (CardLayout)
        private final JPanel panel;

        public StampEditorLoader(PropertyChangeListener listener, JPanel panel) {
            this.listener = listener;
            this.panel = panel;
        }

        /**
         * entity に対応する StampEditor を作成する
         * @param entity
         * @return
         */
        public IStampEditor load(String entity) {
            IStampEditor editor = editorMap.get(entity);

            if (editor == null) {
                if (IInfoModel.ENTITY_TEXT.equals(entity)) {
                    editor = new TextStampEditor();
                } else {
                    editor = new StampEditor(entity);
                }
                editor.addPropertyChangeListener(StampEditorDialog.VALID_DATA_PROP, listener);
                editor.start();
                editor.setEntity(entity);
                editorMap.put(entity, editor);
                panel.add(entity, (JPanel) editor);
            }
            editor.enter();
            return editor;
        }

        /**
         * 作成された全ての StampEditor を dispose する
         */
        public void disposeAll() {
            for (String key : editorMap.keySet()) {
                IStampEditor e = editorMap.get(key);
                if (e != null ) {
                    e.dispose();
                    e.removePropertyChangeListener(StampEditorDialog.VALID_DATA_PROP, listener);
                }
            }
        }
    }
}

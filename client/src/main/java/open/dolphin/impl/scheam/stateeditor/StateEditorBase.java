package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import open.dolphin.impl.scheam.StateEditor;

/**
 * StateEditor の Adapter abstract.
 * @author pns
 */
public abstract class StateEditorBase implements StateEditor {
    /**
     * State が自分に切り替わった時に呼ばれる.
     */
    @Override
    public void start() {}

    /**
     * State が別の State に切り替わった時に呼ばれる.
     * Escape が押されたときにも呼ばれる.
     */
    @Override
    public void end() {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}

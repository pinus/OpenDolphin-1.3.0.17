package open.dolphin.impl.scheam;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * 各種 State でマウスの動きに応じて図形を編集する Editor.
 * できあがった結果を getHolder() で取得する.
 *
 * @author pns
 */
public interface StateEditor {

    public void start();

    public void end();

    public void mouseDown(MouseEvent e);

    public void mouseDragged(MouseEvent e);

    public void mouseUp(MouseEvent e);

    public void mouseMoved(MouseEvent e);

    public void keyPressed(KeyEvent e);

    public void keyReleased(KeyEvent e);

    public ShapeHolder getHolder();
}

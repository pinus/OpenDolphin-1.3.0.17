package open.dolphin.impl.scheam.iconcallback;

import javafx.scene.Node;
import javafx.scene.shape.Shape;
import open.dolphin.impl.scheam.FillMode;
import open.dolphin.impl.scheam.helper.ShapeIcon;
import open.dolphin.impl.scheam.widget.PnsIconCallback;

/**
 *
 * @author pns
 */
public class IconCallbackTranslate implements PnsIconCallback<FillMode, Node> {
    private final Shape icon, selectedIcon;

    public IconCallbackTranslate() {
        icon = ShapeIcon.getTranslatePointer();
        selectedIcon = ShapeIcon.getTranslatePointer();
    }
    @Override
    public Node call(FillMode item) {
        return icon;
    }
    @Override
    public Node callSelected(FillMode item) {
        return selectedIcon;
    }
}

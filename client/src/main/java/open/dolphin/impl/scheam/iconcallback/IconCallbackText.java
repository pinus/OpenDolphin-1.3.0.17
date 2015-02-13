package open.dolphin.impl.scheam.iconcallback;

import javafx.scene.Node;
import javafx.scene.shape.Shape;
import open.dolphin.impl.scheam.constant.Const;
import open.dolphin.impl.scheam.FillMode;
import open.dolphin.impl.scheam.helper.ShapeIcon;
import open.dolphin.impl.scheam.widget.PnsIconCallback;

/**
 *
 * @author pns
 */
public class IconCallbackText implements PnsIconCallback<FillMode, Node>{
    private final Shape icon, selectedIcon;

    public IconCallbackText() {
        icon = ShapeIcon.getText();
        selectedIcon = ShapeIcon.getText();
        selectedIcon.setFill(Const.PNS_WHITE);
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

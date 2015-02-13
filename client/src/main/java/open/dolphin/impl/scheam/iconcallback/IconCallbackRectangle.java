package open.dolphin.impl.scheam.iconcallback;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import open.dolphin.impl.scheam.constant.Const;
import open.dolphin.impl.scheam.FillMode;
import open.dolphin.impl.scheam.helper.ShapeIcon;
import open.dolphin.impl.scheam.widget.PnsIconCallback;

/**
 *
 * @author pns
 */
public class IconCallbackRectangle implements PnsIconCallback<FillMode, Node> {
    private final Shape line, fill, mix, sline, sfill, smix;

    public IconCallbackRectangle() {
        double w = 12, h = 12;
        line = ShapeIcon.getRectangle(w, h, Const.PNS_BLACK, Color.TRANSPARENT);
        fill = ShapeIcon.getRectangle(w, h, null, Const.PNS_GLAY);
        mix = ShapeIcon.getRectangle(w, h, Const.PNS_BLACK, Const.PNS_GLAY);
        sline = ShapeIcon.getRectangle(w, h, Const.PNS_WHITE, Color.TRANSPARENT);
        sfill = ShapeIcon.getRectangle(w, h, null, Const.PNS_LIGHT_GLAY);
        smix = ShapeIcon.getRectangle(w, h, Const.PNS_WHITE, Const.PNS_LIGHT_GLAY);
    }
    @Override
    public Node call(FillMode item) {
        if (FillMode.Line.equals(item)) { return line; }
        if (FillMode.Fill.equals(item)) { return fill; }
        return mix;
    }
    @Override
    public Node callSelected(FillMode item) {
        if (FillMode.Line.equals(item)) { return sline; }
        else if (FillMode.Fill.equals(item)) { return sfill; }
        return smix;
    }
}

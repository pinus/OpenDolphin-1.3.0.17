package open.dolphin.impl.scheam.shapeholder;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaEditorProperties;

/**
 * Text データを保持する ShapeHolder.
 * Text String を Image に変換して保持する.
 * @author pns
 */
public class TextHolder extends ImageHolder {
    private String string;

    /**
     * 文字列から Text ノードを作り ImageHolder にセットする.
     * @param s
     */
    public void setText(String s) {
        string = s;
        SchemaEditorProperties properties = SchemaEditorImpl.getProperties();
        Font font= properties.getFont();
        Text text = new Text(s);
        text.setFont(font);
        text.setStroke(Color.TRANSPARENT);
        text.setFill(properties.getFillColor());

        setNode(text);
    }

    public String getText() { return string; }
}

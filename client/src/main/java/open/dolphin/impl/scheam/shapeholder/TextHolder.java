package open.dolphin.impl.scheam.shapeholder;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextBuilder;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaEditorProperties;

/**
 * Text データを保持する ShapeHolder
 * Text String を Image に変換して保持する
 * @author pns
 */
public class TextHolder extends ImageHolder {
    private String string;

    /**
     * 文字列から Text ノードを作り ImageHolder にセットする
     * @param s
     */
    public void setText(String s) {
        string = s;
        SchemaEditorProperties properties = SchemaEditorImpl.getProperties();
        Font font= properties.getFont();
        Node text = TextBuilder.create().text(s).
                font(font).stroke(Color.TRANSPARENT).fill(properties.getLineColor()).build();

        setNode(text);
    }

    public String getText() { return string; }
}

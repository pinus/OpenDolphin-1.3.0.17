package open.dolphin.impl.scheam.widget;

import java.util.HashMap;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * CardLayout 的なもの.
 * @author pns
 */
public class PnsCardPane extends StackPane {
    private final HashMap<String, Integer> nodeMap = new HashMap<>();
    private int index = -1;

    /**
     * カードを加えるときは必ずここで加える.
     * @param node
     * @param name
     */
    public void addCard(Node node, String name) {
        getChildren().add(node);
        last(); // ここで index がセットされる
        nodeMap.put(name, index);
        // デフォルト Position
        StackPane.setAlignment(node, Pos.CENTER);
    }

    /**
     * index 番目のカードを表示する.
     * @param i
     */
    public void show(int i) {
        if (i >= 0 && i < getChildren().size()) {
            index = i;
            getChildren().forEach(node -> node.setVisible(false));
            Node target = getChildren().get(i);
            target.setVisible(true);
            target.requestFocus();
        }
    }

    /**
     * name という名前で登録したカードを表示する.
     * @param name
     */
    public void show(String name) { show(nodeMap.get(name)); }

    public void first() { show(0); }
    public void last() { show(getChildren().size()-1); }
    public void next() { show(index + 1); }
    public void previous() {show(index -1 ); }

    /**
     * 全てのカードを一度に alingment する.
     * @param pos
     */
    public void alignAll(Pos pos) {
        getChildren().forEach(node -> StackPane.setAlignment(node, pos));
    }
}

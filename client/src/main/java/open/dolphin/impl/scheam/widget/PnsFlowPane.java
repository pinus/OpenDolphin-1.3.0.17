package open.dolphin.impl.scheam.widget;

import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Justification する FlowPane.
 * Orientation = HORIZONTAL 専用.
 * １行におさまる場合は通常の FlowPane の Centering したものを表示.
 * 複数行になるときは，空白が出ないように全体に広げる.
 * @author pns
 */
public class PnsFlowPane extends FlowPane {

    public PnsFlowPane() {
        getStyleClass().add("pns-flow-pane");
        setAlignment(Pos.CENTER);
        setOrientation(Orientation.HORIZONTAL);
    }

    @Override
    protected void layoutChildren() {
        final Insets insets = getInsets();
        final double width = getWidth();
        final double height = getHeight();
        final double top = insets.getTop();
        final double left = insets.getLeft();
        final double bottom = insets.getBottom();
        final double right = insets.getRight();
        final double insideWidth = width - left - right;
        final double insideHeight = height - top - bottom;

        double rowHeight = getPrefHeightOfChildren(getChildren());
        double rowCount = Math.round(insideHeight / rowHeight);

        // １行の場合は super に任せる
        if (rowCount == 1) {
            // pill をセット
            addPills(null);
            super.layoutChildren();

        // 複数行の時は調節する
        } else {
            List<Run> runs = getRun(insideWidth, rowCount);
            addPills(runs);
            runs.forEach(run -> {
                run.rects.forEach(rect -> {
                    layoutInArea(rect.node, rect.x + left, rect.y + top, rect.width, rect.height, run.baselineOffset, null, HPos.CENTER, VPos.CENTER);
                });
            });
        }
    }

    /**
     * StyleClass に pill を付ける.
     * @param runs
     */
    private void addPills(List<Run> runs) {
        // つけた pill を取り除く
        getChildren().forEach(node -> {
            String orig = node.getStyleClass().get(0);
            node.getStyleClass().clear();
            node.getStyleClass().add(orig);
        });
        // 新たに pill を付け直す
        if (runs == null) {
            getChildren().get(0).getStyleClass().add("left-pill");
            for (int i=1; i< getChildren().size()-1; i++) { getChildren().get(i).getStyleClass().add("center-pill"); }
            getChildren().get(getChildren().size()-1).getStyleClass().add("right-pill");

        } else {
            // １行目
            List<LayoutRect> rects = runs.get(0).rects;
            rects.get(0).node.getStyleClass().add("top-left-pill");
            for (int i=1; i<rects.size()-1; i++) { rects.get(i).node.getStyleClass().add("top-center-pill"); }
            rects.get(rects.size()-1).node.getStyleClass().add("top-right-pill");
            // 中間行
            for (int r=1; r<runs.size()-1; r++) {
                rects = runs.get(r).rects;
                rects.get(0).node.getStyleClass().add("center-left-pill");
                for (int i=1; i<rects.size()-1; i++) { rects.get(i).node.getStyleClass().add("center-center-pill"); }
                rects.get(rects.size()-1).node.getStyleClass().add("center-right-pill");
            }
            // 最終行
            rects = runs.get(runs.size()-1).rects;
            rects.get(0).node.getStyleClass().add("bottom-left-pill");
            for (int i=1; i<rects.size()-1; i++) { rects.get(i).node.getStyleClass().add("bottom-center-pill"); }
            rects.get(rects.size()-1).node.getStyleClass().add("bottom-right-pill");
        }
    }

    /**
     * Children を行に分割して justify して配置する.
     * @param insideWidth
     * @param rowCount
     * @return
     */
    private List<Run> getRun(double insideWidth, double rowCount) {
        List<Run> runs = new ArrayList<>();

        // children の数を rowCount に分割する
        List<Integer> childrenPerRow = new ArrayList<>();
        int childrenSize = getChildren().size();
        int quotient = childrenSize / (int) rowCount;
        int residue = childrenSize % (int) rowCount;
        // 商を results に入れる
        for (int row = 0; row < rowCount; row++) {
            childrenPerRow.add(quotient);
        }
        // 余りは，children トータルの幅が短いところに入れる
        // まず，行をキーとした children 幅のマップを作る
        HashMap<Integer, Double> widthMap = new HashMap<>();
        // 各行の node の幅を足してマップを作る
        int count = 0;
        for (int row=0; row<rowCount; row++) {
            double width = 0;
            for (int i=0; i<childrenPerRow.get(row); i++) {
                // 各行の node の幅を足す
                width += getChildren().get(count++).prefWidth(-1);
            }
            widthMap.put(row, width);
        }
        // できたマップを children 幅で小さい順にソートする
        List<Entry<Integer, Double>> entries = new ArrayList<>(widthMap.entrySet());
        entries.sort((o1,o2) -> (int) (o1.getValue() - o2.getValue()));
        // residue を，上で作った順番に入れていく
        for(int i=0; i<residue; i++) {
            int row = entries.get(i).getKey();
            int cpr = childrenPerRow.get(row);
            childrenPerRow.set(row, cpr + 1);
        }

        // 各行の余りスペースを求める
        count = 0;
        List<Integer> extraQuotient = new ArrayList<>();
        List<Integer> extraResidue = new ArrayList<>();
        for (int row = 0; row < rowCount; row++) {
            double totalWidth = 0;
            for (int i=0; i< childrenPerRow.get(row); i++) {
                totalWidth += Math.round(getChildren().get(count++).prefWidth(-1));
            }
            int extraSpace = (int) (insideWidth - totalWidth);
            int cpr = childrenPerRow.get(row);
            extraQuotient.add(extraSpace / cpr);
            extraResidue.add(extraSpace % cpr);
        }

        count = 0;
        double x, y = 0, height = getPrefHeightOfChildren(getChildren());
        for (int row = 0; row < rowCount; row++) {
            // 行ごとに新しい Run を作る
            Run run = new Run();
            // 開始 x 座標は常に 0
            x = 0;
            for (int i=0; i< childrenPerRow.get(row); i++) {
                Node c = getChildren().get(count++);

                LayoutRect rect = new LayoutRect();
                rect.node = c;
                rect.x = x;
                rect.y = y;
                rect.height = height;
                // 余りスペースいっぱいに広げる
                rect.width = Math.round(c.prefWidth(-1)) + extraQuotient.get(row);
                // residue の調節
                if (extraResidue.get(row) < 0 && i < -extraResidue.get(row)) { rect.width--; }
                else if (extraResidue.get(row) > 0 && i < extraResidue.get(row)) { rect.width++; }
                // そこまで広げられるように MaxWidth をセット
                ((Region)c).setMaxWidth(rect.width);

                run.rects.add(rect);

                // x 座標をずらす
                x += rect.width;
            }
            // 行が終了したらこのターンの Run の作成は終了
            runs.add(run);
            y += height;
        }

        return runs;
    }


    /**
     * children の高さとしては，最初の child の高さを使う
     * @param children
     * @return
     */
    private double getPrefHeightOfChildren(List<Node> children) {
        return children.get(0).prefHeight(-1);
    }

    /**
     * 各々の node を保持する
     */
    private class LayoutRect {
        public Node node;
        public double x;
        public double y;
        public double width;
        public double height;
    }

    /**
     * 各行に並べる node を保持する
     */
    private class Run {
        public List<LayoutRect> rects = new ArrayList<>();
        public double width;
        public double height;
        public double baselineOffset;
    }
}

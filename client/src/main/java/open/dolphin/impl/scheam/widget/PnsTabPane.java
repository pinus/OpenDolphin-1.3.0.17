package open.dolphin.impl.scheam.widget;

import javafx.scene.control.skin.TabPaneSkin;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Skin;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

/**
 * @author pns
 */
public class PnsTabPane extends TabPane {

    public PnsTabPane() {
        getStyleClass().addAll(STYLE_CLASS_FLOATING, "pns-tab-pane");
        getTabs().addListener((Observable ov) -> {
            // 付けていた StyleClass を削除
            getTabs().forEach(tab -> {
                String orig = tab.getStyleClass().get(0);
                tab.getStyleClass().clear();
                tab.getStyleClass().add(orig);
            });
            // 新たに付け直す
            getTabs().get(0).getStyleClass().add("left-pill");
            for (int i = 1; i < getTabs().size() - 1; i++) {
                getTabs().get(i).getStyleClass().add("center-pill");
            }
            getTabs().get(getTabs().size() - 1).getStyleClass().add("right-pill");
        });
    }

    /**
     * Tabs をセンタリングするためにここを hook する.
     *
     * @return
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new AdjustableTabPaneSkin(this);
    }

    /**
     * Tabs をセンタリングする TabPaneSkin
     */
    private class AdjustableTabPaneSkin extends TabPaneSkin {

        public AdjustableTabPaneSkin(TabPane tabPane) {
            super(tabPane);
        }

        /**
         * TabHeaderArea の幅と HeadersRegion の幅を使ってセンタリングする.
         * tab-header-area: Tabs が描かれる領域全体.
         * headrs-region: その中で，実際に Tabs が描かれた領域.
         * tab-header-area の幅は，layoutChildren の w に一致する.
         * headers-region の幅は getWidth() だと 0 なので prefWidth(-1) で計算されたものを取得して使う.
         *
         * @param x
         * @param y
         * @param w
         * @param h
         */
        @Override
        protected void layoutChildren(final double x, final double y, final double w, final double h) {
            super.layoutChildren(x, y, w, h);

            if (getSide().equals(Side.TOP) || getSide().equals(Side.BOTTOM)) {
                StackPane region = (StackPane) lookup(".headers-region");
                StackPane header = (StackPane) lookup(".tab-header-area");

                double indent = (w - region.prefWidth(-1)) / 2;
                Insets p = header.getPadding();
                Insets insets = new Insets(p.getTop(), p.getRight(), p.getBottom(), indent);
                header.setPadding(insets);
            }
        }
    }
}

package open.dolphin.helper;

import open.dolphin.client.ChartImpl;
import open.dolphin.client.EditorFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * WindowHolder - 全ての WindowSupport インスタンスを保持する.
 */
public class WindowHolder {
    final private static List<WindowSupport<?>> windowSupports = new ArrayList<>();

    /**
     * WindowSupport をリストに加える.
     * @param windowSupport WindowSupport to add
     */
    public static void add(WindowSupport<?> windowSupport) {
        windowSupports.add(windowSupport);
    }

    /**
     * WindowSupport をリストから取り除く.
     * @param windowSupport to remove
     * @return true if removed
     */
    public static boolean remove(WindowSupport<?> windowSupport) {
        return windowSupports.remove(windowSupport);
    }

    /**
     * List of all WindowSupport instances.
     *
     * @return unmodifiableList
     */
    public static List<WindowSupport<?>> allWindowSupports() {
        return Collections.unmodifiableList(windowSupports);
    }

    /**
     * 保持している WindowSupport の数を返す.
     * @return 保持している WindowSupport の数
     */
    public static int size() {
        return windowSupports.size();
    }

    /**
     * List of all EditorFrame instances.
     *
     * @return 存在しない場合、size 0 で null にはならない
     */
    public static List<EditorFrame> allEditorFrames() {
        return windowSupports.stream()
            .map(WindowSupport::getContent).filter(EditorFrame.class::isInstance).map(EditorFrame.class::cast).toList();
    }

    /**
     * List of all ChartImpl instances.
     *
     * @return 存在しない場合、size 0 で、null にはならない
     */
    public static List<ChartImpl> allCharts() {
        return windowSupports.stream()
            .map(WindowSupport::getContent).filter(ChartImpl.class::isInstance).map(ChartImpl.class::cast).toList();
    }

    /**
     * 指定された WindowSupport を先頭に移動する.
     *
     * @param windowSupport 先頭に移動する WindowSupport
     */
    public static void toTop(WindowSupport<?> windowSupport) {
        if (windowSupports.remove(windowSupport)) {
            windowSupports.add(0, windowSupport);
        }
    }
}

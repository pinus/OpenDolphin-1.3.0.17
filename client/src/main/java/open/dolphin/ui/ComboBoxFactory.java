package open.dolphin.ui;

import java.util.Arrays;
import java.util.List;
import javax.swing.JComboBox;
import open.dolphin.util.PNSPair;

/**
 *
 * @author pns
 */
public class ComboBoxFactory {

    /**
     * 病名抽出期間選択 ComboBox.
     * @return
     */
    public static JComboBox<PNSPair<String,Integer>> createDiagnosisExtractionPeriodCombo() {
        return createComboBox(getDiagnosisExtractionPeriodModel());
    }

    /**
     * 病名抽出期間選択 ComboBox のモデル List を返す.
     * @return
     */
    public static List<PNSPair<String,Integer>> getDiagnosisExtractionPeriodModel() {
        List<PNSPair<String,Integer>> list = Arrays.asList(
            new PNSPair<>("全て", 0),
            new PNSPair<>("1年", -12),
            new PNSPair<>("2年", -24),
            new PNSPair<>("3年", -36),
            new PNSPair<>("5年", -60)
        );
        return list;
    }

    /**
     * 文書抽出期間選択 ComboBox.
     * @return
     */
    public static JComboBox<PNSPair<String,Integer>> createDocumentExtractionPeriodCombo() {
        return createComboBox(getDocumentExtractionPeriodModel());
    }

    /**
     * 文書抽出期間選択 ComboBox のモデル List を返す.
     * @return
     */
    public static List<PNSPair<String,Integer>> getDocumentExtractionPeriodModel() {
        List<PNSPair<String,Integer>> list = Arrays.asList(
            new PNSPair<>("半年", -6),
            new PNSPair<>("1年", -12),
            new PNSPair<>("2年", -24),
            new PNSPair<>("3年", -36),
            new PNSPair<>("5年", -60),
            new PNSPair<>("全て", -360) // 30年 must be enough
        );
        return list;
    }

    /**
     * ラボテスト抽出期間選択 ComboBox.
     * @return
     */
    public static JComboBox<PNSPair<String,Integer>> createLaboExtractionPeriodCombo() {
        return createComboBox(getLaboExtractionPeriodModel());
    }

    /**
     * ラボテスト抽出期間選択 ComboBox のモデル List を返す.
     * @return
     */
    public static List<PNSPair<String,Integer>> getLaboExtractionPeriodModel() {
        List<PNSPair<String,Integer>> list = Arrays.asList(
            new PNSPair<>("1年", -12),
            new PNSPair<>("1ヶ月", -1),
            new PNSPair<>("2ヶ月", -2),
            new PNSPair<>("3ヶ月", -3),
            new PNSPair<>("半年", -6),
            new PNSPair<>("2年", -24),
            new PNSPair<>("3年", -36),
            new PNSPair<>("5年", -60),
            new PNSPair<>("全て", -360) // 30年 must be enough
        );
        return list;
    }

    private static JComboBox<PNSPair<String,Integer>> createComboBox(List<PNSPair<String,Integer>> periods) {
        JComboBox<PNSPair<String,Integer>> combo = new JComboBox<>();
        periods.forEach(periodPair -> combo.addItem(periodPair));

        return combo;
    }
}

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
     * 病名抽出期間選択 ComboBox
     * @return
     */
    public static JComboBox<PNSPair<String,Integer>> getDiagnosisExtractionPeriodCombo() {
        List<PNSPair<String,Integer>> extractionPeriods = getDiagnosisExtractionPeriodModel();
        JComboBox<PNSPair<String,Integer>> combo = new JComboBox<>();
        extractionPeriods.forEach(periodPair -> combo.addItem(periodPair));

        return combo;
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
     * 文書抽出期間選択 ComboBox
     * @return
     */
    public static JComboBox<PNSPair<String,Integer>> getDocumentExtractionPeriodCombo() {
        List<PNSPair<String,Integer>> extractionPeriods = getDocumentExtractionPeriodModel();
        JComboBox<PNSPair<String,Integer>> combo = new JComboBox<>();
        extractionPeriods.forEach(periodPair -> combo.addItem(periodPair));

        return combo;
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
}

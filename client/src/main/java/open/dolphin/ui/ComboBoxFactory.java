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
        List<PNSPair<String,Integer>> extractionPeriods = Arrays.asList(
            new PNSPair<>("全て", 0),
            new PNSPair<>("1年", -12),
            new PNSPair<>("2年", -24),
            new PNSPair<>("3年", -36),
            new PNSPair<>("5年", -60)
        );
        JComboBox<PNSPair<String,Integer>> combo = new JComboBox<>();
        extractionPeriods.forEach(periodPair -> combo.addItem(periodPair));

        return combo;
    }

}

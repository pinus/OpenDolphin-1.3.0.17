package open.dolphin.ui;

import open.dolphin.helper.PNSPair;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author pns
 */
public class ComboBoxFactory {

    /**
     * 病名抽出期間選択 ComboBox.
     *
     * @return
     */
    public static JComboBox<PNSPair<String, Integer>> createDiagnosisExtractionPeriodCombo() {
        return createComboBox(getDiagnosisExtractionPeriodModel());
    }

    /**
     * 病名抽出期間選択 ComboBox のモデル List を返す.
     *
     * @return
     */
    public static List<PNSPair<String, Integer>> getDiagnosisExtractionPeriodModel() {
        return Arrays.asList(
                new PNSPair<>("全て", 0),
                new PNSPair<>("1年", -12),
                new PNSPair<>("2年", -24),
                new PNSPair<>("3年", -36),
                new PNSPair<>("5年", -60)
        );
    }

    /**
     * 文書抽出期間選択 ComboBox.
     *
     * @return
     */
    public static JComboBox<PNSPair<String, Integer>> createDocumentExtractionPeriodCombo() {
        return createComboBox(getDocumentExtractionPeriodModel());
    }

    /**
     * 文書抽出期間選択 ComboBox のモデル List を返す.
     *
     * @return
     */
    public static List<PNSPair<String, Integer>> getDocumentExtractionPeriodModel() {
        return Arrays.asList(
                new PNSPair<>("半年", -6),
                new PNSPair<>("1年", -12),
                new PNSPair<>("2年", -24),
                new PNSPair<>("3年", -36),
                new PNSPair<>("5年", -60),
                new PNSPair<>("全て", -360) // 30年 must be enough
        );
    }

    /**
     * ラボテスト抽出期間選択 ComboBox.
     *
     * @return
     */
    public static JComboBox<PNSPair<String, Integer>> createLaboExtractionPeriodCombo() {
        return createComboBox(getLaboExtractionPeriodModel());
    }

    /**
     * ラボテスト抽出期間選択 ComboBox のモデル List を返す.
     *
     * @return
     */
    public static List<PNSPair<String, Integer>> getLaboExtractionPeriodModel() {
        return Arrays.asList(
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
    }

    /**
     * 用法選択 ComboBox.
     *
     * @return
     */
    public static JComboBox<PNSPair<String, String>> createAdminCategoryCombo() {
        return createComboBox(getAdminCategoryModel());
    }

    /**
     * 用法 ComboBox のモデル List を返す.
     *
     * @return
     */
    public static List<PNSPair<String, String>> getAdminCategoryModel() {
        return Arrays.asList(
                new PNSPair<>("用法検索", ""), new PNSPair<>("内服１回等(100)", "0010001"),
                new PNSPair<>("内服２回等(200)", "0010002"), new PNSPair<>("内服３回等(300)", "0010003"),
                new PNSPair<>("内服その他(400)", "0010004"), new PNSPair<>("頓用等(500)", "0010005"),
                new PNSPair<>("外用等(600)", "0010006"), new PNSPair<>("点眼等(700)", "0010007"),
                new PNSPair<>("部位等(800)", "0010008"), new PNSPair<>("全て", "001"),
                new PNSPair<>("コメント", "810000001"), new PNSPair<>("一般名記載", "099209908")
        );
    }

    private static <T> JComboBox<PNSPair<String, T>> createComboBox(List<PNSPair<String, T>> periods) {
        JComboBox<PNSPair<String, T>> combo = new JComboBox<>();
        periods.forEach(combo::addItem);

        return combo;
    }
}

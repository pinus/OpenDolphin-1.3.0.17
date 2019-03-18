package open.dolphin.helper;

/**
 * １日量文字列ユーティリティー
 *
 * @author pns
 */
public class DailyDoseStringTool {

    /**
     * １日量文字列 src の用量部分を dose で置き換える.
     *
     * @param src  1日量文字列
     * @param dose 用量
     * @return 用量文字列
     */
    public static String getString(String src, double dose) {
        int[] index = getNumberIndex(src);
        if (index[0] == 0) {
            return src;
        } // 数字がなかった場合

        String unit = src.substring(index[2], index[2] + 1);
        String num = doubleToString(dose, unit);

        return src.substring(0, index[0]) + num + src.substring(index[1]);
    }

    /**
     * double で与えられた dose から文字列を作って返す.
     *
     * @param dose 用量
     * @return 用量文字列
     */
    public static String doubleToString(double dose, String unit) {
        // dose を文字列に変換
        String num = String.format("%.2f", dose);
        // 最後の 0 をできるだけ取り除く
        while (num.endsWith("0")) {
            num = num.substring(0, num.length() - 1);
        }
        // 小数点以下がなくなった場合
        if (num.endsWith(".")) {
            if ("ｇ".equals(unit) || "g".equals(unit)) {
                // 粉なら小数点以下１桁は残す
                num += "0";
            } else {
                // 粉じゃなければ小数点は削除
                num = num.substring(0, num.length() - 1);
            }
        }
        return num;
    }

    /**
     * １日量文字列から，１日量を double で返す
     *
     * @param str １日量文字列
     * @return １日量
     */
    public static double getDose(String str) {
        int[] index = getNumberIndex(str);
        if (index[0] == 0) return 0; // 数字がなかった
        return Double.parseDouble(StringTool.toHankakuNumber(str.substring(index[0], index[1])));
    }

    /**
     * １日量文字列 str から数字を検出して，その index を返す.
     *
     * @param str 1日量文字列
     * @return index[0] = startIndex, index[1] = endIndex, index[2] = unit start index
     */
    private static int[] getNumberIndex(String str) {
        int[] index = new int[3];
        // 数字の始まりを検出
        for (int i = str.indexOf("日量") + "日量".length(); i < str.length(); i++) {
            char c = str.charAt(i);
            if (StringTool.isZenkakuNumber(c) || StringTool.isHankakuNumber(c)) {
                index[0] = i;
                break;
            }
        }
        // 数字の終了を検出
        for (int i = index[0]; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!StringTool.isZenkakuNumber(c) && !StringTool.isHankakuNumber(c)) {
                index[1] = i;
                break;
            }
        }
        // 単位を検出
        for (int i = index[1]; i < str.length(); i++) {
            if (!StringTool.isSpace(str.charAt(i))) {
                index[2] = i;
                break;
            }
        }

        return index;
    }
}

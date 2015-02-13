package open.dolphin.util;

/**
 * Utilities to handel String.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class StringTool {

    private static final Character[] HIRAGANA = { Character.valueOf('あ'), Character.valueOf('ん')};
    private static final Character[] KATAKANA = { Character.valueOf('ア'), Character.valueOf('ン') };

    private static final Character[] ZENKAKU_UPPER = {Character.valueOf('Ａ'), Character.valueOf('Ｚ')};
    private static final Character[] ZENKAKU_LOWER = {Character.valueOf('ａ'), Character.valueOf('ｚ')};
    private static final Character[] ZENKAKU_NUMBER = { Character.valueOf('０'), Character.valueOf('９') };

    private static final Character[] HANKAKU_UPPER = {Character.valueOf('A'), Character.valueOf('Z')};
    private static final Character[] HANKAKU_LOWER = {Character.valueOf('a'), Character.valueOf('z')};
    private static final Character[] HANKAKU_NUMBER = { Character.valueOf('0'), Character.valueOf('9') };

    private StringTool() {}

    /**
     * 文字 c がカタカナかどうか
     * @param c
     * @return
     */
    public static boolean isKatakana(char c) {
        // ア 12449 12353 半角
        // ン 12531
        Character test = Character.valueOf(c);
        return (test.compareTo(KATAKANA[0]) >= 0 && test.compareTo(KATAKANA[1]) <= 0) ? true : false;
    }

    /**
     * 文字 c がひらがなかどうか
     * @param c
     * @return
     */
    public static boolean isHiragana(char c) {
        // あ 12354
        // ん 12435
        Character test = Character.valueOf(c);
        return (test.compareTo(HIRAGANA[0]) >= 0 && test.compareTo(HIRAGANA[1]) <= 0) ? true : false;
    }

    /**
     * 文字 c がひらがななら，カタカナに変換する
     * @param c
     * @return
     */
    private static char toKatakana(char c) {
        return isHiragana(c) ? (char) ((int) c + 96) : c;
    }

    /**
     * 文字列 s のひらがな部分をカタカナに変換する
     * @param s
     * @return
     */
    public static String hiraganaToKatakana(String s) {
        int len = s.length();
        char[] src = new char[len];
        s.getChars(0, s.length(), src, 0);
        char[] dst = new char[len];
        for (int i = 0; i < len; i++) dst[i] = toKatakana(src[i]);
        return new String(dst);
    }

    /**
     * 文字列 str が全て半角数字かどうか
     * @param str
     * @return
     */
    public static boolean isAllDigit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }

    /**
     * 文字 c が全角の大文字アルファベットかどうか
     * @param c
     * @return
     */
    public static boolean isZenkakuUpper(char c) {
        Character test = Character.valueOf(c);
        return (test.compareTo(ZENKAKU_UPPER[0]) >= 0 && test.compareTo(ZENKAKU_UPPER[1]) <= 0) ? true : false;
    }

    /**
     * 文字 c が全角の小文字アルファベットかどうか
     * @param c
     * @return
     */
    public static boolean isZenkakuLower(char c) {
        Character test = Character.valueOf(c);
        return (test.compareTo(ZENKAKU_LOWER[0]) >= 0 && test.compareTo(ZENKAKU_LOWER[1]) <= 0) ? true : false;
    }

    /**
     * 文字 c が全角の数字かどうか
     * @param c
     * @return
     */
    public static boolean isZenkakuNumber(char c) {
        Character test = Character.valueOf(c);
        return (test.compareTo(ZENKAKU_NUMBER[0]) >= 0 && test.compareTo(ZENKAKU_NUMBER[1]) <= 0 || test == '．') ? true : false;
    }

    /**
     * 文字 c が半角の大文字アルファベットかどうか
     * @param c
     * @return
     */
    public static boolean isHankakuUpper(char c) {
        Character test = Character.valueOf(c);
        return (test.compareTo(HANKAKU_UPPER[0]) >= 0 && test.compareTo(HANKAKU_UPPER[1]) <= 0) ? true : false;
    }

    /**
     * 文字 c が半角の小文字アルファベットかどうか
     * @param c
     * @return
     */
    public static boolean isHanakuLower(char c) {
        Character test = Character.valueOf(c);
        return (test.compareTo(HANKAKU_LOWER[0]) >= 0 && test.compareTo(HANKAKU_LOWER[1]) <= 0) ? true : false;
    }

    /**
     * 文字 c が半角の数字かどうか
     * @param c
     * @return
     */
    public static boolean isHankakuNumber(char c) {
        Character test = Character.valueOf(c);
        return (test.compareTo(HANKAKU_NUMBER[0]) >= 0 && test.compareTo(HANKAKU_NUMBER[1]) <= 0 || test == '.') ? true : false;
    }

    /**
     * 半角または全角のスペースかどうか
     * @param c
     * @return
     */
    public static boolean isSpace(char c) {
        Character test = Character.valueOf(c);
        return (test.equals(' ') || test.equals('　'))? true : false;
    }

    /**
     * 文字列 s の全角アルファベットを全角に変換する
     * @param s
     * @return
     */
    public static String toZenkakuUpperLower(String s) {
        int len = s.length();
        char[] src = new char[len];
        s.getChars(0, s.length(), src, 0);

        StringBuilder sb = new StringBuilder();
        for (char c : src) {
            if (isHankakuUpper(c) || isHanakuLower(c)) {
                sb.append( (char)((int)c + 65248) );
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 文字列 s のアルファベットを半角に変換する
     * @param s
     * @return
     */
    public static String toHankakuUpperLower(String s) {
        int len = s.length();
        char[] src = new char[len];
        s.getChars(0, s.length(), src, 0);
        StringBuilder sb = new StringBuilder();
        for (char c : src) {
            if (isZenkakuUpper(c) || isZenkakuLower(c)) {
                sb.append( (char)((int)c - 65248) );
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 文字列 s の半角数字を全角に変換する
     * @param s
     * @return
     */
    public static String toZenkakuNumber(String s) {
        int len = s.length();
        char[] src = new char[len];
        s.getChars(0, s.length(), src, 0);
        StringBuilder sb = new StringBuilder();
        for (char c : src) {
            if (isHankakuNumber(c)) {
                sb.append( (char)((int)c + 65248) );
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 文字列 s の全角数字を半角に変換する
     * @param s
     * @return
     */
    public static String toHankakuNumber(String s) {
        int len = s.length();
        char[] src = new char[len];
        s.getChars(0, s.length(), src, 0);
        StringBuilder sb = new StringBuilder();
        for (char c : src) {
            if (isZenkakuNumber(c)) {
                sb.append( (char)((int)c - 65248) );
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 文字列をシングルクオートで囲む
     * @param s
     * @return
     */
    public static String addSingleQuote(String s) {
        return String.format("'%s'", s);
    }

    /**
     * 文字列をシングルクオートで囲み，コンマをつける
     * @param s
     * @return
     */
    public static String addSingleQuoteComma(String s) {
        return String.format("'%s',", s);
    }
}

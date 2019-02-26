package open.dolphin.infomodel;

import org.apache.commons.lang.StringUtils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ModelUtils.
 *
 * @author Minagawa,Kazushi
 * @author pns
 */
public class ModelUtils implements IInfoModel {
    private static final long serialVersionUID = 1L;

    /**
     * MML 形式から日付だけ取り出す.
     * @param mmlDate MML 型式の日付 (2008-02-01T12:23:34)
     * @return 日付 (2008-02-01)
     */
    public static String trimTime(String mmlDate) {
        if (mmlDate == null) {
            return null;
        }

        int index = mmlDate.indexOf('T');
        if (index > -1) {
            return mmlDate.substring(0, index);
        } else {
            return mmlDate;
        }
    }

    /**
     * MML 形式から時：分だけ取り出す.
     * @param mmlDate MML 型式の日付 (2008-02-01T12:23:34)
     * @return 時：分 (12:23)
     */
    public static String trimDate(String mmlDate) {
        if (mmlDate == null) {
            return null;
        }

        int index = mmlDate.indexOf('T');
        if (index > -1) {
            return mmlDate.substring(index + 1, index + 6); // THH:mm:ss -> HH:mm
        } else {
            return mmlDate;
        }
    }

    /**
     * mml 形式の生年月日から年齢付きの形式を作る.
     * @param mmlBirthday 1975-01-01
     * @return 32.10 歳 (S50-01-01)
     */
    public static String getAgeBirthday(String mmlBirthday) {
        String age = getAge(mmlBirthday);
        if (age == null) return null;
        return String.format("%s %s (%s)", age, AGE, toNengo(mmlBirthday));
    }

    /**
     * 年齢付きの生年月日「32.10 歳 (S50-01-01)」形式から mmlBirthday を返す.
     * @param birthdayWithAge 32.10 歳 (S50-01-01)
     * @return 1975-01-01
     */
    public static String getMmlBirthdayFromAge(String birthdayWithAge) {
        String[] s = birthdayWithAge.split("[()]");
        return toSeireki(s[1]);
    }

    /**
     * 西暦 -> 年号変換.
     * @param mmlBirthday 1975-01-01
     * @return nengoBirthday S50-01-01
     */
    public static String toNengo(String mmlBirthday) {
        return Gengo.toGengo(mmlBirthday);
    }

    /**
     * 年号 -> 西暦変換.
     * @param nengoBirthday H22-7-26
     * @return mmlBirthday 2010-07-26
     */
    public static String toSeireki(String nengoBirthday) {
        return Gengo.toSeireki(nengoBirthday);
    }

    /**
     * ORCA 形式を年号形式に.
     * @param orcaBirthday 4220726
     * @return h22-07-26
     */
    public static String orcaDateToNengo(String orcaBirthday) {
        //元号
        String nengo = Gengo.numberToString(orcaBirthday.substring(0, 1));
        //年
        String y = orcaBirthday.substring(1, 3);
        String m = orcaBirthday.substring(3, 5);
        String d = orcaBirthday.substring(5, 7);

        return nengo.toLowerCase() + y + "-" + m + "-" + d;
    }

    /**
     * 年号アルファベットを漢字に変換.
     * @param alphabet [M,T,S,H,...]
     * @return 元号漢字 [㍾,㍽,㍼,㍻,...]
     */
    public static String nengoAlphabetToKanji(String alphabet) { return Gengo.toKanji(alphabet); }

    /**
     * 年齢を作る.
     * @param mmlBirthday 1975-01-01
     * @return 32.10
     */
    public static String getAge(String mmlBirthday) {

        GregorianCalendar gc1 = getCalendar(mmlBirthday);
        if (gc1 == null) { return null; }

        GregorianCalendar gc2 = new GregorianCalendar(); // Today
        int years = 0;

        gc1.clear(Calendar.MILLISECOND);
        gc1.clear(Calendar.SECOND);
        gc1.clear(Calendar.MINUTE);
        gc1.clear(Calendar.HOUR_OF_DAY);

        gc2.clear(Calendar.MILLISECOND);
        gc2.clear(Calendar.SECOND);
        gc2.clear(Calendar.MINUTE);
        gc2.clear(Calendar.HOUR_OF_DAY);

        while (gc1.before(gc2)) {
            gc1.add(Calendar.YEAR, 1);
            years++;
        }
        years--;

        int month = 12;

        while (gc1.after(gc2)) {
            gc1.add(Calendar.MONTH, -1);
            month--;
        }

        return String.format("%d.%d", years, month);
    }

    /**
     * mmlDate 形式から GregorianCalendar を作る.
     * @param mmlDate 1975-01-01
     * @return GregorianCalendar
     */
    public static GregorianCalendar getCalendar(String mmlDate) {

        Date date;
        if (mmlDate.contains("T")) { date = getDateTimeAsObject(mmlDate); }
        else { date = getDateAsObject(mmlDate); }

        if (date == null) { return null; }

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(date.getTime());
        return gc;
    }

    /**
     * 時間なしの mmlDate 形式から Date を作る.
     * @param mmlDate 1975-01-01
     * @return parsed Date
     */
    public static Date getDateAsObject(String mmlDate) {
        if (mmlDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_WITHOUT_TIME);
                return sdf.parse(mmlDate);

            } catch (ParseException e) {
                e.printStackTrace(System.err);
            }
        }
        return null;
    }

    /**
     * 時間付きの mmlDate 形式から Date を作る.
     * @param mmlDate 1975-01-01T12:23:34
     * @return parsed Date
     */
    public static Date getDateTimeAsObject(String mmlDate) {
        if (mmlDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
                return sdf.parse(mmlDate);

            } catch (ParseException e) {
                e.printStackTrace(System.err);
            }
        }
        return null;
    }

    /**
     * Date から時間なしの mmlDate 形式を作る.
     * @param date Date
     * @return 1975-01-01
     */
    public static String getDateAsString(Date date) {
        return getDateAsFormatString(date, DATE_WITHOUT_TIME);
    }

    /**
     * Date から時間付きの mmlDate 形式を作る.
     * @param date Date
     * @return 1975-01-01T12:23:34
     */
    public static String getDateTimeAsString(Date date) {
        return getDateAsFormatString(date, ISO_8601_DATE_FORMAT);
    }

    /**
     * Date から format で指定した形式の日付文字列を作る.
     * @param date Date
     * @param format SimpleDateFormat string
     * @return formatted string
     */
    public static String getDateAsFormatString(Date date, String format) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * ORCA日付（20120401）を MMLフォーマット（2012-04-01）に変換.
     * @param orcaDateString ORCA日付
     * @return MML日付
     */
    public static String toDolphinDateString(String orcaDateString) {
        if (orcaDateString == null || ! orcaDateString.matches("[0-9]+")) { return null; }
        return String.join("-", orcaDateString.substring(0,4), orcaDateString.substring(4,6), orcaDateString.substring(6,8));
    }

    /**
     * male -> 男　変換.
     * @param gender male/female
     * @return 男/女
     */
    public static String getGenderDesc(String gender) {
        if (gender != null) {
            switch(gender.toLowerCase()) {
                case MALE: return MALE_DISP;
                case FEMALE: return FEMALE_DISP;
            }
        }
        return UNKNOWN;
    }

    /**
     * エリアス付きの病名文字列を "," で分離する.
     * @param diagnosis 病名, エリアス
     * @return [0] 病名, [1] エリアス
     */
    public static String[] splitDiagnosis(String diagnosis) {
        return (diagnosis == null)? null : diagnosis.split("\\s*,\\s*");
    }

    /**
     * エリアス付きの病名から病名を取り出す.
     * @param hasAlias エリアス付き病名
     * @return 病名. エリアスがない場合はそのまま返す.
     */
    public static String getDiagnosisName(String hasAlias) {
        String[] splits = splitDiagnosis(hasAlias);
        return (splits != null && splits.length == 2 && splits[0] != null) ? splits[0] : hasAlias;
    }

    /**
     * エリアス付き病名からエリアスを取り出す.
     * @param hasAlias エリアス付き病名
     * @return エリアス. ない場合は null を返す.
     */
    public static String getDiagnosisAlias(String hasAlias) {
        String[] splits = splitDiagnosis(hasAlias);
        return (splits != null && splits.length == 2 && splits[1] != null) ? splits[1] : null;
    }

    /**
     * PVTDelegater で使う date を作成する（WaitingListImpl から移動).
     * date[0] = today, date[1] = AppodateFrom, date[2] = AppodateTo
     * @param date Date
     * @return [0] 今日, [1] 2ヶ月前, [2] あれっ??
     */
    public static String[] getSearchDateAsString(Date date) {

        String[] ret = new String[3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ret[0] = sdf.format(date);

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        gc.add(Calendar.DAY_OF_MONTH, -2);
        ret[1] = sdf.format(gc.getTime());

        gc.add(Calendar.DAY_OF_MONTH, 2);
        ret[2] = sdf.format(gc.getTime());

        return ret;
    }

    /**
     * ORCA転帰を Dolphin転帰に変換.
     * @param orcaOutcome ORCA転帰 (DAO=1,2,3,8 or API=F,D,C,S)
     * @return Dolphin転帰
     */
    public static DiagnosisOutcomeModel toDolphinOutcome(String orcaOutcome) {
        if (Objects.nonNull(orcaOutcome)) {
            switch (orcaOutcome) {
                // 数字=DAO, 文字=API
                case "1":
                case "F":
                    return DiagnosisOutcome.fullyRecovered.model(); // 治癒
                case "2":
                case "D":
                    return DiagnosisOutcome.end.model(); // 死亡→終了に変換
                case "3":
                case "C":
                    return DiagnosisOutcome.pause.model(); // 中止
                case "8":
                case "S":
                    return DiagnosisOutcome.pause.model(); // 移行→中止に変換
            }
        }
        return DiagnosisOutcome.none.model();
    }

    /**
     * Dolphin転帰を ORCA転帰に変換.
     * @param outcome DiagnosisOutcomeModel
     * @return ORCA転帰
     */
    public static String toOrcaOutcome(DiagnosisOutcomeModel outcome) {
        if (Objects.nonNull(outcome)) {
            if (outcome.getOutcome().equals(DiagnosisOutcome.pause.name())) {
                return "C";
            } else if (outcome.getOutcome().equals(DiagnosisOutcome.fullyRecovered.name())
                || outcome.getOutcome().equals(DiagnosisOutcome.end.name())) {
                return "F";
            }
        }
        return "";
    }

    /**
     * Object を beanBytes にエンコードする.
     * @param bean エンコード対象の Object
     * @return エンコードされた byte array
     */
    public static byte[] xmlEncode(Object bean)  {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try (XMLEncoder e = new XMLEncoder(new BufferedOutputStream(bo))) {
            e.writeObject(bean);
        }
        return bo.toByteArray();
    }

    /**
     * beanBytes をデコードする.
     * @param bytes byte array
     * @return デコードされた Object
     */
    public static Object xmlDecode(byte[] bytes) {

        Object o;
        try (XMLDecoder d = new XMLDecoder(
                new BufferedInputStream(
                new ByteArrayInputStream(bytes)))) {
            o = d.readObject();
        }

        return o;
    }

    /**
     * xml から&lt;text&gt;テキスト&lt;/text&gt;のテキストを取り出す.
     * @param xml xmlテキスト
     * @return 取り出したテキスト
     */
    public static String extractText(String xml) {
        StringBuilder buf = new StringBuilder();
        String[] head = xml.split("<text>");
        for (String str : head) {
            String[] tail = str.split("</text>");
            if (tail.length == 2) { buf.append(tail[0].trim()); }
        }
        return buf.toString();
    }

    /**
     * バイナリの健康保険データをオブジェクトにデコードする.
     * @param insurances List of HealthInsuranceModel with BeanBytes
     * @return List of PVTHealthInsuranceModel decoded from BeanBytes
     */
    public static List<PVTHealthInsuranceModel> decodeHealthInsurance(List<HealthInsuranceModel> insurances) {

        if (insurances != null) {
            return insurances.stream().map(ins ->
                    (PVTHealthInsuranceModel)xmlDecode(ins.getBeanBytes())).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
   }

    /**
     * treeBytes を treeXml に変換して返す.
     * @param treeBytes byte array
     * @return tree XML
     */
    public static String toTreeXml(byte[] treeBytes) {
        return new String(treeBytes, StandardCharsets.UTF_8);
    }

    /**
     * treeXml を treeBytes に変換して返す.
     * @param treeXml treeXml
     * @return byte array
     */
    public static byte[] toTreeBytes(String treeXml) {
        return treeXml.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Convert claim insurance code to orca insurance code.
     * https://www.orca.med.or.jp/receipt/tec/claim.html
     * <table>
     * <tr><th>保険の種類</th><th>労災</th><th>自費</th><th>治験</th><th>治験</th><th>公害</th><th>国保</th><th>後期高齢者</th><th>後期特療費</th><th>協会けんぽ</th><th>公費単独</th></tr>
     * <tr><th>Claim</th><td>Rx</td><td>Zx</td><td>Ax</td><td>Bx</td><td>K5</td><td>00</td><td>39</td><td>40</td><td>09</td><td>XX</td></tr>
     * <tr><th>API</th><td>97x</td><td>980</td><td>90x</td><td>91x</td><td>975</td><td>060</td><td>039</td><td>040</td><td>009</td><td>980</td></tr>
     * </table>
     * xは該当の保険番号マスタの保険番号の３桁目
     * @param code Claim Insurance Code R1,R3,Zx,...
     * @return Orca Insurance Code 971,973,901,...
     */
    public static String claimInsuranceCodeToOrcaInsuranceCode(String code) {

        if (Objects.nonNull(code) && code.length() == 2) {
            if (code.startsWith("R")) {
                // 労災
                code = code.replace("R", "97");
            } else if (code.startsWith("Z")) {
                // 自費
                code = "980";
            } else if (code.startsWith("A")) {
                // 治験
                code = code.replace("A", "90");
            } else if (code.startsWith("B")) {
                // 治験
                code = code.replace("B", "91");
            } else if (code.equals("K5")) {
                // 公害
                code = "975";
            } else if (code.equals("00")) {
                // 国保
                code = "060";
            } else if (code.equals("XX")) {
                // 公費単独
                code = "980";
            } else if (code.matches("[0-9][0-9]")) {
                // 数字２桁は頭に 0 を補う
                code = "0" + code;
            }
        }
        return code;
    }

    /**
     * RegisteredDiagnosisModel の病名コードを，Orca Api 用に変換する.
     *  eg) "1013.7061017" → { "ZZZ1013", "7061017" }
     * @param claimByomei Dolphin 型式の病名
     * @return ORCA single 型式病名
     */
    public static String[] toOrcaDiseaseSingle(String claimByomei) {
        String[] singles = claimByomei.split("\\.");
        for (int i=0; i<singles.length; i++) {
            if (singles[i].length() == 4) { singles[i] = "ZZZ" + singles[i]; }
        }
        return singles;
    }

    /**
     * Gengo 元号Enum.
     */
    private enum Gengo {
        DEFAULT("H", "㍻"),
        MEIJI("M", "㍾"),
        TAISHO("T", "㍽"),
        SHOWA("S", "㍼"),
        HEISEI("H", String.valueOf('\u337b')), // ㍻
        ANCHO("A", String.valueOf('\u32ff'));

        String alphabet, kanji;

        Gengo(String alphabet, String kanji) {
            this.alphabet = alphabet;
            this.kanji = kanji;
        }
        public String alphabet() { return alphabet; }
        public String kanji() { return kanji; }

        /**
         * 西暦 -> 元号変換.
         * @param mmlBirthday 1975-01-01
         * @return gengoBirthday S50-01-01
         */
        public static String toGengo(String mmlBirthday) {
            int year;
            int month;
            int day;
            Gengo gengo;

            year = Integer.valueOf(mmlBirthday.substring(0,4));
            month = Integer.valueOf(mmlBirthday.substring(5,7));
            day = Integer.valueOf(mmlBirthday.substring(8,10));

            // 2020年より先は新元号
            if (year >= 2020) {
                gengo = ANCHO; year -= 2018;
            }
            // 2019年だったら，4月30日以前は平成
            else if (year == 2019) {
                if (month <= 4) {
                    gengo = HEISEI; year -= 1988;
                } else {
                    gengo = ANCHO; year -= 2018;
                }
            }
            // 1990年より先は平成
            else if (year >= 1990) {
                gengo = HEISEI; year -= 1988;
            }
            // 1989年だったら，1月7日以前は昭和
            else if (year == 1989) {
                if (month == 1 && day <= 7) {
                    gengo = SHOWA; year = 64;
                }
                else {
                    gengo = HEISEI; year = 1;
                }
            }
            // 1927年から1988年は昭和
            else if (year >= 1927) {
                gengo = SHOWA; year -= 1925;
            }
            // 1926年だったら，12月25日以降は昭和
            else if (year == 1926) {
                if (month == 12 && day >= 25) {
                    gengo = SHOWA; year = 1;
                }
                else {
                    gengo = TAISHO; year = 15;
                }
            }
            // 1913年から1925年は大正
            else if (year >= 1913) {
                gengo = TAISHO; year -= 1911;
            }
            // 1912 年だったら，7/30 以降は大正
            else if (year == 1912) {
                if (month >= 8) {
                    gengo = TAISHO; year = 1;
                }
                else if (month <= 6) {
                    gengo = MEIJI; year = 45;
                }
                else if (day >= 30) {
                    gengo = TAISHO; year = 1;
                }
                else {
                    gengo = MEIJI; year = 45;
                }
            }
            // 1911年以前は明治
            else {
                gengo = MEIJI; year -= 1867;
            }

            return String.format("%s%02d-%02d-%02d", gengo.alphabet(), year, month, day);
        }

        /**
         * 年号 -> 西暦変換.
         * @param gengoBirthday H22-7-26
         * @return mmlBirthday 2010-07-26
         */
        public static String toSeireki(String gengoBirthday) {
            String[] date = gengoBirthday.split("-");
            int year;
            int month;
            int day;

            if (date[0].length() == 4) {
                // 西暦で入ってきた場合
                year = Integer.valueOf(date[0]);
                month = Integer.valueOf(date[1]);
                day = Integer.valueOf(date[2]);

            } else {
                // 元号処理
                String gengo = date[0].substring(0, 1).toUpperCase();
                year = Integer.valueOf(date[0].substring(1));
                month = Integer.valueOf(date[1]);
                day = Integer.valueOf(date[2]);

                if (gengo.equals(MEIJI.alphabet())) {
                    year += 1867;
                } else if (gengo.equals(TAISHO.alphabet())) {
                    year += 1911;
                } else if (gengo.equals(SHOWA.alphabet())) {
                    year += 1925;
                } else if (gengo.equals(HEISEI.alphabet())) {
                    year += 1988;
                } else {
                    year += 2018;
                }
            }

            return String.format("%d-%02d-%02d", year, month, day);
        }

        /**
         * Orca 型式の数字→元号変換.
         * @param number Orca で元号を表す数字 [1,2,3,4,...]
         * @return 元号を表すアルファベット [M,T,S,H,...]
         */
        public static String numberToString(String number) {
            int num = Integer.valueOf(number);
            if (num > values().length || num < 1) { return "U"; }
            else return (values()[num].alphabet());
        }

        /**
         * 年号アルファベットを漢字に変換.
         * @param alphabet [M,T,S,H,...]
         * @return 元号漢字 [㍾,㍽,㍼,㍻,...]
         */
        public static String toKanji(String alphabet) {
            return Arrays.stream(values()).filter(value -> value.alphabet().equals(alphabet)).findAny().orElse(Gengo.DEFAULT).kanji();
        }
    }

    public static void main(String[] argv) {
        System.out.println(toNengo("2019-04-30"));
        System.out.println(toNengo("2019-05-01"));
        System.out.println(toSeireki("h01-04-30"));
        System.out.println(toSeireki("a01-05-01"));
        System.out.println(toSeireki("2019-2-25"));
        System.out.println(orcaDateToNengo("3300101"));
        System.out.println(orcaDateToNengo("4300430"));
        System.out.println(orcaDateToNengo("5010501"));
        System.out.println(nengoAlphabetToKanji("H"));
        System.out.println(nengoAlphabetToKanji("A"));
        System.out.println(claimInsuranceCodeToOrcaInsuranceCode("00"));
        System.out.println(claimInsuranceCodeToOrcaInsuranceCode("09"));
        System.out.println(claimInsuranceCodeToOrcaInsuranceCode("39"));
        System.out.println(claimInsuranceCodeToOrcaInsuranceCode("XX"));
        System.out.println(claimInsuranceCodeToOrcaInsuranceCode("Z0"));
        System.out.println(claimInsuranceCodeToOrcaInsuranceCode("060"));
    }
}

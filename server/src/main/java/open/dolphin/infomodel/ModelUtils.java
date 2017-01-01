package open.dolphin.infomodel;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

/**
 * InfoModel
 *
 * @author Minagawa,Kazushi
 */
public class ModelUtils implements IInfoModel {
    private static final long serialVersionUID = 1L;

    /**
     * MML 形式から時間を取り除いて日付だけ取り出す
     * @param mmlDate
     * @return
     */
    public static String trimTime(String mmlDate) {
        if (mmlDate == null) return null;

        int index = mmlDate.indexOf('T');
        if (index > -1) return mmlDate.substring(0, index);
        else return mmlDate;
    }

    /**
     * MML 形式から日付を取り除いて時間だけ取り出す
     * @param mmlDate
     * @return
     */
    public static String trimDate(String mmlDate) {
        if (mmlDate == null) return null;

        int index = mmlDate.indexOf('T');
        if (index > -1) return mmlDate.substring(index + 1, index + 6);// THH:mm:ss -> HH:mm
        else return mmlDate;
    }

    /**
     * mml 形式の生年月日から年齢付きの形式を作る
     * @param mmlBirthday
     * @return
     */
    public static String getAgeBirthday(String mmlBirthday) {
        String age = getAge(mmlBirthday);
        if (age == null) return null;
        return String.format("%s %s (%s)", age, AGE, toNengo(mmlBirthday));
    }

    /**
     * 年齢付きの生年月日「32.10 歳 (S50-01-01)」形式から mmlBirthday を返す
     * @param birthdayWithAge
     * @return
     */
    public static String getMmlBirthdayFromAge(String birthdayWithAge) {
        String[] s = birthdayWithAge.split("[()]");
        return toSeireki(s[1]);
    }

    /**
     * 西暦=>年号変換
     * @param mmlBirthday
     * @return nengoBirthday
     */
    public static String toNengo(String mmlBirthday) {
        int year;
        int month;
        int day;
        String nengo;

        year = Integer.valueOf(mmlBirthday.substring(0,4));
        month = Integer.valueOf(mmlBirthday.substring(5,7));
        day = Integer.valueOf(mmlBirthday.substring(8,10));

        // 1990年より先は平成
        if (year >= 1990) {
            nengo = "H"; year -= 1988;
        }
        // 1989年だったら，1月7日以前は昭和
        else if (year == 1989) {
            if (month == 1 && day <= 7) {
                nengo = "S"; year = 64;
            }
            else {
                nengo = "H"; year = 1;
            }
        }
        // 1927年から1988年は昭和
        else if (year >= 1927 && year <= 1988) {
            nengo = "S"; year -= 1925;
        }
        // 1926年だったら，12月25日以降は昭和
        else if (year == 1926) {
            if (month == 12 && day >= 25) {
                nengo = "S"; year = 1;
            }
            else {
                nengo = "T"; year = 15;
            }
        }
        // 1913年から1925年は大正
        else if (year >= 1913 && year <= 1925) {
            nengo = "T"; year -= 1911;
        }
        // 1912 年だったら，7/30 以降は大正
        else if (year == 1912) {
            if (month >= 8) {
                nengo = "T"; year = 1;
            }
            else if (month <= 6) {
                nengo = "M"; year = 45;
            }
            else if (day >= 30) {
                nengo = "T"; year = 1;
            }
            else {
                nengo = "M"; year = 45;
            }
        }
        // 1911年以前は明治
        else {
            nengo = "M"; year -= 1867;
        }

        return String.format("%s%02d-%02d-%02d", nengo, year, month, day);
    }

    /**
     * 年号=>西暦変換 H22-7-26 => 2010-07-26
     * @param nengoBirthday
     * @return mmlBirthday
     */
    public static String toSeireki(String nengoBirthday) {

        try {
            StringTokenizer st = new StringTokenizer(nengoBirthday, "-");
            String yearStr = st.nextToken();
            String monthStr = st.nextToken();
            String dateStr = st.nextToken();

            String nengo = yearStr.substring(0,1);
            int year = Integer.valueOf(yearStr.substring(1));

            switch(nengo) {
                case "M": case "m": year += 1867; break;
                case "T": case "t": year += 1911; break;
                case "S": case "s": year += 1925; break;
                case "H": case "h": year += 1988; break;
                // 西暦で入ってきた場合
                default: year = Integer.valueOf(yearStr);
            }

            if (monthStr.length() == 1) monthStr = "0" + monthStr;
            if (dateStr.length() == 1) dateStr = "0" + dateStr;

            return String.format("%d-%s-%s", year, monthStr, dateStr);

        } catch (Exception ex) {
            System.out.println("ModelUtils.java: " + ex);
            return null;
        }
    }
    /**
     * ORCA 形式を年号形式に 4220726 => h22-07-26
     * @param orcaBirthday
     * @return
     */
    public static String OrcaDateToNengo(String orcaBirthday) {
        //元号
        String nengo = "";
        switch(orcaBirthday.substring(0, 1)) {
            case "1": nengo = "m"; break;
            case "2": nengo = "t"; break;
            case "3": nengo = "s"; break;
            case "4": nengo = "h"; break;
        }
        //年
        String y = orcaBirthday.substring(1, 3);
        String m = orcaBirthday.substring(3, 5);
        String d = orcaBirthday.substring(5, 7);

        return nengo + y + "-" + m + "-" + d;
    }

    /**
     * 年齢を作る
     * @param mmlBirthday
     * @return
     */
    public static String getAge(String mmlBirthday) {

        try {
            GregorianCalendar gc1 = getCalendar(mmlBirthday);
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

        } catch (Exception ex) {
            System.out.println("ModelUtils.java: " + ex);
            return null;
        }
    }

    /**
     * mmlDate 形式から GregorianCalendar を作る
     * @param mmlDate
     * @return
     */
    public static GregorianCalendar getCalendar(String mmlDate) {

        Date date;
        if (mmlDate.contains("T")) date = getDateTimeAsObject(mmlDate);
        else date = getDateAsObject(mmlDate);

        if (date == null) return null;

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(date.getTime());
        return gc;
    }

    /**
     * 時間なしの mmlDate 形式から Date を作る
     * @param mmlDate
     * @return
     */
    public static Date getDateAsObject(String mmlDate) {
        if (mmlDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_WITHOUT_TIME);
                return sdf.parse(mmlDate);
            } catch (ParseException e) {
                System.out.println("ModelUtils.java: " + e);
                //e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 時間付きの mmlDate 形式から Date を作る
     * @param mmlDate
     * @return
     */
    public static Date getDateTimeAsObject(String mmlDate) {
        if (mmlDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
                return sdf.parse(mmlDate);

            } catch (ParseException e) {
                System.out.println("ModelUtils.java: " + e);
                //e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Date から時間なしの mmlDate 形式を作る
     * @param date
     * @return
     */
    public static String getDateAsString(Date date) {
        return getDateAsFormatString(date, DATE_WITHOUT_TIME);
    }

    /**
     * Date から時間付きの mmlDate 形式を作る
     * @param date
     * @return
     */
    public static String getDateTimeAsString(Date date) {
        return getDateAsFormatString(date, ISO_8601_DATE_FORMAT);
    }

    /**
     * Date から format で指定した形式の日付文字列を作る
     * @param date
     * @param format
     * @return
     */
    public static String getDateAsFormatString(Date date, String format) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * male => 男　変換
     * @param gender
     * @return
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

    public boolean isValidModel() {
        return true;
    }

    public static String[] splitDiagnosis(String diagnosis) {
        return (diagnosis == null)? null : diagnosis.split("\\s*,\\s*");
    }

    public static String getDiagnosisName(String hasAlias) {
        String[] splits = splitDiagnosis(hasAlias);
        return (splits != null && splits.length == 2 && splits[0] != null) ? splits[0] : hasAlias;
    }

    public static String getDiagnosisAlias(String hasAlias) {
        String[] splits = splitDiagnosis(hasAlias);
        return (splits != null && splits.length == 2 && splits[1] != null) ? splits[1] : null;
    }

    /**
     * PVTDelegater で使う date を作成する（WaitingListImpl から移動
     * date[0] = today, date[1] = AppodateFrom, date[2] = AppodateTo
     * @param date
     * @return
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
     * Object を beanBytes にエンコードする
     * @param bean
     * @return
     */
    public static byte[] xmlEncode(Object bean)  {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try (XMLEncoder e = new XMLEncoder(new BufferedOutputStream(bo))) {
            e.writeObject(bean);
        }
        return bo.toByteArray();
    }

    /**
     * beanBytes をデコードする
     * @param bytes
     * @return
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
     * xml からテキストを取り出す
     * @param xml
     * @return
     */
    public static String extractText(String xml) {
        StringBuilder buf = new StringBuilder();
        String head[] = xml.split("<text>");
        for (String str : head) {
            String tail[] = str.split("</text>");
            if (tail.length == 2) { buf.append(tail[0].trim()); }
        }
        return buf.toString();
    }

    /**
     * バイナリの健康保険データをオブジェクトにデコードする.
     * @param insurances
     * @return
     */
    public static List<PVTHealthInsuranceModel> decodeHealthInsurance(List<HealthInsuranceModel> insurances) {

        List<PVTHealthInsuranceModel> ret = new ArrayList<>();

        if (insurances != null) {
            insurances.forEach(model -> {
                try {
                    // byte[] を XMLDecord
                    PVTHealthInsuranceModel hModel = (PVTHealthInsuranceModel)xmlDecode(model.getBeanBytes());
                    ret.add(hModel);
                } catch (RuntimeException e) {
                    e.printStackTrace(System.err);
                }
            });
        }
        return ret;
    }

    /**
     * treeBytes を treeXml に変換して返す.
     * @param treeBytes
     * @return
     */
    public static String toTreeXml(byte[] treeBytes) {
        try {
            return new String(treeBytes, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * treeXml を treeBytes に変換して返す.
     * @param treeXml
     * @return
     */
    public static byte[] toTreeBytes(String treeXml) {
        try {
            return treeXml.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}

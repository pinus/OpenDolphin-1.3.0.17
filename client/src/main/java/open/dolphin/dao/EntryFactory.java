package open.dolphin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSet から各種 Entry を作る
 * @author pns
 */
public class EntryFactory {

    /**
     * tbl_tensu を検索した ResultSet から OrcaEntry を作る
     * @param rs
     * @return
     */
    public static OrcaEntry createTensuEntry(ResultSet rs) throws SQLException {
        OrcaEntry entry = new OrcaEntry();

        entry.setCode(rs.getString(1)); // srycd
        entry.setName(rs.getString(2)); // name
        entry.setKanaName(rs.getString(3)); // kananame
        entry.setStartDate(rs.getString(4)); // yukostymd
        entry.setEndDate(rs.getString(5)); // yukoedymd
        entry.setFormalName(rs.getString(6)); // formalname
        entry.setUnit(rs.getString(7)); // taniname
        entry.setTensikibetu(rs.getString(8)); // tensikibetu
        entry.setTen(rs.getString(9)); // ten
        entry.setNyugaitekkbn(rs.getString(10)); // nyugaitekkbn
        entry.setRoutekkbn(rs.getString(11)); // routekkbn
        entry.setClaimClassCode(rs.getString(12)); // srysyukbn
        entry.setHospsrykbn(rs.getString(13)); // hospsrykbn
        entry.setYakkakjncd(rs.getString(14)); // yakkakjncd
        entry.setYkzkbn(rs.getString(15)); // ykzkbn

        // 日付のフォーマット確認
        if (!entry.getStartDate().matches("[0-9]*")) entry.setStartDate("00000000");
        if (!entry.getEndDate().matches("[0-9]*")) entry.setEndDate("99999999");

        return entry;
    }

    /**
     * tbl_byomei を検索した ResultSet から OrcaEntry を作る
     * @param rs
     * @return
     */
    public static OrcaEntry createByomeiEntry(ResultSet rs) throws SQLException {
        OrcaEntry entry = new OrcaEntry();

        entry.setCode(rs.getString(1)); // byomeicd
        entry.setName(rs.getString(2)); // byomei
        entry.setKanaName(rs.getString(3)); // byomeikana
        entry.setStartDate(rs.getString(4)); // syusaiymd
        entry.setEndDate(rs.getString(5)); // haisiymd
        entry.setIcd10(rs.getString(6)); // icd10 or icd10_1

        // 日付のフォーマット確認
        if (!entry.getStartDate().matches("[0-9]*")) entry.setStartDate("00000000");
        if (!entry.getEndDate().matches("[0-9]*")) entry.setEndDate("99999999");

        return entry;
    }

    /**
     * tbl_inputcd を検索した ResultSet から OrcaEntry を作る
     * @param rs
     * @return
     * @throws SQLException
     */
    public static OrcaEntry createInputCdEntry(ResultSet rs) throws SQLException {
        OrcaEntry entry = new OrcaEntry();

        entry.setInputCd(rs.getString(1)); // inputcd
        entry.setDspName(rs.getString(2)); // dspname

        // inputcd の桁を 6桁にそろえる
        String cd = entry.getInputCd();
        if (cd.length() > 6) {
            cd = cd.substring(0, 6);
            entry.setInputCd(cd);
        }

        return entry;
    }

    /**
     * tbl_inputset を検索した ResultSet から OrcaEntry を作る
     * @param rs
     * @return
     * @throws SQLException
     */
    public static OrcaEntry createInputSetEntry(ResultSet rs) throws SQLException {
        OrcaEntry entry = new OrcaEntry();

        entry.setInputCd(rs.getString(1)); // .210 616130532 ...
        entry.setSuryo1(rs.getFloat(2)); // suryo1
        entry.setKaisu(rs.getInt(3)); // bundle 数
        entry.setComment(rs.getString(4)); // coment

        return entry;
    }

    /**
     * tbl_ptbyomei を検索した ResultSet から OrcaEntry を作る
     * @param rs
     * @return
     * @throws SQLException
     */
    public static OrcaEntry createPtByomeiEntry(ResultSet rs) throws SQLException {
        OrcaEntry entry = new OrcaEntry();

        entry.setStartDate(rs.getString(1)); // sryymd
        entry.setCode(rs.getString(2)); // khnbyomeicd
        entry.setTenkiKbn(rs.getString(3));// tenkikbn
        entry.setEndDate(rs.getString(4)); // tenkiymd
        entry.setName(rs.getString(5)); // byomei

        // 日付のフォーマット確認
        if (!entry.getStartDate().matches("[0-9]*")) entry.setStartDate("00000000");
        if (!entry.getEndDate().matches("[0-9]*")) entry.setEndDate("99999999");

        return entry;
    }

    /**
     * tbl_syskanri を検索した ResultSet から OrcaEntry を作る
     * @param rs
     * @return
     * @throws SQLException
     */
    public static OrcaEntry createSyskanriEntry(ResultSet rs) throws SQLException {
        OrcaEntry entry = new OrcaEntry();

        entry.setInputCd(rs.getString(1));      // kanricd
        entry.setCode(rs.getString(2));         // kbncd
        entry.setComment(rs.getString(3));      // kanritbl

        return entry;
    }

    public static OrcaEntry createWksryactEntry(ResultSet rs) throws SQLException {
        OrcaEntry entry = new OrcaEntry();

        entry.setCode(rs.getString(1));     // karte_key
        entry.setComment(rs.getString(2));  // mod_flg

        return entry;
    }
}

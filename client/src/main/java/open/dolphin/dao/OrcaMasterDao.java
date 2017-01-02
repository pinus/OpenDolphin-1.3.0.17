package open.dolphin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import open.dolphin.client.DiagnosisDocument;
import open.dolphin.infomodel.*;
import open.dolphin.order.ClaimConst;
import open.dolphin.order.MMLTable;
import open.dolphin.project.Project;
import org.apache.commons.lang.StringUtils;

/**
 * ORCA のテーブルから情報を取得する
 * @author pns
 */
public class OrcaMasterDao extends OrcaDao {

    private String today;

    public OrcaMasterDao () {
        super();

        // 病名テーブルはバージョンの違いに対応必要なので，ここで sql を作成する  // 4.5 はもう使わないのでコメントアウト
        //if (ORCA_DB_VER_45.equals(getDbVersion())) {
        //    SQL_TBL_BYOMEI = SQL_TBL_BYOMEI.replace("icd10_1", "icd10");
        //    SQL_TBL_BYOMEI_BY_MULTIPLE_CODES = SQL_TBL_BYOMEI_BY_MULTIPLE_CODES.replace("icd10_1", "icd10");
        //}

        // ORCA 形式の今日の日付
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        today = sdf.format(new Date());
    }

    /**
     * ワーク診療行為（中途終了データ）から UUID と画面展開フラグ（1=画面展開されている）を返す
     * OrcaEntry の code(UUID)，comment(フラグ) を間借りする
     */
    private static final String SQL_TBL_WKSRYACT = "select karte_key, mod_flg from tbl_wksryact "
            + "where ptid = (select ptid from tbl_ptnum where ptnum = ?)";

    public List<OrcaEntry> getWksryactEntries(String ptId) {
        List<OrcaEntry> ret = new ArrayList<OrcaEntry>();

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(SQL_TBL_WKSRYACT);
            ps.setString(1, ptId);

            // set value from ResultSet
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ret.add(EntryFactory.createWksryactEntry(rs));

            rs.close();

        } catch (Exception e) {
            processError(e);
        }
        closeStatement(ps);
        closeConnection(con);

        return ret;
    }

    /**
     * システム管理テーブル検索クエリ
     */
    private static final String SQL_TBL_SYSKANRI = "select kanricd, kbncd, kanritbl from tbl_syskanri where kanricd = ?";

    /**
     * TBL_SYSKANRI から検索
     * kanricd = Inputcd, kbncd = Code, kanritbl = Comment に格納
     * @param kanricd
     * @return
     */
    public List<OrcaEntry> getSyskanriEntries(String kanricd) {
        List<OrcaEntry> ret = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(SQL_TBL_SYSKANRI);
            ps.setString(1, kanricd);

            // set value from ResultSet
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ret.add(EntryFactory.createSyskanriEntry(rs));

            rs.close();

        } catch (Exception e) {
            processError(e);
        }
        closeStatement(ps);
        closeConnection(con);

        return ret;
    }

    /**
     * 点数テーブル tbl_tensu 検索クエリ
     */
    private static final String SQL_TBL_TENSU = "select srycd, name, kananame, yukostymd, yukoedymd, "
            + "formalname, taniname, tensikibetu, ten, nyugaitekkbn, routekkbn, srysyukbn, hospsrykbn, yakkakjncd, ykzkbn "
            + "from tbl_tensu where srycd ~ ? or name ~ ? or kananame ~ ? order by srycd";
    /**
     * TBL_TENSU からキーワードを検索
     * @param keyword
     * @return
     */
    public List<OrcaEntry> getTensuEntries(String keyword) {
        List<OrcaEntry> ret = new ArrayList<>();
        List<OrcaEntry> expired = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(SQL_TBL_TENSU);
            ps.setString(1, keyword); // srycd
            ps.setString(2, keyword); // name
            ps.setString(3, keyword); // kananame

            // set value from ResultSet
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrcaEntry entry = EntryFactory.createTensuEntry(rs);
                 if (today.compareTo(entry.getEndDate()) <= 0) {
                    ret.add(entry);
                } else {
                    expired.add(entry);
                }
           }
            rs.close();

        } catch (Exception e) {
            processError(e);
        }
        closeStatement(ps);
        closeConnection(con);

        ret.addAll(expired);
        return ret;
    }

    /**
     * 病名テーブル tbl_byomei 検索クエリ
     * コンストラクタで version の違いを吸収する
     */
    private static String SQL_TBL_BYOMEI = "select byomeicd, byomei, byomeikana, syusaiymd, haisiymd, icd10_1 from tbl_byomei "
            + "where (byomei ~ ? or byomeikana ~ ? or byomeicd ~ ?) and haisiymd >= ? order by icd10_1, byomeicd";
    private static String SQL_TBL_BYOMEI_BY_MULTIPLE_CODES = "select byomeicd, byomei, byomeikana, syusaiymd, haisiymd, icd10_1 from tbl_byomei "
            + "where byomeicd in (?) order by icd10_1, byomeicd";

    /**
     * TBL_BYOMEI からキーワードを検索
     * @param keyword
     * @return
     */
    public List<OrcaEntry> getByomeiEntries(String keyword) {
        List<OrcaEntry> ret = new ArrayList<>();
        List<OrcaEntry> expired = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(SQL_TBL_BYOMEI);
            ps.setString(1, keyword); // byomei
            ps.setString(2, keyword); // byomeikama
            ps.setString(3, keyword); // byomeicd
            ps.setString(4, "00000000"); // haisiymd

            // set value from ResultSet
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrcaEntry entry = EntryFactory.createByomeiEntry(rs);
                 if (today.compareTo(entry.getEndDate()) <= 0) {
                    ret.add(entry);
                } else {
                    expired.add(entry);
                }
            }
            rs.close();

        } catch (Exception e) {
            processError(e);
        }
        closeStatement(ps);
        closeConnection(con);

        ret.addAll(expired);
        return ret;
    }

    /**
     * 病名コードのリストに対応する OrcaEntry を返す
     * DiagnosisDocument, DiagnosisTablePanel から呼ばれる
     * @param codes
     * @return DiseaseEntry のリスト
     */
    public List<OrcaEntry> getByomeiEntries(String[] codes) {

        List<OrcaEntry> ret = new ArrayList<>();
        if (codes == null || codes.length == 0) return ret;

        String sql = (codes.length == 1)?
                SQL_TBL_BYOMEI_BY_MULTIPLE_CODES :
                SQL_TBL_BYOMEI_BY_MULTIPLE_CODES.replace("?", "?" + StringUtils.repeat(",?", codes.length - 1));
        //System.out.println("sql=" + sql);

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql);

            int n=1;
            for (String s : codes) ps.setString(n++, s);
            //ps.setArray(1, srycdSet); ってやってみたけどダメだった
            //System.out.println("ps= " + ps);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) ret.add(EntryFactory.createByomeiEntry(rs));

            rs.close();

        } catch (Exception e) {
            processError(e);
        }
        closeStatement(ps);
        closeConnection(con);
        return ret;
    }

    /**
     * inputcd テーブル tbl_inputcd 検索クエリ
     */
    private static final String SQL_TBL_INPUTCD = "select inputcd, dspname from tbl_inputcd "
            + "where hospnum = ? and (inputcd like 'P%' or inputcd like 'S%') order by inputcd";

    /**
     * TBL_INPUTCD を検索してから入力セット（約束処方，診療セット）のリストを返す
     * OrcaTree から呼ばれる
     * @return 入力セットコード(inputcd)の昇順リスト
     */
    public List<OrcaEntry> getOrcaInputCdList() {
        List<OrcaEntry> ret = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(SQL_TBL_INPUTCD);
            ps.setInt(1, getHospNum()); // hospnum

            // set value from ResultSet
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ret.add(EntryFactory.createInputCdEntry(rs));

            rs.close();

        } catch (Exception e) {
            processError(e);
        }
        closeStatement(ps);
        closeConnection(con);

        return ret;
    }

    /**
     * inputset テーブル tbl_inputset 検索クエリ
     */
    private static final String SQL_TBL_INPUTSET = "select inputcd, suryo1, kaisu, coment from tbl_inputset "
            + "where hospnum = ? and setcd = ? order by setseq";

    /**
     * StampInfo を元に TBL_INPUTSET，TBL_TENSU を検索してスタンプの実体を作る
     * @param stampInfo
     * @return
     */
    public List<ModuleModel> getStamp(ModuleInfoBean stampInfo) {
        List<ModuleModel> ret = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();

            // tbl_inputset の検索
            List<OrcaEntry> inputsetEntries = new ArrayList<>();
            ps = con.prepareStatement(SQL_TBL_INPUTSET);
            ps.setInt(1, getHospNum());
            ps.setString(2, stampInfo.getStampId());

            // set value from ResultSet
            ResultSet rs = ps.executeQuery();
            while (rs.next()) inputsetEntries.add(EntryFactory.createInputSetEntry(rs));
            rs.close();
            closeStatement(ps);

            // OrcaEntries を解釈して，stamp に対応した OrcaEntries に分割する
            // 分割した Entries を入れる list
            List<List<OrcaEntry>> lists = new ArrayList<>();
            // Entries を入れておくバッファ
            List<OrcaEntry> entries = new ArrayList<>();

            // １行ずつ解釈して，とってきた inputsetEntries をスタンプ単位に分離して lists に格納
            boolean onAdmin = false;
            for(OrcaEntry e : inputsetEntries) {
                String inputcd = e.getInputCd();

                // 用法の場合 onAdmin をセット
                if (inputcd.startsWith(ClaimConst.ADMIN_CODE_START)) {
                    onAdmin = true;

                } else {
                    // admin でなくなった行があったらそこで分離する
                    if (onAdmin) {
                        onAdmin = false;
                        // バッファの更新
                        lists.add(entries);
                        entries = new ArrayList<OrcaEntry>();

                    } else if (inputcd.startsWith(".") && !entries.isEmpty()) {
                        // .400 のパターンがきたら，そこでバッファ更新
                        lists.add(entries);
                        entries = new ArrayList<OrcaEntry>();
                    }
                }
                entries.add(e);
            }
            // entries を登録
            lists.add(entries);

            // その情報を元に，tbl_tensu を検索して ModuleModel を作成する
            for (List<OrcaEntry> list : lists) {
                ModuleModel stamp = createStamp(stampInfo, list);
                ret.add(stamp);
            }

        } catch (Exception e) {
            processError(e);
        }

        closeConnection(con);

        return ret;
    }

    /**
     * inputset の srycd をキーに tbl_tensu を検索するクエリ
     */
    private static final String SQL_TBL_TENSU_2 = "select srysyukbn, name, taniname, ykzkbn from tbl_tensu "
            //+ "where hospnum = ? and srycd = ? and yukoedymd = '99999999'";
            + "where hospnum = ? and srycd = ?"; // ↑ だと，有効期限が設定された薬剤が取り込めない thnx to inaba-sensei
    /**
     * OrcaEntry の srycd を元にスタンプを作る
     * @param entries
     * @return
     */
    private ModuleModel createStamp(ModuleInfoBean stampInfo, List<OrcaEntry> inputsetEntries) throws SQLException, Exception {

        ModuleModel stamp = null;
        ClaimBundle bundle = null;
        PreparedStatement ps = null;
        Connection con = getConnection();

        for (OrcaEntry inputsetEntry : inputsetEntries) {
            String inputcd = inputsetEntry.getInputCd();
            String stampName = stampInfo.getStampName();

            // inputcd が .210 のパターン
            if (inputcd.startsWith(".")) {
                stamp = createStampModel(stampName, inputcd);
                if (stamp != null) {
                    bundle = (BundleDolphin) stamp.getModel();
                }

            // それ以外は inputcd から tbl_tensu 検索
            // この段階では，既に stamp も bundle もできているはずである
            } else {
                ps = con.prepareStatement(SQL_TBL_TENSU_2);
                ps.setInt(1, getHospNum());
                ps.setString(2, inputcd);
                ResultSet rs = ps.executeQuery();

                // マッチするものは１つしかないはず
                if (rs.next()) {
                    String srysyukbn = rs.getString(1);
                    String name = rs.getString(2);
                    String taniname = rs.getString(3);
                    String ykzKbn = rs.getString(4);

                    ClaimItem item = new ClaimItem();
                    item.setCode(inputcd);
                    item.setName(name);
                    item.setClassCodeSystem(ClaimConst.SUBCLASS_CODE_ID);

                    // 数量の小数点以下処理
                    String dose = String.valueOf(inputsetEntry.getSuryo1());
                    if (dose.endsWith(".0")) dose = dose.substring(0, dose.length() - 2);
                    item.setNumber(dose);

                    String kbn = null;
                    // 手技の場合
                    if (inputcd.startsWith(ClaimConst.SYUGI_CODE_START)) {
                        item.setClassCode(String.valueOf(ClaimConst.SYUGI));
                        kbn = srysyukbn;

                    // 薬剤の場合
                    } else if (inputcd.startsWith(ClaimConst.YAKUZAI_CODE_START)) {
                        item.setClassCode(String.valueOf(ClaimConst.YAKUZAI));
                        item.setNumberCode(ClaimConst.YAKUZAI_TOYORYO);
                        item.setNumberCodeSystem(ClaimConst.NUMBER_CODE_ID);
                        item.setUnit(taniname);
                        kbn = ykzKbn.equals(ClaimConst.YKZ_KBN_NAIYO)?
                                    ClaimConst.RECEIPT_CODE_NAIYO : ClaimConst.RECEIPT_CODE_GAIYO;

                    // 材料の場合
                    } else if (inputcd.startsWith(ClaimConst.ZAIRYO_CODE_START)) {
                        item.setClassCode(String.valueOf(ClaimConst.ZAIRYO));
                        item.setNumberCode(ClaimConst.ZAIRYO_KOSU);
                        item.setNumberCodeSystem(ClaimConst.NUMBER_CODE_ID);
                        item.setUnit(taniname);
                        kbn = "999";

                    // 用法の場合
                    } else if (inputcd.startsWith(ClaimConst.ADMIN_CODE_START)) {
                        item.setClassCode(String.valueOf(ClaimConst.YAKUZAI));
                        item.setNumber("");
                        kbn = "220";

                    // 放射線部位の場合
                    } else if (inputcd.startsWith(ClaimConst.RBUI_CODE_START)) {
                        item.setClassCode(String.valueOf(ClaimConst.SYUGI));
                        kbn = "700";

                    // コメントコードの場合
                    } else if (inputcd.startsWith(ClaimConst.COMMENT_CODE_START)) {
                        item.setClassCode(String.valueOf(ClaimConst.SYUGI));
                        item.setName(inputsetEntry.getComment());
                        kbn = "800";

                    // どれでもない場合
                    } else {
                        System.out.println("OrcaMasterDao: no srysykbn found : inputcd = " + inputcd);
                        continue;
                    }

                    // もし stamp ができていなければスタンプ作成
                    if (bundle == null) {
                        stamp = createStampModel(stampName, kbn);

                        if (stamp != null) {
                            bundle = (ClaimBundle) stamp.getModel();
                        }
                    }

                    if (bundle != null) {
                        //if (inputcd.startsWith(ClaimConst.ADMIN_CODE_START)) {
                        // 部位（001000800-999）は admin に入れない
                        if (inputcd.matches("^001000[0-7].*")) {

                            // 頓用処理
                            if (inputcd.startsWith("0010005")) {
                                bundle.setClassCode(IInfoModel.RECEIPT_CODE_TONYO);
                            }

                            bundle.setAdmin(name);
                            bundle.setAdminCode(inputcd);
                            bundle.setBundleNumber(String.valueOf(inputsetEntry.getKaisu()));
                        } else {
                            bundle.addClaimItem(item);
                        }
                    }
                }

                rs.close();
                closeStatement(ps);
            } // end if
        } // end for

        return stamp;
    }

    /**
     * スタンプ名，診療区分から Stamp のひな形を生成する.
     * @param stampName Stamp名
     * @param kbn 診療区分コード（３桁のコード kbn）
     * @return Stamp
     */
    private ModuleModel createStampModel(String stampName, String srysyukbn) {

        if (srysyukbn == null) return null;

        // .210 のパターンなら，. を削除
        if (srysyukbn.startsWith(".")) srysyukbn = srysyukbn.substring(1);
        // kbn はコードの上位３桁
        if (srysyukbn.length() > 3) srysyukbn = srysyukbn.substring(0, 3);

        // ModuleModel 作成
        ModuleModel stamp = new ModuleModel();

        ModuleInfoBean stampInfo = stamp.getModuleInfo();
        stampInfo.setStampName(stampName);
        stampInfo.setStampRole(IInfoModel.ROLE_P);

        // kbn の範囲からエンティティーを取得
        String entity = ClaimConst.getEntity(srysyukbn);
        stampInfo.setEntity(entity);

        // entity の名前を取得
        String orderName = ClaimConst.EntityNameMap.get(entity);

        // ClaimBundle 作成
        ClaimBundle bundle;

        // 処方（2xx） の場合，BundleMed にして院内／院外をセット
        if (srysyukbn.startsWith("2")) {
            bundle = new BundleMed();
            String inOut = Project.getPreferences().getBoolean(Project.RP_OUT, true)?
                    ClaimConst.EXT_MEDICINE : ClaimConst.IN_MEDICINE;
            bundle.setMemo(inOut);
            ((BundleMed)bundle).setOrderName(orderName);

        } else {
        // 処方以外は BundleDolphin
            bundle = new BundleDolphin();
            ((BundleDolphin)bundle).setOrderName(orderName);
        }

        bundle.setClassCode(srysyukbn);
        bundle.setClassCodeSystem(ClaimConst.CLASS_CODE_ID);
        bundle.setClassName(MMLTable.getClaimClassCodeName(srysyukbn));
        bundle.setBundleNumber("1");

        stamp.setModel(bundle);

        return stamp;
    }

    /**
     * 患者番号テーブル tbl_ptnum, tbl_ptbyomei 検索クエリ
     * order by ? は使えない
     */
    private static final String SQL_TBL_PTNUM = "select ptid from tbl_ptnum where hospnum = ? and ptnum = ?";
    private static final String SQL_TBL_PTBYOMEI_ASC = "select sryymd, khnbyomeicd, tenkikbn, tenkiymd, byomei from tbl_ptbyomei "
                + "where hospnum=? and ptid=? and sryymd >= ? and sryymd <= ? order by sryymd asc";
    private static final String SQL_TBL_PTBYOMEI_DESC = "select sryymd, khnbyomeicd, tenkikbn, tenkiymd, byomei from tbl_ptbyomei "
                + "where hospnum=? and ptid=? and sryymd >= ? and sryymd <= ? order by sryymd desc";

    /**
     * TBL_PTBYOMEI を検索して RegisteredDiagnosisModel を作る
     * @param patientId
     * @param from
     * @param to
     * @param ascend
     * @return
     */
    public List<RegisteredDiagnosisModel> getOrcaDisease(String patientId, String from, String to, Boolean ascend) {
        List<RegisteredDiagnosisModel> ret = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;

        try {
            int ptid = 0; // ORCA の ptid

            con = getConnection();
            ps = con.prepareStatement(SQL_TBL_PTNUM);
            ps.setInt(1, getHospNum()); // hospnum
            ps.setString(2, patientId);

            // ORCA の ptid は bigint
            ResultSet rs = ps.executeQuery();
            if (rs.next()) ptid = rs.getInt(1);
            rs.close();

            if (ptid == 0) return null;

            ps = con.prepareStatement(ascend? SQL_TBL_PTBYOMEI_ASC : SQL_TBL_PTBYOMEI_DESC);
            ps.setInt(1, 1); //hospnum
            ps.setInt(2, ptid); // ptid
            ps.setString(3, from);
            ps.setString(4, to);

            rs = ps.executeQuery();
            List<OrcaEntry> ptbyomeiEntries = new ArrayList<>();
            while (rs.next()) ptbyomeiEntries.add(EntryFactory.createPtByomeiEntry(rs));

            for (OrcaEntry entry : ptbyomeiEntries) {
                RegisteredDiagnosisModel rd = new RegisteredDiagnosisModel();
                // 病名コード
                rd.setDiagnosisCode(entry.getCode());
                // 病名
                rd.setDiagnosis(entry.getName());
                // 開始日
                rd.setStartDate(toDolphinDateStr(entry.getStartDate()));
                // 転帰
                DiagnosisOutcomeModel om = new DiagnosisOutcomeModel();
                om.setOutcomeDesc(toDolphinOutcome(entry.getTenkiKbn()));
                rd.setDiagnosisOutcomeModel(om);
                // 転帰日
                rd.setEndDate(toDolphinDateStr(entry.getEndDate()));
                // ORCA 病名識別
                rd.setStatus(DiagnosisDocument.ORCA_RECORD);

                ret.add(rd);
            }
            rs.close();

        } catch (Exception e) {
            processError(e);
        }
        closeStatement(ps);
        closeConnection(con);

        return ret;
    }

    /**
     * ORCA日付（20120401）を MMLフォーマット（2012-04-01）に変換
     * @param orcaDate
     * @return
     */
    public static String toDolphinDateStr(String orcaDate) {
        if (orcaDate == null || ! orcaDate.matches("[0-9]+")) return null;

        StringBuilder sb = new StringBuilder();
        sb.append(orcaDate.substring(0, 4));
        sb.append("-");
        sb.append(orcaDate.substring(4, 6));
        sb.append("-");
        sb.append(orcaDate.substring(6, 8));

        return sb.toString();
    }

    /**
     * ORCA転帰を Dolphin 転帰に変換
     * @param orcaOutcome
     * @return
     */
    private String toDolphinOutcome(String orcaOutcome) {

        if ("1".equals(orcaOutcome)) {
            return IInfoModel.ORCA_OUTCOME_RECOVERED;

        } else if ("2".equals(orcaOutcome)) {
            return IInfoModel.ORCA_OUTCOME_DIED;

        } else if ("3".equals(orcaOutcome)) {
            return IInfoModel.ORCA_OUTCOME_END;

        } else if ("8".equals(orcaOutcome)) {
            return IInfoModel.ORCA_OUTCOME_TRANSFERED;

        } else {
            return null;
        }
    }
}

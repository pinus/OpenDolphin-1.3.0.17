package open.dolphin.orca;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;

/**
 * ORCA のデータベース検索した情報を入れておくクラス.
 *
 * @author pns
 */
public class OrcaEntry implements Comparable {
    /**
     * 今日の日付
     */
    private static final String refDate;

    static {
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        refDate = f.format(gc.getTime());
    }
    // tbl_tensu, tbl_byome 共通項目
    /**
     * 診療行為コード srycd (or byomeicd)
     */
    private String code;
    /**
     * 漢字名称 name (or byomei)
     */
    private String name;
    /**
     * カナ名称 kananame (or byomeikana)
     */
    private String kananame;
    /**
     * 有効開始年月日 yukostymd (or syusaihymd)
     */
    private String startDate;
    /**
     * 有効終了年月日 yukoedymd (or haishiymd)
     */
    private String endDate;
    /**
     * 正式名称 formalname
     */
    private String formalname;
    /**
     * 単位名称 taniname
     */
    private String unit;
    /**
     * 点数識別 tensikibetu
     */
    private String tensikibetu;
    /**
     * 点数 ten
     */
    private String ten;
    /**
     * 入外適用区分 nyugaitekkbn
     */
    private String nyugaitekkbn;
    /**
     * 後期高齢者適用区分 routekkbn
     */
    private String routekkbn;
    /**
     * 診療種別区分 srysyukbn
     */
    private String claimClassCode;
    /**
     * 病院診療所区分 hospsrykbn
     */
    private String hospsrykbn;
    /**
     * 薬価基準コード yakkakjncd
     */
    private String yakkakjncd;
    /**
     * 薬剤区分 ykzkbn
     */
    private String ykzkbn;
    /**
     * ICD10 icd10(4.5) or icd10_1(4.6)
     */
    private String icd10;

    // tbl_inputcd
    /**
     * 入力コード（tbl_inputset）と共通
     */
    private String inputCd;
    /**
     * 表示名
     */
    private String dspName;

    // tbl_inputset
    private float suryo1;
    private int kaisu;
    private String coment;

    // tbl_ptbyomei
    private String tenkiKbn;

    public OrcaEntry() {
    }

    /**
     * 診療行為コード srycd (or byomeicd)
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * 診療行為コード srycd (or byomeicd)
     *
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 漢字名称 name (or byomei)
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 漢字名称 name (or byomei)
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * カナ名称 kananame (or byomeikana)
     *
     * @return the kananame
     */
    public String getKananame() {
        return kananame;
    }

    /**
     * カナ名称 kananame (or byomeikana)
     *
     * @param kananame the kananame to set
     */
    public void setKanaName(String kananame) {
        this.kananame = kananame;
    }

    /**
     * 有効開始年月日 yukostymd (or syusaihymd)
     *
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 有効開始年月日 yukostymd (or syusaihymd)
     *
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 有効終了年月日 yukoedymd (or haishiymd)
     *
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 有効終了年月日 yukoedymd (or haishiymd)
     *
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 正式名称 formalname
     *
     * @return the formalname
     */
    public String getFormalname() {
        return formalname;
    }

    /**
     * 正式名称 formalname
     *
     * @param formalname the formalname to set
     */
    public void setFormalName(String formalname) {
        this.formalname = formalname;
    }

    /**
     * 単位名称 taniname
     *
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 単位名称 taniname
     *
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 点数識別 tensikibetu
     *
     * @return the tensikibetu
     */
    public String getTensikibetu() {
        return tensikibetu;
    }

    /**
     * 点数識別 tensikibetu
     *
     * @param tensikibetu the tensikibetu to set
     */
    public void setTensikibetu(String tensikibetu) {
        this.tensikibetu = tensikibetu;
    }

    /**
     * 点数 ten
     *
     * @return the ten
     */
    public String getTen() {
        return ten;
    }

    /**
     * 点数 ten
     *
     * @param ten the ten to set
     */
    public void setTen(String ten) {
        this.ten = ten;
    }

    /**
     * 入外適用区分 nyugaitekkbn
     *
     * @return the nyugaitekkbn
     */
    public String getNyugaitekkbn() {
        return nyugaitekkbn;
    }

    /**
     * 入外適用区分 nyugaitekkbn
     *
     * @param nyugaitekkbn the nyugaitekkbn to set
     */
    public void setNyugaitekkbn(String nyugaitekkbn) {
        this.nyugaitekkbn = nyugaitekkbn;
    }

    /**
     * 後期高齢者適用区分 routekkbn
     *
     * @return the routekkbn
     */
    public String getRoutekkbn() {
        return routekkbn;
    }

    /**
     * 後期高齢者適用区分 routekkbn
     *
     * @param routekkbn the routekkbn to set
     */
    public void setRoutekkbn(String routekkbn) {
        this.routekkbn = routekkbn;
    }

    /**
     * 診療種別区分 srysyukbn
     *
     * @return the claimClassCode
     */
    public String getClaimClassCode() {
        return claimClassCode;
    }

    /**
     * 診療種別区分 srysyukbn
     *
     * @param claimClassCode the claimClassCode to set
     */
    public void setClaimClassCode(String claimClassCode) {
        this.claimClassCode = claimClassCode;
    }

    /**
     * 病院診療所区分 hospsrykbn
     *
     * @return the hospsrykbn
     */
    public String getHospsrykbn() {
        return hospsrykbn;
    }

    /**
     * 病院診療所区分 hospsrykbn
     *
     * @param hospsrykbn the hospsrykbn to set
     */
    public void setHospsrykbn(String hospsrykbn) {
        this.hospsrykbn = hospsrykbn;
    }

    /**
     * 薬価基準コード yakkakjncd
     *
     * @return the yakkakjncd
     */
    public String getYakkakjncd() {
        return yakkakjncd;
    }

    /**
     * 薬価基準コード yakkakjncd
     *
     * @param yakkakjncd the yakkakjncd to set
     */
    public void setYakkakjncd(String yakkakjncd) {
        this.yakkakjncd = yakkakjncd;
    }

    /**
     * 薬剤区分 ykzkbn
     *
     * @return the ykzkbn
     */
    public String getYkzkbn() {
        return ykzkbn;
    }

    /**
     * 薬剤区分 ykzkbn
     *
     * @param ykzkbn the ykzkbn to set
     */
    public void setYkzkbn(String ykzkbn) {
        this.ykzkbn = ykzkbn;
    }

    /**
     * ICD10 icd10(4.5) or icd10_1(4.6)
     *
     * @return the icd10
     */
    public String getIcd10() {
        return icd10;
    }

    /**
     * ICD10 icd10(4.5) or icd10_1(4.6)
     *
     * @param icd10 the icd10 to set
     */
    public void setIcd10(String icd10) {
        this.icd10 = icd10;
    }

    /**
     * tbl_inputcd の入力コード
     *
     * @return tbl_inputcd inputcd
     */
    public String getInputCd() {
        return inputCd;
    }

    /**
     * tbl_inputcd の入力コード
     *
     * @param inputCd 入力コード
     */
    public void setInputCd(String inputCd) {
        this.inputCd = inputCd;
    }

    /**
     * tbl_inputcd の表示名
     *
     * @return 表示名
     */
    public String getDspName() {
        return dspName;
    }

    /**
     * tbl_inputcd の表示名
     *
     * @param dspName 表示名
     */
    public void setDspName(String dspName) {
        this.dspName = dspName;
    }

    /**
     * tbl_inputset の回数
     *
     * @return 回数
     */
    public int getKaisu() {
        return kaisu;
    }

    /**
     * tbl_inputset の回数
     *
     * @param kaisu 回数
     */
    public void setKaisu(int kaisu) {
        this.kaisu = (kaisu == 0) ? 1 : kaisu;
    }

    /**
     * tbl_inputset の数量
     *
     * @return 数量
     */
    public float getSuryo1() {
        return suryo1;
    }

    /**
     * tbl_inputset の数量
     *
     * @param suryo1 数量
     */
    public void setSuryo1(float suryo1) {
        this.suryo1 = suryo1;
    }

    /**
     * tbl_inputset の転帰
     *
     * @return 転帰
     */
    public String getTenkiKbn() {
        return tenkiKbn;
    }

    /**
     * tbl_inputset の転帰
     *
     * @param tenkiKbn 転帰
     */
    public void setTenkiKbn(String tenkiKbn) {
        this.tenkiKbn = tenkiKbn;
    }

    /**
     * tbl_inputset のコメント
     *
     * @return コメント
     */
    public String getComment() {
        return coment;
    }

    /**
     * tbl_inputset のコメント
     *
     * @param comment コメント
     */
    public void setComment(String comment) {
        this.coment = comment;
    }

    /**
     * 順番を決定する.
     *
     * @param obj 対象
     * @return negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Object obj) {

        OrcaEntry other = (OrcaEntry) obj;

        int myUse = getUseState(getStartDate(), getEndDate());
        int otherUse = getUseState(other.getStartDate(), other.getEndDate());

        // 有効期限状態が同じなら，コード順で比較
        if (myUse == otherUse && getCode() != null) return getCode().compareTo(other.getCode());
        // 有効期限内ならばそちらを優先
        if (myUse == 1) return -1;
        if (otherUse == 1) return 1;
        // そうでないならば，有効期限前を優先
        return myUse - otherUse;
    }

    /**
     * この Object とパラメータの Object が等しいかどうか.
     *
     * @param other 対象
     * @return {@code true} if this object is the same as the obj
     *         argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof OrcaEntry) && compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.getCode() != null ? this.getCode().hashCode() : 0);
        return hash;
    }

    /**
     * 有効期限内かどうかを返す.
     *
     * @return 有効期限内なら {@code true}
     */
    public boolean isInUse() {
        return getUseState(getStartDate(), getEndDate()) == 1;
    }

    /**
     * 今日の日付と有効期限との関係を判定.
     *
     * @param startDate 開始日
     * @param endDate 終了日
     * @return 期限前＝０，期限内＝１，期限切れ＝２
     */
    private int getUseState(String startDate, String endDate) {
        // 有効期限前
        if (startDate != null && refDate.compareTo(startDate) < 0) return 0;
            // 有効期限後
        else if (endDate != null && refDate.compareTo(endDate) > 0) return 2;
        // 有効期限内
        return 1;
    }

    /**
     * tbl_inputcd の情報から StampInfo を作る.
     *
     * @return ModuleInfoBean
     */
    public ModuleInfoBean getStampInfo() {
        ModuleInfoBean ret = new ModuleInfoBean();
        ret.setStampName(getDspName());
        ret.setStampRole(IInfoModel.ROLE_ORCA_SET);
        ret.setEntity(IInfoModel.ENTITY_MED_ORDER);
        ret.setStampId(getInputCd());
        return ret;
    }

    /**
     * tbl_inputset の inputcd から ClaimItem を作る.
     *
     * @return ClaimItem
     */
    public ClaimItem getClaimItem() {
        ClaimItem ret = new ClaimItem();
        ret.setCode(getInputCd());
        ret.setNumber(String.valueOf(getSuryo1()));
        return ret;
    }
}

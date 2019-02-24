package open.dolphin.orca.orcaapi.bean;

/**
 * Ac_Point_Detail. 点数詳細（繰り返し１６）
 * @author pns
 */
public class AcPointDetail {
    /**
     * 識別コード（識別コード：名称、A00：初・再診料、B00：医学管理等、C00：在宅療養、F00：投薬、G00：注射、J00：処置、K00：手術、L00：麻酔、D00：検査、E00：画像診断、H00：リハビリ、I00：精神科専門、M00：放射線治療、N00 ：病理診断、A10：入院料、001：療養担当手当） (例: A00)
     */
    private String AC_Point_Code;

    /**
     * 名称 (例: 初・再診料)
     */
    private String AC_Point_Name;

    /**
     * 点数 (例: 69)
     */
    private String AC_Point;

    /**
     * 保険適用外金額（ゼロは非表示） (例: 1080)
     */
    private String Me_Money;

    /**
     * 識別コード（識別コード：名称、A00：初・再診料、B00：医学管理等、C00：在宅療養、F00：投薬、G00：注射、J00：処置、K00：手術、L00：麻酔、D00：検査、E00：画像診断、H00：リハビリ、I00：精神科専門、M00：放射線治療、N00 ：病理診断、A10：入院料、001：療養担当手当） (例: A00)
     * @return the AC_Point_Code
     */
    public String getAC_Point_Code() {
        return AC_Point_Code;
    }

    /**
     * 識別コード（識別コード：名称、A00：初・再診料、B00：医学管理等、C00：在宅療養、F00：投薬、G00：注射、J00：処置、K00：手術、L00：麻酔、D00：検査、E00：画像診断、H00：リハビリ、I00：精神科専門、M00：放射線治療、N00 ：病理診断、A10：入院料、001：療養担当手当） (例: A00)
     * @param AC_Point_Code the AC_Point_Code to set
     */
    public void setAC_Point_Code(String AC_Point_Code) {
        this.AC_Point_Code = AC_Point_Code;
    }

    /**
     * 名称 (例: 初・再診料)
     * @return the AC_Point_Name
     */
    public String getAC_Point_Name() {
        return AC_Point_Name;
    }

    /**
     * 名称 (例: 初・再診料)
     * @param AC_Point_Name the AC_Point_Name to set
     */
    public void setAC_Point_Name(String AC_Point_Name) {
        this.AC_Point_Name = AC_Point_Name;
    }

    /**
     * 点数 (例: 69)
     * @return the AC_Point
     */
    public String getAC_Point() {
        return AC_Point;
    }

    /**
     * 点数 (例: 69)
     * @param AC_Point the AC_Point to set
     */
    public void setAC_Point(String AC_Point) {
        this.AC_Point = AC_Point;
    }

    /**
     * 保険適用外金額（ゼロは非表示） (例: 1080)
     * @return the Me_Money
     */
    public String getMe_Money() {
        return Me_Money;
    }

    /**
     * 保険適用外金額（ゼロは非表示） (例: 1080)
     * @param Me_Money the Me_Money to set
     */
    public void setMe_Money(String Me_Money) {
        this.Me_Money = Me_Money;
    }
}
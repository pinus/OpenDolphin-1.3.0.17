package open.dolphin.orca.orcaapi.bean;

/**
 * Menu_Item_Information. メニュー項目情報（繰り返し　最大５０）
 *
 * @author pns
 */
public class MenuItemInformation {
    /**
     * メニュー項目番号 (例: 21)
     * 1 医事業務  3 プログラム更新  29 外来まとめ  11 受付  12 登録  13 照会  14 予約  21 診療行為  22 病名
     * 23 収納  24 会計照会  31 入退院登録  32 入院会計照会  33 入院定期請求  34 退院時仮計算  36 入院患者照会
     * 41 データチェック  42 明細書  43 請求管理  44 総括表  51 日次統計  52 月次統計  71 データ出力  82 外部媒体
     * 91 マスタ登録  92 マスタ更新  101 システム管理  102 点数マスタ  103 チェックマスタ  104 保険番号マスタ
     * 105 保険者マスタ  106 人名辞書マスタ  107 薬剤情報マスタ  108 住所マスタ  109 ヘルプマスタ
     */
    private String Menu_Item_Number;

    /**
     * メニュー項目名称 (例: 医事業務)
     */
    private String Menu_Item_Name;

    /**
     * メニュー項目権限0:操作権限なし1:操作権限あり (例: 1)
     */
    private String Menu_Item_Privilege;

    /**
     * Menu_Item_Number
     *
     * @return Menu_Item_Number
     */
    public String getMenu_Item_Number() {
        return Menu_Item_Number;
    }

    /**
     * Menu_Item_Number
     *
     * @param Menu_Item_Number to set
     */
    public void setMenu_Item_Number(String Menu_Item_Number) {
        this.Menu_Item_Number = Menu_Item_Number;
    }

    /**
     * Menu_Item_Name
     *
     * @return Menu_Item_Name
     */
    public String getMenu_Item_Name() {
        return Menu_Item_Name;
    }

    /**
     * Menu_Item_Name
     *
     * @param Menu_Item_Name to set
     */
    public void setMenu_Item_Name(String Menu_Item_Name) {
        this.Menu_Item_Name = Menu_Item_Name;
    }

    /**
     * Menu_Item_Privilege
     *
     * @return Menu_Item_Privilege
     */
    public String getMenu_Item_Privilege() {
        return Menu_Item_Privilege;
    }

    /**
     * Menu_Item_Privilege
     *
     * @param Menu_Item_Privilege to set
     */
    public void setMenu_Item_Privilege(String Menu_Item_Privilege) {
        this.Menu_Item_Privilege = Menu_Item_Privilege;
    }
}

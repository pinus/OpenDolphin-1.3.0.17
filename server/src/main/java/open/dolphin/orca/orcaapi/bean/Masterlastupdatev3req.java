package open.dolphin.orca.orcaapi.bean;

public class Masterlastupdatev3req {
    /**
     * マスタID
     * 点数マスタ...medication_master, 病名マスタ...disease_master, 未設定の場合、マスタの最終更新日一覧を返却します。 (例: medication_master)
     */
    private String Master_Id;

    /**
     * Master_Id
     *
     * @return Master_Id
     */
    public String getMaster_Id() {
        return Master_Id;
    }

    /**
     * Master_Id
     *
     * @param Master_Id to set
     */
    public void setMaster_Id(String Master_Id) {
        this.Master_Id = Master_Id;
    }
}
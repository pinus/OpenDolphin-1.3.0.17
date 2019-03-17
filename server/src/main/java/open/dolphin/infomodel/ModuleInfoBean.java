package open.dolphin.infomodel;

import open.dolphin.util.ModelUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Stamp 及び Module の属性を保持するクラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Embeddable
public class ModuleInfoBean extends InfoModel implements Comparable<ModuleInfoBean> {
    private static final long serialVersionUID = -3011774071100921454L;

    /**
     * Module 名: StampTree、 オーダ履歴当に表示する名前
     */
    @Column(nullable = false)
    private String name;

    /**
     * SOA または P の役割
     */
    @Column(nullable = false)
    private String role;

    /**
     * ドキュメントに出現する順番
     */
    @Column(nullable = false)
    private int stampNumber;

    /**
     * 情報の実体名
     */
    @Column(nullable = false)
    private String entity;

    /**
     * 編集可能かどうか
     */
    @Transient
    private boolean editable = true;

    /**
     * ASP 提供か
     */
    @Transient
    private boolean asp;

    /**
     * DB 保存されている場合、そのキー
     */
    @Transient
    private String stampId;

    /**
     * Memo の内容説明
     */
    @Transient
    private String memo;

    /**
     * 折り返し表示するかどうか
     */
    @Transient
    private boolean turnIn;

    public String getStampName() {
        return name;
    }

    public void setStampName(String name) {
        this.name = name;
    }

    public String getStampRole() {
        return role;
    }

    public void setStampRole(String role) {
        this.role = role;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public boolean isSerialized() {
        return stampId != null;
    }

    public boolean isASP() {
        return asp;
    }

    public void setASP(boolean asp) {
        this.asp = asp;
    }

    public String getStampId() {
        return stampId;
    }

    public void setStampId(String id) {
        stampId = id;
    }

    public String getStampMemo() {
        return memo;
    }

    public void setStampMemo(String memo) {
        this.memo = memo;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isTurnIn() {
        return turnIn;
    }

    public void setTurnIn(boolean turnIn) {
        this.turnIn = turnIn;
    }

    @Override
    public String toString() {
        // 病名でエイリアスがあればそれを返す
        if (ENTITY_DIAGNOSIS.equals(entity)) {
            String alias = ModelUtils.getDiagnosisAlias(name);
            return alias != null ? alias : name;
        }
        return name;
    }

    public int getStampNumber() {
        return stampNumber;
    }

    public void setStampNumber(int stampNumber) {
        this.stampNumber = stampNumber;
    }

    /**
     * スタンプ番号で比較する.
     *
     * @param other 比較対象の ModuleInfoBean
     * @return 比較値
     */
    @Override
    public int compareTo(ModuleInfoBean other) {
        if (other != null) {
            return getStampNumber() - other.getStampNumber();
        }
        // null は最上位
        return -1;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof ModuleInfoBean) && compareTo((ModuleInfoBean) other) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 53 * hash + (this.stampId != null ? this.stampId.hashCode() : 0);
        return hash;
    }
}

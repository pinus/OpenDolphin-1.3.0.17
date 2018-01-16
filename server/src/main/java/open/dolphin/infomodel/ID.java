package open.dolphin.infomodel;

/**
 * ID
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class ID extends InfoModel {
    private static final long serialVersionUID = -5070888481802856042L;

    private String id;
    private String idType;
    private String idTypeTableId;

    public ID(String id, String idType, String idTypeTableId) {
        this.id = id;
        this.idType = idType;
        this.idTypeTableId = idTypeTableId;
    }

    public String getId() {
        return id;
    }

    public void setId(String val) {
        id = val;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdTypeTableId(String idTypeTableId) {
        this.idTypeTableId = idTypeTableId;
    }

    public String getIdTypeTableId() {
        return idTypeTableId;
    }
}

package open.dolphin.client;

import open.dolphin.infomodel.SchemaModel;

/**
 * SchemaList
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class SchemaList implements java.io.Serializable {
    private static final long serialVersionUID = 5408868424299902180L;

    private SchemaModel[] schemaList;

    /**
     * @return the schemaList
     */
    public SchemaModel[] getSchemaList() {
        return schemaList;
    }

    /**
     * @param schemaList the schemaList to set
     */
    public void setSchemaList(SchemaModel[] schemaList) {
        this.schemaList = schemaList;
    }
}

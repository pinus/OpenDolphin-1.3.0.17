package open.dolphin.dnd;

import open.dolphin.client.OrderList;
import open.dolphin.client.SchemaList;

import java.awt.datatransfer.DataFlavor;

public class DolphinDataFlavor extends DataFlavor {
    public static final DataFlavor stampListFlavor = new DataFlavor(OrderList.class, "Order List");
    public static DataFlavor schemaListFlavor = new DataFlavor(SchemaList.class, "Schema List");

}

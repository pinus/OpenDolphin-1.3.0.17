package open.dolphin.client;

import open.dolphin.infomodel.ModuleModel;

/**
 * OrderList. Contains array of stamps.
 *
 * @author Kazushi Minagawa
 */
public final class OrderList implements java.io.Serializable {
    
    private ModuleModel[] orderList;

    public OrderList(ModuleModel[] stamp) {
        setOrderStamp(stamp);
    }

    public ModuleModel[] getOrderList() {
        return orderList;
    }

    public void setOrderStamp(ModuleModel[] stamp) {
        orderList = stamp;
    }
}

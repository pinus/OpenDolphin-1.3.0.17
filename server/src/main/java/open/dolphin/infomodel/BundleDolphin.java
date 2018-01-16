package open.dolphin.infomodel;

/**
 * BundleDolphin
 *
 * @author  Minagawa,Kazushi
 */
public class BundleDolphin extends ClaimBundle {
    private static final long serialVersionUID = -8747202550129389855L;

    private String orderName;

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getItemNames() {
        ClaimItem[] claimItem = getClaimItem();

        if (claimItem != null && claimItem.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(claimItem[0].getName());
            for (int i = 1; i < claimItem.length; i++) {
                sb.append(",");
                sb.append(claimItem[i].getName());
            }
            return sb.toString();
        }
        return null;
    }

    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        // Print order name
        buf.append(orderName);
        buf.append("\n");
        ClaimItem[] items = getClaimItem();
        int len = items.length;
        ClaimItem item;
        String number;

        for (int i = 0; i < len; i++) {
            item = items[i];

            // Print item name
            buf.append("・");
            buf.append(item.getName());

            // Print item number
            number = item.getNumber();
            if (number != null) {
                buf.append("　");
                buf.append(number);
                if (item.getUnit() != null) {
                    buf.append(item.getUnit());
                }
            }
            buf.append("\n");
        }

        // Print bundleNumber
        if (!"1".equals(getBundleNumber())) {
            buf.append("X　");
            buf.append(getBundleNumber());
            buf.append("\n");
        }

        // Print admMemo
        if (getAdminMemo() != null) {
            buf.append(getAdminMemo());
            buf.append("\n");
        }

        // Print bundleMemo
        if (getMemo() != null) {
            buf.append(getMemo());
            buf.append("\n");
        }

        return buf.toString();
    }
}

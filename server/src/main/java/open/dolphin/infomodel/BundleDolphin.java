package open.dolphin.infomodel;

import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * BundleDolphin.
 *
 * @author Minagawa, Kazushi
 */
public class BundleDolphin extends ClaimBundle {
    private static final long serialVersionUID = -8747202550129389855L;

    private String orderName;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    // Velocity で使っているので消したらだめ!
    public String getItemNames() {
        return getClaimItem() == null ? null :
                Stream.of(getClaimItem()).map(ClaimItem::getName).collect(Collectors.joining(","));
    }

    @Override
    public String toString() {

        StringJoiner sj = new StringJoiner("\n");
        sj.add(orderName);

        addClaimItems(sj);

        // Print bundleNumber
        String num = getBundleNumber();
        if (num != null && !"1".equals(num)) {
            sj.add(String.format("X %s", num));
        }

        addMemo(sj);

        return sj.toString();
    }

    // package-private
    void addClaimItems(StringJoiner sj) {
        ClaimItem[] items = getClaimItem();
        if (items != null) {
            Stream.of(items).forEachOrdered(item -> {
                // Print item name, number, and unit
                String name = item.getName() == null ? "" : item.getName();
                String num = item.getNumber() == null ? "" : item.getNumber();
                String unit = item.getUnit() == null ? "" : item.getUnit();
                sj.add(String.format("・%s %s%s", name, num, unit));
            });
        }
    }

    // package-private
    void addMemo(StringJoiner sj) {
        // Print admMemo
        String adminMemo = getAdminMemo();
        if (adminMemo != null) {
            sj.add(adminMemo);
        }

        // Print bundleMemo
        String bundleMemo = getMemo();
        if (bundleMemo != null) {
            sj.add(bundleMemo);
        }
    }
}

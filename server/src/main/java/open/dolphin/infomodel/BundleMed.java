package open.dolphin.infomodel;

import java.util.StringJoiner;

/**
 * BundleMed.
 *
 * @author pns
 */
public class BundleMed extends BundleDolphin {
    private static final long serialVersionUID = -3898329425428401649L;

    @Override
    public String toString() {

        StringJoiner sj = new StringJoiner("\n");
        sj.add("RP");

        addClaimItems(sj);

        String adminString = getAdminDisplayString();
        if (adminString != null) { sj.add(adminString); }

        addMemo(sj);

        return sj.toString();
    }

    private String getAdminDisplayString() {

        String adminString = null;
        if (getAdmin() != null && (!getAdmin().equals(""))) {

            if (getClassCode().startsWith(IInfoModel.RECEIPT_CODE_GAIYO.substring(0, 2))) {
                // 外用剤 bundleNumber が１の時は表示しない
                if ("1".equals(getBundleNumber())) {
                    adminString = getAdmin();
                } else {
                    adminString = String.format("%s x %s 回分", getAdmin(), getBundleNumber());
                }
            } else {
                if (getClassCode().startsWith(IInfoModel.RECEIPT_CODE_NAIYO.substring(0, 2))) {
                    // １日３回毎食後
                    adminString = String.format("%s x %s 日分", getAdmin(), getBundleNumber());
                } else {
                    // 屯用
                    adminString = String.format("%s x %s 回分", getAdmin(), getBundleNumber());
                }
            }
        }
        return adminString;
    }
}

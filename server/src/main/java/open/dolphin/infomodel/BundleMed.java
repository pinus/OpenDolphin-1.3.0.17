package open.dolphin.infomodel;

/**
 * BundleMed
 *
 * @author pns
 */
public class BundleMed extends BundleDolphin {
    private static final long serialVersionUID = -3898329425428401649L;
    
    public String getAdminDisplayString() {

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
    
    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();
        
        buf.append("RP\n");
        
        ClaimItem[] items = getClaimItem();
        
        for (ClaimItem item : items) {
            String number = (item.getNumber() == null)? "" : item.getNumber();
            String unit = (item.getUnit() == null)? "" : item.getUnit();

            buf.append(String.format("・%s %s%s\n", item.getName(), number, unit));
        }
        
        if (getAdmin() != null && (!getAdmin().equals(""))) {
            buf.append(getAdminDisplayString());
            buf.append("\n");
        }
        
        // Print admMemo
        if (getAdminMemo() != null) {
            buf.append(getAdminMemo());
            buf.append("\n");
        }
        
        // Print Memo
        if (getMemo() != null) {
            buf.append(getMemo());
            buf.append("\n");
        }
        
        return buf.toString();
    }
}
package open.dolphin.infomodel;

/**
 * PhysicalModel
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class PhysicalModel extends InfoModel implements Comparable<PhysicalModel> {
    private static final long serialVersionUID = 5923780180643179995L;

    private long heightId;
    private long weightId;

    // 身長
    private String height;

    // 体重
    private String weight;

    // BMI
    private int bmi;

    // 同定日
    private String identifiedDate;

    // メモ
    private String memo;

    public long getHeightId() {
        return heightId;
    }

    public void setHeightId(long heightId) {
        this.heightId = heightId;
    }

    public long getWeightId() {
        return weightId;
    }

    public void setWeightId(long weightId) {
        this.weightId = weightId;
    }

    // factor
    public String getHeight() {
        return height;
    }
    public void setHeight(String value) {
        height = value;
    }

    // identifiedDate
    public String getIdentifiedDate() {
        return identifiedDate;
    }

    public void setIdentifiedDate(String value) {
        identifiedDate = value;
    }

    // memo
    public String getMemo() {
        return memo;
    }

    public void setMemo(String value) {
        memo = value;
    }

    public void setWeight(String severity) {
        this.weight = severity;
    }

    public String getWeight() {
        return weight;
    }

    public String getBmi() {
        return calcBmi();
    }

    public String calcBmi() {
        if (height != null && weight != null) {
            float fw = Float.parseFloat(weight);
            float fh = Float.parseFloat(height);
            float bmif = (10000f*fw) / (fh*fh);
            String bmiS = String.valueOf(bmif);
            int index = bmiS.indexOf('.');
            int len = bmiS.length();
            if (index >0 && (index + 2 < len)) {
                bmiS = bmiS.substring(0,index+2);
            }
            return bmiS;
        }
        return null;
    }

    public String getStandardWeight() {
        if (getHeight() == null) {
            return  null;
        }
        try {
            float h = Float.parseFloat(getHeight());
            h /= 100.0f;
            float stW = 22.0f * (h * h);
            String stWS = String.valueOf(stW);
            int index = stWS.indexOf('.');
            if (index > 0) {
                stWS = stWS.substring(0, index +2);
            }
            return stWS;

        } catch (NumberFormatException e) {
            System.out.println("PhysicalModel.java: " + e);
        }
        return null;
    }

    @Override
    public int compareTo(PhysicalModel other) {
        if (other != null) {
            String val1 = getIdentifiedDate();
            String val2 = other.getIdentifiedDate();
            return val1.compareTo(val2);
        }
        return 1;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof PhysicalModel) && compareTo((PhysicalModel) other) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (this.weightId ^ (this.weightId >>> 32));
        hash = 83 * hash + (this.identifiedDate != null ? this.identifiedDate.hashCode() : 0);
        return hash;
    }
}
package open.dolphin.infomodel;

/**
 * ClaimItem 要素クラス。
 *
 * @author Kazushi Minagawa, Digital Globe,Inc.
 */
public class ClaimItem extends InfoModel {
    private static final long serialVersionUID = 3256217487799388468L;
	
    private String name;
    private String code;
    private String codeSystem;
    private String classCode;
    private String classCodeSystem;
    private String number;
    private String unit;
    private String numberCode;
    private String numberCodeSystem;
    private String memo;

    public String getName() {
        return name;
    }

    public void setName(String val) {
        name = val;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String val) {
        code = val;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(String val) {
        codeSystem = val;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String val) {
        classCode = val;
    }

    public String getClassCodeSystem() {
        return classCodeSystem;
    }

    public void setClassCodeSystem(String val) {
        classCodeSystem = val;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String val) {
        number = val;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String val) {
        unit = val;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String val) {
        numberCode = val;
    }

    public String getNumberCodeSystem() {
        return numberCodeSystem;
    }

    public void setNumberCodeSystem(String val) {
        numberCodeSystem = val;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String val) {
        memo = val;
    }
}

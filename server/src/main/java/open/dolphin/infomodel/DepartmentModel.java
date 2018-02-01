package open.dolphin.infomodel;

import javax.persistence.Embeddable;

/**
 * DepartmentModel.
 * department (番号), departmentDesc（漢字名）
 * 01 内科, 02 精神科, 03 神経科, 04 神経内科, 05 呼吸器科, 06 消化器科, 07 胃腸科, 08 循環器科, 09 小児科, 10 外科,
 * 11 整形外科, 12 形成外科, 13 美容外科, 14 脳神経外科, 15 呼吸器外科, 16 心臓血管外科, 17 小児外科, 18 皮膚ひ尿器科, 19 皮膚科, 20 ひ尿器科,
 * 21 性病科, 22 こう門科, 23 産婦人科, 24 産科, 25 婦人科, 26 眼科, 27 耳鼻いんこう科, 28 気管食道科, 29 理学診療科, 30 放射線科,
 * 31 麻酔科, 32 人工透析科, 33 心療内科, 34 アレルギー, 35 リウマチ, 36 リハビリ, A1 鍼灸
 * departmentCodeSys=MML0028
 *
 * @author Minagawa,Kazushi
 */
@Embeddable
public class DepartmentModel extends InfoModel {
    private static final long serialVersionUID = -920243869556556218L;

    private String department;
    private String departmentDesc;
    private String departmentCodeSys;

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }

    public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentCodeSys(String departmentCodeSys) {
        this.departmentCodeSys = departmentCodeSys;
    }

    public String getDepartmentCodeSys() {
        return departmentCodeSys;
    }

    @Override
    public String toString() {
        return departmentDesc;
    }
}

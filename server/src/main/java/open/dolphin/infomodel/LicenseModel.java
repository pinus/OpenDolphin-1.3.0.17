package open.dolphin.infomodel;

import javax.persistence.Embeddable;

/**
 * LicenseModel.
 * licenseDesc = 医師, 歯科医師, 看護師, 准看護師, 臨床検査技師, レントゲン技師, 薬剤師, 理学療法士, 作業療法士, 精神保険福祉士, 臨床心理技術者, 栄養士, 歯科衛生士, 歯科技工士, 臨床工学士, 介護支援専門員, その他の医療従事者, 鍼灸士, 患者及びその代理人
 * license = doctor, dentist, nurse, assistantNurse, lab, rad, pharmacist, pt, ot, psy, cps, nutritionist, dentalHygienist, dentalTechnician, clinicalEngineer, careManager, other, acupuncturist, patient
 * licenseCodeSys = MML0026
 *
 * @author Minagawa,Kazushi
 */
@Embeddable
public class LicenseModel extends InfoModel {
    private static final long serialVersionUID = 5120402348495916132L;

    private String license;

    private String licenseDesc;

    private String licenseCodeSys;

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicense() {
        return license;
    }

    public void setLicenseDesc(String licenseDesc) {
        this.licenseDesc = licenseDesc;
    }

    public String getLicenseDesc() {
        return licenseDesc;
    }

    public void setLicenseCodeSys(String licenseCodeSys) {
        this.licenseCodeSys = licenseCodeSys;
    }

    public String getLicenseCodeSys() {
        return licenseCodeSys;
    }

    @Override
    public String toString() {
        return licenseDesc;
    }
}

package open.dolphin.orcaapi;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import open.dolphin.infomodel.*;
import open.dolphin.project.Project;
import open.dolphin.util.ModelUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Element;

/**
 * Orca API 用の Element を JDOM で作成する
 * Orca 4.7 xml2 バージョン
 * @author pns
 */
public class OrcaApiElementXml2 {

    /**
     * 中途終了データ作成（/api21/medicalmodv2）
     */
    public static class MedicalMod extends Element {
        private static final long serialVersionUID = 1L;

        public MedicalMod(DocumentModel documentModel, PVTHealthInsuranceModel insuranceModel) {
            super("data");

            if (insuranceModel == null) { insuranceModel = new PVTHealthInsuranceModel(); }

            addContent(new OrcaApiElementXml2.medicalreq(documentModel, insuranceModel));
        }

        public MedicalMod(List<RegisteredDiagnosisModel> diagnosisModels, PatientVisitModel pvt) {
            super("data");

            addContent(new OrcaApiElementXml2.medicalreq(diagnosisModels, pvt));
        }
    }

    /**
     * 公費の Element
     */
    public static class PublicInsurance_Information extends Element {
        private static final long serialVersionUID = 1L;
        public PublicInsurance_Information(PVTPublicInsuranceItemModel[] models) {
            super("PublicInsurance_Information");
            setAttribute("type", "array");

            for(PVTPublicInsuranceItemModel m : models) {
                Element record = new Element("PublicInsurance_Information_child").setAttribute("type", "record");
                record.addContent(new Element("PublicInsurance_Name").setAttribute("type", "string").addContent(m.getProviderName()));
                record.addContent(new Element("PublicInsurer_Number").setAttribute("type", "string").addContent(m.getProvider()));
                // 生保受給者番号未記入の場合，"mikinyu" という文字列が入っているが，2014/11/25 の orca api 仕様変更で受け付けなくなった
                String jukyushaBango = "mikinyu".equals(m.getRecipient())? "" : m.getRecipient();
                record.addContent(new Element("PublicInsuredPerson_Number").setAttribute("type", "string").addContent(jukyushaBango));
                record.addContent(new Element("Certificate_IssuedDate").setAttribute("type", "string").addContent(m.getStartDate()));
                record.addContent(new Element("Certificate_ExpiredDate").setAttribute("type", "string").addContent(m.getExpiredDate()));
                addContent(record);
            }
        }
    }

    /**
     * 健康保険の Element
     * 2014.9.4　orca support center から回答
     * 公費単独の場合は，InsuranceProvider_Class 〜 Certificate_ExpiredDate に「公費単独(980)」を入れたらだめ
     */
    public static class HealthInsurance_Information extends Element {
        private static final long serialVersionUID = 1L;
        public HealthInsurance_Information(PVTHealthInsuranceModel model) {
            super("HealthInsurance_Information");
            setAttribute("type", "record");

            String insuranceClassCode = model.getInsuranceClassCode();
            // 以下の項目は「公費単独(XX)」 ** 以外 ** の場合のみ add する
            if (! "XX".equals(insuranceClassCode)) {
                String orcaInsuranceClassCode = convertToOrcaInsuranceClassCode(insuranceClassCode);

                // 自費の場合は，Number "9999"，Person_Symbol および Person_Numver に "記載なし" という文字列が入っているが，2014/11/25 の orca api 仕様変更で受け付けなくなった
                String number = "9999".equals(model.getInsuranceNumber())? "" : model.getInsuranceNumber();
                String personSymbol = "記載なし".equals(model.getClientGroup())? "" : model.getClientGroup();
                String personNumber = "記載なし".equals(model.getClientNumber())? "" : model.getClientNumber();

                addContent(new Element("InsuranceProvider_Class").setAttribute("type", "string").addContent(orcaInsuranceClassCode));
                addContent(new Element("InsuranceProvider_Number").setAttribute("type", "string").addContent(number));
                addContent(new Element("InsuranceProvider_WholeName").setAttribute("type", "string").addContent(model.getInsuranceClass()));
                addContent(new Element("HealthInsuredPerson_Symbol").setAttribute("type", "string").addContent(personSymbol));
                addContent(new Element("HealthInsuredPerson_Number").setAttribute("type", "string").addContent(personNumber));
                addContent(new Element("RelationToInsuredPerson").setAttribute("type", "string").addContent("true".equals(model.getFamilyClass())? "1" : "2"));
                addContent(new Element("Certificate_StartDate").setAttribute("type", "string").addContent(model.getStartDate()));
                addContent(new Element("Certificate_ExpiredDate").setAttribute("type", "string").addContent(model.getExpiredDate()));
            }

            PVTPublicInsuranceItemModel[] publicInsuranceModels = model.getPVTPublicInsuranceItem();
            if (publicInsuranceModels != null) {
                addContent(new OrcaApiElementXml2.PublicInsurance_Information(publicInsuranceModels));
            }
        }
    }

    /**
     * dolphin の値（= claim で受け取る値）　　　　　　　　　　　　　OrcaApi の値
     * Rx：労災・自賠（x：該当の保険番号マスタの保険番号の3桁目）      971（労災）, 973（自賠）
　　　* Zx：自費（xは同上）                                      980
　　　* Ax：治験 90x（xは同上）　　(ver 4.5.0以降)
　　　* Bx：治験 91x（xは同上）　　(ver 4.5.0以降)
　　　* K5：公害                                               975
     * 00：国保                                               060
　　　* 39：後期高齢者　　　　　　　　　　　　　　　　　　　　　　　　　　039
　　　* 40：後期特療費(後期高齢者医療特別療養費)                     040
　　　* 09：協会けんぽ                                          090
　　　* XX：公費単独                                            980
     *
     * @param claimInsuranceClassCode
     * @return
     */
    private static String convertToOrcaInsuranceClassCode(String claimInsuranceClassCode) {
        if (claimInsuranceClassCode == null) { return ""; }
        if (claimInsuranceClassCode.equals("R1")) { return "971"; }
        if (claimInsuranceClassCode.equals("R3")) { return "973"; }
        if (claimInsuranceClassCode.equals("K5")) { return "975"; }
        if (claimInsuranceClassCode.equals("XX")) { return "980"; }
        if (claimInsuranceClassCode.startsWith("Z")) { return "980"; }
        if (claimInsuranceClassCode.equals("00")) { return "060"; }
        if (claimInsuranceClassCode.matches("[0-9][0-9]")) { return "0" + claimInsuranceClassCode; }
        return "";
    }

    /**
     * ClaimItem 部分 の Element
     */
    public static class Medication_info extends Element {
        private static final long serialVersionUID = 1L;
        public Medication_info(ClaimItem[] items) {
            super("Medication_info");
            setAttribute("type", "array");

            for (ClaimItem i : items) {
                Element record = new Element("Medication_info_child").setAttribute("type", "record");
                record.addContent(new Element("Medication_Code").setAttribute("type", "string").addContent(i.getCode()));
                record.addContent(new Element("Medication_Name").setAttribute("type", "string").addContent(i.getName()));
                record.addContent(new Element("Medication_Number").setAttribute("type", "string").addContent(i.getNumber()));
                addContent(record);
            }
        }
    }

    /**
     * ClaimBundle 部分の Element
     */
    public static class Medical_Information extends Element {
        private static final long serialVersionUID = 1L;
        public Medical_Information(Collection<ModuleModel> models) {
            super("Medical_Information");
            setAttribute("type", "array");

            for (ModuleModel mm : models) {
                IInfoModel im = mm.getModel();
                if (im instanceof ClaimBundle) {
                    ClaimBundle b = (ClaimBundle) im;
                    Element record = new Element("Medical_Information_child").setAttribute("type", "record");
                    record.addContent(new Element("Medical_Class").setAttribute("type", "string").addContent(b.getClassCode()));
                    record.addContent(new Element("Medical_Class_Name").setAttribute("type", "string").addContent(b.getClassName()));
                    record.addContent(new Element("Medical_Class_Number").setAttribute("type", "string").addContent(b.getBundleNumber()));

                    ClaimItem[] claimItems = b.getClaimItem();
                    if (claimItems != null) {
                        record.addContent(new OrcaApiElementXml2.Medication_info(claimItems));
                    }
                    addContent(record);

                    // admin がある場合は Medication_info にくっつけることになっている
                    if (b.getAdmin() != null) {
                        Element admin = new Element("Medication_info_child").setAttribute("type", "record");
                        admin.addContent(new Element("Medication_Code").setAttribute("type", "string").addContent(b.getAdminCode()));
                        admin.addContent(new Element("Medication_Name").setAttribute("type", "string").addContent(b.getAdmin()));
                        record.getChild("Medication_info").addContent(admin);
                    }
                }
            }
        }
    }

    /**
     * 病名部分の Element
     */
    public static class Disease_Information extends Element {
        private static final long serialVersionUID = 1L;
        public Disease_Information(List<RegisteredDiagnosisModel> models) {
            super("Disease_Information");
            setAttribute("type", "array");

            for (RegisteredDiagnosisModel m : models) {
                Element record = new Element("Disease_Information_child").setAttribute("type", "record");
                record.addContent(new Element("Disease_Code").setAttribute("type", "string").addContent(convertToOrcaByomei(m.getDiagnosisCode())));
                record.addContent(new Element("Disease_Name").setAttribute("type", "string").addContent(m.getDiagnosis()));

                String category = m.getCategory();
                if ("mainDiagnosis".equals(category)) {
                    record.addContent(new Element("Disease_Category").setAttribute("type", "string").addContent("PD"));
                } else if ("suspectedDiagnosis".equals(category)) {
                    record.addContent(new Element("Disease_SuspectedFlag").setAttribute("type", "string").addContent("S"));
                }

                record.addContent(new Element("Disease_StartDate").setAttribute("type", "string").addContent(m.getStartDate()));
                record.addContent(new Element("Disease_EndDate").setAttribute("type", "string").addContent(m.getEndDate()));

                if (m.getOutcome() != null && ! m.getOutcome().equals("")) {
                    record.addContent(new Element("Disease_Outcome").setAttribute("type", "string").addContent("F"));
                }
                addContent(record);
            }
        }
    }

    /**
     * RegisteredDiagnosisModel の病名コードを，Orca Api 用に変換する
     *  eg) 1013.7061017 → ZZZ1013,7061017
     * @param claimByomei
     * @return
     */
    private static String convertToOrcaByomei(String claimByomei) {
        String[] singles = claimByomei.split("\\.");
        StringBuilder b = new StringBuilder();

        for (String s : singles) {
            if (s.length() == 4) b.append("ZZZ");
            b.append(s).append(",");
        }
        return StringUtils.chop(b.toString());
    }

    /**
     * 診療行為 or 病名の Diagnosis_Information Element
     */
    public static class Diagnosis_Information extends Element {
        private static final long serialVersionUID = 1L;
        /**
         * 診療行為を含む Diagnosis_Information Element
         * @param documentModel
         * @param insuranceModel
         */
        public Diagnosis_Information(DocumentModel documentModel, PVTHealthInsuranceModel insuranceModel) {
            super("Diagnosis_Information");
            //addCommonContent(documentModel.getDocInfo().getDepartmentCode(), documentModel.getDocInfo().getAssignedDoctorId());
            addCommonContent(documentModel.getDocInfo().getDepartmentCode(), Project.getProjectStub().getOrcaStaffCode());
            addContent(new OrcaApiElementXml2.HealthInsurance_Information(insuranceModel));

            Collection<ModuleModel> moduleModels = documentModel.getModules();
            if (moduleModels != null) {
                addContent(new OrcaApiElementXml2.Medical_Information(moduleModels));
            }
        }

        /**
         * 病名を含む Diagnosis_Information Element
         * @param diagnosisModels
         * @param pvt
         */
        public Diagnosis_Information(List<RegisteredDiagnosisModel> diagnosisModels, PatientVisitModel pvt) {
            super("Diagnosis_Information");
            //addCommonContent(pvt.getDepartmentCode(), pvt.getAssignedDoctorId());
            addCommonContent(pvt.getDepartmentCode(), Project.getProjectStub().getOrcaStaffCode());

            if (diagnosisModels != null) {
                addContent(new OrcaApiElementXml2.Disease_Information(diagnosisModels));
            }
        }

        private void addCommonContent(String departmentCode, String physicianCode) {
            setAttribute("type", "record");
            addContent(new Element("Department_Code").setAttribute("type", "string").addContent(departmentCode));
            addContent(new Element("Physician_Code").setAttribute("type", "string").addContent(physicianCode));
        }
    }

    /**
     * medicalreq Element
     */
    public static class medicalreq extends Element {
        public medicalreq(DocumentModel documentModel, PVTHealthInsuranceModel insuranceModel) {
            super("medicalreq");
            addCommonContent(documentModel.getKarte().getPatient().getPatientId(), documentModel.getDocInfo().getFirstConfirmDate());
            addContent(new OrcaApiElementXml2.Diagnosis_Information(documentModel, insuranceModel));
        }

        public medicalreq(List<RegisteredDiagnosisModel> diagnosisModels, PatientVisitModel pvt) {
            super("medicalreq");
            addCommonContent(diagnosisModels.get(0).getPatientLiteModel().getPatientId(), diagnosisModels.get(0).getConfirmed());
            addContent(new OrcaApiElementXml2.Diagnosis_Information(diagnosisModels, pvt));
        }

        private void addCommonContent(String patientId, Date confirmDate) {
            String[] date = ModelUtils.getDateTimeAsString(confirmDate).split("T");

            setAttribute("type", "record");
            addContent(new Element("Patient_ID").setAttribute("type", "string").addContent(patientId));
            addContent(new Element("Perform_Date").setAttribute("type", "string").addContent(date[0]));
            addContent(new Element("Perform_Time").setAttribute("type", "string").addContent(date[1]));
        }
    }
}

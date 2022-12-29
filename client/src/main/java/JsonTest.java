import open.dolphin.delegater.DolphinClientContext;
import open.dolphin.dto.KarteBeanSpec;
import open.dolphin.infomodel.*;
import open.dolphin.service.KarteService;
import open.dolphin.service.StampService;
import open.dolphin.service.SystemService;
import open.dolphin.service.UserService;
import open.dolphin.util.JsonUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author pns
 */
public class JsonTest {
    public static void main(String[] arg) throws Exception {
        new JsonTest().start();
    }

    public void start() throws Exception {
        //postHello();
        //testTypeInfo();
        //testBidirectionalReference();
        //testJsonIdentityIndex();
        //getUserTree();
        getKarte();
    }

    private void postHello() {
        DolphinClientContext.configure("localhost:8001", "1.3.6.1.4.1.9414.10.1:admin", "admin");
        ResteasyWebTarget target = DolphinClientContext.getContext().getWebTarget();
        System.out.println("target = " + target);
        SystemService service = target.proxy(SystemService.class);
        service.hello();
    }

    private void getKarte() {
        DolphinClientContext.configure("localhost:8001", "1.3.6.1.4.1.9414.10.1:admin", "admin");
        ResteasyWebTarget target = DolphinClientContext.getContext().getWebTarget();
        System.out.println("target = " + target);

        KarteService karteService = target.proxy(KarteService.class);
        GregorianCalendar today = new GregorianCalendar();
        today.add(GregorianCalendar.MONTH, -60);
        today.clear(Calendar.HOUR_OF_DAY);
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MILLISECOND);
        KarteBeanSpec spec = new KarteBeanSpec();
        spec.setPatientPk(953);
        spec.setFromDate(today.getTime());
        KarteBean karte = karteService.getKarte(spec);
        System.out.println("karte = " + karte);
        System.out.println("karteid = " + karte.getId());
        //getDocument
        List<DocInfoModel> docInfo = karte.getDocInfoEntry();
        List<Long> ids = new ArrayList<>();
        for (DocInfoModel m : docInfo) {
            System.out.println("docInfo pk (Long) = " + m.getDocPk());
            ids.add(m.getDocPk());
        }
        List<DocumentModel> docs = karteService.getDocumentList(ids);
    }

    private void getUserTree() {
        DolphinClientContext.configure("localhost:8080", "1.3.6.1.4.1.9414.10.1:admin", "admin");
        ResteasyWebTarget target = DolphinClientContext.getContext().getWebTarget();
        System.out.println("target = " + target);

        // getUser
        UserService userService = target.proxy(UserService.class);
        UserModel user = userService.getUser("1.3.6.1.4.1.9414.10.1:doctor1");
        System.out.println("user pk = " + user.getId());
        // getStampTree
        StampService stampService = target.proxy(StampService.class);
        PersonalTreeModel userTree = stampService.getTree(950L);
        System.out.println("Stamp = " + userTree.getName());
    }

    private void testJsonIdentityIndex() {
        DocumentModel document = new DocumentModel();
        document.setId(0);
        PatientMemoModel memo = new PatientMemoModel();
        memo.setId(0);
        KarteBean karte = new KarteBean();
        karte.setId(0);

        UserModel user = new UserModel();
        user.setId(950);
        user.setGivenName("given");
        UserModel user1 = new UserModel();
        user1.setId(950);
        user1.setGivenName("given");

        document.setKarte(karte);
        karte.setPatientMemo(memo);

        document.setCreator(user);
        memo.setCreator(user1);
        //memo.setCreator(user);

        String json = JsonUtils.toJson(document);
        System.out.println(json);
        DocumentModel model = JsonUtils.fromJson(json, DocumentModel.class);
        System.out.println("creater = " + model.getKarte().getPatientMemo().getCreator().getGivenName());
    }

    private void testBidirectionalReference() {
        UserModel user = new UserModel();
        RoleModel role = new RoleModel();
        role.setUser(user);
        user.addRole(role);

        String json = JsonUtils.toJson(user);
        System.out.println(json);

        UserModel u = JsonUtils.fromJson(json, UserModel.class);
    }

    private void testTypeInfo() {
        ModuleModel m1 = new ModuleModel();
        ModuleModel m2 = new ModuleModel();
        ModuleModel m3 = new ModuleModel();
        BundleMed bm = new BundleMed();
        BundleDolphin bd = new BundleDolphin();
        ProgressCourse pc = new ProgressCourse();
        m1.setModel(bm);
        m2.setModel(bd);
        m3.setModel(pc);

        String j1 = JsonUtils.toJson(m1);
        String j2 = JsonUtils.toJson(m2);
        String j3 = JsonUtils.toJson(m3);

        System.out.println("j1= " + j1);
        System.out.println("j2= " + j2);
        System.out.println("j3= " + j3);

        ModuleModel rm1 = JsonUtils.fromJson(j1, ModuleModel.class);
        ModuleModel rm2 = JsonUtils.fromJson(j2, ModuleModel.class);
        ModuleModel rm3 = JsonUtils.fromJson(j3, ModuleModel.class);

        System.out.println(rm1.getModel().getClass());
        System.out.println(rm2.getModel().getClass());
        System.out.println(rm3.getModel().getClass());
    }
}

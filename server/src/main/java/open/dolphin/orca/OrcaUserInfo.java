package open.dolphin.orca;

import open.dolphin.orca.orcaapi.OrcaApi;
import open.dolphin.orca.orcaapi.bean.PhysicianInformation;
import open.dolphin.orca.orcaapi.bean.System01Managereq;
import open.dolphin.orca.orcaapi.bean.System01Manageres;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.DependsOn;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import java.util.*;

/**
 * ORCA 職員コードを調べる.
 *
 * @author pns
 */
@Singleton
@Startup
@DependsOn("OrcaHostInfo")
public class OrcaUserInfo {

    /**
     * 漢字フルネームをキーとした ORCA 職員コードのマップ.
     * 姓名の間は半角スペース.
     */
    private static HashMap<String, String> map = new HashMap<>();

    private OrcaUserInfo() {
    }

    @PostConstruct
    public static void init() {
        OrcaApi api = OrcaApi.getInstance();

        System01Managereq req = new System01Managereq();
        req.setRequest_Number("02"); // Dr.
        System01Manageres res = api.post(req);
        List<PhysicianInformation> infos = new ArrayList<>(Arrays.asList(res.getPhysicianres().getPhysician_Information()));

        req.setRequest_Number("03"); // Dr. 以外
        res = api.post(req);
        infos.addAll(Arrays.asList(res.getPhysicianres().getPhysician_Information()));

        infos.stream().filter(info -> Objects.nonNull(info.getCode())).forEach(info -> {
            String name = replaceSpace(info.getWholeName());
            map.put(name, info.getCode());
            //System.out.println("OrcaUserInfo: " + name);
        });
    }

    /**
     * フルネームから ORCA 職員コードを調べる.
     *
     * @param fullName フルネーム
     * @return ORCA 職員コード
     */
    public static String getOrcaUserCode(String fullName) {
        fullName = replaceSpace(fullName);
        return map.get(fullName);
    }

    private static String replaceSpace(String s) {
        return s.replaceAll("　", " ").trim();
    }
}

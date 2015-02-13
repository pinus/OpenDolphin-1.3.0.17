package open.dolphin.service;

import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import open.dolphin.infomodel.FacilityModel;
import open.dolphin.infomodel.InfoModel;
import open.dolphin.infomodel.RadiologyMethodValue;
import open.dolphin.infomodel.RoleModel;
import open.dolphin.infomodel.UserModel;

/**
 * データベース初期化に使う session bean
 * @author pns
 */
@Stateless
public class SystemServiceImpl extends DolphinService implements SystemService {
    /**
     * 施設と管理者情報を登録する。
     *
     * @param user 施設管理者
     */
    @Override
    public void addFacilityAdmin(UserModel user) {
        // OIDをセットし施設レコードを生成する
        FacilityModel facility = user.getFacilityModel();
        String facilityId = facility.getFacilityId();
        if (facilityId == null || facilityId.equals("")) {
            facilityId = InfoModel.DEFAULT_FACILITY_OID;
            facility.setFacilityId(facilityId);
        }

        try {
            em.createQuery("select f from FacilityModel f where f.facilityId = :fid")
            .setParameter("fid", facilityId)
            .getSingleResult();

            // すでに存在している場合は例外をスローする
            throw new EntityExistsException();

        } catch (NoResultException e) {
            // 当たり前
        }

        // 永続化する
        // このメソッドで facility が管理された状態になる
        em.persist(facility);

        // このユーザの複合キーを生成する
        // i.e. userId = facilityId:userId(local)
        StringBuilder sb = new StringBuilder();
        sb.append(facilityId);
        sb.append(InfoModel.COMPOSITE_KEY_MAKER);
        sb.append(user.getUserId());
        user.setUserId(sb.toString());

        // 上記 Facility の Admin User を登録する
        // admin と user Role を設定する
        boolean hasAdminRole = false;
        boolean hasUserRole = false;
        Collection<RoleModel> roles = user.getRoles();
        if (roles != null) {
            for (RoleModel r : roles) {
                if (r.getRole().equals(InfoModel.ADMIN_ROLE)) {
                    hasAdminRole = true;
                } else if (r.getRole().equals(InfoModel.USER_ROLE)) {
                    hasUserRole = true;
                }
            }
        }

        if (!hasAdminRole) {
            RoleModel role = new RoleModel();
            role.setRole(InfoModel.ADMIN_ROLE);
            role.setUser(user);
            role.setUserId(user.getUserId());
            user.addRole(role);
        }

        if (!hasUserRole) {
            RoleModel role = new RoleModel();
            role.setRole(InfoModel.USER_ROLE);
            role.setUser(user);
            role.setUserId(user.getUserId());
            user.addRole(role);
        }

        // 永続化する
        // Role には User から CascadeType.ALL が設定されている
        em.persist(user);
    }

    /**
     * 放射線メソッドマスタを登録する。
     * @param c
     */
    @Override
    public void putRadMethodMaster(List<RadiologyMethodValue> c) {
        if (c != null) {
            for (RadiologyMethodValue value : c) {
                em.persist(value);
            }
        }
    }

    @Override
    public String hello() {
        return "Hello, OpenDolphin!";
    }
}

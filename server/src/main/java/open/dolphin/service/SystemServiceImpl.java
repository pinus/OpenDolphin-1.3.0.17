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
 * データベース初期化に使う session bean.
 *
 * @author pns
 */
@Stateless
public class SystemServiceImpl extends DolphinService implements SystemService {
    /**
     * 施設と管理者情報を登録する.
     * @param user 施設管理者
     */
    @Override
    public void addFacilityAdmin(UserModel user) {
        // OID をセットし施設レコードを生成する
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
        String userId = facilityId + InfoModel.COMPOSITE_KEY_MAKER + user.getUserId();
        user.setUserId(userId);

        // 上記 Facility の Admin User を登録する
        // admin と user Role を設定する
        Collection<RoleModel> roles = user.getRoles();
        boolean hasAdminRole = (roles != null) && roles.stream().map(RoleModel::getRole).anyMatch(InfoModel.ADMIN_ROLE::equals);
        boolean hasUserRole = (roles != null) && roles.stream().map(RoleModel::getRole).anyMatch(InfoModel.USER_ROLE::equals);

        // ない場合は加える
        if (!hasAdminRole) {
            RoleModel role = new RoleModel();
            role.setRole(InfoModel.ADMIN_ROLE);
            role.setUser(user);
            role.setUserId(user.getUserId());
            user.addRole(role);
        }

        // ない場合は加える
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
     * 放射線メソッドマスタを登録する.
     * @param c List of RadiologyMethodValue
     */
    @Override
    public void putRadMethodMaster(List<RadiologyMethodValue> c) {
        if (c != null) {
            c.forEach(em::persist);
        }
    }

    @Override
    public String hello() {
        return "Hello, OpenDolphin!";
    }
}

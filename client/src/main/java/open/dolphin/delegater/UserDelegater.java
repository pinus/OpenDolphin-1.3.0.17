package open.dolphin.delegater;

import java.util.ArrayList;
import java.util.List;
import open.dolphin.infomodel.UserModel;
import open.dolphin.service.UserService;

/**
 *
 * @author pns
 */
public class UserDelegater extends BusinessDelegater {

    /**
     * ユーザを検索して返す.
     * @param uid facilityId:username 型式の userId
     * @return UserModel
     */
    public UserModel getUser(String uid) {
        return getService().getUser(uid);
    }

    /**
     * 全てのユーザリストを返す
     * @return
     */
    public List<UserModel> getAllUser() {
        List<UserModel> ret = new ArrayList<>();
        ret.addAll(getService().getAllUser());
        return ret;
    }

    /**
     * ユーザを保存する.
     * @param userModel
     * @return
     */
    public int putUser(UserModel userModel) {
        return getService().addUser(userModel);
    }

    /**
     * ユーザを更新する
     * @param userModel
     * @return
     */
    public int updateUser(UserModel userModel) {
        return getService().updateUser(userModel);
    }

    /**
     * ユーザ ID を指定してユーザを削除する
     * @param uid facilityId:username 型式の userId
     * @return
     */
    public int removeUser(String uid) {
        return getService().removeUser(uid);
    }

    /**
     * ユーザの FacilityModel を更新する
     * @param user
     * @return
     */
    public int updateFacility(UserModel user) {
        return getService().updateFacility(user);
    }

    private UserService getService() {
        return getService(UserService.class);
    }
}

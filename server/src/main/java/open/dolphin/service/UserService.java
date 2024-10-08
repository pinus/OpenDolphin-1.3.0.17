package open.dolphin.service;

import open.dolphin.infomodel.UserModel;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * UserService
 *
 * @author pns
 */
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserService {
    /**
     * 施設管理者が院内Userを登録する。
     *
     * @param add 登録するUserModel
     * @return 追加件数
     */
    @POST
    @Path("addUser")
    @RolesAllowed("admin")
    int addUser(UserModel add);

    /**
     * Userを検索する。
     *
     * @param userId 検索するユーザの複合キー
     * @return 該当するUser
     */
    @POST
    @Path("getUser")
    @RolesAllowed("user")
    UserModel getUser(String userId);

    /**
     * 施設内の全Userを取得する。
     *
     * @return 施設内ユーザリスト
     */
    @POST
    @Path("getAllUser")
    @RolesAllowed("admin")
    List<UserModel> getAllUser();

    /**
     * User情報(パスワード等)を更新する。
     *
     * @param userModel 更新するUserModel
     * @return 更新件数
     */
    @POST
    @Path("updateUser")
    @RolesAllowed("user")
    int updateUser(UserModel userModel);

    /**
     * Userを削除する。
     *
     * @param removeId 削除するユーザのId
     * @return 削除件数
     */
    @POST
    @Path("removeUser")
    @RolesAllowed("admin")
    int removeUser(String removeId);

    /**
     * 施設情報を更新する。
     *
     * @param userModel 更新する UserModel
     * @return number of updated
     */
    @POST
    @Path("updateFacility")
    @RolesAllowed("admin")
    int updateFacility(UserModel userModel);
}

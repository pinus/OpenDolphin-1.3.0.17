package open.dolphin.service;

import open.dolphin.infomodel.UserModel;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    public int addUser(UserModel add);

    /**
     * Userを検索する。
     *
     * @param userId 検索するユーザの複合キー
     * @return 該当するUser
     */
    @POST
    @Path("getUser")
    @RolesAllowed("user")
    public UserModel getUser(String userId);

    /**
     * 施設内の全Userを取得する。
     *
     * @return 施設内ユーザリスト
     */
    @POST
    @Path("getAllUser")
    @RolesAllowed("admin")
    public List<UserModel> getAllUser();

    /**
     * User情報(パスワード等)を更新する。
     *
     * @param userModel 更新するUserModel
     * @return 更新件数
     */
    @POST
    @Path("updateUser")
    @RolesAllowed("user")
    public int updateUser(UserModel userModel);

    /**
     * Userを削除する。
     *
     * @param removeId 削除するユーザのId
     * @return 削除件数
     */
    @POST
    @Path("removeUser")
    @RolesAllowed("admin")
    public int removeUser(String removeId);

    /**
     * 施設情報を更新する。
     *
     * @param userModel 更新する UserModel
     * @return
     */
    @POST
    @Path("updateFacility")
    @RolesAllowed("admin")
    public int updateFacility(UserModel userModel);
}

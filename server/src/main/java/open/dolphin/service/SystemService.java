package open.dolphin.service;

import open.dolphin.infomodel.RadiologyMethodValue;
import open.dolphin.infomodel.UserModel;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * データベース初期化に使う Rest Service
 *
 * @author pns
 */
@Path("system")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public interface SystemService {
    /**
     * 施設と管理者情報を登録する。
     *
     * @param user 施設管理者
     */
    @POST
    @Path("addFacilityAdmin")
    public void addFacilityAdmin(UserModel user);

    /**
     * 放射線メソッドマスタを登録する。
     *
     * @param c
     */
    @POST
    @Path("putRadMethodMaster")
    public void putRadMethodMaster(List<RadiologyMethodValue> c);

    /**
     * 通信を確認する。
     *
     * @return Hello, OpenDolphin! 文字列
     */
    @GET
    @Path("hello")
    public String hello();
}

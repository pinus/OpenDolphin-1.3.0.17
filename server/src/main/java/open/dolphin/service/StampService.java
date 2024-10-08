package open.dolphin.service;

import open.dolphin.infomodel.PersonalTreeModel;
import open.dolphin.infomodel.PublishedTreeModel;
import open.dolphin.infomodel.StampModel;
import open.dolphin.infomodel.SubscribedTreeModel;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * StampService
 *
 * @author pns
 */
@Path("stamp")
@RolesAllowed("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface StampService {

    @Path("putTree")
    @POST
    long putTree(PersonalTreeModel model);

    @Path("getTree")
    @POST
    PersonalTreeModel getTree(Long userPk);

    @Path("getSubscribedTreeList")
    @POST
    List<PublishedTreeModel> getSubscribedTreeList(Long userPk);

    @Path("publishTree")
    @POST
    int publishTree(PublishedTreeModel model);

    @Path("cancelPublishedTree")
    @POST
    int cancelPublishedTree(PersonalTreeModel model);

    @Path("getPublishedTreeList")
    @POST
    List<PublishedTreeModel> getPublishedTreeList();

    @Path("subscribeTreeList")
    @POST
    List<Long> subscribeTreeList(List<SubscribedTreeModel> addList);

    @Path("unsubscribeTreeList")
    @POST
    int unsubscribeTreeList(List<SubscribedTreeModel> removeList);

    @Path("putStamp")
    @POST
    String putStamp(StampModel model);

    @Path("putStampList")
    @POST
    List<String> putStampList(List<StampModel> list);

    @Path("getStamp")
    @POST
    StampModel getStamp(String stampId);

    @Path("getStampList")
    @POST
    List<StampModel> getStampList(List<String> ids);

    @Path("removeStamp")
    @POST
    int removeStamp(String stampId);

    @Path("removeStampList")
    @POST
    int removeStampList(List<String> ids);
}

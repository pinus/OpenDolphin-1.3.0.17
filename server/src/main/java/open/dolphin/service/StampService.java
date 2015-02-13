package open.dolphin.service;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import open.dolphin.infomodel.PublishedTreeModel;
import open.dolphin.infomodel.StampModel;
import open.dolphin.infomodel.PersonalTreeModel;
import open.dolphin.infomodel.SubscribedTreeModel;

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
    public long putTree(PersonalTreeModel model);

    @Path("getTree")
    @POST
    public PersonalTreeModel getTree(Long userPk);

    @Path("getSubscribedTreeList")
    @POST
    public List<PublishedTreeModel> getSubscribedTreeList(Long userPk);

    @Path("publishTree")
    @POST
    public int publishTree(PublishedTreeModel model);

    @Path("cancelPublishedTree")
    @POST
    public int cancelPublishedTree(PersonalTreeModel model);

    @Path("getPublishedTreeList")
    @POST
    public List<PublishedTreeModel> getPublishedTreeList();

    @Path("subscribeTreeList")
    @POST
    public List<Long> subscribeTreeList(List<SubscribedTreeModel> addList);

    @Path("unsubscribeTreeList")
    @POST
    public int unsubscribeTreeList(List<SubscribedTreeModel> removeList);

    @Path("putStamp")
    @POST
    public String putStamp(StampModel model);

    @Path("putStampList")
    @POST
    public List<String> putStampList(List<StampModel> list);

    @Path("getStamp")
    @POST
    public StampModel getStamp(String stampId);

    @Path("getStampList")
    @POST
    public List<StampModel> getStampList(List<String> ids);

    @Path("removeStamp")
    @POST
    public int removeStamp(String stampId);

    @Path("removeStampList")
    @POST
    public int removeStampList(List<String> ids);
}

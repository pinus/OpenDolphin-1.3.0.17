package open.dolphin.delegater;

import java.util.ArrayList;
import java.util.List;
import open.dolphin.infomodel.*;
import open.dolphin.service.StampService;

/**
 *
 * @author pns
 */
public class StampDelegater extends BusinessDelegater {

    /**
     * StampTree を保存/更新する.
     * @param model 保存する StampTree
     * @return 永続化された StampTreeBean の primary key
     */
    public long putTree(PersonalTreeModel model) {
        return getService().putTree(model);
    }

    /**
     * User のスタンプツリーを読み込む.
     * User 個人のものとサブスクライブしている PublishedTree を混ぜて入れる
     * @param userPk ログインユーザの primary key
     * @return
     */
    public List<StampTreeBean> getTrees(long userPk) {
        List<StampTreeBean> treeList = new ArrayList<>();

        PersonalTreeModel personal = getService().getTree(userPk);
        if (personal != null) {
            // サブスクライブしている PublishTree を取得
            List<PublishedTreeModel> subscribed = getService().getSubscribedTreeList(userPk);

            treeList.add(personal);
            treeList.addAll(subscribed);
        }
        // personal == null の場合は空のリストを返す. StampBoxPlugin が新規 StampTree を作ってくれる.
        return treeList;
    }

    /**
     * 個人用の StampTree を保存し公開する.
     * @param model
     * @return 引数で持ってきた PersonalTreeModel の primary key
     */
    public long publishTree(PublishedTreeModel model) {
        return getService().publishTree(model);
    }

    /**
     * 公開されている Tree を削除する.
     * PersonalTreeModel を更新してから PublishedTreeModel を削除する
     * @param model 削除する PublishTree の元の PersonalTreeModel
     * @return 削除数
     */
    public int cancelPublishedTree(PersonalTreeModel model) {
        return getService().cancelPublishedTree(model);
    }

    /**
     * 公開されている PublishedTree のリストを取得する.
     * @return ローカル及びパブリックTreeのリスト
     */
    public List<PublishedTreeModel> getPublishedTrees() {
        return getService().getPublishedTreeList();
    }

    /**
     * 公開 Tree をサブスクライブする.
     * @param subscribeList サブスクライブする SubscribedTreeModel の List
     * @return
     */
    public List<Long> subscribeTrees(List<SubscribedTreeModel> subscribeList) {
        return getService().subscribeTreeList(subscribeList);
    }

    /**
     * 公開 Tree をアンサブスクライブする.
     * @param removeList アンサブスクライブする PublishedTree の Id リスト
     * @return
     */
    public int unsubscribeTrees(List<SubscribedTreeModel> removeList) {
        return getService().unsubscribeTreeList(removeList);
    }

    /**
     * Stampを保存する.
     * @param list StampModel の List
     * @return 保存件数
     */
    public List<String> putStamp(List<StampModel> list) {
        return getService().putStampList(list);
    }

    /**
     * Stampを保存する.
     * @param model StampModel
     * @return 保存件数
     */
    public String putStamp(StampModel model) {
        return getService().putStamp(model);
    }

    /**
     * Stampを取得する.
     * @param stampId 取得する StampModel の id
     * @return StampModel
     */
    public StampModel getStamp(String stampId) {
        return getService().getStamp(stampId);
    }

    /**
     * Stampを取得する.
     * @param list 取得する ModuleInfoBean の list
     * @return StampModel の list
     */
    public List<StampModel> getStamp(List<ModuleInfoBean> list) {

        List<String> ids = new ArrayList<>(list.size());

        for (ModuleInfoBean info : list) {
            ids.add(info.getStampId());
        }

        return getService().getStampList(ids);
    }

    /**
     * Stampを削除する.
     * @param stampId 削除する StampModel の id
     * @return 削除件数
     */
    public int removeStamp(String stampId) {
        return getService().removeStamp(stampId);
    }

    /**
     * Stampを削除する.
     * @param ids 削除する StampModel の id リスト
     * @return 削除件数
     */
    public int removeStamp(List<String> ids) {
        return getService().removeStampList(ids);
    }

    private StampService getService() {
        return getService(StampService.class);
    }
}

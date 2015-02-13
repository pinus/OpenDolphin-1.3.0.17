package open.dolphin.service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.PublishedTreeModel;
import open.dolphin.infomodel.StampModel;
import open.dolphin.infomodel.PersonalTreeModel;
import open.dolphin.infomodel.SubscribedTreeModel;

/**
 *
 * @author pns
 */
@Stateless
public class StampServiceImpl extends DolphinService implements StampService {
    private static final long serialVersionUID = -9201185729129886533L;

    /**
     * User 個人の StampTree を保存/更新する。
     * @param model 保存する StampTree
     * @return id
     */
    @Override
    public long putTree(PersonalTreeModel model) {
        // Tree の XML を byte[] へ変換してから永続化
        byte[] treeBytes = ModelUtils.toTreeBytes(model.getTreeXml());
        model.setTreeBytes(treeBytes);
        // 新規だと persist，更新だと merge になる
        em.merge(model);
        return model.getId();
    }

    /**
     * User個人の Tree を取得する.
     * @param userPk userId(DB key)
     * @return User 個人の Tree
     */
    @Override
    public PersonalTreeModel getTree(Long userPk) {
        // パーソナルツリーを取得する
        try {
            PersonalTreeModel tree = em.createQuery("select s from PersonalTreeModel s where s.user.id=:userPk", PersonalTreeModel.class)
                .setParameter("userPk", userPk).getSingleResult();

            // treeBytes をいじるので detach
            em.detach(tree);
            // treeBytes を treeXml に変換して返す
            String treeXml = ModelUtils.toTreeXml(tree.getTreeBytes());
            tree.setTreeXml(treeXml);
            tree.setTreeBytes(null);

            return tree;

        } catch (NoResultException e) {
            // 新規ユーザの場合ここへくる
        }
        return null;
    }

    /**
     * サブスクライブしている Tree (内容は PublishedTreeModel) の List を取得する.
     * @param userPk userId(DB key)
     * @return サブスクライブしている PublishedTree のリスト
     */
    @Override
    public List<PublishedTreeModel> getSubscribedTreeList(Long userPk) {
        List<PublishedTreeModel> ret = new ArrayList<>();

        // ユーザがサブスクライブしているStampTreeのリストを取得する
        List<SubscribedTreeModel> subscribed = em.createQuery("select s from SubscribedTreeModel s where s.user.id=:userPk", SubscribedTreeModel.class)
            .setParameter("userPk", userPk).getResultList();

        // サブスクライブリストから公開Treeを取得する
        for (SubscribedTreeModel sm : subscribed) {
            PublishedTreeModel published = em.find(PublishedTreeModel.class, sm.getTreeId());

            if (published == null) {
                // 公開Treeが削除されている場合，サブスクライブリストレコードを削除する
                em.remove(sm);

            } else {
                // treeBytes をいじるので detach
                em.detach(published);
                // treeBytes を treeXml に変換して返す
                String treeXml = ModelUtils.toTreeXml(published.getTreeBytes());
                published.setTreeXml(treeXml);
                published.setTreeBytes(null);
                // 公開Treeがあれば加える
                ret.add(published);
            }
        }
        return ret;
    }

    /**
     * 個人用の Tree を公開する.
     * @param model
     * @return 公開数 1
     */
    @Override
    public int publishTree(PublishedTreeModel model) {
        // treeXml を byte[] へ変換してから永続化
        model.setTreeBytes(ModelUtils.toTreeBytes(model.getTreeXml()));
        // 公開Treeを保存する　〜新規なら persist，既存なら merge になる
        em.merge(model);
        return 1;
    }

    /**
     * 公開したTreeを削除する。
     * @param model
     * @return 削除した数
     */
    @Override
    public int cancelPublishedTree(PersonalTreeModel model) {
        // treeXml を byte[] へ変換してから永続化
        model.setTreeBytes(ModelUtils.toTreeBytes(model.getTreeXml()));
        // 公開属性を更新する
        em.merge(model);
        // 公開Treeを削除する
        PublishedTreeModel exist = em.find(PublishedTreeModel.class, model.getId());
        em.remove(exist);

        return 1;
    }

    /**
     * 公開されているStampTreeのリストを取得する。
     * @return ローカル及びパブリックTreeのリスト
     */
    @Override
    public List<PublishedTreeModel> getPublishedTreeList() {
        // ログインユーザの施設IDを取得する
        String fid = this.getCallersFacilityId();

        List<PublishedTreeModel> ret = new ArrayList<>();

        // local に公開されているTreeを取得する
        // publishType=施設ID
        ret.addAll(em.createQuery("select p from PublishedTreeModel p where p.publishType=:fid", PublishedTreeModel.class)
            .setParameter("fid", fid).getResultList());

        // パブリックTeeを取得する
        ret.addAll(em.createQuery("select p from PublishedTreeModel p where p.publishType='global'", PublishedTreeModel.class)
            .getResultList());

        for (PublishedTreeModel published : ret) {
            // treeBytes をいじるので detach する
            em.detach(published);
            // treeBytes を treeXml に変換して返す
            String treeXml = ModelUtils.toTreeXml(published.getTreeBytes());
            published.setTreeXml(treeXml);
            published.setTreeBytes(null);
        }

        return ret;
    }

    /**
     * 公開 Tree をサブスクライブする.
     * @param addList サブスクライブする SubscribedTreeModel の List
     * @return
     */
    @Override
    public List<Long> subscribeTreeList(List<SubscribedTreeModel> addList) {
        List<Long> ret = new ArrayList<>();
        for (SubscribedTreeModel model : addList) {
            em.persist(model);
            ret.add(model.getId());
        }
        return ret;
    }

    /**
     * 公開 Tree をアンサブスクライブする。
     * @param removeList アンサブスクライブするTreeのIdリスト
     * @return
     */
    @Override
    public int unsubscribeTreeList(List<SubscribedTreeModel> removeList) {
        for (SubscribedTreeModel model : removeList) {
            SubscribedTreeModel remove = em.createQuery("select s from SubscribedTreeModel s where s.user.id=:userPk and s.treeId=:treeId", SubscribedTreeModel.class)
                .setParameter("userPk", model.getUser().getId())
                .setParameter("treeId", model.getTreeId()).getSingleResult();
            em.remove(remove);
        }
        return removeList.size();
    }

    /**
     * Stampを保存する。
     * @param model StampModel
     * @return 保存件数
     */
    @Override
    public String putStamp(StampModel model) {
        // stamp を stampBytes に変換してから永続化
        model.setStampBytes(ModelUtils.xmlEncode(model.getStamp()));
        em.persist(model);
        return model.getId();
    }

    /**
     * Stampを保存する。
     * @param list StampModel list
     * @return 保存した StampModel の Id (String) リスト
     */
    @Override
    public List<String> putStampList(List<StampModel> list) {
        List<String> ret = new ArrayList<>();
        for (StampModel model : list) {
            ret.add(putStamp(model));
        }
        return ret;
    }

    /**
     * Stampを取得する。
     * @param stampId 取得する StampModel の id
     * @return StampModel
     */
    @Override
    public StampModel getStamp(String stampId) {
        StampModel model = em.find(StampModel.class, stampId);
        // 永続化オブジェクトをいじる前に detach する
        em.detach(model);
        // StampBytes をデコードした StampModel をセットしてから返す
        model.setStamp((IInfoModel) ModelUtils.xmlDecode(model.getStampBytes()));
        model.setStampBytes(null);
        return model;
    }

    /**
     * Stampを取得する。
     * @param ids 取得する StampModel の id リスト
     * @return StampModel
     */
    @Override
    public List<StampModel> getStampList(List<String> ids) {
        List<StampModel> ret = new ArrayList<>();
        for (String stampId : ids) {
            ret.add(getStamp(stampId));
        }
        return ret;
    }

    /**
     * Stampを削除する。
     * @param stampId 削除する StampModel の id
     * @return 削除件数 1 or 0
     */
    @Override
    public int removeStamp(String stampId) {
        StampModel exist = em.find(StampModel.class, stampId);
        // zombie stamp returns null
        if (exist != null) {
            em.remove(exist);
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Stampを削除する。
     * @param ids 削除する StampModel の id List
     * @return 削除件数
     */
    @Override
    public int removeStampList(List<String> ids) {
        int cnt = 0;
        for (String stampId : ids) {
            cnt += removeStamp(stampId);
        }
        return cnt;
    }
}

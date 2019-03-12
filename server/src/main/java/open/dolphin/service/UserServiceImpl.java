package open.dolphin.service;

import open.dolphin.infomodel.*;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserServiceImpl.
 *
 * @author pns
 */
@Stateless
public class UserServiceImpl extends DolphinService implements UserService {
    private static final long serialVersionUID = 1L;

    /**
     * 施設管理者が院内 User を登録する.
     * @param add 登録する User
     * @return 登録した User 数 1
     */
    @Override
    public int addUser(UserModel add) {
        try {
            // 既存ユーザの場合は例外をスローする
            getUser(add.getUserId());
            throw new EntityExistsException();
        } catch (NoResultException e) {
        }
        em.persist(add);
        return 1;
    }

    /**
     * User を検索する.
     * @param userId 検索するユーザの複合キー
     * @return 該当する User
     */
    @Override
    public UserModel getUser(String userId) {
        UserModel user = em.createQuery("select u from UserModel u where u.userId = :uid", UserModel.class)
            .setParameter("uid", userId).getSingleResult();

        if (user.getMemberType() != null && user.getMemberType().equals("EXPIRED")) {
            throw new SecurityException("Expired User");
        }

        return user;
    }

    /**
     * 施設内の全 User を取得する.
     *
     * @return 施設内ユーザリスト
     */
    @Override
    public List<UserModel> getAllUser() {
        List<UserModel> results = em.createQuery("select u from UserModel u where u.userId like :fid", UserModel.class)
            .setParameter("fid", getCallersFacilityId()+"%").getResultList();

        return results.stream()
                .filter(user -> (user != null && user.getMemberType() != null && (!user.getMemberType().equals("EXPIRED")))).collect(Collectors.toList());
    }

    /**
     * User 情報(パスワード等)を更新する.
     * @param update 更新する UserModel
     * @return 更新したユーザの数 1
     */
    @Override
    public int updateUser(UserModel update) {
        checkFacility(update.getUserId());
        UserModel current = em.find(UserModel.class, update.getId());
        update.setMemberType(current.getMemberType());
        update.setRegisteredDate(current.getRegisteredDate());
        em.merge(update);
        return 1;
    }

    /**
     * User を削除する.
     * @param removeId 削除するユーザの Id
     * @return 削除したユーザの数 1
     */
    @Override
    public int removeUser(String removeId) {

        UserModel remove = getUser(removeId);
        long removePk = remove.getId();

        // Stamp を削除する
        List<StampModel> stamps = em.createQuery("select s from StampModel s where s.userId = :pk", StampModel.class)
            .setParameter("pk", removePk).getResultList();
        for (StampModel stamp : stamps) {
            em.remove(stamp);
        }

        // Subscribed Tree を削除する
        List<SubscribedTreeModel> subscribedTrees = em.createQuery("select s from SubscribedTreeModel s where s.user.id = :pk", SubscribedTreeModel.class)
            .setParameter("pk", removePk).getResultList();
        for (SubscribedTreeModel tree : subscribedTrees) {
            em.remove(tree);
        }

        // PublishedTree を削除する
        List<PublishedTreeModel> publishedTrees = em.createQuery("select p from PublishedTreeModel p where p.user.id = :pk", PublishedTreeModel.class)
            .setParameter("pk", removePk).getResultList();
        for (PublishedTreeModel tree : publishedTrees) {
            em.remove(tree);
        }

        // PersonalTree を削除する
        PersonalTreeModel stampTree = em.createQuery("select s from PersonalTreeModel s where s.user.id = :pk", PersonalTreeModel.class)
            .setParameter("pk", removePk).getSingleResult();
        em.remove(stampTree);

        // ユーザを削除する
        String note = String.valueOf(new Date());
        remove.setMemo(note);
        remove.setPassword("38afd7ae34bd5e3e6fc170d8b09178a3"); // EXPIRED
        remove.setMemberType("EXPIRED");

        // UserModel は削除しない。ユーザーを inactivate しても、カルテ記録者情報を残しておかねばならない。by masuda-sensei
        em.merge(remove);

        boolean deleteDoc = false; // Document は消さない設定になってる
        if (deleteDoc) {
            // Document, Module, Image (Cascade)
            List<DocumentModel> documents =
                    em.createQuery("select d from DocumentModel d where d.creator.id = :removeId", DocumentModel.class)
                    .setParameter("removeId", removePk).getResultList();
            System.out.println(documents.size() + " 件のドキュメントを削除します。");

            // Document を削除すれば ModuleとImageはカスケード削除される
            for (DocumentModel document : documents) { em.remove(document); }

            // Diagnosis
            List<RegisteredDiagnosisModel> rds =
                    em.createQuery("select d from RegisteredDiagnosisModel d where d.creator.id = :removeId", RegisteredDiagnosisModel.class)
                    .setParameter("removeId", removePk).getResultList();
            System.out.println(rds.size() + " 件の傷病名を削除します。");
            for (RegisteredDiagnosisModel rd : rds) { em.remove(rd); }

            // Observation
            List<ObservationModel> observations =
                    em.createQuery("select o from ObservationModel o where o.creator.id = :removeId", ObservationModel.class)
                    .setParameter("removeId", removePk).getResultList();
            System.out.println(observations.size() + " 件の観測を削除します。");
            for (ObservationModel observation : observations) { em.remove(observation); }

            // 患者メモ
            List<PatientMemoModel> memos =
                    em.createQuery("select o from PatientMemoModel o where o.creator.id = :removeId", PatientMemoModel.class)
                    .setParameter("removeId", removePk).getResultList();
            System.out.println(memos.size() + " 件の患者メモを削除します。");
            for (PatientMemoModel memo : memos) { em.remove(memo); }

            // 予約
            List<AppointmentModel> appos =
                    em.createQuery("select o from AppointmentModel o where o.creator.id = :removeId", AppointmentModel.class)
                    .setParameter("removeId", removePk).getResultList();
            System.out.println(appos.size() + " 件の予約を削除します。");
            for (AppointmentModel appo : appos) { em.remove(appo); }

            // ラボ
            List<LaboModuleValue> labos =
                    em.createQuery("select o from LaboModuleValue o where o.creator.id = :removeId", LaboModuleValue.class)
                    .setParameter("removeId", removePk).getResultList();
            System.out.println(labos.size() + " 件のラボを削除します。");
            for (LaboModuleValue lb : labos) { em.remove(lb); }

            // UserModel 削除
            em.remove(remove);
        }

        return 1;
    }

    /**
     * 施設情報を更新する.
     * @param update 更新する User
     * @return 更新した User 数 1
     */
    @Override
    public int updateFacility(UserModel update) {
        checkFacility(update.getUserId());
        FacilityModel updateFacility = update.getFacilityModel();
        FacilityModel current = em.find(FacilityModel.class, updateFacility.getId());
        updateFacility.setMemberType(current.getMemberType());
        updateFacility.setRegisteredDate(current.getRegisteredDate());
        em.merge(updateFacility );
        return 1;
    }
}

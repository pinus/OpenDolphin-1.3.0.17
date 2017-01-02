package open.dolphin.client;

import java.awt.Component;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import open.dolphin.dao.OrcaEntry;
import open.dolphin.dao.OrcaMasterDao;
import open.dolphin.helper.Task;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.project.Project;

/**
 * ORCA StampTree クラス.
 *
 * @author Kazushi Minagawa
 */
public class OrcaTree extends StampTree {
    private static final long serialVersionUID = 1L;

    private static final String MONITOR_TITLE = "ORCAセット検索";
    private static final String MONITOR_NOTE = "ORCAに接続中";

    /** ORCA 入力セットをフェッチしたかどうかのフラグ */
    private boolean fetched;

    public OrcaTree(TreeModel model) {
        super(model);
    }

    /**
     * StampBox のタブでこのTreeが選択された時コールされる.
     */
    @Override
    public void enter() {

        if (!fetched) {
            String address = Project.getClaimAddress();
            if (address == null || address.equals("")) return;
            fetchOrcaInputCd();
        }
    }

    private void fetchOrcaInputCd() {
        Component c = SwingUtilities.getWindowAncestor(this);
        String message = MONITOR_TITLE;
        String updateMsg = MONITOR_NOTE;
        int masEstimation = 30000;

        Task task = new Task<Boolean>(c, message, updateMsg, masEstimation) {

            @Override
            protected Boolean doInBackground() throws Exception {
                OrcaMasterDao dao = new OrcaMasterDao();

                List<OrcaEntry> entries = dao.getOrcaInputCdList();
                StampTreeNode root = (StampTreeNode) getModel().getRoot();

                for (OrcaEntry entry : entries) {
                    ModuleInfoBean stampInfo = entry.getStampInfo();
                    StampTreeNode node = new StampTreeNode(stampInfo);
                    root.add(node);
                }

                DefaultTreeModel model = (DefaultTreeModel) getModel();
                model.reload(root);

                return true;
            }

            @Override
            protected void succeeded(Boolean result) {
                fetched = true;
            }

            @Override
            protected void cancelled() {
                System.out.println("OrcaTree: Canceled");
                fetched = true;

            }
            @Override
            protected void failed(Throwable cause) {
                System.out.println("OrcaTree: failed " + cause);
                fetched = true;
            }
            @Override
            protected void interrupted(InterruptedException ex) {
                System.out.println("OrcaTree: interrupted " + ex);
                fetched = true;
            }
        };
        task.execute();
    }
}

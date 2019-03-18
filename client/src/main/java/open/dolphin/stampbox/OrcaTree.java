package open.dolphin.stampbox;

import open.dolphin.delegater.OrcaDelegater;
import open.dolphin.helper.Task;
import open.dolphin.infomodel.ModuleInfoBean;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.util.List;

/**
 * ORCA StampTree クラス.
 *
 * @author Kazushi Minagawa
 */
public class OrcaTree extends StampTree {
    private static final long serialVersionUID = 1L;

    private static final String MONITOR_TITLE = "ORCAセット検索";
    private static final String MONITOR_NOTE = "ORCAに接続中";

    public OrcaTree(TreeModel model) {
        super(model);
    }

    /**
     * StampBox のタブでこのTreeが選択された時コールされる.
     */
    @Override
    public void enter() {
        super.enter();
    }

    public void fetchOrcaInputCd() {

        Component c = SwingUtilities.getWindowAncestor(this);
        String message = MONITOR_TITLE;
        String updateMsg = MONITOR_NOTE;
        int masEstimation = 30000;

        Task<Boolean> task = new Task<Boolean>(c, message, updateMsg, masEstimation) {

            @Override
            protected Boolean doInBackground() throws Exception {
                OrcaDelegater delegater = new OrcaDelegater();
                List<ModuleInfoBean> beans = delegater.getOrcaInputCdList();

                StampTreeNode root = (StampTreeNode) getModel().getRoot();
                beans.stream().map(stampInfo -> new StampTreeNode(stampInfo)).forEach(root::add);

                DefaultTreeModel model = (DefaultTreeModel) getModel();
                model.reload(root);

                return true;
            }

            @Override
            protected void succeeded(Boolean result) {
                //fetched = true;
            }


            @Override
            protected void cancelled() {
                System.out.println("OrcaTree: Canceled");
                //fetched = true;

            }

            @Override
            protected void failed(Throwable cause) {
                System.out.println("OrcaTree: failed " + cause);
                //fetched = true;
            }

            @Override
            protected void interrupted(InterruptedException ex) {
                System.out.println("OrcaTree: interrupted " + ex);
                //fetched = true;
            }
        };
        task.execute();
    }
}

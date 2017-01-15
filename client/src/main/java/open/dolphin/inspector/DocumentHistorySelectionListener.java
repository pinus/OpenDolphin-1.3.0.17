package open.dolphin.inspector;

import java.util.EventListener;
import open.dolphin.infomodel.DocInfoModel;

/**
 *
 * @author pns
 */
public interface DocumentHistorySelectionListener extends EventListener {

    public void selected(DocInfoModel[] docInfoModel);
}

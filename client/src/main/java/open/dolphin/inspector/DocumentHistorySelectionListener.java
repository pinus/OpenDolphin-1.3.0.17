package open.dolphin.inspector;

import open.dolphin.infomodel.DocInfoModel;

import java.util.EventListener;

/**
 *
 * @author pns
 */
public interface DocumentHistorySelectionListener extends EventListener {

    public void selected(DocInfoModel[] docInfoModel);
}

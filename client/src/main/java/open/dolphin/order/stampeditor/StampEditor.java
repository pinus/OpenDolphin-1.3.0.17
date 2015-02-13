package open.dolphin.order.stampeditor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import open.dolphin.client.ClientContext;
import open.dolphin.client.GUIConst;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.order.ClaimConst;
import open.dolphin.order.IStampEditor;
import open.dolphin.order.MasterSearchPanel;
import open.dolphin.order.tablepanel.DiagnosisTablePanel;
import open.dolphin.order.tablepanel.ItemTablePanel;
import open.dolphin.order.tablepanel.RadiologyTablePanel;
import open.dolphin.order.tablepanel.RecipeTablePanel;

/**
 * StampEditor
 * StampEditorProxyPanel では，これを必要なだけインスタンス化して保持する
 * @author pns
 */
public class StampEditor extends JPanel implements IStampEditor {
    private static final long serialVersionUID = 1L;

    public static final String VALID_DATA_PROP = "validData";

    private final PropertyChangeSupport boundSupport;
    private boolean isValidModel;
    private String title;
    private ItemTablePanel tablePanel;
    private MasterSearchPanel masterSearchPanel;
    private String entity;

    public StampEditor(String entity) {
        this.entity = entity;
        boundSupport = new PropertyChangeSupport(this);
    }

    @Override
    public void start() {

        // タイトル設定
        this.setTitle(ClaimConst.EntityNameMap.get(entity));

        // TablePanel 作成
        if (IInfoModel.ENTITY_MED_ORDER.equals(entity)) {
            tablePanel = new RecipeTablePanel(this);
        } else if (IInfoModel.ENTITY_RADIOLOGY_ORDER.equals(entity)) {
            tablePanel = new RadiologyTablePanel(this);
        } else if (IInfoModel.ENTITY_DIAGNOSIS.equals(entity)) {
            tablePanel = new DiagnosisTablePanel(this);
        } else {
            tablePanel = new ItemTablePanel(this);
        }

        // MasterSearchPanel 作成
        masterSearchPanel = new MasterSearchPanel(entity);
        masterSearchPanel.addPropertyChangeListener(tablePanel);

        // CLAIM パラメータを設定する
        tablePanel.setOrderName(ClaimConst.EntityNameMap.get(entity));
        tablePanel.setClassCode(ClaimConst.ClaimClassCodeMap.get(entity));
        tablePanel.setClassCodeId(ClaimConst.CLASS_CODE_ID);
        tablePanel.setSubClassCodeId(ClaimConst.SUBCLASS_CODE_ID);

        // 上にスタンプのセットパネル、下にマスタのセットパネルを配置する
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(tablePanel);
        add(masterSearchPanel);
        setPreferredSize(GUIConst.DEFAULT_STAMP_EDITOR_SIZE);
    }

    /**
     * StampEditorProxyPanel を show した際に呼ばれる
     */
    @Override
    public void enter() {
        tablePanel.checkState(); // enter したときの 右ボタンの制御
        masterSearchPanel.requestFocusOnTextField();
    }

    @Override
    public void dispose() {
        masterSearchPanel.removePropertyChangeListener(tablePanel);
    }

    /**
     * TablePanel をセットする
     * @param tablePanel
     */
    public void setTablePanel(ItemTablePanel tablePanel) {
        this.tablePanel = tablePanel;
    }

    /**
     * TablePanel を返す
     * @return
     */
    public ItemTablePanel getTablePanel() {
        return tablePanel;
    }

    public void setMasterSearchPanel(MasterSearchPanel panel) {
        masterSearchPanel = panel;
    }

    public MasterSearchPanel getMasterSearchPanel() {
        return masterSearchPanel;
    }

    /**
     * Entity を TablePanel に伝える
     * @param entity
     */
    @Override
    public void setEntity(String entity) {
        this.entity = entity;
        tablePanel.setEntity(entity);
    }

    /**
     * セットされたエンティティーを返す
     * @return
     */
    public String getEntity() {
        return entity;
    }

    /**
     * TablePanel からデータを取り出す
     * @return
     */
    @Override
    public Object getValue() {
        return tablePanel.getValue();
    }

    /**
     * TablePanel にデータをセットする
     * @param val
     */
    @Override
    public void setValue(Object val) {
        tablePanel.setValue(val);
    }

    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.addPropertyChangeListener(prop, l);
    }

    @Override
    public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.removePropertyChangeListener(prop, l);
    }

    @Override
    public boolean isValidModel() {
        return isValidModel;
    }

    @Override
    public void setValidModel(boolean b) {
        isValidModel = b;
        // 強制 fire (→ tab 切り替え時に右矢印ボタンを制御するため)
        boundSupport.firePropertyChange(VALID_DATA_PROP, null, isValidModel);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String val) {
        StringBuilder buf = new StringBuilder();
        buf.append(val);
        buf.append(ClientContext.getString("application.title.editorText"));
        buf.append(ClientContext.getString("application.title.separator"));
        buf.append(ClientContext.getString("application.title"));
        this.title = buf.toString();
    }
}

package open.dolphin.stampbox;

import javax.swing.*;
import java.util.List;

/**
 * Stamp からメニューを作って格納するための model.
 * <pre>
 * root -- subMenu
 *      |- subMenu
 *      |- subMenu
 *      |- rootItem
 *      |- rootItem
 *      +- rootItem
 * </pre>
 *
 * @author pns
 */
public class StampTreeMenuModel {

    private String entity;
    private JMenu root;
    private List<JMenuItem> rootItems;
    private List<JMenu> subMenus;

    /**
     * @return the entity
     */
    public String getEntity() {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * @return the rootMenu
     */
    public JMenu getRoot() {
        return root;
    }

    /**
     * @param rootMenu the rootMenu to set
     */
    public void setRoot(JMenu rootMenu) {
        this.root = rootMenu;
    }

    /**
     * @return the rootItems
     */
    public List<JMenuItem> getRootItems() {
        return rootItems;
    }

    /**
     * @param rootItems the rootItems to set
     */
    public void setRootItems(List<JMenuItem> rootItems) {
        this.rootItems = rootItems;
    }

    /**
     * @return the subMenus
     */
    public List<JMenu> getSubMenus() {
        return subMenus;
    }

    /**
     * @param subMenus the subMenus to set
     */
    public void setSubMenus(List<JMenu> subMenus) {
        this.subMenus = subMenus;
    }
}

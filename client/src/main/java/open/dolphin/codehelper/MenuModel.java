package open.dolphin.codehelper;

import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Stamp からメニューを作って格納する model.
 * subMenus: 子に JMenuItem を入れる JMenu を集めたもの.
 * rootItems: サブメニューに入らない JMenuItem を集めたもの.
 * @author pns
 */
public class MenuModel {

    private List<JMenuItem> rootItems;
    private List<JMenu> subMenus;

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

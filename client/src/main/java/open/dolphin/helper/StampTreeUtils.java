package open.dolphin.helper;

import open.dolphin.infomodel.PersonalTreeModel;
import open.dolphin.infomodel.PublishedTreeModel;
import open.dolphin.stampbox.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * StampTreeUtils.
 *
 * @author pns
 */
public class StampTreeUtils {

    /**
     * PersonalTreeModel をコピーして Publish 用の treeXml をセットした PublishedTreeModel を作る.
     *
     * @param model PersonalTreeModel
     * @param treeXml TreeXML
     * @return PublishedTreeModel
     */
    public static PublishedTreeModel createPublishedTreeModel(PersonalTreeModel model, String treeXml) {
        PublishedTreeModel publishedModel = new PublishedTreeModel();
        // PersonalTreeModel をコピー
        publishedModel.setId(model.getId());
        publishedModel.setUser(model.getUser());
        publishedModel.setName(model.getName());
        publishedModel.setPublishType(model.getPublishType());
        publishedModel.setCategory(model.getCategory());
        publishedModel.setPartyName(model.getPartyName());
        publishedModel.setUrl(model.getUrl());
        publishedModel.setDescription(model.getDescription());
        publishedModel.setPublishedDate(model.getPublishedDate());
        publishedModel.setLastUpdated(model.getLastUpdated());
        // treeXml だけ，publish 用のをセット
        publishedModel.setTreeXml(treeXml);

        return publishedModel;
    }

    /**
     * StampTree のリストを XML にエンコードする
     *
     * @param tree List of StampTree
     * @return XML
     */
    public static String xmlEncode(List<StampTree> tree) {
        DefaultStampTreeXmlBuilder builder = new DefaultStampTreeXmlBuilder();
        StampTreeXmlDirector director = new StampTreeXmlDirector(builder);
        return director.build(tree);
    }

    /**
     * TreeXml を DefaultStampTreeBuilder でデコードして StampTree のリストを得る.
     *
     * @param treeXml XML
     * @return List of StampTree
     */
    public static List<StampTree> xmlDecode(String treeXml) {
        return xmlDecode(treeXml, new DefaultStampTreeBuilder());
    }

    /**
     * 指定した builder を使って xmlDecode する
     *
     * @param treeXml XML
     * @param builder StampTreeBuilder
     * @return List of StampTree
     */
    public static List<StampTree> xmlDecode(String treeXml, AbstractStampTreeBuilder builder) {
        try (BufferedReader reader = new BufferedReader(new StringReader(treeXml))) {
            StampTreeDirector director = new StampTreeDirector(builder);
            return director.build(reader);

        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}

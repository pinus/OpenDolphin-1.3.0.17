package open.dolphin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import open.dolphin.client.AbstractStampTreeBuilder;
import open.dolphin.client.DefaultStampTreeBuilder;
import open.dolphin.client.DefaultStampTreeXmlBuilder;
import open.dolphin.client.StampTree;
import open.dolphin.client.StampTreeDirector;
import open.dolphin.client.StampTreeXmlDirector;
import open.dolphin.infomodel.PersonalTreeModel;
import open.dolphin.infomodel.PublishedTreeModel;

/**
 *
 * @author pns
 */
public class StampTreeUtils {

    /**
     * PersonalTreeModel をコピーして Publish 用の treeXml をセットした PublishedTreeModel を作る.
     * @param model
     * @param treeXml
     * @return
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
     * @param tree
     * @return
     */
    public static String xmlEncode(List<StampTree> tree) {
        DefaultStampTreeXmlBuilder builder = new DefaultStampTreeXmlBuilder();
        StampTreeXmlDirector director = new StampTreeXmlDirector(builder);
        return director.build(tree);
    }

    /**
     * TreeXml を DefaultStampTreeBuilder でデコードして StampTree のリストを得る.
     * @param treeXml
     * @return
     */
    public static List<StampTree> xmlDecode(String treeXml) {
        return xmlDecode(treeXml, new DefaultStampTreeBuilder());
    }

    /**
     * 指定した builder を使って xmlDecode する
     * @param treeXml
     * @param builder
     * @return
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

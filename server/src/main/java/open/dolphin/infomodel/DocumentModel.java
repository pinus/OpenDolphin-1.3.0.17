package open.dolphin.infomodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.persistence.*;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * DocumentBean
 *
 * @author Minagawa,Kazushi
 *
 */
@Indexed(index="document")      // hibernate search
@Entity
@Table(name = "d_document")
public class DocumentModel extends KarteEntryBean {
    private static final long serialVersionUID = 8273677751373923433L;

    @Embedded
    private DocInfoModel docInfo;

    @IndexedEmbedded        // hibernate search
    @OneToMany(mappedBy="document", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    private Collection<ModuleModel> modules;

    @OneToMany(mappedBy="document", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    private Collection<SchemaModel> schema;

    public DocumentModel() {
        docInfo = new DocInfoModel();
        docInfo.setDocType(DOCTYPE_KARTE);
    }

    public void toDetach() {
        docInfo.setDocPk(getId());
        docInfo.setParentPk(getLinkId());
        docInfo.setConfirmDate(getConfirmed());
        docInfo.setFirstConfirmDate(getStarted());
        docInfo.setStatus(getStatus());
    }

    public void toPersist() {
        setLinkId(docInfo.getParentPk());
        setLinkRelation(docInfo.getParentIdRelation());
        setConfirmed(docInfo.getConfirmDate());
        setFirstConfirmed(docInfo.getFirstConfirmDate());
        setStatus(docInfo.getStatus());
    }

    /**
     * 文書情報を返す。
     * @return 文書情報
     */
    public DocInfoModel getDocInfo() {
        return docInfo;
    }

    /**
     * 文書情報を設定する。
     * @param docInfo 文書情報
     */
    public void setDocInfo(DocInfoModel docInfo) {
        this.docInfo = docInfo;
    }

    /**
     * シェーマを返す。
     * @return シェーマ
     */
    public Collection<SchemaModel> getSchema() {
        return schema;
    }

    /**
     * シェーマを設定する。
     * @param images シェーマ
     */
    public void setSchema(Collection<SchemaModel> images) {
        this.schema = images;
    }

    /**
     * シェーマを追加する。
     * @param model シェーマ
     */
    public void addSchema(SchemaModel model) {
        if (this.schema == null) {
            this.schema = new ArrayList<>();
        }
        this.schema.add(model);
    }

    /**
     * シェーマコレクションをクリアする。
     */
    public void clearSchema() {
        if (schema != null && schema.size() > 0) {
            schema.clear();
        }
    }


    public SchemaModel getSchema(int index) {
        if (schema != null && schema.size() > 0) {
            int cnt = 0;
            for (SchemaModel bean : schema) {
                if (index == cnt) {
                    return bean;
                }
                cnt++;
            }
        }
        return null;
    }

    /**
     * モジュールを返す。
     * @return モジュール
     */
    public Collection<ModuleModel> getModules() {
        return modules;
    }

    /**
     * モジュールを設定する。
     * @param modules モジュール
     */
    public void setModules(Collection<ModuleModel> modules) {
        this.modules = modules;
    }

    /**
     * モジュールモデルの配列を追加する。
     * @param addArray モジュールモデルの配列
     */
    public void addModule(ModuleModel[] addArray) {
        if (modules == null) {
            modules = new ArrayList<>(addArray.length);
        }
        modules.addAll(Arrays.asList(addArray));
    }

    /**
     * モジュールモデルを追加する。
     * @param addModule モジュールモデル
     */
    public void addModule(ModuleModel addModule) {
        if (modules == null) {
            modules = new ArrayList<>();
        }
        modules.add(addModule);
    }

    /**
     * モジュールをクリアする。
     */
    public void clearModules() {
        if (modules != null && modules.size() > 0) {
            modules.clear();
        }
    }

    /**
     * 引数のエンティティを持つモジュールモデルを返す。
     * @param entityName エンティティの名前
     * @return 該当するモジュールモデル
     */
    public ModuleModel getModule(String entityName) {

        if (modules != null) {

            ModuleModel ret = null;

            for (ModuleModel model : modules) {
                if (model.getModuleInfo().getEntity().equals(entityName)) {
                    ret = model;
                    break;
                }
            }
            return ret;
        }

        return null;
    }

    /**
     * 引数のエンティティ名を持つモジュール情報を返す。
     * @param entityName エンティティの名前
     * @return モジュール情報
     */
    public ModuleInfoBean[] getModuleInfo(String entityName) {

        if (modules != null) {

            ArrayList<ModuleInfoBean> list = new ArrayList<>(2);

            for (ModuleModel model : modules) {

                if (model.getModuleInfo().getEntity().equals(entityName)) {
                    list.add(model.getModuleInfo());
                }
            }

            if (list.size() > 0) {
                return  list.toArray(new ModuleInfoBean[list.size()]);
            }
        }

        return null;
    }
}

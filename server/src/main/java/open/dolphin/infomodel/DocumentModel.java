package open.dolphin.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * DocumentModel.
 *
 * @author Minagawa,Kazushi
 *
 */
@Indexed(index="document")      // hibernate search
@Entity
@Table(name = "d_document")
public class DocumentModel extends KarteEntryBean<DocumentModel> {
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

    /**
     * DocumentModel から DocInfoModel に情報をコピーする.
     */
    public void toDetach() {
        docInfo.setDocPk(getId());
        docInfo.setParentPk(getLinkId());
        docInfo.setConfirmDate(getConfirmed());
        docInfo.setFirstConfirmDate(getStarted());
        docInfo.setStatus(getStatus());
    }

    /**
     * DocInfoModel から DocumentModel に情報をコピーする.
     */
    public void toPersist() {
        setLinkId(docInfo.getParentPk());
        setLinkRelation(docInfo.getParentIdRelation());
        setConfirmed(docInfo.getConfirmDate());
        setFirstConfirmed(docInfo.getFirstConfirmDate());
        setStatus(docInfo.getStatus());
    }

    /**
     * 文書情報を返す.
     * @return 文書情報 DocInfoModel
     */
    public DocInfoModel getDocInfo() {
        return docInfo;
    }

    /**
     * 文書情報を設定する.
     * @param docInfo 文書情報 DocInfoModel
     */
    public void setDocInfo(DocInfoModel docInfo) {
        this.docInfo = docInfo;
    }

    /**
     * SchemaModel の Collection を返す.
     * @return Collection of SchemaModel
     */
    public Collection<SchemaModel> getSchema() {
        return schema;
    }

    /**
     * SchemaModel の Collection を設定する.
     * @param images Collection of SchemaModel
     */
    public void setSchema(Collection<SchemaModel> images) {
        this.schema = images;
    }

    /**
     * SchemaModel を追加する.
     * @param model SchemaModel
     */
    public void addSchema(SchemaModel model) {
        if (this.schema == null) {
            this.schema = new ArrayList<>();
        }
        this.schema.add(model);
    }

    /**
     * SchemaModel の Collection をクリアする.
     */
    public void clearSchema() {
        if (schema != null) {
            schema.clear();
        }
    }

    /**
     * index 番目の SchemaModel を取り出す.
     * Collection の実体は ArrayList
     * @param index 取り出す index
     * @return 取り出された SchemaModel. ない場合は null.
     */
    public SchemaModel getSchema(int index) {
        if (schema != null) {
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
     * ModuleModel の Collection を返す.
     * @return Collection of ModuleModel
     */
    public Collection<ModuleModel> getModules() {
        return modules;
    }

    /**
     * ModuleModel の Collection を設定する.
     * @param modules Collection of ModuleModel
     */
    public void setModules(Collection<ModuleModel> modules) {
        this.modules = modules;
    }

    /**
     * ModuleModel を追加する.
     * @param addModule ModuleModel
     */
    public void addModule(ModuleModel addModule) {
        if (modules == null) {
            modules = new ArrayList<>();
        }
        modules.add(addModule);
    }

    /**
     * ModuleModel の Collection をクリアする.
     */
    public void clearModules() {
        if (modules != null) {
            modules.clear();
        }
    }

    /**
     * 引数のエンティティを持つ ModuleModel を返す.
     * @param entityName エンティティの名前
     * @return 該当するモジュールモデル. ない場合は null.
     */
    public ModuleModel getModule(String entityName) {

        if (modules != null) {
            for (ModuleModel model : modules) {
                if (model.getModuleInfo().getEntity().equals(entityName)) {
                    return model;
                }
            }
        }
        return null;
    }

    /**
     * 引数のエンティティ名を持つ ModuleInfoBean を返す.
     * @param entityName エンティティの名前
     * @return モジュール情報 ModuleInfoBean. なければ null.
     */
    public ModuleInfoBean[] getModuleInfo(String entityName) {

        if (modules != null) {

            List<ModuleInfoBean> list = modules.stream()
                    .map(ModuleModel::getModuleInfo)
                    .filter(moduleInfo -> moduleInfo.getEntity().equals(entityName))
                    .collect(Collectors.toList());

            if (! list.isEmpty()) {
                return list.toArray(new ModuleInfoBean[list.size()]);
            }
        }
        return null;
    }
}

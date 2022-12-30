package open.dolphin.infomodel;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import jakarta.persistence.*;

/**
 * ModuleModel.
 * Field 'model' contains any of BundleMed, BundleDolphin, or ProgressCourse
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Entity
@Table(name = "d_module")
public class ModuleModel extends KarteEntryBean<ModuleModel> {
    private static final long serialVersionUID = -8781968977231876023L;

    @Embedded
    private ModuleInfoBean moduleInfo;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
        @JsonSubTypes.Type(BundleDolphin.class),
        @JsonSubTypes.Type(BundleMed.class),
        @JsonSubTypes.Type(TextStampModel.class),
        @JsonSubTypes.Type(ProgressCourse.class)
    })
    @Transient
    private IInfoModel model;

    @Lob
    @Column(nullable = false)
    //@FullTextField(valueBridge = @ValueBridgeRef(type = ModuleModelBridge.class))   // hibernate search
    private byte[] beanBytes;

    /**
     * HibernateSearch6 ValueBridge で byte[] が byte になってしまう.
     * String field をでっち上げて, KarteServiceImpl#setBeanBytes からここに書き込んで @FullTextField させる.
     */
    @Transient
    @FullTextField   // hibernate search
    private String fullText;

    @ManyToOne
    @JoinColumn(name = "doc_id", nullable = false)
    private DocumentModel document;

    public ModuleModel() {
        moduleInfo = new ModuleInfoBean();
    }

    public DocumentModel getDocument() {
        return document;
    }

    public void setDocument(DocumentModel document) {
        this.document = document;
    }

    public ModuleInfoBean getModuleInfo() {
        return moduleInfo;
    }

    public void setModuleInfo(ModuleInfoBean moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public IInfoModel getModel() {
        return model;
    }

    public void setModel(IInfoModel model) { this.model = model; }

    public byte[] getBeanBytes() {
        return beanBytes;
    }

    public void setBeanBytes(byte[] beanBytes) {
        this.beanBytes = beanBytes;
    }

    public void setFullText(String s) {
        fullText = s;
    }

    @Override
    public int compareTo(ModuleModel other) {
        if (other != null && getClass() == other.getClass()) {
            ModuleInfoBean moduleInfo1 = getModuleInfo();
            ModuleInfoBean moduleInfo2 = other.getModuleInfo();
            return moduleInfo1.compareTo(moduleInfo2);
        }
        return -1;
    }
}

package open.dolphin.infomodel;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import open.dolphin.util.ModelUtils;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import jakarta.persistence.*;
import java.util.Objects;

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
    //@FullTextField(valueBridge = @ValueBridgeRef(type = ModuleModelValueBridge.class))   // hibernate search
    private byte[] beanBytes;

    /**
     * HibernateSearch6 ValueBridge で byte[] が byte になってしまう workaround.
     * String field をでっち上げて setBeanBytes からここに書き込んで @FullTextField させる.
     */
    @Transient
    @FullTextField(analyzer = "japanese")  // hibernate search
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

        // FullTextField を Index させるためのでっちあげ
        if (Objects.nonNull(beanBytes)) { setFullText(beanBytesToString(beanBytes)); }
    }

    /**
     * BeanBytes を String に変換.
     * @param beanBytes beanBytes
     * @return String
     */
    private String beanBytesToString(byte[] beanBytes) {
        InfoModel im = (InfoModel) ModelUtils.xmlDecode(beanBytes);
        if (im instanceof ProgressCourse progressCourse) {
            String xml = progressCourse.getFreeText();
            return ModelUtils.extractText(xml);
        } else {
            return im.toString();
        }
    }

    /**
     * この Module Model の full text string を設定する.
     * ここを呼ぶと, Hibernate Search に index される.
     * @param s full text string
     */
    public void setFullText(String s) {
        fullText = s;
    }

    /**
     * この ModuleModel の full text string を返す.
     * MassIndexer がここを呼ぶ.
     * @return full text string
     */
    public String getFullText() {
        if (Objects.nonNull(beanBytes)) {
            fullText = beanBytesToString(beanBytes);
        }
        return fullText;
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

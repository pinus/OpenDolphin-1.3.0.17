package open.dolphin.infomodel;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javax.persistence.*;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;

/**
 * ModuleModel.
 * Field 'model' contains either BundleMed, BundleDolphin, or ProgressCourse
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Entity
@Table(name = "d_module")
public class ModuleModel extends KarteEntryBean {
    private static final long serialVersionUID = -8781968977231876023L;;

    @Embedded
    private ModuleInfoBean moduleInfo;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
    @Transient
    private IInfoModel model;

    @Lob
    @Field(index=Index.YES)                         // hibernate search
    @FieldBridge(impl = ModuleModelBridge.class)    // hibernate search
    @Analyzer(impl = CJKAnalyzer.class)             // hibernate search
    @Column(nullable=false)
    private byte[] beanBytes;

    @ManyToOne
    @JoinColumn(name="doc_id", nullable=false)
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

    public void setModuleInfo(ModuleInfoBean moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public ModuleInfoBean getModuleInfo() {
        return moduleInfo;
    }

    public void setModel(IInfoModel model) {
        this.model = model;
    }

    public IInfoModel getModel() {
        return model;
    }

    public byte[] getBeanBytes() {
        return beanBytes;
    }

    public void setBeanBytes(byte[] beanBytes) {
        this.beanBytes = beanBytes;
    }

    @Override
    public int compareTo(Object other) {
        if (other != null && getClass() == other.getClass()) {
            ModuleInfoBean moduleInfo1 = getModuleInfo();
            ModuleInfoBean moduleInfo2 = ((ModuleModel)other).getModuleInfo();
            return moduleInfo1.compareTo(moduleInfo2);
        }
        return -1;
    }
}

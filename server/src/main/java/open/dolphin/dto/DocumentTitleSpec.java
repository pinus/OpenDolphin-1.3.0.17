package open.dolphin.dto;

/**
 * DocumentTitleSpec.
 *
 * @author pns
 */
public class DocumentTitleSpec {
    private long docInfoPk;
    private String title;

    public long getDocumentPk() {
        return docInfoPk;
    }

    public void setDocInfoPk(long documentPk) {
        this.docInfoPk = documentPk;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

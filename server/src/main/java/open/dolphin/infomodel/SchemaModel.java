package open.dolphin.infomodel;

import javax.persistence.*;
import javax.swing.ImageIcon;

/**
 * SchemaModel.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Entity
@Table(name = "d_image")
public class SchemaModel extends KarteEntryBean<SchemaModel> {
    private static final long serialVersionUID = -2500342431785313368L;

    @Embedded
    private ExtRefModel extRef;

    @Lob
    @Column(nullable=false)
    private byte[] jpegByte;

    @ManyToOne
    @JoinColumn(name="doc_id", nullable=false)
    private DocumentModel document;

    // Compatible props
    @Transient
    private String fileName;

    @Transient
    private ImageIcon icon;

    @Transient
    private int imageNumber;

    public ExtRefModel getExtRef() { return extRef; }

    public void setExtRef(ExtRefModel val) {
        extRef = val;
    }

    public DocumentModel getDocument() {
        return document;
    }

    public void setDocument(DocumentModel document) {
        this.document = document;
    }

    public byte[] getJpegByte() {
        return jpegByte;
    }

    public void setJpegByte(byte[] jpegByte) {
        this.jpegByte = jpegByte;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon val) {
        icon = val;
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String val) {
        fileName = val;
    }

    public IInfoModel getModel() { return getExtRef(); }

    public void setModel(IInfoModel val) {
        setExtRef((ExtRefModel)val);
    }

    /**
     * 確定日及びイメージ番号で比較する.
     * @param other 比較対象
     * @return 比較結果
     */
    @Override
    public int compareTo(SchemaModel other) {
        int result = super.compareTo(other);
        if (result == 0) {
            // primitive なので比較はOK
            int no1 = getImageNumber();
            int no2 = other.getImageNumber();
            result = no1 - no2;
        }
        return result;
    }
}

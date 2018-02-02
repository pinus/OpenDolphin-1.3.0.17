package open.dolphin.infomodel;

/**
 * VersionModel.
 *
 * @author Kazushi Minagawa
 */
public class VersionModel extends InfoModel {
    private static final long serialVersionUID = 5196580420531493579L;

    private int number;

    private int revision;

    private String releaseNote;

    public void initialize() {
        number = 1;
    }

    public String getVersionNumber() {
        return String.format("%d.%d", number, revision);
    }

    public void setVersionNumber(String vn) {
        int index = vn.indexOf('.');
        try {
            if (index >=0 ) {
                String n = vn.substring(0, index);
                String r = vn.substring(index+1);
                number = Integer.parseInt(n);
                revision = Integer.parseInt(r);
            } else {
                number = Integer.parseInt(vn);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace(System.err);
        }
    }

    public void incrementNumber() {
        number++;
    }

    public void incrementRevision() {
        revision++;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public String getReleaseNote() {
        return releaseNote;
    }
}

package open.dolphin.infomodel;

/**
 * TextStampModel.
 *
 * @author Kazushi Minagawa
 */
public class TextStampModel extends InfoModel {
    
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String val) {
        text = val;
    }

    @Override
    public String toString() {
        return getText();
    }
}

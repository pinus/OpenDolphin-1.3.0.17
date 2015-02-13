package open.dolphin.infomodel;

/**
 * TextStampModel
 * 
 * @author Kazushi Minagawa
 */
public class TextStampModel extends InfoModel {
    private static final long serialVersionUID = -7296082989470881197L;

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

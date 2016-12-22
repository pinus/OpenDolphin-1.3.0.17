package open.dolphin.client;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import javafx.application.Platform;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.Position;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.infomodel.SchemaModel;
import open.dolphin.ui.MyBorderFactory;
import org.apache.log4j.Logger;

/**
 * スタンプのデータを保持するコンポーネントで TextPane に挿入される.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class SchemaHolder extends AbstractComponentHolder {
    private static final long serialVersionUID = 1777560751402251092L;

    private static final Color SELECTED_BORDER = new Color(255, 0, 153);
    private SchemaModel schema;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    // Junzo SATO
    // to restrict the size of the component,
    // setBounds and setSize are overridden.
    private int fixedSize = 192;//160;/////////////////////////////////////////
    private int fixedWidth = fixedSize;
    private int fixedHeight = fixedSize;
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    private SchemaEditor editor = null;
    private boolean selected;
    private Position start;
    private Position end;
    private KartePane kartePane;
    private Color selectedBorder = SELECTED_BORDER;
    private static final Border MY_SELECTED_BORDER = MyBorderFactory.createSelectedBorder();
    private static final Border MY_CLEAR_BORDER = MyBorderFactory.createClearBorder();

    private Logger logger;

    public SchemaHolder(KartePane kartePane, SchemaModel schema) {

        logger = ClientContext.getBootLogger();
        logger.debug("SchemaHolder constractor");

        this.kartePane = kartePane;
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        // Junzo SATO
        // for simplicity, the acpect ratio of the fixed rect is set to 1.

//pns   schema の大きさ可変にしてみた
//        this.setSize(fixedWidth, fixedHeight);
//        this.setMaximumSize(new Dimension(fixedWidth, fixedHeight));
//        this.setMinimumSize(new Dimension(fixedWidth, fixedHeight));
//        this.setPreferredSize(new Dimension(fixedWidth, fixedHeight));
        // adjustment for printer
//        this.setDoubleBuffered(false);
//        this.setOpaque(true);
//        this.setBackground(Color.white);
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        this.schema = schema;
        this.setImageIcon(schema.getIcon());

    }

    public void setImageIcon(final ImageIcon icon) {
        logger.debug("SchemaHolder setImageIcon");
//pns^  最初から Border をセットしておく
//      選択したときに初めて Border が作られると，微妙に schema が動いてしまう
//      kartePane の back ground color は invokeLater しないと取れない
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(MY_CLEAR_BORDER);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setIcon(adjustImageSize(icon, new Dimension(fixedWidth, fixedHeight)));
                JTextPane pane = kartePane.getTextPane();
                if (pane != null) {
                    setBackground(pane.getBackground());
                }
            }
        });
//pns$
    }

    @Override
    public ContentType getContentType() {
        return ContentType.TT_IMAGE;
    }

    @Override
    public KartePane getKartePane() {
        return kartePane;
    }

    public SchemaModel getSchema() {
        return schema;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void enter(ActionMap map) {

        logger.debug("SchemaHolder enter");

//        ChartMediator mediator = kartePane.getMediator();
//        mediator.getAction(GUIConst.ACTION_CUT).setEnabled(false);
//        mediator.getAction(GUIConst.ACTION_COPY).setEnabled(false);
//        mediator.getAction(GUIConst.ACTION_PASTE).setEnabled(false);
//        mediator.getAction(GUIConst.ACTION_UNDO).setEnabled(false);
//        mediator.getAction(GUIConst.ACTION_REDO).setEnabled(false);
//        mediator.getAction(GUIConst.ACTION_INSERT_TEXT).setEnabled(false);
//        mediator.getAction(GUIConst.ACTION_INSERT_SCHEMA).setEnabled(false);
//        mediator.getAction(GUIConst.ACTION_INSERT_STAMP).setEnabled(false);

        map.get(GUIConst.ACTION_COPY).setEnabled(true);

        if (kartePane.getTextPane().isEditable()) {
            map.get(GUIConst.ACTION_CUT).setEnabled(true);
        } else {
            map.get(GUIConst.ACTION_CUT).setEnabled(false);
        }

        map.get(GUIConst.ACTION_PASTE).setEnabled(false);

        setSelected(true);
    }

    @Override
    public void exit(ActionMap map) {
        logger.debug("SchemaHolder exit");
        setSelected(false);
    }

    @Override
    public JLabel getComponent() {
        return this;
    }

    @Override
    public void maybeShowPopup(MouseEvent e) {
        JPopupMenu popup = new JPopupMenu();
        popup.setFocusable(false);
        ChartMediator mediator = kartePane.getMediator();
        popup.add(mediator.getAction(GUIConst.ACTION_CUT));
        popup.add(mediator.getAction(GUIConst.ACTION_COPY));
        popup.add(mediator.getAction(GUIConst.ACTION_PASTE));
        popup.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    public void setSelected(boolean selected) {
        logger.debug("SchemaHolder setSelected " + selected);
        boolean old = this.selected;
        this.selected = selected;
        if (old != this.selected) {
            if (this.selected) {
//pns           this.setBorder(BorderFactory.createLineBorder(selectedBorder));
                this.setBorder(MY_SELECTED_BORDER);
            } else {
//pns           this.setBorder(BorderFactory.createLineBorder(kartePane.getTextPane().getBackground()));
                this.setBorder(MY_CLEAR_BORDER);
            }
        }
    }

    @Override
    public void edit() {

        logger.debug("SchemaHolder edit");

        if (kartePane.getTextPane().isEditable() && this.isEditable()) {

            // JavaFX thread
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    if (editor == null) {
                        editor = new SchemaEditorImpl();
                    }
                    editor.setSchema(schema);
                    editor.setEditable(kartePane.getTextPane().isEditable());
                    editor.addPropertyChangeListener(SchemaHolder.this);
                    editor.start();
                }
            });

            // schema edit 開始したら dirty と設定
            kartePane.setDirty(true);
            // 二重起動の禁止
            //kartePane.getTextPane().setEditable(false);
            this.setEditable(false);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        logger.debug("SchemaHolder propertyChange");

        // 二重起動の解除
        //kartePane.getTextPane().setEditable(true);
        this.setEditable(true);

        SchemaModel newSchema = (SchemaModel)e.getNewValue();
        if (newSchema ==  null) {
            return;
        }

        schema = newSchema;
        setIcon(adjustImageSize(schema.getIcon(), new Dimension(fixedWidth, fixedHeight)));
    }

    @Override
    public void setEntry(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int getStartPos() {
        return start.getOffset();
    }

    @Override
    public int getEndPos() {
        return end.getOffset();
    }

    /**
     * LDAP Programming with Java.
     */
    protected ImageIcon adjustImageSize(ImageIcon icon, Dimension dim) {
        logger.debug("SchemaHolder adjustImageSize");
//pns
        ImageIcon newIcon;

        if ( (icon.getIconHeight() > dim.height) ||
                (icon.getIconWidth() > dim.width) ) {
            Image img = icon.getImage();
            float hRatio = (float)icon.getIconHeight() / dim.height;
            float wRatio = (float)icon.getIconWidth() / dim.width;
            int h, w;
            if (hRatio > wRatio) {
                h = dim.height;
                w = (int)(icon.getIconWidth() / hRatio);
            } else {
                w = dim.width;
                h = (int)(icon.getIconHeight() / wRatio);
            }
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
//pns       return new ImageIcon(img);
            newIcon = new ImageIcon(img);
        } else {
//pns       return icon;
            newIcon = icon;
        }
//pns^  border の分大きくしないと icon が切れてしまう
        this.setPreferredSize(new Dimension(newIcon.getIconWidth()+6, newIcon.getIconHeight()+6));
        this.setMinimumSize(new Dimension(newIcon.getIconWidth()+6, newIcon.getIconHeight()+6));

        return newIcon;
//pns$
    }
}

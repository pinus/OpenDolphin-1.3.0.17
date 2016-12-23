package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import open.dolphin.ui.MyBorderFactory;
import open.dolphin.ui.MyJScrollPane;

/**
 * KartePanel をつくるファクトリー.
 * @author pns
 */
public class KartePanelFactory {
    private static final int TIMESTAMP_PANEL_HEIGHT = 22; // 22で固定

    private final KartePanel kartePanel;
    private JTextPane soaTextPane;
    private JTextPane pTextPane;
    private JLabel timeStampLabel;
    private JPanel timeStampPanel;
    private JPanel textPanePanel;

    private KartePanelFactory(boolean editor) {
        kartePanel = (editor)? new EditorPanel() : new ViewerPanel();
    }

    public static KartePanel createViewerPanel() {
        return new KartePanelFactory(false).getProduct();
    }

    public static KartePanel createEditorPanel() {
        return new KartePanelFactory(true).getProduct();
    }

    private KartePanel getProduct() {
        return kartePanel;
    }

    /**
     * Viewer 用の KartePanel.
     * soaTextaPenel と soaTextPanel の中身の実際の高さの高い方を preferredSize として返す
     */
    private class ViewerPanel extends KartePanel {
        private static final long serialVersionUID = 1L;

        public ViewerPanel() {
            initComponents();

            // workaround http://ron.shoutboot.com/2010/05/23/swing-jscrollpane-scrolls-to-bottom/
            soaTextPane.setCaret(new DefaultCaret() {
                private static final long serialVersionUID = 1L;
                @Override
                protected void adjustVisibility(Rectangle r) {}
            });
            pTextPane.setCaret(new DefaultCaret() {
                private static final long serialVersionUID = 1L;
                @Override
                protected void adjustVisibility(Rectangle r) {}
            });

            textPanePanel.add(soaTextPane);
            textPanePanel.add(pTextPane);

            setLayout(new BorderLayout(0,0));
            add(timeStampPanel, BorderLayout.NORTH);
            add(textPanePanel, BorderLayout.CENTER);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            // やっぱり soaTextPane.getPreferredSize().height が正しくない場合がある
            // その場合，modelToView が null になる. つまり，コンポネントのサイズが決定されていない
            // eg) 13842, 15571
            // その場合，コンポネントサイズが決定されるまでやり直しする
            // ・textPane.setSize(1,1) したら workaround できた
            // ・やっぱりおかしいことある？　--　復活させて様子を見る → ここではなかった

            //try {
            //   if (soaTextPane.modelToView(0) == null) { revalidateAndRepaint(); }
            //} catch (Exception e) {}

            // h は textPane の高さ これに timeStamp 部分足すと1枚のカルテの高さ. ぎりぎりにならないように 10 だけ余裕
            d.height = Math.max(soaTextPane.getPreferredSize().height, pTextPane.getPreferredSize().height)
                    + TIMESTAMP_PANEL_HEIGHT + 10;
            return d;
        }

        //private void revalidateAndRepaint() {
        //    System.out.println("KartePanelFactory: revalidate and repaint");
        //    SwingUtilities.invokeLater(new Runnable(){
        //        @Override
        //        public void run() {
        //            revalidate();
        //            repaint();
        //        }
        //    });
        //}

        @Override
        public JTextPane getPTextPane() {
            return pTextPane;
        }
        @Override
        public JTextPane getSoaTextPane() {
            return soaTextPane;
        }
        @Override
        public JLabel getTimeStampLabel() {
            return timeStampLabel;
        }
        @Override
        public int getTimeStampPanelHeight() {
            return TIMESTAMP_PANEL_HEIGHT;
        }
        @Override
        public JPanel getTimeStampPanel () {
            return timeStampPanel;
        }
    }

    /**
     * Editor 用の KartePanel
     */
    private class EditorPanel extends KartePanel {
        private static final long serialVersionUID = 1L;

        public EditorPanel() {
            initComponents();

            MyJScrollPane soaScrollPane = new MyJScrollPane();
            soaScrollPane.setViewportView(soaTextPane);
            soaScrollPane.setHorizontalScrollBarPolicy(MyJScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            soaScrollPane.setVerticalScrollBarPolicy(MyJScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            MyJScrollPane pScrollPane = new MyJScrollPane();
            pScrollPane.setViewportView(pTextPane);
            pScrollPane.setHorizontalScrollBarPolicy(MyJScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            pScrollPane.setVerticalScrollBarPolicy(MyJScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            textPanePanel.add(soaScrollPane);
            textPanePanel.add(pScrollPane);

            setLayout(new BorderLayout(0,0));
            add(timeStampPanel, BorderLayout.NORTH);
            add(textPanePanel, BorderLayout.CENTER);
        }

        @Override
        public JTextPane getPTextPane() {
            return pTextPane;
        }
        @Override
        public JTextPane getSoaTextPane() {
            return soaTextPane;
        }
        @Override
        public JLabel getTimeStampLabel() {
            return timeStampLabel;
        }
        @Override
        public int getTimeStampPanelHeight() {
            return TIMESTAMP_PANEL_HEIGHT;
        }
        @Override
        public JPanel getTimeStampPanel () {
            return timeStampPanel;
        }
    }

    private void initComponents() {
        soaTextPane = new JTextPane();
        soaTextPane.setEditorKit(new WrapEditorKit());
        soaTextPane.setMargin(new Insets(10, 10, 10, 10));
        soaTextPane.setMinimumSize(new Dimension(340, 1));
        //soaTextPane.setPreferredSize(new Dimension(340, 500));
        soaTextPane.setSize(1, 1); // なぜかこれでうまくいく。謎.

        pTextPane = new JTextPane();
        pTextPane.setEditorKit(new WrapEditorKit());
        pTextPane.setMargin(new Insets(10, 10, 10, 10));
        pTextPane.setMinimumSize(new Dimension(340, 1));
        //pTextPane.setPreferredSize(new Dimension(340, 500));
        pTextPane.setSize(1, 1);

        // これをセットしないと，勝手に cut copy paste のポップアップがセットされてしまう.
        soaTextPane.putClientProperty("Quaqua.TextComponent.showPopup", false);
        pTextPane.putClientProperty("Quaqua.TextComponent.showPopup", false);

        timeStampPanel = new JPanel();
        timeStampPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, TIMESTAMP_PANEL_HEIGHT));
        timeStampPanel.setMinimumSize(new Dimension(682, TIMESTAMP_PANEL_HEIGHT));
        timeStampPanel.setPreferredSize(new Dimension(682, TIMESTAMP_PANEL_HEIGHT));
        timeStampPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        timeStampPanel.setBorder(MyBorderFactory.createTitleBorder(new Insets(0,0,0,0)));

        timeStampLabel = new JLabel();
        timeStampPanel.add(timeStampLabel);

        textPanePanel = new JPanel();
        textPanePanel.setLayout(new GridLayout(1,2,2,0));
        textPanePanel.setOpaque(true); // KarteScrollPane で snap 取るとき，境界が黒くならないように
        textPanePanel.setPreferredSize(new Dimension(682,500));
    }

    /**
     * workaround for long word wrapping
     * http://stackoverflow.com/questions/8666727/wrap-long-words-in-jtextpane-java-7
     */
    private class WrapEditorKit extends StyledEditorKit {
        private static final long serialVersionUID = 1L;
        ViewFactory defaultFactory = new WrapColumnFactory();
        @Override
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }
    }

    private class WrapColumnFactory implements ViewFactory {
        @Override
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new WrapLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }
            // default to text display
            return new LabelView(elem);
        }
    }

    private class WrapLabelView extends LabelView {
        public WrapLabelView(Element elem) {
            super(elem);
        }
        @Override
        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }

    }}

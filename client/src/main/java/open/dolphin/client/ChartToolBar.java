package open.dolphin.client;

import com.sun.glass.events.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import open.dolphin.event.ProxyAction;
import open.dolphin.helper.WindowSupport;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.stampbox.DefaultStampTreeMenuListener;
import open.dolphin.stampbox.StampBoxPlugin;
import open.dolphin.stampbox.StampTree;
import open.dolphin.stampbox.StampTreeMenuBuilder;
import open.dolphin.ui.CompletableSearchField;

/**
 * ChartImpl と EditorFrame 特有の JToolBar.
 * @author pns
 */
public class ChartToolBar extends JToolBar {
    private static final long serialVersionUID = 1L;

    private final MainWindow window;
    private final ChartImpl realChart;
    private final ChartMediator mediator;
    private final Preferences prefs;

    public ChartToolBar(final Chart chart) {
        super();
        window = chart.getContext();
        mediator = chart.getChartMediator();
        prefs = Preferences.userNodeForPackage(this.getClass());

        realChart = chart instanceof ChartImpl ?
            (ChartImpl) chart : (ChartImpl) ((EditorFrame)chart).getChart();

        initComponents();
    }

    private void initComponents() {

        setFloatable(false);
        setOpaque(false);
        setBorderPainted(false);

        add(createStampButton());
        //add(createTextStampButton());
        add(createSchemaButton());
        add(createWindowButton());
        add(createDiagnosisSearchPanel());
    }

    private JButton createStampButton() {
        JButton stampButton = new JButton();
        stampButton.setBorderPainted(false);
        stampButton.setName("stampBtn");
        stampButton.setAction(mediator.getActions().get(GUIConst.ACTION_INSERT_STAMP));
        stampButton.setText("");
        stampButton.setToolTipText("スタンプを挿入します。");
        //stampButton.setIcon(GUIConst.ICON_STAMP_22);
        stampButton.setIcon(GUIConst.ICON_STAMP_32);
        stampButton.setOpaque(false);
        stampButton.addActionListener(e-> {
            JButton b = (JButton) e.getSource();
            JPopupMenu popup = new JPopupMenu();
            mediator.addStampMenu(popup);
            Point loc = b.getLocation();
            popup.show(b.getParent(), loc.x + b.getWidth()/2, loc.y + b.getHeight()/2);
        });
        return stampButton;
    }

    private JButton createTextStampButton() {
        JButton textStampButton = new JButton();
        textStampButton.setBorderPainted(false);
        textStampButton.setName("textBtn");
        textStampButton.setAction(mediator.getActions().get(GUIConst.ACTION_INSERT_TEXT));
        textStampButton.setText("");
        textStampButton.setToolTipText("テキストスタンプを挿入します。");
        textStampButton.setIcon(GUIConst.ICON_STAMP_TEXT_22);
        textStampButton.setOpaque(false);
        textStampButton.addActionListener(e -> {
            JButton b = (JButton) e.getSource();
            JPopupMenu popup = new JPopupMenu();
            mediator.addTextMenu(popup);
            Point loc = b.getLocation();
            popup.show(b.getParent(), loc.x + b.getWidth()/2, loc.y + b.getHeight()/2);
        });
        return textStampButton;
    }

    private JButton createSchemaButton() {
        JButton schemaButton = new JButton();
        schemaButton.setBorderPainted(false);
        schemaButton.setName("schemaBtn");
        schemaButton.setAction(mediator.getActions().get(GUIConst.ACTION_INSERT_SCHEMA));
        schemaButton.setText("");
        schemaButton.setToolTipText("シェーマボックスを起動します。");
        //schemaButton.setIcon(GUIConst.ICON_GRAPHICS_BRUSH_22);
        schemaButton.setIcon(GUIConst.ICON_BRUSH_32);
        schemaButton.setOpaque(false);
        schemaButton.addActionListener(e -> window.showSchemaBox());
        return schemaButton;
    }

    private JButton createWindowButton() {
        JButton windowButton = new JButton();
        windowButton.setBorderPainted(false);
        //windowButton.setIcon(GUIConst.ICON_WINDOWS_22);
        windowButton.setIcon(GUIConst.ICON_WINDOW_STACK_32);
        windowButton.setToolTipText("開いているカルテの一覧を表示します。");
        windowButton.setOpaque(false);
        windowButton.addActionListener(e -> {
            JPopupMenu popup = new JPopupMenu();
            final List<WindowSupport> windows = WindowSupport.getAllWindows();
            Action action;
            String name;

            int count = 0;
            // カルテを popup に追加
            for (WindowSupport ws : windows) {
                action = ws.getWindowAction();
                name = action.getValue(Action.NAME).toString();
                if (name.contains("カルテ")) {
                    action.putValue(Action.SMALL_ICON, WindowSupport.getIcon(ws.getFrame()));
                    popup.add(action);
                    count++;
                }
            }
            if (count != 0) { popup.addSeparator(); count = 0; }

            // インスペクタを popup に追加
            for (WindowSupport ws : windows) {
                action = ws.getWindowAction();
                name = action.getValue(Action.NAME).toString();
                if (name.contains("インスペクタ")) {
                    action.putValue(Action.SMALL_ICON, WindowSupport.getIcon(ws.getFrame()));
                    popup.add(action);
                    count++;
                }
            }

            // インスペクタウインドウ整列
            if (count != 0) {
                popup.addSeparator();

                //Action a = new ProxyAction("インスペクタを整列", GUIConst.ICON_WINDOWS_22, () -> {
                Action a = new ProxyAction("インスペクタを整列", GUIConst.ICON_WINDOW_STACK_16, () -> {
                    int x = WindowSupport.INITIAL_X; int y = WindowSupport.INITIAL_Y; int width = 0; int height = 0;

                    for (WindowSupport ws : windows) {
                        JFrame f = ws.getFrame();
                        if (f.getTitle().contains("インスペクタ")) {
                            if (width == 0) { width = f.getBounds().width; }
                            if (height == 0) { height = f.getBounds().height; }

                            f.setBounds(x, y, width, height);
                            f.toFront();
                            x += WindowSupport.INITIAL_DX; y += WindowSupport.INITIAL_DY;
                        }
                    }
                });
                popup.add(a);
            }
            JButton b = (JButton) e.getSource();
            Point loc = b.getLocation();
            popup.show(b.getParent(), loc.x + b.getWidth()/2, loc.y + b.getHeight()/2);
        });

        return windowButton;
    }

    private JPanel createDiagnosisSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        CompletableSearchField keywordFld = new CompletableSearchField(30);
        keywordFld.setLabel("病名検索");
        keywordFld.setPreferences(prefs);
        keywordFld.putClientProperty("Quaqua.TextField.style", "search");
        keywordFld.setPreferredSize(new Dimension(500,24));
        keywordFld.addActionListener(e -> {
            String text = keywordFld.getText();

            if (text != null && ! text.equals("")) {
                JPopupMenu popup = new JPopupMenu();
                String pattern = ".*" + keywordFld.getText() + ".*";

                StampBoxPlugin stampBox = mediator.getStampBox();
                StampTree tree = stampBox.getStampTree(IInfoModel.ENTITY_DIAGNOSIS);

                StampTreeMenuBuilder builder = new StampTreeMenuBuilder(tree, pattern);
                builder.addStampTreeMenuListener(new DefaultStampTreeMenuListener(realChart.getDiagnosisDocument().getDiagnosisTable()));
                builder.buildRootless(popup);

                if (popup.getComponentCount() != 0) {
                    Point loc = keywordFld.getLocation();
                    popup.show(keywordFld.getParent(), loc.x, loc.y + keywordFld.getHeight());
                }
            }
        });

        // ctrl-return でもリターンキーの notify-field-accept が発生するようにする
        InputMap map = keywordFld.getInputMap();
        Object value =  map.get(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), value);

        panel.add(keywordFld, BorderLayout.CENTER);
        panel.add(Box.createVerticalStrut(9), BorderLayout.NORTH);
        panel.add(Box.createVerticalStrut(9), BorderLayout.SOUTH);
        panel.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
        panel.add(Box.createHorizontalStrut(5), BorderLayout.EAST);

        return panel;
    }
}

package open.dolphin.client;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import open.dolphin.helper.WindowSupport;
import open.dolphin.ui.MyJPopupMenu;

/**
 * ChartImpl と EditorFrame 特有の JToolBar
 * @author pns
 */
public class ChartToolBar extends JToolBar {
    private static final long serialVersionUID = 1L;

    public ChartToolBar(final Chart chart) {
        super();

        final ChartMediator mediator = chart.getChartMediator();
        final MainWindow context = chart.getContext();

        setFloatable(false);
        //setBorder(MyBorderFactory.createGroupBoxBorder(new Insets(1,5,1,5)));
        setOpaque(false);

        JButton textStampButton = new JButton();
        textStampButton.setBorderPainted(false);
        textStampButton.setName("textBtn");
        textStampButton.setAction(mediator.getActions().get(GUIConst.ACTION_INSERT_TEXT));
        textStampButton.setText("");
        textStampButton.setToolTipText("テキストスタンプを挿入します。");
        textStampButton.setIcon(GUIConst.ICON_STAMP_TEXT_22);
        textStampButton.setOpaque(false);
        textStampButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!((JButton) e.getSource()).isEnabled()) return;
                MyJPopupMenu popup = new MyJPopupMenu();
                mediator.addTextMenu(popup);
                if (!e.isPopupTrigger()) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        JButton schemaButton = new JButton();
        schemaButton.setBorderPainted(false);
        schemaButton.setName("schemaBtn");
        schemaButton.setAction(mediator.getActions().get(GUIConst.ACTION_INSERT_SCHEMA));
        schemaButton.setText("");
        schemaButton.setToolTipText("シェーマボックスを起動します。");
        schemaButton.setIcon(GUIConst.ICON_GRAPHICS_BRUSH_22);
        schemaButton.setOpaque(false);
        schemaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!((JButton) e.getSource()).isEnabled()) return;
                context.showSchemaBox();
            }
        });

        JButton stampButton = new JButton();
        stampButton.setBorderPainted(false);
        stampButton.setName("stampBtn");
        stampButton.setAction(mediator.getActions().get(GUIConst.ACTION_INSERT_STAMP));
        stampButton.setText("");
        stampButton.setToolTipText("スタンプを挿入します。");
        stampButton.setIcon(GUIConst.ICON_STAMP_22);
        stampButton.setOpaque(false);
        stampButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!((JButton) e.getSource()).isEnabled()) return;
                MyJPopupMenu popup = new MyJPopupMenu();
                mediator.addStampMenu(popup);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        JButton windowButton = new JButton();
        windowButton.setBorderPainted(false);
        windowButton.setIcon(GUIConst.ICON_WINDOWS_22);
        windowButton.setToolTipText("開いているカルテの一覧を表示します。");
        windowButton.setOpaque(false);
        windowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MyJPopupMenu popup = new MyJPopupMenu();
                final ArrayList<WindowSupport> windows = WindowSupport.getAllWindows();
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
                    count = 0;

                    Action a = new AbstractAction() {
                        private static final long serialVersionUID = 1L;
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFrame f;
                            int x = WindowSupport.INITIAL_X; int y = WindowSupport.INITIAL_Y; int width = 0; int height = 0;

                            for (WindowSupport ws : windows) {
                                f = ws.getFrame();
                                if (f.getTitle().contains("インスペクタ")) {
                                    if (width == 0) width = f.getBounds().width;
                                    if (height == 0) height = f.getBounds().height;

                                    f.setBounds(x, y, width, height);
                                    f.toFront();
                                    x += WindowSupport.INITIAL_DX; y += WindowSupport.INITIAL_DY;
                                }
                            }
                        }
                    };

                    a.putValue(Action.NAME, "インスペクタを整列");
                    a.putValue(Action.SMALL_ICON, GUIConst.ICON_WINDOWS_22);
                    popup.add(a);
                }

                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        add(stampButton);
        add(textStampButton);
        add(schemaButton);
        add(windowButton);
    }
}

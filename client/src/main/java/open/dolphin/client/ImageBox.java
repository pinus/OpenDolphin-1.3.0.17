package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.EventHandler;
import java.io.File;
import java.io.FileFilter;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import open.dolphin.helper.ComponentMemory;
import open.dolphin.helper.MenuSupport;
import open.dolphin.helper.Task;
import open.dolphin.helper.WindowSupport;
import open.dolphin.ui.IMEControl;
import open.dolphin.ui.MainFrame;
import open.dolphin.ui.PNSTabbedPane;
import org.apache.log4j.Logger;

/**
 * ImageBox
 *
 * @author Minagawa,Kazushi
 */
public class ImageBox extends AbstractMainTool {

    private static final int DEFAULT_COLUMN_COUNT 	=   3;
    private static final int DEFAULT_IMAGE_WIDTH 	= 120;
    private static final int DEFAULT_IMAGE_HEIGHT 	= 120;
    private static final String[] DEFAULT_IMAGE_SUFFIX = {".jpg"};

    private String imageLocation  = ClientContext.getLocation("schema");
//pns    private JTabbedPane tabbedPane;
    private PNSTabbedPane tabbedPane;
    private JButton refreshBtn;
    private int columnCount = DEFAULT_COLUMN_COUNT;
    private int imageWidth = DEFAULT_IMAGE_WIDTH;
    private int imageHeight = DEFAULT_IMAGE_HEIGHT;
    private String[] suffix = DEFAULT_IMAGE_SUFFIX;
    private int defaultWidth = 406;
    private int defaultHeight = 587;
    private int defaultLocX = 537;
    private int defaultLocY = 22;

    private MainFrame frame; // なぜか JFrame をやめて JDialog にした形跡がある
//pns   private JDialog frame; オリジナルでは JDialog で作られている
    private String title = "シェーマ箱";
    private static final int TIMER_DELAY 	=  200;		// 200 msec 毎にチェック
    private static final int MAX_ESTIMATION 	= 5000;		// 全体の見積もり時間
    private static final String PROGRESS_NOTE = "画像をロードしています...";

    private Logger logger;

//pns   SchemaBox でもメニューを出すため
//pns   private Mediator mediator;
    private MenuSupport mediator;
    private boolean isMac;

    public void start() {
        logger = ClientContext.getBootLogger();
        isMac = ClientContext.isMac();

        initComponent();
        connect();
        setImageLocation(imageLocation);
    }

//pns^ 多重起動しないための入り口
    @Override
    public void enter() {
        if (frame != null) {
            frame.setVisible(true);
            IMEControl.setImeOff(frame);
        }
    }
//pns$

    public void stop() {
//pns   多重起動しないようにするので，window を close しても，window は残しておいて，見えなくするだけにする
//pns   if (tabbedPane != null) {
//pns       int cnt = tabbedPane.getTabCount();
//pns       for (int i = 0; i < cnt; i++) {
//pns           ImagePalette ip = (ImagePalette) tabbedPane.getComponentAt(i);
//pns           if (ip != null) {
//pns               ip.dispose();
//pns           }
//pns       }
//pns   }
//pns   frame.dispose();
        frame.setVisible(false);
    }

    public MainFrame getFrame() {
        return frame;
//pns   return null;
    }

    public void toFront() {
        if (frame != null) {
            if (!frame.isVisible()) {
                frame.setVisible(true);
            }
            frame.toFront();
        }
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String loc) {

        this.imageLocation = loc;
        String message = "シェーマ画像";
        Component c = null;

        Task task = new Task<Void>(c, message, PROGRESS_NOTE, MAX_ESTIMATION) {

            @Override
            protected Void doInBackground() throws Exception {
                createImagePalettes();
                return null;
            }

            @Override
            protected void succeeded(Void result) {
                if (! frame.isVisible()) {
                    frame.setVisible(true);
                }
                logger.debug("Task succeeded");
            }
        };
        //task.setMillisToPopup(TIMER_DELAY);
        task.execute();
    }

    public void refresh() {

        final ImagePalette imageTable = (ImagePalette) tabbedPane.getSelectedComponent();
        String message = "シェーマ画像";
        Component c = this.getFrame();
        String note = "画像リストを更新しています";

        Task task = new Task<Void>(c, message, note, MAX_ESTIMATION) {

            @Override
            protected Void doInBackground() throws Exception {
                imageTable.refresh();
                return null;
            }

            @Override
            protected void succeeded(Void result) {
                if (! frame.isVisible()) {
                    frame.setVisible(true);
                }
                logger.debug("Task succeeded");
            }
        };
        //task.setMillisToPopup(TIMER_DELAY);
        task.execute();
    }

    private void initComponent() {
        //
        // TabbedPane を生成する
        //
//pns   tabbedPane = new JTabbedPane();
        tabbedPane = new PNSTabbedPane();
        tabbedPane.setButtonVgap(4);
        //tabbedPane.getButtonPanel().setPadding(new Dimension(0,4));
        tabbedPane.getButtonPanel().setBottomLineAlpha(0.4f);

        //
        // 更新ボタンを生成する
        //
        refreshBtn = new JButton(GUIConst.ICON_ARROW_CIRCLE_DOUBLE_16);
        refreshBtn.setText("更新");
        refreshBtn.setHorizontalAlignment(SwingConstants.LEADING);
        refreshBtn.putClientProperty("Quaqua.Button.style", "bevel");
        refreshBtn.setFocusable(false);
//pns$
        refreshBtn.addActionListener(EventHandler.create(ActionListener.class, this, "refresh"));
        refreshBtn.setToolTipText("シェーマリストを更新します");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // リフレッシュボタン省略
        // btnPanel.add(refreshBtn);

        //
        // 全体を配置する
        //
//      JPanel p = new JPanel(new BorderLayout());
//      p.add(btnPanel, BorderLayout.NORTH);
//      p.add(tabbedPane, BorderLayout.CENTER);
//pns   p.setBorder(BorderFactory.createEmptyBorder(12,12,11,11));
//      p.setBorder(BorderFactory.createEmptyBorder());

//pns^  mac で SchemaBox にもメニューバーを出す
        if (isMac) {
            WindowSupport windowSupport = WindowSupport.create(title);
            frame = windowSupport.getFrame();
            javax.swing.JMenuBar myMenuBar = windowSupport.getMenuBar();
            mediator = new MenuSupport(this);
            MenuFactory appMenu = new MenuFactory();

            // mainWindow の menuSupport をセットしておけばメニュー処理は mainWindow がしてくれる
            appMenu.setMenuSupports(getContext().getMenuSupport(), mediator);
            appMenu.build(myMenuBar);
            mediator.registerActions(appMenu.getActionMap());
            mediator.disableAllMenus();
            String[] enables = new String[]{
                GUIConst.ACTION_SHOW_STAMPBOX,
                GUIConst.ACTION_SET_KARTE_ENVIROMENT
            };
            mediator.enableMenus(enables);
        } else {
            //frame = new JFrame(title);
            frame = new MainFrame(title);
        }
//pns$
        //frame = new JFrame(title); //← 以前は JFrame で作られていた形跡がある
//pns   frame = new JDialog((JFrame) null, title, false); // ←これがオリジナル
        //frame.setFocusableWindowState(false);

        ComponentMemory cm = new ComponentMemory(frame,
                new Point(defaultLocX,defaultLocY),
                new Dimension(defaultWidth, defaultHeight),
                this);
        cm.setToPreferenceBounds();

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                processWindowClosing();
            }
        });
//      frame.getContentPane().add(p);

        // command panel, status panel は使わない
        frame.removeCommandPanel();
        frame.removeStatusPanel();
        // MainPanel に TabbedPane を挿入
        MainFrame.MainPanel mainPanel = frame.getMainPanel();
        mainPanel.setLayout(new BorderLayout(0,0));
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    private void connect() {
    }

    public void createImagePalettes() {

        File baseDir = new File(imageLocation);
        if ( (! baseDir.exists()) || (! baseDir.isDirectory()) ) {
            return;
        }

        File[] directories = listDirectories(baseDir);
        if (directories == null || directories.length == 0) {
            return;
        }

        //Dimension imageSize = new Dimension(imageWidth, imageHeight);
        for (int i = 0; i < directories.length; i++) {
            String tabName = directories[i].getName();

            ImagePalette imageTable = new ImagePalette(null, columnCount, imageWidth, imageHeight);
            imageTable.setImageSuffix(suffix);
            imageTable.setImageDirectory(directories[i]);
            tabbedPane.addTab(tabName, imageTable);
        }
    }

    private File[] listDirectories(File dir) {
        DirectoryFilter filter = new DirectoryFilter();
        File[] directories = dir.listFiles(filter);
        return directories;
    }

    public void processWindowClosing() {
        stop();
    }

    /**
     * @param columnCount The columnCount to set.
     */
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    /**
     * @return Returns the columnCount.
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * @param imageWidth The imageWidth to set.
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * @return Returns the imageWidth.
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * @param imageHeight The imageHeight to set.
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * @return Returns the imageHeight.
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @param suffix The suffix to set.
     */
    public void setSuffix(String[] suffix) {
        this.suffix = suffix;
    }

    /**
     * @return Returns the suffix.
     */
    public String[] getSuffix() {
        return suffix;
    }

    class DirectoryFilter implements FileFilter {

        public boolean accept(File path) {
            return path.isDirectory();
        }
    }
}

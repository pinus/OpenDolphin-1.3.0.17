package open.dolphin.setting;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.*;
import open.dolphin.client.ClientContext;
import open.dolphin.client.GUIFactory;
import open.dolphin.helper.ProxyActionListener;
import open.dolphin.project.Project;
import org.apache.log4j.Logger;

/**
 * 環境設定ダイアログ。
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class ProjectSettingDialog implements PropertyChangeListener {

    // GUI
    private JDialog dialog;
    private JPanel itemPanel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton okButton;
    private JButton cancelButton;

    // 全体のモデル
    private HashMap<String, AbstractSettingPanel> settingMap;
    private ArrayList<AbstractSettingPanel> allSettings;
    private ArrayList<JToggleButton> allBtns;
    private String startSettingName;
    private boolean loginState;
    private PropertyChangeSupport boundSupport;
    private static final String SETTING_PROP = "SETTING_PROP";
    private boolean okState;
    private Logger logger;
    private static int DEFAULT_WIDTH = 600;
    private static int DEFAULT_HEIGHT = 630;
    private Frame parentFrame = null;

    public ProjectSettingDialog() {
        logger = ClientContext.getBootLogger();
    }

    public ProjectSettingDialog(Window f) {
        this();
        parentFrame = (Frame) f;
    }

    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.addPropertyChangeListener(prop, l);
    }

    public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.addPropertyChangeListener(prop, l);
    }

    public boolean getLoginState() {
        return loginState;
    }

    public void setLoginState(boolean b) {
        loginState = b;
    }

    public boolean getValue() {
        return Project.getProjectStub().isValid();
    }

    public void notifyResult() {
        boolean valid = Project.getProjectStub().isValid();
        boundSupport.firePropertyChange(SETTING_PROP, !valid, valid);
    }

    /**
     * オープン時に表示する設定画面をセットする。
     * @param startSettingName
     */
    public void setProject(String startSettingName) {
        this.startSettingName = startSettingName;
    }

    /**
     * 設定画面を開始する。
     */
    public void start() {

        Runnable r = () -> {
            // モデルを得る
            allSettings = new ArrayList<>();

            allSettings.add(new HostSettingPanel());
            allSettings.add(new ClaimSettingPanel());
            allSettings.add(new KarteSettingPanel());
            allSettings.add(new CodeHelperSettingPanel());

            // 設定パネル(AbstractSettingPanel)を格納する Hashtableを生成する
            // key=設定プラグインの名前 value=設定プラグイン
            settingMap = new HashMap<>();

            // GUI を構築しモデルをバインドする
            initComponents();
            logger.debug("component initialized");

            // オープン時に表示する設定画面を決定する
            int index = 0;

            if (startSettingName != null) {
                logger.debug("startSettingName = " + startSettingName);
                for (AbstractSettingPanel setting : allSettings) {
                    if (startSettingName.equals(setting.getId())) {
                        logger.debug("found index " + index);
                        break;
                    }
                    index++;
                }
            }

            index = (index >= 0 && index < allSettings.size()) ? index : 0;

            // ボタンを押して表示する
            allBtns.get(index).doClick();
        };

        Thread t = new Thread(r);
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }

    /**
     * GUI を構築する。
     */
    private void initComponents() {

        itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //itemPanel = new JPanel();
        //itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));

        //
        // 設定プラグインを起動するためのトグルボタンを生成し
        // パネルへ加える
        //
        allBtns = new ArrayList<>();
        ButtonGroup bg = new ButtonGroup();
        allSettings.stream().map(setting -> {
            String id = setting.getId();
            String text = setting.getTitle();
            JToggleButton tb = new JToggleButton(text, setting.getIcon());
            // pns が作った ElCapitanQuaquaToggleButtonUI だと selection の文字が白くなってしまう
            tb.setUI(new ch.randelshofer.quaqua.QuaquaToggleButtonUI());
            tb.setFocusable(false);
            if (ClientContext.isWin()) {
                tb.setMargin(new Insets(0, 0, 0, 0));
            }
            tb.setHorizontalTextPosition(SwingConstants.CENTER);
            tb.setVerticalTextPosition(SwingConstants.BOTTOM);
            itemPanel.add(tb);
            bg.add(tb);
            tb.setActionCommand(id);
            return tb;
        }).forEachOrdered(tb -> allBtns.add(tb));

        //
        // 設定パネルのコンテナとなるカードパネル
        //
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // コマンドボタン
        String text = ClientContext.getString("settingDialog.saveButtonText");
        okButton = GUIFactory.createButton(text, null, null);
        okButton.setEnabled(false);

        // Cancel
        text = (String) UIManager.get("OptionPane.cancelButtonText");
        cancelButton = GUIFactory.createButton(text, "C", null);

        // 全体ダイアログのコンテントパネル
        JPanel panel = new JPanel(new BorderLayout(11, 0));
        panel.add(itemPanel, BorderLayout.NORTH);
        panel.add(cardPanel, BorderLayout.CENTER);
        // quaqua で表示が乱れるのを防ぐ
        panel.setPreferredSize(new Dimension(DEFAULT_WIDTH-50, DEFAULT_HEIGHT-90));
        panel.setMinimumSize(new Dimension(DEFAULT_WIDTH-50, DEFAULT_HEIGHT-90));

        // ダイアログを生成する
        String title = ClientContext.getString("settingDialog.title");
        Object[] options = new Object[]{okButton, cancelButton};

        JOptionPane jop = new JOptionPane(
                panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                okButton);

        dialog = jop.createDialog( parentFrame, ClientContext.getFrameTitle(title));
        // この方法で作った dialog のタイトルバーは "brushMetalLook" にすると 241 の Gray １色になる
        // 構造は JDialog > JPanel (ContentPane) > JOptionPane となっている
        dialog.getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
        JOptionPane op = (JOptionPane) dialog.getContentPane().getComponent(0);
        op.setOpaque(true);
        op.setBackground(new Color(241,241,241));

        dialog.setResizable(false);
        dialog.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - DEFAULT_WIDTH) / 2;
        int y = (size.height - DEFAULT_HEIGHT) / 3;
        dialog.setLocation(x, y);
        logger.debug("dialog created");

        // イベント接続を行う
        connect();
    }

    /**
     * GUI コンポーネントのイベント接続を行う。
     */
    private void connect() {

        // 設定項目ボタンに追加するアクションリスナを生成する
        ActionListener al = event -> {
            logger.debug("actionPerformed");
            AbstractSettingPanel theSetting = null;
            String name = event.getActionCommand();
            logger.debug("actionCmd = " + name);
            for (AbstractSettingPanel setting : allSettings) {
                String id = setting.getId();
                if (id.equals(name)) {
                    theSetting = setting;
                    logger.debug("found the setting " + theSetting.getClass().getName());
                    break;
                }
            }
            if (theSetting != null) {
                startSetting(theSetting);
            }
        };

        // 全てのボタンにリスナを追加する
        allBtns.stream().forEach((btn) -> btn.addActionListener(al));

        // Save
        okButton.addActionListener(ProxyActionListener.create(this, "doOk"));
        okButton.setEnabled(false);

        // Cancel
        cancelButton.addActionListener(ProxyActionListener.create(this, "doCancel"));

        // ESC でウインドウクローズ
        InputMap im = dialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        im.put(key, "close-window");
        dialog.getRootPane().getActionMap().put("close-window", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        });

        // Dialog
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                doCancel();
            }
        });
    }

    /**
     * 選択された項目(SettingPanel)の編集を開始する.
     */
    private void startSetting(final AbstractSettingPanel sp) {

        if (sp.getContext() != null) {
            adjustHeight(sp);
            cardLayout.show(cardPanel, sp.getTitle());
            return;
        }

        Runnable r = () -> {
            // まだ生成されていない場合は
            // 選択された設定パネルを生成しカードに追加する
            settingMap.put(sp.getId(), sp);
            sp.setContext(ProjectSettingDialog.this);
            sp.setProjectStub(Project.getProjectStub());
            sp.start();

            SwingUtilities.invokeLater(() -> {
                cardPanel.add(sp.getUI(), sp.getTitle());
                adjustHeight(sp);
                cardLayout.show(cardPanel, sp.getTitle());
                if (!dialog.isVisible()) {
                    dialog.setVisible(true);
                }
            });
        };

        Thread t = new Thread(r);
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }

    private void adjustHeight(AbstractSettingPanel settingPanel) {

    }

    /**
     * SettingPanel の state が変化した場合に通知を受け、
     * 全てのカードをスキャンして OK ボタンをコントロールする。
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        String prop = e.getPropertyName();
        if (!prop.equals(AbstractSettingPanel.STATE_PROP)) {
            return;
        }

        // 全てのカードをスキャンして OK ボタンをコントロールする
        boolean newOk = true;
        Iterator<AbstractSettingPanel> iter = settingMap.values().iterator();
        int cnt = 0;
        while (iter.hasNext()) {
            cnt++;
            AbstractSettingPanel p = iter.next();
            if (p.getState().equals(AbstractSettingPanel.State.INVALID_STATE)) {
                newOk = false;
                break;
            }
        }

        if (okState != newOk) {
            okState = newOk;
            okButton.setEnabled(okState);
        }
    }

    public void doOk() {

        Iterator<AbstractSettingPanel> iter = settingMap.values().iterator();
        while (iter.hasNext()) {
            AbstractSettingPanel p = iter.next();
            logger.debug(p.getTitle());
            p.save();
        }

        dialog.setVisible(false);
        dialog.dispose();
        notifyResult();
    }

    public void doCancel() {
        dialog.setVisible(false);
        dialog.dispose();
        notifyResult();
    }
}

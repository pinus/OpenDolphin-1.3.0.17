package open.dolphin.setting;

import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.prefs.Preferences;
import javax.swing.*;
import open.dolphin.client.AddFacilityDialog;
import open.dolphin.client.GUIConst;
import open.dolphin.client.GUIFactory;
import open.dolphin.client.ServerInfo;
import open.dolphin.event.ProxyDocumentListener;
import open.dolphin.helper.GridBagBuilder;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.project.DolphinPrincipal;
import open.dolphin.project.Project;
import open.dolphin.project.ProjectStub;
import open.dolphin.ui.IMEControl;

/**
 * HostSettingPanel.
 *
 * @author Kazushi Minagawa
 * @author pns
 */
public class HostSettingPanel extends AbstractSettingPanel {

    private final String ipAddressPattern = "[A-Za-z0-9.\\-_]*";
    private static final String ID = "hostSetting";
    private static final String TITLE = "サーバ";
    private static final ImageIcon ICON = GUIConst.ICON_NETWORK_32;
    private static final int DEFAULT_HOST_PORT = 4447;  // jboss 7

    // 設定用の GUI components
    private JRadioButton aspMember;
    private JRadioButton facilityUser;
    private JTextField userIdField;
    private JTextField hostAddressField;
    private JTextField facilityIdField;
    private JButton registTesterBtn;
    private JSpinner checkIntervalSpinner;
    private JSlider checkIntervalSlider;
    private JCheckBox checkIntervalBox;
    private JCheckBox receivePvtBroadcastBox;

    private final Preferences prefs;

    // RMI Server PORT
    //private int hostPort = 1099; // jboss 5
    private int hostPort;

    /** 画面用のモデル */
    private ServerModel model;
    private StateMgr stateMgr;

    private static final String DEFAULT_FACILITY_OID = IInfoModel.DEFAULT_FACILITY_OID;

    public HostSettingPanel() {
        prefs = Project.getPreferences();
        init();
    }

    private void init() {
        this.setId(ID);
        this.setTitle(TITLE);
        this.setIcon(ICON);
    }

    /**
     * サーバ設定画面を開始する.
     */
    @Override
    public void start() {
        // 画面モデルを生成し初期化する
        model = new ServerModel();
        model.populate(getProjectStub());

        initComponents();
        connect();
        bindModelToView();
    }

    /**
     * GUI コンポーネントを初期化する.
     */
    private void initComponents() {

        String serverInfoText  = "サーバ情報";
        String serverStyleText = "利用形式:";
        String aspMemberText   = "ASP";
        String useLocaltext    = "院内サーバ";
        String ipAddressText   = "IPアドレス:";

        String userInfoText    = "ユーザ情報";
        String userIdText      = "ユーザID:";
        String facilityIdText  = "医療機関ID:";

        String initServerText  = "ASP評価の申し込み";
        String addSuperUserText = "アカウント作成";

        // テキストフィールドを生成する
        hostAddressField = new JTextField(10);
        facilityIdField = new JTextField(15);
        userIdField = new JTextField(10);

        // パターン制約を加える
        // RegexConstrainedDocument hostDoc = new RegexConstrainedDocument(ipAddressPattern);
        // hostAddressField.setDocument(hostDoc);

        // ボタングループを生成する
        ButtonGroup bg = new ButtonGroup();
        aspMember = GUIFactory.createRadioButton(aspMemberText, null, bg);
        facilityUser = GUIFactory.createRadioButton(useLocaltext, null, bg);

        // 管理者登録ボタン
        registTesterBtn = new JButton(addSuperUserText);

        // サーバ情報パネル
        GridBagBuilder gb = new GridBagBuilder(serverInfoText);
        int row = 0;
        JLabel label = new JLabel(serverStyleText, SwingConstants.RIGHT);
        JPanel panel = GUIFactory.createRadioPanel(new JRadioButton[]{aspMember,facilityUser});
        gb.add(label, 0, row, GridBagConstraints.EAST);
        gb.add(panel, 1, row, GridBagConstraints.WEST);

        row++;
        label = new JLabel(ipAddressText, SwingConstants.RIGHT);
        gb.add(label,            0, row, GridBagConstraints.EAST);
        gb.add(hostAddressField, 1, row, GridBagConstraints.WEST);
        JPanel sip = gb.getProduct();

        // ユーザ情報パネル
        gb = new GridBagBuilder(userInfoText);
        row = 0;
        label = new JLabel(userIdText, SwingConstants.RIGHT);
        gb.add(label,       0, row, GridBagConstraints.EAST);
        gb.add(userIdField, 1, row, GridBagConstraints.WEST);

        row++;
        label = new JLabel(facilityIdText, SwingConstants.RIGHT);
        gb.add(label,           0, row, GridBagConstraints.EAST);
        gb.add(facilityIdField, 1, row, GridBagConstraints.WEST);
        JPanel uip = gb.getProduct();

        // アカウント作成
        gb = new GridBagBuilder(initServerText);
        row = 0;
        label = new JLabel("");
        gb.add(label,           0, row, GridBagConstraints.EAST);
        gb.add(registTesterBtn, 1, row, GridBagConstraints.CENTER);
        JPanel iip = gb.getProduct();

        //masuda   facility user固定にしておく
        aspMember.setEnabled(false);
        model.setUserType(Project.UserType.FACILITY_USER);
        facilityIdField.setEnabled(false);

        // pvt チェック間隔
        JPanel checkIntervalPanel = GUIFactory.createSliderPanel(5, 30, 30);
        checkIntervalSlider = (JSlider) checkIntervalPanel.getComponent(0);
        checkIntervalSpinner = (JSpinner) checkIntervalPanel.getComponent(1);
        checkIntervalBox = new JCheckBox("受付チェック間隔（秒）:");
        receivePvtBroadcastBox = new JCheckBox("受付サーバからのブロードキャストを受け取る");

        gb = new GridBagBuilder("受付チェック間隔");
        row = 0;
        gb.add(checkIntervalBox,   0, row, GridBagConstraints.EAST);
        gb.add(checkIntervalPanel, 1, row, GridBagConstraints.WEST);
        row++;
        gb.add(receivePvtBroadcastBox,   0, row, 2, 1, GridBagConstraints.WEST);
        JPanel intervalPanel = gb.getProduct();

        // 全体レイアウト
        gb = new GridBagBuilder();
        row = 0;
        gb.add(sip, 0, row++, GridBagConstraints.HORIZONTAL, 1.0, 0.0);
        gb.add(uip, 0, row++, GridBagConstraints.HORIZONTAL, 1.0, 0.0);
        gb.add(iip, 0, row++, GridBagConstraints.HORIZONTAL, 1.0, 0.0);
        gb.add(intervalPanel, 0, row++, GridBagConstraints.HORIZONTAL, 1.0, 0.0);
        gb.add(new JLabel(""), 0, row++, GridBagConstraints.BOTH, 1.0, 1.0);
        setUI(gb.getProduct());
    }

    /**
     * コンポーネントのリスナ接続を行う.
     */
    private void connect() {

        stateMgr = new StateMgr();

        // TextField へ入力または削除があった場合，cutState へ checkState() を送る
        ProxyDocumentListener dl = e -> stateMgr.checkState();

        hostAddressField.getDocument().addDocumentListener(dl);
        facilityIdField.getDocument().addDocumentListener(dl);
        userIdField.getDocument().addDocumentListener(dl);

        // IME OFF FocusAdapter
        IMEControl.setImeOffIfFocused(hostAddressField);
        IMEControl.setImeOffIfFocused(facilityIdField);
        IMEControl.setImeOffIfFocused(userIdField);

        // サーバの利用形態 ラジオボタンがクリックされたら　cutState へ checkState を送る
        ActionListener al = e -> stateMgr.controlAddressField();
        aspMember.addActionListener(al);
        facilityUser.addActionListener(al);

        // 管理者登録ボタンがクリックされたら自身をPropertyChangeListener にし
        // 管理者登録ダイアログを別スレッドでスタートさせる
        registTesterBtn.addActionListener(e -> make5TestAccount());

        // フォーカスを次の text field に送っていく
        hostAddressField.addActionListener(e -> userIdField.requestFocus());
        userIdField.addActionListener(e -> facilityIdField.requestFocus());
        facilityIdField.addActionListener(e -> hostAddressField.requestFocus());

        // ログインしている状態の場合，この設定はできないようにする
        if (isLoginState()) {
            facilityUser.setEnabled(false);
            aspMember.setEnabled(false);
            userIdField.setEnabled(false);
            hostAddressField.setEnabled(false);
            facilityIdField.setEnabled(false);
            registTesterBtn.setEnabled(false);
            checkIntervalSlider.setEnabled(false);
            checkIntervalSpinner.setEnabled(false);
            checkIntervalBox.setEnabled(false);
            receivePvtBroadcastBox.setEnabled(false);
        }

        checkIntervalBox.addChangeListener(e -> {
            if (checkIntervalBox.isSelected()) {
                if (! isLoginState()) {
                    checkIntervalSlider.setEnabled(true);
                    checkIntervalSpinner.setEnabled(true);
                }
            } else {
                checkIntervalSlider.setEnabled(false);
                checkIntervalSpinner.setEnabled(false);
            }
        });
    }

    /**
     * Model 値を表示する.
     */
    private void bindModelToView() {

        // userId設定する
        String val = model.getUserId();
        val = val == null ? "" : val;
        userIdField.setText(val);

        // 施設IDを設定する
        val = model.getFacilityId();
        val = val == null ? DEFAULT_FACILITY_OID : val;
        facilityIdField.setText(val);

        // IP address
        val = model.getIpAddress();
        val = val == null ? "localhost" : val;

        // port number
        int intval = model.getPort();
        hostPort = intval == 0? DEFAULT_HOST_PORT : intval;

        // hostaddress:port の形式とする
        hostAddressField.setText(String.format("%s:%d", val, hostPort));
        facilityUser.doClick();

        // チェック間隔
        intval = model.getInterval();
        if (intval == 0) {
            checkIntervalBox.setSelected(false);
            checkIntervalSpinner.setValue(30);
            checkIntervalSpinner.setEnabled(false);
            checkIntervalSlider.setEnabled(false);

        } else {
            checkIntervalSpinner.setValue(intval);
            checkIntervalBox.setSelected(true);
        }

        // pvt ブロードキャスト受信
        receivePvtBroadcastBox.setSelected(model.getReceiveBroadcast());
    }

    /**
     * Viewの値をモデルへ設定する.
     */
    private void bindViewToModel() {

        // 施設IDとユーザIDを保存する
        String facilityId = facilityIdField.getText().trim();
        String userId = userIdField.getText().trim();
        model.setFacilityId(facilityId);
        model.setUserId(userId);

        // hostadress:port の形式
        String val = hostAddressField.getText().trim();
        if (!val.equals("")) {
            String adr[] = val.split(":");
            model.setIpAddress(adr[0]);
            if (adr.length > 1) {
                model.setPort(Integer.parseInt(adr[1]));
            }
            model.setUserType(Project.UserType.FACILITY_USER);
        }

        // チェック間隔
        if (checkIntervalBox.isSelected()) {
            model.setInterval(checkIntervalSpinner.getValue());
        } else {
            model.setInterval(0);
        }

        // pvt ブロードキャスト受信
        model.setReceiveBroadcast(receivePvtBroadcastBox.isSelected());
    }

    /**
     * 5分間評価用のアカウントを作成する.
     */
    public void make5TestAccount() {
        //masuda アカウント作成前にホストアドレスを保存するため
        save();
        AddFacilityDialog af = new AddFacilityDialog();
        PropertyChangeListener pl = evt -> newAccount((ServerInfo) evt.getNewValue());
        af.addPropertyChangeListener(AddFacilityDialog.ACCOUNT_INFO, pl);
        Thread t = new Thread(af);
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }

    /**
     * 管理者登録ダイアログの結果を受け取り情報を表示する.
     * @param info
     */
    public void newAccount(ServerInfo info) {

        if (info != null) {
            facilityIdField.setText(info.getFacilityId());
            userIdField.setText(info.getAdminId());
            //masuda facility user固定にしておく
            //aspMember.doClick();
            facilityUser.doClick();
        }
    }

    /**
     * 設定値を保存する.
     */
    @Override
    public void save() {
        bindViewToModel();
        model.restore(getProjectStub());
    }

    /**
     * サーバ画面設定用のモデルクラス.
     */
    private class ServerModel {

        private Project.UserType userType;
        private String ipAddress;
        private int port;
        private String facilityId;
        private String userId;
        private int interval;
        private boolean receiveBroadcast;

        public ServerModel() {
        }

        /**
         * ProjectStub からポピュレイトする.
         */
        public void populate(ProjectStub stub) {
            // userId設定する
            setUserId(stub.getUserId());
            // 施設IDを設定する
            setFacilityId(stub.getFacilityId());
            // UserTypeを設定する
            setUserType(stub.getUserType());
            // IPAddressを設定する
            setIpAddress(stub.getHostAddress());
            // Portを設定する
            setPort(stub.getHostPort());
            // チェック間隔を設定する
            setInterval(prefs.getInt(Project.PVT_CHECK_INTERVAL, 30));
            // pvt ブロードキャスト useAsPVTServer をリサイクル利用
            setReceiveBroadcast(stub.getUseAsPVTServer());
        }

        /**
         * ProjectStubへリストアする.
         */
        public void restore(ProjectStub stub) {

            // 施設IDとユーザIDを保存する
            stub.setFacilityId(getFacilityId());
            stub.setUserId(getUserId());
            // Principleを保存する
            DolphinPrincipal principal = new DolphinPrincipal();
            principal.setFacilityId(getFacilityId());
            principal.setUserId(getUserId());
            stub.setDolphinPrincipal(principal);
            // メンバータイプを保存する
            stub.setUserType(getUserType());
            // IPAddressを保存する
            stub.setHostAddress(getIpAddress());
            // Portを設定を保存する
            stub.setHostPort(getPort());
            // チェック間隔
            prefs.putInt(Project.PVT_CHECK_INTERVAL, getInterval());
            // pvt ブロードキャスト useAsPVTServer をリサイクル利用
            stub.setUseAsPVTServer(getReceiveBroadcast());
        }

        public Project.UserType getUserType() {
            return userType;
        }
        public void setUserType(Project.UserType userType) {
            this.userType = userType;
        }
        public String getIpAddress() {
            return ipAddress;
        }
        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }
        public int getPort() {
            return port;
        }
        public void setPort(int port) {
            this.port = port;
        }
        public String getFacilityId() {
            return facilityId;
        }
        public void setFacilityId(String facilityId) {
            this.facilityId = facilityId;
        }
        public String getUserId() {
            return userId;
        }
        public void setUserId(String userId) {
            this.userId = userId;
        }
        public int getInterval() {
            return interval;
        }
        public void setInterval(Object interval) {
            this.interval = (Integer) interval;
        }
        public boolean getReceiveBroadcast() {
            return receiveBroadcast;
        }
        public void setReceiveBroadcast(boolean b) {
            this.receiveBroadcast = b;
        }
    }

    /**
     * Mediator 的 StateMgr クラス.
     */
    private class StateMgr {

        public void checkState() {

            AbstractSettingPanel.State newState = isValid()
                                                ? AbstractSettingPanel.State.VALID_STATE
                                                : AbstractSettingPanel.State.INVALID_STATE;
            if (newState != state) {
                setState(newState);
            }
        }

        public void controlAddressField() {

            if (aspMember.isSelected()) {
                hostAddressField.setText("");
                hostAddressField.setEnabled(false);

            } else if (facilityUser.isSelected()) {
                hostAddressField.setEnabled(true);
            }

            this.checkState();
        }

        private boolean isValid() {

            boolean hostAddrOk = isIPAddress(hostAddressField.getText().trim());
            boolean facilityIdOk = (facilityIdField.getText().trim().equals("") == false);
            boolean userIdOk = (userIdField.getText().trim().equals("") == false);

            if (facilityUser.isSelected()) {
                //masuda registerTesterBtnも制御する
                boolean ret = facilityIdOk && hostAddrOk && userIdOk;
                registTesterBtn.setEnabled(ret);
                return ret;

            } else {
                return (facilityIdOk && userIdOk);
            }
        }

        private boolean isIPAddress(String test) {

            boolean ret = false;

            if (test != null) {
                // hostname or hostname:port or address or address:port
                String adr[] = test.split(":");
                if (adr[0].matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+") || adr[0].matches("[A-Za-z].*")) { ret = true; }
                if (adr.length > 1 && !adr[1].matches("[0-9]+")) { ret = false; }
            }

            return ret;
        }
    }
}

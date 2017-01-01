package open.dolphin.setting;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import open.dolphin.client.*;
import open.dolphin.dao.OrcaEntry;
import open.dolphin.dao.OrcaMasterDao;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.helper.GridBagBuilder;
import open.dolphin.event.ProxyDocumentListener;
import open.dolphin.project.ProjectStub;
import open.dolphin.ui.IMEControl;

/**
 * ClaimSettingPanel
 *
 * @author Kazushi Minagawa Digital Globe, Inc.
 *
 */
public class ClaimSettingPanel extends AbstractSettingPanel {

    private static final String ID = "claimSetting";
    private static final String TITLE = "ORCA";
    private static final ImageIcon ICON = GUIConst.ICON_COMPUTER_32;

    // GUI staff
    private JRadioButton sendClaimYes;
    private JRadioButton sendClaimNo;
    private JComboBox claimHostCombo;
    private JCheckBox claim01;
    //private JRadioButton v34;
    //private JRadioButton v40;
    private JTextField jmariField;
    private JTextField claimAddressField;
    private JTextField claimPortField;
    // private JCheckBox useAsPVTServer;

    private JRadioButton useOrcaApi;
    private JRadioButton useClaim;
    private JTextField orcaUserIdField;
    private JPasswordField orcaPasswordField;
    private JTextField orcaStaffCodeField;
    private JButton orcaStaffCodeButton;

    /** 画面モデル */
    private ClaimModel model;

    private StateMgr stateMgr;


    public ClaimSettingPanel() {
        this.setId(ID);
        this.setTitle(TITLE);
        this.setIcon(ICON);
    }

    /**
     * GUI 及び State を生成する.
     */
    @Override
    public void start() {

        //
        // モデルを生成し初期化する
        //
        model = new ClaimModel();
        model.populate(getProjectStub());

        //
        // GUIを構築する
        //
        initComponents();

        //
        // bind する
        //
        bindModelToView();
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
     * GUIを構築する
     */
    private void initComponents() {

        // 診療行為送信ボタン
        ButtonGroup bg1 = new ButtonGroup();
        sendClaimYes = GUIFactory.createRadioButton("送信する", null, bg1);
        sendClaimNo = GUIFactory.createRadioButton("送信しない", null, bg1);

        // バージョン
        ButtonGroup bg2 = new ButtonGroup();
        //v34 = GUIFactory.createRadioButton("3.4", null, bg2);
        //v40 = GUIFactory.createRadioButton("4.0", null, bg2);

        // ORCA API
        useClaim = GUIFactory.createRadioButton("CLAIM", null, bg2);
        useOrcaApi = GUIFactory.createRadioButton("ORCA API", null, bg2);
        orcaUserIdField = new JTextField(10);
        orcaPasswordField = new JPasswordField(10);
        orcaStaffCodeField = new JTextField(10);
        orcaStaffCodeButton = new JButton("コード検索");
        orcaStaffCodeButton.addActionListener(new ActionListener(){
            /**
             * Orca Dao を使って，職員コードを検索する
             */
            @Override
            public void actionPerformed(ActionEvent ev) {
                orcaStaffCodeField.setText("");

                // 初めて起動したときは orca アドレスがまだ Project に設定されていないので，現在の field から数値を取る
                getProjectStub().setClaimAddress(claimAddressField.getText().trim());

                OrcaMasterDao dao = SqlDaoFactory.createOrcaMasterDao();
                ArrayList<OrcaEntry> entry = dao.getSyskanriEntries("1010"); // 1010 職員情報

                for (OrcaEntry e : entry) {
                    String drid = e.getComment();   // 最初の 16 文字が ユーザー ID
                    String cd = e.getCode();        // ORCA の ドクター ID（職員コード）

                    if (drid != null && cd != null) {
                        drid = drid.substring(0, 16).trim();
                        cd = cd.trim();
                        // ユーザーID が一致した職員コードを doctor id field に設定する
                        if (drid.equals(orcaUserIdField.getText())) {
                            orcaStaffCodeField.setText(cd);
                            break;
                        }
                    }
                }

            }
        });

        // 01 小児科等
        claim01 = new JCheckBox("デフォルト01を使用");

        // JMARI，ホスト名，アドレス，ポート番号
        String[] hostNames = ClientContext.getStringArray("settingDialog.claim.hostNames");
        claimHostCombo = new JComboBox(hostNames);
        jmariField = GUIFactory.createTextField(10, null, null, null);
        jmariField.setToolTipText("医療機関コードの数字部分のみ12桁を入力してください。");
        claimAddressField = GUIFactory.createTextField(10, null, null, null);
        claimPortField = GUIFactory.createTextField(5, null, null, null);

        // 受付受信ボタン
        // useAsPVTServer = GUIFactory.createCheckBox("サーバからの患者受付通知を受信する", null);

        // CLAIM（請求）送信情報
        GridBagBuilder gbl = new GridBagBuilder("CLAIM（請求データ）送信");
        int row = 0;
        JLabel label = new JLabel("診療行為送信:");
        JPanel panel = GUIFactory.createRadioPanel(new JRadioButton[]{sendClaimYes,sendClaimNo});
        gbl.add(label, 0, row, GridBagConstraints.EAST);
        gbl.add(panel, 1, row, GridBagConstraints.CENTER);
        JPanel sendClaim = gbl.getProduct();

        // レセコン情報
        gbl = new GridBagBuilder("ORCA通信情報");
        //row = 0;
        //label = new JLabel("機種:");
        //gbl.add(label,          0, row, GridBagConstraints.EAST);
        //gbl.add(claimHostCombo, 1, row, GridBagConstraints.WEST);

        //row++;
        row = 0;
        //label = new JLabel("バージョン:");
        //JPanel vPanel = GUIFactory.createRadioPanel(new JRadioButton[]{v34,v40});
        label = new JLabel("通信方法:");
        JPanel vPanel = GUIFactory.createRadioPanel(new JRadioButton[]{useOrcaApi, useClaim});
        gbl.add(label,  0, row, GridBagConstraints.EAST);
        gbl.add(vPanel, 1, row, GridBagConstraints.WEST);

        //row++;
        //label = new JLabel("CLAIM診療科コード:");
        //gbl.add(label,  0, row, GridBagConstraints.EAST);
        //gbl.add(claim01,1, row, GridBagConstraints.WEST);

        row++;
        label = new JLabel("ORCA ログインID:");
        gbl.add(label,  0, row, GridBagConstraints.EAST);
        gbl.add(orcaUserIdField,1, row, GridBagConstraints.WEST);

        row++;
        label = new JLabel("ORCA パスワード:");
        gbl.add(label,  0, row, GridBagConstraints.EAST);
        gbl.add(orcaPasswordField,1, row, GridBagConstraints.WEST);

        row++;
        label = new JLabel("ORCA 職員コード:");
        JPanel doctorPanel = new JPanel();
        doctorPanel.setLayout(new BoxLayout(doctorPanel, BoxLayout.X_AXIS));
        doctorPanel.add(orcaStaffCodeField);
        doctorPanel.add(orcaStaffCodeButton);
        gbl.add(label,  0, row, GridBagConstraints.EAST);
        gbl.add(doctorPanel,1, row, GridBagConstraints.WEST);

        row++;
        label = new JLabel("医療機関ID:  JPN");
        gbl.add(label,      0, row, GridBagConstraints.EAST);
        gbl.add(jmariField, 1, row, GridBagConstraints.WEST);

        row++;
        label = new JLabel("IPアドレス:");
        gbl.add(label,             0, row, GridBagConstraints.EAST);
        gbl.add(claimAddressField, 1, row, GridBagConstraints.WEST);

        row++;
        label = new JLabel("ポート番号:");
        gbl.add(label,          0, row, GridBagConstraints.EAST);
        gbl.add(claimPortField, 1, row, GridBagConstraints.WEST);
        JPanel port = gbl.getProduct();

        // レセコンからの受付受信
        //gbl = new GridBagBuilder("受付情報の受信");
        //gbl.add(useAsPVTServer, 0, 0, GridBagConstraints.CENTER);
        //JPanel pvt = gbl.getProduct();

        // 全体レイアウト
        gbl = new GridBagBuilder();
        gbl.add(sendClaim, 0, 0, GridBagConstraints.HORIZONTAL, 1.0, 0.0);
        gbl.add(port,      0, 1, GridBagConstraints.HORIZONTAL, 1.0, 0.0);
        //gbl.add(pvt,       0, 2, GridBagConstraints.HORIZONTAL, 1.0, 0.0);
        gbl.add(new JLabel(""), 0, 3, GridBagConstraints.BOTH,  1.0, 1.0);
        setUI(gbl.getProduct());

        connect();
    }

    /**
     * リスナを接続する.
     */
    private void connect() {

        stateMgr = new StateMgr();

        // DocumentListener
        ProxyDocumentListener dl = e -> stateMgr.checkState();
        String jmariPattern = "[0-9]*";
        RegexConstrainedDocument jmariDoc = new RegexConstrainedDocument(jmariPattern);
        jmariField.setDocument(jmariDoc);
        jmariField.getDocument().addDocumentListener(dl);
        jmariField.setEnabled(true);
        IMEControl.setImeOffIfFocused(jmariField);

        String portPattern = "[0-9]*";
        RegexConstrainedDocument portDoc = new RegexConstrainedDocument(portPattern);
        claimPortField.setDocument(portDoc);
        claimPortField.getDocument().addDocumentListener(dl);
        IMEControl.setImeOffIfFocused(claimPortField);

        String ipPattern = "[A-Za-z0-9.]*";
        RegexConstrainedDocument ipDoc = new RegexConstrainedDocument(ipPattern);
        claimAddressField.setDocument(ipDoc);
        claimAddressField.getDocument().addDocumentListener(dl);
        IMEControl.setImeOffIfFocused(claimAddressField);

        // アクションリスナ
        ActionListener al = e -> stateMgr.controlClaim();
        sendClaimYes.addActionListener(al);
        sendClaimNo.addActionListener(al);

        // バージョン制御
        //ActionListener al2 = ProxyActionListener.create(stateMgr, "controlVersion");
        //v34.addActionListener(al2);
        //v40.addActionListener(al2);

        // orca api
        ActionListener al2 = e -> stateMgr.useOrcaApi();
        useOrcaApi.addActionListener(al2);
        useClaim.addActionListener(al2);
        orcaUserIdField.getDocument().addDocumentListener(dl);
        IMEControl.setImeOffIfFocused(orcaUserIdField);
        orcaPasswordField.getDocument().addDocumentListener(dl);
        IMEControl.setImeOffIfFocused(orcaPasswordField);
        orcaStaffCodeField.getDocument().addDocumentListener(dl);
        IMEControl.setImeOffIfFocused(orcaStaffCodeField);
    }

    /**
     * ModelToView
     */
    private void bindModelToView() {
        //
        // 診療行為送信を選択する
        //
        boolean sending = model.isSendClaim();
        sendClaimYes.setSelected(sending);
        sendClaimNo.setSelected(!sending);
        claimPortField.setEnabled(sending);

        // バージョン 選択
        //String ver = model.getVersion();
        //if (ver.startsWith("4")) {
        //    v40.setSelected(true);
        //} else {
        //    v34.setSelected(true);
        //}

        // orca api
        boolean orcaApi = model.isUseOrcaApi();
        if (orcaApi) useOrcaApi.setSelected(true);
        else useClaim.setSelected(true);
        orcaUserIdField.setEnabled(orcaApi);
        orcaUserIdField.setText(model.getOrcaUserId());
        orcaPasswordField.setEnabled(orcaApi);
        orcaPasswordField.setText(model.getOrcaPassword());
        orcaStaffCodeField.setEnabled(orcaApi);
        orcaStaffCodeField.setText(model.getOrcaStaffCode());
        orcaStaffCodeButton.setEnabled(orcaApi);

        // JMARICode
        String jmari = model.getJmariCode();
        jmari = jmari != null ? jmari : "";
        if (!jmari.equals("") && jmari.startsWith("JPN")) {
            jmari = jmari.substring(3);
            jmariField.setText(jmari);
        }

        // CLAIM ホストのIPアドレスを設定する
        claimAddressField.setText(model.getClaimAddress());

        // CLAIM ホストのポート番号を設定する
        String val = String.valueOf(model.getClaimPort());
        val = val != null ? val : "";
        claimPortField.setText(val);
        claimPortField.setEnabled(!orcaApi);

        // ホスト名
        claimHostCombo.setSelectedItem(model.getClaimHostName());

        // 受付受信
        //useAsPVTServer.setSelected(model.isUseAsPVTServer());

        // 01 小児科
        claim01.setSelected(model.isClaim01());

    }

    /**
     * ViewToModel
     */
    private void bindViewToModel() {
        //
        // 診療行為送信，仮保存時，修正時，病名送信
        // の設定を保存する
        //
        model.setSendClaim(sendClaimYes.isSelected());

        // バージョン
        //if (v40.isSelected()) {
            model.setVersion("40");
        //} else {
        //    model.setVersion("34");
        //}

        // orca api
        model.setUseOrcaApi(useOrcaApi.isSelected());
        model.setOrcaUserId(orcaUserIdField.getText());
        model.setOrcaPassword(new String(orcaPasswordField.getPassword()));
        model.setOrcaStaffCode(orcaStaffCodeField.getText());

        // JMARI
        String jmari = jmariField.getText().trim();
        if (!jmari.equals("")) {
            model.setJmariCode("JPN"+jmari);
        } else {
            model.setJmariCode(null);
        }

        // ホスト名を保存する
        String val = (String)claimHostCombo.getSelectedItem();
        model.setClaimHostName(val);

        // IPアドレスを保存する
        val = claimAddressField.getText().trim();
        model.setClaimAddress(val);

        // ポート番号を保存する
        val = claimPortField.getText().trim();
        try {
            int port = Integer.parseInt(val);
            model.setClaimPort(port);

        } catch (NumberFormatException e) {
            System.out.println("ClaimSettingPanel.java: " + e);
            model.setClaimPort(5001);
        }

        // 受付受信を保存する
        // model.setUseAsPVTServer(useAsPVTServer.isSelected());

        // 01 小児科
        model.setClaim01(claim01.isSelected());
    }

    /**
     * 画面モデルクラス.
     */
    private class ClaimModel {

        private boolean sendClaim;
        private String claimHostName;
        private String version;
        private String jmariCode;
        private String claimAddress;
        private int claimPort;
        // private boolean useAsPvtServer;
        private boolean claim01;
        private boolean useOrcaApi;
        private String orcaUserId;
        private String orcaPassword;
        private String orcaStaffCode;

        public void populate(ProjectStub stub) {

            // 診療行為送信
            setSendClaim(stub.getSendClaim());

            // バージョン
            setVersion(stub.getOrcaVersion());

            // JMARI code
            setJmariCode(stub.getJMARICode());

            // CLAIM ホストのIPアドレス
            setClaimAddress(stub.getClaimAddress());

            // CLAIM ホストのポート番号
            setClaimPort(stub.getClaimPort());

            // ホスト名
            setClaimHostName(stub.getClaimHostName());

            // 受付受信
            // setUseAsPVTServer(stub.getUseAsPVTServer());

            // 01 小児科等
            setClaim01(stub.isClaim01());

            // orca api
            setUseOrcaApi(stub.isUseOrcaApi());
            setOrcaUserId(stub.getOrcaUserId());
            setOrcaPassword(stub.getOrcaPassword());
            setOrcaStaffCode(stub.getOrcaStaffCode());
        }

        public void restore(ProjectStub stub) {

            // 診療行為送信
            stub.setSendClaim(isSendClaim());

            // バージョン
            stub.setOrcaVersion(getVersion());
            //System.out.println(stub.getOrcaVersion());

            // JMARI
            stub.setJMARICode(getJmariCode());
            //System.out.println(stub.getJMARICode());

            // CLAIM ホストのIPアドレス
            stub.setClaimAddress(getClaimAddress());

            // CLAIM ホストのポート番号
            stub.setClaimPort(getClaimPort());

            // ホスト名
            stub.setClaimHostName(getClaimHostName());

            // 受付受信
            // stub.setUseAsPVTServer(isUseAsPVTServer());

            // 01 小児科
            stub.setClaim01(isClaim01());

            // orca api
            stub.setUseOrcaApi(isUseOrcaApi());
            stub.setOrcaUserIdPassword(getOrcaUserId(), getOrcaPassword());
            stub.setOrcaStaffCode(getOrcaStaffCode());
        }

        public boolean isSendClaim() {
            return sendClaim;
        }

        public void setSendClaim(boolean sendClaim) {
            this.sendClaim = sendClaim;
        }

        // public boolean isUseAsPVTServer() {
        //     return useAsPvtServer;
        // }

        // public void setUseAsPVTServer(boolean useAsPvtServer) {
        //     this.useAsPvtServer = useAsPvtServer;
        // }

        public String getClaimHostName() {
            return claimHostName == null? "" : claimHostName;
        }

        public void setClaimHostName(String claimHostName) {
            this.claimHostName = claimHostName;
        }

        public String getClaimAddress() {
            return claimAddress == null? "" : claimAddress;
        }

        public void setClaimAddress(String claimAddress) {
            this.claimAddress = claimAddress;
        }

        public int getClaimPort() {
            return claimPort;
        }

        public void setClaimPort(int claimPort) {
            this.claimPort = claimPort;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getJmariCode() {
            return jmariCode;
        }

        public void setJmariCode(String jmariCode) {
            this.jmariCode = jmariCode;
        }

        public boolean isClaim01() {
            return claim01;
        }

        public void setClaim01(boolean b) {
            this.claim01 = b;
        }

        public boolean isUseOrcaApi() {
            return useOrcaApi;
        }

        public void setUseOrcaApi(boolean b) {
            this.useOrcaApi = b;
        }

        public String getOrcaUserId() {
            return orcaUserId == null? "" : orcaUserId;
        }

        public void setOrcaUserId(String id) {
            this.orcaUserId = id;
        }

        public String getOrcaPassword() {
            return orcaPassword == null? "" : orcaPassword;
        }

        public void setOrcaPassword(String pass) {
            this.orcaPassword = pass;
        }

        public String getOrcaStaffCode() {
            return orcaStaffCode == null? "" : orcaStaffCode;
        }

        public void setOrcaStaffCode(String id) {
            this.orcaStaffCode = id;
        }
    }

    public class StateMgr { // should be 'public'

        public void checkState() {

            AbstractSettingPanel.State newState = isValid()
            ? AbstractSettingPanel.State.VALID_STATE
                    : AbstractSettingPanel.State.INVALID_STATE;
            if (newState != state) {
                setState(newState);
            }
        }

        public void controlClaim() {

            //
            // 診療行為の送信を行う場合のみ
            // 仮保存，修正，病名送信，ホスト選択，ポートがアクティブになる
            //
            boolean b = sendClaimYes.isSelected();

            //claimHostCombo.setEnabled(b);
            claimPortField.setEnabled(b);

            this.checkState();
        }

        //public void controlVersion() {

        //    boolean b = v40.isSelected();
        //    jmariField.setEnabled(b);
        //    this.checkState();
        //}

        public void useOrcaApi() {
            boolean orcaApi = useOrcaApi.isSelected();
            orcaUserIdField.setEnabled(orcaApi);
            orcaPasswordField.setEnabled(orcaApi);
            orcaStaffCodeField.setEnabled(orcaApi);
            orcaStaffCodeButton.setEnabled(orcaApi);
            claimPortField.setEnabled(!orcaApi);
            this.checkState();
        }

        private boolean isValid() {

            boolean jmariOk = false;
            boolean claimAddrOk;
            boolean claimPortOk;
            boolean orcaApiOk;

            //if (v40.isSelected()) {
                String code = jmariField.getText().trim();
                if (!code.equals("") && code.length() == 12) {
                    jmariOk = true;
                }
            //} else {
            //    jmariOk = true;
            //}

            if (sendClaimYes.isSelected()) {
                claimAddrOk = !claimAddressField.getText().trim().equals("");

                if (useOrcaApi.isSelected()) {
                    claimPortOk = true;
                    orcaApiOk = !orcaUserIdField.getText().trim().equals("") && !orcaStaffCodeField.getText().trim().equals("");

                } else {
                    claimPortOk = !claimPortField.getText().trim().equals("");
                    orcaApiOk = true;
                }
            } else {
                claimAddrOk = true;
                claimPortOk = true;
                orcaApiOk = true;
            }

            return jmariOk && claimAddrOk && claimPortOk && orcaApiOk;
        }
    }
}

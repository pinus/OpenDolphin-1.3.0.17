package open.dolphin.impl.login;

import open.dolphin.client.ClientContext;
import open.dolphin.client.Dolphin;
import open.dolphin.delegater.DolphinClientContext;
import open.dolphin.delegater.UserDelegater;
import open.dolphin.event.ProxyDocumentListener;
import open.dolphin.helper.PNSTask;
import open.dolphin.infomodel.UserModel;
import open.dolphin.project.DolphinPrincipal;
import open.dolphin.project.Project;
import open.dolphin.setting.ProjectSettingDialog;
import open.dolphin.ui.BlockGlass2;
import open.dolphin.ui.Focuser;
import open.dolphin.ui.PNSOptionPane;
import open.dolphin.util.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

/**
 * ログインダイアログ　クラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class LoginDialog {

    private LoginView view;
    private BlockGlass2 blockGlass;

    // 認証制御用
    private Logger logger;
    private int tryCount;
    private int maxTryCount;

    // 認証状態
    private LoginState result;

    // 認証状態 Listener
    private LoginListener loginListener;

    // モデル
    private DolphinPrincipal principal;

    // State
    private boolean okState;

    /**
     * Creates new LoginDialog.
     */
    public LoginDialog() {
    }

    /**
     * ログイン状態リスナーを登録する.
     *
     * @param listener LoginListener
     */
    public void addLoginListener(LoginListener listener) {
        loginListener = listener;
    }

    /**
     * ログイン画面を開始する.
     */
    public void start() {
        // GUI を構築しモデルを表示する
        initComponents();
        bindModelToView();

        // LoginView をセンタリングして表示
        int width = view.getWidth();
        int height = view.getHeight();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int n = Dolphin.forWin ? 2 : 3;
        int left = (screen.width - width) / 2;
        int top = (screen.height - height) / n;
        view.setLocation(left, top);
        view.setVisible(true);
    }

    /**
     * ログインリスナに結果を通知する.
     */
    private void notifyResult(final LoginState ret) {
        loginListener.state(ret);
    }

    /**
     * 認証を試みる.
     * DatabaseLoginModule を使用，UserValueが取得できた場合に認証が成功したとみなす.
     * 詳細はBusiness Delegator へ委譲.
     */
    public void tryLogin() {

        // ロガーを取得する
        if (logger == null) {
            logger = LoggerFactory.getLogger(LoginDialog.class);
        }

        // トライ出来る最大回数を得る
        if (maxTryCount == 0) {
            maxTryCount = ClientContext.getInt("loginDialog.maxTryCount");
        }

        logger.info("認証を開始します");

        // 試行回数 += 1
        tryCount++;

        // userIdとpasswordを取得する
        bindViewToModel();
        final String password = new String(view.getPasswordField().getPassword());

        // LoginTask を生成する
        int maxEstimation = ClientContext.getInt("task.default.maxEstimation");
        //int delay = ClientContext.getInt("task.default.delay");
        //int lengthOfTask = maxEstimation / delay;    // タスクの長さ = 最大予想時間 / 割り込み間隔

        String message = "ログイン";
        String note = "認証中...";

        PNSTask<UserModel> task = new PNSTask<>(view, message, note, maxEstimation) {
            @Override
            protected UserModel doInBackground() {
                // ログイン手順開始
                String hostAddress = String.format("%s:%d", Project.getHostAddress(), Project.getHostPort());
                String pk = principal.getFacilityId() + ":" + principal.getUserId();
                DolphinClientContext.configure(hostAddress, pk, password);

                // User 情報を取得するためのデリゲータを得る
                UserDelegater userDlg = new UserDelegater();
                return userDlg.getUser(pk);
            }

            @Override
            protected void succeeded(UserModel userModel) {
                logger.debug("Task succeeded");
                if (userModel != null) {

                    Project.UserType userType = Project.UserType.valueOf(userModel.getMemberType());
                    logger.info("User Type = " + userType);

                    // 認証成功
                    String time = ModelUtils.getDateTimeAsString(new Date());
                    logger.info(time + ": " + userModel.getUserId() + " がログインしました");

                    // ユーザID，施設ID，ユーザモデルを ProjectStub へ保存する
                    Project.getProjectStub().setUserId(principal.getUserId());
                    Project.getProjectStub().setUserModel(userModel);
                    Project.getProjectStub().setDolphinPrincipal(principal);
                    Project.getProjectStub().setUserPassword(password);

                    result = LoginState.AUTHENTICATED;
                    notifyClose(result);

                } else {
                    logger.warn("User == null, this should never occur");
                }
            }

            @Override
            protected void failed(java.lang.Throwable cause) {
                logger.warn("Task failed");
                logger.warn(cause.getMessage());
                cause.printStackTrace(System.err);

                if (tryCount <= maxTryCount) {
                    String errMsg = cause.getMessage();
                    showMessageDialog(errMsg);
                } else {
                    showMessageDialog(String.format("%s\nアプリケーションを終了します", cause.getMessage()));
                    result = LoginState.NOT_AUTHENTICATED;
                    notifyClose(result);
                }
            }
        };
        task.setInputBlocker(new Blocker());
        task.setMillisToDecidePopup(3000);
        task.execute();
    }

    /**
     * 警告メッセージを表示する.
     *
     * @param msg 表示するメッセージ
     */
    private void showMessageDialog(String msg) {
        String title = view.getTitle();

        PNSOptionPane.showMessageDialog(view, msg, title, JOptionPane.WARNING_MESSAGE);
        EventQueue.invokeLater(() -> {
            view.getPasswordField().requestFocusInWindow();
            view.getPasswordField().selectAll();
        });
    }

    /**
     * ログインダイアログを終了する.
     *
     * @param result LoginState
     */
    private void notifyClose(LoginState result) {
        view.setVisible(false);
        view.dispose();
        notifyResult(result);
    }

    /**
     * GUI を構築する.
     */
    private void initComponents() {

        view = new LoginView();
        view.getRootPane().setDefaultButton(view.getLoginBtn());
        blockGlass = new BlockGlass2();
        view.setGlassPane(blockGlass);

        setWindowTitle();

        // イベント接続を行う
        connect();
    }

    /**
     * window title を表示する.　HostAddress-OpenDolphin-Version という表示.
     */
    private void setWindowTitle() {
        String title = ClientContext.getFrameTitle(Project.getProjectStub().getHostAddress());
        view.setTitle(title);
    }

    /**
     * イベント接続を行う.
     */
    private void connect() {
        //
        // フィールドにリスナを登録する
        //
        // User ID Field
        JTextField userIdField = view.getUserIdField();
        userIdField.getDocument().addDocumentListener((ProxyDocumentListener) e -> checkButtons());
        userIdField.addActionListener(e -> requestFocus(view.getPasswordField()));

        // Password Field
        JPasswordField passwdField = view.getPasswordField();
        passwdField.getDocument().addDocumentListener((ProxyDocumentListener) e -> checkButtons());
        passwdField.addActionListener(e -> {
            if (view.getUserIdField().getText().isEmpty()) {
                requestFocus(view.getUserIdField());

            } else if (view.getPasswordField().getPassword().length != 0 && okState) {
                // ログインボタンをクリックする
                view.getLoginBtn().doClick();
            }
        });

        //
        // ボタンに ActionListener を登録する
        //
        view.getSettingBtn().addActionListener(ev -> doSettingDialog());
        view.getCancelBtn().addActionListener(ev -> doCancel());
        view.getLoginBtn().addActionListener(ev -> tryLogin());
        view.getLoginBtn().setEnabled(false);

        //
        // ダイアログに WindowAdapter を設定する
        //
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {

                if (!view.getUserIdField().getText().trim().isEmpty()) {
                    // UserId に有効な値が設定されていればパスワードフィールドにフォーカスする
                    requestFocus(view.getPasswordField());
                }
            }
        });
    }

    /**
     * モデルを表示する.
     */
    private void bindModelToView() {
        // host
        view.getHostField().setText(Project.getProjectStub().getHostAddress());

        // user id, facility id, password
        String uid = Project.getUserId();
        String fid = Project.getFacilityId();
        String password = Project.getUserPassword();
        boolean savePassword = Project.getProjectStub().isSavePassword();

        view.getUserIdField().setText(uid);
        view.getSavePasswordCbx().setSelected(savePassword);
        if (view.getSavePasswordCbx().isSelected()) {
            view.getPasswordField().setText(password);
            view.getPasswordField().selectAll();
        }

        // save data in DolphinPrincipal
        principal = new DolphinPrincipal();
        principal.setUserId(uid);
        principal.setFacilityId(fid);
    }

    /**
     * モデル値を取得する.
     */
    private void bindViewToModel() {
        // host
        Project.getProjectStub().setHostAddress(view.getHostField().getText());

        // user id
        String id = view.getUserIdField().getText().trim();
        if (!id.isEmpty()) {
            principal.setUserId(id);
        }
        // save password
        Project.getProjectStub().setSavePassword(view.getSavePasswordCbx().isSelected());
    }

    /**
     * 設定ボタンがおされた時，設定画面を開始する.
     */
    public void doSettingDialog() {
        blockGlass.setVisible(true);

        ProjectSettingDialog sd = new ProjectSettingDialog();
        sd.addValidListener(this::setNewParams);
        sd.setLoginState(false);
        sd.start();
    }

    /**
     * 設定ダイアログから通知を受ける.
     * 有効なプロジェクトでればユーザIDをフィールドに設定しパスワードフィールドにフォーカスする.
     *
     * @param valid Validity
     **/
    public void setNewParams(Boolean valid) {
        blockGlass.setVisible(false);

        if (valid) {
            principal.setUserId(Project.getUserId());
            principal.setFacilityId(Project.getFacilityId());
            bindModelToView();
            view.getPasswordField().requestFocus();
            setWindowTitle();
        }
    }

    /**
     * ログインをキャンセルする.
     */
    public void doCancel() {
        view.setVisible(false);
        view.dispose();
        result = LoginState.CANCELD;
        notifyResult(result);

        bindViewToModel();
    }

    /**
     * ログインボタンの enable/disable を制御する.
     */
    public void checkButtons() {
        boolean userEmpty = view.getUserIdField().getText().isEmpty();
        boolean passwdEmpty = view.getPasswordField().getPassword().length == 0;
        boolean newOKState = !userEmpty && !passwdEmpty;

        if (newOKState != okState) {
            view.getLoginBtn().setEnabled(newOKState);
            okState = newOKState;
        }
    }

    private void requestFocus(Component c) {
        Focuser.requestFocus(c);
    }

    private class Blocker implements PNSTask.InputBlocker {
        @Override
        public void block() {
            blockGlass.setVisible(true);
            view.getUserIdField().setEnabled(false);
            view.getPasswordField().setEnabled(false);
            view.getSettingBtn().setEnabled(false);
            view.getLoginBtn().setEnabled(false);
            view.getCancelBtn().setEnabled(false);
        }

        @Override
        public void unblock() {
            blockGlass.setVisible(false);
            view.getUserIdField().setEnabled(true);
            view.getPasswordField().setEnabled(true);
            view.getSettingBtn().setEnabled(true);
            view.getLoginBtn().setEnabled(true);
            view.getCancelBtn().setEnabled(true);
        }
    }
}

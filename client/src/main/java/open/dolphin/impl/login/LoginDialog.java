package open.dolphin.impl.login;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import open.dolphin.client.BlockGlass;
import open.dolphin.client.ClientContext;
import open.dolphin.delegater.DolphinClientContext;
import open.dolphin.delegater.UserDelegater;
import open.dolphin.event.ProxyDocumentListener;
import open.dolphin.helper.Task;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.UserModel;
import open.dolphin.project.DolphinPrincipal;
import open.dolphin.project.Project;
import open.dolphin.setting.ProjectSettingDialog;
import open.dolphin.ui.Focuser;
import open.dolphin.ui.IMEControl;
import org.apache.log4j.Logger;

/**
 * ログインダイアログ　クラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class LoginDialog {

    private LoginView view;
    private BlockGlass blockGlass;

    // 認証制御用
    private Logger part11Logger;
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
     * @param listener
     */
    public void addLoginListener(LoginListener listener) {
        loginListener = listener;
    }

    /**
     * ログイン画面を開始する.
     */
    public void start() {

        //
        // ダイアログモデルを生成し値を初期化する
        //
        principal = new DolphinPrincipal();
        if (Project.isValid()) {
            principal.setFacilityId(Project.getFacilityId());
            principal.setUserId(Project.getUserId());
        }

        //
        // GUI を構築しモデルを表示する
        //
        initComponents();
        bindModelToView();

        //
        // EDT からコールされている
        //
        int width = view.getWidth();
        int height = view.getHeight();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int n = ClientContext.isMac() ? 3 : 2;
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
     * 詳細はBusiness Delegater へ委譲.
     */
    public void tryLogin() {

        // Part11 ロガーを取得する
        if (part11Logger == null) {
            part11Logger = ClientContext.getPart11Logger();
        }

        // トライ出来る最大回数を得る
        if (maxTryCount == 0) {
            maxTryCount = ClientContext.getInt("loginDialog.maxTryCount");
        }

        part11Logger.info("認証を開始します");

        // 試行回数 += 1
        tryCount++;

        // userIdとpasswordを取得する
        bindViewToModel();
        final String password = new String(view.getPasswordField().getPassword());

        // LoginTask を生成する
        int maxEstimation = ClientContext.getInt("task.default.maxEstimation");
        int delay = ClientContext.getInt("task.default.delay");
        int lengthOfTask = maxEstimation / delay;	// タスクの長さ = 最大予想時間 / 割り込み間隔

        String message = "ログイン";
        String note = "認証中...";

        Task task = new Task<UserModel>(view, message, note, maxEstimation) {
            private UserDelegater userDlg;

            @Override
            protected UserModel doInBackground() throws Exception {

                // ログイン手順開始
                String hostAddress = String.format("%s:%d", Project.getHostAddress(), Project.getHostPort());
                String pk = principal.getFacilityId() + ":" + principal.getUserId();
                DolphinClientContext.configure(hostAddress, pk, password);

                // User 情報を取得するためのデリゲータを得る
                userDlg = new UserDelegater();

                return userDlg.getUser(pk);
            }

            @Override
            protected void succeeded(UserModel userModel) {
                part11Logger.debug("Task succeeded");
                if (userModel != null) {

                    Project.UserType userType = Project.UserType.valueOf(userModel.getMemberType());
                    part11Logger.info("User Type = " + userType.toString());

                    // 認証成功
                    String time = ModelUtils.getDateTimeAsString(new Date());
                    part11Logger.info(time + ": " + userModel.getUserId() + " がログインしました");

                    // ユーザID，施設ID，ユーザモデルを rojectStub へ保存する
                    Project.getProjectStub().setUserId(principal.getUserId());
                    Project.getProjectStub().setUserModel(userModel);
                    Project.getProjectStub().setDolphinPrincipal(principal);
                    Project.getProjectStub().setUserPassword(password);

                    result = LoginState.AUTHENTICATED;
                    notifyClose(result);

                } else {
                    part11Logger.warn("User == null, this never ocuured");
                }
            }

            @Override
            protected void failed(java.lang.Throwable cause) {
                part11Logger.warn("Task failed");
                part11Logger.warn(cause.getCause());
                part11Logger.warn(cause.getMessage());
                if (tryCount <= maxTryCount && cause instanceof Exception) {
                    userDlg.processError((Exception) cause);
                    String errMsg = userDlg.getErrorMessage();
                    showMessageDialog(errMsg);
                } else {
                    StringBuilder sb = new StringBuilder();
                    userDlg.processError((Exception) cause);
                    sb.append(userDlg.getErrorMessage());
                    sb.append("\n");
                    sb.append(ClientContext.getString("loginDialog.forceClose"));
                    String msg = sb.toString();
                    showMessageDialog(msg);
                    result = LoginState.NOT_AUTHENTICATED;
                    notifyClose(result);
                }
            }
        };
        task.setInputBlocker(new Blocker());
        task.setMillisToDecidePopup(3000);
        task.execute();
    }

    private class Blocker implements Task.InputBlocker {

        @Override
        public void block() {
            blockGlass.block();
            view.getUserIdField().setEnabled(false);
            view.getPasswordField().setEnabled(false);
            view.getSettingBtn().setEnabled(false);
            view.getLoginBtn().setEnabled(false);
            view.getCancelBtn().setEnabled(false);
            view.getProgressBar().setIndeterminate(true);
        }

        @Override
        public void unblock() {
            blockGlass.unblock();
            view.getUserIdField().setEnabled(true);
            view.getPasswordField().setEnabled(true);
            view.getSettingBtn().setEnabled(true);
            view.getLoginBtn().setEnabled(true);
            view.getCancelBtn().setEnabled(true);
            view.getProgressBar().setIndeterminate(false);
            view.getProgressBar().setValue(0);
        }
    }

    /**
     * 警告メッセージを表示する.
     * @param msg 表示するメッセージ
     */
    private void showMessageDialog(String msg) {
        String title = view.getTitle();

        JOptionPane.showMessageDialog(view, msg, title, JOptionPane.WARNING_MESSAGE);
        EventQueue.invokeLater(() -> {
            view.getPasswordField().requestFocusInWindow();
            view.getPasswordField().selectAll();
        });
    }

    /**
     * ログインダイアログを終了する.
     * @param result
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

        view = new LoginView(null, false);
        view.getRootPane().setDefaultButton(view.getLoginBtn());
        blockGlass = new BlockGlass();
        view.setGlassPane(blockGlass);

        setWindowTitle();

        //
        // イベント接続を行う
        //
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
        IMEControl.setImeOffIfFocused(userIdField);
        userIdField.addActionListener(e -> requestFocus(view.getPasswordField()));

        // Password Field
        JPasswordField passwdField = view.getPasswordField();
        passwdField.getDocument().addDocumentListener((ProxyDocumentListener) e -> checkButtons());
        IMEControl.setImeOffIfFocused(passwdField);
        passwdField.addActionListener(e -> {
            if (view.getUserIdField().getText().equals("")) {
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

                if (!view.getUserIdField().getText().trim().equals("")) {
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

        if (principal.getUserId() != null && (!principal.getUserId().equals(""))) {
            view.getUserIdField().setText(principal.getUserId());
            view.getSavePasswordCbx().setSelected(Project.getProjectStub().isSavePassword());

            if (view.getSavePasswordCbx().isSelected()) {
                view.getPasswordField().setText(Project.getUserPassword());
                view.getPasswordField().selectAll();
            }
        }
    }

    /**
     * モデル値を取得する.
     */
    private void bindViewToModel() {

        String id = view.getUserIdField().getText().trim();

        if (!id.equals("")) {
            principal.setUserId(id);
        }

        Project.getProjectStub().setSavePassword(view.getSavePasswordCbx().isSelected());
    }


    /**
     * 設定ボタンがおされた時，設定画面を開始する.
     */
    public void doSettingDialog() {

        blockGlass.block();

        ProjectSettingDialog sd = new ProjectSettingDialog();
        sd.addValidListener(this::setNewParams);
        sd.setLoginState(false);
        sd.start();
    }

    /**
     * 設定ダイアログから通知を受ける.
     * 有効なプロジェクトでればユーザIDをフィールドに設定しパスワードフィールドにフォーカスする.
     * @param valid
     **/
    public void setNewParams(Boolean valid) {
        blockGlass.unblock();

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
            boolean userEmpty = view.getUserIdField().getText().length() == 0;
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
}

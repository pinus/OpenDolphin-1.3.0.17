package open.dolphin.impl.login;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import open.dolphin.client.BlockGlass;
import open.dolphin.client.ClientContext;
import open.dolphin.delegater.DolphinClientContext;
import open.dolphin.delegater.UserDelegater;
import open.dolphin.event.ProxyDocumentListener;
import open.dolphin.helper.ProxyPropertyChangeListener;
import open.dolphin.helper.Task;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.UserModel;
import open.dolphin.project.DolphinPrincipal;
import open.dolphin.project.Project;
import open.dolphin.setting.ProjectSettingDialog;
import open.dolphin.ui.IMEControl;
import open.dolphin.ui.MyJSheet;
import org.apache.log4j.Logger;

/**
 * ログインダイアログ　クラス.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class LoginDialog {

    /** Login Status */
    public enum LoginStatus {AUTHENTICATED, NOT_AUTHENTICATED, CANCELD};

    private LoginView view;
    private BlockGlass blockGlass;

    // 認証制御用
    private Logger part11Logger;
    private int tryCount;
    private int maxTryCount;

    // 認証結果のプロパティ
    private LoginStatus result;
    private PropertyChangeSupport boundSupport;

    // モデル
    private DolphinPrincipal principal;

    // StateMgr
    private StateMgr stateMgr;


    /**
     * Creates new LoginService
     */
    public LoginDialog() {
    }

    /**
     * 認証結果プロパティリスナを登録する.
     * @param prop
     * @param listener 登録する認証結果リスナ
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.addPropertyChangeListener(prop, listener);
    }

    /**
     * 認証結果プロパティリスナを登録する.
     * @param prop
     * @param listener 削除する認証結果リスナ
     */
    public void removePropertyChangeListener(String prop, PropertyChangeListener listener) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.addPropertyChangeListener(prop, listener);
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
     * 認証が成功したかどうかを返す.
     * @return true 認証が成功した場合
     */
    public LoginStatus getResult() {
        return result;
    }

    /**
     * PropertyChange で結果を受け取るアプリに通知する.
     * @param result true 認証が成功した場合
     */
    private void notifyResult(final LoginStatus ret) {
         SwingUtilities.invokeLater(() -> boundSupport.firePropertyChange("LOGIN_PROP", -100, ret));
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

                    result = LoginStatus.AUTHENTICATED;
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
                    result = LoginStatus.NOT_AUTHENTICATED;
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

        MyJSheet.showMessageDialog(view, msg, title, JOptionPane.WARNING_MESSAGE);
        EventQueue.invokeLater(() -> {
            view.getPasswordField().requestFocusInWindow();
            view.getPasswordField().selectAll();
        });
    }

    /**
     * ログインダイアログを終了する.
     * @param result
     */
    private void notifyClose(LoginStatus result) {
        view.setVisible(false);
        view.dispose();
        notifyResult(result);
    }

    /**
     * GUI を構築する.
     */
    private void initComponents() {

        view = new LoginView((Frame) null, false);
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
     * window title を表示する　HostAddress-OpenDolphin-Version という表示
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
        // Mediator ライクな StateMgr
        //
        stateMgr = new StateMgr();

        //
        // フィールドにリスナを登録する
        //
        // User ID Field
        JTextField userIdField = view.getUserIdField();
        userIdField.getDocument().addDocumentListener((ProxyDocumentListener) e -> stateMgr.checkButtons());
        IMEControl.setImeOffIfFocused(userIdField);
        userIdField.addActionListener(e -> stateMgr.onUserIdAction());
        // Password Field
        JPasswordField passwdField = view.getPasswordField();
        passwdField.getDocument().addDocumentListener((ProxyDocumentListener) e -> stateMgr.checkButtons());
        IMEControl.setImeOffIfFocused(passwdField);
        passwdField.addActionListener(e -> stateMgr.onPasswordAction());

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
        view.addWindowListener(stateMgr);
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
        PropertyChangeListener pl = --
                ProxyPropertyChangeListener.create(this, "setNewParams", new Class[]{Boolean.class});
        sd.addPropertyChangeListener("SETTING_PROP", pl);
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
        result = LoginStatus.CANCELD;
        notifyResult(result);

        bindViewToModel();
    }

    /**
     * ログインボタンを制御する簡易 StateMgr クラス.
     * ProxyDocumentListener から呼ばれるので，public でなければならない
     */
    public class StateMgr extends WindowAdapter {

        private boolean okState;

        public StateMgr() {
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

        /**
         * UserId フィールドでリターンきーが押された時の処理を行う.
         */
        public void onUserIdAction() {
            view.getPasswordField().requestFocus();
        }

        /**
         * Password フィールドでリターンきーが押された時の処理を行う.
         */
        public void onPasswordAction() {

            if (view.getUserIdField().getText().equals("")) {

                view.getUserIdField().requestFocus();

            } else if (view.getPasswordField().getPassword().length != 0 && okState) {
                //
                // ログインボタンをクリックする
                //
                view.getLoginBtn().doClick();
            }
        }

        @Override
        public void windowClosing(WindowEvent e) {
            doCancel();
        }

        @Override
        public void windowOpened(WindowEvent e) {

            if (!view.getUserIdField().getText().trim().equals("")) {
                //
                // UserId に有効な値が設定されていれば
                // パスワードフィールドにフォーカスする
                //
                view.getPasswordField().requestFocus();
            }
        }
    }
}

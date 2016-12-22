package open.dolphin.client;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeSupport;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import open.dolphin.helper.ProxyDocumentListener;
import open.dolphin.ui.MyJSheet;

/**
 * FindDialog の JSheet バージョン
 * @author pns
 */
public class FindDialog {

    private MyJSheet sheet;
    private final Frame parent;
    private boolean isSearchReady;
    private boolean isTextReady;
    private boolean isSoapBoxReady;

    private boolean isSoaBoxChecked; // soaBox がチェックされているかどうか
    private boolean isPBoxChecked; // pBox がチェックされているかどうか

    private static String searchText;
    private JCheckBox soaBox;
    private JCheckBox pBox;
    private JTextField searchTextField;
    private JButton searchButton;
    private JButton cancelButton;

    private PropertyChangeSupport boundSupport;

    public FindDialog(Frame parentComponent) {
        parent = parentComponent;
    }

    public void start() {
        initComponents();
        connect();
        sheet.setVisible(true);
    }

    private void initComponents() {

        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // TextField
        searchTextField = new JTextField(20);
        content.add(new JLabel("検索語"));
        content.add(searchTextField);
        // CheckBox
        soaBox = new JCheckBox("所見・症状欄");
        pBox = new JCheckBox("処置欄");
        content.add(soaBox);
        content.add(pBox);
        // Buttons
        searchButton = new JButton("検索");
        cancelButton = new JButton("キャンセル");

        Object[] options = new Object[] { searchButton, cancelButton };

        // OptinoPane
        JOptionPane jop = new JOptionPane(
                content,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                searchButton);

        sheet = MyJSheet.createDialog(jop, parent);

        // 初期値設定
        soaBox.setSelected(true);
        pBox.setSelected(true);
        searchButton.setEnabled(false);
        searchTextField.setText(searchText);
        checkState();
    }

    private void connect() {

        sheet.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                searchTextField.requestFocusInWindow();
                searchTextField.selectAll();
            }
            @Override
            public void windowActivated(WindowEvent e) {
                searchTextField.requestFocusInWindow();
                searchTextField.selectAll();
            }
        });

        // 検索文字列があれば，検索ボタンを有効化する　入力中にチェックする
        searchTextField.getDocument().addDocumentListener((ProxyDocumentListener) e -> checkState());

        // リターンが押されたときの処理
        searchTextField.addActionListener(e -> onTextAction());

        // ボタン類
        searchButton.addActionListener(e -> doSearch());
        cancelButton.addActionListener(e -> doCancel());

        pBox.addActionListener(e -> checkState());
        soaBox.addActionListener(e -> checkState());
    }

    // 検索ボタンの ON/OFF 制御
    private void setSearchButtonState() {
        isSearchReady = isSoapBoxReady && isTextReady;
        searchButton.setEnabled(isSearchReady);
        searchButton.setSelected(isSearchReady);
    }

    // 結果を保持している部分
    public String getSearchText() {
       return searchText;
    }

    public boolean isSoaBoxOn() {
        return isSoaBoxChecked;
    }

    public boolean isPBoxOn() {
        return isPBoxChecked;
    }

    public void doSearch() {
        searchText = searchTextField.getText();
        close();
    }

    public void doCancel() {
        searchText = "";
        close();
    }

    // 検索ボタンの状態を制御
    public void checkState () {
        isTextReady = !searchTextField.getText().equals("");
        isSoaBoxChecked = soaBox.isSelected();
        isPBoxChecked = pBox.isSelected();
        isSoapBoxReady = isSoaBoxChecked || isPBoxChecked;

        setSearchButtonState();
    }

    private void close() {
        sheet.setVisible(false);
        sheet.dispose();
    }

    /**
     * Text フィールドでリターンキーが押された時の処理を行う。
     */
    public void onTextAction() {
        if (isTextReady && isSearchReady) {
            searchButton.doClick();
        }
    }
}


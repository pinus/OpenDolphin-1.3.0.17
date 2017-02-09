package open.dolphin.ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.lang.StringUtils;

/**
 * CompletableJTextField.
 * modified from JAVA SWING HACKS.
 * @author pns
 */
public class CompletableJTextField extends JTextField
        implements ListSelectionListener, FocusListener, KeyListener, ComponentListener, ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String PREFS = "prefs";

    private Completer completer;
    private JList<String> completionList;
    private DefaultListModel<String> completionListModel;
    private JWindow listWindow;
    private Window parentFrame;
    private int keyCode;
    private Preferences prefs;

    public CompletableJTextField(int col) {
        super(col);
        initComponents();
        connect();
    }

    private void initComponents() {
        completer = new Completer();
        completionListModel = new DefaultListModel<>();
        completionList = new JList<>(completionListModel);
        completionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        completionList.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        completionList.setBackground(new Color(255,255,240));

        listWindow = new JWindow();
        listWindow.setOpacity(0.7f);
        listWindow.getContentPane().setLayout(new BorderLayout());
        listWindow.getContentPane().add(completionList, BorderLayout.CENTER);
    }

    private void connect() {
        completionList.addListSelectionListener(this);
        getDocument().addDocumentListener(completer);
        addFocusListener(this);
        addKeyListener(this);
        addComponentListener(this);
        addActionListener(this);
    }

    public void dispose() {
        listWindow.dispose();
    }

    /**
     * 補完した内容を保存する preferences をセットする.
     * @param prefs
     */
    public void setPreferences(Preferences prefs) {
        this.prefs = prefs;
        loadPrefs();
    }

    private void savePrefs() {
        StringBuilder sb = new StringBuilder();
        List<String> items = getCompletions();
        items.stream().forEach(s -> sb.append(s).append("\t"));

        prefs.put(PREFS, StringUtils.chop(sb.toString()));
    }

    private void loadPrefs() {
        String str = prefs.get(PREFS, "");
        String[] items = str.split("\t");
        // 古い方から登録
        for(int i=items.length-1; i>=0; i--) {
            completer.addCompletion(items[i]);
        }
    }

    public void addCompletion(String s) {
        completer.addCompletion(s);
        if (prefs != null) { savePrefs(); }
    }

    public void removeCompletion(String s) {
        completer.removeCompletion(s);
    }

    public void clearCompletions() {
        completer.clearCompletions();
    }

    private List<String> getCompletions() {
        return completer.getCompletions();
    }

    /**
     * 補完ウインドウを適切な場所に表示する.
     */
    private void showListWindow() {
        // figure out where the text field is,
        // and where its bottom left is
        java.awt.Point los = getLocationOnScreen();
        int popX = los.x;
        int popY = los.y + getHeight();
        listWindow.pack();
        int h = listWindow.getHeight();
        listWindow.setBounds(popX+5, popY-5, getWidth()-10, h);
        listWindow.setVisible(true);
    }

    /**
     * リストが選択されたときの処理.
     * @param e
     */
    @Override
    public void valueChanged(final ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || completionList.getModel().getSize() == 0) { return; }

        final String completionString = completionList.getSelectedValue();
        if (completionString == null) { return; }

        SwingUtilities.invokeLater(() -> {
            // リストが選択されたら，選択された文字を text field に挿入
            // その間，completer には止まっていてもらう必要がある
            completer.setUpdate(false);
            setText(completionString);
            completer.setUpdate(true);
        });
    }

    /**
     * フォーカスを取ったら補完ウインドウを出す.
     * @param e
     */
    @Override
    public void focusGained(FocusEvent e) {
        completer.buildAndShowPopup();
    }

    /**
     * フォーカスを失ったら補完ウインドウは消す.
     * @param e
     */
    @Override
    public void focusLost(FocusEvent e) {
        listWindow.setVisible(false);
    }

    /**
     * Enter キー入力の動作.
     * リストが選択された状態でリターン＝リストの文字をフィールドにセット.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // リストが選択されている時の enter の処理
        if (completionList.getSelectedIndex() != -1) {
            completer.setUpdate(true);
            completionList.getSelectionModel().clearSelection();
            listWindow.setVisible(false);

        } else {
            // リストが選択されていないとき
            addCompletion(getText());
        }
    }

    /**
     * キー入力を監視.
     * 上キー：選択を上へ，下キー：選択を下へ
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keyCode = e.getKeyCode();

        // リストが表示されているとき
        if (listWindow.isVisible()) {
            int size = completionListModel.getSize();
            int selection = completionList.getSelectedIndex();

            switch (keyCode) {
                case KeyEvent.VK_UP:
                    if (selection > 0) {
                        selection --;
                        completionList.getSelectionModel().setSelectionInterval(selection, selection);
                    }
                    e.consume();
                    break;

                case KeyEvent.VK_DOWN:
                    if (selection < size - 1){
                        selection ++;
                        completionList.getSelectionModel().setSelectionInterval(selection, selection);
                    } else if (selection == -1) {
                        // ウインドウが表示されていて選択されていない状態
                        selection = 0;
                        completionList.getSelectionModel().setSelectionInterval(selection, selection);
                    }
                    e.consume();
                    break;

                case KeyEvent.VK_CLEAR:
                    clearCompletions();
                    e.consume();
                    break;

                default:
                    break;
            }
        } else {
            // リストが表示されていないとき，下キーで候補を全部出す
            if (keyCode == KeyEvent.VK_DOWN && getText().length() < 1 && ! getCompletions().isEmpty()) {
                completer.showAll();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    // window が動いたら listWindow は消す
    @Override
    public void componentResized(ComponentEvent e) {
        if (parentFrame == null) {
            parentFrame = SwingUtilities.getWindowAncestor(this);
            removeComponentListener(this);
            parentFrame.addComponentListener(this);
        }
        listWindow.setVisible(false);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        listWindow.setVisible(false);
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        listWindow.setVisible(false);
    }

    /**
     * inner class does the matching of the JTextField's
     * document to completion strings kept in an ArrayList.
     */
    private class Completer implements DocumentListener {

        private boolean update = true;
        private Pattern pattern;
        private final List<String> completions;
        public Completer() {
            completions = new ArrayList<>();
        }

        public void addCompletion(String s) {
            // 新しく追加したものが最初に来る
            if (! completions.contains(s)) {
                completions.add(0, s);
            }
            // 50項目まで保存
            if (completions.size() > 50) {
                completions.remove(50);
            }

            buildAndShowPopup();
        }

        public void removeCompletion(String s) {
            completions.remove(s);
            buildAndShowPopup();
        }

        public void clearCompletions() {
            completions.clear();
            buildPopup();
            listWindow.setVisible(false);
        }

        public List<String> getCompletions() {
            return Collections.unmodifiableList(completions);
        }

        public void setUpdate(boolean b) {
            update = b;
        }

        private void buildPopup() {
            completionListModel.clear();
            //System.out.println("buildPopup for " + completions.size() + " completions");
            //pattern = Pattern.compile(getText() + ".*");
            pattern = Pattern.compile(getText() + ".+");

            completions.stream().filter(completion -> pattern.matcher(completion).matches())
                    .forEach(completion -> completionListModel.add(completionListModel.getSize(), completion));
        }

        private void showPopup() {
            if (completionListModel.getSize() == 0) {
                listWindow.setVisible(false);
                return;
            }
            showListWindow();
        }

        private void buildAndShowPopup() {
            if (! update) { return; }

            if (getText().length() < 1) {
                listWindow.setVisible(false);
                return;
            }
            buildPopup();
            showPopup();
        }

        private void showAll() {
            // complitionListModel に completions 全部入れる
            completionListModel.clear();
            completions.forEach(completion -> completionListModel.add(completionListModel.getSize(), completion));
            showPopup();
        }

        // DocumentListener implementation
        @Override
        public void insertUpdate(DocumentEvent e) {
            buildAndShowPopup();
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            buildAndShowPopup();
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            buildAndShowPopup();
        }

    }

    public static void main(String[] argv) {
        try {
            UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException e) {
            System.out.println("Dolphin.java: " + e);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        final CompletableJTextField completableField = new CompletableJTextField(75) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintBorder(Graphics g) {
                super.paintBorder(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                g2d.setColor(getBackground());
                g2d.fillRect(5, 5, getWidth()-11, getHeight()-11);
            }
        };

        // quaqua doesn't support textfield background color
        completableField.setBackground(new Color(255,255,0));

        panel.add(completableField);
        JPanel bottom = new JPanel();
        bottom.add(new JLabel("Completion:"));
        final JTextField completionField = new JTextField(40);
        completionField.addActionListener(e -> {
            completableField.addCompletion(completionField.getText());
            completionField.setText("");
        });
        bottom.add(completionField);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            completableField.addCompletion(completionField.getText());
            completionField.setText("");
        });
        bottom.add(addButton);
        panel.add(bottom);

        JFrame f = new JFrame("HACK #47: Completions...");
        //f.getContentPane().add(panel);
        //f.pack();
        f.setSize(800, 100);
        f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);

        JOptionPane jop = new JOptionPane(
                panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                new String[]{"A", "B"}

                );
        MyJSheet sheet = MyJSheet.createDialog(jop, f);
        sheet.setVisible(true);

        Preferences prefs = Preferences.userNodeForPackage(CompletableJTextField.class);
        completableField.setPreferences(prefs);
    }
}

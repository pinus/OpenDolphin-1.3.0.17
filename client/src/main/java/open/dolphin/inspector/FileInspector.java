package open.dolphin.inspector;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.ClientContext;
import open.dolphin.client.GUIConst;
import open.dolphin.event.BadgeEvent;
import open.dolphin.event.BadgeListener;
import open.dolphin.helper.ExecuteScript;
import open.dolphin.ui.MyJScrollPane;
import open.dolphin.util.StringTool;
import org.apache.log4j.Logger;

/**
 * 関連文書ファイルを表示するクラス.
 * @author pns
 */
public class FileInspector implements IInspector {
    public static final InspectorCategory CATEGORY = InspectorCategory.関連文書;

    private static final String DEFAULT_DOCUMENT_FOLDER = "/Volumes/documents/";
    private static final FileFilter FF_REGULAR = file -> !file.getName().startsWith(".");

    private JPanel filePanel;
    private JList<File> list;
    private final ChartImpl context;
    private DefaultListModel<File> model;

    private BadgeListener badgeListener;
    private int tabIndex;

    private final Logger logger;

    /**
     * MemoInspectorオブジェクトを生成する.
     * @param parent
     */
    public FileInspector(PatientInspector parent) {

        context = parent.getContext();
        logger = ClientContext.getBootLogger();
        initComponents();
    }

    /**
     * 患者 id から，関連文書の path を作る.
     * <pre>
     * 関連文書ファイル構造
     *   １万台フォルダ/千台フォルダ/関連文書
     *   000001-010000/000001-001000/${id}
     *   000001-010000/001001-002000/${id}
     *         :
     *   010001-020000/010001-011000/${id}
     *         :
     * </pre>
     * @param id
     * @return
     */
    public static String getDocumentPath(String id) {
        // id の範囲によって subfolder を決める
        int index10k = (Integer.parseInt(id) - 1) / 10000; // 上位2桁
        int index1k = (Integer.parseInt(id) - 1) / 1000; // 上位3桁

        String subfolder = String.format("%02d0001-%02d0000/%03d001-%03d000/",
                index10k, (index10k+1), index1k, (index1k+1));

        return DEFAULT_DOCUMENT_FOLDER + subfolder + id;
    }

    @Override
    public void addBadgeListener(BadgeListener listener, int index) {
        badgeListener = listener;
        tabIndex = index;
    }

    /**
     * GUI コンポーネントを初期化する.
     */
    private void initComponents() {

        model = new DefaultListModel<>();
        list = new JList<>(model);
        list.putClientProperty("Quaqua.List.style", "striped");
        list.setCellRenderer(new FileListCellRenderer());
        list.setFixedCellHeight(GUIConst.DEFAULT_LIST_ROW_HEIGHT);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new FileSelectionListener());

        filePanel = new JPanel(new BorderLayout());
        filePanel.setName(CATEGORY.name());

        filePanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, 100));

        final MyJScrollPane scrollPane = new MyJScrollPane(list);
        scrollPane.putClientProperty("JComponent.sizeVariant", "small");
        filePanel.add(scrollPane);
    }

    /**
     * PaientInspector にレイアウト用のパネルを返す.
     * @return レイアウトパネル
     */
    @Override
    public JPanel getPanel() {
        return filePanel;
    }

    @Override
    public String getName() {
        return CATEGORY.name();
    }

    @Override
    public String getTitle() {
        return CATEGORY.title();
    }

    /**
     * データのアップデート.
     */
    @Override
    public void update() {
        File infoFolder = new File (getDocumentPath(context.getKarte().getPatient().getPatientId()));
        File[] files = infoFolder.listFiles(FF_REGULAR);
        if (files == null) { return; }

        Arrays.sort(files, new FileNameComparator());
        Arrays.asList(files).forEach(file -> model.addElement(file));

        // BadgeListener に通知
        if (badgeListener != null) {
            BadgeEvent e = new BadgeEvent(this);
            e.setBadgeNumber(files.length);
            e.setTabIndex(tabIndex);
            badgeListener.badgeChanged(e);
        }
    }

    /**
     * 選択されたら，QuickLook で文書を見る.
     */
    private class FileSelectionListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            Point mousePoint = e.getPoint();
            int index = list.locationToIndex(mousePoint);
            if (index == -1) { return; }

            Point indexPoint = list.indexToLocation(index);
            // あまりマウスが離れたところをクリックしてたらクリアする
            if (indexPoint != null && Math.abs((indexPoint.y+6) - mousePoint.y) > 12) {
                list.clearSelection();
            } else {
                String path = model.getElementAt(index).getPath();
                ExecuteScript.quickLook(path);
            }
        }
    }

    private class FileListCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(
            JList list,              // the list
            Object value,            // value to display
            int index,               // cell index
            boolean isSelected,      // is the cell selected
            boolean cellHasFocus)  { // does the cell have focus

            if (isSelected) {
                this.setForeground(list.getSelectionForeground());
                this.setBackground(list.getSelectionBackground());
            } else {
                this.setForeground(list.getForeground());
                this.setBackground(list.getBackground());
            }

            String fileName = ((File)value).getName();
            this.setText(" " + fileName);
            //this.setText(" " + fileName + " " + ModelUtils.getDateAsString(new java.util.Date(((File)value).lastModified())));
            return this;
        }
    }

    /**
     * file name comparator.
     * File2, File10 などの数字付きのファイル名が正しくソートできるようにする.
     */
    private class FileNameComparator implements Comparator<File> {

        // 紹介状15.ods の '15.' を切り出すパターン
        private final Pattern NUMBER_PLUS_DOT = Pattern.compile("[0-9]+\\.");

        @Override
        public int compare(File o1, File o2) {
            // 全角数字だった場合，半角数字に変換してから比較する
            String name1 = StringTool.toHankakuNumber(o1.getName());
            String name2 = StringTool.toHankakuNumber(o2.getName());

            Matcher m1 = NUMBER_PLUS_DOT.matcher(name1);
            Matcher m2 = NUMBER_PLUS_DOT.matcher(name2);

            // 両方に '数字.' のパターンがみつかった
            if (m1.find() && m2.find()) {
                // 数字部分の文字列
                String n1 = m1.group();
                String n2 = m2.group();
                // 長い方に桁をそろえる
                int len = Math.max(n1.length(), n2.length());
                n1 = adjustDigit(n1, len);
                n2 = adjustDigit(n2, len);

                name1 = m1.replaceFirst(n1);
                name2 = m2.replaceFirst(n2);
            }

            return name2.compareTo(name1);
        }

        /**
         * 先頭に 0 をつけて，桁数を n 桁にする
         * @param str
         * @param n
         * @return
         */
        private String adjustDigit(String str, int n) {
            StringBuilder sb = new StringBuilder();
            for(int i=n; i> str.length(); i--) { sb.append('0'); }
            sb.append(str);
            return sb.toString();
        }
    }

    public static void main (String[] arg) {
        System.out.println(getDocumentPath("000125"));
    }
}

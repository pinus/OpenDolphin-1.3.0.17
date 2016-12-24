package open.dolphin.client;

import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.ActionMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import open.dolphin.delegater.StampDelegater;
import open.dolphin.helper.MenuActionManager;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.StampModel;
import open.dolphin.project.Project;
import open.dolphin.ui.MyJPopupMenu;
import open.dolphin.ui.MyJSheet;
import open.dolphin.util.HexBytesTool;
import org.jdom2.Element;
import open.dolphin.helper.MenuActionManager.MenuAction;

/**
 * StampBox の特別(gear)メニュー
 * @author pns
 * modified by masuda
 */
public class StampBoxPluginExtraMenu extends MouseAdapter {

    private MyJPopupMenu popup;
    private final StampBoxPlugin context;
    private final AbstractStampBox stampBox;
    private final BlockGlass blockGlass;
    // @MenuAction で定義された action の actionMap， key は method 名
    private ActionMap actionMap;

    public StampBoxPluginExtraMenu(StampBoxPlugin ctx) {
        super();
        context = ctx;
        stampBox = context.getUserStampBox();
        blockGlass = new BlockGlass();
        context.getFrame().setGlassPane(blockGlass);
        blockGlass.setSize(context.getFrame().getSize());
        buildPopupMenu();
    }

//    TODO
//    @MenuAction
//    public void search() {
//        System.out.println("Search Stamp");
//    }

    @MenuAction
    public void collapseAll() {
        StampTreePanel p = (StampTreePanel) stampBox.getSelectedComponent();
        StampTree tree = p.getTree();
        tree.collapseAll();
    }

    @MenuAction
    public void expandAll() {
        StampTreePanel p = (StampTreePanel) stampBox.getSelectedComponent();
        StampTree tree = p.getTree();
        tree.expandAll();
    }

    @MenuAction
    public void publishStamp() {
        context.publishStamp();
    }

    @MenuAction
    public void importStamp() {
        context.importStamp();
    }

    /**
     * ポップアップメニューを作る
     */
    private void buildPopupMenu() {
        MenuActionManager m = new MenuActionManager(this);
        actionMap = m.getActionMap();

        popup = new MyJPopupMenu();
        popup.add(m.getMenuItem("collapseAll", "フォルダを全て閉じる", GUIConst.ICON_TREE_COLLAPSED_16));
        popup.add(m.getMenuItem("expandAll", "フォルダを全て展開する", GUIConst.ICON_TREE_EXPANDED_16));
        popup.addSeparator();
        popup.add(m.getMenuItem("exportUserStampBox", "スタンプをファイルに保存する...", GUIConst.ICON_DISK_16));
        popup.add(m.getMenuItem("importUserStampBox", "スタンプをファイルから読み込む...", GUIConst.ICON_EMPTY_16));
        popup.addSeparator();
        popup.add(m.getMenuItem("publishStamp", "スタンプ公開...", GUIConst.ICON_EMPTY_16));
        popup.add(m.getMenuItem("importStamp", "公開スタンプのインポート...", GUIConst.ICON_EMPTY_16));
    }

    /**
     * publish メニューの enable/disable
     * @param b
     */
    public void setPublishEnabled(boolean b) {
        actionMap.get("publishStamp").setEnabled(b);
    }

    /**
     * import メニューの enable/disable
     * @param b
     */
    public void setImportEnabled(boolean b) {
        actionMap.get("importStamp").setEnabled(b);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // shift + click or double click で，ツリーを全て閉じる
        if (e.isShiftDown() || e.getClickCount() == 2) {
            collapseAll();
            return;
        }
        popup.show((Component) e.getSource(),e.getX(), e.getY());
    }

    /**
     * 増田内科様の ExtendedStampTreeXmlBuilder.java を組み込む
     * DefaultStampTreeXmlBuilder の必要箇所を private -> protected に変更して無理矢理 extends した
     */
    private class ExtendedStampTreeXmlBuilder extends DefaultStampTreeXmlBuilder {
        int count = 0;

        @Override
        public void buildStart() throws IOException {
            stringWriter = new StringWriter();
            writer = new BufferedWriter(stringWriter);
            writer.write(makeComment());
            writer.write("<extendedStampTree project=");
            writer.write(addQuote("open.dolphin"));
            writer.write(" version=");
            writer.write(addQuote("1.0"));
            writer.write(">\n");
        }

        private String makeComment() {
            StringBuilder sb = new StringBuilder();
            sb.append("<!-- StampBox Export Data, Creator: ");
            sb.append(Project.getUserModel().getFacilityModel().getFacilityName());
            sb.append(", Created on: ");
            sb.append(new Date().toString());
            sb.append(" -->\n");
            return sb.toString();
        }

        @Override
        protected void buildLeafNode(StampTreeNode node) throws IOException {

            StampTreeNode myParent = (StampTreeNode) node.getParent();
            StampTreeNode curNode = getCurrentNode();

            if (myParent != curNode) {
                closeBeforeMyParent(myParent);
            }

            // 特殊文字を変換する
            writer.write("<stampInfo name=");
            String val = toXmlText(node.toString());
            writer.write(addQuote(val));

            ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();

            writer.write(" role=");
            writer.write(addQuote(info.getStampRole()));

            writer.write(" entity=");
            writer.write(addQuote(info.getEntity()));

            writer.write(" editable=");
            val = String.valueOf(info.isEditable());
            writer.write(addQuote(val));

            val = info.getStampMemo();
            if (val != null) {
                writer.write(" memo=");
                val = toXmlText(val);
                writer.write(addQuote(val));
            }

            if (info.isSerialized()) {
                val = info.getStampId();
                writer.write(" stampId=");
                writer.write(addQuote(val));
                // ここで対応するstampBytesをデータベースから読み込み登録する.
                // Zombie Stamp だと Exception が発生する
                String stampHexBytes = null;
                try {
                    stampHexBytes = getHexStampBytes(val);
                } catch (RuntimeException e) {
                    //e.printStackTrace(System.err);
                    System.out.println("Zombie Stamp" + toXmlText(node.toString()));
                }
                // 実態のないスタンプの場合 null になる
                if (stampHexBytes != null) {
                    writer.write(" stampBytes=");
                    writer.write(addQuote(stampHexBytes));
                }
            }
            writer.write("/>\n");
            blockGlass.setText(String.valueOf(count++) + " 個のスタンプを保存しました");
        }

        /**
         * StampIdから対応するStampModelを取得してstampBytesのHex文字列を作成する
         */
        private String getHexStampBytes(String stampId) throws RuntimeException {
            StampDelegater del = new StampDelegater();
            // スタンプの実態を取得
            // rest 化でゾンビスタンプがあると，ここで InternalServerErrorException が発生するようになった
            StampModel model = del.getStamp(stampId);

            // stampBytesを返す
            byte[] stampBytes = ModelUtils.xmlEncode(model.getStamp());
            return HexBytesTool.bytesToHex(stampBytes);
        }

        @Override
        public void buildEnd() throws IOException {
            writer.write("</extendedStampTree>\n");
            writer.flush();
        }
    }

    /**
     * 増田内科様の ExtendedStampTreeBuilder.java を組み込む
     * DefaultStampTreeBuilder の必要箇所を private -> protected に変更して無理矢理 extends した
     */
    private class ExtendedStampTreeBuilder extends DefaultStampTreeBuilder {

        private void buildStampInfo(String name,
                String role,
                String entity,
                String editable,
                String memo,
                String id,
                String stampHexBytes) {     // stampBytesのHex文字列を追加

            // 「エディタから発行」以外は stampHexBytes がなければ無効なので無視
            if (stampHexBytes == null && !FROM_EDITOR.equals(name)) {
                System.out.println("ExtendedStampTreeBuilder: hexBytes is null: stamp '" + name + "' ignored");
                return;
            }

            buildStampInfo(name, role, entity, editable, memo, id);

            if (id != null) {
                StampDelegater sdl = new StampDelegater();

                // rest 化で，InternalServerErrorException が出るようになった
                StampModel model = null;
                try {
                    model = sdl.getStamp(id);
                } catch (RuntimeException e) {
                    System.out.println("adding stamp: " + name);
                }

                // データベースにスタンプが存在しない場合は新たに作成して登録する.
                if (model == null) {
                    model = new StampModel();
                    long userId = Project.getUserModel().getId();
//                  String stampId = GUIDGenerator.generate(model);
//                  model.setId(stampId);
                    model.setId(id); // id 再利用
                    model.setEntity(entity);
                    model.setUserId(userId);
                    byte[] stampBytes = HexBytesTool.hexToBytes(stampHexBytes);
                    IInfoModel stamp = (IInfoModel) ModelUtils.xmlDecode(stampBytes);
                    model.setStamp(stamp);
                    // 新たに作成したStampModelをデータベースに登録する
                    sdl.putStamp(model);
                    // infoのstampIdは新たに生成したものに置き換える
//                  info.setStampId(stampId);
                }
            }
        }
    }

    /**
     * 増田内科様の ExtendedStampTreeDirector.java を組み込む
     * StampTreeDirector の final を除去したり，コンストラクタを作ったり，private->protected にして無理矢理 extends した
     */
    private class ExtendedStampTreeDirector extends StampTreeDirector {
        private int count = 0;

        private ExtendedStampTreeDirector(ExtendedStampTreeBuilder builder) {
            this.builder = builder;
        }

        @Override
        public int startElement(String eName, Element e) {

            if (eName.equals("stampInfo")) {
                ((ExtendedStampTreeBuilder)builder).buildStampInfo(
                        e.getAttributeValue("name"),
                        e.getAttributeValue("role"),
                        e.getAttributeValue("entity"),
                        e.getAttributeValue("editable"),
                        e.getAttributeValue("memo"),
                        e.getAttributeValue("stampId"),
                        e.getAttributeValue("stampBytes")
                        );
                blockGlass.setText(String.valueOf(count++) + " 個のスタンプを読み込みました");
                return TT_STAMP_INFO;
            } else if (eName.equals("node")) {
                builder.buildNode(e.getAttributeValue("name"));
                return TT_NODE;
            } else if (eName.equals("root")) {
                builder.buildRoot(e.getAttributeValue("name"), e.getAttributeValue("entity"));
                return TT_ROOT;
            }
            return -1;
        }
    }


    /**
     * スタンプを xml ファイルに書き出す
     */
    @MenuAction
    public void exportUserStampBox() {

//masuda^   blockGlassを入れたりSwingWorkerを入れたり・・・
//masuda    stampBytesを含めたデータを書き出す
        ExtendedStampTreeXmlBuilder builder = new ExtendedStampTreeXmlBuilder();
        final StampTreeXmlDirector director = new StampTreeXmlDirector(builder);

//masuda    エクスポートデータ作成より前にファイル選択させる
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        MyJSheet.showSaveSheet(fileChooser, context.getFrame(), new SheetListener() {
            public void optionSelected(SheetEvent e) {
                if (e.getOption() == JFileChooser.APPROVE_OPTION) {
                    final File file = fileChooser.getSelectedFile();
                    if (!file.exists() || overwriteConfirmed(file)) {

                        SwingWorker worker = new SwingWorker<String, Void>() {

                            @Override
                            protected String doInBackground() throws Exception {
                                blockGlass.block();
                                ArrayList<StampTree> publishList = new ArrayList<StampTree>(IInfoModel.STAMP_ENTITIES.length);
                                List<StampTree> trees = stampBox.getAllTrees();
                                publishList.addAll(trees);
                                String ret = director.build(publishList);
                                return ret;
                            }

                            @Override
                            protected void done() {
                                String xml = null;
                                FileOutputStream fos = null;
                                OutputStreamWriter writer = null;

                                try {
                                    xml = get();
                                    fos = new FileOutputStream(file);
                                    writer = new OutputStreamWriter(fos, "UTF-8");
                                    // 書き出す内容
                                    writer.write(xml);
                                } catch (InterruptedException ex) {
                                    processException(ex);
                                } catch (ExecutionException ex) {
                                    processException(ex);
                                } catch (FileNotFoundException ex) {
                                    processException(ex);
                                } catch (UnsupportedEncodingException ex) {
                                    processException(ex);
                                } catch (IOException ex) {
                                    processException(ex);
                                } finally {
                                    try {
                                        writer.close();
                                        fos.close();
                                    } catch (IOException ex) {
                                        processException(ex);
                                    }
                                }
                                blockGlass.setText("");
                                blockGlass.unblock();
                            }

                            private void processException(Exception ex){
                                System.out.println("StampBoxPluginExtraMenu.java: " + ex);
                            }
                        };
                        worker.execute();
                    }
                }
            }
//masuda$
        });
    }

    /**
     * ファイル上書き確認ダイアログを表示する.
     * @param file 上書き対象ファイル
     * @return 上書きOKが指示されたらtrue
     */
    private boolean overwriteConfirmed(File file){
        String title = "上書き確認";
        String message = "既存のファイル " + file.toString() + "\n"
                        +"を上書きしようとしています。続けますか？";

        int confirm = MyJSheet.showConfirmDialog(
            context.getFrame(), message, title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE );

        if(confirm == JOptionPane.OK_OPTION) return true;

        return false;
    }

    /**
     * xml ファイルから新しい userStampBox を作る
     */
    @MenuAction
    public void importUserStampBox() {

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        MyJSheet.showOpenSheet(fileChooser, context.getFrame(), new SheetListener() {
            public void optionSelected(SheetEvent e) {
                if (e.getOption() == JFileChooser.APPROVE_OPTION) {
                    final File file = fileChooser.getSelectedFile();

                    SwingWorker worker = new SwingWorker(){

                        @Override
                        protected Object doInBackground() throws Exception {
                            blockGlass.block();
                            try {
                                // xml ファイルから StampTree 作成
                                FileInputStream in = new FileInputStream(file);
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        //masuda^   stampBytesを含めたデータを読み込む
                                ExtendedStampTreeBuilder builder = new ExtendedStampTreeBuilder();
                                ExtendedStampTreeDirector director = new ExtendedStampTreeDirector(builder);
        //masuda$
                                List<StampTree> userTrees = director.build(reader);
                                reader.close();

                                // StampTree に組み込む transfer handler を作っておく
                                StampTreeTransferHandler transferHandler = new StampTreeTransferHandler();

                                for (final StampTree stampTree : userTrees) {
                                    // ORCA は無視
                                    if (stampTree.getEntity().equals(IInfoModel.ENTITY_ORCA)) {
                                        continue;
                                    }

                                    // 読み込んだ stampTree に transfer handler などを組み込む
                                    stampTree.setUserTree(true);
                                    stampTree.setTransferHandler(transferHandler);
                                    stampTree.setStampBox(context);

                                    // ポップアップメニュー組込
                                    final StampTreePopupMenu popup = new StampTreePopupMenu(stampTree);
                                    stampTree.addMouseListener(new MouseAdapter(){
                                        @Override
                                        public void mousePressed(MouseEvent e) { showPopup(e);}
                                        @Override
                                        public void mouseReleased(MouseEvent e) { showPopup(e);}

                                        private void showPopup(MouseEvent e) {
                                            if (e.isPopupTrigger()) {
                                                popup.show(stampTree, e.getX(), e.getY());
                                            }
                                        }
                                    });

                                    // StampTreePanel 作成
                                    StampTreePanel treePanel = new StampTreePanel(stampTree);

                                    // 作った StampTreePanel を該当する tab に replace
                                    String treeName = stampTree.getTreeName();
                                    int index = stampBox.indexOfTab(treeName);
                                    if (index == -1) {
                                        // 同じ名前のタブがなければ最後に加える
                                        stampBox.add(treeName, treePanel);
                                    } else {
                                        // 同じタブがあれば，replace
                                        stampBox.setComponentAt(index, treePanel);
                                    }
                                }

                            } catch (FileNotFoundException ex) {
                                processException(ex);
                            } catch (UnsupportedEncodingException ex) {
                                processException(ex);
                            } catch (IOException ex) {
                                processException(ex);
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            blockGlass.setText("");
                            blockGlass.unblock();
                        }

                        private void processException(Exception ex) {
                            System.out.println("StampBoxPluginExtraMenu.java: " + ex);
                        }
                    };
                    worker.execute();
                }
            }
        });
    }
}

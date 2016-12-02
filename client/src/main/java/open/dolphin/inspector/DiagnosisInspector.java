package open.dolphin.inspector;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.TooManyListenersException;
import javax.swing.*;
import open.dolphin.client.*;
import open.dolphin.helper.ActionManager;
import open.dolphin.helper.ActionManager.Action;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.ui.MyBorder;
import open.dolphin.ui.MyJScrollPane;
import org.apache.log4j.Logger;

/**
 * 病名を表示するクラス
 * @author pns
 */
public class DiagnosisInspector {

    public static final String NAME = "diagnosisInspector";

    /** 呼び元の ChartImpl */
    private final ChartImpl context;
    /** PatientInspector に返すパネル */
    private JPanel diagPanel;
    /** 病名を保持するリスト */
    private JList diagList;
    /** 病名を保持するモデル <RegisteredDiagnosisModel> */
    private DefaultListModel listModel;
    /** DiagnosisDocument */
    private DiagnosisDocument doc;
    /** ListSelectionLisner 循環呼び出し lock */
    private boolean locked = false;

    private static final String SUSPECT = " 疑い";
    private final Logger logger;

    /**
     * DiagnosisInspectorオブジェクトを生成する。
     * @param context
     */
    public DiagnosisInspector(ChartImpl context) {

        this.context = context;
        logger = ClientContext.getBootLogger();
        initComponents();
    }

    /**
     * GUI コンポーネントを初期化する。
     */
    private void initComponents() {

        listModel = new DefaultListModel();
        diagList = new JList(listModel);
        diagList.setName(NAME);
        diagList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        diagList.putClientProperty("Quaqua.List.style", "striped");
        diagList.setCellRenderer(new DiagnosisListCellRenderer());
        diagList.setFixedCellHeight(GUIConst.DEFAULT_LIST_ROW_HEIGHT);
        diagList.setTransferHandler(new DiagnosisInspectorTransferHandler(context));

        // 複数選択しているとき，focus の変更で１つの項目しか repaint されないのの workaround
        diagList.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e) {
                diagList.repaint();
            }
            @Override
            public void focusLost(FocusEvent e) {
                diagList.repaint();
            }
        });

        // 右クリックでポップアップを表示する
        // ダブルクリックでエディタを立ち上げる
        if (!context.isReadOnly()) {
            diagList.addMouseListener(new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent e) {
                    Point mousePoint = e.getPoint();
                    int index = diagList.locationToIndex(mousePoint);
                    if (index == -1) {
                        // 診断が一つもないときはこっちに入る
                        // ダブルクリックで，エディタを立ち上げる
                        if (e.getClickCount() == 2) {
                            diagList.setFocusable(false);
                            doc.openEditor2();
                        }
                        return;
                    }

                    Point indexPoint = diagList.indexToLocation(index);
                    // あまりマウスが離れたところをクリックしてたらクリアする
                    if (indexPoint != null && Math.abs((indexPoint.y+6) - mousePoint.y) > 12) {
                        diagList.clearSelection();
                    }
                    // ポップアップメニュー
                    if (e.isPopupTrigger()) maybeShowPopup(e);

                    // 診断が一つでもある場合はこちらに入る
                    // ダブルクリックならエディタを立ち上げる
                    else if (e.getClickCount() == 2) {

                        // sleep を入れないと，なぜか diagList がフォーカスを横取りしてしまい，エディタがフォーカスを取れない。
                        // 同じ現象は DiagnosisDocument からエディタを立ち上げた場合にもおこるが，
                        // そちらは diagTable.setFocasable(false) を一時的に設定することで回避している
                        //try{Thread.sleep(10);} catch (InterruptedException ex){}

                        // こちらも，setFocasable で対応することにした
                        diagList.setFocusable(false);

                        int sel = diagList.getSelectedIndex();
                        if (sel < 0) {
                            // 項目のないところダブルクリックした場合
                            doc.openEditor2();
                        } else {
                            // 項目があるところをダブルクリックした場合
                            RegisteredDiagnosisModel model = (RegisteredDiagnosisModel) listModel.get(sel);
                            doc.openEditor3(model);
                        }
                    }
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) maybeShowPopup(e);
                }
                private void maybeShowPopup(MouseEvent e) {
                    if ((e.getModifiers() & InputEvent.ALT_MASK) != 0) {
                        // option キーを押していたら category：主病名，疑い病名
                        doc.getDiagnosisDocumentPopup().getCategoryPopup().show(diagList, e.getX(), e.getY());
                    } else {
                        // 右端の方が押されていたら outcome：全治，中止
                        if (e.getX() > diagList.getWidth() - 48)
                            doc.getDiagnosisDocumentPopup().getOutcomePopup().show(diagList, e.getX(), e.getY());
                        // それ以外は病名修飾
                        else doc.getDiagnosisDocumentPopup().getDiagPopup().show(diagList, e.getX(), e.getY());
                    }
                }
            });
        }

        // undo/redo のショートカットキー登録
        ActionMap map = ActionManager.getActionMap(this);
        InputMap im = diagList.getInputMap();
        ActionMap am = diagList.getActionMap();

        String func = "undo";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.META_DOWN_MASK), func);
        am.put(func, map.get(func));
        func = "redo";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.META_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), func);
        am.put(func, map.get(func));
        func = "addLeft";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_DOWN_MASK), func); // sinistro
        am.put(func, map.get(func));
        func = "addRight";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_DOWN_MASK), func); // destro
        am.put(func, map.get(func));
        func = "addBoth";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_DOWN_MASK), func); // entrambi
        am.put(func, map.get(func));
        func = "finish";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), func);
        am.put(func, map.get(func));
        func = "discontinue";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), func);
        am.put(func, map.get(func));
        func = "renew";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), func);
        am.put(func, map.get(func));
        func = "dropPrepos";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), func); // togliere
        am.put(func, map.get(func));
        func = "delete";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), func);
        am.put(func, map.get(func));
        func = "sendClaim";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.META_DOWN_MASK), func);
        am.put(func, map.get(func));
        func = "duplicate";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.META_DOWN_MASK), func);
        am.put(func, map.get(func));
        func = "suspected";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0), func);
        am.put(func, map.get(func));
        func = "mainDiag";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.SHIFT_DOWN_MASK), func);
        am.put(func, map.get(func));

        // GUI 形成
        diagPanel = new DropPanel(new BorderLayout());

        MyJScrollPane scrollPane = new MyJScrollPane(diagList);
        scrollPane.putClientProperty("JComponent.sizeVariant", "small");
        diagPanel.add(scrollPane);

        // ChartImpl#getDiagnosisDocument() を呼ぶと，ChartImpl から DiagnosisDocument#start() が呼ばれて
        // DiagnosisDocument でサーバーから診断リストを読み込んで，それが読み終わると
        // DiagnosisDocument からここの update(model) が呼ばれて diagList がセットされるという紆余曲折な仕組み
        Thread t = new Thread(){
            @Override
            public void run() {
                // ここで初めて DiagnosisDocument の実体が現れる
                doc = context.getDiagnosisDocument();

                // DiagnosisInspector の list と DiagnosisDocument の table の選択範囲を一致させる
                DiagnosisDocumentTable table = doc.getDiagnosisTable();
                DiagnosisDocumentTableModel model = (DiagnosisDocumentTableModel) table.getModel();
                ListSelectionModel selectionModel = table.getSelectionModel();

                diagList.addListSelectionListener(e -> {
                    if (locked) { return; }
                    locked = true;

                    selectionModel.clearSelection();

                    diagList.getSelectedValuesList().forEach(o -> {
                        for(int i=0; i<model.getObjectCount(); i++) {
                            if (model.getObject(i).equals(o)) {
                                int row = table.convertRowIndexToView(i);
                                selectionModel.addSelectionInterval(row,row);
                            }
                        }
                    });
                    locked = false;
                });
                selectionModel.addListSelectionListener(e -> {
                    if (locked) { return; }
                    locked = true;

                    diagList.clearSelection();

                    int[] rows = table.getSelectedRows();
                    for (int view : rows) {
                        int row = table.convertRowIndexToModel(view);
                        for (int i=0; i<diagList.getModel().getSize(); i++) {
                            if (diagList.getModel().getElementAt(i).equals(model.getObject(row))) {
                                diagList.addSelectionInterval(i, i);
                            }
                        }
                    }
                    locked = false;
                });
            }
        };
        t.start();
    }

    /**
     * PaientInspector にレイアウト用のパネルを返す。
     * @return レイアウトパネル
     */
    public JPanel getPanel() {
        return diagPanel;
    }

    public JList getList() {
        return diagList;
    }

    /**
     * DiagnosisDocument で，diagList を focasable にしてもらう
     * @param b
     */
    public void setFocasable(boolean b) {
        diagList.setFocusable(b);
    }

    /**
     * データのアップデート
     * @param model
     */
    public void update(DiagnosisDocumentTableModel model) {
        // model から，endDate の有無でリストを分ける
        ArrayList active = new ArrayList();
        ArrayList ended = new ArrayList();

        model.getObjectList().forEach(o -> {
            RegisteredDiagnosisModel rd = (RegisteredDiagnosisModel)o;
            if (rd.getEndDate() == null) active.add(o);
            else ended.add(o);
        });
        // 選択を保存　hashCode を保存しておく
        ArrayList<Integer> selected = new ArrayList<>();
        for (int r : diagList.getSelectedIndices()) {
            selected.add(System.identityHashCode(listModel.get(r)));
        }
        // listModel にセット
        listModel.clear();
        for (int i=0; i < active.size(); i++) listModel.addElement(active.get(i));
        for (int i=0; i < ended.size(); i++) listModel.addElement(ended.get(i));

        // 選択を復元　hashCode で同じオブジェクトを判定
        selected.forEach(h -> {
            for(int i=0; i<listModel.getSize(); i++) {
                if (h == System.identityHashCode(listModel.get(i))) {
                    diagList.addSelectionInterval(i, i);
                }
            }
        });
    }

    private class DiagnosisListCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(
            JList list,              // the diagList
            Object value,            // value to display
            int index,               // cell index
            boolean isSelected,      // is the cell selected
            boolean isFocused)  { // does the cell have focus

            RegisteredDiagnosisModel rd = (RegisteredDiagnosisModel) value;
            String diagName = rd.getDiagnosis();
            // 疑いの場合
            if (DiagnosisDocument.SUSPECTED_DIAGNOSIS.equals(rd.getCategoryDesc())) {
                diagName += SUSPECT;
            }

            boolean deleted = DiagnosisDocument.DELETED_RECORD.equals(rd.getStatus());
            boolean ended = rd.getEndDate() != null;
            boolean ikou = DiagnosisDocument.IKOU_BYOMEI_RECORD.equals(rd.getStatus());

            if (isSelected) {
                // foreground
                if (deleted || ended) {
                    int rgb = list.getSelectionForeground().getRGB();
                    int adjust = 0x2f2f2f;
                    if ((rgb & 0x00ffffff) > adjust) rgb -= adjust;
                    if ((rgb & 0x00ffffff) < adjust) rgb += adjust;
                    setForeground(new Color(rgb));
                } else if (ikou) {
                    setForeground(DiagnosisDocument.IKOU_BYOMEI_COLOR);
                } else {
                    setForeground(list.getSelectionForeground());
                }
                // background
                setBackground(list.getSelectionBackground());
            } else {
                // foreground
                if (deleted) setForeground(DiagnosisDocument.DELETED_COLOR);
                else if (ended) setForeground(DiagnosisDocument.ENDED_COLOR);
                else if (ikou) setForeground(DiagnosisDocument.IKOU_BYOMEI_COLOR);
                else setForeground(list.getForeground());
                // background
                setBackground(list.getBackground());
            }

            this.setText(" " + diagName);

            return this;
        }

        // 罫線を入れる
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g.setColor(Color.WHITE);
            // Retina 対応
            //g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
            g.drawLine(0, getHeight(), getWidth(), getHeight());
            g.dispose();
        }
    }

    /**
     * ドロップするとき，Border にフィードバックを出すパネル
     */
    private class DropPanel extends JPanel {
        private boolean showFeedback = false;

        public DropPanel(LayoutManager layout) {
            super(layout);

            try {
                DropTarget dt = diagList.getDropTarget();
                dt.addDropTargetListener(new DropTargetListener() {
                    @Override
                    public void dragEnter(DropTargetDragEvent dtde) {
                        // ALT キーを押していると「疑い」が入力される
                        doc.action = dtde.getDropAction();
                        showFeedback = true;
                        repaint();
                    }

                    @Override
                    public void dropActionChanged(DropTargetDragEvent dtde) {
                        doc.action = dtde.getDropAction();
                    }

                    @Override
                    public void drop(DropTargetDropEvent dtde) {
                        showFeedback = false;
                        repaint();
                    }
                    @Override
                    public void dragOver(DropTargetDragEvent dtde) {}
                    @Override
                    public void dragExit(DropTargetEvent dte) {
                        showFeedback = false;
                        repaint();
                    }
                });
                dt.setActive(true);
            } catch (TooManyListenersException e) {}
        }

        @Override
        public void paintBorder(Graphics graphics) {
            super.paintBorder(graphics);
            if (showFeedback) {
                Graphics2D g = (Graphics2D) graphics.create();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // cut and try
                int x = 4;
                int y = 16;
                int width = getWidth() - 2*x;
                int height = getHeight() - y - 3;
                MyBorder.drawSelectedBlueRoundRect(this, g, x, y, width, height,10,10);
                g.dispose();
            }
        }
    }

    @Action
    public void undo() {
        doc.undo();
    }
    @Action
    public void redo() {
        doc.redo();
    }
    @Action
    public void addLeft() {
        doc.getDiagnosisDocumentPopup().doClickDiagPopup("左");
    }
    @Action
    public void addRight() {
        doc.getDiagnosisDocumentPopup().doClickDiagPopup("右");
    }
    @Action
    public void addBoth() {
        doc.getDiagnosisDocumentPopup().doClickDiagPopup("両");
    }
    @Action
    public void finish() {
        doc.getDiagnosisDocumentPopup().doClickOutcomePopup("全治");
    }
    @Action
    public void discontinue() {
        doc.getDiagnosisDocumentPopup().doClickOutcomePopup("中止");
    }
    @Action
    public void renew() {
        doc.getDiagnosisDocumentPopup().doClickOutcomePopup("");
    }
    @Action
    public void delete() {
        doc.delete();
    }
    @Action
    public void sendClaim() {
        doc.sendClaim();
    }
    @Action
    public void duplicate(){
        doc.duplicateDiagnosis();
    }
    @Action
    public void dropPrepos(){
        doc.getDiagnosisDocumentPopup().dropPreposition();
    }
    @Action
    public void suspected() {
        doc.getDiagnosisDocumentPopup().doClickCategoryPopup("疑い病名");
    }
    @Action
    public void mainDiag() {
        doc.getDiagnosisDocumentPopup().doClickCategoryPopup("主病名");
    }
}

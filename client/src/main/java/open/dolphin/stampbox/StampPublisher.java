package open.dolphin.stampbox;

import open.dolphin.client.ClientContext;
import open.dolphin.client.Dolphin;
import open.dolphin.client.GUIConst;
import open.dolphin.client.GUIFactory;
import open.dolphin.delegater.StampDelegater;
import open.dolphin.event.ProxyDocumentListener;
import open.dolphin.helper.GridBagBuilder;
import open.dolphin.helper.StampTreeUtils;
import open.dolphin.helper.PNSTask;
import open.dolphin.helper.WindowSupport;
import open.dolphin.infomodel.*;
import open.dolphin.project.Project;
import open.dolphin.ui.PNSOptionPane;
import open.dolphin.util.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

/**
 * StampTreePublisher.
 *
 * @author Kazushi, Minagawa
 */
public class StampPublisher {
    private final StampBoxPlugin stampBox;
    private final String title = "スタンプ公開";
    private final Logger logger;
    private WindowSupport<StampPublisher> windowSupport;
    private JLabel infoLabel;
    private JLabel instLabel;
    private JLabel publishedDate;
    private JTextField stampBoxName;
    private JTextField partyName;
    private JTextField contact;
    private JTextField description;
    private JRadioButton local;
    private JRadioButton global;
    private JButton publish;
    private JButton cancel;
    private JButton cancelPublish;
    private JCheckBox[] entities;
    private JComboBox<String> category;
    private PublishType publishType = PublishType.TT_NONE;
    private boolean okState;
    private StampDelegater sdl;
    private PublishedState publishState;

    public StampPublisher(StampBoxPlugin stampBox) {
        this.stampBox = stampBox;
        logger = LoggerFactory.getLogger(StampPublisher.class);
    }

    public void start() {
        windowSupport = new WindowSupport<>(ClientContext.getFrameTitle(title), this);
        JFrame dialog = windowSupport.getFrame();
        dialog.getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);
        dialog.setIconImage(GUIConst.ICON_DOLPHIN.getImage());

        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });

        JPanel contentPane = createContentPane();
        contentPane.setOpaque(true);
        dialog.setContentPane(contentPane);
        windowSupport.toCenter();

        stampBox.getFrame().getGlassPane().setVisible(true);
        dialog.setVisible(true);
    }

    public void stop() {
        windowSupport.dispose();
        stampBox.getFrame().getGlassPane().setVisible(false);
    }

    private JPanel createContentPane() {

        JPanel contentPane = new JPanel();

        // GUIコンポーネントを生成する
        infoLabel = new JLabel(GUIConst.ICON_INFORMATION_16);
        instLabel = new JLabel("");
        instLabel.setFont(new Font("Dialog", Font.PLAIN, ClientContext.getInt("waitingList.state.font.size")));
        publishedDate = new JLabel("");

        stampBoxName = GUIFactory.createTextField(15, null, null, null);
        partyName = GUIFactory.createTextField(20, null, null, null);
        contact = GUIFactory.createTextField(30, null, null, null);
        description = GUIFactory.createTextField(30, null, null, null);
        local = new JRadioButton(IInfoModel.PUBLISH_TREE_LOCAL);
        global = new JRadioButton(IInfoModel.PUBLISH_TREE_PUBLIC);
        publish = new JButton("");
        publish.setEnabled(false);
        cancelPublish = new JButton("公開を止める");
        cancelPublish.setEnabled(false);
        cancel = new JButton("ダイアログを閉じる");

        entities = new JCheckBox[IInfoModel.STAMP_NAMES.length];
        for (int i = 0; i < IInfoModel.STAMP_NAMES.length; i++) {
            entities[i] = new JCheckBox(IInfoModel.STAMP_NAMES[i]);
            if (IInfoModel.STAMP_NAMES[i].equals(IInfoModel.TABNAME_ORCA)) {
                entities[i].setEnabled(false);
            }
        }
        JPanel chkPanel1 = GUIFactory.createCheckBoxPanel(new JCheckBox[]{entities[0], entities[1], entities[2], entities[3], entities[4], entities[5], entities[6], entities[7]});
        JPanel chkPanel2 = GUIFactory.createCheckBoxPanel(new JCheckBox[]{entities[8], entities[9], entities[10], entities[11], entities[12], entities[13], entities[14], entities[15]});

        String[] categories = ClientContext.getStringArray("stamp.publish.categories");
        //医薬品,ジェネリック医薬品,検査,器材,パス,地域連携,調査,治験,院内シェア,スタンプ大作戦
        category = new JComboBox<>(categories);
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        categoryPanel.add(category);

        // 公開先RadioButtonパネル
        JPanel radioPanel = GUIFactory.createRadioPanel(new JRadioButton[]{local, global});

        // 属性設定パネル
        GridBagBuilder gbl = new GridBagBuilder("スタンプ公開設定");

        int y = 0;
        gbl.add(infoLabel, 0, y, GridBagConstraints.EAST);
        gbl.add(instLabel, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("公開スタンプセット名"), 0, y, GridBagConstraints.EAST);
        gbl.add(stampBoxName, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("公開先"), 0, y, GridBagConstraints.EAST);
        gbl.add(radioPanel, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("カテゴリ"), 0, y, GridBagConstraints.EAST);
        gbl.add(categoryPanel, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("公開するスタンプ"), 0, y, GridBagConstraints.EAST);
        gbl.add(chkPanel1, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel(" "), 0, y, GridBagConstraints.EAST);
        gbl.add(chkPanel2, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("公開者名"), 0, y, GridBagConstraints.EAST);
        gbl.add(partyName, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("URL等"), 0, y, GridBagConstraints.EAST);
        gbl.add(contact, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("利用者への説明"), 0, y, GridBagConstraints.EAST);
        gbl.add(description, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("公開日"), 0, y, GridBagConstraints.EAST);
        gbl.add(publishedDate, 1, y, GridBagConstraints.WEST);

        // コマンドパネル
        JPanel cmdPanel;
        if (Dolphin.forWin) {
            cmdPanel = GUIFactory.createCommandButtonPanel(new JButton[]{publish, cancelPublish, cancel});
        } else {
            cmdPanel = GUIFactory.createCommandButtonPanel(new JButton[]{cancel, cancelPublish, publish});
        }

        // 配置する
        contentPane.setLayout(new BorderLayout(0, 17));
        contentPane.add(gbl.getProduct(), BorderLayout.CENTER);
        contentPane.add(cmdPanel, BorderLayout.SOUTH);
        contentPane.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));

        // PublishState に応じて振り分ける
        StampTreeBean stampTree = stampBox.getUserStampBox().getStampTreeModel();
        FacilityModel facility = Project.getUserModel().getFacilityModel();
        String facilityId = facility.getFacilityId();
        long treeId = stampTree.getId();
        String publishTypeStr = stampTree.getPublishType();

        if (treeId == 0L && publishTypeStr == null) {
            // StampTree非保存（最初のログイン時）
            publishState = PublishedState.NONE;

        } else if (treeId != 0L && publishTypeStr == null) {
            // 保存されているStampTreeで非公開のケース
            publishState = PublishedState.SAVED_NONE;

        } else if (treeId != 0L && publishTypeStr.equals(facilityId)) {
            // publishType=facilityId ローカルに公開されている
            publishState = PublishedState.LOCAL;

        } else if (treeId != 0L && publishTypeStr.equals(IInfoModel.PUBLISHED_TYPE_GLOBAL)) {
            // publishType=global グローバルに公開されている
            publishState = PublishedState.GLOBAL;
        }

        // GUIコンポーネントに初期値を入力する
        switch (publishState) {
            case NONE, SAVED_NONE -> {
                String dateStr = ModelUtils.getDateAsString(new Date());
                String url = facility.getUrl();
                instLabel.setText("このスタンプは公開されていません。");
                partyName.setText(facility.getFacilityName());
                if (url != null) { contact.setText(url); }
                publishedDate.setText(dateStr);
                publish.setText("公開する");
            }
            case LOCAL -> {
                instLabel.setText("このスタンプは院内に公開されています。");
                stampBoxName.setText(stampTree.getName());
                local.setSelected(true);
                global.setSelected(false);
                publishType = PublishType.TT_LOCAL;

                //
                // Publish している Entity をチェックする
                //
                String published = ((PersonalTreeModel) stampTree).getPublished();
                if (published != null) {
                    StringTokenizer st = new StringTokenizer(published, ",");
                    while (st.hasMoreTokens()) {
                        String entity = st.nextToken();
                        for (int i = 0; i < IInfoModel.STAMP_ENTITIES.length; i++) {
                            if (entity.equals(IInfoModel.STAMP_ENTITIES[i])) {
                                entities[i].setSelected(true);
                                break;
                            }
                        }
                    }
                }

                category.setSelectedItem(stampTree.getCategory());
                partyName.setText(stampTree.getPartyName());
                contact.setText(stampTree.getUrl());
                description.setText(stampTree.getDescription());
                String timeStamp = ModelUtils.getDateAsString(stampTree.getPublishedDate()) +
                    "  最終更新日( " +
                    ModelUtils.getDateAsString(stampTree.getLastUpdated()) +
                    " )";
                publishedDate.setText(timeStamp);
                publish.setText("更新する");
                publish.setEnabled(true);
                cancelPublish.setEnabled(true);
            }
            case GLOBAL -> {
                instLabel.setText("このスタンプはグローバルに公開されています。");
                stampBoxName.setText(stampTree.getName());
                local.setSelected(false);
                global.setSelected(true);
                category.setSelectedItem(stampTree.getCategory());
                partyName.setText(stampTree.getPartyName());
                contact.setText(stampTree.getUrl());
                description.setText(stampTree.getDescription());
                publishType = PublishType.TT_PUBLIC;

                String published = ((PersonalTreeModel) stampTree).getPublished();
                if (published != null) {
                    StringTokenizer st = new StringTokenizer(published, ",");
                    while (st.hasMoreTokens()) {
                        String entity = st.nextToken();
                        for (int i = 0; i < IInfoModel.STAMP_ENTITIES.length; i++) {
                            if (entity.equals(IInfoModel.STAMP_ENTITIES[i])) {
                                entities[i].setSelected(true);
                                break;
                            }
                        }
                    }
                }

                String timeStamp = ModelUtils.getDateAsString(stampTree.getPublishedDate()) +
                    "  最終更新日( " +
                    ModelUtils.getDateAsString(stampTree.getLastUpdated()) +
                    " )";
                publishedDate.setText(timeStamp);
                publish.setText("更新する");
                publish.setEnabled(true);
                cancelPublish.setEnabled(true);
            }
        }

        // コンポーネントのイベント接続を行う
        // Text入力をチェックする
        ProxyDocumentListener dl = e -> checkButton();
        stampBoxName.getDocument().addDocumentListener(dl);
        partyName.getDocument().addDocumentListener(dl);
        contact.getDocument().addDocumentListener(dl);
        description.getDocument().addDocumentListener(dl);

        // RadioButton
        ButtonGroup bg = new ButtonGroup();
        bg.add(local);
        bg.add(global);
        PublishTypeListener pl = new PublishTypeListener();
        local.addActionListener(pl);
        global.addActionListener(pl);

        // CheckBox listener
        ActionListener cbListener = this::checkCheckBox;
        for (JCheckBox cb : entities) {
            cb.addActionListener(cbListener);
        }

        // publish & cancel
        publish.addActionListener(e -> publish());
        cancelPublish.addActionListener(e -> cancelPublish());
        cancel.addActionListener(e -> stop());

        return contentPane;
    }

    /**
     * スタンプを公開する.
     */
    public void publish() {

        // 公開するStampTreeを取得する
        List<StampTree> publishList = new ArrayList<>();

        // Entity のカンマ連結用 StringBuilder
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < IInfoModel.STAMP_ENTITIES.length; i++) {

            if (entities[i].isSelected()) {
                // Entity チェックボックスがチェックされている時
                // 対応するEntity名を取得する
                String entity = IInfoModel.STAMP_ENTITIES[i];

                // StampBox からEntityに対応するStampTreeを得る
                StampTree st = stampBox.getStampTreeFromUserBox(entity);

                // 公開リストに加える
                publishList.add(st);

                // Entity 名をカンマで連結する
                sb.append(",");
                sb.append(entity);
            }
        }
        String published = sb.toString();
        published = published.substring(1); // 頭の , を取る

        // 公開する StampTree の XML データを生成する
        final String publishXml = StampTreeUtils.xmlEncode(publishList);

        // 公開時の自分（個人用）の StampTree と同期をとる
        List<StampTree> trees = stampBox.getUserStampBox().getAllTrees();
        String personalXml = StampTreeUtils.xmlEncode(trees);

        // 個人用の PersonalTreeModel に公開時点の XML をセットする
        final PersonalTreeModel personalTree = (PersonalTreeModel) stampBox.getUserStampBox().getStampTreeModel();
        personalTree.setTreeXml(personalXml);

        // 公開情報を設定する
        personalTree.setName(stampBoxName.getText().trim());
        String pubType = global.isSelected() ? IInfoModel.PUBLISHED_TYPE_GLOBAL : Project.getUserModel().getFacilityModel().getFacilityId();
        personalTree.setPublishType(pubType);
        personalTree.setCategory((String) category.getSelectedItem());
        personalTree.setPartyName(partyName.getText().trim());
        personalTree.setUrl(contact.getText().trim());
        personalTree.setDescription(description.getText().trim());
        personalTree.setPublished(published);

        // 公開及び更新日を設定する
        Date now = new Date();
        if (publishState == PublishedState.NONE || publishState == PublishedState.SAVED_NONE) {
            personalTree.setPublishedDate(now);
        }
        personalTree.setLastUpdated(now);

        // Delegator を生成する
        sdl = new StampDelegater();

        int delay = 200;
        int maxEstimation = 30 * 1000;

        String message = "スタンプ公開";
        String note = "公開しています...";
        Component c = windowSupport.getFrame();

        PNSTask<Boolean> task = new PNSTask<>(c, message, note, maxEstimation) {
            @Override
            protected Boolean doInBackground() {

                // 現時点の personal tree を永続化
                sdl.putTree(personalTree);

                // その後，published tree を永続化
                PublishedTreeModel publishedTree = StampTreeUtils.createPublishedTreeModel(personalTree, publishXml);
                sdl.publishTree(publishedTree);

                return sdl.isNoError();
            }

            @Override
            protected void succeeded(Boolean succeeded) {
                logger.debug("Task succeeded");

                if (succeeded) {
                    PNSOptionPane.showMessageDialog(windowSupport.getFrame(),
                            "スタンプを公開しました。",
                            ClientContext.getFrameTitle(title),
                            JOptionPane.INFORMATION_MESSAGE);
                    stop();

                } else {
                    PNSOptionPane.showMessageDialog(windowSupport.getFrame(),
                            sdl.getErrorMessage(),
                            ClientContext.getFrameTitle(title),
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        };
        //task.setMillisToPopup(delay);
        task.execute();
    }

    /**
     * 公開しているTreeを取り消す.
     */
    public void cancelPublish() {

        // 確認を行う
        JLabel msg1 = new JLabel("公開を取り消すとサブスクライブしているユーザがあなたの");
        JLabel msg2 = new JLabel("スタンプを使用できなくなります。公開を取り消しますか?");
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        p1.add(msg1);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        p2.add(msg2);
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.add(p1);
        box.add(p2);
        box.setBorder(BorderFactory.createEmptyBorder(0, 0, 11, 11));

        int option = PNSOptionPane.showConfirmDialog(windowSupport.getFrame(),
                new Object[]{box},
                ClientContext.getFrameTitle(title),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null);

        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        // StampTree を表す XML データを生成する
        List<StampTree> list = stampBox.getUserStampBox().getAllTrees();
        String treeXml = StampTreeUtils.xmlEncode(list);

        // 個人用の PersonalTreeModel にXMLをセットする
        final PersonalTreeModel stampTree = (PersonalTreeModel) stampBox.getUserStampBox().getStampTreeModel();

        // 公開データをクリアする
        stampTree.setTreeXml(treeXml);
        stampTree.setPublishType(null);
        stampTree.setPublishedDate(null);
        stampTree.setLastUpdated(null);
        stampTree.setCategory(null);
        stampTree.setName(ClientContext.getString("stampTree.personal.box.name"));
        stampTree.setDescription(ClientContext.getString("stampTree.personal.box.tooltip"));

        sdl = new StampDelegater();

        int delay = 200;
        int maxEstimation = 60 * 1000;

        String message = "スタンプ公開";
        String note = "公開を取り消しています...";
        Component c = windowSupport.getFrame();

        PNSTask<Boolean> task = new PNSTask<>(c, message, note, maxEstimation) {
            @Override
            protected Boolean doInBackground() {
                sdl.cancelPublishedTree(stampTree);
                return sdl.isNoError();
            }

            @Override
            protected void succeeded(Boolean succeeded) {
                logger.debug("Task succeeded");

                if (succeeded) {
                    PNSOptionPane.showMessageDialog(windowSupport.getFrame(),
                            "公開を取り消しました。",
                            ClientContext.getFrameTitle(title),
                            JOptionPane.INFORMATION_MESSAGE);
                    stop();

                } else {
                    PNSOptionPane.showMessageDialog(windowSupport.getFrame(),
                            sdl.getErrorMessage(),
                            ClientContext.getFrameTitle(title),
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        };
        // task.setMillisToPopup(delay);
        task.execute();
    }

    /**
     * META クリックで CheckBox 全部の ON/OFF
     *
     * @param e ActionEvent
     */
    private void checkCheckBox(ActionEvent e) {
        if ((e.getModifiers() & ActionEvent.META_MASK) != 0) {
            JCheckBox cb = (JCheckBox) e.getSource();
            for (int i = 0; i < IInfoModel.STAMP_NAMES.length; i++) {
                if (!IInfoModel.STAMP_NAMES[i].equals(IInfoModel.TABNAME_ORCA)) {
                    entities[i].setSelected(cb.isSelected());
                }
            }
        }
        checkButton();
    }

    private void checkButton() {
        boolean stampNameOk = !stampBoxName.getText().isBlank();
        boolean partyNameOk = !partyName.getText().isBlank();
        boolean descriptionOk = ! description.getText().isBlank();
        boolean checkOk = Arrays.stream(entities).anyMatch(JCheckBox::isSelected);

        switch (publishType) {
            case TT_LOCAL -> {
                boolean newOk = stampNameOk && partyNameOk && descriptionOk && checkOk;
                if (newOk != okState) {
                    okState = newOk;
                    publish.setEnabled(okState);
                }
            }
            case TT_PUBLIC -> {
                boolean urlOk = !contact.getText().isBlank();
                boolean newOk = stampNameOk && partyNameOk && descriptionOk && checkOk && urlOk;
                if (newOk != okState) {
                    okState = newOk;
                    publish.setEnabled(okState);
                }
            }
        }
    }

    private enum PublishedState { NONE, SAVED_NONE, LOCAL, GLOBAL }

    private enum PublishType { TT_NONE, TT_LOCAL, TT_PUBLIC }

    private class PublishTypeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (local.isSelected()) {
                publishType = PublishType.TT_LOCAL;
                category.setSelectedIndex(ClientContext.getInt("stamp.publish.categories.localItem")); //8
            } else if (global.isSelected()) {
                publishType = PublishType.TT_PUBLIC;
            }
            checkButton();
        }
    }
}

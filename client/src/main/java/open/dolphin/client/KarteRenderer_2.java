package open.dolphin.client;

import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.ProgressCourse;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.swing.text.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.*;

/**
 * KarteRenderer_2.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class KarteRenderer_2 {

    private static final String COMPONENT_ELEMENT_NAME = "component";

    private static final String STAMP_HOLDER = "stampHolder";

    private static final String SCHEMA_HOLDER = "schemaHolder";

    private static final int TT_SECTION = 0;

    private static final int TT_PARAGRAPH = 1;

    private static final int TT_CONTENT = 2;

    private static final int TT_ICON = 3;

    private static final int TT_COMPONENT = 4;

    private static final int TT_PROGRESS_COURSE = 5;

    private static final String SECTION_NAME = "section";

    private static final String PARAGRAPH_NAME = "paragraph";

    private static final String CONTENT_NAME = "content";

    private static final String COMPONENT_NAME = "component";

    private static final String ICON_NAME = "icon";

    private static final String ALIGNMENT_NAME = "Alignment";

    private static final String FOREGROUND_NAME = "foreground";

    private static final String SIZE_NAME = "size";

    private static final String BOLD_NAME = "bold";

    private static final String ITALIC_NAME = "italic";

    private static final String UNDERLINE_NAME = "underline";

    private static final String TEXT_NAME = "text";

    private static final String NAME_NAME = "name";

    private static final String LOGICAL_STYLE_NAME = "logicalStyle";

    private static final String PROGRESS_COURSE_NAME = "kartePane";

    private static final String[] REPLACES = new String[]{"<", ">", "&", "'", "\""};

    private static final String[] MATCHES = new String[]{"&lt;", "&gt;", "&amp;", "&apos;", "&quot;"};

    private static final String NAME_STAMP_HOLDER = "name=\"stampHolder\"";
    private final KartePane soaPane;
    private final KartePane pPane;
    private final Logger logger;
    private DocumentModel model;
    private KartePane thePane;
    private boolean logicalStyle;
    private boolean bSoaPane;
    private List<ModuleModel> soaModules;
    private List<ModuleModel> pModules;

    public KarteRenderer_2(KartePane soaPane, KartePane pPane) {
        this.soaPane = soaPane;
        this.pPane = pPane;
        logger = Logger.getLogger(KarteRenderer_2.class);
    }

    /**
     * DocumentModel をレンダリングする.
     *
     * @param model レンダリングする DocumentModel
     */
    public void render(DocumentModel model) {

        this.model = model;

        Collection<ModuleModel> modules = model.getModules();

        // SOA と P のモジュールをわける
        // また夫々の Pane の spec を取得する
        soaModules = new ArrayList<>();
        pModules = new ArrayList<>();
        String soaSpec = null;
        String pSpec = null;

        for (ModuleModel bean : modules) {

            String role = bean.getModuleInfo().getStampRole();

            switch (role) {
                case IInfoModel.ROLE_SOA:
                    soaModules.add(bean);
                    break;
                case IInfoModel.ROLE_SOA_SPEC:
                    soaSpec = ((ProgressCourse) bean.getModel()).getFreeText();
                    break;
                case IInfoModel.ROLE_P:
                    pModules.add(bean);
                    break;
                case IInfoModel.ROLE_P_SPEC:
                    pSpec = ((ProgressCourse) bean.getModel()).getFreeText();
                    break;
            }
        }

        if (soaSpec != null && pSpec != null) {

            int index = soaSpec.indexOf(NAME_STAMP_HOLDER);
            if (index > 0) {
                String sTmp = soaSpec;
                String pTmp = pSpec;
                soaSpec = pTmp;
                pSpec = sTmp;
            }
        }

        // SOA Pane をレンダリングする
        if (soaSpec == null || soaSpec.equals("")) {
            soaModules.forEach(moduleModel -> {
                soaPane.stamp(moduleModel);
                soaPane.makeParagraph();
            });
            // モジュールのみの場合も dirty にセット
            if (soaModules != null) {
                soaPane.setDirty(true);
            }

        } else {
            debug("Render SOA Pane");
            debug("Module count = " + soaModules.size());
            bSoaPane = true;
            thePane = soaPane;
            renderPane(soaSpec);
        }

        // P Pane をレンダリングする
        if (pSpec == null || pSpec.equals("")) {
            // 前回処方適用のようにモジュールのみの場合
            pModules.forEach(moduleModel -> {
                //pPane.stamp(mm);
                pPane.flowStamp(moduleModel);
                pPane.makeParagraph();
                pPane.makeParagraph();
            });
            // モジュールだけの場合も dirty にセット
            if (pModules != null && pPane != null) {
                pPane.setDirty(true);
            }

        } else {
            bSoaPane = false;
            thePane = pPane;
            renderPane(pSpec);

            // StampHolder直後の改行がない場合は補う
            pPane.getDocument().fixCrAfterStamp();
        }
    }

    /**
     * TextPane Dump の XML を解析する.
     *
     * @param xml TextPane Dump の XML
     */
    private void renderPane(String xml) {

        debug(xml);

        SAXBuilder docBuilder = new SAXBuilder();
        StringReader sr = new StringReader(xml);

        try (BufferedReader br = new BufferedReader(sr)) {
            Document doc = docBuilder.build(br);
            Element root = doc.getRootElement();

            writeChildren(root);
        }
        // indicates a well-formedness error
        catch (JDOMException | IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 子要素をパースする.
     *
     * @param current 要素
     */
    private void writeChildren(Element current) {

        int eType = -1;
        String eName = current.getName();

        if (eName.equals(PARAGRAPH_NAME)) {
            eType = TT_PARAGRAPH;
            startParagraph(current.getAttributeValue(LOGICAL_STYLE_NAME),
                    current.getAttributeValue(ALIGNMENT_NAME));

        } else if (eName.equals(CONTENT_NAME) && (current.getChild(TEXT_NAME) != null)) {
            eType = TT_CONTENT;
            startContent(current.getAttributeValue(FOREGROUND_NAME),
                    current.getAttributeValue(SIZE_NAME),
                    current.getAttributeValue(BOLD_NAME),
                    current.getAttributeValue(ITALIC_NAME),
                    current.getAttributeValue(UNDERLINE_NAME),
                    current.getChildText(TEXT_NAME));

        } else if (eName.equals(COMPONENT_NAME)) {
            eType = TT_COMPONENT;
            startComponent(current.getAttributeValue(NAME_NAME), // compoenet=number
                    current.getAttributeValue(COMPONENT_ELEMENT_NAME));

        } else if (eName.equals(ICON_NAME)) {
            eType = TT_ICON;
            startIcon(current);

        } else if (eName.equals(PROGRESS_COURSE_NAME)) {
            eType = TT_PROGRESS_COURSE;
            startProgressCourse();

        } else if (eName.equals(SECTION_NAME)) {
            eType = TT_SECTION;
            startSection();

        } else {
            debug("Other element:" + eName);
        }

        // 子を探索するのはパラグフとトップ要素のみ
        if (eType == TT_PARAGRAPH || eType == TT_PROGRESS_COURSE
                || eType == TT_SECTION) {

            List<Element> children = current.getChildren();
            Iterator<Element> iterator = children.iterator();

            while (iterator.hasNext()) {
                Element child = iterator.next();
                writeChildren(child);
            }
        }

        switch (eType) {

            case TT_PARAGRAPH:
                endParagraph();
                break;

            case TT_CONTENT:
                endContent();
                break;

            case TT_ICON:
                endIcon();
                break;

            case TT_COMPONENT:
                endComponent();
                break;

            case TT_PROGRESS_COURSE:
                endProgressCourse();
                break;

            case TT_SECTION:
                endSection();
                break;
        }
    }

    private void startSection() {
    }

    private void endSection() {
    }

    private void startProgressCourse() {
    }

    private void endProgressCourse() {
    }

    private void startParagraph(String lStyle, String alignStr) {

        thePane.setLogicalStyle("default");
        logicalStyle = true;

        if (alignStr != null) {
            DefaultStyledDocument doc = (DefaultStyledDocument) thePane
                    .getTextPane().getDocument();
            Style style0 = doc.getStyle("default");
            Style style = doc.addStyle("alignment", style0);
            switch (alignStr) {
                case "0":
                    StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
                    break;
                case "1":
                    StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
                    break;
                case "2":
                    StyleConstants.setAlignment(style, StyleConstants.ALIGN_RIGHT);
                    break;
            }
            thePane.setLogicalStyle("alignment");
            logicalStyle = true;
        }
    }

    private void endParagraph() {

        //thePane.makeParagraph(); // trim() の廃止で廃止
        if (logicalStyle) {
            thePane.clearLogicalStyle();
            logicalStyle = false;
        }
    }

    private void startContent(String foreground, String size, String bold,
                              String italic, String underline, String text) {

        // 特殊文字を戻す
        for (int i = 0; i < REPLACES.length; i++) {
            text = text.replaceAll(MATCHES[i], REPLACES[i]);
        }

        // このコンテントに設定する AttributeSet
        MutableAttributeSet atts = new SimpleAttributeSet();

        // foreground 属性を設定する
        if (foreground != null) {
            StringTokenizer stk = new StringTokenizer(foreground, ",");
            if (stk.hasMoreTokens()) {
                int r = Integer.parseInt(stk.nextToken());
                int g = Integer.parseInt(stk.nextToken());
                int b = Integer.parseInt(stk.nextToken());
                StyleConstants.setForeground(atts, new Color(r, g, b));
            }
        }

        // size 属性を設定する
        if (size != null) {
            StyleConstants.setFontSize(atts, Integer.parseInt(size));
        }

        // bold 属性を設定する
        if (bold != null) {
            StyleConstants.setBold(atts, Boolean.valueOf(bold));
        }

        // italic 属性を設定する
        if (italic != null) {
            StyleConstants.setItalic(atts, Boolean.valueOf(italic));
        }

        // underline 属性を設定する
        if (underline != null) {
            StyleConstants.setUnderline(atts, Boolean.valueOf(underline));
        }

        // テキストを挿入する
        thePane.insertFreeString(text, atts);
    }

    private void endContent() {
    }

    private void startComponent(String name, String number) {

        debug("Entering startComponent");
        debug("Name = " + name);
        debug("Number = " + number);
        debug("soaPane = " + bSoaPane);

        try {
            if (name != null && name.equals(STAMP_HOLDER)) {
                int index = Integer.parseInt(number);
                ModuleModel stamp = bSoaPane
                        ? soaModules.get(index)
                        : pModules.get(index);
                thePane.flowStamp(stamp);

            } else if (name != null && name.equals(SCHEMA_HOLDER)) {
                int index = Integer.parseInt(number);
                thePane.flowSchema(model.getSchema(index));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace(System.err);
        }
    }

    private void endComponent() {
    }

    private void startIcon(org.jdom2.Element current) {

        String name = current.getChildTextTrim("name");

        if (name != null) {
            debug(name);
        }
    }

    private void endIcon() {
    }

    private void debug(String msg) {
        logger.debug(msg);
    }
}

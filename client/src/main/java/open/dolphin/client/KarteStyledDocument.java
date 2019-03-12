package open.dolphin.client;

import open.dolphin.project.Project;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.util.ArrayList;
import java.util.List;

/**
 * KartePane の StyledDocument class.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class KarteStyledDocument extends DefaultStyledDocument {
    private static final long serialVersionUID = 1L;

    // スタンプの先頭を改行する
    private boolean topSpace;
//masuda^
    private static final String STAMP_STYLE      = "stampHolder";
    private static final String SCHEMA_STYLE     = "schemaHolder";
//masuda$
    // KartePane
    private KartePane kartePane;


    /** Creates new TestDocument */
    public KarteStyledDocument() {

        topSpace = Project.getPreferences().getBoolean("stampSpace", true);

//masuda^   編集時に文書先頭・末尾のStamp/Schema HolderのEntryを更新する
        this.addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                KarteStyledDocument kd = kartePane.getDocument();
                int posStart = e.getOffset();
                int posEnd = posStart + e.getLength();
                int len = kd.getLength();
                int pos;

                if (posStart == 0 && len > 1) {
                    // 文頭に挿入したときは
                    pos = posEnd;
                } else if (posEnd == len && posStart > 0) {
                    // 文末に挿入したとき
                    pos = posStart - 1;
                } else {
                    return;
                }
                // エントリを修正するComponentHolderを取得
                Object obj = StyleConstants.getComponent(kd.getCharacterElement(pos).getAttributes());
                try {
                    if (obj instanceof ComponentHolder){
                        ComponentHolder ch = (ComponentHolder) obj;
                        // エントリを修正
                        ch.setEntry(kd.createPosition(pos), kd.createPosition(pos + 1));
                    }
                } catch (BadLocationException ex) {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
//masuda$
    }

    public void setParent(KartePane kartePane) {
        this.kartePane = kartePane;
    }

    public void setLogicalStyle(String str) {
        Style style = this.getStyle(str);
        this.setLogicalStyle(this.getLength(), style);
    }

    public void clearLogicalStyle() {
        this.setLogicalStyle(this.getLength(), null);
    }

    public void makeParagraph() {
        try {
            insertString(getLength(), "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stamp を挿入する.
     * @param sh 挿入するスタンプホルダ
     */
    public void stamp(final StampHolder sh) {

        try {
            Style runStyle = this.getStyle(STAMP_STYLE);
            if (runStyle == null) {
                runStyle = addStyle(STAMP_STYLE, null);
            }
            StyleConstants.setComponent(runStyle, sh);

            // キャレット位置を取得する
            int start = kartePane.getTextPane().getCaretPosition();

            // Stamp を挿入する
            if (topSpace) {
                insertString(start, "\n", null);
                insertString(start+1, " ", runStyle);
                insertString(start+2, "\n", null);                           // 改行をつけないとテキスト入力制御がやりにくくなる
                sh.setEntry(createPosition(start+1), createPosition(start+2)); // スタンプの開始と終了位置を生成して保存する
            } else {
                insertString(start, " ", runStyle);
                insertString(start+1, "\n", null);                           // 改行をつけないとテキスト入力制御がやりにくくなる
                sh.setEntry(createPosition(start), createPosition(start+1)); // スタンプの開始と終了位置を生成して保存する
            }

        } catch(BadLocationException be) {
            be.printStackTrace();
        } catch(NullPointerException ne) {
            ne.printStackTrace();
        }
    }

    /**
     * Stamp を挿入する.
     * @param sh 挿入するスタンプホルダ
     */
    public void flowStamp(final StampHolder sh) {


        try {
            Style runStyle = this.getStyle(STAMP_STYLE);
            if (runStyle == null) {
                runStyle = addStyle(STAMP_STYLE, null);
            }
            // このスタンプ用のスタイルを動的に生成する
            //Style runStyle = addStyle(STAMP_STYLE, null);
            StyleConstants.setComponent(runStyle, sh);

            // キャレット位置を取得する
            int start = kartePane.getTextPane().getCaretPosition();

            // Stamp を挿入する
            insertString(start, " ", runStyle);

            // スタンプの開始と終了位置を生成して保存する
            sh.setEntry(createPosition(start), createPosition(start+1));

        } catch(BadLocationException be) {
            be.printStackTrace();
        } catch(NullPointerException ne) {
            ne.printStackTrace();
        }
    }

    /**
     * Stampを削除する.
     * @param start 削除開始のオフセット位置
     * @param len
     */
    public void removeStamp(int start, int len) {

        try {
//masuda^   Stamp/Schemaをremoveするときは直後の改行も削除する
            // Stamp は一文字で表されている
            //remove(start, 1);
            if (start < getLength() && "\n".equals(getText(start+1, 1))) {
                remove(start, 2);
            } else {
                remove(start, 1);
            }
//masuda$
        } catch(BadLocationException be) {
            be.printStackTrace();
        }
    }

    /**
     * Stampを指定されたポジションに挿入する.
     * @param inPos　挿入ポジション
     * @param sh　挿入する StampHolder
     */
    public void insertStamp(Position inPos, StampHolder sh) {

        try {
            Style runStyle = this.getStyle(STAMP_STYLE);
            if (runStyle == null) {
                runStyle = addStyle(STAMP_STYLE, null);
            }
            //Style runStyle = this.addStyle(STAMP_STYLE, null);
            StyleConstants.setComponent(runStyle, sh);

            // 挿入位置
            int start = inPos.getOffset();
            insertString(start, " ", runStyle);
            sh.setEntry(createPosition(start), createPosition(start+1));
        } catch(BadLocationException be) {
            be.printStackTrace();
        }
    }

    public void stampSchema(SchemaHolder sc) {

        try {
            Style runStyle = this.getStyle(SCHEMA_STYLE);
            if (runStyle == null) {
                runStyle = addStyle(SCHEMA_STYLE, null);
            }
            // このスタンプ用のスタイルを動的に生成する
            //Style runStyle = addStyle(SCHEMA_STYLE, null);
            StyleConstants.setComponent(runStyle, sc);

            // Stamp同様
            int start = kartePane.getTextPane().getCaretPosition();
            insertString(start, " ", runStyle);
            insertString(start+1, "\n", null);
            sc.setEntry(createPosition(start), createPosition(start+1));
        } catch(BadLocationException be) {
            be.printStackTrace();
        }
    }

    public void flowSchema(final SchemaHolder sh) {

        try {
            Style runStyle = this.getStyle(SCHEMA_STYLE);
            if (runStyle == null) {
                runStyle = addStyle(SCHEMA_STYLE, null);
            }
            // このスタンプ用のスタイルを動的に生成する
            //Style runStyle = addStyle(SCHEMA_STYLE, null);
            StyleConstants.setComponent(runStyle, sh);

            // キャレット位置を取得する
            int start = kartePane.getTextPane().getCaretPosition();

            // Stamp を挿入する
            insertString(start, " ", runStyle);

            // スタンプの開始と終了位置を生成して保存する
            sh.setEntry(createPosition(start), createPosition(start+1));

        } catch(BadLocationException be) {
            be.printStackTrace();
        } catch(NullPointerException ne) {
            ne.printStackTrace();
        }
    }

    public void insertTextStamp(String text) {

        try {
            //System.out.println("insertTextStamp");
            clearLogicalStyle();
            setLogicalStyle("default"); // mac 2207-03-31
            int pos = kartePane.getTextPane().getCaretPosition();
            //System.out.println("pos = " + pos);
            insertString(pos, text, null);
            //System.out.println("inserted TextStamp");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void insertFreeString(String text, AttributeSet a) {
        try {
            insertString(getLength(), text, a);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

//masuda^   KarteStyledDocument内のStampHolderを取得する. pns先生のコード
    public List<StampHolder> getStampHolders() {

        List<StampHolder> list = new ArrayList<StampHolder>();
        int length = getLength();
        for (int i = 0; i < length; ++i) {
            StampHolder sh = (StampHolder) StyleConstants.getComponent(getCharacterElement(i).getAttributes());
            if (sh != null) {
                list.add(sh);
            }
        }
        return list;
    }

    // StampHolder直後の改行がない場合は補う
    public void fixCrAfterStamp(){
        try {
            int i = 0;
            while (i < getLength()) {
                StampHolder sh = (StampHolder) StyleConstants.getComponent(getCharacterElement(i).getAttributes());
                String strNext = getText(++i, 1);
                if (sh != null && !"\n".equals(strNext)) {
                    insertString(i, "\n" ,null);
                }

            }
        } catch (BadLocationException ex) {
        }
    }
//masuda$
}

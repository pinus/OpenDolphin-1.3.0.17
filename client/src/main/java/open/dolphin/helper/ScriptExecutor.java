package open.dolphin.helper;

import java.awt.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Script でいろいろする.
 * @author pns
 */
public class ScriptExecutor {

    private static final String CR = "\n";
    private static final String QUOTE = "\"";

    private static final String[] OPEN_PATIENT_FOLDER_SCRIPT = {
        "tell application \"Finder\"",
        "   set targetFolder to argv as POSIX file",                    // Folder の時は POSIX から Mac 形式に変換が必要 (ファイルは無変換でもOK）
        "   open targetFolder",
        "   set displayBounds to bounds of window of desktop",          // 画面全体の大きさ x1, y1, x2, y2 の順番
        "   set win to Finder window 1",
        "   set winBounds to bounds of win",
        "   set item 1 of winBounds to (item 3 of displayBounds)/3*2",  // x1 = 全体の 2/3
        "   set item 2 of winBounds to 20",                             // y1 = 一番上から20ドット下
        "   set item 3 of winBounds to (item 3 of displayBounds)-50",   // x2 = 右端から 50ドット左
        "   set item 4 of winBounds to (item 4 of displayBounds)/2",    // y2 = 全体の半分
        "   set bounds of win to winBounds",
        "end tell",
    };

    private static final String DISPLAY_NOTIFICATION_SCRIPT =
        "display notification \"%s\" with title \"%s\" subtitle \"%s\"";

    private static final String IME_ON_SCRIPT =
        "tell application \"System Events\" to tell process \"WhateverItIs\" to key code 104";

    private static final String IME_OFF_SCRIPT =
        "tell application \"System Events\" to tell process \"WhateverItIs\" to key code 102";

    //private static final String[] GET_ATOK_MEM_SIZE_SCRIPT = {"/bin/sh", "-c",
    //    //"ps -A -o rss,command | grep ATOK24.app | grep -v grep | sed -e \'s/\\/.*$//\' -e \'s/ //g\'"
    //    "ps -A -o rss,command | grep \'/Contents/MacOS/ATOK25\' | grep -v grep | sed -e \'s/\\/.*$//\' -e \'s/ //g\'"
    //};

    //private static final String RESTART_ATOK24_SCRIPT =
    //    //"tell application \"ATOK24\"" + CR +
    //    "tell application \"ATOK25\"" + CR +
    //    "   quit" + CR +
    //    "   delay 0.1" + CR +
    //    "   launch" + CR +
    //    "end tell" + CR;

    // private static boolean atokRestarted = false;

    /**
     * 通知センターに通知を表示する.
     * @param message Message
     * @param title Title
     * @param subtitle Subtitle
     */
    public static void displayNotification(String message, String title, String subtitle) {
        String script = String.format(DISPLAY_NOTIFICATION_SCRIPT, message, title, subtitle);
        //new AppleScriptExecutor(script).start(); // これだとダメ
        executeShellScript( new String[] {"osascript", "-e", script});
    }

    /**
     * 情報フォルダを開く.
     * @param path POSIX path
     */
    public static void openPatientFolder(final String path) {
        // スクリプトに path を設定 → POSIX file がエラーを出すようになった 2018-12-14
        //OPEN_PATIENT_FOLDER_SCRIPT[1] = "set targetFolder to \""+ path + "\" as POSIX file";
        //new AppleScriptExecutor(getCodeString(OPEN_PATIENT_FOLDER_SCRIPT)).start();

        // convert POSIX path to folder "folder1" of folder "folder2" ... of disk "disk"
        String[] folders = path.replace("/Volumes/", "").split("/");
        StringBuilder sb = new StringBuilder();
        int len = folders.length - 1;
        sb.append("folder ").append(QUOTE).append(folders[len]).append(QUOTE);
        for (int i=len-1; i>=1; i--) {
            sb.append(" of folder ").append(QUOTE).append(folders[i]).append(QUOTE);
        }
        sb.append(" of disk ").append(QUOTE).append(folders[0]).append(QUOTE);

        OPEN_PATIENT_FOLDER_SCRIPT[1] = "";
        OPEN_PATIENT_FOLDER_SCRIPT[2] = "open " + sb.toString();

        new AppleScriptExecutor(getCodeString(OPEN_PATIENT_FOLDER_SCRIPT)).start();
    }

    /**
     * かなキー（keycode 104）を System Event に送る.
     */
    public static void setImeOn() {
        new AppleScriptExecutor(IME_ON_SCRIPT).start();
    }

    /**
     * 英数キー（keycode 102）を System Event に送る.
     */
    public static void setImeOff() {
        new AppleScriptExecutor(IME_OFF_SCRIPT).start();
    }

    /**
     * window の InputMethodContext で U.S. になるまで待つ ImeOff.
     * @param w Window
     */
    public static void setImeOff(Window w) {

        boolean b = isImeUs(w);
        if (!b) {
            setImeOff();
            b = isImeUs(w);
            // retry
            int timeout = 0;
            while (!b && timeout < 20) {
                timeout++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                setImeOff();
                b = isImeUs(w);
                System.out.println("ExecuteScript: setImeOff: us?=" + b + "  retry=" + timeout);
            }
        }
    }

    /**
     * 現在の ime モードが U.S. かどうかを返す.
     * @param w InputContext を調べる JFrame or JDialog
     * @return true if Locale.US
     */
    public static boolean isImeUs(Window w) {
        //sun.awt.im.InputContext ic = (sun.awt.im.InputContext) w.getInputContext();
        //return ic.getInputMethodInfo().equals("U.S.");
        return w.getInputContext().getLocale().equals(Locale.US);
    }

    /**
     * ATOK24 をリスタートする.
     */
    //public static void restartAtok24() {
    //    // これは，thread ではなくて，直接 run で起動して，帰ってくるのを待つ
    //    // 待たないと，次の getAtok24MemSize が間に合わない
    //    atokRestarted = true;
    //    new AppleScriptExecutor(RESTART_ATOK24_SCRIPT).run();
    //}

    /**
     * ATOK24 の使用メモリサイズを調べる（単位 KB）.
     * ATOK24 が走っていなければ 0 を返す.
     * @return
     */
    //public static int getAtok24MemSize() {
    //    List<String> output = executeShellScript(GET_ATOK_MEM_SIZE_SCRIPT);
    //    //if (output.isEmpty()) System.out.println("ATOK25 not found");
    //    return (output.size() == 1)? Integer.valueOf(output.get(0)) : atokRestarted? 1:0;
    //}

    /**
     * 選択ファイルを QuickLook する.
     * @param path POSIX Path to target
     */
    public static void quickLook(String path) {
        final String[] command = {"qlmanage", "-p", path};
        executeShellScript(command);
    }

    /**
     * shell command を実行する.
     * @param command Shell commands in a string array
     */
    private static void executeShellScript(String[] command) {
        Thread t = new Thread(()-> {
            try {
                Runtime.getRuntime().exec(command).waitFor();
            } catch (IOException | InterruptedException ex) {
                System.out.println("ExecuteScript.java: " + ex);
            }
        });
        t.start();
    }

    /**
     * shell command を実行して，標準出力を返す.
     * @param command Commands in a string array
     * @return outPut Result strings in List
     */
    private static List<String> executeShellScriptWithResponce(String[] command) {
        List<String> output = new ArrayList<>();

        try {
            Process p = Runtime.getRuntime().exec(command);
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                output.add(line);
            }
            p.waitFor();

        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
        }

        return output;
    }

    /**
     * 複数ストリング配列に入ったスクリプトを１行スクリプトに変換.
     * @param code Code lines in a string array
     * @return Joined string
     */
    private static String getCodeString(String[] code) {
        StringBuilder cmd = new StringBuilder();
        for (String line : code) {
            cmd.append(line);
            cmd.append(CR);
        }
        return cmd.toString();
    }

    public static void main (String[] arg) {
        // 7088 /Library/Input Methods/ATOK25.app/Contents/MacOS/ATOK25 -psn_0_512125
        // 1124 /usr/bin/codesign --display --entitlements - /Library/Input Methods/ATOK25.app

        ScriptExecutor.openPatientFolder("/Volumes/Documents/000001/");
        //ExecuteScript.quickLook("/Volumes/documents/008113/お返事2011-01-11.pdf");
        //ExecuteScript.restartAtok24();
        //System.out.println(ScriptExecutor.getAtok24MemSize());
        //ExecuteScript.setImeOff();
        //ScriptExecutor.displayNotification("message", "title", "subtitle");
    }
}

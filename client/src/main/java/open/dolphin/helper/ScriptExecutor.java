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
 *
 * @author pns
 */
public class ScriptExecutor {

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

    private static final String[] IME_ON_SCRIPT = {
            "tell application \"System Events\" to tell process \"WhateverItIs\" to key code 104" };

    private static final String[] IME_OFF_SCRIPT = {
            "tell application \"System Events\" to tell process \"WhateverItIs\" to key code 102" };

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
     *
     * @param message  Message
     * @param title    Title
     * @param subtitle Subtitle
     */
    public static void displayNotification(String message, String title, String subtitle) {
        String script = String.format(DISPLAY_NOTIFICATION_SCRIPT, message, title, subtitle);
        executeShellScript(new String[]{"osascript", "-e", script});
    }

    /**
     * 情報フォルダを開く.
     *
     * @param path POSIX path
     */
    public static void openPatientFolder(final String path) {
        // スクリプトに path を設定
        OPEN_PATIENT_FOLDER_SCRIPT[1] = "set targetFolder to " + QUOTE + path + QUOTE + " as POSIX file";
        executeAppleScript(OPEN_PATIENT_FOLDER_SCRIPT);
    }

    /**
     * かなキー（keycode 104）を System Event に送る.
     */
    public static void setImeOn() {
        executeAppleScript(IME_ON_SCRIPT);
    }

    /**
     * 英数キー（keycode 102）を System Event に送る.
     */
    public static void setImeOff() {
        executeAppleScript(IME_OFF_SCRIPT);
    }

    /**
     * window の InputMethodContext で U.S. になるまで待つ ImeOff.
     *
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
     *
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
     *
     * @param path POSIX Path to target
     */
    public static void quickLook(String path) {
        final String[] command = {"qlmanage", "-p", path};
        executeShellScript(command);
    }

    /**
     * shell command を実行する.
     *
     * @param command Shell commands in a string array
     */
    private static void executeShellScript(String[] command) {
        Thread t = new Thread(() -> {
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
     *
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
     * AppleScript を osascript で実行する.
     *
     * @param script Arrays of script
     */
    public static void executeAppleScript(String[] script) {
        List<String> codes = new ArrayList<>();
        codes.add("osascript");
        Arrays.stream(script).forEach(line -> {
            codes.add("-e");
            codes.add(line);
        });
        executeShellScript(codes.toArray(new String[0]));
    }

    public static void main(String[] arg) {
        // 7088 /Library/Input Methods/ATOK25.app/Contents/MacOS/ATOK25 -psn_0_512125
        // 1124 /usr/bin/codesign --display --entitlements - /Library/Input Methods/ATOK25.app

        //ScriptExecutor.openPatientFolder("/Volumes/Documents/000001-010000/000001-001000/000001");
        //ScriptExecutor.quickLook("/Volumes/documents/000001-010000/000001-001000/000001/2007年12月16日14時05分33秒.pdf");
        //ExecuteScript.restartAtok24();
        //System.out.println(ScriptExecutor.getAtok24MemSize());
        //ScriptExecutor.setImeOff();
        ScriptExecutor.displayNotification("message", "title", "subtitle");
    }
}

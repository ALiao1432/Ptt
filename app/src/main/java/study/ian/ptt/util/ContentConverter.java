package study.ian.ptt.util;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ContentConverter {

    private static final String TAG = "ContentConverter";
    private static final List<Pair<String, String>> replaceStyleList = new ArrayList<>();
    private static final Pair<String, String> brPair;

    static {
        replaceStyleList.add(new Pair<>("class=\"f0\"", "style=\"color:#000000\""));
        replaceStyleList.add(new Pair<>("class=\"f0 hl\"", "style=\"color:#666666\""));
        replaceStyleList.add(new Pair<>("class=\"f0 b1\"", "style=\"color:#000000 background-color:#BB0000\""));
        replaceStyleList.add(new Pair<>("class=\"f0 b2\"", "style=\"color:#000000 background-color:#00BB00\""));
        replaceStyleList.add(new Pair<>("class=\"f0 b3\"", "style=\"color:#000000 background-color:#BBBB00\""));
        replaceStyleList.add(new Pair<>("class=\"f0 b4\"", "style=\"color:#000000 background-color:#0000BB\""));
        replaceStyleList.add(new Pair<>("class=\"f0 b5\"", "style=\"color:#000000 background-color:#BB00BB\""));
        replaceStyleList.add(new Pair<>("class=\"f0 b6\"", "style=\"color:#000000 background-color:#00BBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f0 b7\"", "style=\"color:#000000 background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f0 b7 hl\"", "style=\"color:#666666 background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f1\"", "style=\"color:#990000\""));
        replaceStyleList.add(new Pair<>("class=\"f1 hl\"", "style=\"color:#FF6666\""));
        replaceStyleList.add(new Pair<>("class=\"f1 b1\"", "style=\"color:#990000 background-color:#BB0000\""));
        replaceStyleList.add(new Pair<>("class=\"f1 b2\"", "style=\"color:#990000 background-color:#00BB00\""));
        replaceStyleList.add(new Pair<>("class=\"f1 b3\"", "style=\"color:#990000 background-color:#BBBB00\""));
        replaceStyleList.add(new Pair<>("class=\"f1 b4 hl\"", "style=\"color:#FF6666 background-color:#0000BB\""));
        replaceStyleList.add(new Pair<>("class=\"f1 b5\"", "style=\"color:#990000 background-color:#BB00BB\""));
        replaceStyleList.add(new Pair<>("class=\"f1 b6\"", "style=\"color:#990000 background-color:#00BBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f1 b6 hl\"", "style=\"color:#666666 background-color:#00BBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f1 b7\"", "style=\"color:#990000 background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f2\"", "style=\"color:#009900\""));
        replaceStyleList.add(new Pair<>("class=\"f2 b1\"", "style=\"color:#009900 background-color:#BB0000\""));
        replaceStyleList.add(new Pair<>("class=\"f2 b2\"", "style=\"color:#009900 background-color:#00BB00\""));
        replaceStyleList.add(new Pair<>("class=\"f2 b3\"", "style=\"color:#009900 background-color:#BBBB00\""));
        replaceStyleList.add(new Pair<>("class=\"f2 b4\"", "style=\"color:#009900 background-color:#0000BB\""));
        replaceStyleList.add(new Pair<>("class=\"f2 b5\"", "style=\"color:#009900 background-color:#BB00BB\""));
        replaceStyleList.add(new Pair<>("class=\"f2 b6\"", "style=\"color:#009900 background-color:#00BBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f2 b7\"", "style=\"color:#009900 background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f2 hl\"", "style=\"color:#66FF66\""));
        replaceStyleList.add(new Pair<>("class=\"f3\"", "style=\"color:#FFFF66\""));
        replaceStyleList.add(new Pair<>("class=\"f3 hl\"", "style=\"color:#FFFF66\""));
        replaceStyleList.add(new Pair<>("class=\"f3 b1\"", "style=\"color:#999900 background-color:#BB0000\""));
        replaceStyleList.add(new Pair<>("class=\"f3 b1 hl\"", "style=\"color:#FFFF66 background-color:#BB0000\""));
        replaceStyleList.add(new Pair<>("class=\"f3 b2\"", "style=\"color:#999900 background-color:#00BB00\""));
        replaceStyleList.add(new Pair<>("class=\"f3 b3\"", "style=\"color:#999900 background-color:#BBBB00\""));
        replaceStyleList.add(new Pair<>("class=\"f3 b4\"", "style=\"color:#999900 background-color:#0000BB\""));
        replaceStyleList.add(new Pair<>("class=\"f3 b4 hl\"", "style=\"color:#FFFF66 background-color:#0000BB\""));
        replaceStyleList.add(new Pair<>("class=\"f3 b5 hl\"", "style=\"color:#FFFF66 background-color:#BB00BB\""));
        replaceStyleList.add(new Pair<>("class=\"f3 b6\"", "style=\"color:#999900 background-color:#00BBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f3 b7\"", "style=\"color:#999900 background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f4\"", "style=\"color:#000099\""));
        replaceStyleList.add(new Pair<>("class=\"f4 hl\"", "style=\"color:#6666FF\""));
        replaceStyleList.add(new Pair<>("class=\"f4 b2\"", "style=\"color:#000099 background-color:#00BB00\""));
        replaceStyleList.add(new Pair<>("class=\"f4 b3\"", "style=\"color:#000099 background-color:#BBBB00\""));
        replaceStyleList.add(new Pair<>("class=\"f4 b3 hl\"", "style=\"color:#6666FF background-color:#BBBB00\""));
        replaceStyleList.add(new Pair<>("class=\"f4 b4 hl\"", "style=\"color:#6666FF background-color:#0000BB\""));
        replaceStyleList.add(new Pair<>("class=\"f4 b7 hl\"", "style=\"color:#6666FF background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f5 b5 hl\"", "style=\"color:#FF66FF background-color:#BB00BB\""));
        replaceStyleList.add(new Pair<>("class=\"f5 b7\"", "style=\"color:#990099 background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f5 b7 hl\"", "style=\"color:#FF66FF background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f5 hl\"", "style=\"color:#FF66FF\""));
        replaceStyleList.add(new Pair<>("class=\"f6\"", "style=\"color:#009999\""));
        replaceStyleList.add(new Pair<>("class=\"f6 b1\"", "style=\"color:#009999 background-color:#BB0000\""));
        replaceStyleList.add(new Pair<>("class=\"f6 b2\"", "style=\"color:#009999 background-color:#00BB00\""));
        replaceStyleList.add(new Pair<>("class=\"f6 b3\"", "style=\"color:#009999 background-color:#BBBB00\""));
        replaceStyleList.add(new Pair<>("class=\"f6 b4\"", "style=\"color:#009999 background-color:#0000BB\""));
        replaceStyleList.add(new Pair<>("class=\"f6 b5\"", "style=\"color:#009999 background-color:#BB00BB\""));
        replaceStyleList.add(new Pair<>("class=\"f6 b6\"", "style=\"color:#009999 background-color:#00BBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f6 b7\"", "style=\"color:#009999 background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"f6 hl\"", "style=\"color:#66FFFF\""));
        replaceStyleList.add(new Pair<>("class=\"b1\"", "style=\"color:#BBBBBB background-color:#BB0000\""));
        replaceStyleList.add(new Pair<>("class=\"b1 hl\"", "style=\"color:#FFFFFF background-color:#BB0000\""));
        replaceStyleList.add(new Pair<>("class=\"b2 hl\"", "style=\"color:#FFFFFF background-color:#00BB00\""));
        replaceStyleList.add(new Pair<>("class=\"b3 hl\"", "style=\"color:#FFFFFF background-color:#BBBB00\""));
        replaceStyleList.add(new Pair<>("class=\"b4 hl\"", "style=\"color:#FFFFFF background-color:#0000BB\""));
        replaceStyleList.add(new Pair<>("class=\"b5\"", "style=\"color:#BBBBBB background-color:#BB00BB\""));
        replaceStyleList.add(new Pair<>("class=\"b5 hl\"", "style=\"color:#FFFFFF background-color:#BB00BB\""));
        replaceStyleList.add(new Pair<>("class=\"b6 hl\"", "style=\"color:#FFFFFF background-color:#00BBBB\""));
        replaceStyleList.add(new Pair<>("class=\"b7\"", "style=\"color:#BBBBBB background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"b7 hl\"", "style=\"color:#FFFFFF background-color:#BBBBBB\""));
        replaceStyleList.add(new Pair<>("class=\"hl\"", "style=\"color:#FFFFFF\""));

        brPair = new Pair<>("\n", "<br>");
    }

    public static String classToStyle(String source) {
        for (Pair<String, String> pair : replaceStyleList) {
            source = source.replace(pair.first, pair.second);
        }
        return source;
    }

    public static String newLineToBr(String source) {
        return source.replaceAll(brPair.first, brPair.second);
    }
}

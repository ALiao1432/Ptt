package study.ian.ptt.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import androidx.appcompat.app.AppCompatActivity;

public class PreManager {

    private static final String PREF_NAME = "study.ian.ptt";
    private static final String FAV_BOARD = "favBoard";
    private static final String BLACK_LIST = "blackList";

    private SharedPreferences sharedPreferences;
    private Context context;
    private static Set<String> favSet = new HashSet<>();

    public PreManager(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        initFavSet();
    }

    private void initFavSet() {
        String boards = sharedPreferences.getString(FAV_BOARD, "");
        StringTokenizer tokenizer = new StringTokenizer(boards);
        while (tokenizer.hasMoreTokens()) {
            favSet.add(tokenizer.nextToken());
        }
    }

    public void setAppTheme(int styleId, int layoutId) {
        context.setTheme(styleId);
        ((AppCompatActivity) context).setContentView(layoutId);
    }

    public void toggleFavBoard(String board) {
        if (isFavBoard(board)) {
            // if it is fav board, then remove it
            removeFavBoard(board);
        } else {
            // if it is not fav board, then add it
            addFavBoard(board);
        }
    }

    public void addFavBoard(String board) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String temp = board + " " + sharedPreferences.getString(FAV_BOARD, "");
        editor.putString(FAV_BOARD, temp);
        editor.apply();

        favSet.add(board);
    }

    public void removeFavBoard(String board) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String temp = sharedPreferences.getString(FAV_BOARD, "");
        if (temp != null && temp.contains(board)) {
            temp = temp.replace(board, "").trim();
            editor.putString(FAV_BOARD, temp);
            editor.apply();
        }

        favSet.remove(board);
    }

    public String getFavBoard() {
        return sharedPreferences.getString(FAV_BOARD, "");
    }

    public void addBlackList(String black) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String temp = black + " " + sharedPreferences.getString(BLACK_LIST, "");
        editor.putString(BLACK_LIST, temp);
        editor.apply();
    }

    public String getBlackList() {
        return sharedPreferences.getString(BLACK_LIST, "");
    }

    public boolean isFavBoard(String targetBoard) {
        return favSet.contains(targetBoard);
    }
}

package study.ian.ptt.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import androidx.appcompat.app.AppCompatActivity;

public class PreManager {

    private static final String PREF_NAME = "study.ian.ptt";
    private static final String FAV_BOARD = "favBoard";
    private static final String BLACK_LIST = "blackList";

    public static final int FAV_ACTION_ADD = 0;
    public static final int FAV_ACTION_REMOVE = 1;

    private static Set<String> favSet = new HashSet<>();
    private static PreManager preManager;
    private static Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<OnFavActionListener> onFavActionListenerList = new ArrayList<>();

    public interface OnFavActionListener {
        void onFavAction(String b, int action);
    }

    public void setOnFavActionListener(OnFavActionListener listener) {
        onFavActionListenerList.add(listener);
    }

    private PreManager(Context cxt) {
        sharedPreferences = cxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        initFavSet();
    }

    private void initFavSet() {
        String boards = sharedPreferences.getString(FAV_BOARD, "");
        StringTokenizer tokenizer = new StringTokenizer(boards);
        while (tokenizer.hasMoreTokens()) {
            favSet.add(tokenizer.nextToken());
        }
    }

    public static synchronized void initPreManager(Context cxt) {
        if (preManager == null) {
            preManager = new PreManager(cxt);
            context = cxt;
        }
    }

    public static synchronized PreManager getInstance() {
        return preManager;
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
        editor = sharedPreferences.edit();
        String temp = board + " " + sharedPreferences.getString(FAV_BOARD, "");
        editor.putString(FAV_BOARD, temp);
        editor.apply();

        favSet.add(board);
        onFavActionListenerList.forEach(l -> l.onFavAction(board, FAV_ACTION_ADD));
    }

    public void removeFavBoard(String board) {
        editor = sharedPreferences.edit();
        String temp = sharedPreferences.getString(FAV_BOARD, "");
        if (temp != null && temp.contains(board)) {
            temp = temp.replace(board, "").trim();
            editor.putString(FAV_BOARD, temp);
            editor.apply();
        }

        favSet.remove(board);
        onFavActionListenerList.forEach(l -> l.onFavAction(board, FAV_ACTION_REMOVE));
    }

    public Set<String> getFavBoardSet() {
        return favSet;
    }

    public String getFavBoard() {
        return sharedPreferences.getString(FAV_BOARD, "");
    }

    public void addBlackList(String black) {
        editor = sharedPreferences.edit();
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

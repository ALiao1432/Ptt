package study.ian.ptt.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class PreManager {

    private static final String PREF_NAME = "study.ian.ptt";
    private static final String FAV_BOARD = "favBoard";
    private static final String BLACK_LIST = "blackList";

    private static final int FAV_ACTION_ADD = 0;
    private static final int FAV_ACTION_REMOVE = 1;

    private static Set<String> favSet = new HashSet<>();
    private static Set<String> blackListSet;
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
        initBlackListSet();
    }

    private void initFavSet() {
        favSet = new HashSet<>();

        String boards = sharedPreferences.getString(FAV_BOARD, "");
        StringTokenizer tokenizer = new StringTokenizer(boards);
        while (tokenizer.hasMoreTokens()) {
            favSet.add(tokenizer.nextToken());
        }
    }

    private void initBlackListSet() {
        blackListSet = new HashSet<>();

        String blackList = sharedPreferences.getString(BLACK_LIST, "");
        StringTokenizer tokenizer = new StringTokenizer(blackList);
        while (tokenizer.hasMoreTokens()) {
            blackListSet.add(tokenizer.nextToken());
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
        String b = sharedPreferences.getString(BLACK_LIST, "");
        if (b != null && !b.contains(black)) {
            editor.putString(BLACK_LIST, black + " " + b);

            blackListSet.add(black);
        }
        editor.apply();
    }

    public void updateBlackList(String blacks) {
        editor = sharedPreferences.edit();
        editor.putString(BLACK_LIST, blacks);
        editor.apply();

        initBlackListSet();
    }

    public String getBlackList() {
        return sharedPreferences.getString(BLACK_LIST, "");
    }

    public boolean isFavBoard(String targetBoard) {
        return favSet.contains(targetBoard);
    }

    public boolean isBlacklist(String black) {
        return blackListSet.contains(black);
    }
}

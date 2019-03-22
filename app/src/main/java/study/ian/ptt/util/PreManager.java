package study.ian.ptt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class PreManager {

    private static final String PREF_NAME = "study.ian.ptt";
    private static final String FAV_BOARD = "favBoard";
    private static final String BLACK_LIST = "blackList";

    private SharedPreferences sharedPreferences;
    private Context context;

    public PreManager(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setAppTheme(int styleId, int layoutId) {
        context.setTheme(styleId);
        ((AppCompatActivity) context).setContentView(layoutId);
    }

    public void addFavBoard(String board) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String temp = board + ";" + sharedPreferences.getString(FAV_BOARD, "");
        editor.putString(FAV_BOARD, temp);
        editor.apply();
    }

    public String getFavBoard() {
        return sharedPreferences.getString(FAV_BOARD, "");
    }

    public void addBlackList(String black) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String temp = black + ";" + sharedPreferences.getString(BLACK_LIST, "");
        editor.putString(BLACK_LIST, temp);
        editor.apply();
    }

    public String getBlackList() {
        return sharedPreferences.getString(BLACK_LIST, "");
    }
}

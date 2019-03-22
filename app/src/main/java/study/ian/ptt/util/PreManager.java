package study.ian.ptt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class PreManager {

    private static final String PREF_NAME = "study.ian.ptt";
    private static final String FAV_BOARD = "favBoard";

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
        String temp = sharedPreferences.getString(FAV_BOARD, "") + ";" + board;
        editor.putString(FAV_BOARD, temp);
        editor.apply();
    }

    public String getFavBoard() {
        return sharedPreferences.getString(FAV_BOARD, "");
    }
}

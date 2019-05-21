package study.ian.ptt.util;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreManager.initPreManager(this);
    }
}

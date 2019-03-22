package study.ian.ptt.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class SlideTransformer implements ViewPager.PageTransformer {

    private final String TAG = "SlideTransformer";

    @Override
    public void transformPage(@NonNull View view, float position) {
        Log.d(TAG, "transformPage: position : " + position);
        if (position < -1) {
            view.setAlpha(0f);
        } else if (position == -1) {
            view.setTranslationX(0);
        } else if (position < 0) {
            float startTranslation = -0.8f;
            view.setTranslationX((float) view.getWidth() * startTranslation * position);
            view.setAlpha(1 + position);
            view.setElevation(0f);
        } else if (position == 0f) {
            view.setTranslationX(0f);
            view.setElevation(1f);
            view.setAlpha(1f);
        } else if (position > 0 && position <= 1) {
            view.setAlpha(1f);
            view.setElevation(1f);
        } else {
            view.setAlpha(0f);
        }
    }
}

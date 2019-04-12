package study.ian.ptt.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class SlideTransformer implements ViewPager.PageTransformer {

    private final String TAG = "SlideTransformer";

    @Override
    public void transformPage(@NonNull View view, float position) {
        if (position < -1) {
            view.setAlpha(0f);
        } else if (position == -1) {
            view.setTranslationX(0);
        } else if (position < 0) {
            float startTranslation = -0.8f;
            view.setTranslationX((float) view.getWidth() * startTranslation * position);
            view.setAlpha(1 + position);
        } else if (position == 0f) {
            view.setTranslationX(0f);
            view.setAlpha(1f);
        } else if (position > 0 && position <= 1) {
            view.setAlpha(1f);
        } else {
            view.setAlpha(0f);
        }
    }
}

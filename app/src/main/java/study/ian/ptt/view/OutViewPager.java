package study.ian.ptt.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class OutViewPager extends ViewPager {

    private final String TAG = "OutViewPager";

    public static boolean interceptTouch = false;

    public OutViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!interceptTouch) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }
}

package study.ian.ptt.util;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class GlideImageGetter implements Html.ImageGetter {

    private TextView textView;
    private Drawable drawable = null;

    public GlideImageGetter(TextView view) {
        this.textView = view;
    }

    @Override
    public Drawable getDrawable(String source) {

        Glide.with(textView)
                .asDrawable()
                .load(source)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        drawable = resource;
                        textView.setText(textView.getText());
                        textView.postInvalidate();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        return drawable;
    }

    private class BitmapDrawableHolder extends BitmapDrawable {


    }
}

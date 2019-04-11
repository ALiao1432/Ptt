package study.ian.ptt.util;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

public class GlideImageGetter implements Html.ImageGetter {

    private final String TAG = "GlideImageGetter";
    private TextView textView;

    public GlideImageGetter(TextView textView) {
        this.textView = textView;
    }

    @Override
    public Drawable getDrawable(String source) {
        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder(textView);
        GlideApp.with(textView)
                .load(source)
                .transition(new DrawableTransitionOptions().crossFade(250))
                .into(drawable);
        return drawable;
    }

    class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target<Drawable> {

        private TextView textView;
        private Drawable drawable;
        private Request request;

        BitmapDrawablePlaceHolder(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        @Override
        public void onLoadStarted(@Nullable Drawable placeholder) {

        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {

        }

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
            this.drawable = resource;

            float scaleFac = 1f;
            if (drawable.getIntrinsicWidth() != textView.getWidth()) {
                scaleFac = (float) textView.getWidth() / (float) drawable.getIntrinsicWidth();
            }
            Log.d(TAG, "onResourceReady: dra wid : " + drawable.getIntrinsicWidth() + ", scaF : " + scaleFac);
            drawable.setBounds(0, 0, Math.round(drawable.getIntrinsicWidth() * scaleFac), Math.round(drawable.getIntrinsicHeight() * scaleFac));
            this.setBounds(0, 0, Math.round(drawable.getIntrinsicWidth() * scaleFac), Math.round(drawable.getIntrinsicHeight() * scaleFac));
            textView.setText(textView.getText());
        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {

        }

        @Override
        public void getSize(@NonNull SizeReadyCallback cb) {
            cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        }

        @Override
        public void removeCallback(@NonNull SizeReadyCallback cb) {

        }

        @Override
        public void setRequest(@Nullable Request request) {
            this.request = request;
        }

        @Nullable
        @Override
        public Request getRequest() {
            return request;
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onDestroy() {

        }
    }
}

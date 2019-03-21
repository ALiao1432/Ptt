package study.ian.ptt.util;

import android.support.v4.text.HtmlCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class SpanUtil {

    public static Spannable getSpanned(TextView textView, String source) {
        return new SpannableStringBuilder(HtmlCompat.fromHtml(
                source,
                HtmlCompat.FROM_HTML_MODE_LEGACY,
                new GlideImageGetter(textView),
                null
        ));
    }

    public static void setImageClickListener(Spannable spannable, ImageSpanClickListener listener) {
        ImageSpan[] imageSpans = spannable.getSpans(0, spannable.length(), ImageSpan.class);
        for (ImageSpan imageSpan : imageSpans) {
            int start = spannable.getSpanStart(imageSpan);
            int end = spannable.getSpanEnd(imageSpan);

            if (start != -1 && end != -1 && end > start) {
                spannable.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        listener.onImageClick(imageSpan);
                    }
                }, start, end, spannable.getSpanFlags(imageSpan));
            }
        }
    }
}

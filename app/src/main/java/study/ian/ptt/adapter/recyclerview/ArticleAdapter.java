package study.ian.ptt.adapter.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import kotlin.Unit;
import study.ian.ptt.R;
import study.ian.ptt.model.article.Article;
import study.ian.ptt.model.article.Push;
import study.ian.ptt.util.OnPollClickedListener;
import study.ian.ptt.util.OnPollLongClickedListener;
import study.ian.ptt.util.SpanUtil;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "ArticleAdapter";
    private final static int VIEW_TYPE_HEADER = 0;
    private final static int VIEW_TYPE_CONTENT = 1;
    private final static int VIEW_TYPE_PUSH = 2;
    private final static int VIEW_TYPE_POLL = 3;
    private final static int TAG_STATE_NORMAL = 0;
    private final static int TAG_STATE_FLOOR = 1;
    private final int COLOR_PUSH;
    private final int COLOR_ARROW;
    private final int COLOR_SHUSH;
    private final int COLOR_KNOWN;
    private final PublishSubject<Integer> tagFloorSubject = PublishSubject.create();
    private final PublishSubject<String> highlightAuthorSubject = PublishSubject.create();

    private OnPollClickedListener pollClickedListener;
    private OnPollLongClickedListener pollLongClickedListener;
    private Article article;
    private List<Push> pushList;
    private Spannable spannable;
    private String highLightAuthor = "";
    private int tagState = TAG_STATE_NORMAL;

    public ArticleAdapter(Context context) {
        Resources resources = context.getResources();

        COLOR_PUSH = ResourcesCompat.getColor(resources, R.color.push, null);
        COLOR_ARROW = ResourcesCompat.getColor(resources, R.color.arrow, null);
        COLOR_SHUSH = ResourcesCompat.getColor(resources, R.color.shush, null);
        COLOR_KNOWN = ResourcesCompat.getColor(resources, R.color.unknownTag, null);
    }

    public void addResults(Article article) {
        this.article = article;
        pushList = article.getPushList();
        notifyDataSetChanged();
    }

    public void clearResults() {
        highLightAuthor = "";

        if (article != null) {
            article = null;
            pushList.clear();
            notifyDataSetChanged();
        }
    }

    public void setOnPollClickedListener(OnPollClickedListener listener) {
        pollClickedListener = listener;
    }

    public void setOnPollLongClickedListener(OnPollLongClickedListener listener) {
        pollLongClickedListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                v = inflater.inflate(R.layout.holder_article_header, parent, false);
                return new ArticleHeaderHolder(v);
            case VIEW_TYPE_CONTENT:
                v = inflater.inflate(R.layout.holder_article_content, parent, false);
                return new ArticleContentHolder(v);
            case VIEW_TYPE_PUSH:
                v = inflater.inflate(R.layout.holder_article_push, parent, false);
                return new ArticlePushHolder(v);
            case VIEW_TYPE_POLL:
                v = inflater.inflate(R.layout.holder_article_poll, parent, false);
                return new ArticlePollHolder(v);
            default:
                v = inflater.inflate(R.layout.holder_article_push, parent, false);
                return new ArticlePushHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleHeaderHolder) {
            configHeaderHolder((ArticleHeaderHolder) holder);
        } else if (holder instanceof ArticleContentHolder) {
            configContentHolder((ArticleContentHolder) holder);
        } else if (holder instanceof ArticlePushHolder) {
            configPushHolder((ArticlePushHolder) holder, position);
        } else {
            configPollHolder((ArticlePollHolder) holder);
        }
    }

    private void configHeaderHolder(ArticleHeaderHolder holder) {
        holder.articleTitleText.setText(article.getTitle());
        holder.articleAuthorText.setText(article.getAuthor());
        holder.articleBoardText.setText(article.getBoard());
        holder.articleTimeText.setText(article.getArticleTime());
    }

    private void configContentHolder(ArticleContentHolder holder) {
        spannable = SpanUtil.getSpanned(holder.articleContentText, article.getMainContent().trim());
        SpanUtil.setImageClickListener(spannable, imageSpan -> Log.d(TAG, "configContentHolder: span : " + spannable + ", imageSpan : " + imageSpan));
        holder.articleContentText.setText(spannable);
    }

    private void configPushHolder(ArticlePushHolder holder, int position) {
        int posInList = position - 2;
        Push push = pushList.get(posInList);
        String pushTagCount = push.getPushTagCount() + push.getPushTag();
        String floorCount = (position - 1) + "樓";

        holder.pushTagText.setText(tagState == TAG_STATE_NORMAL ? pushTagCount : floorCount);
        holder.pushTagText.setTextColor(getTagColor(push.getPushTag()));
        holder.pushAuthorText.setText(push.getAuthor());
        holder.pushContentText.setText(push.getContent());
        holder.pushTimeText.setText(push.getTime());

        holder.pushTagClickObservable
                .doOnNext(u -> tagFloorSubject.onNext(toggleTagState()))
                .doOnError(t -> Log.d(TAG, "configPushHolder: click pushTagText error : " + t))
                .subscribe();

        tagFloorSubject.doOnNext(state -> holder.pushTagText.setText(state == TAG_STATE_NORMAL ? pushTagCount : floorCount))
                .doOnError(t -> Log.d(TAG, "configPushHolder: set tag text error : " + t))
                .subscribe();

        holder.pushAuthorClickObservable
                .doOnNext(u -> {
                    highLightAuthor = (push.getAuthor().equals(highLightAuthor) ? "" : push.getAuthor());
                    highlightAuthorSubject.onNext(push.getAuthor());
                })
                .doOnError(t -> Log.d(TAG, "configPushHolder: click push author error : " + t))
                .subscribe();

        highlightAuthorSubject.doOnNext(highlight -> toggleHighlightAuthor(holder.articlePushLayout, push.getAuthor()))
                .doOnError(t -> Log.d(TAG, "configPushHolder: set highlight author error : " + t))
                .subscribe();

        toggleHighlightAuthor(holder.articlePushLayout, push.getAuthor());
    }

    private void configPollHolder(ArticlePollHolder holder) {
        holder.pollClickObservable
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .doOnNext(unit -> pollClickedListener.onPollClicked())
                .doOnError(t -> Log.d(TAG, "configPollHolder: poll click error : " + t))
                .subscribe();

        holder.pollLongClickObservable
                .doOnNext(unit -> pollLongClickedListener.onPollLongClicked())
                .doOnError(t -> Log.d(TAG, "configPollHolder: poll long click error : " + t))
                .subscribe();
    }

    private int getTagColor(String tag) {
        switch (tag) {
            case "推":
                return COLOR_PUSH;
            case "→":
                return COLOR_ARROW;
            case "噓":
                return COLOR_SHUSH;
        }
        return COLOR_KNOWN;
    }

    private int toggleTagState() {
        if (tagState == TAG_STATE_NORMAL) {
            tagState = TAG_STATE_FLOOR;
        } else {
            tagState = TAG_STATE_NORMAL;
        }
        return tagState;
    }

    private void toggleHighlightAuthor(View view, String author) {
        if (author.equals(highLightAuthor) || highLightAuthor.equals("")) {
            view.setAlpha(1f);
        } else {
            view.setAlpha(.25f);
        }
    }

    @Override
    public int getItemCount() {
        if (article == null) {
            return 0;
        }
        return pushList.size() + 3;
    }

    @Override
    public int getItemViewType(int pos) {
        if (pos == 0) {
            return VIEW_TYPE_HEADER;
        } else if (pos == 1) {
            return VIEW_TYPE_CONTENT;
        } else if (pos == pushList.size() + 2) {
            return VIEW_TYPE_POLL;
        } else {
            return VIEW_TYPE_PUSH;
        }
    }

    class ArticleHeaderHolder extends RecyclerView.ViewHolder {

        private final TextView articleTitleText;
        private final TextView articleBoardText;
        private final TextView articleAuthorText;
        private final TextView articleTimeText;

        ArticleHeaderHolder(@NonNull View v) {
            super(v);
            articleTitleText = v.findViewById(R.id.articleTitleText);
            articleBoardText = v.findViewById(R.id.articleBoardText);
            articleAuthorText = v.findViewById(R.id.articleAuthorText);
            articleTimeText = v.findViewById(R.id.articleTimeText);
        }
    }

    class ArticleContentHolder extends RecyclerView.ViewHolder {

        private final TextView articleContentText;

        ArticleContentHolder(@NonNull View v) {
            super(v);
            articleContentText = v.findViewById(R.id.articleContentText);
        }
    }

    class ArticlePushHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout articlePushLayout;
        private final TextView pushTagText;
        private final TextView pushAuthorText;
        private final TextView pushTimeText;
        private final TextView pushContentText;
        private final Observable<Unit> pushTagClickObservable;
        private final Observable<Unit> pushAuthorClickObservable;

        ArticlePushHolder(@NonNull View v) {
            super(v);
            articlePushLayout = v.findViewById(R.id.articlePushLayout);
            pushTagText = v.findViewById(R.id.pushTagText);
            pushAuthorText = v.findViewById(R.id.pushAuthorText);
            pushTimeText = v.findViewById(R.id.pushTimeText);
            pushContentText = v.findViewById(R.id.pushContentText);
            pushTagClickObservable = RxView.clicks(pushTagText);
            pushAuthorClickObservable = RxView.clicks(pushAuthorText);
        }
    }

    class ArticlePollHolder extends RecyclerView.ViewHolder {

        private final MaterialButton pollBtn;
        private final Observable<Unit> pollClickObservable;
        private final Observable<Unit> pollLongClickObservable;

        ArticlePollHolder(@NonNull View v) {
            super(v);
            pollBtn = v.findViewById(R.id.pollBtn);
            pollClickObservable = RxView.clicks(pollBtn);
            pollLongClickObservable = RxView.longClicks(pollBtn);
        }
    }
}

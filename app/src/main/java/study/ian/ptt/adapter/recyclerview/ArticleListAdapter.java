package study.ian.ptt.adapter.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import kotlin.Unit;
import study.ian.ptt.R;
import study.ian.ptt.model.category.ArticleInfo;
import study.ian.ptt.util.CountTextConverter;
import study.ian.ptt.util.OnArticleListClickedListener;
import study.ian.ptt.util.OnArticleListLongClickedListener;
import study.ian.ptt.util.PreManager;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleHolder> {

    private final String TAG = "ArticleListAdapter";

    private final Context context;
    private final ViewPager outPager;
    private final PreManager preManager;
    private final Resources resources;
    private List<ArticleInfo> infoList = new ArrayList<>();
    private OnArticleListLongClickedListener longClickListener;
    private OnArticleListClickedListener clickedListener;

    public ArticleListAdapter(@NotNull Context context, ViewPager pager) {
        this.context = context;
        resources = context.getResources();
        preManager = PreManager.getInstance();
        outPager = pager;
    }

    public void addResults(List<ArticleInfo> list) {
        int addPosition = infoList.size();
        infoList.addAll(list);
        infoList = infoList.stream()
                .filter(info -> !preManager.isBlacklist(info.getAuthor()))
                .collect(Collectors.toList());
        notifyItemInserted(addPosition);
    }

    public void clearResults() {
        infoList.clear();
        notifyDataSetChanged();
    }

    public void setOnArticleListLongClickedListener(OnArticleListLongClickedListener listener) {
        longClickListener = listener;
    }

    public void setOnArticleListClickedListener(OnArticleListClickedListener listener) {
        clickedListener = listener;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_article, parent, false);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        ArticleInfo info = infoList.get(position);

        holder.articleLongClickObservable
                .doOnNext(unit -> longClickListener.onArticleListLongClicked(info))
                .doOnError(t -> Log.d(TAG, "onBindViewHolder: long click article error : " + t))
                .subscribe();

        holder.articleClickObservable
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .filter(unit -> info.getHref().length() > 0)
                .doOnNext(unit -> {
                    clickedListener.onArticleListClicked(info);
                    outPager.setCurrentItem(2);
                })
                .doOnError(t -> Log.d(TAG, "onBindViewHolder: click article error : " + t))
                .subscribe();

        holder.countText.setText(info.getCount());
        holder.countText.setTextColor(CountTextConverter.getPushCountColor(resources, info.getCount()));
        holder.titleText.setText(info.getTitle());
        holder.authorText.setText(info.getAuthor());
        holder.markText.setText(info.getMark());
        holder.dateText.setText(info.getDate());
        holder.articleLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_up));
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    class ArticleHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout articleLayout;
        private final TextView countText;
        private final TextView titleText;
        private final TextView authorText;
        private final TextView markText;
        private final TextView dateText;
        private final Observable<Unit> articleLongClickObservable;
        private final Observable<Unit> articleClickObservable;

        ArticleHolder(@NonNull View v) {
            super(v);

            articleLayout = v.findViewById(R.id.articleLayout);
            countText = v.findViewById(R.id.countText);
            titleText = v.findViewById(R.id.titleText);
            authorText = v.findViewById(R.id.authorText);
            markText = v.findViewById(R.id.markText);
            dateText = v.findViewById(R.id.dateText);
            articleLongClickObservable = RxView.longClicks(articleLayout);
            articleClickObservable = RxView.clicks(articleLayout);
        }
    }
}

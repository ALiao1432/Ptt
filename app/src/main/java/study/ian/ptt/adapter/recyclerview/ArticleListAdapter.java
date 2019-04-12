package study.ian.ptt.adapter.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subjects.PublishSubject;
import study.ian.ptt.R;
import study.ian.ptt.model.category.ArticleInfo;
import study.ian.ptt.util.CountTextConverter;
import study.ian.ptt.util.OnArticleListClickedListener;
import study.ian.ptt.util.OnArticleListLongClickedListener;
import study.ian.ptt.util.PreManager;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleHolder> {

    private final String TAG = "ArticleListAdapter";

    private final ViewPager outPager;
    private List<ArticleInfo> infoList = new ArrayList<>();
    private PreManager preManager;
    private Resources resources;
    private OnArticleListLongClickedListener longClickListener;
    private OnArticleListClickedListener clickedListener;

    public ArticleListAdapter(Context context, ViewPager pager) {
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

        RxView.longClicks(holder.articleCard)
                .doOnNext(unit -> longClickListener.onArticleListLongClicked(info))
                .doOnError(t -> Log.d(TAG, "onBindViewHolder: long click article error : " + t))
                .subscribe();

        RxView.clicks(holder.articleCard)
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
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    class ArticleHolder extends RecyclerView.ViewHolder {

        private CardView articleCard;
        private TextView countText;
        private TextView titleText;
        private TextView authorText;
        private TextView markText;
        private TextView dateText;

        ArticleHolder(@NonNull View v) {
            super(v);

            articleCard = v.findViewById(R.id.articleCard);
            countText = v.findViewById(R.id.countText);
            titleText = v.findViewById(R.id.titleText);
            authorText = v.findViewById(R.id.authorText);
            markText = v.findViewById(R.id.markText);
            dateText = v.findViewById(R.id.dateText);
        }
    }
}

package study.ian.ptt.adapter.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import study.ian.ptt.R;
import study.ian.ptt.model.category.ArticleInfo;
import study.ian.ptt.util.CountTextConverter;
import study.ian.ptt.util.PreManager;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private final String TAG = "ArticleAdapter";

    private List<ArticleInfo> infoList = new ArrayList<>();
    private PreManager preManager;
    private Resources resources;

    public ArticleAdapter(Context context) {
        resources = context.getResources();
        preManager = PreManager.getInstance();
    }

    public void addResults(List<ArticleInfo> list) {
        infoList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearResults() {
        infoList.clear();
        notifyDataSetChanged();
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

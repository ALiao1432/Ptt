package study.ian.ptt.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import study.ian.ptt.R;
import study.ian.ptt.model.article.Push;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "ArticleAdapter";
    private final static int VIEW_TYPE_HEADER = 0;
    private final static int VIEW_TYPE_CONTENT = 1;
    private final static int VIEW_TYPE_PUSH = 2;

    private List<Push> pushList = new ArrayList<>();

    public ArticleAdapter() {

    }

    public void addResults(List<Push> pushList) {
        this.pushList = pushList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                View headerView = inflater.inflate(R.layout.holder_article_header, parent, false);
                return new ArticleHeaderHolder(headerView);
            case VIEW_TYPE_CONTENT:
                View contentView = inflater.inflate(R.layout.holder_article_content, parent, false);
                return new ArticleContentHolder(contentView);
            case VIEW_TYPE_PUSH:
            default:
                View pushView = inflater.inflate(R.layout.holder_article_push, parent, false);
                return new ArticlePushHolder(pushView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleHeaderHolder) {
            configHeaderHolder((ArticleHeaderHolder) holder, position);
        } else if (holder instanceof ArticleContentHolder) {
            configContentHolder((ArticleContentHolder) holder, position);
        } else if (holder instanceof ArticlePushHolder) {
            configPushHolder((ArticlePushHolder) holder, position);
        }
    }

    private void configHeaderHolder(ArticleHeaderHolder holder, int position) {

    }

    private void configContentHolder(ArticleContentHolder holder, int position) {

    }

    private void configPushHolder(ArticlePushHolder holder, int position) {
        int posInList = position - 2;

    }

    @Override
    public int getItemCount() {
        return pushList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_HEADER;
            case 1:
                return VIEW_TYPE_CONTENT;
            default:
                return VIEW_TYPE_PUSH;
        }
    }

    class ArticleHeaderHolder extends RecyclerView.ViewHolder {

        private TextView articleTitleText;
        private TextView articleBoardText;
        private TextView articleAuthorText;
        private TextView articleDateText;

        ArticleHeaderHolder(@NonNull View itemView) {
            super(itemView);

            articleTitleText = itemView.findViewById(R.id.articleTitleText);
            articleBoardText = itemView.findViewById(R.id.articleBoardText);
            articleAuthorText = itemView.findViewById(R.id.articleAuthorText);
            articleDateText = itemView.findViewById(R.id.articleDateText);
        }
    }

    class ArticleContentHolder extends RecyclerView.ViewHolder {

        private TextView articleContentText;

        ArticleContentHolder(@NonNull View itemView) {
            super(itemView);

            articleContentText = itemView.findViewById(R.id.articleContentText);
        }
    }

    class ArticlePushHolder extends RecyclerView.ViewHolder {

        private TextView pushTagText;
        private TextView pushAuthorText;
        private TextView pushTagCountText;
        private TextView pushTimeText;
        private TextView pushContentText;

        ArticlePushHolder(@NonNull View itemView) {
            super(itemView);

            pushTagText = itemView.findViewById(R.id.pushTagText);
            pushAuthorText = itemView.findViewById(R.id.pushAuthorText);
            pushTagCountText = itemView.findViewById(R.id.pushTagCountText);
            pushTimeText = itemView.findViewById(R.id.pushTimeText);
            pushContentText = itemView.findViewById(R.id.pushContentText);
        }
    }
}

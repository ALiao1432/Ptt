package study.ian.ptt.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import study.ian.ptt.R;
import study.ian.ptt.adapter.recyclerview.ArticleAdapter;
import study.ian.ptt.model.category.ArticleInfo;
import study.ian.ptt.util.OnArticleListClickedListener;

public class ArticleFragment extends BaseFragment implements OnArticleListClickedListener {

    private final String TAG = "ArticleFragment";

    private RecyclerView articleRecyclerView;
    private ArticleInfo articleInfo;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_article, container, false);

        findViews(v);
        setViews();

        return v;
    }

    private void findViews(View v) {
        articleRecyclerView = v.findViewById(R.id.recyclerViewArticle);
    }

    private void setViews() {
        ArticleAdapter articleAdapter = new ArticleAdapter();

    }

    public void loadData() {

    }

    @Override
    public void onArticleListClicked(ArticleInfo info) {
        Log.d(TAG, "onArticleListClicked: info : " + info);
        articleInfo = info;
    }
}

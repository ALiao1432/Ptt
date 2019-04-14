package study.ian.ptt.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import retrofit2.Response;
import study.ian.ptt.R;
import study.ian.ptt.adapter.recyclerview.ArticleAdapter;
import study.ian.ptt.model.article.Article;
import study.ian.ptt.model.category.ArticleInfo;
import study.ian.ptt.service.PttService;
import study.ian.ptt.service.ServiceBuilder;
import study.ian.ptt.util.ObserverHelper;
import study.ian.ptt.util.OnArticleListClickedListener;

public class ArticleFragment extends BaseFragment implements OnArticleListClickedListener {

    private final String TAG = "ArticleFragment";

    private final PttService pttService = ServiceBuilder.getPttService();
    private Context context;
    private RecyclerView articleRecyclerView;
    private TextView articleInfoText;
    private ArticleAdapter articleAdapter;
    private Article article;
    private ArticleInfo articleInfo;
    private ValueAnimator alphaAnimator;
    private ValueAnimator scaleAnimator;
    private boolean runAnimation;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_article, container, false);

        initAnimator();
        findViews(v);
        setViews();

        return v;
    }

    private void findViews(View v) {
        articleRecyclerView = v.findViewById(R.id.recyclerViewArticle);
        articleInfoText = v.findViewById(R.id.articleInfoText);
    }

    private void setViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        articleAdapter = new ArticleAdapter(context);

        articleRecyclerView.setAdapter(articleAdapter);
        articleRecyclerView.setHasFixedSize(true);
        articleRecyclerView.setLayoutManager(layoutManager);

        articleInfoText.setTextSize(25);
    }

    private void initAnimator() {
        long ANIM_DURATION = 250;
        alphaAnimator = ValueAnimator.ofFloat(1, 0);
        alphaAnimator.setDuration(ANIM_DURATION);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());
        alphaAnimator.addUpdateListener(animation -> articleInfoText.setAlpha((float) animation.getAnimatedValue()));

        scaleAnimator = ValueAnimator.ofFloat(1, .75f);
        scaleAnimator.setDuration(ANIM_DURATION);
        scaleAnimator.setInterpolator(new DecelerateInterpolator());
        scaleAnimator.addUpdateListener(animation -> {
            articleInfoText.setScaleX((float) animation.getAnimatedValue());
            articleInfoText.setScaleY((float) animation.getAnimatedValue());
        });
        scaleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                runAnimation = false;
                articleAdapter.addResults(article);
                articleInfoText.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void loadData() {
        final Observable<Response<Document>> o = pttService.getArticle(ServiceBuilder.COOKIE, articleInfo.getHref());
        processObservable(o);
    }

    private void processObservable(Observable<Response<Document>> o) {
        o.compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(this::configData)
                .doOnError(t -> Log.d(TAG, "processObservable: get article error : " + t))
                .subscribe();
    }

    private void configData(Document document) {
        article = new Article(document);

        if (runAnimation) {
            alphaAnimator.start();
            scaleAnimator.start();
        } else {
            articleAdapter.addResults(article);
        }
    }

    private void restoreTextState(TextView textView, String text) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);
        textView.setAlpha(1f);
        textView.setScaleX(1f);
        textView.setScaleY(1f);
    }

    @Override
    public void onArticleListClicked(ArticleInfo info) {
        articleInfo = info;
        articleAdapter.clearResults();
        runAnimation = true;
        restoreTextState(articleInfoText, info.getTitle());
        loadData();
    }
}

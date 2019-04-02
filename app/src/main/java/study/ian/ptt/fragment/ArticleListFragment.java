package study.ian.ptt.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;

import org.jsoup.nodes.Document;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Response;
import study.ian.ptt.R;
import study.ian.ptt.adapter.recyclerview.ArticleAdapter;
import study.ian.ptt.model.category.Category;
import study.ian.ptt.service.PttService;
import study.ian.ptt.service.ServiceBuilder;
import study.ian.ptt.util.ObserverHelper;
import study.ian.ptt.util.OnCategorySelectedListener;

public class ArticleListFragment extends BaseFragment implements OnCategorySelectedListener {

    private final String TAG = "ArticleListFragment";

    private Context context;
    private TextView categoryText;
    private BottomAppBar bottomAppBar;
    private ValueAnimator alphaAnimator;
    private ValueAnimator scaleAnimator;
    private RecyclerView articleRecyclerView;
    private ArticleAdapter articleAdapter;
    private Category category;
    private String cate;
    private boolean isLoading = false;
    private boolean runAnimation;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_article_list, container, false);

        initAnimator();
        findViews(v);
        setViews();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.bottomappbar_menu, menu);
    }

    private void findViews(View view) {
        articleRecyclerView = view.findViewById(R.id.recyclerViewArticle);
        categoryText = view.findViewById(R.id.categoryInfoText);
        bottomAppBar = view.findViewById(R.id.bottomBar);
    }

    private void setViews() {
        categoryText.setTextSize(50);

        bottomAppBar.replaceMenu(R.menu.bottomappbar_menu);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        articleAdapter = new ArticleAdapter(context);

        articleRecyclerView.setNestedScrollingEnabled(true);
        articleRecyclerView.setLayoutManager(layoutManager);
        articleRecyclerView.setAdapter(articleAdapter);
        articleRecyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            final int LOAD_MORE_THRESHOLD = 60;
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int totalItem = layoutManager.getItemCount();

            if (!isLoading && (lastVisibleItem + LOAD_MORE_THRESHOLD) >= totalItem) {
                if (category == null || category.hasPreviousPage()) {
                    loadData();
                }
            }
        });
    }

    private void loadData() {
        isLoading = true;

        ServiceBuilder.getService(PttService.class)
                .getCategory(ServiceBuilder.COOKIE, category == null ? cate + "/index.html" : category.getPrePage())
                .compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(this::configData)
                .doOnError(t -> Log.d(TAG, "setViews: get hot board error : " + t))
                .subscribe();
    }

    private void configData(Document document) {
        isLoading = false;

        category = new Category(document);
        if (runAnimation) {
            alphaAnimator.start();
            scaleAnimator.start();
        } else {
            articleAdapter.addResults(category.getArticleInfoList());
        }
    }

    private void initAnimator() {
        long ANIM_DURATION = 400;
        alphaAnimator = ValueAnimator.ofFloat(1, 0);
        alphaAnimator.setDuration(ANIM_DURATION);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());
        alphaAnimator.addUpdateListener(animation -> categoryText.setAlpha((float) animation.getAnimatedValue()));

        scaleAnimator = ValueAnimator.ofFloat(1, .75f);
        scaleAnimator.setDuration(ANIM_DURATION);
        scaleAnimator.setInterpolator(new DecelerateInterpolator());
        scaleAnimator.addUpdateListener(animation -> {
            categoryText.setScaleX((float) animation.getAnimatedValue());
            categoryText.setScaleY((float) animation.getAnimatedValue());
        });
        scaleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                runAnimation = false;
                articleAdapter.addResults(category.getArticleInfoList());
                categoryText.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void restoreTextState(TextView textView, String text) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);
        textView.setAlpha(1f);
        textView.setScaleX(1f);
        textView.setScaleY(1f);
    }

    @Override
    public void onCategorySelected(String cate) {
        this.cate = cate;
        category = null;
        runAnimation = true;
        articleAdapter.clearResults();
        restoreTextState(categoryText, cate);
        loadData();
    }
}

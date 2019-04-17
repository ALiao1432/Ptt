package study.ian.ptt.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jsoup.nodes.Document;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import retrofit2.Response;
import study.ian.ptt.R;
import study.ian.ptt.adapter.recyclerview.ArticleAdapter;
import study.ian.ptt.model.article.Article;
import study.ian.ptt.model.board.BoardInfo;
import study.ian.ptt.model.category.ArticleInfo;
import study.ian.ptt.service.PttService;
import study.ian.ptt.service.ServiceBuilder;
import study.ian.ptt.util.BottomSheetManager;
import study.ian.ptt.util.ObserverHelper;
import study.ian.ptt.util.OnArticleListClickedListener;
import study.ian.ptt.util.OnCategoryClickedListener;
import study.ian.ptt.util.OnPollClickedListener;
import study.ian.ptt.util.OnPollLongClickedListener;

public class ArticleFragment extends BaseFragment
        implements OnArticleListClickedListener, OnCategoryClickedListener, OnPollClickedListener, OnPollLongClickedListener {

    private final String TAG = "ArticleFragment";

    private final PttService pttService = ServiceBuilder.getPttService();
    private Context context;
    private RecyclerView articleRecyclerView;
    private TextView articleInfoText;
    private ConstraintLayout pollOptionLayout;
    private TextInputEditText pollIntervalEdt;
    private MaterialButton pollStartBtn;
    private MaterialButton pollCancelBtn;
    private ArticleAdapter articleAdapter;
    private Article article;
    private ArticleInfo articleInfo;
    private ValueAnimator alphaAnimator;
    private ValueAnimator scaleAnimator;
    private BottomSheetBehavior pollOptionSheet;
    private BottomSheetManager sheetManager = new BottomSheetManager();
    private boolean runAnimation;
    private boolean autoPolling = false;

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
        pollOptionLayout = v.findViewById(R.id.pollOptionBottomSheet);

        pollIntervalEdt = pollOptionLayout.findViewById(R.id.pollIntervalEdt);
        pollStartBtn = pollOptionLayout.findViewById(R.id.pollStartBtn);
        pollCancelBtn = pollOptionLayout.findViewById(R.id.pollCancelBtn);
    }

    private void setViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        articleAdapter = new ArticleAdapter(context);
        articleAdapter.setOnPollClickedListener(this);
        articleAdapter.setOnPollLongClickedListener(this);

        articleRecyclerView.setAdapter(articleAdapter);
        articleRecyclerView.setHasFixedSize(true);
        articleRecyclerView.setLayoutManager(layoutManager);

        articleInfoText.setTextSize(25);

        pollOptionSheet = BottomSheetBehavior.from(pollOptionLayout);
        sheetManager.addToSet(pollOptionSheet);

        View.OnClickListener pollClickListener = v -> {
            switch (v.getId()) {
                case R.id.pollStartBtn:
                    autoPolling = true;
                    Log.d(TAG, "setViews: start polling");
                    break;
                case R.id.pollCancelBtn:
                    autoPolling = false;
                    Log.d(TAG, "setViews: cancel polling");
                    break;
            }
            sheetManager.collapseSheet(pollOptionSheet);
        };
        pollStartBtn.setOnClickListener(pollClickListener);
        pollCancelBtn.setOnClickListener(pollClickListener);
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

    private void restoreTextState(TextView textView, SpannableString text) {
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

    @Override
    public void onCategoryClicked(BoardInfo boardInfo) {
        String hint = context.getResources().getString(R.string.unselected_article_hint);
        String s = hint + "\n" + boardInfo.getName();
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new RelativeSizeSpan(2.0f), hint.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.themeDarkPrimaryText, null)), hint.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        restoreTextState(articleInfoText, spannableString);
        articleAdapter.clearResults();
    }

    @Override
    public void onPollClicked() {
        Log.d(TAG, "onPollClicked: ");
    }

    @Override
    public void onPollLongClicked() {
        sheetManager.expandSheet(pollOptionSheet);

    }
}

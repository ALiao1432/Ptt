package study.ian.ptt.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Response;
import study.ian.ptt.R;
import study.ian.ptt.adapter.recyclerview.ArticleAdapter;
import study.ian.ptt.model.article.Article;
import study.ian.ptt.model.board.BoardInfo;
import study.ian.ptt.model.category.ArticleInfo;
import study.ian.ptt.model.poll.Poll;
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
    private LinearLayoutManager layoutManager;
    private RecyclerView.SmoothScroller smoothScroller;
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
    private CompositeDisposable pollDisposables = new CompositeDisposable();
    private PublishSubject<Integer> pollStateSubject = PublishSubject.create();
    private String cacheKey = "";
    private String currentOffset = "";
    private String currentOffsetSig = "";
    private int pollingState = ServiceBuilder.POLL_STATE_IDLE;
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

    private void findViews(@NotNull View v) {
        articleRecyclerView = v.findViewById(R.id.recyclerViewArticle);
        articleInfoText = v.findViewById(R.id.articleInfoText);
        pollOptionLayout = v.findViewById(R.id.pollOptionBottomSheet);

        pollIntervalEdt = pollOptionLayout.findViewById(R.id.pollIntervalEdt);
        pollStartBtn = pollOptionLayout.findViewById(R.id.pollStartBtn);
        pollCancelBtn = pollOptionLayout.findViewById(R.id.pollCancelBtn);
    }

    private void setViews() {
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        smoothScroller = new LinearSmoothScroller(context) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 100f / displayMetrics.densityDpi;
            }

            @Override
            protected int calculateTimeForScrolling(int dx) {
                return 250;
            }
        };

        articleAdapter = new ArticleAdapter(context);
        articleAdapter.setOnPollClickedListener(this);
        articleAdapter.setOnPollLongClickedListener(this);
        articleAdapter.setPollStateSubject(pollStateSubject);

        articleRecyclerView.setAdapter(articleAdapter);
        articleRecyclerView.setHasFixedSize(true);
        articleRecyclerView.setLayoutManager(layoutManager);

        articleInfoText.setTextSize(25);

        pollOptionSheet = BottomSheetBehavior.from(pollOptionLayout);
        sheetManager.addToSet(pollOptionSheet);

        View.OnClickListener pollClickListener = v -> {
            switch (v.getId()) {
                case R.id.pollStartBtn:
                    int interval;
                    Editable intervalEditable = pollIntervalEdt.getText();
                    if (intervalEditable != null && intervalEditable.length() != 0) {
                        interval = Integer.valueOf(intervalEditable.toString());
                    } else {
                        interval = 5;
                    }
                    loadPollData(interval);
                    break;
                case R.id.pollCancelBtn:
                    sheetManager.collapseSheet(pollOptionSheet);
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

    private Observable<Response<Poll>> initPollObservable() {
        return pttService.getLongPoll(ServiceBuilder.API_POLL_URL + article.getLongPollUrl(), ServiceBuilder.COOKIE)
                .takeUntil(Observable.timer(ServiceBuilder.LONG_POLL_TIMEOUT, TimeUnit.SECONDS))
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .filter(longPoll -> {
                    if (!currentOffset.equals(longPoll.getSize()) && !currentOffsetSig.equals(longPoll.getSig())) {
                        return true;
                    } else {
                        setPollState(ServiceBuilder.POLL_STATE_IDLE);
                        return false;
                    }
                })
                .flatMap(longPoll -> {
                    final Observable<Response<Poll>> poll = pttService.getPoll(ServiceBuilder.API_POLL_URL + article.getPollUrl().getPath(), ServiceBuilder.COOKIE, cacheKey, currentOffset, currentOffsetSig, longPoll.getSize(), longPoll.getSig());
                    currentOffset = longPoll.getSize();
                    currentOffsetSig = longPoll.getSig();
                    return poll;
                })
                .compose(ObserverHelper.applyHelper())
                .doOnNext(poll -> processPoll(poll.body()))
                .doOnError(t -> Log.d(TAG, "initPollObservable: get poll error : " + t));
    }

    private void loadPollData() {
        final Disposable disposable = initPollObservable().subscribe();
        pollDisposables.add(disposable);
    }

    private void loadPollData(final int interval) {
        final Disposable disposable = initPollObservable()
                .doOnEach(poll -> setPollState(ServiceBuilder.POLL_STATE_LOADING))
                .repeatWhen(o -> o.delay(interval, TimeUnit.SECONDS))
                .subscribe();
        pollDisposables.add(disposable);
    }

    private void processPoll(@Nullable Poll poll) {
        if (poll != null) {
            Document doc = Jsoup.parse(poll.getContentHtml());
            Elements pushElements = doc.select("div");

            if (pushElements.size() != 0) {
                article.addPushList(pushElements);
                articleAdapter.notifyDataSetChanged();
                smoothScroller.setTargetPosition(article.getPushList().size() + 2);
                layoutManager.startSmoothScroll(smoothScroller);
            }
        }
        setPollState(ServiceBuilder.POLL_STATE_IDLE);
    }

    private void loadData() {
        final Observable<Response<Document>> o = pttService.getArticle(ServiceBuilder.COOKIE, articleInfo.getHref());
        processArticle(o);
    }

    private void processArticle(@NotNull Observable<Response<Document>> o) {
        o.compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(this::configData)
                .doOnError(t -> Log.d(TAG, "processObservable: get article error : " + t))
                .subscribe();
    }

    private void configData(Document document) {
        article = new Article(document);
        if (cacheKey.length() == 0 || currentOffset.length() == 0 || currentOffsetSig.length() == 0) {
            cacheKey = article.getPollUrl().getQueryParameter("cacheKey");
            currentOffset = article.getPollUrl().getQueryParameter("offset");
            currentOffsetSig = article.getPollUrl().getQueryParameter("offset-sig");
        }

        if (runAnimation) {
            alphaAnimator.start();
            scaleAnimator.start();
        } else {
            articleAdapter.addResults(article);
        }
    }

    private void restoreTextState(@NotNull TextView textView, String text) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);
        textView.setAlpha(1f);
        textView.setScaleX(1f);
        textView.setScaleY(1f);
    }

    private void restoreTextState(@NotNull TextView textView, SpannableString text) {
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
        pollIntervalEdt.setText("");
        pollDisposables.clear();
        runAnimation = true;
        setPollState(ServiceBuilder.POLL_STATE_IDLE);
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

        cacheKey = "";
        currentOffset = "";
        currentOffsetSig = "";

        articleAdapter.clearResults();
    }

    private void setPollState(int state) {
        pollingState = state;
        pollStateSubject.onNext(state);
    }

    @Override
    public void onPollClicked() {
        if (pollingState == ServiceBuilder.POLL_STATE_IDLE) {
            setPollState(ServiceBuilder.POLL_STATE_LOADING);
            loadPollData();
        } else {
            setPollState(ServiceBuilder.POLL_STATE_IDLE);
            pollDisposables.clear();
        }
    }

    @Override
    public void onPollLongClicked() {
        if (pollingState == ServiceBuilder.POLL_STATE_IDLE) {
            setPollState(ServiceBuilder.POLL_STATE_LOADING);
            sheetManager.expandSheet(pollOptionSheet);
        } else {
            setPollState(ServiceBuilder.POLL_STATE_IDLE);
            pollDisposables.clear();
        }
    }
}

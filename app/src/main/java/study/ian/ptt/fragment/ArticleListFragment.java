package study.ian.ptt.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jsoup.nodes.Document;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import retrofit2.Response;
import study.ian.ptt.R;
import study.ian.ptt.adapter.recyclerview.ArticleAdapter;
import study.ian.ptt.model.category.ArticleInfo;
import study.ian.ptt.model.category.Category;
import study.ian.ptt.service.PttService;
import study.ian.ptt.service.ServiceBuilder;
import study.ian.ptt.util.ObserverHelper;
import study.ian.ptt.util.OnArticleListLongClickListener;
import study.ian.ptt.util.OnCategorySelectedListener;
import study.ian.ptt.util.PreManager;

public class ArticleListFragment extends BaseFragment
        implements OnCategorySelectedListener, OnArticleListLongClickListener {

    private final String TAG = "ArticleListFragment";
    private final static int LOAD_NORMAL_ARTICLE = 0;
    private final static int LOAD_SAME_TITLE = 1;
    private final static int LOAD_SAME_AUTHOR = 2;
    private final static int LOAD_SEARCH_AUTHOR = 3;
    private final static int LOAD_SEARCH_PUSH = 4;

    private Context context;
    private PreManager preManager;
    private PttService pttService = ServiceBuilder.getService(PttService.class);
    private TextView categoryText;
    private ConstraintLayout keywordBlackListLayout;
    private ConstraintLayout articleOptionLayout;
    private TextInputEditText searchEdt;
    private TextInputEditText blackListEdt;
    private MaterialButton authorBtn;
    private MaterialButton pushBtn;
    private MaterialButton updateBlackBtn;
    private MaterialButton addBlackBtn;
    private MaterialButton cancelBlackBtn;
    private MaterialButton sameTitleBtn;
    private MaterialButton sameAuthorBtn;
    private RecyclerView articleRecyclerView;
    private BottomSheetBehavior keywordBlackListSheet;
    private BottomSheetBehavior articleOptionSheet;
    private BottomAppBar bottomAppBar;
    private ValueAnimator alphaAnimator;
    private ValueAnimator scaleAnimator;
    private ArticleAdapter articleAdapter;
    private Category category;
    private String cate;
    private String searchQuery;
    private ArticleInfo selectInfo;
    private BottomSheetManager sheetManager = new BottomSheetManager();
    private int currentLoading = LOAD_NORMAL_ARTICLE;
    private boolean isLoading = false;
    private boolean runAnimation;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        preManager = PreManager.getInstance();
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

    private void findViews(View view) {
        articleRecyclerView = view.findViewById(R.id.recyclerViewArticle);
        categoryText = view.findViewById(R.id.categoryInfoText);
        bottomAppBar = view.findViewById(R.id.bottomBar);
        keywordBlackListLayout = view.findViewById(R.id.keywordBlackListBottomSheet);
        articleOptionLayout = view.findViewById(R.id.articleOptionBottomSheet);

        searchEdt = keywordBlackListLayout.findViewById(R.id.searchEdt);
        authorBtn = keywordBlackListLayout.findViewById(R.id.searchAuthorBtn);
        pushBtn = keywordBlackListLayout.findViewById(R.id.searchPushBtn);
        updateBlackBtn = keywordBlackListLayout.findViewById(R.id.updateBlackBtn);
        blackListEdt = keywordBlackListLayout.findViewById(R.id.blackListEdt);

        addBlackBtn = articleOptionLayout.findViewById(R.id.addBlackBtn);
        cancelBlackBtn = articleOptionLayout.findViewById(R.id.cancelBlackBtn);
        sameTitleBtn = articleOptionLayout.findViewById(R.id.sameTitleBtn);
        sameAuthorBtn = articleOptionLayout.findViewById(R.id.sameAuthorBtn);
    }

    private void setViews() {
        keywordBlackListSheet = BottomSheetBehavior.from(keywordBlackListLayout);
        articleOptionSheet = BottomSheetBehavior.from(articleOptionLayout);
        sheetManager.addToSet(keywordBlackListSheet);
        sheetManager.addToSet(articleOptionSheet);

        categoryText.setTextSize(50);

        bottomAppBar.setNavigationOnClickListener(v -> sheetManager.expandSheet(keywordBlackListSheet));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        articleAdapter = new ArticleAdapter(context);
        articleAdapter.setOnArticleListLongClickListener(this);

        articleRecyclerView.setNestedScrollingEnabled(true);
        articleRecyclerView.setLayoutManager(layoutManager);
        articleRecyclerView.setAdapter(articleAdapter);
        articleRecyclerView.setHasFixedSize(true);
        articleRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    sheetManager.collapseSheet(keywordBlackListSheet);
                    sheetManager.collapseSheet(articleOptionSheet);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                final int LOAD_MORE_THRESHOLD = 60;
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItem = layoutManager.getItemCount();

                if (!isLoading && (lastVisibleItem + LOAD_MORE_THRESHOLD) >= totalItem) {
                    if (category == null || category.hasPreviousPage()) {
                        switch (currentLoading) {
                            case LOAD_NORMAL_ARTICLE:
                                loadData();
                                break;
                            case LOAD_SAME_TITLE:
                                loadSameTitle();
                                break;
                            case LOAD_SAME_AUTHOR:
                                loadSameAuthor();
                                break;
                            case LOAD_SEARCH_AUTHOR:
                                loadSearchAuthor(searchQuery);
                                break;
                            case LOAD_SEARCH_PUSH:
                                loadSearchPush(searchQuery);
                                break;
                        }
                    }
                }
            }
        });

        View.OnClickListener searchClickListener = v -> {
            Editable sEditable = searchEdt.getText();
            String keyword = "";
            if (sEditable != null) {
                keyword = sEditable.toString();
            }

            Editable bEditable = blackListEdt.getText();
            String blacks = "";
            if (bEditable != null) {
                blacks = bEditable.toString();
            }

            switch (v.getId()) {
                case R.id.searchAuthorBtn:
                    searchEdt.setText("");
                    searchQuery = ServiceBuilder.SEARCH_AUTHOR + keyword;
                    loadSearchAuthor(searchQuery);
                    break;
                case R.id.searchPushBtn:
                    searchEdt.setText("");
                    searchQuery = ServiceBuilder.SEARCH_PUSH + keyword;
                    loadSearchPush(searchQuery);
                    break;
                case R.id.updateBlackBtn:
                    preManager.updateBlackList(blacks);
                    blackListEdt.setText(preManager.getBlackList());
                    break;
            }
            sheetManager.collapseSheet(keywordBlackListSheet);
        };
        authorBtn.setOnClickListener(searchClickListener);
        pushBtn.setOnClickListener(searchClickListener);
        updateBlackBtn.setOnClickListener(searchClickListener);
        blackListEdt.setText(preManager.getBlackList());

        View.OnClickListener articleOptionClickListener = v -> {
            switch (v.getId()) {
                case R.id.addBlackBtn:
                    preManager.addBlackList(selectInfo.getAuthor());
                    blackListEdt.setText(preManager.getBlackList());
                    break;
                case R.id.cancelBlackBtn:
                    break;
                case R.id.sameTitleBtn:
                    loadSameTitle();
                    break;
                case R.id.sameAuthorBtn:
                    loadSameAuthor();
                    break;
            }
            sheetManager.collapseSheet(articleOptionSheet);
        };
        addBlackBtn.setOnClickListener(articleOptionClickListener);
        cancelBlackBtn.setOnClickListener(articleOptionClickListener);
        sameTitleBtn.setOnClickListener(articleOptionClickListener);
        sameAuthorBtn.setOnClickListener(articleOptionClickListener);
    }

    private void loadData() {
        isLoading = true;

        Observable<Response<Document>> o = pttService.getCategory(ServiceBuilder.COOKIE, category == null ? cate + "/index.html" : category.getPrePage());
        processObservable(o);
    }

    private void loadSameTitle() {
        isLoading = true;

        String href = currentLoading == LOAD_SAME_TITLE ? category.getPrePage() : selectInfo.getSameTitleHref();
        if (currentLoading != LOAD_SAME_TITLE) {
            articleAdapter.clearResults();
        }
        currentLoading = LOAD_SAME_TITLE;

        Observable<Response<Document>> observable = pttService.getSearchResult(ServiceBuilder.API_BASE_URL + href, ServiceBuilder.COOKIE);
        processObservable(observable);
    }

    private void loadSameAuthor() {
        isLoading = true;

        String href = currentLoading == LOAD_SAME_AUTHOR ? category.getPrePage() : selectInfo.getSameAuthorHref();
        if (currentLoading != LOAD_SAME_AUTHOR) {
            articleAdapter.clearResults();
        }
        currentLoading = LOAD_SAME_AUTHOR;

        Observable<Response<Document>> observable = pttService.getSearchResult(ServiceBuilder.API_BASE_URL + href, ServiceBuilder.COOKIE);
        processObservable(observable);
    }

    private void loadSearchAuthor(String query) {
        isLoading = true;

        String href = currentLoading == LOAD_SEARCH_AUTHOR ? category.getPrePage() : cate + "/search?q=" + query;
        if (currentLoading != LOAD_SEARCH_AUTHOR) {
            articleAdapter.clearResults();
        }
        currentLoading = LOAD_SEARCH_AUTHOR;
        Observable<Response<Document>> observable = pttService.getSearchResult(ServiceBuilder.API_BASE_URL + href, ServiceBuilder.COOKIE);
        processObservable(observable);
    }

    private void loadSearchPush(String query) {
        isLoading = true;

        String href = currentLoading == LOAD_SEARCH_PUSH ? category.getPrePage() : cate + "/search?q=" + query;
        if (currentLoading != LOAD_SEARCH_PUSH) {
            articleAdapter.clearResults();
        }
        currentLoading = LOAD_SEARCH_PUSH;
        Observable<Response<Document>> observable = pttService.getSearchResult(ServiceBuilder.API_BASE_URL + href, ServiceBuilder.COOKIE);
        processObservable(observable);
    }

    private void processObservable(Observable<Response<Document>> o) {
        o.compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(this::configData)
                .doOnError(t -> Log.d(TAG, "setViews: load data error : " + t))
                .subscribe();
    }

    private void configData(Document document) {
        isLoading = false;

        boolean reverseData = (currentLoading == LOAD_NORMAL_ARTICLE);
        category = new Category(document, reverseData);
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
        searchEdt.setText("");
        currentLoading = LOAD_NORMAL_ARTICLE;
        articleAdapter.clearResults();
        restoreTextState(categoryText, cate);
        loadData();
    }

    @Override
    public void OnArticleListLongClick(ArticleInfo info) {
        selectInfo = info;
        sheetManager.expandSheet(articleOptionSheet);
    }

    class BottomSheetManager {
        Set<BottomSheetBehavior> behaviorSet = new HashSet<>();

        void addToSet(BottomSheetBehavior behavior) {
            behaviorSet.add(behavior);
        }

        void expandSheet(BottomSheetBehavior behavior) {
            for (BottomSheetBehavior b : behaviorSet) {
                if (b != behavior) {
                    collapseSheet(b);
                }
            }
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        void collapseSheet(BottomSheetBehavior behavior) {
            if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    }
}

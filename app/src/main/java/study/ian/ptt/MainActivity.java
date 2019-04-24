package study.ian.ptt;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import study.ian.ptt.adapter.viewpager.GenAdapter;
import study.ian.ptt.fragment.ArticleFragment;
import study.ian.ptt.fragment.ArticleListFragment;
import study.ian.ptt.fragment.BoardFragment;
import study.ian.ptt.model.board.BoardInfo;
import study.ian.ptt.model.category.ArticleInfo;
import study.ian.ptt.service.ServiceBuilder;
import study.ian.ptt.transformer.SlideTransformer;
import study.ian.ptt.util.PreManager;
import study.ian.ptt.view.OutViewPager;

// TODO: 2019-03-20 bug : fix space is not correct issue,
// TODO: 2019-03-21 bug : when display some img will cause scroll not smooth issue

// TODO: 2019-03-21 feature : set app theme color

// TODO: 2019-03-20 improve : text color

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private final int BACK_PRESS_CONFIRM_TIME = 2000;
    private CoordinatorLayout mainLayout;
    private OutViewPager outPager;
    private Disposable disposable;
    private Snackbar snackbar;
    private Intent intent;
    private int currentOutPage;
    private boolean doubleBackClickOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_TrueDark);
        setContentView(R.layout.activity_main);

        ServiceBuilder.watchNetworkState(this);

        PreManager.initPreManager(getApplicationContext());

        intent = getIntent();

        findViews();
        setViews();
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    private void findViews() {
        mainLayout = findViewById(R.id.mainLayout);
        outPager = findViewById(R.id.outPager);
    }

    private void setViews() {
        snackbar = Snackbar.make(mainLayout, getResources().getString(R.string.back_exit_confirm), Snackbar.LENGTH_SHORT);
        snackbar.setDuration(BACK_PRESS_CONFIRM_TIME);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.themeTrueDark, null));

        List<Fragment> outFragList = new ArrayList<>();

        BoardFragment boardFragment = new BoardFragment();
        ArticleListFragment articleListFragment = new ArticleListFragment();
        ArticleFragment articleFragment = new ArticleFragment();

        boardFragment.setOutPager(outPager);
        boardFragment.setOnCategoryClickedListener(articleListFragment);

        articleListFragment.setOutPager(outPager);
        articleListFragment.setOnArticleListClickedListener(articleFragment);
        articleListFragment.setOnCategoryCLickedListener(articleFragment);

        outFragList.add(boardFragment);
        outFragList.add(articleListFragment);
        outFragList.add(articleFragment);

        GenAdapter oAdapter = new GenAdapter(getSupportFragmentManager(), outFragList);
        outPager.setAdapter(oAdapter);
        outPager.setOffscreenPageLimit(outFragList.size());
        outPager.setPageTransformer(false, new SlideTransformer());
        outPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentOutPage = position;
                OutViewPager.interceptTouch = position != 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
            final Uri uri = intent.getData();
            if (uri != null) {
                if (uri.toString().endsWith("html")) {
                    String path = uri.getPath();
                    StringTokenizer tokenizer = new StringTokenizer(path, "/");

                    tokenizer.nextToken();
                    String board = tokenizer.nextToken();

                    articleListFragment.onAttach((Context) this);
                    articleListFragment.onCreateView(LayoutInflater.from(this), outPager, null);

                    articleFragment.onAttach((Context) this);
                    articleFragment.onCreateView(LayoutInflater.from(this), outPager, null);

                    articleListFragment.onCategoryClicked(new BoardInfo("/bbs/" + board + "/index.html", board, "", "", ""));
                    articleFragment.onArticleListClicked(new ArticleInfo("", path, "", "", "", "", "", ""));
                    articleFragment.setRunAnimation(true);
                    outPager.setCurrentItem(2);
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (isShouldHideInput(v, ev)) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (currentOutPage != 0) {
            outPager.setCurrentItem(currentOutPage - 1);
            return;
        } else if (doubleBackClickOnce) {
            super.onBackPressed();
        }

        snackbar.show();
        doubleBackClickOnce = true;
        disposable = Single.timer(BACK_PRESS_CONFIRM_TIME, TimeUnit.MILLISECONDS)
                .doOnSuccess(l -> doubleBackClickOnce = false)
                .subscribe();
    }
}

package study.ian.ptt;

import android.os.Bundle;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import study.ian.ptt.adapter.viewpager.GenAdapter;
import study.ian.ptt.fragment.ArticleListFragment;
import study.ian.ptt.fragment.BoardFragment;
import study.ian.ptt.fragment.TestFragment;
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

    private OutViewPager outPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_TrueDark);
        setContentView(R.layout.activity_main);

        ServiceBuilder.watchNetworkState(this);

        PreManager.initPreManager(getApplicationContext());

        findViews();
        setViews();
    }

    private void findViews() {
        outPager = findViewById(R.id.outPager);
    }

    private void setViews() {
        List<Fragment> outFragList = new ArrayList<>();

        BoardFragment boardFragment = new BoardFragment();
        ArticleListFragment articleListFragment = new ArticleListFragment();
        TestFragment fragment2 = new TestFragment();
        TestFragment fragment3 = new TestFragment();

        boardFragment.setOutPager(outPager);
        boardFragment.setOnCategorySelectedListener(articleListFragment);
        fragment2.setString("2");
        fragment3.setString("3");

        outFragList.add(boardFragment);
        outFragList.add(articleListFragment);
        outFragList.add(fragment2);
        outFragList.add(fragment3);

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
                OutViewPager.interceptTouch = position != 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

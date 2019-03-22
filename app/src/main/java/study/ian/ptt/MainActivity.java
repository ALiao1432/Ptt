package study.ian.ptt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import study.ian.ptt.adapter.GenAdapter;
import study.ian.ptt.fragment.BoardFragment;
import study.ian.ptt.fragment.TestFragment;
import study.ian.ptt.transformer.SlideTransformer;
import study.ian.ptt.util.PreManager;
import study.ian.ptt.view.OutViewPager;

// TODO: 2019-03-20 bug : fix space is not correct issue,
// TODO: 2019-03-21 bug : when display some img will cause scroll not smooth issue

// TODO: 2019-03-21 feature : set app theme color
// TODO: 2019-03-21 feature : add user fav board

// TODO: 2019-03-20 improve : text color

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private PreManager preManager;
    private OutViewPager outPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preManager = new PreManager(this);
        preManager.setAppTheme(R.style.AppTheme_TrueDark, R.layout.activity_main);

        findViews();
        setViews();
    }

    private void findViews() {
        outPager = findViewById(R.id.outPager);
    }

    private void setViews() {
        List<Fragment> outFragList = new ArrayList<>();

        BoardFragment boardFragment = new BoardFragment();
        TestFragment fragment1 = new TestFragment();
        TestFragment fragment2 = new TestFragment();
        TestFragment fragment3 = new TestFragment();

        boardFragment.setOutPager(outPager);
        fragment1.setString("1");
        fragment2.setString("2");
        fragment3.setString("3");

        outFragList.add(boardFragment);
        outFragList.add(fragment1);
        outFragList.add(fragment2);
        outFragList.add(fragment3);

        GenAdapter oAdapter = new GenAdapter(getSupportFragmentManager(), outFragList);
        outPager.setAdapter(oAdapter);
        outPager.setOffscreenPageLimit(outFragList.size());
        outPager.setPageTransformer(true, new SlideTransformer());
    }
}

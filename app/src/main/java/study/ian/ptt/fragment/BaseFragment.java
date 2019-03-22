package study.ian.ptt.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

public class BaseFragment extends Fragment {

    ViewPager outPager;

    public void setOutPager(ViewPager pager) {
        outPager = pager;
    }
}

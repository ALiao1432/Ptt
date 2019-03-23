package study.ian.ptt.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class BaseFragment extends Fragment {

    ViewPager outPager;

    public void setOutPager(ViewPager pager) {
        outPager = pager;
    }
}

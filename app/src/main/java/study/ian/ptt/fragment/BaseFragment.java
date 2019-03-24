package study.ian.ptt.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import study.ian.ptt.util.OnBoardSelectedListener;

public class BaseFragment extends Fragment {

    ViewPager outPager;
    OnBoardSelectedListener onBoardSelectedListener;

    public void setOutPager(ViewPager pager) {
        outPager = pager;
    }

    public void setOnBoardSelectedListener(OnBoardSelectedListener listener) {
        onBoardSelectedListener = listener;
    }
}

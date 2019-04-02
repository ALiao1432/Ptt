package study.ian.ptt.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import study.ian.ptt.util.OnCategorySelectedListener;

public class BaseFragment extends Fragment {

    ViewPager outPager;
    OnCategorySelectedListener onCategorySelectedListener;

    public void setOutPager(ViewPager pager) {
        outPager = pager;
    }

    public void setOnCategorySelectedListener(OnCategorySelectedListener listener) {
        onCategorySelectedListener = listener;
    }
}

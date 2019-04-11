package study.ian.ptt.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import study.ian.ptt.util.OnArticleListClickedListener;
import study.ian.ptt.util.OnCategoryClickedListener;

public class BaseFragment extends Fragment {

    ViewPager outPager;
    OnCategoryClickedListener onCategoryClickedListener;
    OnArticleListClickedListener onArticleListClickedListener;

    public void setOutPager(ViewPager pager) {
        outPager = pager;
    }

    public void setOnCategoryClickedListener(OnCategoryClickedListener listener) {
        onCategoryClickedListener = listener;
    }

    public void setOnArticleListClickedListener(OnArticleListClickedListener listener) {
        onArticleListClickedListener = listener;
    }
}

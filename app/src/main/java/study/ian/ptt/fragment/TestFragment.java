package study.ian.ptt.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import study.ian.ptt.R;

public class TestFragment extends Fragment {

    private String string;
    private ViewPager outPager;

    public void setString(String s) {
        this.string = s;
    }

    public void setOutPager(ViewPager pager) {
        outPager = pager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment, container, false);
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(string);
        textView.setTextSize(400);
        if (outPager != null) {
            textView.setOnClickListener(v -> outPager.setCurrentItem(1));
        }
        return view;
    }
}

package study.ian.ptt.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import study.ian.ptt.R;

public class FavFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fav, container, false);
        TextView textView = view.findViewById(R.id.favText);
        textView.setOnClickListener(v -> outPager.setCurrentItem(1));
        return view;
    }
}

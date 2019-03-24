package study.ian.ptt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import study.ian.ptt.R;
import study.ian.ptt.util.OnBoardSelectedListener;

public class ArticleListFragment extends BaseFragment implements OnBoardSelectedListener {

    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_article_list, container, false);
        textView = view.findViewById(R.id.boardText);
        textView.setTextSize(50);
        return view;
    }

    @Override
    public void onBoardSelected(String board) {
        textView.setText(board);
    }
}

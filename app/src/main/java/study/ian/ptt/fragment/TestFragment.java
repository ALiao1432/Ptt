package study.ian.ptt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import study.ian.ptt.R;

public class TestFragment extends BaseFragment {

    private final String TAG = "TestFragment";

    private String string;

    public void setString(String s) {
        this.string = s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment, container, false);
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(string);
        textView.setTextSize(400);
        return view;
    }
}

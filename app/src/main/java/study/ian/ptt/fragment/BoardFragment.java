package study.ian.ptt.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import study.ian.ptt.R;
import study.ian.ptt.adapter.viewpager.GenAdapter;

public class BoardFragment extends BaseFragment {

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_board, container, false);
        ViewPager viewPager = view.findViewById(R.id.boardViewPager);

        List<Fragment> fragmentList = new ArrayList<>();

        FavFragment favFragment = new FavFragment();
        HotFragment hotFragment = new HotFragment();
        PttClassFragment pttClassFragment = new PttClassFragment();

        favFragment.setOutPager(outPager);

        fragmentList.add(favFragment);
        fragmentList.add(hotFragment);
        fragmentList.add(pttClassFragment);

        viewPager.setAdapter(new GenAdapter(((FragmentActivity) context).getSupportFragmentManager(), fragmentList));

        return view;
    }
}

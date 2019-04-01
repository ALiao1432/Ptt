package study.ian.ptt.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import study.ian.ptt.R;
import study.ian.ptt.util.OnBoardSelectedListener;

public class ArticleListFragment extends BaseFragment implements OnBoardSelectedListener {

    private final String TAG = "ArticleListFragment";

    private TextView boardTitleText;
    private ValueAnimator alphaAnimator;
    private ValueAnimator scaleAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_article_list, container, false);
        boardTitleText = view.findViewById(R.id.boardTitleText);
        boardTitleText.setTextSize(50);

        alphaAnimator = ValueAnimator.ofFloat(1, 0);
        alphaAnimator.setDuration(1000);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());
        alphaAnimator.addUpdateListener(animation -> boardTitleText.setAlpha((float) animation.getAnimatedValue()));

        scaleAnimator = ValueAnimator.ofFloat(1, 1.5f);
        scaleAnimator.setDuration(1000);
        scaleAnimator.setInterpolator(new DecelerateInterpolator());
        scaleAnimator.addUpdateListener(animation -> {
            boardTitleText.setScaleX((float) animation.getAnimatedValue());
            boardTitleText.setScaleY((float) animation.getAnimatedValue());
        });
        scaleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                boardTitleText.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return view;
    }

    @Override
    public void onBoardSelected(String board) {
        boardTitleText.setVisibility(View.VISIBLE);
        boardTitleText.setText(board);
        alphaAnimator.start();
        scaleAnimator.start();
    }
}

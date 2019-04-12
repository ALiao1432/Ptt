package study.ian.ptt.adapter.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import study.ian.morphviewlib.MorphView;
import study.ian.ptt.R;
import study.ian.ptt.model.board.BoardInfo;
import study.ian.ptt.util.CountTextConverter;
import study.ian.ptt.util.OnCategoryClickedListener;
import study.ian.ptt.util.OnPageReloadRequestListener;
import study.ian.ptt.util.PreManager;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardHolder> {

    private final String TAG = "BoardAdapter";

    private final ViewPager outPager;
    private OnCategoryClickedListener onCategoryClickedListener;
    private OnPageReloadRequestListener onPageReloadRequestListener;
    private List<BoardInfo> infoList = new ArrayList<>();
    private final Resources resources;
    private final PreManager preManager;

    public BoardAdapter(Context context, ViewPager pager) {
        resources = context.getResources();
        preManager = PreManager.getInstance();
        outPager = pager;
    }

    public void setOnCategoryClickedListener(OnCategoryClickedListener listener) {
        onCategoryClickedListener = listener;
    }

    public void setOnPageReloadRequestListener(OnPageReloadRequestListener listener) {
        onPageReloadRequestListener = listener;
    }

    public void setResults(List<BoardInfo> list) {
        infoList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BoardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_board, parent, false);
        return new BoardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardHolder holder, int position) {
        BoardInfo info = infoList.get(position);

        RxView.clicks(holder.boardCard)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .doOnNext(unit -> {
                    if (info.getSort() == BoardInfo.SORT_BOARD) {
                        if (onCategoryClickedListener != null) {
                            onCategoryClickedListener.onCategoryClicked(info.getName());
                        }
                        outPager.setCurrentItem(1);
                    } else if (info.getSort() == BoardInfo.SORT_CLASS) {
                        if (onPageReloadRequestListener != null) {
                            onPageReloadRequestListener.onPageReloadRequest(info.getHref().substring(5));
                        }
                    }
                })
                .doOnError(t -> Log.d(TAG, "onBindViewHolder: click board card error : " + t))
                .subscribe();

        holder.userNumText.setText(CountTextConverter.getUserCountText(info.getUserCount()));
        holder.userNumText.setTextColor(CountTextConverter.getUserCountColor(resources, info.getUserCount()));
        holder.categoryNameText.setText(info.getName());
        holder.categoryTitleText.setText(info.getBoardTitle());

        if (info.getSort() == BoardInfo.SORT_BOARD) {
            holder.favView.setVisibility(View.VISIBLE);
            setFavViewPaint(info.getName(), holder.favView);

            RxView.clicks(holder.favView)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .doOnNext(unit -> {
                        preManager.toggleFavBoard(info.getName().trim());
                        setFavViewPaint(info.getName(), holder.favView);
                    })
                    .doOnError(t -> Log.d(TAG, "onBindViewHolder: click fav error : " + t))
                    .subscribe();
        } else if (info.getSort() == BoardInfo.SORT_CLASS) {
            holder.favView.setVisibility(View.INVISIBLE);
        }
    }

    private void setFavViewPaint(String board, MorphView view) {
        if (preManager.isFavBoard(board)) {
            view.setPaintStyle(Paint.Style.FILL);
        } else {
            view.setPaintStyle(Paint.Style.STROKE);
        }
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    class BoardHolder extends RecyclerView.ViewHolder {

        private final CardView boardCard;
        private final TextView userNumText;
        private final TextView categoryNameText;
        private final TextView categoryTitleText;
        private final MorphView favView;

        BoardHolder(@NonNull View v) {
            super(v);

            boardCard = v.findViewById(R.id.boardCard);
            userNumText = v.findViewById(R.id.userNumText);
            categoryNameText = v.findViewById(R.id.categoryNameText);
            categoryTitleText = v.findViewById(R.id.categoryInfoText);
            favView = v.findViewById(R.id.favView);

            favView.setCurrentId(R.drawable.vd_fav);
            favView.setPaintWidth(4);
            favView.setSize(300, 300);
            favView.setPaintColor(ResourcesCompat.getColor(resources, R.color.fav_view_color, null));
        }
    }
}

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import study.ian.morphviewlib.MorphView;
import study.ian.ptt.R;
import study.ian.ptt.model.board.BoardInfo;
import study.ian.ptt.util.PreManager;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardHolder> {

    private final String TAG = "BoardAdapter";

    private List<BoardInfo> infoList = new ArrayList<>();
    private Resources resources;
    private PreManager preManager;

    public BoardAdapter(Context context) {
        resources = context.getResources();
        preManager = new PreManager(context);
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
        holder.userNumText.setText(getUserNumText(info.getUserCount()));
        holder.userNumText.setTextColor(getUserNumColor(info.getUserCount()));
        holder.boardNameText.setText(info.getName());
        holder.boardTitleText.setText(info.getBoardTitle());

        holder.favView.setCurrentId(R.drawable.vd_fav);
        holder.favView.setPaintWidth(4);
        holder.favView.setSize(300, 300);
        holder.favView.setPaintColor(ResourcesCompat.getColor(resources, R.color.fav_view_color, null));

        if (preManager.isFavBoard(info.getName())) {
            holder.favView.setPaintStyle(Paint.Style.FILL);
        } else {
            holder.favView.setPaintStyle(Paint.Style.STROKE);
        }

        RxView.clicks(holder.favView)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .doOnNext(unit -> {
                    preManager.toggleFavBoard(info.getName());
                    if (preManager.isFavBoard(info.getName())) {
                        holder.favView.setPaintStyle(Paint.Style.FILL);
                        holder.favView.postInvalidate();
                    } else {
                        holder.favView.setPaintStyle(Paint.Style.STROKE);
                        holder.favView.postInvalidate();
                    }
                })
                .doOnError(t -> Log.d(TAG, "onBindViewHolder: click fav error : " + t))
                .subscribe();
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    @NotNull
    private String getUserNumText(int count) {
        if (count <= 99) {
            return String.valueOf(count);
        } else if (count <= 999) {
            return "HOT";
        } else {
            return "爆";
        }
    }

    private int getUserNumColor(int count) {
        if (count == 0) {
            return ResourcesCompat.getColor(resources, R.color.userNum_0, null);
        } else if (count > 0 && count <= 10) {
            return ResourcesCompat.getColor(resources, R.color.userNum_1_10, null);
        } else if (count >= 11 && count <= 49) {
            return ResourcesCompat.getColor(resources, R.color.userNum_11_49, null);
        } else if (count >= 50 && count <= 99) {
            return ResourcesCompat.getColor(resources, R.color.userNum_50_99, null);
        } else if (count >= 100 && count <= 999) {
            return ResourcesCompat.getColor(resources, R.color.userNum_100_999, null);
        } else if (count >= 1000 && count <= 1999) {
            return ResourcesCompat.getColor(resources, R.color.userNum_1000_1999, null);
        } else if (count >= 2000 && count <= 4999) {
            return ResourcesCompat.getColor(resources, R.color.userNum_2000_4999, null);
        } else if (count >= 5000 && count <= 9999) {
            return ResourcesCompat.getColor(resources, R.color.userNum_5000_9999, null);
        } else if (count >= 10000 && count <= 29999) {
            return ResourcesCompat.getColor(resources, R.color.userNum_10000_29999, null);
        } else if (count >= 30000 && count <= 59999) {
            return ResourcesCompat.getColor(resources, R.color.userNum_30000_59999, null);
        } else if (count >= 60000 && count <= 99999) {
            return ResourcesCompat.getColor(resources, R.color.userNum_60000_99999, null);
        } else if (count >= 100000) {
            return ResourcesCompat.getColor(resources, R.color.userNum_100000, null);
        } else {
            return ResourcesCompat.getColor(resources, R.color.userNum_0, null);
        }
    }

    class BoardHolder extends RecyclerView.ViewHolder {

        private TextView userNumText;
        private TextView boardNameText;
        private TextView boardTitleText;
        private MorphView favView;


        BoardHolder(@NonNull View v) {
            super(v);

            userNumText = v.findViewById(R.id.userNumText);
            boardNameText = v.findViewById(R.id.boardNameText);
            boardTitleText = v.findViewById(R.id.boardTitleText);
            favView = v.findViewById(R.id.favView);
        }
    }
}

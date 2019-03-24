package study.ian.ptt.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import study.ian.ptt.R;
import study.ian.ptt.adapter.recyclerview.BoardAdapter;
import study.ian.ptt.model.board.BoardInfo;
import study.ian.ptt.util.PreManager;

public class FavFragment extends BaseFragment {

    private final String TAG = "FavFragment";

    private Context context;
    private PreManager preManager;
    private SwipeRefreshLayout favRefreshLayout;
    private RecyclerView favRecyclerView;
    private BoardAdapter boardAdapter;
    private List<BoardInfo> favList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        preManager = new PreManager(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fav, container, false);

        findViews(v);
        setViews();
        return v;
    }

    private void findViews(View view) {
        favRecyclerView = view.findViewById(R.id.recyclerViewFav);
        favRefreshLayout = view.findViewById(R.id.refreshLayoutFav);
    }

    private void setViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        boardAdapter = new BoardAdapter(context, outPager);
        boardAdapter.setOnBoardSelectedListener(onBoardSelectedListener);
        boardAdapter.setPreManager(preManager);

        favRecyclerView.setLayoutManager(layoutManager);
        favRecyclerView.setAdapter(boardAdapter);

        favRefreshLayout.setOnRefreshListener(this::loadData);
        loadData();
    }

    private void loadData() {
        favRefreshLayout.setRefreshing(true);

        favList = preManager.getFavBoardSet()
                .stream()
                .map(board -> new BoardInfo("/bbs/" + board + "/index.html", board, "", "", 0))
                .sorted((info1, info2) -> info1.getName().charAt(0) - info2.getName().charAt(0))
                .collect(Collectors.toList());
        boardAdapter.setResults(favList);

        favRefreshLayout.setRefreshing(false);
    }
}

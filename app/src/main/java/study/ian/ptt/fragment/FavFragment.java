package study.ian.ptt.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.nodes.Document;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Response;
import study.ian.ptt.R;
import study.ian.ptt.adapter.recyclerview.BoardAdapter;
import study.ian.ptt.model.board.Board;
import study.ian.ptt.service.PttService;
import study.ian.ptt.service.ServiceBuilder;
import study.ian.ptt.util.ObserverHelper;
import study.ian.ptt.util.PreManager;

public class FavFragment extends BaseFragment {

    private final String TAG = "FavFragment";

    private Context context;
    private PreManager preManager;
    private SwipeRefreshLayout favRefreshLayout;
    private RecyclerView favRecyclerView;
    private BoardAdapter boardAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_hot, container, false);

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

        favRecyclerView.setLayoutManager(layoutManager);
        favRecyclerView.setAdapter(boardAdapter);

        favRefreshLayout.setOnRefreshListener(this::loadData);
        loadData();
    }

    private void loadData() {
        favRefreshLayout.setRefreshing(true);


    }

    private void configData(Document doc) {
        Board board = new Board(doc);
        boardAdapter.setResults(board.getInfoList());
        favRefreshLayout.setRefreshing(false);
    }
}

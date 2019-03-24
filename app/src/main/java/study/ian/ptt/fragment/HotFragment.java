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

public class HotFragment extends BaseFragment {

    private final String TAG = "HotFragment";

    private Context context;
    private SwipeRefreshLayout hotRefreshLayout;
    private RecyclerView hotRecyclerView;
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
        hotRecyclerView = view.findViewById(R.id.recyclerViewHot);
        hotRefreshLayout = view.findViewById(R.id.refreshLayoutHot);
    }

    private void setViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        boardAdapter = new BoardAdapter(context, outPager);
        boardAdapter.setOnBoardSelectedListener(onBoardSelectedListener);

        hotRecyclerView.setLayoutManager(layoutManager);
        hotRecyclerView.setAdapter(boardAdapter);

        hotRefreshLayout.setOnRefreshListener(this::loadData);
        loadData();
    }

    private void loadData() {
        hotRefreshLayout.setRefreshing(true);

        ServiceBuilder.getService(PttService.class)
                .getHotBoard()
                .compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(this::configData)
                .doOnError(t -> Log.d(TAG, "setViews: get hot board error : " + t))
                .subscribe();
    }

    private void configData(Document doc) {
        Board board = new Board(doc);
        boardAdapter.setResults(board.getInfoList());
        hotRefreshLayout.setRefreshing(false);
    }

}

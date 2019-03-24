package study.ian.ptt.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.nodes.Document;

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
import study.ian.ptt.util.OnPageReloadRequestListener;

public class PttClassFragment extends BaseFragment implements OnPageReloadRequestListener {

    private final String TAG = "PttClassFragment";

    private Context context;
    private SwipeRefreshLayout pttClassRefreshLayout;
    private RecyclerView pttClassRecyclerView;
    private BoardAdapter boardAdapter;
    private String classPath = "1";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_ptt_class, container, false);

        findViews(v);
        setViews();
        return v;
    }

    private void findViews(View view) {
        pttClassRecyclerView = view.findViewById(R.id.recyclerViewPttClass);
        pttClassRefreshLayout = view.findViewById(R.id.refreshLayoutPttClass);
    }

    private void setViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        boardAdapter = new BoardAdapter(context, outPager);
        boardAdapter.setOnBoardSelectedListener(onBoardSelectedListener);
        boardAdapter.setOnPageReloadRequestListener(this);

        pttClassRecyclerView.setLayoutManager(layoutManager);
        pttClassRecyclerView.setAdapter(boardAdapter);

        pttClassRefreshLayout.setOnRefreshListener(() -> loadData(classPath));
        loadData(classPath);
    }

    private void loadData(String classPath) {
        pttClassRefreshLayout.setRefreshing(true);

        ServiceBuilder.getService(PttService.class)
                .getPttClass(classPath)
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
        pttClassRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPageReloadRequest(String reloadPath) {
        classPath = reloadPath;
        loadData(classPath);
    }
}

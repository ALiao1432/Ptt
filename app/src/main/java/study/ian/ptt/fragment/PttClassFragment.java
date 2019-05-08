package study.ian.ptt.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.nodes.Document;

import java.util.Stack;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Response;
import study.ian.ptt.R;
import study.ian.ptt.adapter.recyclerview.BoardAdapter;
import study.ian.ptt.model.board.Board;
import study.ian.ptt.model.board.BoardInfo;
import study.ian.ptt.service.ServiceBuilder;
import study.ian.ptt.util.ObserverHelper;
import study.ian.ptt.util.OnPageReloadRequestListener;
import study.ian.ptt.util.PreManager;

public class PttClassFragment extends BaseFragment implements OnPageReloadRequestListener, PreManager.OnFavActionListener {

    private final String TAG = "PttClassFragment";

    private Context context;
    private SwipeRefreshLayout pttClassRefreshLayout;
    private RecyclerView pttClassRecyclerView;
    private FloatingActionButton pttClassFab;
    private BoardAdapter boardAdapter;
    private Board board;
    private String classPath = "1";
    private String currentPath;
    private final Stack<String> pageStack = new Stack<>();
    private final PublishSubject<String> pageStackSubject = PublishSubject.create();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PreManager preManager = PreManager.getInstance();
        preManager.setOnFavActionListener(this);

        View v = inflater.inflate(R.layout.layout_ptt_class, container, false);

        findViews(v);
        setViews();
        return v;
    }

    private void findViews(View v) {
        pttClassRecyclerView = v.findViewById(R.id.recyclerViewPttClass);
        pttClassRefreshLayout = v.findViewById(R.id.refreshLayoutPttClass);
        pttClassFab = v.findViewById(R.id.pttClassFab);
    }

    private void setViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        boardAdapter = new BoardAdapter(context, outPager);
        boardAdapter.setOnCategoryClickedListener(onCategoryClickedListener);
        boardAdapter.setOnPageReloadRequestListener(this);

        pttClassRecyclerView.setLayoutManager(layoutManager);
        pttClassRecyclerView.setHasFixedSize(true);
        pttClassRecyclerView.setAdapter(boardAdapter);

        pttClassRefreshLayout.setOnRefreshListener(() -> loadData(classPath));

        pttClassFab.hide();
        pttClassFab.setOnClickListener(v -> {
            if (!pageStack.isEmpty()) {
                classPath = pageStack.peek();
                loadData(pageStack.pop());
            }
        });

        pageStackSubject.doOnNext(path -> toggleFabState())
                .doOnError(t -> Log.d(TAG, "setViews: toggle fab state error : " + t))
                .subscribe();

        currentPath = classPath;
        loadData(classPath);
    }

    private void toggleFabState() {
        if (pageStack.size() == 0) {
            pttClassFab.hide();
        } else {
            pttClassFab.show();
        }
    }

    private void loadData(String classPath) {
        pttClassRefreshLayout.setRefreshing(true);
        pageStackSubject.onNext(classPath);
        currentPath = classPath;

        ServiceBuilder.getPttService()
                .getPttClass(classPath)
                .compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(this::configData)
                .doOnError(t -> Log.d(TAG, "setViews: get hot board error : " + t))
                .subscribe();
    }

    private void configData(Document doc) {
        board = new Board(doc);
        boardAdapter.setResults(board.getInfoList());
        pttClassRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPageReloadRequest(String reloadPath) {
        classPath = reloadPath;
        pageStack.push(currentPath);
        loadData(classPath);
    }

    private void reloadItem(int changeIndex) {
        boardAdapter.notifyItemChanged(changeIndex);
        pttClassRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFavAction(String b, int action) {
        pttClassRefreshLayout.setRefreshing(true);
        int id = board.getInfoList()
                .stream()
                .map(BoardInfo::getName)
                .collect(Collectors.toList())
                .indexOf(b);

        reloadItem(id);
    }
}

package study.ian.ptt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import retrofit2.Response;
import study.ian.ptt.model.article.Article;
import study.ian.ptt.model.category.Category;
import study.ian.ptt.model.search.Search;
import study.ian.ptt.service.PttService;
import study.ian.ptt.service.ServiceBuilder;
import study.ian.ptt.util.ObserverHelper;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        textView.setTextIsSelectable(true);
        textView.setTextSize(10);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setTextColor(Color.parseColor("#CCCCCC"));

        PttService pttService = ServiceBuilder.getService(PttService.class);

        pttService.getCategory("over18=1", "NBA")
                .compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(doc -> {
                    Category category = new Category(doc);
                    Log.d(TAG, "onCreate: category : " + category);
                })
                .doOnError(t -> Log.d(TAG, "onCreate: t : " + t))
                .subscribe();

        pttService.getArticle("over18=1", "/bbs/NBA/M.1552791764.A.A82.html")
//                .getArticle("//bbs/Isayama/M.1552408321.A.CC0.html")
//                .getArticle("/bbs/NARUTO/M.1552885525.A.F15.html")
//                .getArticle("/bbs/NBA/M.1552702011.A.0B3.html")
//                .getArticle("/bbs/asciiart/M.1542412240.A.D0A.html")
                .compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(doc -> {
                    final Article article = new Article(doc);
                    Log.d(TAG, "onCreate: article : " + article.getMainContent());
                    textView.setText(article.getMainContent());
                })
                .doOnError(t -> Log.d(TAG, "onCreate: t : " + t))
                .subscribe();

        pttService.getSearchResult("over18=1", "NBA", "kobe", 1)
                .compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(doc -> {
                    final Search search = new Search(doc);
                    Log.d(TAG, "onCreate: search : " + search.getArticleInfoList());
                })
                .doOnError(t -> Log.d(TAG, "onCreate: t : " + t))
                .subscribe();
    }
}

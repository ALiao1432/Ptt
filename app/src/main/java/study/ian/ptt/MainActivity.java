package study.ian.ptt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import retrofit2.Response;
import study.ian.ptt.model.article.Article;
import study.ian.ptt.service.PttService;
import study.ian.ptt.service.ServiceBuilder;
import study.ian.ptt.util.GlideImageGetter;
import study.ian.ptt.util.ObserverHelper;

// TODO: 2019-03-20 fix space is not correct issue, 
// TODO: 2019-03-20 improve color

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView.setTextIsSelectable(true);
        textView.setTextSize(10);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setTextColor(Color.parseColor("#CCCCCC"));

        PttService pttService = ServiceBuilder.getService(PttService.class);

//        pttService.getCategory(ServiceBuilder.COOKIE, "NBA")
//                .compose(ObserverHelper.applyHelper())
//                .filter(r -> r.code() == 200)
//                .map(Response::body)
//                .doOnNext(doc -> {
//                    Category category = new Category(doc);
//                    Log.d(TAG, "onCreate: category : " + category);
//                })
//                .doOnError(t -> Log.d(TAG, "onCreate: t : " + t))
//                .subscribe();

        pttService
//                .getArticle(ServiceBuilder.COOKIE, "/bbs/NBA/M.1552791764.A.A82.html")
//                .getArticle(ServiceBuilder.COOKIE, "//bbs/Isayama/M.1552408321.A.CC0.html")
//                .getArticle(ServiceBuilder.COOKIE, "/bbs/NARUTO/M.1552885525.A.F15.html")
//                .getArticle(ServiceBuilder.COOKIE,"/bbs/NBA/M.1552702011.A.0B3.html")
//                .getArticle(ServiceBuilder.COOKIE, "/bbs/beauty/M.1552897508.A.E34.html")
                .getArticle(ServiceBuilder.COOKIE, "/bbs/Beauty/M.1552929594.A.5CE.html")
//                .getArticle(ServiceBuilder.COOKIE, "/bbs/asciiart/M.1542412240.A.D0A.html")
                .compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(doc -> {
                    final Article article = new Article(doc);
                    Log.d(TAG, "onCreate: article : " + article.getMainContent());
                    textView.setText(HtmlCompat.fromHtml(article.getMainContent(), HtmlCompat.FROM_HTML_MODE_LEGACY, new GlideImageGetter(textView), null));
                })
                .doOnError(t -> Log.d(TAG, "onCreate: t : " + t))
                .subscribe();

//        pttService.getSearchResult(ServiceBuilder.COOKIE, "NBA", "kobe", 1)
//                .compose(ObserverHelper.applyHelper())
//                .filter(r -> r.code() == 200)
//                .map(Response::body)
//                .doOnNext(doc -> {
//                    final Search search = new Search(doc);
//                    Log.d(TAG, "onCreate: search : " + search.getArticleInfoList());
//                })
//                .doOnError(t -> Log.d(TAG, "onCreate: t : " + t))
//                .subscribe();
    }
}

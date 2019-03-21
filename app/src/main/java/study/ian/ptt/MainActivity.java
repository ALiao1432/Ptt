package study.ian.ptt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import retrofit2.Response;
import study.ian.ptt.model.article.Article;
import study.ian.ptt.service.PttService;
import study.ian.ptt.service.ServiceBuilder;
import study.ian.ptt.util.ObserverHelper;
import study.ian.ptt.util.SpanUtil;

// TODO: 2019-03-20 fix space is not correct issue, 
// TODO: 2019-03-20 improve color
// TODO: 2019-03-21 when display some img will cause scroll not smooth issue

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView.setTextIsSelectable(true);
        textView.setTextSize(14);
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

        pttService
                .getArticle(ServiceBuilder.COOKIE, "/bbs/NBA/M.1552791764.A.A82.html")
//                .getArticle(ServiceBuilder.COOKIE, "//bbs/Isayama/M.1552408321.A.CC0.html")
//                .getArticle(ServiceBuilder.COOKIE, "/bbs/NARUTO/M.1552885525.A.F15.html")
//                .getArticle(ServiceBuilder.COOKIE,"/bbs/NBA/M.1552702011.A.0B3.html")
//                .getArticle(ServiceBuilder.COOKIE,"/bbs/Beauty/M.1549815861.A.ED4.html")
//                .getArticle(ServiceBuilder.COOKIE,"/bbs/Gossiping/M.1553136592.A.41E.html")
//                .getArticle(ServiceBuilder.COOKIE, "/bbs/beauty/M.1552897508.A.E34.html")
//                .getArticle(ServiceBuilder.COOKIE, "/bbs/Beauty/M.1552929594.A.5CE.html")
//                .getArticle(ServiceBuilder.COOKIE, "/bbs/asciiart/M.1542412240.A.D0A.html")
                .compose(ObserverHelper.applyHelper())
                .filter(r -> r.code() == 200)
                .map(Response::body)
                .doOnNext(doc -> {
                    final Article article = new Article(doc);
                    String tets = article.getMainContent();
                    Log.d(TAG, "onCreate: article.getMainContent : " + tets);
                    Spannable spannable = SpanUtil.getSpanned(textView, article.getMainContent());
                    SpanUtil.setImageClickListener(spannable, imageSpan -> Log.d(TAG, "onImageClick: testt click : " + imageSpan.getSource()));
                    textView.setText(spannable);
                })
                .doOnError(t -> Log.d(TAG, "onCreate: t : " + t))
                .subscribe();
    }
}

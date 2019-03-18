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
import study.ian.ptt.util.ObserverHelper;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        textView.setTextIsSelectable(true);
        textView.setTextSize(6);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setTextColor(Color.parseColor("#CCCCCC"));

//        ServiceBuilder.getService(PttService.class)
//                .getCategory("NBA")
//                .compose(ObserverHelper.applyHelper())
//                .filter(r -> r.code() == 200)
//                .map(Response::body)
//                .doOnNext(doc -> {
//                        Log.d(TAG, "onCreate: category : " + doc);
//                    Category category = new Category(doc);
//                    Log.d(TAG, "onCreate: category : " + category.getArticleInfoList());
//                })
//                .doOnError(t -> Log.d(TAG, "onCreate: t : " + t))
//                .subscribe();

        ServiceBuilder.getService(PttService.class)
                .getArticle("/bbs/NBA/M.1552791764.A.A82.html")
//                .getArticle("//bbs/Isayama/M.1552408321.A.CC0.html")
//                .getArticle("/bbs/NBA/M.1552702011.A.0B3.html")
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
    }
}

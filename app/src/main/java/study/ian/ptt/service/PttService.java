package study.ian.ptt.service;

import org.jsoup.nodes.Document;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PttService {

    @GET("{category}/index.html")
    Observable<Response<Document>> getCategory(
            @Path("category") String category
    );

    @GET("/{articlePath}")
    Observable<Response<Document>> getArticle(
            @Path("articlePath") String articlePath
    );
}

package study.ian.ptt.service;

import org.jsoup.nodes.Document;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PttService {

    @GET("{category}/index.html")
    Observable<Response<Document>> getCategory(
            @Header("Cookie") String cookie,
            @Path("category") String category
    );

    @GET("/{articlePath}")
    Observable<Response<Document>> getArticle(
            @Header("Cookie") String cookie,
            @Path("articlePath") String articlePath
    );

    @GET("{category}/search")
    Observable<Response<Document>> getSearchResult(
            @Header("Cookie") String cookie,
            @Path("category") String category,
            @Query("q") String query,
            @Query("page") int page
    );
}

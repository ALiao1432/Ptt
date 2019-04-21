package study.ian.ptt.service;

import org.jsoup.nodes.Document;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import study.ian.ptt.model.poll.LongPoll;
import study.ian.ptt.model.poll.Poll;

public interface PttService {

    @GET("{categoryPath}")
    Observable<Response<Document>> getCategory(
            @Header("Cookie") String cookie,
            @Path("categoryPath") String categoryPath
    );

    @GET("/{articlePath}")
    Observable<Response<Document>> getArticle(
            @Header("Cookie") String cookie,
            @Path("articlePath") String articlePath
    );

    @GET
    Observable<Response<Document>> getSearchResult(
            @Url String searchUrl,
            @Header("Cookie") String cookie
    );

    @GET("/cls/{classPath}")
    Observable<Response<Document>> getPttClass(
            @Path("classPath") String classPath
    );

    @GET("/bbs/hotboards.html")
    Observable<Response<Document>> getHotBoard();

    @GET
    Observable<Response<LongPoll>> getLongPoll(
            @Url String longPollUrl,
            @Header("Cookie") String cookie
    );

    @GET
    Observable<Response<Poll>> getPoll(
            @Url String pollUri,
            @Header("Cookie") String cookie,
            @Query("cacheKey") String cacheKey,
            @Query("offset") String offset,
            @Query("offset-sig") String offsetSig,
            @Query("size") String size,
            @Query("size-sig") String sizeSig
    );
}

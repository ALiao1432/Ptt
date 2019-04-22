package study.ian.ptt.service;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.processors.PublishProcessor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import study.ian.networkstateutil.ConnectionType;
import study.ian.networkstateutil.NetworkStateChangeListener;
import study.ian.networkstateutil.NetworkStateUtil;
import study.ian.ptt.util.JsoupConverter;

public class ServiceBuilder {
    public final static String API_BASE_URL = "https://www.ptt.cc/bbs/";
    public final static String API_POLL_URL = "https://www.ptt.cc";
    public final static String SEARCH_AUTHOR = "author:";
    public final static String SEARCH_PUSH = "recommend:";
    public final static String COOKIE = "over18=1";
    public final static long LONG_POLL_TIMEOUT = 28L;
    public final static int POLL_STATE_LOADING = 0;
    public final static int POLL_STATE_IDLE = 1;
    private final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(JsoupConverter.FACTORY)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    private final static PublishProcessor<ConnectionType> connectionTypeProcessor = PublishProcessor.create();
    private static PttService pttService = null;

    public static void watchNetworkState(Context context) {
        new NetworkStateUtil(context, new NetworkStateChangeListener() {
            @Override
            public void onConnected(ConnectionType connectionType) {
                connectionTypeProcessor.onNext(connectionType);
            }

            @Override
            public void onDisconnected() {

            }
        });
    }

    @NotNull
    public static <T> T getService(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    public static PttService getPttService() {
        if (pttService == null) {
            pttService = retrofit.create(PttService.class);
        }
        return pttService;
    }

    public static Observable<ConnectionType> getConnectStateObservable() {
        return connectionTypeProcessor.toObservable();
    }
}

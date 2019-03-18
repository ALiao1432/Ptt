package study.ian.ptt.service;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.processors.PublishProcessor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import study.ian.networkstateutil.ConnectionType;
import study.ian.networkstateutil.NetworkStateChangeListener;
import study.ian.networkstateutil.NetworkStateUtil;
import study.ian.ptt.util.JsoupConverter;

public class ServiceBuilder {

    private final static String API_BASE_URL = "https://www.ptt.cc/bbs/";
    private final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(JsoupConverter.FACTORY)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    private final static PublishProcessor<ConnectionType> connectionTypeProcessor = PublishProcessor.create();

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

    public static Observable<ConnectionType> getConnectStateObservable() {
        return connectionTypeProcessor.toObservable();
    }
}

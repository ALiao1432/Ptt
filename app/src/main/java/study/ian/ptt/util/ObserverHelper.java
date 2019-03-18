package study.ian.ptt.util;

import org.jetbrains.annotations.Contract;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import study.ian.ptt.service.ServiceBuilder;

public class ObserverHelper {

    private static final ObservableTransformer observableHelper =
            upstream -> upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(t -> ServiceBuilder.getConnectStateObservable());

    @Contract(pure = true)
    public static <T> ObservableTransformer<T, T> applyHelper() {
        return (ObservableTransformer<T, T>) observableHelper;
    }
}

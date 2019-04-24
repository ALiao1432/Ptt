package study.ian.ptt.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoggingInterceptor implements Interceptor {

    private final String TAG = "LoggingInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long sendTime = System.nanoTime();
        Log.d(TAG, "intercept: send request at " + sendTime + " , url : " + request.url());

        Response response = chain.proceed(request);

        long responseTime = System.nanoTime();
        Log.d(TAG, "intercept: response at " + responseTime + " , url : " + response.request().url());

        return response;
    }
}

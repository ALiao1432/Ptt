package study.ian.ptt.util;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class JsoupConverter implements Converter<ResponseBody, Document> {

    public static final Factory FACTORY = new Factory() {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (type == Document.class)
                return new JsoupConverter();
            return null;
        }
    };

    @Override
    public Document convert(@NotNull ResponseBody value) throws IOException {
        return Jsoup.parse(value.string());
    }
}

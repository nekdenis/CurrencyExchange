package nekdenis.github.com.exchangerates.di;

import java.util.concurrent.TimeUnit;

import nekdenis.github.com.exchangerates.BuildConfig;
import nekdenis.github.com.exchangerates.api.Api;
import nekdenis.github.com.exchangerates.util.Constants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Just a simple Injector that should be replaced with dagger2 in production
 */
public class Injector {

    private static Api service;
    private static OkHttpClient okHttpClient;

    public static Api provideService() {
        if (service == null) {
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(provideHttpClient())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            service = restAdapter.create(Api.class);
        }

        return service;

    }

    private static OkHttpClient provideHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.readTimeout(Constants.WEB_SERVICE_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClientBuilder.connectTimeout(Constants.WEB_SERVICE_TIMEOUT, TimeUnit.MILLISECONDS);
            if (BuildConfig.DEBUG) {
                okHttpClientBuilder.interceptors().add(provideLogInterceptor());
            }
            okHttpClient = okHttpClientBuilder.build();
        }
        return okHttpClient;
    }

    private static Interceptor provideLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    public static Scheduler provideIoScheduler() {
        return Schedulers.io();
    }

    public static Scheduler provideUIScheduler() {
        return AndroidSchedulers.mainThread();
    }
}

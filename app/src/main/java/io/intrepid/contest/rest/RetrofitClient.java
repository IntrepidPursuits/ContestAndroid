package io.intrepid.contest.rest;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.intrepid.contest.BuildConfig;
import io.intrepid.contest.settings.UserSettings;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class RetrofitClient {

    private static final String ACCEPT_APPLICATION = "vnd.judgy-server.herokuapp.com";
    private static final String BASE_URL = "http://judgy-server.herokuapp.com/api/";
    private static final int CONNECTION_TIMEOUT = 30;
    private static final int API_VERSION = 1;
    private static RestApi restApi;

    private RetrofitClient() {
    }

    public static void init(UserSettings userSettings) {
        restApi = createRestApi(BASE_URL, userSettings);
    }

    public static RestApi getApi() {
        return restApi;
    }

    @VisibleForTesting
    static RestApi getTestApi(String baseUrl, @NonNull UserSettings userSettings) {
        return createRestApi(baseUrl, userSettings);
    }

    private static RestApi createRestApi(String baseUrl, UserSettings userSettings) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Token token=" + userSettings.getAuthenticationToken())
                            .addHeader("Accept", "application/" + ACCEPT_APPLICATION +
                                    "; version=" + String.valueOf(API_VERSION))
                            .addHeader("Content-type", "application/json")
                            .build();
                    return chain.proceed(request);
                });
        if (BuildConfig.LOG_CONSOLE) {
            builder.addInterceptor(new HttpLoggingInterceptor(message -> Timber.v(message)).setLevel(
                    HttpLoggingInterceptor.Level.BODY));
        }
        OkHttpClient httpClient = builder
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(getConverter())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RestApi.class);
    }

    private static Converter.Factory getConverter() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setPrettyPrinting()
                .create();
        return GsonConverterFactory.create(gson);
    }
}

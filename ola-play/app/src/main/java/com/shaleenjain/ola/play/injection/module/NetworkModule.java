package com.shaleenjain.ola.play.injection.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shaleenjain.ola.play.data.LoggingInterceptor;
import com.shaleenjain.ola.play.data.model.MyGsonTypeAdapterFactory;
import com.shaleenjain.ola.play.data.remote.APIService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides network related dependencies
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new LoggingInterceptor())
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }

    @Provides
    @Singleton
    APIService provideTodoServiceService(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(APIService.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIService.class);
    }
}

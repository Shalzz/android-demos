package in.eightbitlabs.guidesdemo.injection.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryanharter.auto.value.gson.AutoValueGsonTypeAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import in.eightbitlabs.guidesdemo.data.LoggingInterceptor;
import in.eightbitlabs.guidesdemo.data.remote.GuidesService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
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
                .registerTypeAdapterFactory(new AutoValueGsonTypeAdapterFactory())
                .create();
    }

    @Provides
    @Singleton
    GuidesService provideGuidesService(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(GuidesService.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GuidesService.class);
    }
}
package com.example.shaleenjain.contactapp.utils;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TwilloService {

    String ENDPOINT = "https://api.twilio.com/";

    @FormUrlEncoded
    @POST("2010-04-01/Accounts/AC99c1ecb8a760a83a8edbc6f16ab71c8b/Messages")
    Call<ResponseBody> sendSMS(@Header("Authorization") String authorization,
            @Field("To") String to, @Field("From")  String from,
                               @Field("Body")  String text);

    /******** Helper class that sets up a new services *******/
    class Creator {

        static OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();

        public static TwilloService newTwilloService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TwilloService.ENDPOINT)
                    .client(client)
                    .build();
            return retrofit.create(TwilloService.class);
        }
    }
}
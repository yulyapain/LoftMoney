package com.azizova.loftmoney;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoftApp extends Application {

    private Api mApi;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG)
            loggingInterceptor.level(Level.BODY);
        else
            loggingInterceptor.level(Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://loftschool.com/android-api/basic/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mApi = retrofit.create(Api.class);

    }

    public Api getApi(){
        return mApi;
    }
}

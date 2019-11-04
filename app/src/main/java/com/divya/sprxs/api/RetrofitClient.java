package com.divya.sprxs.api;

import android.media.MediaDescription;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class    RetrofitClient {

    private static final String BASE_URL = "http://fifth9-prototype-server1604.westeurope.cloudapp.azure.com:39457";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60,TimeUnit.SECONDS)
                .connectTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();


        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
package com.antiblangsak.antiblangsak.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Syukri on 12/14/17.
 */

public class ApiClient {

    public static final String BASE_URL_DEV = "https://www.antiblangsak.com/api-dev/v1/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(request);
            }
        });

        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL_DEV)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL_DEV)
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}

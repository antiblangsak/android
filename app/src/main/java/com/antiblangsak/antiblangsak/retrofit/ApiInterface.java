package com.antiblangsak.antiblangsak.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Syukri on 12/14/17.
 */

public interface ApiInterface {

    @POST("login")
    @FormUrlEncoded
    Call<Object> login(@Field("email") String email, @Field("password") String body);
}

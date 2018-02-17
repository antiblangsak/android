package com.antiblangsak.antiblangsak.retrofit;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Syukri on 12/14/17.
 */

public interface ApiInterface {

    @POST("login")
    @FormUrlEncoded
    Call<Object> login(@Field("email") String email, @Field("password") String password);

    @POST("logout")
    @FormUrlEncoded
    Call<Object> logout(@Header("Authorization") String token, @Field("email") String email);

    @POST("register")
    @FormUrlEncoded
    Call<Object> register(@Field("name") String name,
                       @Field("email") String email,
                       @Field("password") String password,
                       @Field("password_confirmation") String password_confirmation);

    @GET("user/{userId}")
    Call<Object> profile(@Header("Authorization") String token, @Path("userId") int id);

    @GET("dkk/{familyId}/get_prepayment_info")
    Call<Object> prepayment(@Header("Authorization") String token, @Path("familyId") int id);

    @POST("payment")
    @FormUrlEncoded
    Call<Object> payment(@Header("Authorization") String token,
                         @Field("service_id") int service_id,
                         @Field("bank_account_id") int bank_account_id,
                         @Field("ref_user_id") int ref_user_id,
                         @Field("clients[]") ArrayList<Integer> clients,
                         @Field("payment_amount") int payment_amount);
}

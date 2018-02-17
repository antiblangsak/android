package com.antiblangsak.antiblangsak.retrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @POST("client")
    @FormUrlEncoded
    Call<Object> registerClient(@Header("Authorization") String token,
                                @Field("ref_user_id") int userId,
                                @Field("list_family_member_id[]") ArrayList<Integer> list_member,
                                @Field("service_id") int serviceId);

    @GET("family/{familyId}")
    Call<Object> getFamilyProfile(@Header("Authorization") String token, @Path("familyId") int familyId);

    @GET("claim/{claimId}")
    Call<Object> getClaimDetail(@Header("Authorization") String token, @Path("claimId") int id);

    @POST("claim")
    @FormUrlEncoded
    Call<Object> postClaim(@Header("Authorization") String token,
                           @Field("service_id") int serviceId,
                           @Field("ref_user_id") int userId,
                           @Field("client_id") int clientId);

    @GET("payment/{payId}")
    Call<Object> getPaymentDetail(@Header("Authorization") String token, @Path("payId") int id);

    @PUT("payment/{payId}")
    @FormUrlEncoded
    Call<Object> confirmPayment(@Header("Authorization") String token, @Path("payId") int id,
                                @Field("status") int status);

    @GET("dkk/{familyId}/family_members")
    Call<Object> getAllDKKFamilyMembers(@Header("Authorization") String token, @Path("familyId") int familyId);

    @GET("dkk/{familyId}/history")
    Call<Object> getDKKHistory(@Header("Authorization") String token, @Path("familyId") int familyId);

    @GET("dpgk/{familyId}/family_members")
    Call<Object> getAllDPGKFamilyMembers(@Header("Authorization") String token, @Path("familyId") int familyId);

    @GET("dpgk/{familyId}/history")
    Call<Object> getDPGKHistory(@Header("Authorization") String token, @Path("familyId") int familyId);

    @GET("dpgk/{familyId}/get_claim_info")
    Call<Object> getDPGKClaimInfo(@Header("Authorization") String token, @Path("familyId") int familyId);

    @GET("dwk/{familyId}/family_members")
    Call<Object> getAllDWKFamilyMembers(@Header("Authorization") String token, @Path("familyId") int familyId);

    @GET("dwk/{familyId}/history")
    Call<Object> getDWKHistory(@Header("Authorization") String token, @Path("familyId") int familyId);

    @GET("dwk/{familyId}/get_claim_info")
    Call<Object> getDWKClaimInfo(@Header("Authorization") String token, @Path("familyId") int familyId);

    @GET("dpgk/{familyId}/get_prepayment_info")
    Call<Object> getDPGKPaymentInfo(@Header("Authorization") String token, @Path("familyId") int id);

    @GET("dkk/{familyId}/get_prepayment_info")
    Call<Object> getDKKPaymentInfo(@Header("Authorization") String token, @Path("familyId") int id);

    @GET("dwk/{familyId}/get_prepayment_info")
    Call<Object> getDWKPaymentInfo(@Header("Authorization") String token, @Path("familyId") int id);

    @POST("payment")
    @FormUrlEncoded
    Call<Object> payment(@Header("Authorization") String token,
                         @Field("service_id") int service_id,
                         @Field("bank_account_id") int bank_account_id,
                         @Field("ref_user_id") int ref_user_id,
                         @Field("clients[]") ArrayList<Integer> clients,
                         @Field("payment_amount") int payment_amount);
}

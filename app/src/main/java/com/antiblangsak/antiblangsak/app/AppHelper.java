package com.antiblangsak.antiblangsak.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.antiblangsak.antiblangsak.common.LoginActivity;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Syukri on 1/1/18.
 */

public class AppHelper {

    public static void showImageWithGlide(Activity activity, Bitmap bitmap, PhotoView imageView) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Glide.with(activity)
                .load(stream.toByteArray())
                .asBitmap()
                .into(imageView);
    }

    public static String formatRupiah(int harga){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(harga);
    }

    public static void performLogout(Response response, SharedPrefManager sharedPrefManager, Activity activity, ApiInterface apiInterface) {
        try {
            Log.w("body", response.errorBody().string());
            sharedPrefManager.logout();
            activity.startActivity(new Intent(activity, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

            Call callLogout = apiInterface.logout(sharedPrefManager.getToken(), sharedPrefManager.getEmail());
            callLogout.enqueue(AppConstant.EMPTY_CALLBACK);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvMemberSince;
    private TextView tvEmail;
    private TextView tvPhone;

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvMemberSince =  findViewById(R.id.tvMemberSince);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);

        final int id = sharedPrefManager.getId();
        final String token = sharedPrefManager.getToken();
        final String emailUser = sharedPrefManager.getEmail();

        Log.w("GET PROFIlE", "ID " + id + " called");
        Log.w("GET PROFILE", "token " + token + " called");

        Call call = apiInterface.profile(token, id);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {

                JSONObject body = null;
                int statusCode = response.code();
                Log.w("status", "status: " + statusCode);

                if (statusCode == 200) {
                    try {
                        body = new JSONObject(new Gson().toJson(response.body()));
                        Log.w("RESPONSE", "body: " + body.toString());

                        int id = body.getJSONObject("data").getInt("id");
                        String name = body.getJSONObject("data").getString("name");
                        String memberSince = body.getJSONObject("data").getString("member_since");
                        String email = body.getJSONObject("data").getString("email");
                        String phone = body.getJSONObject("data").getString("phone_number");

                        tvName.setText(name);
                        tvMemberSince.setText(memberSince);
                        tvEmail.setText(email);
                        tvPhone.setText(phone);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        Log.w("body", response.errorBody().string());
                        sharedPrefManager.saveBoolean(SharedPrefManager.STATUS_LOGIN, false);
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();

                        Call callLogout = apiInterface.logout(token, emailUser);
                        callLogout.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {

                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

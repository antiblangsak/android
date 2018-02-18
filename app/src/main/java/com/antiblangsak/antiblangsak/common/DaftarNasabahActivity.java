package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.app.AppHelper;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DaftarNasabahActivity extends AppCompatActivity {

    private RelativeLayout btnKeluargaBaru;
    private RelativeLayout btnKeluargaTerdaftar;

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private LinearLayout mainLayout;
    private ProgressBar progressBar;

    private int userId;
    private String token;
    private String emailUser;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_nasabah);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        mainLayout = findViewById(R.id.mainLayout);
        progressBar = findViewById(R.id.progressBar);

        userId = sharedPrefManager.getId();
        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();

        btnKeluargaBaru = (RelativeLayout) findViewById(R.id.btnDaftarAsNasabahKeluargaBaru);
        btnKeluargaTerdaftar = (RelativeLayout) findViewById(R.id.btnDaftarAsNasabahKeluargaTerdaftar);

        btnKeluargaBaru.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                Call call = apiInterface.getUsersBankAccount(token, userId);
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

                                JSONArray data = body.getJSONArray("data");
                                if (data.length() == 0) {
                                    startActivity(new Intent(DaftarNasabahActivity.this, DaftarNasabahKeluargaBaruActivity.class));
                                } else {
                                    startActivity(new Intent(DaftarNasabahActivity.this, DaftarNasabahUploadFotoActivity.class));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else if (statusCode == AppConstant.HTTP_RESPONSE_401_UNAUTHORIZED) {
                            Toast.makeText(getApplicationContext(), AppConstant.SESSION_EXPIRED_STRING, Toast.LENGTH_SHORT).show();
                            AppHelper.performLogout(response, sharedPrefManager, DaftarNasabahActivity.this, apiInterface);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), AppConstant.API_CALL_UNKNOWN_ERROR_STRING + statusCode, Toast.LENGTH_SHORT).show();
                            mainLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_SHORT).show();
                        mainLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        btnKeluargaTerdaftar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DaftarNasabahActivity.this,
                        DaftarNasabahKeluargaTerdaftarActivity.class);
                startActivity(myIntent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        mainLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
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

package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
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

public class DaftarNasabahKeluargaTerdaftarActivity extends AppCompatActivity {

    private Button btnDaftarkan;
    private EditText etKeluargaId;
    private EditText etNik;

    private String keluargaId;
    private String nik;

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private String token;
    private String emailUser;
    private int userId;

    private ProgressBar pbDaftarkan;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_nasabah_keluarga_terdaftar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etKeluargaId = findViewById(R.id.etKeluargaId);
        etNik = findViewById(R.id.etNik);
        pbDaftarkan = findViewById(R.id.pbDaftarkan);

        sharedPrefManager = new SharedPrefManager(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();
        userId = sharedPrefManager.getId();

        btnDaftarkan = (Button) findViewById(R.id.btnDaftarkan);
        btnDaftarkan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    pbDaftarkan.setVisibility(View.VISIBLE);
                    btnDaftarkan.setVisibility(View.GONE);
                    Call call = apiInterface.connectFamily(token, userId, nik, Integer.parseInt(keluargaId));
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            JSONObject body = null;
                            int statusCode = response.code();
                            Log.w("status", "status: " + statusCode);

                            if(statusCode == 201){
                               try {
                                   Log.w("body", response.body().toString());
                                   body = new JSONObject(new Gson().toJson(response.body()));
                                   Log.w("body", body.toString());
                                   JSONObject data = body.getJSONObject("data");
                                   String sukses = data.getString("success");
                                   int familyStatus = data.getInt("status");

                                   sharedPrefManager.saveBoolean(SharedPrefManager.HAS_FAMILY, true);
                                   sharedPrefManager.saveInt(SharedPrefManager.FAMILY_ID, Integer.parseInt(keluargaId));
                                   sharedPrefManager.saveInt(SharedPrefManager.FAMILY_STATUS, familyStatus);

                                   Toast.makeText(getApplicationContext(), sukses, Toast.LENGTH_SHORT).show();
                                   Intent myIntent = new Intent(DaftarNasabahKeluargaTerdaftarActivity.this,
                                           MainActivity.class)
                                           .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                   startActivity(myIntent);

                               }
                               catch (JSONException e) {
                                   e.printStackTrace();
                               }

                            } else if(statusCode == 404) {
                                try {
                                    Log.w("body", response.errorBody().string());
                                    Toast.makeText(getApplicationContext(), "ID keluarga atau NIK anda belum terdaftar" , Toast.LENGTH_SHORT).show();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                try {
                                    body = new JSONObject(new Gson().toJson(response.body()));
                                    Log.w("body", body.toString());
                                    sharedPrefManager.logout();
                                    Toast.makeText(getApplicationContext(), AppConstant.SESSION_EXPIRED_STRING, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DaftarNasabahKeluargaTerdaftarActivity.this, LoginActivity.class)
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pbDaftarkan.setVisibility(View.GONE);
                            btnDaftarkan.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            pbDaftarkan.setVisibility(View.GONE);
                            btnDaftarkan.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), AppConstant.GENERAL_MISSING_FIELD_ERROR_MESSAGE,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                etKeluargaId.clearFocus();
                etNik.clearFocus();
                return true;
            }
        });
    }

    public boolean validateInput() {
        keluargaId = etKeluargaId.getText().toString();
        nik = etNik.getText().toString();

        boolean isValid = true;

        if(keluargaId.isEmpty()){
            etKeluargaId.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etKeluargaId.setError(null);
        }

        if(nik.isEmpty()){
            etNik.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etNik.setError(null);
        }

        return isValid;
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

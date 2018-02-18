package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.R;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirmation;
    private Button btnRegister;
    private TextView tvToLoginPage;
    private TextView tvSudahPunyaAkun;
    private ProgressBar pbRegister;

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        getSupportActionBar().hide();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        etName = (EditText) findViewById(R.id.etName);

        etEmail = (EditText) findViewById(R.id.etEmail);
        Typeface typeFace = etEmail.getTypeface();

        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setTypeface(typeFace);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        etPasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
        etPasswordConfirmation.setTypeface(typeFace);
        etPasswordConfirmation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setText(Html.fromHtml("<b>DAFTAR</b>"));

        tvToLoginPage = (TextView) findViewById(R.id.tvToLoginPage);
        tvSudahPunyaAkun = (TextView) findViewById(R.id.tvSudahPunyaAkun);
        pbRegister = (ProgressBar) findViewById(R.id.pbRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.setVisibility(View.GONE);
                tvToLoginPage.setVisibility(View.GONE);
                tvSudahPunyaAkun.setVisibility(View.GONE);
                pbRegister.setVisibility(View.VISIBLE);

                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String password_confirmation = etPasswordConfirmation.getText().toString();

                if (!validateInput(name, email, password, password_confirmation)) {
                    pbRegister.setVisibility(View.GONE);
                    btnRegister.setVisibility(View.VISIBLE);
                    tvToLoginPage.setVisibility(View.VISIBLE);
                    tvSudahPunyaAkun.setVisibility(View.VISIBLE);
                    return;
                }

                Call call = apiInterface.register(name, email, password, password_confirmation);
                call.enqueue(new Callback() {

                    @Override
                    public void onResponse(Call call, Response response) {
                        pbRegister.setVisibility(View.GONE);

                        JSONObject body = null;
                        int statusCode = response.code();
                        Log.w("status", "status: " + statusCode);

                        if (statusCode == 201) {
                            Toast.makeText(getApplicationContext(), "Register berhasil!", Toast.LENGTH_SHORT).show();
                            try {
                                body = new JSONObject(new Gson().toJson(response.body()));
                                Log.w("RESPONSE", "body: " + body.toString());

                                int id = body.getJSONObject("data").getInt("id");
                                String email = body.getJSONObject("data").getString("email");
                                String token = body.getJSONObject("data").getString("api_token");

                                sharedPrefManager.saveInt(SharedPrefManager.ID, id);
                                sharedPrefManager.saveString(SharedPrefManager.EMAIL, email);
                                sharedPrefManager.saveString(SharedPrefManager.TOKEN, token);
                                sharedPrefManager.saveBoolean(SharedPrefManager.STATUS_LOGIN, true);

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            etEmail.requestFocus();
                            etEmail.setError("Email sudah terdaftar!");

                            btnRegister.setVisibility(View.VISIBLE);
                            tvToLoginPage.setVisibility(View.VISIBLE);
                            tvSudahPunyaAkun.setVisibility(View.VISIBLE);

                            try {
                                Log.w("body", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call call, Throwable t) {
                        pbRegister.setVisibility(View.GONE);
                        btnRegister.setVisibility(View.VISIBLE);
                        tvToLoginPage.setVisibility(View.VISIBLE);
                        tvSudahPunyaAkun.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        tvToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                etName.clearFocus();
                etEmail.clearFocus();
                etPassword.clearFocus();
                etPasswordConfirmation.clearFocus();
                return true;
            }
        });
    }


    private boolean validateInput(String name, String email, String password,
                                  String password_confirmation) {
        if (name.isEmpty()) {
            etName.requestFocus();
            etName.setError("Nama tidak valid!");
            return false;
        }

        if (email.isEmpty()) {
            etEmail.requestFocus();
            etEmail.setError("Email tidak valid!");
            return false;
        }

        if (password.isEmpty() || password.length() < 8) {
            etPassword.requestFocus();
            etPassword.setError("Password harus lebih dari 8 karakter!");
            return false;
        }

        if (password_confirmation.isEmpty()) {
            etPasswordConfirmation.requestFocus();
            etPasswordConfirmation.setError("Password tidak valid!");
            return false;
        }

        if (!password.equals(password_confirmation)) {
            etPasswordConfirmation.requestFocus();
            etPasswordConfirmation.setError("Konfirmasi password tidak sesuai!");
            return false;
        }

        etName.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);
        etPasswordConfirmation.setError(null);
        return true;
    }

}


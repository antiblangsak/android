package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvToRegisterPage;
    private TextView tvBelumPunyaAkun;
    private ProgressBar pbLogin;

    private SharedPrefManager sharedPrefManager;
    private ApiInterface apiInterface;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefManager = new SharedPrefManager(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        etEmail = (EditText) findViewById(R.id.etEmail);
        Typeface typeFace = etEmail.getTypeface();

        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setTypeface(typeFace);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setText(Html.fromHtml("<b>MASUK</b>"));

        tvToRegisterPage = (TextView) findViewById(R.id.tvToRegisterPage);
        tvBelumPunyaAkun = (TextView) findViewById(R.id.tvBelumPunyaAkun);
        pbLogin = (ProgressBar) findViewById(R.id.pbLogin);

        if (sharedPrefManager.getStatusLogin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                btnLogin.setVisibility(View.GONE);
                tvToRegisterPage.setVisibility(View.GONE);
                tvBelumPunyaAkun.setVisibility(View.GONE);
                pbLogin.setVisibility(View.VISIBLE);

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                Log.w("EMAIL", "EMAIL: " + email);
                Log.w("PASSWORD","password: " + password);

                if (!validateInput(email, password)) {
                    btnLogin.setVisibility(View.VISIBLE);
                    tvToRegisterPage.setVisibility(View.VISIBLE);
                    tvBelumPunyaAkun.setVisibility(View.VISIBLE);
                    pbLogin.setVisibility(View.GONE);
                    return;
                }

                Call call = apiInterface.login(email, password);
                call.enqueue(new Callback() {

                    @Override
                    public void onResponse(Call call, Response response) {
                        pbLogin.setVisibility(View.GONE);

                        JSONObject body = null;
                        int statusCode = response.code();
                        Log.w("RESPONSE", "status: " + statusCode);

                        if (statusCode == 200) {
                            try {
                                body = new JSONObject(new Gson().toJson(response.body()));
                                Log.w("RESPONSE", "body: " + body.toString());

                                int id = body.getJSONObject("data").getInt("id");
                                String email = body.getJSONObject("data").getString("email");
                                String token = body.getJSONObject("data").getString("api_token");

                                sharedPrefManager.saveInt(SharedPrefManager.ID, id);
                                sharedPrefManager.saveString(SharedPrefManager.EMAIL, email);
                                sharedPrefManager.saveString(SharedPrefManager.TOKEN, "Bearer " + token);
                                sharedPrefManager.saveBoolean(SharedPrefManager.STATUS_LOGIN, true);

                                startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            btnLogin.setVisibility(View.VISIBLE);
                            tvToRegisterPage.setVisibility(View.VISIBLE);
                            tvBelumPunyaAkun.setVisibility(View.VISIBLE);

                            etEmail.requestFocus();
                            etEmail.setError("Email atau password tidak terdaftar");
                            try {
                                Log.w("ERROR", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        pbLogin.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                        tvToRegisterPage.setVisibility(View.VISIBLE);
                        tvBelumPunyaAkun.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        tvToRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);

            }
        });

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                etEmail.clearFocus();
                etPassword.clearFocus();
                return true;
            }
        });
    }

    private boolean validateInput(String email, String password) {
        boolean isValid = true;

        if(email.isEmpty()){
            etEmail.setError("Email tidak valid!");
            isValid = false;
        }

        if(password.isEmpty() || password.length() < 8){
            etPassword.setError("Password harus lebih dari 8 karakter!");
            isValid = false;
        }

        if (isValid) {
            etEmail.setError(null);
            etPassword.setError(null);
        }
        return isValid;
    }
}

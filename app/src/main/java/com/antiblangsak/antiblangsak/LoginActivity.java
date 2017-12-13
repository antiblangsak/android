package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

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
    private Button loginButton;
    private ApiInterface apiInterface;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        etEmail = (EditText) findViewById(R.id.etEmail);
        Typeface typeFace = etEmail.getTypeface();

        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setTypeface(typeFace);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());

        loginButton = (Button) findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                Log.w("email", "email: " + email);
                Log.w("password","password: " + password);

                if (!validateInput(email, password)) {
                    return;
                }

                Call call = apiInterface.login(email, password);
                call.enqueue(new Callback() {

                    @Override
                    public void onResponse(Call call, Response response) {
                        JSONObject body = null;
                        int statusCode = response.code();
                        Log.w("status", "status: " + statusCode);

                        if (statusCode == 200) {
                            Toast.makeText(getApplicationContext(), "Login berhasil!", Toast.LENGTH_LONG).show();
                            try {
                                body = new JSONObject(new Gson().toJson(response.body()));
                                Log.w("body:", body.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Login gagal!", Toast.LENGTH_LONG).show();
                            try {
                                Log.w("body", response.errorBody().string());
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

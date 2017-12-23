package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirmation;
    private Button registerButton;
    private TextView toLoginPageTextView;
    private ApiInterface apiInterface;

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

        etName = (EditText) findViewById(R.id.etName);

        etEmail = (EditText) findViewById(R.id.etEmail);
        Typeface typeFace = etEmail.getTypeface();

        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setTypeface(typeFace);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());

        etPasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
        etPasswordConfirmation.setTypeface(typeFace);
        etPasswordConfirmation.setTransformationMethod(new PasswordTransformationMethod());

        registerButton = (Button) findViewById(R.id.btnRegister);
        toLoginPageTextView = (TextView) findViewById(R.id.tvToLoginPage);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String password_confirmation = etPasswordConfirmation.getText().toString();

                Log.w("name", "name: " + name);
                Log.w("email", "email: " + email);
                Log.w("password", "password: " + password);
                Log.w("password_confirmation", "password_confirmation: " + password_confirmation);


                if (!validateInput(name, email, password, password_confirmation)) {
                    return;
                }

                Call call = apiInterface.register(name, email, password, password_confirmation);
                call.enqueue(new Callback() {

                    @Override
                    public void onResponse(Call call, Response response) {
                        JSONObject body = null;
                        int statusCode = response.code();
                        Log.w("status", "status: " + statusCode);

                        if (statusCode == 201) {
                            Toast.makeText(getApplicationContext(), "Register berhasil!", Toast.LENGTH_LONG).show();
                            try {
                                body = new JSONObject(new Gson().toJson(response.body()));
                                Log.w("body:", body.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Register gagal!", Toast.LENGTH_LONG).show();
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

        toLoginPageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);

            }
        });
    }


    private boolean validateInput(String name, String email, String password,
                                  String password_confirmation) {
        boolean isValid = true;

        if (name.isEmpty()) {
            etName.setError("Nama tidak valid!");
            isValid = false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email tidak valid!");
            isValid = false;
        }

        if (password.isEmpty() || password.length() < 8) {
            etPassword.setError("Password harus lebih dari 8 karakter!");
            isValid = false;
        }

        if (password_confirmation.isEmpty()) {
            etPasswordConfirmation.setError("Password tidak sesuai!");
            isValid = false;
        }

        if (isValid) {
            etName.setError(null);
            etEmail.setError(null);
            etPassword.setError(null);
            etPasswordConfirmation.setError(null);
        }
        return isValid;
    }

}


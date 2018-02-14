package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvMemberSince;
    private TextView tvEmail;

    private ListView bankAccList;
    private TextView tvEndRekening;
    private ArrayAdapter<String> arrayAdapter;

    private ProgressBar progressBar;
    private LinearLayout upperLayout;
    private RelativeLayout lowerLayout;

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private int id;
    private String token;
    private String email;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.gray_dark)));

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvMemberSince =  findViewById(R.id.tvMemberSince);
        bankAccList = findViewById(R.id.listRekening);
        tvEndRekening = findViewById(R.id.tvEndRekening);

        progressBar = findViewById(R.id.progressBar);
        upperLayout = findViewById(R.id.relativeLayout);
        lowerLayout = findViewById(R.id.relativeLayout2);

        id = sharedPrefManager.getId();
        token = sharedPrefManager.getToken();
        email = sharedPrefManager.getEmail();

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

                        JSONArray bankAccounts = body.getJSONObject("data").getJSONArray("bank_accounts");
                        String[] bankAccountsStr = new String[bankAccounts.length()];

                        for (int i = 0; i < bankAccounts.length(); i++) {
                            JSONObject bankAccount = bankAccounts.getJSONObject(i);
                            bankAccountsStr[i] = bankAccount.getString("name");
                        }

                        tvName.setText(name);
                        tvMemberSince.setText(memberSince);
                        tvEmail.setText(email);

                        arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this, R.layout.listitem_rekening, bankAccountsStr);
                        bankAccList.setAdapter(arrayAdapter);

                        lowerLayout.setVisibility(View.VISIBLE);
                        upperLayout.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        Log.w("body", response.errorBody().string());
                        sharedPrefManager.logout();
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        ProfileActivity.this.finish();

                        Call callLogout = apiInterface.logout(token, email);
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
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_LONG).show();
                finish();
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
